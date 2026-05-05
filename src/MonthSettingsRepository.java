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
    // Hlavička CSV súboru s nastaveniami dní.
    private static final String HEADER = "date;included";

    // Cesta ku kalendárovému CSV súboru.
    private final Path monthSettingsPath = AppPath.calendarPath();

    public MonthSettingsRepository()
    {
        // Pri vytvorení repository sa overí, že calendar.csv existuje a má správnu hlavičku.
        AppFile.checkCsvFile(monthSettingsPath, HEADER);
    }

    public List<MonthDaySetting> loadAllSettings()
    {
        // Zoznam všetkých načítaných nastavení dní zo všetkých mesiacov.
        List<MonthDaySetting> settings = new ArrayList<>();

        try
        {
            // Načítanie všetkých riadkov z calendar.csv.
            List<String> lines = Files.readAllLines(monthSettingsPath);

            for(String line : lines)
            {
                // Prázdne riadky a hlavička sa preskočia.
                if(line.isBlank() || line.equals(HEADER))
                {
                    continue;
                }

                String[] parts = line.split(";", -1);

                // Každý dátový riadok musí mať presne dva stĺpce: dátum a included.
                if(parts.length != 2)
                {
                    continue;
                }

                // Dátum je uložený vo formáte LocalDate, napríklad 2026-05-04.
                LocalDate date = LocalDate.parse(parts[0]);

                // Hodnota included určuje, či sa deň započítava do generovania.
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
            // Zachytáva napríklad chybný formát dátumu alebo inú neočakávanú chybu pri spracovaní CSV.
            Svet.sprava("Chyba pri spracovaní month_settings.csv:\n" + e.getMessage(), "Chyba");
        }

        // Nastavenia sa vždy vracajú zoradené podľa dátumu.
        sortSettingsByDate(settings);
        return settings;
    }

    public boolean saveAllSettings(List<MonthDaySetting> settings)
    {
        // Pred uložením sa dni zoradia podľa dátumu, aby bol CSV súbor prehľadný a stabilný.
        sortSettingsByDate(settings);

        // Vytvorenie kompletného obsahu CSV súboru.
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
            // Prepísanie celého kalendárového CSV súboru.
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
        // Načítanie všetkých už existujúcich nastavení dní.
        List<MonthDaySetting> settings = loadAllSettings();

        // Sleduje, či bolo potrebné do CSV doplniť nové dni.
        boolean changed = false;

        // Prejde sa každý deň požadovaného mesiaca.
        for(int day = 1; day <= month.lengthOfMonth(); day++)
        {
            LocalDate date = month.atDay(day);

            // Ak deň v CSV ešte nie je, doplní sa.
            if(!containsDate(settings, date))
            {
                // Pracovné dni sa predvolene zahrnú do generovania,
                // víkendy sa predvolene nezahrnú.
                boolean included = !isWeekend(date);

                settings.add(new MonthDaySetting(date, included));
                changed = true;
            }
        }

        // Ak sa niečo doplnilo, celý CSV súbor sa uloží.
        if(changed)
        {
            saveAllSettings(settings);
        }
    }

    public List<MonthDaySetting> loadMonthSettings(YearMonth month)
    {
        // Načíta sa celý kalendár zo súboru.
        List<MonthDaySetting> allSettings = loadAllSettings();

        // Sem sa vyberú iba dni patriace do požadovaného mesiaca.
        List<MonthDaySetting> monthSettings = new ArrayList<>();

        for(MonthDaySetting setting : allSettings)
        {
            if(isSameMonth(setting.getDate(), month))
            {
                monthSettings.add(setting);
            }
        }

        // Výsledok sa zoradí podľa dátumu.
        sortSettingsByDate(monthSettings);
        return monthSettings;
    }

    public boolean saveMonthSettings(YearMonth month, List<MonthDaySetting> monthSettings)
    {
        // Načíta sa celý existujúci kalendár.
        List<MonthDaySetting> allSettings = loadAllSettings();

        // Do updatedSettings sa vložia všetky dni okrem dní upravovaného mesiaca.
        List<MonthDaySetting> updatedSettings = new ArrayList<>();

        for(MonthDaySetting setting : allSettings)
        {
            if(!isSameMonth(setting.getDate(), month))
            {
                updatedSettings.add(setting);
            }
        }

        // Potom sa pridajú nové alebo upravené nastavenia daného mesiaca.
        updatedSettings.addAll(monthSettings);

        // Pred uložením sa celý zoznam zoradí podľa dátumu.
        sortSettingsByDate(updatedSettings);

        // Uloženie celého kalendára späť do CSV.
        return saveAllSettings(updatedSettings);
    }

    public void deleteOlderThanMonth(YearMonth month)
    {
        // Načíta sa celý kalendár.
        List<MonthDaySetting> settings = loadAllSettings();

        // Hraničný dátum je prvý deň zadaného mesiaca.
        LocalDate firstDayOfMonth = month.atDay(1);

        // Odstránia sa všetky dni pred týmto mesiacom.
        boolean changed = settings.removeIf(setting ->
                setting.getDate().isBefore(firstDayOfMonth)
        );

        // Ak sa niečo odstránilo, súbor sa uloží.
        if(changed)
        {
            saveAllSettings(settings);
        }
    }

    public List<MonthDaySetting> prepareMonth(YearMonth month)
    {
        // Najprv sa zabezpečí, že požadovaný mesiac v CSV existuje.
        // Ak ešte neexistuje, doplnia sa všetky jeho dni.
        ensureMonthExists(month);

        // Potom sa načítajú iba dni požadovaného mesiaca.
        List<MonthDaySetting> monthSettings = loadMonthSettings(month);

        boolean changed = false;

        // Bezpečnostná normalizácia víkendov.
        // Aj keby sa víkend v CSV ručne nastavil na true, repository ho vráti späť na false.
        for(MonthDaySetting setting : monthSettings)
        {
            if(isWeekend(setting.getDate()) && setting.isIncluded())
            {
                setting.setIncluded(false);
                changed = true;
            }
        }

        // Ak sa museli opraviť víkendy, zmena sa uloží späť do CSV.
        if(changed)
        {
            saveMonthSettings(month, monthSettings);
        }

        return monthSettings;
    }

    private boolean containsDate(List<MonthDaySetting> settings, LocalDate date)
    {
        // Kontrola, či už zoznam obsahuje nastavenie pre konkrétny dátum.
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
        // Podpis mesiaca slúži na porovnanie, či sa kalendár od vygenerovania náhľadu nezmenil.
        // Používa sa pri potvrdení rozpisu, aby sa nepotvrdil starý náhľad po zmene kalendára.
        List<MonthDaySetting> monthSettings = prepareMonth(month);

        StringBuilder signature = new StringBuilder();

        for(MonthDaySetting setting : monthSettings)
        {
            // Do podpisu vstupuje dátum a informácia, či je deň zahrnutý do generovania.
            signature.append(setting.getDate()).append("=").append(setting.isIncluded()).append(";");
        }

        return signature.toString();
    }

    private boolean isWeekend(LocalDate date)
    {
        // Vráti true, ak dátum pripadá na sobotu alebo nedeľu.
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private boolean isSameMonth(LocalDate date, YearMonth month)
    {
        // Porovná, či dátum patrí do zadaného mesiaca a roka.
        return (date.getYear() == month.getYear() && date.getMonthValue() == month.getMonthValue());
    }

    private void sortSettingsByDate(List<MonthDaySetting> settings)
    {
        // Stabilné zoradenie nastavení podľa dátumu.
        settings.sort(Comparator.comparing(MonthDaySetting::getDate));
    }
}