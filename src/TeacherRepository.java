import knižnica.Svet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepository
{
    private static final byte CSV_ITEMS = 13;
    private static final String HEADER =
            "id;degreeBeforeName;firstName;middleName;lastName;degreeAfterName;dutyCount;lastDutyWeek;canMonday;canTuesday;canWednesday;canThursday;canFriday";

    private final Path teachersPath = AppPath.teachersPath();
    private final Path appDataPath = AppPath.appDataPath();
    private final Path teachersBackupPath = AppPath.appDataPath().resolve("teachers_backup.csv");

    public TeacherRepository(){};

    public List<Teacher> loadTeachers()
    {
        List<Teacher> teachers = new ArrayList<>();
        try
        {
            ensureTeachersFileReady();

            List<String> lines = Files.readAllLines(teachersPath);

            for(String line : lines)
            {
                if(line.isBlank() || line.startsWith("id;")) continue;

                String[] parts = line.split(";", -1);

                if(parts.length != CSV_ITEMS) continue;

                int id = Integer.parseInt(parts[0]);
                String degreeBeforeName = parts[1];
                String firstName = parts[2];
                String middleName = parts[3];
                String lastName = parts[4];
                String degreeAfterName = parts[5];

                int dutyCount = Integer.parseInt(parts[6]);
                int lastDutyWeek = Integer.parseInt(parts[7]);

                boolean canMonday = Boolean.parseBoolean(parts[8]);
                boolean canTuesday = Boolean.parseBoolean(parts[9]);
                boolean canWednesday = Boolean.parseBoolean(parts[10]);
                boolean canThursday = Boolean.parseBoolean(parts[11]);
                boolean canFriday = Boolean.parseBoolean(parts[12]);

                Teacher teacher = new Teacher(id,
                        degreeBeforeName,
                        firstName,
                        middleName,
                        lastName,
                        degreeAfterName,
                        canMonday,
                        canTuesday,
                        canWednesday,
                        canThursday,
                        canFriday);

                teacher.setDutyCount(dutyCount);
                teacher.setLastDutyWeek(lastDutyWeek);

                teachers.add(teacher);
            }
        }
        catch(IOException e)
        {
            Svet.sprava("Chyba pri čítaní teachers.csv:\n" + e.getMessage(), "CHYBA");
        }
        catch(NumberFormatException e)
        {
            Svet.sprava("Chyba pri spracovaní údajov z teachers.csv:\n" + e.getMessage(), "Chyba");
        }

        return teachers;
    }

    private void ensureTeachersFileReady()
    {
        try
        {
            Files.createDirectories(appDataPath);

            if(!Files.exists(teachersPath))
            {
                Files.writeString(teachersPath, HEADER + System.lineSeparator());
                return;
            }

            List<String> lines = Files.readAllLines(teachersPath);

            if(lines.isEmpty())
            {
                Files.writeString(teachersPath, HEADER + System.lineSeparator());
                return;
            }

            if(!lines.get(0).equals(HEADER))
            {
                Files.copy(teachersPath, teachersBackupPath, StandardCopyOption.REPLACE_EXISTING);
                Files.writeString(teachersPath, HEADER + System.lineSeparator());

                Svet.sprava("Pôvodný súbor \"teachers.csv\" je poškodený. Ukladám ho ako teachers_backup.csv", "CHYBA");
            }

        }
        catch(IOException e)
        {
            Svet.sprava("Chyba pri čítaní teachers.csv:\n" + e.getMessage(), "Chyba");
        }
    }

    public int getNextId()
    {
        try
        {
            ensureTeachersFileReady();

            List<String> lines = Files.readAllLines(teachersPath);

            int lastId = 0;

            for(String line : lines)
            {
                if(line.isBlank() || line.startsWith("id;"))
                {
                    continue;
                }

                String[] parts = line.split(";", -1);

                int currentId = Integer.parseInt(parts[0]);

                if(currentId > lastId)
                {
                    lastId = currentId;
                }
            }

            return lastId + 1;
        }
        catch(IOException e)
        {
            Svet.sprava("Chyba pri čítaní teachers.csv:\n" + e.getMessage(), "Chyba");
            return -1;
        }
        catch(NumberFormatException e)
        {
            Svet.sprava("Chyba pri čítaní ID učiteľa v teachers.csv:\n" + e.getMessage(), "Chyba");
            return -1;
        }
    }

    public boolean saveTeacher(Teacher teacher)
    {
        try
        {
            ensureTeachersFileReady();

            String line = toCsvLine(teacher) + System.lineSeparator();

            Files.writeString(teachersPath, line, StandardOpenOption.APPEND);

            return true;
        }
        catch(IOException e)
        {
            Svet.sprava("Nepodarilo sa zapísať údaje do súboru csv. " + e, "CHYBA");
            return false;
        }
    }

    public boolean updateTeacher(Teacher updatedTeacher)
    {
        List<Teacher> teachers = loadTeachers();

        boolean found = false;

        for(int i = 0; i < teachers.size(); i++)
        {
            if(teachers.get(i).getId() == updatedTeacher.getId())
            {
                teachers.set(i, updatedTeacher);
                found = true;
                break;
            }
        }

        if(!found)
        {
            Svet.sprava("Učiteľ na úpravu nebol nájdený.", "Chyba");
            return false;
        }

        StringBuilder content = new StringBuilder();
        content.append(HEADER).append(System.lineSeparator());

        for(Teacher teacher : teachers)
        {
            content.append(toCsvLine(teacher)).append(System.lineSeparator());
        }

        try
        {
            Files.writeString(teachersPath, content.toString());
            return true;
        }
        catch (IOException e)
        {
            Svet.sprava("Nepodarilo sa uložiť upraveného učiteľa:\n" + e.getMessage(), "Chyba");
            return false;
        }
    }

    private String toCsvLine(Teacher teacher)
    {
        return teacher.getId() + ";" +
                clean(teacher.getDegreeBeforeName()) + ";" +
                clean(teacher.getFirstName()) + ";" +
                clean(teacher.getMiddleName()) + ";" +
                clean(teacher.getLastName()) + ";" +
                clean(teacher.getDegreeAfterName()) + ";" +
                teacher.getDutyCount() + ";" +
                teacher.getLastDutyWeek() + ";" +
                teacher.isCanMonday() + ";" +
                teacher.isCanTuesday() + ";" +
                teacher.isCanWednesday() + ";" +
                teacher.isCanThursday() + ";" +
                teacher.isCanFriday();
    }

    private String clean(String text)
    {
        if(text == null) return "";

        return text.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }
}
