import knižnica.Svet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTeacherFrame extends JFrame
{
    private final AddTeacherForm form;
    Teacher newTeacher;

    public AddTeacherFrame()
    {
        this.form = new AddTeacherForm();

        setTitle("Pridať učiteľa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
    }

    public void createTeacher(boolean leave)
    {
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
            focusFirstNameField();
            return;
        }
        if(lastName.isEmpty())
        {
            Svet.sprava("Priezvisko je povinné", "Chyba");
            focusLastNameField();
            return;
        }


        newTeacher = new Teacher(
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

        Svet.sprava("Pridaný učiteľ: " + newTeacher.getFullName() + ".", "Pridaný učiteľ");

        if(leave)
        {
            dispose();
        }
        else
        {
            form.clearForm();
            focusFirstNameField();
        }
    }

    private void focusFirstNameField()
    {
        focus();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                form.getFirstNameField().requestFocusInWindow();
            }
        });
    }

    private void focusLastNameField()
    {
        focus();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                form.getLastNameField().requestFocusInWindow();
            }
        });
    }

    private void focus()
    {
        setState(JFrame.NORMAL);
        toFront();
        requestFocus();
    }

    public Teacher getNewTeacher()
    {
        return this.newTeacher;
    }
}
