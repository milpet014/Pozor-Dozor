import knižnica.Svet;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DutyGenerator
{
    // Repository so zoznamom učiteľov a ich štatistikami.
    private final TeacherRepository teacherRepository = new TeacherRepository();

    // Repository s nastavením dní v mesiaci.
    private final MonthSettingsRepository monthSettingsRepository = new MonthSettingsRepository();

    public List<DutyEntry> generateMonthDuties(YearMonth month)
    {
        // Načítanie všetkých učiteľov z CSV.
        List<Teacher> teachers = teacherRepository.loadTeachers();

        // Načítanie alebo automatická príprava kalendára pre daný mesiac.
        // prepareMonth zároveň doplní chýbajúce dni mesiaca, ak ešte v CSV neexistujú.
        List<MonthDaySetting> monthSettings = monthSettingsRepository.prepareMonth(month);

        // Výsledný zoznam vygenerovaných dozorov.
        List<DutyEntry> duties = new ArrayList<>();

        // Počet dozorov pridelených učiteľovi iba v rámci práve generovaného mesiaca.
        // Kľúč je ID učiteľa, hodnota je počet už pridelených dozorov v tomto generovaní.
        Map<Integer, Integer> generatedCount = new HashMap<>();

        // Posledný dátum, kedy bol učiteľ použitý v práve generovanom mesiaci.
        // Pomáha nerozdávať dozory tomu istému učiteľovi príliš blízko pri sebe.
        Map<Integer, LocalDate> lastGeneratedDate = new HashMap<>();

        // Prechádzajú sa všetky dni mesiaca podľa nastavenia kalendára.
        for(MonthDaySetting setting : monthSettings)
        {
            // Dni, ktoré nie sú zahrnuté do generovania, sa preskočia.
            // Typicky ide o víkendy, sviatky, prázdniny alebo ručne vypnuté dni.
            if(!setting.isIncluded())
            {
                continue;
            }

            LocalDate date = setting.getDate();
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            // Kandidáti sú učitelia, ktorí môžu mať dozor v daný deň týždňa.
            List<Teacher> candidates = new ArrayList<>();

            for(Teacher teacher : teachers)
            {
                if(isAvailableForDay(teacher, dayOfWeek))
                {
                    candidates.add(teacher);
                }
            }

            // Kandidáti sa zoradia podľa férovosti výberu.
            // Najvyššiu prioritu majú učitelia s najnižším celkovým počtom dozorov
            // vrátane dozorov pridelených v práve generovanom mesiaci.
            candidates.sort(buildComparator(generatedCount, lastGeneratedDate));

            // Na jeden deň sú potrební dvaja učitelia.
            // Ak ich nie je možné vybrať, generovanie sa zastaví chybou.
            if(candidates.size() < 2)
            {
                throw new IllegalStateException(
                        "Pre deň " + date + " nie sú dostupní aspoň dvaja učitelia."
                );
            }

            // Po zoradení sa zoberú prví dvaja najvhodnejší kandidáti.
            Teacher firstTeacher = candidates.get(0);
            Teacher secondTeacher = candidates.get(1);

            // Vytvorenie záznamu dozoru pre konkrétny deň.
            duties.add(new DutyEntry(date, firstTeacher, secondTeacher));

            // Zvýšenie počítadla dozorov v rámci aktuálneho generovania pre prvého učiteľa.
            generatedCount.put(
                    firstTeacher.getId(),
                    generatedCount.getOrDefault(firstTeacher.getId(), 0) + 1
            );

            // Zvýšenie počítadla dozorov v rámci aktuálneho generovania pre druhého učiteľa.
            generatedCount.put(
                    secondTeacher.getId(),
                    generatedCount.getOrDefault(secondTeacher.getId(), 0) + 1
            );

            // Uloženie dátumu posledného pridelenia v tomto generovaní.
            lastGeneratedDate.put(firstTeacher.getId(), date);
            lastGeneratedDate.put(secondTeacher.getId(), date);
        }

        // Výsledok zatiaľ nie je zapísaný do teachers.csv.
        // Ide iba o návrh rozpisu, ktorý sa zapíše až po potvrdení.
        return duties;
    }

    private boolean isAvailableForDay(Teacher teacher, DayOfWeek dayOfWeek)
    {
        // Kontrola dostupnosti učiteľa podľa konkrétneho dňa v týždni.
        // Dostupnosť sa číta z hodnôt uložených pri učiteľovi.
        switch(dayOfWeek)
        {
            case MONDAY:
                return teacher.isCanMonday();
            case TUESDAY:
                return teacher.isCanTuesday();
            case WEDNESDAY:
                return teacher.isCanWednesday();
            case THURSDAY:
                return teacher.isCanThursday();
            case FRIDAY:
                return teacher.isCanFriday();
            default:
                // Sobota a nedeľa sa nepoužívajú na generovanie dozorov.
                return false;
        }
    }

    private Comparator<Teacher> buildComparator(Map<Integer, Integer> generatedCount, Map<Integer, LocalDate> lastGeneratedDate)
    {
        // Comparator určuje poradie kandidátov na dozor.
        // Čím je učiteľ v zoradení vyššie, tým skôr bude vybraný.
        return Comparator

                // 1. Kritérium: celkový počet dozorov.
                // Berie sa historický dutyCount z CSV plus počet dozorov pridelených v aktuálnom generovaní.
                // Tým sa predchádza tomu, aby jeden učiteľ dostal v jednom mesiaci príliš veľa dozorov.
                .comparingInt((Teacher teacher) ->
                        teacher.getDutyCount() + generatedCount.getOrDefault(teacher.getId(), 0))

                // 2. Kritérium: rozostup v rámci práve generovaného mesiaca.
                // Učiteľ, ktorý v tomto generovaní ešte nebol použitý, má prednosť.
                // Ak už použitý bol, prednosť má ten, kto mal dozor dávnejšie.
                .thenComparing((Teacher a, Teacher b) ->
                        compareLastGeneratedDate(a, b, lastGeneratedDate))

                // 3. Kritérium: historický týždeň posledného dozoru.
                // Nižšia hodnota znamená, že učiteľ mal posledný dozor dávnejšie.
                // Ide zatiaľ o jednoduché historické kritérium založené na čísle týždňa.
                .thenComparingInt(Teacher::getLastDutyWeek)

                // 4. Kritérium: abecedné zoradenie podľa priezviska.
                // Slúži ako stabilný rozhodovací mechanizmus, keď sú predchádzajúce kritériá rovnaké.
                .thenComparing(Teacher::getLastName, String.CASE_INSENSITIVE_ORDER)

                // 5. Kritérium: abecedné zoradenie podľa mena.
                .thenComparing(Teacher::getFirstName, String.CASE_INSENSITIVE_ORDER);
    }

    private int compareLastGeneratedDate(
            Teacher a,
            Teacher b,
            Map<Integer, LocalDate> lastGeneratedDate)
    {
        // Zistenie posledného dátumu, kedy boli porovnávaní učitelia použití
        // v práve generovanom mesiaci.
        LocalDate aDate = lastGeneratedDate.get(a.getId());
        LocalDate bDate = lastGeneratedDate.get(b.getId());

        // Ak ani jeden učiteľ ešte v tomto generovaní nebol použitý,
        // podľa tohto kritéria sú rovnocenní.
        if(aDate == null && bDate == null)
        {
            return 0;
        }

        // Učiteľ, ktorý ešte nebol použitý, má prednosť.
        if(aDate == null)
        {
            return -1;
        }

        // Ak druhý učiteľ ešte nebol použitý, prednosť má on.
        if(bDate == null)
        {
            return 1;
        }

        // Ak už boli použití obaja, prednosť má ten, ktorého posledný dozor bol skôr.
        return aDate.compareTo(bDate);
    }

    public boolean applyGeneratedDuties(List<DutyEntry> duties)
    {
        // Načítanie aktuálneho zoznamu učiteľov zo súboru.
        // Je dôležité načítať aktuálny stav, nie používať staré objekty z náhľadu.
        List<Teacher> teachers = teacherRepository.loadTeachers();

        // Nastavenie pravidiel pre výpočet čísla týždňa.
        // Používa sa default locale systému, takže formát týždňov sa môže riadiť prostredím.
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        // Každý potvrdený záznam dozoru sa započíta obom učiteľom.
        for(DutyEntry duty : duties)
        {
            // Číslo týždňa sa uloží ako informácia o poslednom dozore učiteľa.
            int weekNumber = duty.getDate().get(weekFields.weekOfWeekBasedYear());

            // Aktualizácia prvého učiteľa z daného dozoru.
            boolean firstUpdated = updateTeacherAfterDuty(teachers, duty.getFirstTeacher().getId(), weekNumber);

            // Aktualizácia druhého učiteľa z daného dozoru.
            boolean secondUpdated = updateTeacherAfterDuty(teachers, duty.getSecondTeacher().getId(), weekNumber);

            // Ak sa niektorý učiteľ už nenachádza v teachers.csv, rozpis sa nesmie zapísať.
            // Môže sa to stať napríklad vtedy, keď bol učiteľ po vygenerovaní náhľadu vymazaný.
            if(!firstUpdated || !secondUpdated)
            {
                Svet.sprava(
                        "Rozpis obsahuje učiteľa, ktorý už neexistuje v teachers.csv.\n" +
                                "Vygenerujte rozpis znova.",
                        "Chyba"
                );

                return false;
            }
        }

        // Po úspešnej aktualizácii všetkých učiteľov sa prepíše celý teachers.csv.
        return teacherRepository.saveAllTeachers(teachers);
    }

    private boolean updateTeacherAfterDuty(List<Teacher> teachers, int teacherId, int weekNumber)
    {
        // Vyhľadanie učiteľa podľa ID v aktuálnom zozname učiteľov.
        for(Teacher teacher : teachers)
        {
            if(teacher.getId() == teacherId)
            {
                // Zvýšenie počtu započítaných dozorov o jeden.
                teacher.setDutyCount(teacher.getDutyCount() + 1);

                // Uloženie čísla týždňa, v ktorom mal učiteľ posledný potvrdený dozor.
                teacher.setLastDutyWeek(weekNumber);

                return true;
            }
        }

        // Učiteľ sa v aktuálnom zozname nenašiel.
        return false;
    }
}