public class CloudBackupResult
{
    // Počet súborov, ktoré sa podarilo nahrať alebo stiahnuť.
    private final int fileCount;

    // Kód školy použitý pri cloudovej operácii.
    private final String schoolID;

    public CloudBackupResult(int fileCount, String schoolID)
    {
        // Uloženie výsledných údajov cloudovej operácie.
        this.fileCount = fileCount;
        this.schoolID = schoolID;
    }

    public int getFileCount()
    {
        // Vracia počet spracovaných súborov.
        return fileCount;
    }

    public String getSchoolID()
    {
        // Vracia kód školy použitý pri zálohe alebo obnove.
        return schoolID;
    }
}