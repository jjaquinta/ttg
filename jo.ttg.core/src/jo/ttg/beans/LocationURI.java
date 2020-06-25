/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.beans;

import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import jo.ttg.logic.OrdLogic;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class LocationURI
{
	public static final int UNIVERSE = 0;
	public static final int SECTOR = 1;
	public static final int SUBSECTOR = 2;
	public static final int MAINWORLD = 3;
	public static final int SYSTEM = 4;
	public static final int BODY = 5;
	
	private int mType;
	private OrdBean	mOrds;
	private String	mPath;
	private Properties	mParams;
	private LocBean	mSpace;
	
	public LocationURI()
	{
		mType = -1;
		mParams = new Properties();
	}
	
	public LocationURI(String uri)
	{
	    this();
	    setURI(uri);
	}
	
	/**
	 * @return
	 */
	public OrdBean getOrds()
	{
		return mOrds;
	}
	
	@Override
    
    public String toString()
    {
        return getURI();
    }
    
    private String getOrdString()
    {
//      StringBuffer ret = new StringBuffer();
//      ret.append(mOrds.getX());
//      ret.append(",");
//      ret.append(mOrds.getY());
//      ret.append(",");
//      ret.append(mOrds.getZ());
//      return ret.toString();
        return mOrds.toURIString();
    }

    public String getURI()
    {
        StringBuffer ret = new StringBuffer();
        switch (mType)
        {
            case UNIVERSE:
                ret.append("uni://");
                break;
            case SECTOR:
                ret.append("sec://");
                ret.append(getOrdString());
                break;
            case SUBSECTOR:
                ret.append("sub://");
                ret.append(getOrdString());
                break;
            case MAINWORLD:
                ret.append("mw://");
                ret.append(getOrdString());
                break;
            case SYSTEM:
                ret.append("sys://");
                ret.append(getOrdString());
                ret.append("/");
                break;
            case BODY:
                ret.append("body://");
                ret.append(getOrdString());
                ret.append("/");
                ret.append(mPath);
                break;
        }
        boolean first = true;
        for (Iterator<Object> i = mParams.keySet().iterator(); i.hasNext(); )
        {
            if (first)
            {
                ret.append("?");
                first = false;
            }
            else
                ret.append("&");
            String key = (String)i.next();
            String val = mParams.getProperty(key);
            ret.append(key);
            ret.append("=");
            ret.append(val);
        }
        return ret.toString();
    }
    
    public void setURI(String uri)
    {
        mType = -1;
        mParams.clear();
        if (uri == null)
            return;
        int o = uri.indexOf("://");
        if (o < 0)
            return;
        String type = uri.substring(0, o);
        uri = uri.substring(o+3);
        String ords = "";
        String path = "";
        o = uri.indexOf("/");
        if (o < 0)
        {
            if (uri.length() > 0)
                ords = uri;
        }
        else
        {
            ords = uri.substring(0, o);
            path = uri.substring(o+1);
        }
        o = path.indexOf("?");
        if (o >= 0)
        {
            StringTokenizer p = new StringTokenizer(path.substring(o+1), "&");
            path = path.substring(0, o);
            while (p.hasMoreTokens())
            {
                String kv = p.nextToken();
                o = kv.indexOf("=");
                if (o < 0)
                    mParams.put(kv, "true");
                else
                    mParams.put(kv.substring(0, o), kv.substring(o+1));
            }
        }
        if (type.equals("uni"))
            mType = UNIVERSE;
        else if (type.equals("sec"))
        {
            mType = SECTOR;
            mOrds = OrdLogic.parseString(ords);
        }
        else if (type.equals("sub"))
        {
            mType = SUBSECTOR;
            mOrds = OrdLogic.parseString(ords);
        }
        else if (type.equals("mw"))
        {
            mType = MAINWORLD;
            mOrds = OrdLogic.parseString(ords);
        }
        else if (type.equals("sys"))
        {
            mType = SYSTEM;
            mOrds = OrdLogic.parseString(ords);
            mPath = path;
        }
        else if (type.equals("body"))
        {
            mType = BODY;
            mOrds = OrdLogic.parseString(ords);
            mPath = path;
        }
    }

	/**
	 * @return
	 */
	public String getPath()
	{
		return mPath;
	}

	/**
	 * @return
	 */
	public LocBean getSpace()
	{
		return mSpace;
	}

	/**
	 * @return
	 */
	public int getType()
	{
		return mType;
	}

	/**
	 * @param bean
	 */
	public void setOrds(OrdBean bean)
	{
		mOrds = bean;
	}

	/**
	 * @param string
	 */
	public void setPath(String string)
	{
		mPath = string;
	}

	/**
	 * @param bean
	 */
	public void setSpace(LocBean bean)
	{
		mSpace = bean;
	}

	/**
	 * @param i
	 */
	public void setType(int i)
	{
		mType = i;
	}

	public Properties getParams()
	{
	    return mParams;
	}

	public String getParam(String key)
	{
	    return mParams.getProperty(key);
	}
	
	public void setParam(String key, String val)
	{
        if (val == null)
            mParams.remove(key);
        else
            mParams.setProperty(key, val);
	}
	
	public boolean equals(Object o)
	{
	    if (this == o)
	        return true;
	    if (!(o instanceof LocationURI))
	        return false;
	    LocationURI uri = (LocationURI)o;
	    if (getType() != uri.getType())
	        return false;
	    if (!getOrds().equals(uri.getOrds()))
	        return false;
	    if (!getPath().equals(uri.getPath()))
	        return false;
	    return true;
	}
}
