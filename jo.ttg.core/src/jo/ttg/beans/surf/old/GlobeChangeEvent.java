/*
 * Created on Jul 31, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.beans.surf.old;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GlobeChangeEvent
{
	public static final int	CHANGE_COORD = 0;
	public static final int	CHANGE_GLOBE = 1;
	
	int			mID;
	HEALGlobe	mGlobe;
	HEALCoord	mCoord;
	Object		mOldValue;
	Object		mNewValue;
	/**
	 * @return
	 */
	public HEALCoord getCoord()
	{
		return mCoord;
	}

	/**
	 * @return
	 */
	public HEALGlobe getGlobe()
	{
		return mGlobe;
	}

	/**
	 * @return
	 */
	public int getID()
	{
		return mID;
	}

	/**
	 * @return
	 */
	public Object getNewValue()
	{
		return mNewValue;
	}

	/**
	 * @return
	 */
	public Object getOldValue()
	{
		return mOldValue;
	}

	/**
	 * @param coord
	 */
	public void setCoord(HEALCoord coord)
	{
		mCoord = coord;
	}

	/**
	 * @param globe
	 */
	public void setGlobe(HEALGlobe globe)
	{
		mGlobe = globe;
	}

	/**
	 * @param i
	 */
	public void setID(int i)
	{
		mID = i;
	}

	/**
	 * @param object
	 */
	public void setNewValue(Object object)
	{
		mNewValue = object;
	}

	/**
	 * @param object
	 */
	public void setOldValue(Object object)
	{
		mOldValue = object;
	}

}
