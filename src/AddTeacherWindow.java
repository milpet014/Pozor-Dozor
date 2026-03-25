import knižnica.*;

import java.awt.*;

public class AddTeacherWindow
{
    private static final int WINDOW_SIZE_X = 200;
    private static final int WINDOW_SIZE_Y = 100;

    private Obrazok background;
    private Okno window;

    private PoznamkovyBlok firstName;
    private Tlacidlo btnMonday;

    private boolean canMonday = true;

    public AddTeacherWindow()
    {
        this.background = new Obrazok(WINDOW_SIZE_X, WINDOW_SIZE_Y);

        this.window = new Okno(background, "Pridať učiteľa", true);

        btnMonday = new Tlacidlo("Pondelok");
        btnMonday.polohaX(-200);
        btnMonday.polohaY(20);
        btnMonday.šírka(120);
        btnMonday.výška(40);

        btnMonday.prenes(window, true);

        updateDayButtons();
    }


    private void updateDayButtons()
    {
        if(canMonday)
        {
            btnMonday.setBackground(Color.GREEN);
        }
        else
        {
            btnMonday.setBackground(Color.RED);
        }
    }

    public void toggleMonday()
    {
        canMonday = !canMonday;
        updateDayButtons();
    }

    public Tlacidlo getBtnMonday()
    {
        return btnMonday;
    }

    public Okno getAddTeacherWindow()
    {
        return window;
    }
}
