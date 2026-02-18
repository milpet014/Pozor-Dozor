import knižnica.*;
import java.nio.file.*;
import java.sql.SQLOutput;

public class Main
{
    private static final int WINDOW_SIZE_X = 400;
    private static final int WINDOW_SIZE_Y = 200;
    private static final String HEADER = "Pozor Dozor";
    private static final String APP_NAME = HEADER;

    private static String schoolID = "none";
    private static String home;

    private static boolean firstRun = true;
    private static boolean usingCloud = false;

    private static int useCloud;
    private static int rewriteSchoolID;

    static void main() {
        GRobot svet = new knižnica.GRobot(WINDOW_SIZE_X, WINDOW_SIZE_Y, HEADER);

        home = System.getProperty("user.home");

        if(firstRun)
        {
            Svet.sprava("Welcome text", "Vitajte");

            useCloud = Svet.otázka("Prajete si využívať cloudové služby aplikácie pre zálohy Vašej práce?", "Cloud");

            if(useCloud == 0)
            {
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

            System.out.println(getAppDataDir());

            System.out.println(useCloud);
        }






    }

    private static Path getAppDataDir()
    {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return Paths.get(System.getProperty("user.home"),"Documents", APP_NAME);
        } else {
            return Paths.get(System.getProperty("user.home"),"." + APP_NAME.toLowerCase());
        }
    }
}
