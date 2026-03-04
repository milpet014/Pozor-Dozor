import knižnica.Svet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public final class CreateConfig
{
    private static Path appDataPath = AppPath.getAppDataDir(Main.APP_NAME);
    private static Path appConfigPath = appDataPath.resolve("config.cfg");
    private static String appName = Main.APP_NAME;

    private CreateConfig(){};

    public static void createConfig()
    {
        if(Files.exists(appConfigPath))
        {
            if(!CreateConfig.checkConfig())
            {
                try
                {
                    Files.delete(appConfigPath);
                    CreateConfig.createConfigFile();
                }
                catch(IOException e)
                {
                    Svet.sprava("Chyba, nepodarilo sa zmazat stary config: \n" + e.getMessage());
                }
            }
        }
        else
        {
            createConfigFile();
        }
    }

    private static void createConfigFile()
    {
        try
        {
            Files.createDirectories(appDataPath);
            Files.writeString(appConfigPath, "#" + appName, StandardOpenOption.CREATE);
        }
        catch (IOException e)
        {
            Svet.sprava("Chyba, nepodarilo sa vytvorit config: \n" + e.getMessage());
        }
    }

    private static boolean checkConfig()
    {
        String header = "#" + appName;
        try
        {
            List<String> lines = Files.readAllLines(appConfigPath);
            if(lines.isEmpty() || !lines.get(0).equals(header))
            {
                return false;
            }

            return true;
        }
        catch(IOException e)
        {
            System.out.println("Unknown error in checkConfig \n" + e.getMessage());
        }
        return false;
    }
}
