package jo.ttg.beans.scans;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.URIBean;
import jo.ttg.beans.sys.BodyBean;
import jo.util.beans.Bean;

public class BodyScanBean extends Bean implements URIBean
{
    public static final String SCHEME = "scan://";
    
    private String      mURI;
    private BodyBean    mBody;
    private DateBean    mDate;
    private BodyBean    mAt;
    private DateBean    mNow;
    private int         mValue;
    
    public String getURI()
    {
        return mURI;
    }
    public void setURI(String uRI)
    {
        mURI = uRI;
    }
    public BodyBean getBody()
    {
        return mBody;
    }
    public void setBody(BodyBean body)
    {
        mBody = body;
    }
    public DateBean getDate()
    {
        return mDate;
    }
    public void setDate(DateBean date)
    {
        mDate = date;
    }
    public BodyBean getAt()
    {
        return mAt;
    }
    public void setAt(BodyBean at)
    {
        mAt = at;
    }
    public int getValue()
    {
        return mValue;
    }
    public void setValue(int value)
    {
        mValue = value;
    }
    public DateBean getNow()
    {
        return mNow;
    }
    public void setNow(DateBean now)
    {
        mNow = now;
    }

    
}
