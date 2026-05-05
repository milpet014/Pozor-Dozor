import java.time.LocalDate;

public class MonthDaySetting
{
    // Dátum konkrétneho dňa v mesiaci.
    private final LocalDate date;

    // Určuje, či sa daný deň má zahrnúť do generovania dozorov.
    private boolean included;

    public MonthDaySetting(LocalDate date, boolean included)
    {
        // Nastavenie dátumu a stavu zahrnutia dňa.
        this.date = date;
        this.included = included;
    }

    public LocalDate getDate()
    {
        // Vracia dátum daného nastavenia.
        return date;
    }

    public boolean isIncluded()
    {
        // Vracia, či sa deň započítava do generovania.
        return included;
    }

    public void setIncluded(boolean included)
    {
        // Nastaví, či sa deň má započítať do generovania.
        this.included = included;
    }
}