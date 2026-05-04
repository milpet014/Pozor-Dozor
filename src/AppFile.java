import knižnica.Svet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public final class AppFile
{
    private static Path appDataPath = AppPath.resolveAppDataDir();

    private AppFile(){};

    public static void checkFile(Path file, String header)
    {
        if(Files.exists(file))
        {
            if(!checkFileHeader(file, header))
            {
                try
                {
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
            createFile(file, header);
        }
    }

    private static void createFile(Path file, String header)
    {
        try
        {
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
        checkCsvFile(file, header, null, null);
    }

    public static void checkCsvFile(Path file, String header, Path backupFile, String fileLabel)
    {
        if(Files.exists(file))
        {
            if(!checkCsvFileHeader(file, header))
            {
                repairCsvFile(file, header, backupFile, fileLabel);
            }
        }
        else
        {
            createCsvFile(file, header);
        }
    }

    private static void repairCsvFile(Path file, String header, Path backupFile, String fileLabel)
    {
        try
        {
            if(backupFile != null && Files.exists(file))
            {
                Files.copy(file, backupFile, StandardCopyOption.REPLACE_EXISTING);
            }

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
