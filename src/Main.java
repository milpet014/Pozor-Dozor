import knižnica.*;

public class Main
{
    private static final int WINDOW_SIZE_X = 400;
    private static final int WINDOW_SIZE_Y = 200;
    static final String HEADER = "Pozor Dozor";
    static final String APP_NAME = "pozor_dozor";

    private static boolean firstRun = true;

    private static PolozkaPonuky addTeacher;

    public static void main(String[] args)
    {
        System.out.println("DISPLAY=" + System.getenv("DISPLAY"));
        System.out.println("WAYLAND_DISPLAY=" + System.getenv("WAYLAND_DISPLAY"));
        System.out.println("XDG_SESSION_TYPE=" + System.getenv("XDG_SESSION_TYPE"));
        System.out.println("headless=" + java.awt.GraphicsEnvironment.isHeadless());

        GRobot svet = new knižnica.GRobot(WINDOW_SIZE_X, WINDOW_SIZE_Y, HEADER);

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

        new AppRobot();
    }

    public static PolozkaPonuky getAddTeacher()
    {
        return addTeacher;
    }
}