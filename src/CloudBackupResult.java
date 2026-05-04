public class CloudBackupResult
{
    private final int fileCount;
    private final String schoolID;

    public CloudBackupResult(int fileCount, String schoolID)
    {
        this.fileCount = fileCount;
        this.schoolID = schoolID;
    }

    public int getFileCount()
    {
        return fileCount;
    }

    public String getSchoolID()
    {
        return schoolID;
    }
}