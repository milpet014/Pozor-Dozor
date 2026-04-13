import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ListTeachersFrame extends JFrame
{
    private final ListTeachersForm form = new ListTeachersForm();
    private final List<Teacher> teachers;

    private int currentPage = 0;
    private static final int PAGE_SIZE = 10;

    public ListTeachersFrame(List<Teacher> teachers)
    {

        this.teachers = teachers;

        setTitle("Upraviť záznam učiteľa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(form.getRootPanel());
        pack();
        setLocationRelativeTo(null);

        form.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        form.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextPage();
            }
        });

        form.getBackButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousPage();
            }
        });

        //UIStyles.styleSoftButton(form.getBackButton());
        //UIStyles.styleSoftButton(form.getNextButton());
        //UIStyles.styleSuccessButton(form.getOkButton());

        for(byte i  = 0; i < PAGE_SIZE; i++)
        {
            //UIStyles.stylePrimaryButton(form.getTeacherButton(i));

            final byte buttonIndex = i;
            form.getTeacherButton(buttonIndex).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openTeacher(buttonIndex);
                }
            });
        }

        refreshPage();
    }

    private void openTeacher(byte buttonIndex)
    {
        int teacherIndex = currentPage * PAGE_SIZE + buttonIndex;

        Teacher teacher = teachers.get(teacherIndex);

        EditTeacherFrame frame = new EditTeacherFrame(teacher);
        frame.setVisible(true);
    }

    private void refreshPage()
    {
        form.clearTeacherList();

        int startIndex = currentPage * PAGE_SIZE;

        for(byte i = 0; i < PAGE_SIZE; i++)
        {
            int teacherIndex = startIndex + i;

            if(teacherIndex < teachers.size())
            {
                Teacher teacher = teachers.get(teacherIndex);

                form.setTeacherLabelText(i, teacher.getFullName());
                form.setTeacherButtonEnabled(i, true);
            }
        }

        int totalPages = (teachers.size() + PAGE_SIZE - 1) / PAGE_SIZE;

        if(totalPages == 0) totalPages = 1;

        form.setPageLabelText("Strana " + (currentPage + 1) + " / " + totalPages);

        form.getBackButton().setEnabled(currentPage > 0);
        form.getNextButton().setEnabled((currentPage + 1) * PAGE_SIZE < teachers.size());
    }

    private void nextPage()
    {
        if((currentPage + 1) * PAGE_SIZE < teachers.size())
        {
            currentPage++;
            refreshPage();
        }
    }

    private void previousPage()
    {
        if(currentPage > 0)
        {
            currentPage--;
            refreshPage();
        }
    }
}
