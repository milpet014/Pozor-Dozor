import knižnica.Svet;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTeacherFrame extends JFrame
{
    private enum FocusField
    {
        TITLE_BEFORE,
        FIRST_NAME,
        LAST_NAME
    }

    private Teacher newTeacher;
    private final TeacherRepository teacherRepository = new TeacherRepository();

    private final AddTeacherForm form = new AddTeacherForm();

    public AddTeacherFrame()
    {
        setTitle("Pridať učiteľa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icons/pozor-dozor.png")).getImage());
        setContentPane(form.getRootPanel());
        pack();
        setLocationRelativeTo(null);

        form.getCancelButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });

        form.getSaveButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                createTeacher(false);
            }
        });

        form.getLeaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTeacher(true);
            }
        });

        getRootPane().setDefaultButton(form.getSaveButton());
    }

    public void createTeacher(boolean leave)
    {
        int newId = teacherRepository.getNextId();

        if(newId == -1)
        {
            Svet.sprava("Neznáma chyba pri prideľovaní ID", "CHYBA");
            return;
        }

        String degreeBeforeName = form.getTitleBeforeText();
        String firstName = form.getFirstNameText();
        String middleName = form.getMiddleNameText();
        String lastName = form.getLastNameText();
        String degreeAfterName = form.getTitleAfterText();

        boolean canMonday = form.isMondaySelected();
        boolean canTuesday = form.isTuesdaySelected();
        boolean canWednesday = form.isWednesdaySelected();
        boolean canThursday = form.isThursdaySelected();
        boolean canFriday = form.isFridaySelected();

        if(firstName.isEmpty())
        {
            Svet.sprava("Meno je povinné", "Chyba");
            focus(FocusField.FIRST_NAME);
            return;
        }
        if(lastName.isEmpty())
        {
            Svet.sprava("Priezvisko je povinné", "Chyba");
            focus(FocusField.LAST_NAME);
            return;
        }

        newTeacher = new Teacher(
                newId,
                degreeBeforeName,
                firstName,
                middleName,
                lastName,
                degreeAfterName,
                canMonday,
                canTuesday,
                canWednesday,
                canThursday,
                canFriday
        );

        boolean saved = teacherRepository.saveTeacher(newTeacher);

        if(!saved)
        {
            Svet.sprava("Nepodarilo sa uložiť učiteľa", "CHYBA");
            return;
        }

        Main.invalidatePendingDuties();

        Svet.sprava("Pridaný učiteľ: " + newTeacher.getFullName() + ", id: " + newTeacher.getId() + ".", "Pridaný učiteľ");

        if(leave)
        {
            dispose();
        }
        else
        {
            form.clearForm();
            focus(FocusField.TITLE_BEFORE);
        }
    }

    private void focus(FocusField field)
    {
        setState(JFrame.NORMAL);
        toFront();
        requestFocus();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                switch(field)
                {
                    case TITLE_BEFORE:
                        form.getTitleBeforeField().requestFocusInWindow();
                        break;
                    case FIRST_NAME:
                        form.getFirstNameField().requestFocusInWindow();
                        break;
                    case LAST_NAME:
                        form.getLastNameField().requestFocusInWindow();
                }
            }
        });
    }

    public Teacher getNewTeacher()
    {
        return this.newTeacher;
    }
}
