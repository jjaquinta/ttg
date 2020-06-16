package jo.util.net.http;

import jo.util.beans.Bean;

public class HTTPCacheBean extends Bean
{
    private String  mURL;
    private long    mDate;
    private String  mFileName;
    
    public long getDate()
    {
        return mDate;
    }
    public void setDate(long date)
    {
        mDate = date;
    }
    public String getFileName()
    {
        return mFileName;
    }
    public void setFileName(String fileName)
    {
        mFileName = fileName;
    }
    public String getURL()
    {
        return mURL;
    }
    public void setURL(String url)
    {
        mURL = url;
    }
}
