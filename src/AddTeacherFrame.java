import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTeacherFrame extends JFrame
{
    private final AddTeacherForm form;

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
    }
}
