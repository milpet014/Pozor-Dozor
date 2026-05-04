import knižnica.*;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.UIManager;
import java.awt.*;
import java.time.YearMonth;
import java.util.List;

public class Main
{
    private static final int WINDOW_SIZE_X = 594;
    private static final int WINDOW_SIZE_Y = 900;
    private static final int PAPER_SIZE_X = WINDOW_SIZE_X;
    private static final int PAPER_SIZE_Y = WINDOW_SIZE_Y - 60;

    static final String HEADER = "Pozor Dozor";
    static final String APP_NAME = "pozor_dozor";

    private static boolean firstRun = true;
    public static boolean usingCloud = false;

    private static PolozkaPonuky addTeacher;
    private static PolozkaPonuky editTeacher;
    private static PolozkaPonuky calendar;
    private static PolozkaPonuky settings;
    private static PolozkaPonuky help;
    private static PolozkaPonuky documentation;
    private static PolozkaPonuky loadFromCloud;
    private static PolozkaPonuky saveToCloud;

    private static GRobot worldRobot;
    private static final String pngPathStr = AppPath.pngPath().normalize().toString();

    private static Tlacidlo generateButton;
    private static Tlacidlo confirmButton;
    private static Tlacidlo saveButton;
    private static Tlacidlo closeButton;

    private static List<DutyEntry> pendingDuties = null;
    private static YearMonth pendingMonth = null;
    private static String pendingCalendarSignature = null;
    private static String pendingTeachersSignature = null;

    public static void main(String[] args)
    {
        worldRobot = new GRobot(WINDOW_SIZE_X, WINDOW_SIZE_Y, HEADER);

        Svet.ikona(new javax.swing.ImageIcon(Main.class.getResource("/icons/pozor-dozor.png")).getImage());

        Svet.zbal();
        Svet.vystred();

        worldRobot.skry();

        loading();

        applyFlatLafUI();

        AppConfig config = new AppConfig();

        firstRun = config.isFirstRun();

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

        usingCloud = config.isUsingCloud();

        createMenuItems();

        new AppRobot();

        createButtons();

        GRobot.podlaha.vymazGrafiku();
        worldRobot.text("Tu bude náhľad dozoru. Pridajte učiteľov do aplikácie a generujte zoznam.");

        if(!firstRun) initializeCurrentMonthPreview();
    }

    private static void createMenuItems()
    {
        addTeacher = new PolozkaPonuky("Pridať učiteľa");
        editTeacher = new PolozkaPonuky("Upraviť záznamy");
        calendar = new PolozkaPonuky("Kalendár");

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
        generateButton = new Tlacidlo("Generuj");
        confirmButton = new Tlacidlo("Potvrdiť");
        saveButton = new Tlacidlo("Uložiť PDF");
        closeButton = new Tlacidlo("Koniec");

        setupButton(generateButton, -180);
        setupButton(confirmButton, -60);
        setupButton(saveButton, 60);
        setupButton(closeButton, 180);
    }

    private static void setupButton(Tlacidlo button, double x)
    {
        button.sirka(110);
        button.vyska(25);

        button.polohaX(x);
        button.polohaY(((double)WINDOW_SIZE_Y / 2 - 30) * -1);
    }

    public static void loading()
    {
        GRobot.podlaha.vymazGrafiku();
        worldRobot.text("Načítava sa...");
        Svet.prekresli();
    }

    private static void applyFlatLafUI()
    {
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
        Svet.uvolni(pngPathStr);

        Obrazok originalImage = Obrazok.precitaj(pngPathStr);

        double imageWidth = originalImage.sirka();
        double imageHeight = originalImage.vyska();

        double scale = Math.min(
                (double)PAPER_SIZE_X / imageWidth,
                (double)PAPER_SIZE_Y / imageHeight
        );

        int scaledWidth = (int)Math.round(imageWidth * scale);
        int scaledHeight = (int)Math.round(imageHeight * scale);

        Image scaledImage = originalImage.getScaledInstance(
                scaledWidth,
                scaledHeight,
                Image.SCALE_SMOOTH
        );

        GRobot.podlaha.vymazGrafiku();
        GRobot.podlaha.obrazok(scaledImage);
        Svet.prekresli();
    }

    public static void initializeCurrentMonthPreview()
    {
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

            String currentCalendarSignature = monthSettingsRepository.buildMonthSignature(month);
            String currentTeachersSignature = teacherRepository.buildTeachersSignature();

            DutyGenerator generator = new DutyGenerator();
            List<DutyEntry> duties = generator.generateMonthDuties(month);

            pendingDuties = duties;
            pendingMonth = month;
            pendingCalendarSignature = currentCalendarSignature;
            pendingTeachersSignature = currentTeachersSignature;

            generatePdfPreview(duties, month);

            if(showSummary)
            {
                showDutiesSummary(duties, "Vygenerovaný náhľad dozorov");
            }

            return true;
        }
        catch(Exception e)
        {
            invalidatePendingDuties();

            Svet.sprava("Generovanie náhľadu zlyhalo:\n" + e.getMessage(), "Chyba");
            return false;
        }
    }

    public static boolean confirmCurrentMonthDuties()
    {
        try
        {
            if(pendingDuties == null ||
                    pendingMonth == null ||
                    pendingCalendarSignature == null ||
                    pendingTeachersSignature == null)
            {
                Svet.sprava("Najprv treba vygenerovať náhľad rozpisu.", "Upozornenie");
                return false;
            }

            AppConfig config = new AppConfig();
            MonthSettingsRepository monthSettingsRepository = new MonthSettingsRepository();
            TeacherRepository teacherRepository = new TeacherRepository();

            String monthKey = pendingMonth.toString();
            String currentCalendarSignature = monthSettingsRepository.buildMonthSignature(pendingMonth);
            String currentTeachersSignature = teacherRepository.buildTeachersSignature();

            if(monthKey.equals(config.getLastGeneratedMonth()))
            {
                Svet.sprava(
                        "Rozpis pre tento mesiac už bol potvrdený.\n\n" +
                                "Dozory sa nezapočítajú znova, aby sa učiteľom nenavýšil dutyCount druhýkrát.",
                        "Upozornenie"
                );

                return false;
            }

            if(!currentCalendarSignature.equals(pendingCalendarSignature))
            {
                invalidatePendingDuties();

                Svet.sprava(
                        "Kalendár sa od vygenerovania náhľadu zmenil.\n" +
                                "Vygenerujte rozpis znova.",
                        "Upozornenie"
                );

                return false;
            }

            if(!currentTeachersSignature.equals(pendingTeachersSignature))
            {
                invalidatePendingDuties();

                Svet.sprava(
                        "Zoznam učiteľov sa od vygenerovania náhľadu zmenil.\n" +
                                "Vygenerujte rozpis znova.",
                        "Upozornenie"
                );

                return false;
            }

            int confirm = Svet.otazka(
                    "Naozaj chcete potvrdiť tento rozpis?\n\n" +
                            "Po potvrdení sa učiteľom zapíšu dozory do teachers.csv.",
                    "Potvrdiť rozpis"
            );

            if(confirm != 0)
            {
                return false;
            }

            DutyGenerator generator = new DutyGenerator();
            boolean updatedTeachers = generator.applyGeneratedDuties(pendingDuties);

            if(!updatedTeachers)
            {
                Svet.sprava(
                        "Rozpis existuje, ale nepodarilo sa uložiť nové počty dozorov.",
                        "Chyba"
                );

                return false;
            }

            config.setLastGeneratedMonth(monthKey);
            config.setLastGeneratedCalendarSignature(currentCalendarSignature);
            config.saveConfig();

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
        boolean generated = generateCurrentMonthPreview(showSummary);

        if(!generated)
        {
            return false;
        }

        if(applyGeneratedDuties)
        {
            return confirmCurrentMonthDuties();
        }

        return true;
    }

    public static void invalidatePendingDuties()
    {
        pendingDuties = null;
        pendingMonth = null;
        pendingCalendarSignature = null;
        pendingTeachersSignature = null;
    }

    public static void refreshCurrentPdfPreview()
    {
        try
        {
            if(pendingDuties != null && pendingMonth != null)
            {
                generatePdfPreview(pendingDuties, pendingMonth);
            }
            else
            {
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
        TeacherPdfGenerator pdfGenerator = new TeacherPdfGenerator();

        pdfGenerator.generateTeacherListPdf(duties, month);
        pdfGenerator.renderTeacherListPreview();

        showTeacherPreviewOnCanvas();
    }

    private static void showDutiesSummary(List<DutyEntry> duties, String title)
    {
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