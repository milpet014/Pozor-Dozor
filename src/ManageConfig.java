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
    // Trieda slúži iba na načítanie a uloženie konfigurácie.
    private ManageConfig(){};

    public static Properties loadConfigProperties(Path appConfigPath)
    {
        // Objekt, do ktorého sa načítajú hodnoty z config.cfg.
        Properties p = new Properties();

        try
        {
            // Otvorenie konfiguračného súboru na čítanie.
            InputStream in = Files.newInputStream(appConfigPath);

            // Načítanie vlastností zo súboru.
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
            // Otvorenie konfiguračného súboru na prepísanie.
            OutputStream out = Files.newOutputStream(appConfigPath, StandardOpenOption.TRUNCATE_EXISTING);

            // Uloženie konfigurácie do súboru.
            configProperties.store(out, appName);
        }
        catch (IOException e)
        {
            Svet.sprava("Chyba, nepodarilo sa ulozit data do configu: \n" + e.getMessage());
        }
    }
}