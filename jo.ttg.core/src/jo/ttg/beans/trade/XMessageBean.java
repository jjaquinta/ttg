package jo.ttg.beans.trade;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.URIBean;
import jo.util.beans.Bean;

public class XMessageBean extends Bean implements URIBean
{
    public static final String SCHEME = "xmessage://";
    
    public static final int      XT_SCOUT = 0;
    public static final int      XT_NAVY = 1;
    public static final int      XT_CIVIL = 2;

    private String               mOrigin;
    private String               mDestination;
    private int                  mType;
    private String               mName;
    private boolean              mEncrypted;
    private long                 mValue;
    private DateBean             mExpires;
    private String               mURI;

    public XMessageBean()
    {
        mOrigin = null;
        mDestination = null;
        mType = XT_SCOUT;
        mName = "";
        mEncrypted = false;
        mValue = 0;
        mExpires = new DateBean();
        mURI = "";
    }

    public String getOrigin()
    {
        return mOrigin;
    }

    public void setOrigin(String origin)
    {
        mOrigin = origin;
    }

    public String getDestination()
    {
        return mDestination;
    }

    public void setDestination(String destination)
    {
        mDestination = destination;
    }

    public int getType()
    {
        return mType;
    }

    public void setType(int type)
    {
        mType = type;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public boolean isEncrypted()
    {
        return mEncrypted;
    }

    public void setEncrypted(boolean encrypted)
    {
        mEncrypted = encrypted;
    }

    public long getValue()
    {
        return mValue;
    }

    public void setValue(long value)
    {
        mValue = value;
    }

    public DateBean getExpires()
    {
        return mExpires;
    }

    public void setExpires(DateBean expires)
    {
        mExpires = expires;
    }

    public String getURI()
    {
        return mURI;
    }

    public void setURI(String uRI)
    {
        mURI = uRI;
    }
}
