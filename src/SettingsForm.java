import javax.swing.*;

public class SettingsForm
{
    private JPanel rootPanel;

    private JTextArea pdfFootnoteTextArea;

    private JSpinner pdfTitleFontSizeSpinner;
    private JSpinner pdfDutyFontSizeSpinner;
    private JSpinner pdfFootnoteFontSizeSpinner;

    private JButton saveButton;
    private JButton cancelButton;
    private JButton defaultButton;

    private JLabel pdfFootnoteLabel;
    private JLabel pdfTitleFontSizeLabel;
    private JLabel pdfDutyFontSizeLabel;
    private JLabel pdfFootnoteFontSizeLabel;
    private JLabel fontSizesLabel;
    private JLabel schoolIDLabel;
    private JTextField schoolIDField;

    public JPanel getRootPanel()
    {
        return rootPanel;
    }

    public JButton getSaveButton()
    {
        return saveButton;
    }

    public JButton getCancelButton()
    {
        return cancelButton;
    }

    public JButton getDefaultButton()
    {
        return defaultButton;
    }

    public String getPdfFootnoteText()
    {
        return pdfFootnoteTextArea.getText();
    }

    public void setPdfFootnoteText(String text)
    {
        pdfFootnoteTextArea.setText(text);
    }

    public double getPdfTitleFontSize()
    {
        return getSpinnerDoubleValue(pdfTitleFontSizeSpinner);
    }

    public void setPdfTitleFontSize(double value)
    {
        pdfTitleFontSizeSpinner.setValue(value);
    }

    public double getPdfDutyFontSize()
    {
        return getSpinnerDoubleValue(pdfDutyFontSizeSpinner);
    }

    public void setPdfDutyFontSize(double value)
    {
        pdfDutyFontSizeSpinner.setValue(value);
    }

    public double getPdfFootnoteFontSize()
    {
        return getSpinnerDoubleValue(pdfFootnoteFontSizeSpinner);
    }

    public String getSchoolID()
    {
        return schoolIDField.getText().trim();
    }

    public void setSchoolID(String value)
    {
        schoolIDField.setText(value == null ? "" : value);
    }

    public void setPdfFootnoteFontSize(double value)
    {
        pdfFootnoteFontSizeSpinner.setValue(value);
    }

    public void setupSpinners()
    {
        pdfTitleFontSizeSpinner.setModel(
                new SpinnerNumberModel(18.0, 8.0, 40.0, 0.5)
        );

        pdfDutyFontSizeSpinner.setModel(
                new SpinnerNumberModel(9.0, 6.0, 24.0, 0.5)
        );

        pdfFootnoteFontSizeSpinner.setModel(
                new SpinnerNumberModel(8.0, 5.0, 18.0, 0.5)
        );
    }

    private double getSpinnerDoubleValue(JSpinner spinner)
    {
        Object value = spinner.getValue();

        if(value instanceof Number)
        {
            return ((Number)value).doubleValue();
        }

        return Double.parseDouble(value.toString().replace(",", "."));
    }
}