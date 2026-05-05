import knižnica.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import java.awt.*;
import java.time.YearMonth;
import java.util.List;

public class Main
{
    // Rozmery hlavného okna aplikácie.
    private static final int WINDOW_SIZE_X = 594;
    private static final int WINDOW_SIZE_Y = 900;

    // Rozmery priestoru určeného na náhľad PDF.
    private static final int PAPER_SIZE_X = WINDOW_SIZE_X;
    private static final int PAPER_SIZE_Y = WINDOW_SIZE_Y - 60;

    // Základné názvy aplikácie.
    static final String HEADER = "Pozor Dozor";
    static final String APP_NAME = "pozor_dozor";

    // Základný stav aplikácie načítaný z konfigurácie.
    private static boolean firstRun = true;
    public static boolean usingCloud = false;

    // Položky hlavného menu.
    private static PolozkaPonuky addTeacher;
    private static PolozkaPonuky editTeacher;
    private static PolozkaPonuky calendar;
    private static PolozkaPonuky settings;
    private static PolozkaPonuky help;
    private static PolozkaPonuky documentation;
    private static PolozkaPonuky loadFromCloud;
    private static PolozkaPonuky saveToCloud;

    // Hlavný GRobot svet a cesta k PNG náhľadu PDF.
    private static GRobot worldRobot;
    private static final String pngPathStr = AppPath.pngPath().normalize().toString();

    // Tlačidlá v spodnej časti hlavného okna.
    private static Tlacidlo generateButton;
    private static Tlacidlo confirmButton;
    private static Tlacidlo saveButton;
    private static Tlacidlo closeButton;

    // Rozpracovaný, zatiaľ nepotvrdený rozpis dozorov.
    private static List<DutyEntry> pendingDuties = null;
    private static YearMonth pendingMonth = null;
    private static String pendingCalendarSignature = null;
    private static String pendingTeachersSignature = null;

    public static void main(String[] args)
    {
        // Vytvorenie hlavného okna GRobot sveta.
        worldRobot = new GRobot(WINDOW_SIZE_X, WINDOW_SIZE_Y, HEADER);

        // Nastavenie ikony hlavného okna.
        Svet.ikona(new javax.swing.ImageIcon(Main.class.getResource("/icons/pozor-dozor.png")).getImage());

        // Úprava polohy a veľkosti hlavného okna.
        Svet.zbal();
        Svet.vystred();

        // Skrytie samotného robota, keďže používame len plátno.
        worldRobot.skry();

        loading();

        applyFlatLafUI();

        // Načítanie konfigurácie aplikácie.
        AppConfig config = new AppConfig();

        firstRun = config.isFirstRun();

        // Prvé spustenie aplikácie a uloženie základných nastavení.
        if(firstRun)
        {
            FirstRunWizard.firstRunWizard();

            config.setFirstRun(FirstRunWizard.getFirstRun());
            config.setUsingCloud(FirstRunWizard.getUsingCloud());
            config.setSchoolID(FirstRunWizard.getSchoolID());

            config.saveConfig();

            GRobot.podlaha.vymazGrafiku();
            worldRobot.text("Vitajte. Po pridaní učiteľov do aplikácie môžete generovať dozor");
        }

        // Načítanie informácie, či má byť dostupná cloudová záloha.
        usingCloud = config.isUsingCloud();

        createMenuItems();

        // Registrácia obsluhy udalostí GRobot aplikácie.
        new AppRobot();

        createButtons();

        // Predvolený text hlavnej plochy pred prvým náhľadom.
        GRobot.podlaha.vymazGrafiku();
        worldRobot.text("Tu bude náhľad dozoru. Pridajte učiteľov do aplikácie a generujte zoznam.");

        // Pri bežnom spustení sa hneď vytvorí náhľad aktuálneho mesiaca.
        if(!firstRun) initializeCurrentMonthPreview();
    }

    private static void createMenuItems()
    {
        // Základné položky menu.
        addTeacher = new PolozkaPonuky("Pridať učiteľa");
        editTeacher = new PolozkaPonuky("Upraviť záznamy");
        calendar = new PolozkaPonuky("Kalendár");

        // Cloudové položky sa zobrazia len vtedy, ak sú povolené v konfigurácii.
        if(usingCloud)
        {
            loadFromCloud = new PolozkaPonuky("Načítať z cloudu");
            saveToCloud = new PolozkaPonuky("Uložiť na cloud");
        }

        settings = new PolozkaPonuky("Nastavenia");
        help = new PolozkaPonuky("O programe");
        documentation = new PolozkaPonuky("Dokumentácia");

    }

    private static void createButtons()
    {
        // Vytvorenie hlavných tlačidiel na plátne.
        generateButton = new Tlacidlo("Generuj");
        confirmButton = new Tlacidlo("Potvrdiť");
        saveButton = new Tlacidlo("Uložiť PDF");
        closeButton = new Tlacidlo("Koniec");

        // Nastavenie pozície tlačidiel.
        setupButton(generateButton, -180);
        setupButton(confirmButton, -60);
        setupButton(saveButton, 60);
        setupButton(closeButton, 180);
    }

    private static void setupButton(Tlacidlo button, double x)
    {
        // Spoločné rozmery a poloha tlačidiel.
        button.sirka(110);
        button.vyska(25);

        button.polohaX(x);
        button.polohaY(((double)WINDOW_SIZE_Y / 2 - 30) * -1);
    }

    public static void loading()
    {
        // Jednoduchá načítavacia obrazovka.
        GRobot.podlaha.vymazGrafiku();
        worldRobot.text("Načítava sa...");
        Svet.prekresli();
    }

    private static void applyFlatLafUI()
    {
        // Nastavenie moderného Swing vzhľadu.
        try
        {
            UIManager.setLookAndFeel(new FlatLightLaf());
        }
        catch(Exception e)
        {
            Svet.sprava("Neočakávaná chyba: " + e, "Chyba");
        }
    }

    private static void showTeacherPreviewOnCanvas()
    {
        // Uvoľnenie predchádzajúceho obrázka z pamäte GRobota.
        Svet.uvolni(pngPathStr);

        // Načítanie PNG náhľadu vygenerovaného z PDF.
        Obrazok originalImage = Obrazok.precitaj(pngPathStr);

        double imageWidth = originalImage.sirka();
        double imageHeight = originalImage.vyska();

        // Výpočet mierky tak, aby sa obrázok zmestil na plátno.
        double scale = Math.min((double)PAPER_SIZE_X / imageWidth, (double)PAPER_SIZE_Y / imageHeight);

        int scaledWidth = (int)Math.round(imageWidth * scale);
        int scaledHeight = (int)Math.round(imageHeight * scale);

        // Zmenšenie alebo zväčšenie náhľadu na vhodnú veľkosť.
        Image scaledImage = originalImage.getScaledInstance(
                scaledWidth,
                scaledHeight,
                Image.SCALE_SMOOTH
        );

        // Vykreslenie náhľadu na plátno.
        GRobot.podlaha.vymazGrafiku();
        GRobot.podlaha.obrazok(scaledImage);
        Svet.prekresli();
    }

    public static void initializeCurrentMonthPreview()
    {
        // Vytvorenie prvého náhľadu pre aktuálny mesiac.
        loading();
        generateCurrentMonthPreview(false);
    }

    public static boolean generateCurrentMonthPreview(boolean showSummary)
    {
        try
        {
            YearMonth month = YearMonth.now();

            MonthSettingsRepository monthSettingsRepository = new MonthSettingsRepository();
            TeacherRepository teacherRepository = new TeacherRepository();

            // Podpisy slúžia na zistenie, či sa po vygenerovaní náhľadu nezmenil kalendár alebo učitelia.
            String currentCalendarSignature = monthSettingsRepository.buildMonthSignature(month);
            String currentTeachersSignature = teacherRepository.buildTeachersSignature();

            // Vygenerovanie dozorov pre aktuálny mesiac.
            DutyGenerator generator = new DutyGenerator();
            List<DutyEntry> duties = generator.generateMonthDuties(month);

            // Uloženie rozpisu iba do pamäte, nie do CSV.
            pendingDuties = duties;
            pendingMonth = month;
            pendingCalendarSignature = currentCalendarSignature;
            pendingTeachersSignature = currentTeachersSignature;

            // Vytvorenie PDF a jeho PNG náhľadu.
            generatePdfPreview(duties, month);

            if(showSummary)
            {
                showDutiesSummary(duties, "Vygenerovaný náhľad dozorov");
            }

            return true;
        }
        catch(Exception e)
        {
            // Pri chybe sa rozpracovaný rozpis zahodí.
            invalidatePendingDuties();

            Svet.sprava("Generovanie náhľadu zlyhalo:\n" + e.getMessage(), "Chyba");
            return false;
        }
    }

    public static boolean confirmCurrentMonthDuties()
    {
        try
        {
            // Bez rozpracovaného náhľadu nie je čo potvrdiť.
            if(pendingDuties == null || pendingMonth == null || pendingCalendarSignature == null || pendingTeachersSignature == null)
            {
                Svet.sprava("Najprv treba vygenerovať náhľad rozpisu.", "Upozornenie");
                return false;
            }

            AppConfig config = new AppConfig();
            MonthSettingsRepository monthSettingsRepository = new MonthSettingsRepository();
            TeacherRepository teacherRepository = new TeacherRepository();

            String monthKey = pendingMonth.toString();

            // Aktuálny stav dát sa porovná so stavom pri generovaní náhľadu.
            String currentCalendarSignature = monthSettingsRepository.buildMonthSignature(pendingMonth);
            String currentTeachersSignature = teacherRepository.buildTeachersSignature();

            // Jeden mesiac sa môže potvrdiť iba raz.
            if(monthKey.equals(config.getLastGeneratedMonth()))
            {
                Svet.sprava("Rozpis pre tento mesiac už bol potvrdený.\n\n" + "Dozory sa nezapočítajú znova, aby sa učiteľom nenavýšil dutyCount druhýkrát.","Upozornenie");

                return false;
            }

            // Ak sa zmenil kalendár, starý náhľad už nie je platný.
            if(!currentCalendarSignature.equals(pendingCalendarSignature))
            {
                invalidatePendingDuties();

                Svet.sprava("Kalendár sa od vygenerovania náhľadu zmenil.\n" + "Vygenerujte rozpis znova.", "Upozornenie");

                return false;
            }

            // Ak sa zmenili učitelia, starý náhľad už nie je bezpečné potvrdiť.
            if(!currentTeachersSignature.equals(pendingTeachersSignature))
            {
                invalidatePendingDuties();

                Svet.sprava("Zoznam učiteľov sa od vygenerovania náhľadu zmenil.\n" + "Vygenerujte rozpis znova.", "Upozornenie");

                return false;
            }

            // Posledné potvrdenie pred zápisom do teachers.csv.
            int confirm = Svet.otazka("Naozaj chcete potvrdiť tento rozpis?\n\n" + "Po potvrdení sa učiteľom zapíšu dozory do teachers.csv.","Potvrdiť rozpis");

            if(confirm != 0)
            {
                return false;
            }

            // Až tu sa rozpis reálne zapíše učiteľom.
            DutyGenerator generator = new DutyGenerator();
            boolean updatedTeachers = generator.applyGeneratedDuties(pendingDuties);

            if(!updatedTeachers)
            {
                Svet.sprava("Rozpis existuje, ale nepodarilo sa uložiť nové počty dozorov.", "Chyba");

                return false;
            }

            // Uloženie informácie, že mesiac už bol potvrdený.
            config.setLastGeneratedMonth(monthKey);
            config.setLastGeneratedCalendarSignature(currentCalendarSignature);
            config.saveConfig();

            // Po potvrdení už pending rozpis nie je potrebný.
            invalidatePendingDuties();

            Svet.sprava("Rozpis bol potvrdený a dozory boli započítané.", "Hotovo");

            return true;
        }
        catch(Exception e)
        {
            Svet.sprava("Potvrdenie rozpisu zlyhalo:\n" + e.getMessage(), "Chyba");
            return false;
        }
    }

    public static boolean generateCurrentMonth(boolean applyGeneratedDuties, boolean showSummary)
    {
        // Kompatibilná metóda: najprv vytvorí náhľad.
        boolean generated = generateCurrentMonthPreview(showSummary);

        if(!generated) return false;

        // Voliteľne hneď spustí aj potvrdenie.
        if(applyGeneratedDuties) return confirmCurrentMonthDuties();

        return true;
    }

    public static void invalidatePendingDuties()
    {
        // Zneplatnenie rozpracovaného rozpisu.
        pendingDuties = null;
        pendingMonth = null;
        pendingCalendarSignature = null;
        pendingTeachersSignature = null;
    }

    public static void refreshCurrentPdfPreview()
    {
        try
        {
            // Ak existuje pending rozpis, obnoví sa iba jeho PDF vzhľad.
            if(pendingDuties != null && pendingMonth != null)
            {
                generatePdfPreview(pendingDuties, pendingMonth);
            }
            else
            {
                // Inak sa vytvorí nový náhľad aktuálneho mesiaca.
                generateCurrentMonthPreview(false);
            }
        }
        catch(Exception e)
        {
            Svet.sprava("Nepodarilo sa obnoviť náhľad PDF:\n" + e.getMessage(), "Chyba");
        }
    }

    private static void generatePdfPreview(List<DutyEntry> duties, YearMonth month) throws Exception
    {
        // Generovanie PDF súboru a následného PNG náhľadu.
        TeacherPdfGenerator pdfGenerator = new TeacherPdfGenerator();

        pdfGenerator.generateTeacherListPdf(duties, month);
        pdfGenerator.renderTeacherListPreview();

        showTeacherPreviewOnCanvas();
    }

    private static void showDutiesSummary(List<DutyEntry> duties, String title)
    {
        // Textový súhrn vygenerovaných dozorov.
        StringBuilder text = new StringBuilder();

        if(duties.isEmpty())
        {
            text.append("Pre aktuálny mesiac nie sú žiadne dni na generovanie.");
        }
        else
        {
            for(DutyEntry duty : duties)
            {
                text.append(duty.getDate())
                        .append(" - ")
                        .append(duty.getFirstTeacher().getFullName())
                        .append(" / ")
                        .append(duty.getSecondTeacher().getFullName())
                        .append("\n");
            }
        }

        Svet.sprava(text.toString(), title);
    }

    public static PolozkaPonuky getAddTeacher()
    {
        return addTeacher;
    }

    public static PolozkaPonuky getEditTeacher()
    {
        return editTeacher;
    }

    public static PolozkaPonuky getCalendar()
    {
        return calendar;
    }

    public static PolozkaPonuky getSettings()
    {
        return settings;
    }

    public static PolozkaPonuky getHelp()
    {
        return help;
    }

    public static PolozkaPonuky getDocumentation()
    {
        return documentation;
    }

    public static PolozkaPonuky getLoadFromCloud()
    {
        return loadFromCloud;
    }

    public static PolozkaPonuky getSaveToCloud()
    {
        return saveToCloud;
    }

    public static Tlacidlo getGenerateButton()
    {
        return generateButton;
    }

    public static Tlacidlo getConfirmButton()
    {
        return confirmButton;
    }

    public static Tlacidlo getSaveButton()
    {
        return saveButton;
    }

    public static Tlacidlo getCloseButton()
    {
        return closeButton;
    }
}