import knižnica.*;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

public class Main
{
    private static final int WINDOW_SIZE_X = 400;
    private static final int WINDOW_SIZE_Y = 200;
    static final String HEADER = "Pozor Dozor";
    static final String APP_NAME = "pozor_dozor";

    private static boolean firstRun = true;

    private static PolozkaPonuky addTeacher;
    private static PolozkaPonuky editTeacher;
    private static PolozkaPonuky help;
    private static PolozkaPonuky documentation;

    public static void main(String[] args)
    {
        GRobot svet = new knižnica.GRobot(WINDOW_SIZE_X, WINDOW_SIZE_Y, HEADER);

        try
        {
            UIManager.setLookAndFeel(new FlatLightLaf());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        AppConfig config = new AppConfig();

        firstRun = config.isFirstRun();

        if(firstRun)
        {
            FirstRunWizard.firstRunWizard();

            config.setFirstRun(FirstRunWizard.getFirstRun());
            config.setUsingCloud(FirstRunWizard.getUsingCloud());
            config.setSchoolID(FirstRunWizard.getSchoolID());

            config.saveConfig();
        }

        addTeacher = new PolozkaPonuky("Pridať učiteľa");
        editTeacher = new PolozkaPonuky("Upraviť záznamy");
        help = new PolozkaPonuky("O programe");
        documentation = new PolozkaPonuky("Dokumentácia");

        new AppRobot();
    }

    public static PolozkaPonuky getAddTeacher()
    {
        return addTeacher;
    }

    public static PolozkaPonuky getEditTeacher()
    {
        return editTeacher;
    }

    public static PolozkaPonuky getHelp()
    {
        return help;
    }

    public static PolozkaPonuky getDocumentation()
    {
        return documentation;
    }
}