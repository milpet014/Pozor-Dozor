import java.time.LocalDate;

public class DutyEntry
{
    // Dátum, pre ktorý je dozor pridelený.
    private final LocalDate date;

    // Prvý učiteľ pridelený na dozor.
    private final Teacher firstTeacher;

    // Druhý učiteľ pridelený na dozor.
    private final Teacher secondTeacher;

    public DutyEntry(LocalDate date, Teacher firstTeacher, Teacher secondTeacher)
    {
        // Nastavenie dátumu a dvojice učiteľov pre jeden dozor.
        this.date = date;
        this.firstTeacher = firstTeacher;
        this.secondTeacher = secondTeacher;
    }

    public LocalDate getDate()
    {
        // Vracia dátum dozoru.
        return date;
    }

    public Teacher getFirstTeacher()
    {
        // Vracia prvého učiteľa prideleného na dozor.
        return firstTeacher;
    }

    public Teacher getSecondTeacher()
    {
        // Vracia druhého učiteľa prideleného na dozor.
        return secondTeacher;
    }
}