import java.nio.file.Path;
import java.nio.file.Paths;

public final class AppPath
{
    private static final String APP_NAME = Main.APP_NAME;
    private static final Path APP_DATA_DIR = resolveAppDataDir();

    private static final String CONFIG = "config.cfg";
    private static final String TEACHERS = "teachers.csv";
    private static final String TEACHERS_PDF = "teachers.pdf";
    private static final String TEACHERS_PREVIEW = "teachers_preview.png";
    private static final String CALENDAR = "calendar.csv";

    private AppPath(){};

    public static Path resolveAppDataDir()
    {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return Paths.get(System.getProperty("user.home"),"Documents", APP_NAME);
        } else {
            return Paths.get(System.getProperty("user.home"),"." + APP_NAME.toLowerCase());
        }
    }

    public static Path appDataPath()
    {
        return APP_DATA_DIR;
    }

    public static Path configPath()
    {
        return APP_DATA_DIR.resolve(CONFIG);
    }

    public static Path teachersPath()
    {
        return APP_DATA_DIR.resolve(TEACHERS);
    }

    public static Path pdfPath()
    {
        return APP_DATA_DIR.resolve(TEACHERS_PDF);
    }

    public static Path pngPath()
    {
        return APP_DATA_DIR.resolve(TEACHERS_PREVIEW);
    }

    public static Path calendarPath()
    {
        return APP_DATA_DIR.resolve(CALENDAR);
    }
}
