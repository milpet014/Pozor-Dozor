import javax.swing.*;
import java.awt.*;

public class MonthSettingsForm
{
    private JPanel rootPanel;
    private JLabel monthLabel;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JCheckBox checkBox4;
    private JCheckBox checkBox5;
    private JCheckBox checkBox6;
    private JCheckBox checkBox7;
    private JCheckBox checkBox8;
    private JCheckBox checkBox9;
    private JCheckBox checkBox10;
    private JCheckBox checkBox11;
    private JCheckBox checkBox12;
    private JCheckBox checkBox13;
    private JCheckBox checkBox14;
    private JCheckBox checkBox15;
    private JPanel buttonsPanel;
    private JButton previousButton;
    private JButton nextButton;
    private JButton okButton;
    private JButton cancelButton;
    private JCheckBox checkBox16;
    private JCheckBox checkBox17;
    private JCheckBox checkBox18;
    private JCheckBox checkBox19;
    private JCheckBox checkBox20;
    private JCheckBox checkBox21;
    private JCheckBox checkBox22;
    private JCheckBox checkBox23;
    private JCheckBox checkBox24;
    private JCheckBox checkBox25;
    private JCheckBox checkBox26;
    private JCheckBox checkBox27;
    private JCheckBox checkBox28;
    private JCheckBox checkBox29;
    private JCheckBox checkBox30;
    private JCheckBox checkBox31;

    public JPanel getRootPanel()
    {
        return rootPanel;
    }

    public JButton getPreviousButton()
    {
        return previousButton;
    }

    public JButton getNextButton()
    {
        return nextButton;
    }

    public JButton getOkButton()
    {
        return okButton;
    }

    public JButton getCancelButton()
    {
        return cancelButton;
    }

    public void setMonthLabelText(String text)
    {
        monthLabel.setText(text);
    }

    public JCheckBox getCheckBox(int index)
    {
        switch(index)
        {
            case 0: return checkBox1;
            case 1: return checkBox2;
            case 2: return checkBox3;
            case 3: return checkBox4;
            case 4: return checkBox5;
            case 5: return checkBox6;
            case 6: return checkBox7;
            case 7: return checkBox8;
            case 8: return checkBox9;
            case 9: return checkBox10;
            case 10: return checkBox11;
            case 11: return checkBox12;
            case 12: return checkBox13;
            case 13: return checkBox14;
            case 14: return checkBox15;
            case 15: return checkBox16;
            case 16: return checkBox17;
            case 17: return checkBox18;
            case 18: return checkBox19;
            case 19: return checkBox20;
            case 20: return checkBox21;
            case 21: return checkBox22;
            case 22: return checkBox23;
            case 23: return checkBox24;
            case 24: return checkBox25;
            case 25: return checkBox26;
            case 26: return checkBox27;
            case 27: return checkBox28;
            case 28: return checkBox29;
            case 29: return checkBox30;
            case 30: return checkBox31;
            default: throw new IllegalArgumentException("Neplatný index checkboxu: " + index);
        }
    }

    public void clearCheckBoxes()
    {
        for(int i = 0; i < 31; i++)
        {
            JCheckBox checkBox = getCheckBox(i);

            checkBox.setText("");
            checkBox.setSelected(false);
            checkBox.setEnabled(false);
            checkBox.setVisible(false);
            checkBox.setForeground(Color.BLACK);
        }
    }
}