import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class AddTeacherForm {
    // Hlavný panel formulára vytvorený cez IntelliJ GUI Designer.
    private JPanel rootPanel;

    // Popisy textových polí.
    private JLabel titleBeforeLabel;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel titleAfterLabel;

    // Textové polia pre základné údaje učiteľa.
    private JTextField titleBeforeField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField titleAfterField;

    // Informačný text vo formulári.
    private JLabel infoLabel;

    // Checkboxy dostupnosti učiteľa podľa dní v týždni.
    private JCheckBox tuesdayCheckBox;
    private JCheckBox mondayCheckBox;
    private JCheckBox wednesdayCheckBox;
    private JCheckBox thursdayCheckBox;
    private JCheckBox fridayCheckBox;

    // Ovládacie tlačidlá formulára.
    private JButton saveButton;
    private JButton cancelButton;

    // Pole pre stredné meno.
    private JLabel middleNameLabel;
    private JTextField middleNameField;

    // Tlačidlo s doplnkovou akciou podľa režimu formulára.
    private JButton leaveButton;

    public void clearForm() {
        // Vyčistenie osobných údajov po uložení učiteľa.
        // Tituly sa zámerne nemažú, aby sa pri hromadnom zadávaní dali ponechať.
        //titleBeforeField.setText("");
        firstNameField.setText("");
        middleNameField.setText("");
        lastNameField.setText("");
        //titleAfterField.setText("");

        // Po vyčistení formulára sa učiteľ predvolene nastaví ako dostupný každý pracovný deň.
        mondayCheckBox.setSelected(true);
        tuesdayCheckBox.setSelected(true);
        wednesdayCheckBox.setSelected(true);
        thursdayCheckBox.setSelected(true);
        fridayCheckBox.setSelected(true);
    }

    public void setTitleBeforeText(String text)
    {
        // Nastaví titul pred menom.
        titleBeforeField.setText(text);
    }

    public void setFirstNameText(String text)
    {
        // Nastaví meno učiteľa.
        firstNameField.setText(text);
    }

    public void setMiddleNameText(String text)
    {
        // Nastaví stredné meno učiteľa.
        middleNameField.setText(text);
    }

    public void setLastNameText(String text)
    {
        // Nastaví priezvisko učiteľa.
        lastNameField.setText(text);
    }

    public void setTitleAfterText(String text)
    {
        // Nastaví titul za menom.
        titleAfterField.setText(text);
    }

    public void setMondaySelected(boolean selected)
    {
        // Nastaví dostupnosť v pondelok.
        mondayCheckBox.setSelected(selected);
    }

    public void setTuesdaySelected(boolean selected)
    {
        // Nastaví dostupnosť v utorok.
        tuesdayCheckBox.setSelected(selected);
    }

    public void setWednesdaySelected(boolean selected)
    {
        // Nastaví dostupnosť v stredu.
        wednesdayCheckBox.setSelected(selected);
    }

    public void setThursdaySelected(boolean selected)
    {
        // Nastaví dostupnosť vo štvrtok.
        thursdayCheckBox.setSelected(selected);
    }

    public void setFridaySelected(boolean selected)
    {
        // Nastaví dostupnosť v piatok.
        fridayCheckBox.setSelected(selected);
    }

    public JTextField getFirstNameField() {
        // Vracia pole mena, napríklad na nastavenie focusu.
        return firstNameField;
    }

    public JTextField getLastNameField() {
        // Vracia pole priezviska, napríklad na nastavenie focusu.
        return lastNameField;
    }

    public JTextField getTitleBeforeField() {
        // Vracia pole titulu pred menom.
        return titleBeforeField;
    }

    public JPanel getRootPanel() {
        // Vracia hlavný panel formulára.
        return rootPanel;
    }

    public JButton getCancelButton() {
        // Vracia tlačidlo zrušenia.
        return cancelButton;
    }

    public JButton getSaveButton() {
        // Vracia tlačidlo uloženia.
        return saveButton;
    }

    public JButton getLeaveButton() {
        // Vracia doplnkové tlačidlo formulára.
        return leaveButton;
    }

    public String getTitleBeforeText() {
        // Vracia titul pred menom bez okolitých medzier.
        return titleBeforeField.getText().trim();
    }

    public String getFirstNameText() {
        // Vracia meno bez okolitých medzier.
        return firstNameField.getText().trim();
    }

    public String getMiddleNameText() {
        // Vracia stredné meno bez okolitých medzier.
        return middleNameField.getText().trim();
    }

    public String getLastNameText() {
        // Vracia priezvisko bez okolitých medzier.
        return lastNameField.getText().trim();
    }

    public String getTitleAfterText() {
        // Vracia titul za menom bez okolitých medzier.
        return titleAfterField.getText().trim();
    }

    public boolean isMondaySelected() {
        // Vracia dostupnosť učiteľa v pondelok.
        return mondayCheckBox.isSelected();
    }

    public boolean isTuesdaySelected() {
        // Vracia dostupnosť učiteľa v utorok.
        return tuesdayCheckBox.isSelected();
    }

    public boolean isWednesdaySelected() {
        // Vracia dostupnosť učiteľa v stredu.
        return wednesdayCheckBox.isSelected();
    }

    public boolean isThursdaySelected() {
        // Vracia dostupnosť učiteľa vo štvrtok.
        return thursdayCheckBox.isSelected();
    }

    public boolean isFridaySelected() {
        // Vracia dostupnosť učiteľa v piatok.
        return fridayCheckBox.isSelected();
    }

}