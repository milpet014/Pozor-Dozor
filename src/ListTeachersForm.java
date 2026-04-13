import javax.swing.*;

public class ListTeachersForm {
    private JButton nextButton;
    private JButton backButton;
    private JButton teacherButton1;
    private JButton teacherButton2;
    private JButton teacherButton3;
    private JButton teacherButton4;
    private JButton teacherButton5;
    private JButton teacherButton6;
    private JButton teacherButton7;
    private JButton teacherButton8;
    private JButton teacherButton9;
    private JButton teacherButton10;
    private JPanel rootPanel;
    private JLabel teacherLabel1;
    private JLabel teacherLabel2;
    private JLabel teacherLabel3;
    private JLabel teacherLabel4;
    private JLabel teacherLabel5;
    private JLabel teacherLabel6;
    private JLabel teacherLabel7;
    private JLabel teacherLabel8;
    private JLabel teacherLabel9;
    private JLabel teacherLabel10;
    private JButton okButton;
    private JLabel pageLabel;

    public JPanel getRootPanel()
    {
        return rootPanel;
    }

    public JButton getNextButton()
    {
        return nextButton;
    }

    public JButton getBackButton()
    {
        return backButton;
    }

    public JButton getOkButton()
    {
        return okButton;
    }

    public JLabel getPageLabel()
    {
        return pageLabel;
    }

    public void setPageLabelText(String text)
    {
        pageLabel.setText(text);
    }

    public JButton getTeacherButton(byte index)
    {
        switch(index)
        {
            case 0:
                return teacherButton1;
            case 1:
                return teacherButton2;
            case 2:
                return teacherButton3;
            case 3:
                return teacherButton4;
            case 4:
                return teacherButton5;
            case 5:
                return teacherButton6;
            case 6:
                return teacherButton7;
            case 7:
                return teacherButton8;
            case 8:
                return teacherButton9;
            case 9:
                return teacherButton10;
            default:
                throw new IllegalArgumentException("Neplatný index labelu: " + index);
        }
    }

    public JLabel getTeacherLabelText(byte index)
    {
        switch (index)
        {
            case 0:
                return teacherLabel1;
            case 1:
                return teacherLabel2;
            case 2:
                return teacherLabel3;
            case 3:
                return teacherLabel4;
            case 4:
                return teacherLabel5;
            case 5:
                return teacherLabel6;
            case 6:
                return teacherLabel7;
            case 7:
                return teacherLabel8;
            case 8:
                return teacherLabel9;
            case 9:
                return teacherLabel10;
            default:
                throw new IllegalArgumentException("Neplatný index labelu: " + index);
        }
    }

    public void setTeacherLabelText(byte index, String text)
    {
        switch(index)
        {
            case 0:
                teacherLabel1.setText(text);
                break;
            case 1:
                teacherLabel2.setText(text);
                break;
            case 2:
                teacherLabel3.setText(text);
                break;
            case 3:
                teacherLabel4.setText(text);
                break;
            case 4:
                teacherLabel5.setText(text);
                break;
            case 5:
                teacherLabel6.setText(text);
                break;
            case 6:
                teacherLabel7.setText(text);
                break;
            case 7:
                teacherLabel8.setText(text);
                break;
            case 8:
                teacherLabel9.setText(text);
                break;
            case 9:
                teacherLabel10.setText(text);
                break;
            default:
                throw new IllegalArgumentException("Neplatný index labelu: " + index);
        }
    }

    public void setTeacherButtonEnabled(byte index, boolean enabled)
    {
        getTeacherButton(index).setEnabled(enabled);
    }

    public void clearTeacherList()
    {
        for(byte i = 0; i < 10; i++)
        {
            setTeacherLabelText(i, "");
            setTeacherButtonEnabled(i, false);
        }
    }
}
