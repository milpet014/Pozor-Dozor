import java.nio.file.Path;
import java.util.Properties;

public class AppConfig
{
    private static Path appConfigPath = AppPath.configPath();

    private static final String CONFIG_HEADER = "config file";

    private static final String DEFAULT_PDF_FOOTNOTE_TEXT = "Začiatok služby 7:20 hod. – 7:45 hod.\n" + "Veľká prestávka 10:10 hod. – 10:25 hod.";

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

    public String getLastGeneratedMonth()
    {
        return this.p.getProperty("lastGeneratedMonth", "none");
    }

    public void setLastGeneratedMonth(String value)
    {
        this.p.setProperty("lastGeneratedMonth", value == null ? "none" : value);
    }

    public String getLastGeneratedCalendarSignature()
    {
        return this.p.getProperty("lastGeneratedCalendarSignature", "none");
    }

    public void setLastGeneratedCalendarSignature(String value)
    {
        this.p.setProperty("lastGeneratedCalendarSignature", value == null ? "none" : value);
    }

    public String getPdfFootnoteText()
    {
        return this.p.getProperty("pdfFootnoteText", DEFAULT_PDF_FOOTNOTE_TEXT);
    }

    public void setPdfFootnoteText(String value)
    {
        if(value == null || value.trim().isEmpty())
        {
            this.p.setProperty("pdfFootnoteText", DEFAULT_PDF_FOOTNOTE_TEXT);
        }
        else
        {
            this.p.setProperty("pdfFootnoteText", value.trim());
        }
    }

    public float getPdfTitleFontSize()
    {
        return getFloatProperty("pdfTitleFontSize", 18.0f);
    }

    public void setPdfTitleFontSize(float value)
    {
        this.p.setProperty("pdfTitleFontSize", String.valueOf(value));
    }

    public float getPdfDutyFontSize()
    {
        return getFloatProperty("pdfDutyFontSize", 9.0f);
    }

    public void setPdfDutyFontSize(float value)
    {
        this.p.setProperty("pdfDutyFontSize", String.valueOf(value));
    }

    public float getPdfFootnoteFontSize()
    {
        return getFloatProperty("pdfFootnoteFontSize", 8.0f);
    }

    public void setPdfFootnoteFontSize(float value)
    {
        this.p.setProperty("pdfFootnoteFontSize", String.valueOf(value));
    }

    private float getFloatProperty(String key, float defaultValue)
    {
        try
        {
            String value = this.p.getProperty(key, String.valueOf(defaultValue));
            value = value.replace(",", ".");
            return Float.parseFloat(value);
        }
        catch(Exception e)
        {
            return defaultValue;
        }
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
        p.putIfAbsent("lastGeneratedMonth", "none");
        p.putIfAbsent("lastGeneratedCalendarSignature", "none");
        p.putIfAbsent("lastGeneratedCalendarSignature", "none");
        p.putIfAbsent("pdfFootnoteText", DEFAULT_PDF_FOOTNOTE_TEXT);
        p.putIfAbsent("pdfTitleFontSize", String.valueOf(SettingsFrame.DEFAULT_TITLE_FONT_SIZE));
        p.putIfAbsent("pdfDutyFontSize", String.valueOf(SettingsFrame.DEFAULT_DUTY_FONT_SIZE));
        p.putIfAbsent("pdfFootnoteFontSize", String.valueOf(SettingsFrame.DEFAULT_FOOTNOTE_FONT_SIZE));
    }

    public void saveConfig()
    {
        ManageConfig.saveConfigProperties(appConfigPath, CONFIG_HEADER, p);
    }
}
