import knižnica.Svet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class TeacherRepository
{
    // Počet položiek v jednom riadku CSV súboru.
    private static final byte CSV_ITEMS = 13;

    // Očakávaná hlavička súboru teachers.csv.
    private static final String HEADER =
            "id;degreeBeforeName;firstName;middleName;lastName;degreeAfterName;dutyCount;lastDutyWeek;canMonday;canTuesday;canWednesday;canThursday;canFriday";

    // Cesty k hlavnému CSV súboru, dátovému priečinku a zálohe poškodeného CSV.
    private final Path teachersPath = AppPath.teachersPath();
    private final Path appDataPath = AppPath.appDataPath();
    private final Path teachersBackupPath = AppPath.appDataPath().resolve("teachers_backup.csv");

    public TeacherRepository(){};

    public List<Teacher> loadTeachers()
    {
        // Zoznam učiteľov načítaných zo súboru.
        List<Teacher> teachers = new ArrayList<>();

        try
        {
            // Pred čítaním sa overí existencia a hlavička CSV súboru.
            ensureTeachersFileReady();

            List<String> lines = Files.readAllLines(teachersPath);

            for(String line : lines)
            {
                // Prázdne riadky a hlavička sa preskočia.
                if(line.isBlank() || line.startsWith("id;")) continue;

                String[] parts = line.split(";", -1);

                // Riadky s nesprávnym počtom stĺpcov sa ignorujú.
                if(parts.length != CSV_ITEMS) continue;

                // Načítanie základných údajov učiteľa.
                int id = Integer.parseInt(parts[0]);
                String degreeBeforeName = parts[1];
                String firstName = parts[2];
                String middleName = parts[3];
                String lastName = parts[4];
                String degreeAfterName = parts[5];

                // Načítanie štatistických údajov učiteľa.
                int dutyCount = Integer.parseInt(parts[6]);
                int lastDutyWeek = Integer.parseInt(parts[7]);

                // Načítanie dostupnosti podľa pracovných dní.
                boolean canMonday = Boolean.parseBoolean(parts[8]);
                boolean canTuesday = Boolean.parseBoolean(parts[9]);
                boolean canWednesday = Boolean.parseBoolean(parts[10]);
                boolean canThursday = Boolean.parseBoolean(parts[11]);
                boolean canFriday = Boolean.parseBoolean(parts[12]);

                // Vytvorenie objektu učiteľa.
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

                // Doplnenie štatistických údajov, ktoré nie sú súčasťou konštruktora.
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

    public String buildTeachersSignature()
    {
        // Podpis zoznamu učiteľov slúži na zistenie, či sa údaje po vygenerovaní náhľadu nezmenili.
        List<Teacher> teachers = loadTeachers();

        // Zoradenie podľa ID zabezpečí stabilný podpis bez ohľadu na poradie v pamäti.
        teachers.sort(Comparator.comparingInt(Teacher::getId));

        StringBuilder signature = new StringBuilder();

        for(Teacher teacher : teachers)
        {
            signature.append(teacher.getId()).append("|")
                    .append(clean(teacher.getDegreeBeforeName())).append("|")
                    .append(clean(teacher.getFirstName())).append("|")
                    .append(clean(teacher.getMiddleName())).append("|")
                    .append(clean(teacher.getLastName())).append("|")
                    .append(clean(teacher.getDegreeAfterName())).append("|")
                    .append(teacher.getDutyCount()).append("|")
                    .append(teacher.getLastDutyWeek()).append("|")
                    .append(teacher.isCanMonday()).append("|")
                    .append(teacher.isCanTuesday()).append("|")
                    .append(teacher.isCanWednesday()).append("|")
                    .append(teacher.isCanThursday()).append("|")
                    .append(teacher.isCanFriday()).append(";")
                    .append(System.lineSeparator());
        }

        return signature.toString();
    }

    public boolean deleteTeacherById(int id)
    {
        // Načítanie aktuálneho zoznamu učiteľov.
        List<Teacher> teachers = loadTeachers();

        // Odstránenie učiteľa podľa ID.
        boolean removed = teachers.removeIf(teacher -> teacher.getId() == id);

        if(!removed)
        {
            Svet.sprava("Učiteľ na vymazanie nebol nájdený.", "Chyba");
            return false;
        }

        // Vytvorenie nového obsahu CSV bez vymazaného učiteľa.
        StringBuilder content = new StringBuilder();
        content.append(HEADER).append(System.lineSeparator());

        for(Teacher teacher : teachers)
        {
            content.append(toCsvLine(teacher)).append(System.lineSeparator());
        }

        try
        {
            // Prepísanie CSV súboru novým obsahom.
            Files.writeString(teachersPath, content.toString());
            return true;
        }
        catch(IOException e)
        {
            Svet.sprava("Nepodarilo sa vymazať učiteľa:\n" + e.getMessage(), "Chyba");
            return false;
        }
    }

    private void ensureTeachersFileReady()
    {
        try
        {
            // Vytvorenie dátového priečinka, ak ešte neexistuje.
            Files.createDirectories(appDataPath);

            // Ak teachers.csv neexistuje, vytvorí sa s hlavičkou.
            if(!Files.exists(teachersPath))
            {
                Files.writeString(teachersPath, HEADER + System.lineSeparator());
                return;
            }

            List<String> lines = Files.readAllLines(teachersPath);

            // Prázdny súbor sa obnoví iba s hlavičkou.
            if(lines.isEmpty())
            {
                Files.writeString(teachersPath, HEADER + System.lineSeparator());
                return;
            }

            // Ak hlavička nesedí, pôvodný súbor sa zálohuje a vytvorí sa nový.
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
            // Pred hľadaním ID sa overí súbor.
            ensureTeachersFileReady();

            List<String> lines = Files.readAllLines(teachersPath);

            int lastId = 0;

            for(String line : lines)
            {
                // Hlavička a prázdne riadky sa preskočia.
                if(line.isBlank() || line.startsWith("id;"))
                {
                    continue;
                }

                String[] parts = line.split(";", -1);

                // Z prvého stĺpca sa načíta ID.
                int currentId = Integer.parseInt(parts[0]);

                if(currentId > lastId)
                {
                    lastId = currentId;
                }
            }

            // Nové ID je o 1 vyššie než najvyššie existujúce ID.
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

    public boolean saveAllTeachers(List<Teacher> teachers)
    {
        // Vytvorenie kompletného obsahu CSV zo zoznamu učiteľov.
        StringBuilder content = new StringBuilder();
        content.append(HEADER).append(System.lineSeparator());

        for(Teacher teacher : teachers)
        {
            content.append(toCsvLine(teacher)).append(System.lineSeparator());
        }

        try
        {
            // Prepísanie celého teachers.csv.
            Files.writeString(teachersPath, content.toString());
            return true;
        }
        catch(IOException e)
        {
            Svet.sprava("Nepodarilo sa uložiť teachers.csv:\n" + e.getMessage(), "Chyba");
            return false;
        }
    }

    public boolean saveTeacher(Teacher teacher)
    {
        try
        {
            // Pred zápisom sa overí, že CSV existuje a má správnu hlavičku.
            ensureTeachersFileReady();

            // Jeden učiteľ sa pridá ako nový riadok na koniec CSV.
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
        // Načítanie aktuálneho zoznamu učiteľov.
        List<Teacher> teachers = loadTeachers();

        boolean found = false;

        // Vyhľadanie učiteľa podľa ID a jeho nahradenie upravenou verziou.
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

        // Vytvorenie nového obsahu CSV s upraveným učiteľom.
        StringBuilder content = new StringBuilder();
        content.append(HEADER).append(System.lineSeparator());

        for(Teacher teacher : teachers)
        {
            content.append(toCsvLine(teacher)).append(System.lineSeparator());
        }

        try
        {
            // Prepísanie CSV súboru.
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
        // Prevod učiteľa na jeden CSV riadok.
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
        // Ošetrenie textu pred uložením do jednoduchého CSV formátu.
        if(text == null) return "";

        return text.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }
}