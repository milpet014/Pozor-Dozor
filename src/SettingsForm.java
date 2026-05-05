import javax.swing.*;

public class SettingsForm
{
    // Hlavný panel formulára vytvorený cez IntelliJ GUI Designer.
    private JPanel rootPanel;

    // Text poznámky pod čiarou v PDF.
    private JTextArea pdfFootnoteTextArea;

    // Číselné polia na nastavenie veľkostí písma v PDF.
    private JSpinner pdfTitleFontSizeSpinner;
    private JSpinner pdfDutyFontSizeSpinner;
    private JSpinner pdfFootnoteFontSizeSpinner;

    // Ovládacie tlačidlá formulára.
    private JButton saveButton;
    private JButton cancelButton;
    private JButton defaultButton;

    // Popisné labely vo formulári.
    private JLabel pdfFootnoteLabel;
    private JLabel pdfTitleFontSizeLabel;
    private JLabel pdfDutyFontSizeLabel;
    private JLabel pdfFootnoteFontSizeLabel;
    private JLabel fontSizesLabel;
    private JLabel schoolIDLabel;

    // Pole pre kód školy.
    private JTextField schoolIDField;

    public JPanel getRootPanel()
    {
        // Vracia hlavný panel formulára.
        return rootPanel;
    }

    public JButton getSaveButton()
    {
        // Vracia tlačidlo na uloženie nastavení.
        return saveButton;
    }

    public JButton getCancelButton()
    {
        // Vracia tlačidlo na zatvorenie formulára bez uloženia.
        return cancelButton;
    }

    public JButton getDefaultButton()
    {
        // Vracia tlačidlo na obnovenie predvolených hodnôt.
        return defaultButton;
    }

    public String getPdfFootnoteText()
    {
        // Vracia text poznámky pod čiarou.
        return pdfFootnoteTextArea.getText();
    }

    public void setPdfFootnoteText(String text)
    {
        // Nastaví text poznámky pod čiarou.
        pdfFootnoteTextArea.setText(text);
    }

    public double getPdfTitleFontSize()
    {
        // Vracia veľkosť písma nadpisu v PDF.
        return getSpinnerDoubleValue(pdfTitleFontSizeSpinner);
    }

    public void setPdfTitleFontSize(double value)
    {
        // Nastaví veľkosť písma nadpisu v PDF.
        pdfTitleFontSizeSpinner.setValue(value);
    }

    public double getPdfDutyFontSize()
    {
        // Vracia veľkosť písma zoznamu dozorov.
        return getSpinnerDoubleValue(pdfDutyFontSizeSpinner);
    }

    public void setPdfDutyFontSize(double value)
    {
        // Nastaví veľkosť písma zoznamu dozorov.
        pdfDutyFontSizeSpinner.setValue(value);
    }

    public double getPdfFootnoteFontSize()
    {
        // Vracia veľkosť písma poznámky pod čiarou.
        return getSpinnerDoubleValue(pdfFootnoteFontSizeSpinner);
    }

    public String getSchoolID()
    {
        // Vracia kód školy bez okolitých medzier.
        return schoolIDField.getText().trim();
    }

    public void setSchoolID(String value)
    {
        // Nastaví kód školy.
        schoolIDField.setText(value == null ? "" : value);
    }

    public void setPdfFootnoteFontSize(double value)
    {
        // Nastaví veľkosť písma poznámky pod čiarou.
        pdfFootnoteFontSizeSpinner.setValue(value);
    }

    public void setupSpinners()
    {
        // Nastavenie povoleného rozsahu veľkosti písma nadpisu.
        pdfTitleFontSizeSpinner.setModel(
                new SpinnerNumberModel(18.0, 8.0, 40.0, 0.5)
        );

        // Nastavenie povoleného rozsahu veľkosti písma zoznamu dozorov.
        pdfDutyFontSizeSpinner.setModel(
                new SpinnerNumberModel(9.0, 6.0, 24.0, 0.5)
        );

        // Nastavenie povoleného rozsahu veľkosti písma poznámky pod čiarou.
        pdfFootnoteFontSizeSpinner.setModel(
                new SpinnerNumberModel(8.0, 5.0, 18.0, 0.5)
        );
    }

    private double getSpinnerDoubleValue(JSpinner spinner)
    {
        // Bezpečné získanie číselnej hodnoty zo spinnera.
        Object value = spinner.getValue();

        if(value instanceof Number)
        {
            return ((Number)value).doubleValue();
        }

        // Záloha pre prípad, že by hodnota bola uložená ako text.
        return Double.parseDouble(value.toString().replace(",", "."));
    }
}