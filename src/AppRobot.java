import knižnica.GRobot;
import knižnica.UdajeUdalosti;

import javax.swing.SwingUtilities;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AppRobot extends GRobot
{
    private AddTeacherFrame addTeacherFrame;

    public AppRobot(){};

    @Override
    public void volbaPolozkyPonuky()
    {
        if(UdajeUdalosti.polozkaPonuky() == Main.getAddTeacher())
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run() {
                    openAddTeacherFrame();
                }
            });
        }
    }

    private void openAddTeacherFrame()
    {
        if(addTeacherFrame == null)
        {
            addTeacherFrame = new AddTeacherFrame();

            addTeacherFrame.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(WindowEvent e)
                {
                    addTeacherFrame = null;
                }
            });

            addTeacherFrame.setVisible(true);
        }
        else
        {
            addTeacherFrame.setState(Frame.NORMAL);
            addTeacherFrame.toFront();
            addTeacherFrame.requestFocus();
        }
    }
}


