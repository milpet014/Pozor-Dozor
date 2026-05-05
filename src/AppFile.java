import knižnica.Svet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public final class AppFile
{
    // Hlavný dátový priečinok aplikácie.
    private static Path appDataPath = AppPath.resolveAppDataDir();

    // Trieda slúži iba staticky, preto sa nemá vytvárať jej inštancia.
    private AppFile(){};

    public static void checkFile(Path file, String header)
    {
        // Kontrola bežného konfiguračného súboru s hlavičkou začínajúcou znakom #.
        if(Files.exists(file))
        {
            if(!checkFileHeader(file, header))
            {
                try
                {
                    // Ak má súbor nesprávnu hlavičku, vytvorí sa nanovo.
                    Files.delete(file);
                    createFile(file, header);
                }
                catch(IOException e)
                {
                    Svet.sprava("Chyba, nepodarilo sa zmazat subor: " + file + "\n" + e.getMessage());
                }
            }
        }
        else
        {
            // Ak súbor neexistuje, vytvorí sa.
            createFile(file, header);
        }
    }

    private static void createFile(Path file, String header)
    {
        try
        {
            // Vytvorenie dátového priečinka a základného súboru s hlavičkou.
            Files.createDirectories(appDataPath);
            Files.writeString(file, "#" + header);
        }
        catch(IOException e)
        {
            Svet.sprava("Chyba, nepodarilo sa zmazat subor: " + file + "\n" + e.getMessage());
        }
    }

    private static boolean checkFileHeader(Path file, String header)
    {
        try
        {
            // Prvý riadok musí obsahovať očakávanú hlavičku.
            List<String> lines = Files.readAllLines(file);
            if(lines.isEmpty() || !lines.get(0).equals("#" + header))
            {
                return false;
            }

            return true;
        }
        catch(IOException e)
        {
            System.out.println("Unknown error in checkFile \n" + e.getMessage());
        }
        return false;
    }

    public static void checkCsvFile(Path file, String header)
    {
        // Zjednodušená kontrola CSV bez záložného súboru.
        checkCsvFile(file, header, null, null);
    }

    public static void checkCsvFile(Path file, String header, Path backupFile, String fileLabel)
    {
        // Kontrola CSV súboru a jeho hlavičky.
        if(Files.exists(file))
        {
            if(!checkCsvFileHeader(file, header))
            {
                // Pri nesprávnej hlavičke sa CSV opraví alebo vytvorí nanovo.
                repairCsvFile(file, header, backupFile, fileLabel);
            }
        }
        else
        {
            // Ak CSV neexistuje, vytvorí sa s očakávanou hlavičkou.
            createCsvFile(file, header);
        }
    }

    private static void repairCsvFile(Path file, String header, Path backupFile, String fileLabel)
    {
        try
        {
            // Ak je zadaná cesta k zálohe, pôvodný poškodený súbor sa najprv skopíruje.
            if(backupFile != null && Files.exists(file))
            {
                Files.copy(file, backupFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // Vytvorenie nového čistého CSV súboru.
            createCsvFile(file, header);

            if(fileLabel != null)
            {
                Svet.sprava("Súbor \"" + fileLabel + "\" bol poškodený a bol vytvorený nanovo.", "Chyba");
            }
        }
        catch(IOException e)
        {
            Svet.sprava("Chyba, nepodarilo sa opraviť CSV súbor: " + file + "\n" + e.getMessage(), "Chyba");
        }
    }

    private static void createCsvFile(Path file, String header)
    {
        try
        {
            // Vytvorenie dátového priečinka a CSV súboru s hlavičkou.
            Files.createDirectories(appDataPath);
            Files.writeString(file, header + System.lineSeparator());
        }
        catch(IOException e)
        {
            Svet.sprava("Chyba, nepodarilo sa vytvoriť CSV súbor: " + file + "\n" + e.getMessage(), "Chyba");
        }
    }

    private static boolean checkCsvFileHeader(Path file, String header)
    {
        try
        {
            // CSV súbor musí mať v prvom riadku presne očakávanú hlavičku.
            List<String> lines = Files.readAllLines(file);

            if(lines.isEmpty() || !lines.get(0).equals(header))
            {
                return false;
            }

            return true;
        }
        catch(IOException e)
        {
            Svet.sprava("Chyba, nepodarilo sa čítať CSV súbor: " + file + "\n" + e.getMessage(), "Chyba");
            return false;
        }
    }
}