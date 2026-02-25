import knižnica.*;

import java.io.IOException;
import java.nio.file.*;

public class Main
{
    private static final int WINDOW_SIZE_X = 400;
    private static final int WINDOW_SIZE_Y = 200;
    private static final String HEADER = "Pozor Dozor";
    private static final String APP_NAME = HEADER;
    private static final String APP_DATA_DIR = "pozor_dozor";

    private static String schoolID = "none";
    private static Path appData;
    private static Path appConfig;

    private static boolean firstRun = true;
    private static boolean usingCloud = false;

    private static int useCloud;
    private static int rewriteSchoolID;

    public static void main(String[] args)
    {

        //Vytvorenie sveta
        GRobot svet = new knižnica.GRobot(WINDOW_SIZE_X, WINDOW_SIZE_Y, HEADER);

        appData = getAppDataDir();
        appConfig = appData.resolve("config.cfg");

        if(Files.exists(appConfig))
        {
            System.out.println("OK");
        }
        else
        {
            try
            {
                Files.createDirectory(appData);
                Files.writeString(appConfig, "Config rw test", StandardOpenOption.CREATE);
            }
            catch (IOException e)
            {
                Svet.sprava("Chyba, nepodarilo sa vytvorit config: \n" + e.getMessage());
            }
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
    }

    private static void loadInitialInfoFromFile()
    {
        //ToDo
    }

    private static void saveInitialInfoToFile()
    {
        //ToDo
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