package jo.util.utils;
public interface IProgressMon
{
    public void beginTask(String title, int max);
    public void setTaskName(String name);
    public void worked(int soFar);
    public void stop();
    public boolean isCanceled();
}
