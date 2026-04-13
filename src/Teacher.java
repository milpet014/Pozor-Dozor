import java.time.LocalDate;

public class Teacher {
    private int id;

    private String degreeBeforeName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String degreeAfterName;

    private int dutyCount;
    private int lastDutyWeek;

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
        this.id = id;

        this.degreeBeforeName = degreeBeforeName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.degreeAfterName = degreeAfterName;

        this.canMonday = canMonday;
        this.canTuesday = canTuesday;
        this.canWednesday = canWednesday;
        this.canThursday = canThursday;
        this.canFriday = canFriday;

        this.dutyCount = 0;
        this.lastDutyWeek = -1;
    }

    public int getId()
    {
        return this.id;
    }

    public String getDegreeBeforeName()
    {
        return degreeBeforeName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getDegreeAfterName()
    {
        return degreeAfterName;
    }

    public int getDutyCount()
    {
        return dutyCount;
    }

    public int getLastDutyWeek()
    {
        return lastDutyWeek;
    }

    public boolean isCanMonday()
    {
        return canMonday;
    }

    public boolean isCanTuesday()
    {
        return canTuesday;
    }

    public boolean isCanWednesday()
    {
        return canWednesday;
    }

    public boolean isCanThursday()
    {
        return canThursday;
    }

    public boolean isCanFriday()
    {
        return canFriday;
    }

    public void setDutyCount(int dutyCount)
    {
        this.dutyCount = dutyCount;
    }

    public void setLastDutyWeek(int lastDutyWeek)
    {
        this.lastDutyWeek = lastDutyWeek;
    }

    public String getFullName()
    {
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

