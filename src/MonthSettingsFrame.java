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
    private final MonthSettingsForm form = new MonthSettingsForm();
    private final MonthSettingsRepository repository = new MonthSettingsRepository();

    private YearMonth currentMonth = YearMonth.now();
    private List<MonthDaySetting> currentMonthSettings;

    public MonthSettingsFrame()
    {
        setTitle("Nastavenie mesiaca");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icons/pozor-dozor.png")).getImage());
        setContentPane(form.getRootPanel());
        pack();
        setLocationRelativeTo(null);

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

        form.getOkButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                saveCurrentMonth();
                dispose();
            }
        });

        form.getCancelButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });

        loadMonth(currentMonth);
    }

    private void loadMonth(YearMonth month)
    {
        currentMonthSettings = repository.prepareMonth(month);

        form.setMonthLabelText(buildMonthTitle(month));
        form.clearCheckBoxes();

        for(int i = 0; i < currentMonthSettings.size(); i++)
        {
            MonthDaySetting setting = currentMonthSettings.get(i);
            LocalDate date = setting.getDate();

            JCheckBox checkBox = form.getCheckBox(i);

            checkBox.setText(buildDayText(date));
            checkBox.setVisible(true);
            checkBox.setSelected(setting.isIncluded());

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
        for(int i = 0; i < currentMonthSettings.size(); i++)
        {
            MonthDaySetting setting = currentMonthSettings.get(i);
            JCheckBox checkBox = form.getCheckBox(i);

            if(isWeekend(setting.getDate()))
            {
                setting.setIncluded(false);
            }
            else
            {
                setting.setIncluded(checkBox.isSelected());
            }
        }

        boolean saved = repository.saveMonthSettings(currentMonth, currentMonthSettings);

        if(!saved)
        {
            Svet.sprava("Nepodarilo sa uložiť nastavenia mesiaca.", "Chyba");
        }
        else
        {
            Main.invalidatePendingDuties();
        }
    }

    private String buildMonthTitle(YearMonth month)
    {
        String monthName = month.getMonth()
                .getDisplayName(TextStyle.FULL_STANDALONE, new Locale("sk", "SK"));

        return monthName.substring(0, 1).toUpperCase(new Locale("sk", "SK")) +
                monthName.substring(1) +
                " " +
                month.getYear();
    }

    private String buildDayText(LocalDate date)
    {
        String dayName = date.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("sk", "SK"));

        String capitalizedDayName =
                dayName.substring(0, 1).toUpperCase(new Locale("sk", "SK")) +
                        dayName.substring(1);

        return date.getDayOfMonth() + "." +
                date.getMonthValue() + ". " +
                capitalizedDayName;
    }

    private boolean isWeekend(LocalDate date)
    {
        switch(date.getDayOfWeek())
        {
            case SATURDAY:
            case SUNDAY:
                return true;
            default:
                return false;
        }
    }
}