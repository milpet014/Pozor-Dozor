import knižnica.Svet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public final class ManageConfig
{


    private ManageConfig(){};

    public static Properties loadConfigProperties(Path appConfigPath)
    {
        Properties p = new Properties();

        try
        {
            InputStream in = Files.newInputStream(appConfigPath);
            p.load(in);
        }
        catch (IOException e)
        {
            Svet.sprava("Chyba, nepodarilo sa citat data z configu: \n" + e.getMessage());
        }

        return p;
    }

    public static void saveConfigProperties(Path appConfigPath, String appName, Properties configProperties)
    {
        try
        {
            OutputStream out = Files.newOutputStream(appConfigPath, StandardOpenOption.TRUNCATE_EXISTING);
            configProperties.store(out, appName);
        }
        catch (IOException e)
        {
            Svet.sprava("Chyba, nepodarilo sa ulozit data do configu: \n" + e.getMessage());
        }
    }
}
