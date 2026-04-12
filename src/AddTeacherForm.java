import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class AddTeacherForm {
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
    private JLabel middleNameLabel;
    private JTextField middleNameField;
    private JButton leaveButton;

    public void clearForm() {
        titleBeforeField.setText("");
        firstNameField.setText("");
        middleNameField.setText("");
        lastNameField.setText("");
        titleAfterField.setText("");

        mondayCheckBox.setSelected(true);
        tuesdayCheckBox.setSelected(true);
        wednesdayCheckBox.setSelected(true);
        thursdayCheckBox.setSelected(true);
        fridayCheckBox.setSelected(true);
    }

    public JTextField getFirstNameField() {
        return firstNameField;
    }

    public JTextField getLastNameField() {
        return lastNameField;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getLeaveButton() {
        return leaveButton;
    }

    public String getTitleBeforeText() {
        return titleBeforeField.getText().trim();
    }

    public String getFirstNameText() {
        return firstNameField.getText().trim();
    }

    public String getMiddleNameText() {
        return middleNameField.getText().trim();
    }

    public String getLastNameText() {
        return lastNameField.getText().trim();
    }

    public String getTitleAfterText() {
        return titleAfterField.getText().trim();
    }

    public boolean isMondaySelected() {
        return mondayCheckBox.isSelected();
    }

    public boolean isTuesdaySelected() {
        return tuesdayCheckBox.isSelected();
    }

    public boolean isWednesdaySelected() {
        return wednesdayCheckBox.isSelected();
    }

    public boolean isThursdaySelected() {
        return thursdayCheckBox.isSelected();
    }

    public boolean isFridaySelected() {
        return fridayCheckBox.isSelected();
    }

}