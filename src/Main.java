import knižnica.*;
import com.formdev.flatlaf.FlatLightLaf;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionThread;

import javax.swing.UIManager;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

public class Main
{
    private static final int WINDOW_SIZE_X = 594;
    private static final int WINDOW_SIZE_Y = 840;
    static final String HEADER = "Pozor Dozor";
    static final String APP_NAME = "pozor_dozor";

    private static boolean firstRun = true;

    private static PolozkaPonuky addTeacher;
    private static PolozkaPonuky editTeacher;
    private static PolozkaPonuky help;
    private static PolozkaPonuky documentation;

    private static GRobot worldRobot;
    private static Path pngPath = AppPath.appDataPath().resolve("teachers_preview.png");
    private static String pngPathStr = pngPath.normalize().toString();

    public static void main(String[] args)
    {
        worldRobot = new knižnica.GRobot(WINDOW_SIZE_X, WINDOW_SIZE_Y, HEADER);
        worldRobot.skry();

        Svet.zbal();
        Svet.vystred();
        Svet.upevni();

        applyFlatLafUI();

        refreshTeachersPdfPreview();

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

    private static void applyFlatLafUI()
    {
        try
        {
            UIManager.setLookAndFeel(new FlatLightLaf());
        }
        catch (Exception e)
        {
            Svet.sprava("Neoćakávana chyba: " + e, "Chyba");
        }
    }

    private static void refreshTeachersPdfPreview()
    {
        try
        {
            TeacherPdfGenerator teacherPdfGenerator = new TeacherPdfGenerator();
            teacherPdfGenerator.generateTeacherListPdf();
            teacherPdfGenerator.renderTeacherListPreview();

            showTeacherPreviewOnCanvas();
        }
        catch(IOException e)
        {
            Svet.sprava("Chyba, nepodarilo sa generovať PDF " + e, "chyba");
        }
    }

    private static void showTeacherPreviewOnCanvas()
    {
        Svet.uvolni(pngPathStr);

        Obrazok originalImage = Obrazok.precitaj(pngPathStr);

        double imageWidth = originalImage.sirka();
        double imageHeight = originalImage.vyska();

        originalImage.kresli(pngPathStr);

        double scale = Math.min((double) WINDOW_SIZE_X / imageWidth,(double) WINDOW_SIZE_Y / imageHeight);

        int scaledWidth = (int)Math.round(imageWidth * scale);
        int scaledHeight = (int)Math.round(imageHeight * scale);

        Image scaledImage = originalImage.getScaledInstance(
                scaledWidth,
                scaledHeight,
                Image.SCALE_SMOOTH
        );

        worldRobot.podlaha.vymazGrafiku();
        worldRobot.podlaha.obrazok(scaledImage);
        Svet.prekresli();
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