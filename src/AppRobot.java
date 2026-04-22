import knižnica.GRobot;
import knižnica.Svet;
import knižnica.UdajeUdalosti;

import javax.swing.SwingUtilities;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class AppRobot extends GRobot
{
    private AddTeacherFrame addTeacherFrame;
    private ListTeachersFrame listTeachersFrame;

    public AppRobot()
    {
        skry();
    };

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

        if(UdajeUdalosti.polozkaPonuky() == Main.getEditTeacher())
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run() {
                    TeacherRepository teacherRepository = new TeacherRepository();
                    List<Teacher> teachers = teacherRepository.loadTeachers();
                    openListTeacherFrame(teachers);
                }
            });
        }

        if(UdajeUdalosti.polozkaPonuky() == Main.getHelp())
        {
            Svet.sprava("Verzia: Alpha-0.4", "O programe");
        }

        if(UdajeUdalosti.polozkaPonuky() == Main.getDocumentation())
        {
            Svet.sprava("It Works", "Placeholder");
        }
    }

    public void openListTeacherFrame(List<Teacher> teachers)
    {
        if(listTeachersFrame == null)
        {
            listTeachersFrame = new ListTeachersFrame(teachers);

            listTeachersFrame.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(WindowEvent e)
                {
                    listTeachersFrame = null;
                }
            });

            listTeachersFrame.setVisible(true);
        }
        else
        {
            listTeachersFrame.setState(Frame.NORMAL);
            listTeachersFrame.toFront();
            listTeachersFrame.requestFocus();
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


