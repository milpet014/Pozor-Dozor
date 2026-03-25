import knižnica.*;

public class AppRobot extends GRobot
{
    private AddTeacherWindow addTeacherWindow;

    public AppRobot(){};

    @Override
    public void volbaPolozkyPonuky()
    {
        if(UdajeUdalosti.polozkaPonuky() == Main.getAddTeacher())
        {
            addTeacherWindow = new AddTeacherWindow();
        }
    }

    @Override
    public void volbaTlacidla()
    {
        if(UdajeUdalosti.tlacidlo() == addTeacherWindow.getBtnMonday())
        {
            addTeacherWindow.toggleMonday();
        }
    }
}


