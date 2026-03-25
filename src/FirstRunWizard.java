import knižnica.Svet;

public class FirstRunWizard
{
    private static int useCloud;
    private static int rewriteSchoolID;

    private static String schoolID = "none";

    private static boolean usingCloud = false;
    private static boolean firstRun;

    private FirstRunWizard(){}

    public static void firstRunWizard()
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
    }
    public static boolean getFirstRun()
    {
        return firstRun;
    }

    public static String getSchoolID()
    {
        return schoolID;
    }

    public static boolean getUsingCloud()
    {
        return usingCloud;
    }
}
