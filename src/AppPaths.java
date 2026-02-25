import java.nio.file.*;

public final class AppPaths
{
    private AppPaths(){}

    private static Path getAppDataDir(String appDataDir)
    {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return Paths.get(System.getProperty("user.home"),"Documents", appDataDir);
        } else {
            return Paths.get(System.getProperty("user.home"),"." + appDataDir.toLowerCase());
        }
    }
}
