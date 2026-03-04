import java.nio.file.Path;
import java.util.Properties;

public class AppConfig
{
    private static Path appConfigPath = AppPath.getAppDataDir(Main.APP_NAME).resolve("config.cfg");

    private Properties p;

    public AppConfig()
    {
        CreateConfig.createConfig();

        this.p = ManageConfig.loadConfigProperties(appConfigPath);
    }

    public boolean isFirstRun()
    {
        return Boolean.parseBoolean(this.p.getProperty("firstRun", "true"));
    }

    public void setFirstRun(boolean value)
    {
        this.p.setProperty("firstRun", String.valueOf(value));
    }

    public boolean isUsingCloud()
    {
        return Boolean.parseBoolean(this.p.getProperty("usingCloud", "false"));
    }

    public void setUsingCloud(boolean value)
    {
        this.p.setProperty("usingCloud", String.valueOf(value));
    }

    public String getSchoolID()
    {
        return this.p.getProperty("schoolID", "none");
    }

    public void setSchoolID(String id)
    {
        this.p.setProperty("schoolID", id == null ? "none" : id);
    }



    public Properties raw()
    {
        return this.p;
    }

    public void setDefaults()
    {
        this.p.setProperty("firstRun", "true");
        this.p.setProperty("usingCloud", "false");
        this.p.setProperty("schoolID", "none");
    }

    public void saveConfig()
    {
        ManageConfig.saveConfigProperties(appConfigPath, Main.APP_NAME, p);
    }
}
