import knižnica.Svet;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditTeacherFrame extends JFrame
{
    private enum FocusField
    {
        FIRST_NAME,
        LAST_NAME
    }

    private final AddTeacherForm form = new AddTeacherForm();
    private final Teacher teacher;

    public EditTeacherFrame(Teacher teacher)
    {
        this.teacher = teacher;

        setTitle("Úprava záznamu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icons/pozor-dozor.png")).getImage());
        setContentPane(form.getRootPanel());
        pack();
        setLocationRelativeTo(null);

        form.getLeaveButton().setText("Vymazať");
        form.getSaveButton().setText("Uložiť");

        form.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        form.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTeacherChanges();
            }
        });

        form.getLeaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTeacher();
            }
        });

        fillForm();
    }

    private void deleteTeacher()
    {
        int confirm = Svet.otazka(
                "Naozaj chcete vymazať učiteľa:\n" + teacher.getFullName() + "?",
                "Vymazať učiteľa"
        );

        if(confirm != 0)
        {
            return;
        }

        TeacherRepository teacherRepository = new TeacherRepository();
        boolean deleted = teacherRepository.deleteTeacherById(teacher.getId());

        if(deleted)
        {
            Main.invalidatePendingDuties();

            Svet.sprava("Učiteľ bol vymazaný.", "Hotovo");
            dispose();
        }
        else
        {
            Svet.sprava("Nepodarilo sa vymazať učiteľa.", "Chyba");
        }
    }

    private void saveTeacherChanges()
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
            focus(FocusField.FIRST_NAME);
            return;
        }
        if(lastName.isEmpty())
        {
            Svet.sprava("Priezvisko je povinné", "Chyba");
            focus(FocusField.LAST_NAME);
            return;
        }

        Teacher updatedTeacher = new Teacher(
                teacher.getId(),
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

        updatedTeacher.setDutyCount(teacher.getDutyCount());
        updatedTeacher.setLastDutyWeek(teacher.getLastDutyWeek());

        TeacherRepository teacherRepository = new TeacherRepository();
        boolean updated = teacherRepository.updateTeacher(updatedTeacher);

        if(updated)
        {
            Main.invalidatePendingDuties();

            Svet.sprava("Upravený učiteľ: " + updatedTeacher.getFullName() + ", id: " + updatedTeacher.getId() + ".", "Pridaný učiteľ");
            dispose();
        }
        else
        {
            Svet.sprava("Nepodarilo sa uložiť učiteľa", "CHYBA");
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
                    case FIRST_NAME:
                        form.getFirstNameField().requestFocusInWindow();
                        break;
                    case LAST_NAME:
                        form.getLastNameField().requestFocusInWindow();
                }
            }
        });
    }

    private void fillForm()
    {
        form.setTitleBeforeText(teacher.getDegreeBeforeName());
        form.setFirstNameText(teacher.getFirstName());
        form.setMiddleNameText(teacher.getMiddleName());
        form.setLastNameText(teacher.getLastName());
        form.setTitleAfterText(teacher.getDegreeAfterName());

        form.setMondaySelected(teacher.isCanMonday());
        form.setTuesdaySelected(teacher.isCanTuesday());
        form.setWednesdaySelected(teacher.isCanWednesday());
        form.setThursdaySelected(teacher.isCanThursday());
        form.setFridaySelected(teacher.isCanFriday());
    }
}
