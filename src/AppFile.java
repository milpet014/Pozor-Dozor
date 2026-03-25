import knižnica.Svet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        catch (IOException e)
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
}
