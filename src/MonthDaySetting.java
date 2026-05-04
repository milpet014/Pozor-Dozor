import java.time.LocalDate;

public class MonthDaySetting
{
    private final LocalDate date;
    private boolean included;

    public MonthDaySetting(LocalDate date, boolean included)
    {
        this.date = date;
        this.included = included;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public boolean isIncluded()
    {
        return included;
    }

    public void setIncluded(boolean included)
    {
        this.included = included;
    }
}
