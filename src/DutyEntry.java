import java.time.LocalDate;

public class DutyEntry
{
    private final LocalDate date;
    private final Teacher firstTeacher;
    private final Teacher secondTeacher;

    public DutyEntry(LocalDate date, Teacher firstTeacher, Teacher secondTeacher)
    {
        this.date = date;
        this.firstTeacher = firstTeacher;
        this.secondTeacher = secondTeacher;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public Teacher getFirstTeacher()
    {
        return firstTeacher;
    }

    public Teacher getSecondTeacher()
    {
        return secondTeacher;
    }
}