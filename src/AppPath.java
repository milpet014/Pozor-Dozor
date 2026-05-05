import java.nio.file.Path;
import java.nio.file.Paths;

public final class AppPath
{
    // Názov aplikácie používaný pri vytváraní dátového priečinka.
    private static final String APP_NAME = Main.APP_NAME;

    // Hlavný dátový priečinok aplikácie.
    private static final Path APP_DATA_DIR = resolveAppDataDir();

    // Názvy súborov používaných aplikáciou.
    private static final String CONFIG = "config.cfg";
    private static final String TEACHERS = "teachers.csv";
    private static final String TEACHERS_PDF = "teachers.pdf";
    private static final String TEACHERS_PREVIEW = "teachers_preview.png";
    private static final String CALENDAR = "calendar.csv";

    // Trieda slúži iba na poskytovanie ciest, preto sa nemá vytvárať jej inštancia.
    private AppPath(){};

    public static Path resolveAppDataDir()
    {
        // Na Windowse sa dáta ukladajú do priečinka Dokumenty.
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return Paths.get(System.getProperty("user.home"),"Documents", APP_NAME);
        } else {
            // Na Linuxe a podobných systémoch sa dáta ukladajú do skrytého priečinka v domovskom adresári.
            return Paths.get(System.getProperty("user.home"),"." + APP_NAME.toLowerCase());
        }
    }

    public static Path appDataPath()
    {
        // Vracia hlavný dátový priečinok aplikácie.
        return APP_DATA_DIR;
    }

    public static Path configPath()
    {
        // Vracia cestu ku konfiguračnému súboru.
        return APP_DATA_DIR.resolve(CONFIG);
    }

    public static Path teachersPath()
    {
        // Vracia cestu k CSV súboru so zoznamom učiteľov.
        return APP_DATA_DIR.resolve(TEACHERS);
    }

    public static Path pdfPath()
    {
        // Vracia cestu k aktuálne vygenerovanému PDF.
        return APP_DATA_DIR.resolve(TEACHERS_PDF);
    }

    public static Path pngPath()
    {
        // Vracia cestu k PNG náhľadu aktuálneho PDF.
        return APP_DATA_DIR.resolve(TEACHERS_PREVIEW);
    }

    public static Path calendarPath()
    {
        // Vracia cestu ku kalendárovému CSV súboru.
        return APP_DATA_DIR.resolve(CALENDAR);
    }
}