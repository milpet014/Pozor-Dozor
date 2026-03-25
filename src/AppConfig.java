import java.nio.file.Path;
import java.util.Properties;

public class AppConfig
{
    private static Path appConfigPath = AppPath.configPath();

    private static final String CONFIG_HEADER = "config file";

    private Properties p;

    public AppConfig()
    {
        AppFile.checkFile(AppPath.configPath(), CONFIG_HEADER);

        this.p = ManageConfig.loadConfigProperties(appConfigPath);

        ensureDefaults();
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

    public void ensureDefaults()
    {
        p.putIfAbsent("firstRun", "true");
        p.putIfAbsent("usingCloud", "false");
        p.putIfAbsent("schoolID", "none");
    }

    public void saveConfig()
    {
        ManageConfig.saveConfigProperties(appConfigPath, CONFIG_HEADER, p);
    }
}
