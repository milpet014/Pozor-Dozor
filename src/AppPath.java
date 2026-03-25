import java.nio.file.Path;
import java.nio.file.Paths;

public final class AppPath
{
    private static final String APP_NAME = Main.APP_NAME;
    private static final Path APP_DATA_DIR = resolveAppDataDir();

    private static final String CONFIG = "config.cfg";
    private static final String TEACHERS = "teachers.csv";

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
}
