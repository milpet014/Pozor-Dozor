import java.nio.file.Path;
import java.nio.file.Paths;

public final class AppPath
{
    private AppPath(){};

    public static Path getAppDataDir(String appDataDir)
    {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return Paths.get(System.getProperty("user.home"),"Documents", appDataDir);
        } else {
            return Paths.get(System.getProperty("user.home"),"." + appDataDir.toLowerCase());
        }
    }
}
