import java.time.LocalDate;

public class Teacher {
    // Jednoznačné ID učiteľa v CSV súbore.
    private int id;

    // Meno učiteľa vrátane voliteľných titulov.
    private String degreeBeforeName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String degreeAfterName;

    // Štatistické údaje používané pri rozdeľovaní dozorov.
    private int dutyCount;
    private int lastDutyWeek;

    // Dostupnosť učiteľa podľa pracovných dní.
    private boolean canMonday;
    private boolean canTuesday;
    private boolean canWednesday;
    private boolean canThursday;
    private boolean canFriday;

    public Teacher(int id,
                   String degreeBeforeName,
                   String firstName,
                   String middleName,
                   String lastName,
                   String degreeAfterName,
                   boolean canMonday,
                   boolean canTuesday,
                   boolean canWednesday,
                   boolean canThursday,
                   boolean canFriday) {

        // Nastavenie základného identifikátora.
        this.id = id;

        // Nastavenie mena a titulov učiteľa.
        this.degreeBeforeName = degreeBeforeName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.degreeAfterName = degreeAfterName;

        // Nastavenie dostupnosti v jednotlivých pracovných dňoch.
        this.canMonday = canMonday;
        this.canTuesday = canTuesday;
        this.canWednesday = canWednesday;
        this.canThursday = canThursday;
        this.canFriday = canFriday;

        // Nový učiteľ začína bez započítaných dozorov.
        this.dutyCount = 0;
        this.lastDutyWeek = -1;
    }

    public int getId()
    {
        // Vracia ID učiteľa.
        return this.id;
    }

    public String getDegreeBeforeName()
    {
        // Vracia titul pred menom.
        return degreeBeforeName;
    }

    public String getFirstName()
    {
        // Vracia meno učiteľa.
        return firstName;
    }

    public String getMiddleName()
    {
        // Vracia stredné meno učiteľa.
        return middleName;
    }

    public String getLastName()
    {
        // Vracia priezvisko učiteľa.
        return lastName;
    }

    public String getDegreeAfterName()
    {
        // Vracia titul za menom.
        return degreeAfterName;
    }

    public int getDutyCount()
    {
        // Vracia počet už započítaných dozorov.
        return dutyCount;
    }

    public int getLastDutyWeek()
    {
        // Vracia týždeň posledného dozoru.
        return lastDutyWeek;
    }

    public boolean isCanMonday()
    {
        // Vracia dostupnosť učiteľa v pondelok.
        return canMonday;
    }

    public boolean isCanTuesday()
    {
        // Vracia dostupnosť učiteľa v utorok.
        return canTuesday;
    }

    public boolean isCanWednesday()
    {
        // Vracia dostupnosť učiteľa v stredu.
        return canWednesday;
    }

    public boolean isCanThursday()
    {
        // Vracia dostupnosť učiteľa vo štvrtok.
        return canThursday;
    }

    public boolean isCanFriday()
    {
        // Vracia dostupnosť učiteľa v piatok.
        return canFriday;
    }

    public void setDutyCount(int dutyCount)
    {
        // Nastaví počet započítaných dozorov.
        this.dutyCount = dutyCount;
    }

    public void setLastDutyWeek(int lastDutyWeek)
    {
        // Nastaví týždeň posledného dozoru.
        this.lastDutyWeek = lastDutyWeek;
    }

    public String getFullName()
    {
        // Zloženie celého mena vrátane voliteľných titulov.
        String fullName = "";

        if(!degreeBeforeName.isEmpty())
        {
            fullName += degreeBeforeName + " ";
        }

        fullName += firstName + " ";

        if(!middleName.isEmpty())
        {
            fullName += middleName + " ";
        }

        fullName += lastName;

        if(!degreeAfterName.isEmpty())
        {
            fullName += " " + degreeAfterName;
        }

        return fullName;
    }
}