import java.nio.file.Path;
import java.util.Properties;

public class AppConfig
{
    // Cesta ku konfiguračnému súboru aplikácie.
    private static Path appConfigPath = AppPath.configPath();

    // Hlavička konfiguračného súboru.
    private static final String CONFIG_HEADER = "config file";

    // Predvolený text poznámky pod čiarou v PDF.
    private static final String DEFAULT_PDF_FOOTNOTE_TEXT = "Začiatok služby 7:20 hod. – 7:45 hod.\n" + "Veľká prestávka 10:10 hod. – 10:25 hod.";

    // Vlastnosti načítané z config.cfg.
    private Properties p;

    public AppConfig()
    {
        // Kontrola existencie a základnej platnosti konfiguračného súboru.
        AppFile.checkFile(AppPath.configPath(), CONFIG_HEADER);

        // Načítanie konfigurácie zo súboru.
        this.p = ManageConfig.loadConfigProperties(appConfigPath);

        // Doplnenie chýbajúcich predvolených hodnôt.
        ensureDefaults();
    }

    public boolean isFirstRun()
    {
        // Vracia informáciu, či ide o prvé spustenie aplikácie.
        return Boolean.parseBoolean(this.p.getProperty("firstRun", "true"));
    }

    public void setFirstRun(boolean value)
    {
        // Nastaví stav prvého spustenia.
        this.p.setProperty("firstRun", String.valueOf(value));
    }

    public boolean isUsingCloud()
    {
        // Vracia informáciu, či sú povolené cloudové služby.
        return Boolean.parseBoolean(this.p.getProperty("usingCloud", "false"));
    }

    public void setUsingCloud(boolean value)
    {
        // Nastaví používanie cloudových služieb.
        this.p.setProperty("usingCloud", String.valueOf(value));
    }

    public String getSchoolID()
    {
        // Vracia identifikátor školy používaný aj pri cloudovej zálohe.
        return this.p.getProperty("schoolID", "none");
    }

    public void setSchoolID(String id)
    {
        // Uloží identifikátor školy.
        this.p.setProperty("schoolID", id == null ? "none" : id);
    }

    public String getLastGeneratedMonth()
    {
        // Vracia posledný mesiac, pre ktorý bol rozpis potvrdený.
        return this.p.getProperty("lastGeneratedMonth", "none");
    }

    public void setLastGeneratedMonth(String value)
    {
        // Uloží posledný potvrdený mesiac.
        this.p.setProperty("lastGeneratedMonth", value == null ? "none" : value);
    }

    public String getLastGeneratedCalendarSignature()
    {
        // Vracia podpis kalendára pri poslednom potvrdenom rozpisu.
        return this.p.getProperty("lastGeneratedCalendarSignature", "none");
    }

    public void setLastGeneratedCalendarSignature(String value)
    {
        // Uloží podpis kalendára pri potvrdení rozpisu.
        this.p.setProperty("lastGeneratedCalendarSignature", value == null ? "none" : value);
    }

    public String getPdfFootnoteText()
    {
        // Vracia text poznámky pod čiarou v PDF.
        return this.p.getProperty("pdfFootnoteText", DEFAULT_PDF_FOOTNOTE_TEXT);
    }

    public void setPdfFootnoteText(String value)
    {
        // Ak je text prázdny, použije sa predvolená poznámka.
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
        // Vracia veľkosť písma nadpisu v PDF.
        return getFloatProperty("pdfTitleFontSize", 18.0f);
    }

    public void setPdfTitleFontSize(float value)
    {
        // Uloží veľkosť písma nadpisu v PDF.
        this.p.setProperty("pdfTitleFontSize", String.valueOf(value));
    }

    public float getPdfDutyFontSize()
    {
        // Vracia veľkosť písma zoznamu dozorov v PDF.
        return getFloatProperty("pdfDutyFontSize", 9.0f);
    }

    public void setPdfDutyFontSize(float value)
    {
        // Uloží veľkosť písma zoznamu dozorov v PDF.
        this.p.setProperty("pdfDutyFontSize", String.valueOf(value));
    }

    public float getPdfFootnoteFontSize()
    {
        // Vracia veľkosť písma poznámky pod čiarou v PDF.
        return getFloatProperty("pdfFootnoteFontSize", 8.0f);
    }

    public void setPdfFootnoteFontSize(float value)
    {
        // Uloží veľkosť písma poznámky pod čiarou v PDF.
        this.p.setProperty("pdfFootnoteFontSize", String.valueOf(value));
    }

    private float getFloatProperty(String key, float defaultValue)
    {
        // Bezpečné načítanie desatinného čísla z konfigurácie.
        try
        {
            String value = this.p.getProperty(key, String.valueOf(defaultValue));
            value = value.replace(",", ".");
            return Float.parseFloat(value);
        }
        catch(Exception e)
        {
            // Pri neplatnej hodnote sa použije predvolená hodnota.
            return defaultValue;
        }
    }

    public Properties raw()
    {
        // Vracia celé Properties pre prípad nízkoúrovňovej práce s konfiguráciou.
        return this.p;
    }

    public void ensureDefaults()
    {
        // Doplnenie základných hodnôt, ak v config.cfg ešte neexistujú.
        p.putIfAbsent("firstRun", "true");
        p.putIfAbsent("usingCloud", "false");
        p.putIfAbsent("schoolID", "none");
        p.putIfAbsent("lastGeneratedMonth", "none");
        p.putIfAbsent("lastGeneratedCalendarSignature", "none");
        p.putIfAbsent("lastGeneratedCalendarSignature", "none");

        // Doplnenie predvolených hodnôt pre vzhľad PDF.
        p.putIfAbsent("pdfFootnoteText", DEFAULT_PDF_FOOTNOTE_TEXT);
        p.putIfAbsent("pdfTitleFontSize", String.valueOf(SettingsFrame.DEFAULT_TITLE_FONT_SIZE));
        p.putIfAbsent("pdfDutyFontSize", String.valueOf(SettingsFrame.DEFAULT_DUTY_FONT_SIZE));
        p.putIfAbsent("pdfFootnoteFontSize", String.valueOf(SettingsFrame.DEFAULT_FOOTNOTE_FONT_SIZE));
    }

    public void saveConfig()
    {
        // Uloženie konfigurácie späť do config.cfg.
        ManageConfig.saveConfigProperties(appConfigPath, CONFIG_HEADER, p);
    }
}