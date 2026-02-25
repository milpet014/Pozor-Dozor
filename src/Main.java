import knižnica.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Properties;

public class Main
{
    private static final int WINDOW_SIZE_X = 400;
    private static final int WINDOW_SIZE_Y = 200;
    private static final String HEADER = "Pozor Dozor";
    private static final String APP_NAME = "pozor_dozor";
    private static final String APP_DATA_DIR = APP_NAME;

    private static Path appData;
    private static Path appConfig;

    private static int rewriteSchoolID;

    private static int useCloud;
    private static boolean firstRun = true;
    private static boolean usingCloud = false;
    private static String schoolID = "none";

    private static Properties configProperties = new Properties();

    public static void main(String[] args)
    {

        //Vytvorenie sveta
        GRobot svet = new knižnica.GRobot(WINDOW_SIZE_X, WINDOW_SIZE_Y, HEADER);

        appData = getAppDataDir();
        appConfig = appData.resolve("config.cfg");

        if(Files.exists(appConfig))
        {
            if(!checkConfig())
            {
                try
                {
                    Files.delete(appConfig);
                    createConfig();
                }
                catch(IOException e)
                {
                    Svet.sprava("Chyba, nepodarilo sa zmazat stary config: \n" + e.getMessage());
                }
            }
            configProperties = loadConfigProperties();

            System.out.println(configProperties.getProperty("RW_test", "failed"));
        }
        else
        {
            createConfig();
            configProperties = loadConfigProperties();

            System.out.println(configProperties.getProperty("RW_test", "failed"));
        }

        //Uvodna inicializacia
        if(firstRun)
        {
            firstRun = false;
            Svet.sprava("Welcome text", "Vitajte");

            useCloud = Svet.otázka("Prajete si využívať cloudové služby aplikácie pre zálohy Vašej práce?", "Cloud");

            if(useCloud == 0)
            {
                usingCloud = true;

                if (!(schoolID.equals("none")))
                {
                    rewriteSchoolID = Svet.otazka("Aplikácia už má pridelené ID. Prajete si ho zmeniť?", "Cloud");
                    if(rewriteSchoolID == 0)
                    {
                        schoolID = Svet.zadajRetazec("Zadajte identifikátor školy.", "Cloud");
                    }
                    else
                    {
                        Svet.sprava("Aplikácia bude využívať pôvodné ID", "Cloud");
                    }

                }
                else
                {
                    schoolID = Svet.zadajRetazec("Zadajte identifikátor školy.", "Cloud");
                }
            }
            else
            {
                Svet.sprava("Aplikácia ukladá vsetky dáta lokálne.", "Cloud");
                usingCloud = false;
            }

            //DEBUG!!!

            System.out.println(getAppDataDir());

            System.out.println(usingCloud);
        }

        saveConfigProperties();
    }

    private static void createConfig()
    {
        try
        {
            Files.createDirectories(appData);
            Files.writeString(appConfig, "#" + APP_NAME + "\n" + "RW_test=ok", StandardOpenOption.CREATE);
        }
        catch (IOException e)
        {
            Svet.sprava("Chyba, nepodarilo sa vytvorit config: \n" + e.getMessage());
        }
    }

    private static boolean checkConfig()
    {
        String header = "#" + APP_NAME;
        try
        {
            List<String> lines = Files.readAllLines(appConfig);
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

    private static Properties loadConfigProperties()
    {
        Properties p = new Properties();

        try
        {
            InputStream in = Files.newInputStream(appConfig);
            p.load(in);
        }
        catch (IOException e)
        {
            Svet.sprava("Chyba, nepodarilo sa citat data z configu: \n" + e.getMessage());
        }

        return p;
    }

    private static void saveConfigProperties()
    {
        try
        {
            OutputStream out = Files.newOutputStream(appConfig, StandardOpenOption.TRUNCATE_EXISTING);
            configProperties.store(out, APP_NAME);
        }
        catch (IOException e)
        {
            Svet.sprava("Chyba, nepodarilo sa ulozit data do configu: \n" + e.getMessage());
        }
    }


    private static Path getAppDataDir()
    {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return Paths.get(System.getProperty("user.home"),"Documents", APP_DATA_DIR);
        } else {
            return Paths.get(System.getProperty("user.home"),"." + APP_DATA_DIR.toLowerCase());
        }
    }
}