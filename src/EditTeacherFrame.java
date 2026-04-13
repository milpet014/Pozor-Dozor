import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditTeacherFrame extends JFrame
{
    private final AddTeacherForm form = new AddTeacherForm();
    private final Teacher teacher;

    public EditTeacherFrame(Teacher teacher)
    {
        this.teacher = teacher;

        setTitle("Úprava záznamu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(form.getRootPanel());
        pack();
        setLocationRelativeTo(null);

        form.getLeaveButton().setVisible(false);
        form.getSaveButton().setText("Uložiť");
        form.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //UIStyles.stylePrimaryButton(form.getSaveButton());
        //UIStyles.styleDangerButton(form.getDeleteButton());
        //UIStyles.styleDangerButton(form.getCancelButton());

        fillForm();
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
