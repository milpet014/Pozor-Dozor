import knižnica.Svet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MonthSettingsRepository
{
    private static final String HEADER = "date;included";

    private final Path monthSettingsPath = AppPath.calendarPath();

    public MonthSettingsRepository()
    {
        AppFile.checkCsvFile(monthSettingsPath, HEADER);
    }

    public List<MonthDaySetting> loadAllSettings()
    {
        List<MonthDaySetting> settings = new ArrayList<>();

        try
        {
            List<String> lines = Files.readAllLines(monthSettingsPath);

            for(String line : lines)
            {
                if(line.isBlank() || line.equals(HEADER))
                {
                    continue;
                }

                String[] parts = line.split(";", -1);

                if(parts.length != 2)
                {
                    continue;
                }

                LocalDate date = LocalDate.parse(parts[0]);
                boolean included = Boolean.parseBoolean(parts[1]);

                settings.add(new MonthDaySetting(date, included));
            }
        }
        catch(IOException e)
        {
            Svet.sprava("Chyba pri čítaní month_settings.csv:\n" + e.getMessage(), "Chyba");
        }
        catch(Exception e)
        {
            Svet.sprava("Chyba pri spracovaní month_settings.csv:\n" + e.getMessage(), "Chyba");
        }

        sortSettingsByDate(settings);
        return settings;
    }

    public boolean saveAllSettings(List<MonthDaySetting> settings)
    {
        sortSettingsByDate(settings);

        StringBuilder content = new StringBuilder();
        content.append(HEADER).append(System.lineSeparator());

        for(MonthDaySetting setting : settings)
        {
            content.append(setting.getDate())
                    .append(";")
                    .append(setting.isIncluded())
                    .append(System.lineSeparator());
        }

        try
        {
            Files.writeString(monthSettingsPath, content.toString());
            return true;
        }
        catch(IOException e)
        {
            Svet.sprava("Chyba pri ukladaní month_settings.csv:\n" + e.getMessage(), "Chyba");
            return false;
        }
    }

    public void ensureMonthExists(YearMonth month)
    {
        List<MonthDaySetting> settings = loadAllSettings();
        boolean changed = false;

        for(int day = 1; day <= month.lengthOfMonth(); day++)
        {
            LocalDate date = month.atDay(day);

            if(!containsDate(settings, date))
            {
                boolean included = !isWeekend(date);
                settings.add(new MonthDaySetting(date, included));
                changed = true;
            }
        }

        if(changed)
        {
            saveAllSettings(settings);
        }
    }

    public List<MonthDaySetting> loadMonthSettings(YearMonth month)
    {
        List<MonthDaySetting> allSettings = loadAllSettings();
        List<MonthDaySetting> monthSettings = new ArrayList<>();

        for(MonthDaySetting setting : allSettings)
        {
            if(isSameMonth(setting.getDate(), month))
            {
                monthSettings.add(setting);
            }
        }

        sortSettingsByDate(monthSettings);
        return monthSettings;
    }

    public boolean saveMonthSettings(YearMonth month, List<MonthDaySetting> monthSettings)
    {
        List<MonthDaySetting> allSettings = loadAllSettings();
        List<MonthDaySetting> updatedSettings = new ArrayList<>();

        for(MonthDaySetting setting : allSettings)
        {
            if(!isSameMonth(setting.getDate(), month))
            {
                updatedSettings.add(setting);
            }
        }

        updatedSettings.addAll(monthSettings);
        sortSettingsByDate(updatedSettings);

        return saveAllSettings(updatedSettings);
    }

    public void deleteOlderThanMonth(YearMonth month)
    {
        List<MonthDaySetting> settings = loadAllSettings();
        LocalDate firstDayOfMonth = month.atDay(1);

        boolean changed = settings.removeIf(setting ->
                setting.getDate().isBefore(firstDayOfMonth)
        );

        if(changed)
        {
            saveAllSettings(settings);
        }
    }

    public List<MonthDaySetting> prepareMonth(YearMonth month)
    {
        ensureMonthExists(month);

        List<MonthDaySetting> monthSettings = loadMonthSettings(month);

        boolean changed = false;

        for(MonthDaySetting setting : monthSettings)
        {
            if(isWeekend(setting.getDate()) && setting.isIncluded())
            {
                setting.setIncluded(false);
                changed = true;
            }
        }

        if(changed)
        {
            saveMonthSettings(month, monthSettings);
        }

        return monthSettings;
    }

    private boolean containsDate(List<MonthDaySetting> settings, LocalDate date)
    {
        for(MonthDaySetting setting : settings)
        {
            if(setting.getDate().equals(date))
            {
                return true;
            }
        }

        return false;
    }

    public String buildMonthSignature(YearMonth month)
    {
        List<MonthDaySetting> monthSettings = prepareMonth(month);

        StringBuilder signature = new StringBuilder();

        for(MonthDaySetting setting : monthSettings)
        {
            signature.append(setting.getDate())
                    .append("=")
                    .append(setting.isIncluded())
                    .append(";");
        }

        return signature.toString();
    }

    private boolean isWeekend(LocalDate date)
    {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private boolean isSameMonth(LocalDate date, YearMonth month)
    {
        return date.getYear() == month.getYear() &&
                date.getMonthValue() == month.getMonthValue();
    }

    private void sortSettingsByDate(List<MonthDaySetting> settings)
    {
        settings.sort(Comparator.comparing(MonthDaySetting::getDate));
    }
}