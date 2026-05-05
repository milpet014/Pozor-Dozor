import knižnica.Svet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsFrame extends JFrame
{
    // Predvolený text poznámky pod čiarou v PDF.
    private static final String DEFAULT_PDF_FOOTNOTE_TEXT =
            "Začiatok služby 7:20 hod. – 7:45 hod.\n" +
                    "Veľká prestávka 10:10 hod. – 10:25 hod.";

    // Predvolené veľkosti písma v PDF.
    public static final double DEFAULT_TITLE_FONT_SIZE = 20.0;
    public static final double DEFAULT_DUTY_FONT_SIZE = 12.0;
    public static final double DEFAULT_FOOTNOTE_FONT_SIZE = 10.0;

    // Formulár nastavení vytvorený cez IntelliJ GUI Designer.
    private final SettingsForm form = new SettingsForm();

    public SettingsFrame()
    {
        // Základné nastavenie okna.
        setTitle("Nastavenia");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icons/pozor-dozor.png")).getImage());
        setContentPane(form.getRootPanel());

        // Inicializácia spinnerov a načítanie aktuálnych nastavení.
        form.setupSpinners();
        loadSettings();

        pack();
        setLocationRelativeTo(null);

        // Uloženie nastavení.
        form.getSaveButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                saveSettings();
            }
        });

        // Zatvorenie okna bez uloženia zmien.
        form.getCancelButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });

        // Obnovenie predvolených hodnôt vo formulári.
        form.getDefaultButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setDefaultValues();
            }
        });

        // Enter vo formulári aktivuje uloženie.
        getRootPane().setDefaultButton(form.getSaveButton());
    }

    private void loadSettings()
    {
        // Načítanie aktuálnych nastavení z config.cfg.
        AppConfig config = new AppConfig();

        form.setPdfFootnoteText(config.getPdfFootnoteText());
        form.setPdfTitleFontSize(config.getPdfTitleFontSize());
        form.setPdfDutyFontSize(config.getPdfDutyFontSize());
        form.setPdfFootnoteFontSize(config.getPdfFootnoteFontSize());
        form.setSchoolID(config.getSchoolID());
    }

    private void saveSettings()
    {
        // Text poznámky pod čiarou nesmie byť prázdny.
        String footnoteText = form.getPdfFootnoteText().trim();

        if(footnoteText.isEmpty())
        {
            Svet.sprava("Poznámka pod čiarou nemôže byť prázdna.", "Chyba");
            return;
        }

        AppConfig config = new AppConfig();

        // Uloženie nastavení vzhľadu PDF.
        config.setPdfFootnoteText(footnoteText);
        config.setPdfTitleFontSize((float)form.getPdfTitleFontSize());
        config.setPdfDutyFontSize((float)form.getPdfDutyFontSize());
        config.setPdfFootnoteFontSize((float)form.getPdfFootnoteFontSize());

        // Kód školy sa používa najmä pri cloudovej zálohe.
        String schoolID = form.getSchoolID();

        if(schoolID.isBlank())
        {
            Svet.sprava("Kód školy nemôže byť prázdny.", "Chyba");
            return;
        }

        config.setSchoolID(schoolID);

        // Uloženie konfigurácie do súboru.
        config.saveConfig();

        // Po zmene nastavení sa zneplatní prípadný rozpracovaný rozpis.
        Main.invalidatePendingDuties();

        // Obnovenie PDF náhľadu s novými nastaveniami.
        Main.refreshCurrentPdfPreview();

        Svet.sprava("Nastavenia boli uložené.", "Hotovo");

        dispose();
    }

    private void setDefaultValues()
    {
        // Nastavenie predvolených hodnôt iba vo formulári.
        form.setPdfFootnoteText(DEFAULT_PDF_FOOTNOTE_TEXT);
        form.setPdfTitleFontSize(DEFAULT_TITLE_FONT_SIZE);
        form.setPdfDutyFontSize(DEFAULT_DUTY_FONT_SIZE);
        form.setPdfFootnoteFontSize(DEFAULT_FOOTNOTE_FONT_SIZE);
    }
}