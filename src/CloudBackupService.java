import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CloudBackupService
{
    // Verejný WebDAV endpoint Nextcloudu.
    // Za túto časť URL sa doplní kód školy, ktorý v tomto projekte slúži ako token verejného zdieľania.
    private static final String NEXTCLOUD_PUBLIC_WEBDAV_PREFIX =
            "https://cloud.milpet.eu/public.php/dav/files/";

    // XML telo požiadavky PROPFIND.
    // PROPFIND je WebDAV metóda, pomocou ktorej sa získava zoznam súborov a priečinkov.
    // Tu sa pýta iba na resourcetype, aby bolo možné rozlíšiť súbory od priečinkov.
    private static final String PROPFIND_BODY =
            "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" +
                    "<d:propfind xmlns:d=\"DAV:\">" +
                    "<d:prop>" +
                    "<d:resourcetype/>" +
                    "</d:prop>" +
                    "</d:propfind>";

    // HTTP klient používaný na komunikáciu s Nextcloud WebDAV rozhraním.
    // Redirecty sú povolené, pretože server môže niekedy presmerovať požiadavku na finálnu URL.
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public CloudBackupResult uploadAppFolder() throws Exception
    {
        // Z konfigurácie sa načíta kód školy.
        // V tomto riešení sa používa ako token verejného Nextcloud zdieľania.
        String schoolID = getSchoolID();

        // Z kódu školy sa vytvorí vzdialená WebDAV adresa.
        URI remoteDirectoryUri = buildRemoteDirectoryUri(schoolID);

        // Lokálny dátový priečinok aplikácie.
        Path appDirectory = AppPath.appDataPath();

        // Pre istotu sa vytvorí, ak by ešte neexistoval.
        Files.createDirectories(appDirectory);

        int uploadedFiles = 0;

        // Prejdenie všetkých položiek v dátovom priečinku aplikácie.
        try(DirectoryStream<Path> files = Files.newDirectoryStream(appDirectory))
        {
            for(Path file : files)
            {
                // Nahrávajú sa iba bežné súbory, nie priečinky.
                // Zároveň sa ignorujú dočasné súbory.
                if(Files.isRegularFile(file) && shouldUploadFile(file))
                {
                    uploadFile(remoteDirectoryUri, file);
                    uploadedFiles++;
                }
            }
        }

        // Výsledok obsahuje počet nahratých súborov a použitý kód školy.
        return new CloudBackupResult(uploadedFiles, schoolID);
    }

    public CloudBackupResult downloadAppFolder() throws Exception
    {
        // Z konfigurácie sa načíta kód školy, teda token verejného zdieľania.
        String schoolID = getSchoolID();

        // Vytvorenie vzdialenej WebDAV adresy.
        URI remoteDirectoryUri = buildRemoteDirectoryUri(schoolID);

        // Získanie zoznamu súborov dostupných na cloude.
        List<String> remoteFiles = listRemoteFiles(remoteDirectoryUri);

        // Lokálny dátový priečinok aplikácie.
        Path appDirectory = AppPath.appDataPath();

        // Ak neexistuje, vytvorí sa.
        Files.createDirectories(appDirectory);

        int downloadedFiles = 0;

        // Postupné stiahnutie všetkých bezpečných názvov súborov.
        for(String remoteFileName : remoteFiles)
        {
            if(isSafeFileName(remoteFileName))
            {
                downloadFile(remoteDirectoryUri, remoteFileName, appDirectory);
                downloadedFiles++;
            }
        }

        // Výsledok obsahuje počet stiahnutých súborov a použitý kód školy.
        return new CloudBackupResult(downloadedFiles, schoolID);
    }

    private String getSchoolID()
    {
        // Konfigurácia obsahuje schoolID.
        // V tejto aplikácii schoolID zároveň predstavuje token verejného Nextcloud zdieľania.
        AppConfig config = new AppConfig();

        String schoolID = config.getSchoolID();

        // Bez kódu školy sa cloudová záloha nedá vykonať.
        if(schoolID == null || schoolID.isBlank() || schoolID.equals("none"))
        {
            throw new IllegalStateException("Nie je nastavený kód školy.");
        }

        // Token sa očistí, aby sa nedal zneužiť ako nebezpečná časť URL.
        return sanitizeShareToken(schoolID);
    }

    private String sanitizeShareToken(String value)
    {
        // Odstránenie nebezpečných alebo nevhodných znakov zo share tokenu.
        String sanitized = value.trim();

        // Lomky sa odstránia, aby token nemohol meniť cestu vo WebDAV URL.
        sanitized = sanitized.replace("\\", "");
        sanitized = sanitized.replace("/", "");

        // Odstránenie pokusov o zápis rodičovskej cesty.
        sanitized = sanitized.replace("..", "");

        // Povolené sú iba bežné znaky vhodné do tokenu.
        sanitized = sanitized.replaceAll("[^a-zA-Z0-9._-]", "");

        if(sanitized.isBlank())
        {
            throw new IllegalStateException("Kód školy je neplatný.");
        }

        return sanitized;
    }

    private URI buildRemoteDirectoryUri(String schoolID)
    {
        // Zloženie finálnej verejnej WebDAV adresy.
        // Výsledok má tvar:
        // https://cloud.milpet.eu/public.php/dav/files/<schoolID>/
        String baseUrl = NEXTCLOUD_PUBLIC_WEBDAV_PREFIX + encodePathSegment(schoolID) + "/";
        return URI.create(baseUrl);
    }

    private boolean shouldUploadFile(Path file)
    {
        // Do cloudu sa nemajú nahrávať dočasné pracovné súbory.
        String fileName = file.getFileName().toString();

        // Súbor s príponou .download vzniká pri sťahovaní ako dočasný súbor.
        if(fileName.endsWith(".download"))
        {
            return false;
        }

        // Dočasné .tmp súbory sa tiež vynechajú.
        if(fileName.endsWith(".tmp"))
        {
            return false;
        }

        return true;
    }

    private void uploadFile(URI remoteDirectoryUri, Path localFile) throws Exception
    {
        // Názov lokálneho súboru sa použije ako názov súboru na cloude.
        String fileName = localFile.getFileName().toString();

        // Vytvorenie WebDAV URL konkrétneho súboru.
        URI remoteFileUri = remoteDirectoryUri.resolve(encodePathSegment(fileName));

        // PUT požiadavka nahrá alebo prepíše súbor na WebDAV serveri.
        HttpRequest request = requestBuilder(remoteFileUri)
                .header("Content-Type", "application/octet-stream")
                .PUT(HttpRequest.BodyPublishers.ofFile(localFile))
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        int status = response.statusCode();

        // Úspešné WebDAV/HTTP odpovede pri nahrávaní súboru.
        // 200 = prepísané alebo úspešne spracované,
        // 201 = vytvorené,
        // 204 = úspešne spracované bez tela odpovede.
        if(status == 200 || status == 201 || status == 204)
        {
            return;
        }

        // Pri inom stave sa vyhodí chyba aj s odpoveďou servera.
        throw new IOException("Nepodarilo sa nahrať súbor: " + fileName + "\n" + "HTTP " + status + "\n" + response.body());
    }

    private List<String> listRemoteFiles(URI remoteDirectoryUri) throws Exception
    {
        // PROPFIND s Depth: 1 načíta položky priamo v danom priečinku.
        // Nelezie hlbšie do podpriečinkov.
        HttpRequest request = requestBuilder(remoteDirectoryUri)
                .header("Depth", "1")
                .header("Content-Type", "application/xml; charset=utf-8")
                .method("PROPFIND", HttpRequest.BodyPublishers.ofString(PROPFIND_BODY))
                .build();

        HttpResponse<byte[]> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofByteArray()
        );

        int status = response.statusCode();

        // WebDAV pri úspešnom PROPFIND typicky vracia 207 Multi-Status.
        // Pre istotu sa akceptujú aj všeobecné úspešné 2xx odpovede.
        if(status != 207 && (status < 200 || status >= 300))
        {
            throw new IOException("Nepodarilo sa načítať zoznam súborov z cloudu.\n" + "HTTP " + status);
        }

        // XML odpoveď servera sa spracuje na zoznam názvov súborov.
        return parseRemoteFileNames(response.body());
    }

    private List<String> parseRemoteFileNames(byte[] xmlBytes) throws Exception
    {
        // Výsledný zoznam názvov vzdialených súborov.
        List<String> fileNames = new ArrayList<>();

        // Parser XML odpovede z WebDAV PROPFIND.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        // Bezpečnostné nastavenia XML parsera.
        // Zakazujú DTD a externé entity, aby sa predišlo problémom typu XXE.
        setXmlFeature(factory, "http://apache.org/xml/features/disallow-doctype-decl", true);
        setXmlFeature(factory, "http://xml.org/sax/features/external-general-entities", false);
        setXmlFeature(factory, "http://xml.org/sax/features/external-parameter-entities", false);

        Document document = factory.newDocumentBuilder().parse(
                new ByteArrayInputStream(xmlBytes)
        );

        // WebDAV odpoveď obsahuje viacero elementov response.
        // Najprv sa hľadá namespace-aware spôsobom.
        NodeList responses = document.getElementsByTagNameNS("*", "response");

        // Záloha pre servery alebo odpovede, kde namespace nemusí byť spracovaný očakávane.
        if(responses.getLength() == 0)
        {
            responses = document.getElementsByTagName("response");
        }

        for(int i = 0; i < responses.getLength(); i++)
        {
            Element responseElement = (Element)responses.item(i);

            // href obsahuje cestu k súboru alebo priečinku.
            String href = getFirstText(responseElement, "href");

            if(href == null || href.isBlank())
            {
                continue;
            }

            // Ak response označuje priečinok, preskočí sa.
            boolean isDirectory = hasElement(responseElement, "collection");

            if(isDirectory)
            {
                continue;
            }

            // Z href sa vytiahne samotný názov súboru.
            String fileName = extractFileNameFromHref(href);

            // Do výsledku sa pridajú iba bezpečné názvy súborov.
            if(fileName != null && isSafeFileName(fileName))
            {
                fileNames.add(fileName);
            }
        }

        return fileNames;
    }

    private void downloadFile(
            URI remoteDirectoryUri,
            String remoteFileName,
            Path appDirectory
    ) throws Exception
    {
        // Vytvorenie vzdialenej URL konkrétneho súboru.
        URI remoteFileUri = remoteDirectoryUri.resolve(encodePathSegment(remoteFileName));

        // GET požiadavka stiahne obsah vzdialeného súboru.
        HttpRequest request = requestBuilder(remoteFileUri).GET().build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        int status = response.statusCode();

        // Pri sťahovaní sa očakáva klasická odpoveď 200 OK.
        if(status != 200)
        {
            throw new IOException("Nepodarilo sa stiahnuť súbor: " + remoteFileName + "\n" + "HTTP " + status);
        }

        // Cieľová lokálna cesta súboru.
        Path targetFile = appDirectory.resolve(remoteFileName).normalize();

        // Ochrana pred path traversal útokom.
        // Súbor sa môže uložiť iba priamo do dátového priečinka aplikácie.
        if(!targetFile.getParent().equals(appDirectory.normalize()))
        {
            throw new IOException("Neplatný názov vzdialeného súboru: " + remoteFileName);
        }

        // Najprv sa zapisuje do dočasného súboru.
        // Tým sa zníži riziko poškodenia pôvodného súboru pri prerušení sťahovania.
        Path tempFile = targetFile.resolveSibling(targetFile.getFileName() + ".download");

        Files.write(tempFile, response.body());

        // Po úspešnom stiahnutí sa dočasný súbor presunie na finálnu cestu.
        moveReplacing(tempFile, targetFile);
    }

    private void moveReplacing(Path tempFile, Path targetFile) throws IOException
    {
        try
        {
            // Najprv sa skúsi atomický presun.
            // Ak ho súborový systém podporuje, cieľový súbor sa vymení naraz.
            Files.move(tempFile, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        }
        catch(AtomicMoveNotSupportedException e)
        {
            // Nie každý súborový systém podporuje atomický presun.
            // V takom prípade sa použije obyčajné prepísanie.
            Files.move(tempFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private HttpRequest.Builder requestBuilder(URI uri)
    {
        // Spoločný základ pre všetky HTTP požiadavky.
        // Timeout zabraňuje tomu, aby aplikácia čakala donekonečna.
        // User-Agent pomáha serveru identifikovať klienta.
        return HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(30)).header("User-Agent", Main.APP_NAME + "-cloud-backup");
    }

    private String encodePathSegment(String text)
    {
        // Zakódovanie jednej časti URL cesty.
        // URLEncoder používa + pre medzeru, preto sa nahradí za %20, čo je vhodnejšie pre URL cestu.
        return URLEncoder.encode(text, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private String extractFileNameFromHref(String href)
    {
        try
        {
            // WebDAV href môže byť absolútna alebo relatívna cesta.
            // URI parser pomôže získať samotnú cestu.
            String path = URI.create(href).getPath();

            if(path == null || path.isBlank())
            {
                path = href;
            }

            // Ak href končí lomkou, ide typicky o priečinok.
            // Pre istotu sa koncová lomka odstráni pred extrakciou názvu.
            if(path.endsWith("/"))
            {
                path = path.substring(0, path.length() - 1);
            }

            int lastSlashIndex = path.lastIndexOf('/');

            // Názov súboru je posledná časť cesty za lomkou.
            String encodedName = lastSlashIndex >= 0  ? path.substring(lastSlashIndex + 1) : path;

            // Dekódovanie URL zápisu späť na bežný názov súboru.
            return URLDecoder.decode(encodedName, StandardCharsets.UTF_8);
        }
        catch(Exception e)
        {
            // Ak sa názov nepodarí spoľahlivo získať, položka sa neskôr ignoruje.
            return null;
        }
    }

    private boolean isSafeFileName(String fileName)
    {
        // Kontrola, že názov súboru je jednoduchý názov bez cesty.
        // Tým sa bráni prepísaniu súborov mimo dátového priečinka aplikácie.
        return fileName != null &&
                !fileName.isBlank() &&
                !fileName.contains("/") &&
                !fileName.contains("\\") &&
                !fileName.equals(".") &&
                !fileName.equals("..");
    }

    private String getFirstText(Element root, String localName)
    {
        // Najprv sa hľadá element podľa lokálneho mena bez ohľadu na XML namespace.
        NodeList nodes = root.getElementsByTagNameNS("*", localName);

        if(nodes.getLength() > 0)
        {
            return nodes.item(0).getTextContent();
        }

        // Záložné hľadanie bez namespace.
        nodes = root.getElementsByTagName(localName);

        if(nodes.getLength() > 0)
        {
            return nodes.item(0).getTextContent();
        }

        // Posledná záloha: prehľadanie všetkých elementov a kontrola názvu vrátane prefixu.
        NodeList allElements = root.getElementsByTagName("*");

        for(int i = 0; i < allElements.getLength(); i++)
        {
            String nodeName = allElements.item(i).getNodeName();

            if(nodeName.equals(localName) || nodeName.endsWith(":" + localName))
            {
                return allElements.item(i).getTextContent();
            }
        }

        return null;
    }

    private boolean hasElement(Element root, String localName)
    {
        // Kontrola existencie elementu podľa lokálneho mena bez ohľadu na namespace.
        NodeList nodes = root.getElementsByTagNameNS("*", localName);

        if(nodes.getLength() > 0)
        {
            return true;
        }

        // Záložná kontrola bez namespace.
        nodes = root.getElementsByTagName(localName);

        if(nodes.getLength() > 0)
        {
            return true;
        }

        // Posledná záloha: prehľadanie všetkých elementov.
        NodeList allElements = root.getElementsByTagName("*");

        for(int i = 0; i < allElements.getLength(); i++)
        {
            String nodeName = allElements.item(i).getNodeName();

            if(nodeName.equals(localName) || nodeName.endsWith(":" + localName))
            {
                return true;
            }
        }

        return false;
    }

    private void setXmlFeature(
            DocumentBuilderFactory factory,
            String feature,
            boolean value
    )
    {
        try
        {
            // Nastavenie bezpečnostnej vlastnosti XML parsera.
            factory.setFeature(feature, value);
        }
        catch(Exception ignored)
        {
            // Niektoré XML parsery nemusia podporovať všetky vlastnosti.
            // Ignorovanie je prijateľné, aby aplikácia nespadla na nepodporovanom nastavení.
        }
    }
}