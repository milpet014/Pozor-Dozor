import knižnica.Svet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class MonthSettingsFrame extends JFrame
{
    // Formulár kalendára vytvorený cez IntelliJ GUI Designer.
    private final MonthSettingsForm form = new MonthSettingsForm();

    // Repository pre načítanie a uloženie nastavení dní v mesiaci.
    private final MonthSettingsRepository repository = new MonthSettingsRepository();

    // Aktuálne zobrazený mesiac.
    private YearMonth currentMonth = YearMonth.now();

    // Nastavenia jednotlivých dní aktuálneho mesiaca.
    private List<MonthDaySetting> currentMonthSettings;

    public MonthSettingsFrame()
    {
        // Základné nastavenie okna.
        setTitle("Nastavenie mesiaca");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icons/pozor-dozor.png")).getImage());
        setContentPane(form.getRootPanel());
        pack();
        setLocationRelativeTo(null);

        // Uloženie aktuálneho mesiaca a prechod na predchádzajúci mesiac.
        form.getPreviousButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                saveCurrentMonth();
                currentMonth = currentMonth.minusMonths(1);
                loadMonth(currentMonth);
            }
        });

        // Uloženie aktuálneho mesiaca a prechod na nasledujúci mesiac.
        form.getNextButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                saveCurrentMonth();
                currentMonth = currentMonth.plusMonths(1);
                loadMonth(currentMonth);
            }
        });

        // Uloženie aktuálneho mesiaca a zatvorenie okna.
        form.getOkButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                saveCurrentMonth();
                dispose();
            }
        });

        // Zatvorenie okna bez uloženia aktuálnych zmien.
        form.getCancelButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });

        // Načítanie aktuálneho mesiaca pri otvorení okna.
        loadMonth(currentMonth);
    }

    private void loadMonth(YearMonth month)
    {
        // Príprava a načítanie nastavení dní pre daný mesiac.
        currentMonthSettings = repository.prepareMonth(month);

        // Nastavenie názvu mesiaca a vyčistenie checkboxov.
        form.setMonthLabelText(buildMonthTitle(month));
        form.clearCheckBoxes();

        // Vyplnenie checkboxov podľa dní v mesiaci.
        for(int i = 0; i < currentMonthSettings.size(); i++)
        {
            MonthDaySetting setting = currentMonthSettings.get(i);
            LocalDate date = setting.getDate();

            JCheckBox checkBox = form.getCheckBox(i);

            checkBox.setText(buildDayText(date));
            checkBox.setVisible(true);
            checkBox.setSelected(setting.isIncluded());

            // Víkendy sa zvýraznia červenou a vždy sa považujú za nezapočítané.
            if(isWeekend(date))
            {
                checkBox.setSelected(false);
                checkBox.setEnabled(true);
                checkBox.setForeground(new Color(220, 46, 46));
            }
            else
            {
                checkBox.setEnabled(true);
                checkBox.setForeground(Color.BLACK);
            }
        }
    }

    private void saveCurrentMonth()
    {
        // Prenesenie stavu checkboxov späť do dátového modelu.
        for(int i = 0; i < currentMonthSettings.size(); i++)
        {
            MonthDaySetting setting = currentMonthSettings.get(i);
            JCheckBox checkBox = form.getCheckBox(i);

            // Víkendy sa nikdy nezapočítavajú do generovania.
            if(isWeekend(setting.getDate()))
            {
                setting.setIncluded(false);
            }
            else
            {
                setting.setIncluded(checkBox.isSelected());
            }
        }

        // Uloženie nastavení mesiaca do CSV.
        boolean saved = repository.saveMonthSettings(currentMonth, currentMonthSettings);

        if(!saved)
        {
            Svet.sprava("Nepodarilo sa uložiť nastavenia mesiaca.", "Chyba");
        }
        else
        {
            // Po zmene kalendára už prípadný rozpracovaný rozpis nie je platný.
            Main.invalidatePendingDuties();
        }
    }

    private String buildMonthTitle(YearMonth month)
    {
        // Slovenský názov mesiaca pre titulok okna.
        String monthName = month.getMonth()
                .getDisplayName(TextStyle.FULL_STANDALONE, new Locale("sk", "SK"));

        // Prvé písmeno názvu mesiaca sa zobrazí veľké.
        return monthName.substring(0, 1).toUpperCase(new Locale("sk", "SK")) +
                monthName.substring(1) + " " + month.getYear();
    }

    private String buildDayText(LocalDate date)
    {
        // Slovenský názov dňa v týždni.
        String dayName = date.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("sk", "SK"));

        // Prvé písmeno názvu dňa sa zobrazí veľké.
        String capitalizedDayName = dayName.substring(0, 1).toUpperCase(new Locale("sk", "SK")) + dayName.substring(1);

        // Text checkboxu, napríklad: 5.5. Pondelok.
        return date.getDayOfMonth() + "." + date.getMonthValue() + ". " + capitalizedDayName;
    }

    private boolean isWeekend(LocalDate date)
    {
        // Kontrola, či dátum pripadá na sobotu alebo nedeľu.
        switch(date.getDayOfWeek())
        {
            case SATURDAY:
                return true;
            case SUNDAY:
                return true;
            default:
                return false;
        }
    }
}