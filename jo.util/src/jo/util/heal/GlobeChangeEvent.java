/*
 * Created on Jul 31, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.util.heal;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GlobeChangeEvent<T>
{
    public static final int CHANGE_COORD = 0;
    public static final int CHANGE_GLOBE = 1;

    int                     mID;
    IHEALGlobe<T>           mGlobe;
    IHEALCoord              mCoord;
    T                       mOldValue;
    T                       mNewValue;

    /**
     * @return
     */
    public IHEALCoord getCoord()
    {
        return mCoord;
    }

    /**
     * @return
     */
    public IHEALGlobe<T> getGlobe()
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
    public T getNewValue()
    {
        return mNewValue;
    }

    /**
     * @return
     */
    public T getOldValue()
    {
        return mOldValue;
    }

    /**
     * @param coord
     */
    public void setCoord(IHEALCoord coord)
    {
        mCoord = coord;
    }

    /**
     * @param globe
     */
    public void setGlobe(IHEALGlobe<T> globe)
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
    public void setNewValue(T object)
    {
        mNewValue = object;
    }

    /**
     * @param object
     */
    public void setOldValue(T object)
    {
        mOldValue = object;
    }

}
