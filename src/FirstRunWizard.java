import knižnica.Svet;

public class FirstRunWizard
{
    // Výsledky odpovedí používateľa v dialógoch.
    private static int useCloud;
    private static int rewriteSchoolID;

    // Identifikátor školy používaný pri cloudovej zálohe.
    private static String schoolID = "none";

    // Nastavenia zistené počas prvého spustenia.
    private static boolean usingCloud = false;
    private static boolean firstRun;

    // Trieda slúži iba staticky, preto sa nemá vytvárať jej inštancia.
    private FirstRunWizard(){}

    public static void firstRunWizard()
    {
        // Po dokončení sprievodcu už aplikácia nebude v režime prvého spustenia.
        firstRun = false;

        // Úvodný text vysvetľujúci účel aplikácie a cloudovej zálohy.
        Svet.sprava("Pozor Dozor je jednoduchá aplikácia určená na prípravu a generovanie rozpisu školských dozorov. Umožňuje evidovať učiteľov, nastavovať ich dostupnosť počas týždňa, upravovať dni v kalendári a následne vytvoriť prehľadný mesačný rozpis dozorov vo forme PDF dokumentu.\n" +
                "\n" +
                "Aplikácia podporuje aj manuálnu zálohu dát na cloud. Používateľ môže uložiť aktuálne súbory aplikácie na cloud alebo ich neskôr načítať späť, napríklad pri prenose aplikácie na iný počítač alebo pri obnove údajov. Cloudová záloha využíva kód školy, ktorý slúži na prístup k príslušnému cloudovému priestoru.\n" +
                "\n" +
                "Kód školy je možné získať po žiadosti na emailovej adrese: email@gmail.com.", "Vitajte");

        // Otázka, či chce používateľ využívať cloudové zálohy.
        useCloud = Svet.otázka("Prajete si využívať cloudové služby aplikácie pre zálohy Vašej práce?", "Cloud");

        if(useCloud == 0)
        {
            // Používateľ povolil cloudové služby.
            usingCloud = true;

            // Ak už existuje ID školy, používateľ sa môže rozhodnúť, či ho zmení.
            if (!(schoolID.equals("none")))
            {
                rewriteSchoolID = Svet.otazka("Aplikácia už má pridelené ID. Prajete si ho zmeniť?", "Cloud");

                if(rewriteSchoolID == 0)
                {
                    // Zadanie nového identifikátora školy.
                    schoolID = Svet.zadajRetazec("Zadajte identifikátor školy.", "Cloud");
                }
                else
                {
                    // Ponechanie pôvodného identifikátora školy.
                    Svet.sprava("Aplikácia bude využívať pôvodné ID", "Cloud");
                }
            }
            else
            {
                // Prvé zadanie identifikátora školy.
                schoolID = Svet.zadajRetazec("Zadajte identifikátor školy.", "Cloud");
            }
        }
        else
        {
            // Používateľ cloudové služby nechce používať.
            Svet.sprava("Aplikácia ukladá vsetky dáta lokálne.", "Cloud");
            usingCloud = false;
        }
    }

    public static boolean getFirstRun()
    {
        // Vracia, či má byť aplikácia stále považovaná za prvý beh.
        return firstRun;
    }

    public static String getSchoolID()
    {
        // Vracia identifikátor školy zadaný používateľom.
        return schoolID;
    }

    public static boolean getUsingCloud()
    {
        // Vracia, či používateľ povolil cloudové služby.
        return usingCloud;
    }
}