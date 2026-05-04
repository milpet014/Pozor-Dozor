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
    private final TeacherRepository teacherRepository = new TeacherRepository();
    private final MonthSettingsRepository monthSettingsRepository = new MonthSettingsRepository();

    public List<DutyEntry> generateMonthDuties(YearMonth month)
    {
        List<Teacher> teachers = teacherRepository.loadTeachers();
        List<MonthDaySetting> monthSettings = monthSettingsRepository.prepareMonth(month);

        List<DutyEntry> duties = new ArrayList<>();

        Map<Integer, Integer> generatedCount = new HashMap<>();
        Map<Integer, LocalDate> lastGeneratedDate = new HashMap<>();

        for(MonthDaySetting setting : monthSettings)
        {
            if(!setting.isIncluded())
            {
                continue;
            }

            LocalDate date = setting.getDate();
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            List<Teacher> candidates = new ArrayList<>();

            for(Teacher teacher : teachers)
            {
                if(isAvailableForDay(teacher, dayOfWeek))
                {
                    candidates.add(teacher);
                }
            }

            candidates.sort(buildComparator(generatedCount, lastGeneratedDate));

            if(candidates.size() < 2)
            {
                throw new IllegalStateException(
                        "Pre deň " + date + " nie sú dostupní aspoň dvaja učitelia."
                );
            }

            Teacher firstTeacher = candidates.get(0);
            Teacher secondTeacher = candidates.get(1);

            duties.add(new DutyEntry(date, firstTeacher, secondTeacher));

            generatedCount.put(
                    firstTeacher.getId(),
                    generatedCount.getOrDefault(firstTeacher.getId(), 0) + 1
            );

            generatedCount.put(
                    secondTeacher.getId(),
                    generatedCount.getOrDefault(secondTeacher.getId(), 0) + 1
            );

            lastGeneratedDate.put(firstTeacher.getId(), date);
            lastGeneratedDate.put(secondTeacher.getId(), date);
        }

        return duties;
    }

    private boolean isAvailableForDay(Teacher teacher, DayOfWeek dayOfWeek)
    {
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
                return false;
        }
    }

    private Comparator<Teacher> buildComparator(Map<Integer, Integer> generatedCount, Map<Integer, LocalDate> lastGeneratedDate)
    {
        return Comparator
                .comparingInt((Teacher teacher) ->
                        teacher.getDutyCount() + generatedCount.getOrDefault(teacher.getId(), 0))
                .thenComparing((Teacher a, Teacher b) ->
                        compareLastGeneratedDate(a, b, lastGeneratedDate))
                .thenComparingInt(Teacher::getLastDutyWeek)
                .thenComparing(Teacher::getLastName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Teacher::getFirstName, String.CASE_INSENSITIVE_ORDER);
    }

    private int compareLastGeneratedDate(
            Teacher a,
            Teacher b,
            Map<Integer, LocalDate> lastGeneratedDate)
    {
        LocalDate aDate = lastGeneratedDate.get(a.getId());
        LocalDate bDate = lastGeneratedDate.get(b.getId());

        if(aDate == null && bDate == null)
        {
            return 0;
        }

        if(aDate == null)
        {
            return -1;
        }

        if(bDate == null)
        {
            return 1;
        }

        return aDate.compareTo(bDate);
    }

    public boolean applyGeneratedDuties(List<DutyEntry> duties)
    {
        List<Teacher> teachers = teacherRepository.loadTeachers();

        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for(DutyEntry duty : duties)
        {
            int weekNumber = duty.getDate().get(weekFields.weekOfWeekBasedYear());

            boolean firstUpdated = updateTeacherAfterDuty(teachers, duty.getFirstTeacher().getId(), weekNumber);

            boolean secondUpdated = updateTeacherAfterDuty(teachers, duty.getSecondTeacher().getId(), weekNumber);

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

        return teacherRepository.saveAllTeachers(teachers);
    }

    private boolean updateTeacherAfterDuty(List<Teacher> teachers, int teacherId, int weekNumber)
    {
        for(Teacher teacher : teachers)
        {
            if(teacher.getId() == teacherId)
            {
                teacher.setDutyCount(teacher.getDutyCount() + 1);
                teacher.setLastDutyWeek(weekNumber);
                return true;
            }
        }

        return false;
    }
}