package jo.ttg.beans.sys;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.URIBean;
import jo.util.beans.Bean;

public class SystemBean extends Bean implements URIBean
{
    // Name
    private String mName;
    public String getName()
    {
        return mName;
    }
    public void setName(String v)
    {
        mName = v;
    }

    // SystemRoot
    private BodyBean mSystemRoot;
    public BodyBean getSystemRoot()
    {
        return mSystemRoot;
    }
    public void setSystemRoot(BodyBean v)
    {
        mSystemRoot = v;
    }

    // Ords
    private OrdBean mOrds;
    public OrdBean getOrds()
    {
        return mOrds;
    }
    public void setOrds(OrdBean v)
    {
        mOrds = v;
    }
    
    private long mSeed;


    // constructor
    public SystemBean()
    {
        mName = new String();
        mSystemRoot = null;
        mOrds = new OrdBean();
    }
    
	public String getURI()
	{
		return "sys://"+getOrds().toURIString();
	}
    public long getSeed()
    {
        return mSeed;
    }
    public void setSeed(long seed)
    {
        mSeed = seed;
    }
}
