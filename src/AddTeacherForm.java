import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTeacherForm
{
    private JPanel rootPanel;
    private JLabel titleBeforeLabel;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel titleAfterLabel;
    private JTextField titleBeforeField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField titleAfterField;
    private JLabel infoLabel;
    private JCheckBox tuesdayCheckBox;
    private JCheckBox mondayCheckBox;
    private JCheckBox wednesdayCheckBox;
    private JCheckBox thursdayCheckBox;
    private JCheckBox fridayCheckBox;
    private JButton saveButton;
    private JButton cancelButton;

    public JPanel getRootPanel()
    {
        return rootPanel;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}
