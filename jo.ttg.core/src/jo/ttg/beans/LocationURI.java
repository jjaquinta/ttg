/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.beans;

import java.util.Properties;

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
	
	/**
	 * @return
	 */
	public OrdBean getOrds()
	{
		return mOrds;
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
