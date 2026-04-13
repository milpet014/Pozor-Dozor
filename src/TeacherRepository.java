import knižnica.Svet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TeacherRepository
{
    private final Path teachersPath = AppPath.teachersPath();

    public int getNextId()
    {
        try
        {
            if(!Files.exists(teachersPath))
            {
                return 1;
            }

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
}
