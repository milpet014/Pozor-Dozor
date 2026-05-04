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
    private static final String NEXTCLOUD_PUBLIC_WEBDAV_PREFIX =
            "https://cloud.milpet.eu/public.php/dav/files/";

    private static final String PROPFIND_BODY =
            "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" +
                    "<d:propfind xmlns:d=\"DAV:\">" +
                    "<d:prop>" +
                    "<d:resourcetype/>" +
                    "</d:prop>" +
                    "</d:propfind>";

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public CloudBackupResult uploadAppFolder() throws Exception
    {
        String schoolID = getSchoolID();
        URI remoteDirectoryUri = buildRemoteDirectoryUri(schoolID);

        Path appDirectory = AppPath.appDataPath();
        Files.createDirectories(appDirectory);

        int uploadedFiles = 0;

        try(DirectoryStream<Path> files = Files.newDirectoryStream(appDirectory))
        {
            for(Path file : files)
            {
                if(Files.isRegularFile(file) && shouldUploadFile(file))
                {
                    uploadFile(remoteDirectoryUri, file);
                    uploadedFiles++;
                }
            }
        }

        return new CloudBackupResult(uploadedFiles, schoolID);
    }

    public CloudBackupResult downloadAppFolder() throws Exception
    {
        String schoolID = getSchoolID();
        URI remoteDirectoryUri = buildRemoteDirectoryUri(schoolID);

        List<String> remoteFiles = listRemoteFiles(remoteDirectoryUri);

        Path appDirectory = AppPath.appDataPath();
        Files.createDirectories(appDirectory);

        int downloadedFiles = 0;

        for(String remoteFileName : remoteFiles)
        {
            if(isSafeFileName(remoteFileName))
            {
                downloadFile(remoteDirectoryUri, remoteFileName, appDirectory);
                downloadedFiles++;
            }
        }

        return new CloudBackupResult(downloadedFiles, schoolID);
    }

    private String getSchoolID()
    {
        AppConfig config = new AppConfig();

        String schoolID = config.getSchoolID();

        if(schoolID == null || schoolID.isBlank() || schoolID.equals("none"))
        {
            throw new IllegalStateException("Nie je nastavený kód školy.");
        }

        return sanitizeShareToken(schoolID);
    }

    private String sanitizeShareToken(String value)
    {
        String sanitized = value.trim();

        sanitized = sanitized.replace("\\", "");
        sanitized = sanitized.replace("/", "");
        sanitized = sanitized.replace("..", "");

        sanitized = sanitized.replaceAll("[^a-zA-Z0-9._-]", "");

        if(sanitized.isBlank())
        {
            throw new IllegalStateException("Kód školy je neplatný.");
        }

        return sanitized;
    }

    private URI buildRemoteDirectoryUri(String schoolID)
    {
        String baseUrl = NEXTCLOUD_PUBLIC_WEBDAV_PREFIX +
                encodePathSegment(schoolID) +
                "/";

        return URI.create(baseUrl);
    }

    private boolean shouldUploadFile(Path file)
    {
        String fileName = file.getFileName().toString();

        if(fileName.endsWith(".download"))
        {
            return false;
        }

        if(fileName.endsWith(".tmp"))
        {
            return false;
        }

        return true;
    }

    private void uploadFile(URI remoteDirectoryUri, Path localFile) throws Exception
    {
        String fileName = localFile.getFileName().toString();

        URI remoteFileUri = remoteDirectoryUri.resolve(encodePathSegment(fileName));

        HttpRequest request = requestBuilder(remoteFileUri)
                .header("Content-Type", "application/octet-stream")
                .PUT(HttpRequest.BodyPublishers.ofFile(localFile))
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        int status = response.statusCode();

        if(status == 200 || status == 201 || status == 204)
        {
            return;
        }

        throw new IOException(
                "Nepodarilo sa nahrať súbor: " + fileName + "\n" +
                        "HTTP " + status + "\n" +
                        response.body()
        );
    }

    private List<String> listRemoteFiles(URI remoteDirectoryUri) throws Exception
    {
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

        if(status != 207 && (status < 200 || status >= 300))
        {
            throw new IOException(
                    "Nepodarilo sa načítať zoznam súborov z cloudu.\n" +
                            "HTTP " + status
            );
        }

        return parseRemoteFileNames(response.body());
    }

    private List<String> parseRemoteFileNames(byte[] xmlBytes) throws Exception
    {
        List<String> fileNames = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        setXmlFeature(factory, "http://apache.org/xml/features/disallow-doctype-decl", true);
        setXmlFeature(factory, "http://xml.org/sax/features/external-general-entities", false);
        setXmlFeature(factory, "http://xml.org/sax/features/external-parameter-entities", false);

        Document document = factory.newDocumentBuilder().parse(
                new ByteArrayInputStream(xmlBytes)
        );

        NodeList responses = document.getElementsByTagNameNS("*", "response");

        if(responses.getLength() == 0)
        {
            responses = document.getElementsByTagName("response");
        }

        for(int i = 0; i < responses.getLength(); i++)
        {
            Element responseElement = (Element)responses.item(i);

            String href = getFirstText(responseElement, "href");

            if(href == null || href.isBlank())
            {
                continue;
            }

            boolean isDirectory = hasElement(responseElement, "collection");

            if(isDirectory)
            {
                continue;
            }

            String fileName = extractFileNameFromHref(href);

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
        URI remoteFileUri = remoteDirectoryUri.resolve(encodePathSegment(remoteFileName));

        HttpRequest request = requestBuilder(remoteFileUri)
                .GET()
                .build();

        HttpResponse<byte[]> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofByteArray()
        );

        int status = response.statusCode();

        if(status != 200)
        {
            throw new IOException(
                    "Nepodarilo sa stiahnuť súbor: " + remoteFileName + "\n" +
                            "HTTP " + status
            );
        }

        Path targetFile = appDirectory.resolve(remoteFileName).normalize();

        if(!targetFile.getParent().equals(appDirectory.normalize()))
        {
            throw new IOException("Neplatný názov vzdialeného súboru: " + remoteFileName);
        }

        Path tempFile = targetFile.resolveSibling(targetFile.getFileName() + ".download");

        Files.write(tempFile, response.body());

        moveReplacing(tempFile, targetFile);
    }

    private void moveReplacing(Path tempFile, Path targetFile) throws IOException
    {
        try
        {
            Files.move(
                    tempFile,
                    targetFile,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
            );
        }
        catch(AtomicMoveNotSupportedException e)
        {
            Files.move(
                    tempFile,
                    targetFile,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
    }

    private HttpRequest.Builder requestBuilder(URI uri)
    {
        return HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(30))
                .header("User-Agent", Main.APP_NAME + "-cloud-backup");
    }

    private String encodePathSegment(String text)
    {
        return URLEncoder.encode(text, StandardCharsets.UTF_8)
                .replace("+", "%20");
    }

    private String extractFileNameFromHref(String href)
    {
        try
        {
            String path = URI.create(href).getPath();

            if(path == null || path.isBlank())
            {
                path = href;
            }

            if(path.endsWith("/"))
            {
                path = path.substring(0, path.length() - 1);
            }

            int lastSlashIndex = path.lastIndexOf('/');

            String encodedName = lastSlashIndex >= 0
                    ? path.substring(lastSlashIndex + 1)
                    : path;

            return URLDecoder.decode(encodedName, StandardCharsets.UTF_8);
        }
        catch(Exception e)
        {
            return null;
        }
    }

    private boolean isSafeFileName(String fileName)
    {
        return fileName != null &&
                !fileName.isBlank() &&
                !fileName.contains("/") &&
                !fileName.contains("\\") &&
                !fileName.equals(".") &&
                !fileName.equals("..");
    }

    private String getFirstText(Element root, String localName)
    {
        NodeList nodes = root.getElementsByTagNameNS("*", localName);

        if(nodes.getLength() > 0)
        {
            return nodes.item(0).getTextContent();
        }

        nodes = root.getElementsByTagName(localName);

        if(nodes.getLength() > 0)
        {
            return nodes.item(0).getTextContent();
        }

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
        NodeList nodes = root.getElementsByTagNameNS("*", localName);

        if(nodes.getLength() > 0)
        {
            return true;
        }

        nodes = root.getElementsByTagName(localName);

        if(nodes.getLength() > 0)
        {
            return true;
        }

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
            factory.setFeature(feature, value);
        }
        catch(Exception ignored)
        {
        }
    }
}