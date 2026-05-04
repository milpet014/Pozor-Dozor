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
        Svet.sprava("Pozor Dozor je jednoduchá aplikácia určená na prípravu a generovanie rozpisu školských dozorov. Umožňuje evidovať učiteľov, nastavovať ich dostupnosť počas týždňa, upravovať dni v kalendári a následne vytvoriť prehľadný mesačný rozpis dozorov vo forme PDF dokumentu.\n" +
                "\n" +
                "Aplikácia podporuje aj manuálnu zálohu dát na cloud. Používateľ môže uložiť aktuálne súbory aplikácie na cloud alebo ich neskôr načítať späť, napríklad pri prenose aplikácie na iný počítač alebo pri obnove údajov. Cloudová záloha využíva kód školy, ktorý slúži na prístup k príslušnému cloudovému priestoru.\n" +
                "\n" +
                "Kód školy je možné získať po žiadosti na emailovej adrese: email@gmail.com.", "Vitajte");

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
