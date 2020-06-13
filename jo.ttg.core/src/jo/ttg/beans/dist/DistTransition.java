/*
 * Created on Dec 11, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jo.ttg.beans.dist;

/**
 * @author Jo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DistTransition
{
    public static final int ORBIT_TO_ORBIT = 1;
    public static final int STAR_TO_STAR = 2;
    
    private int mType;
    
    /**
     * @return Returns the type.
     */
    public int getType()
    {
        return mType;
    }
    /**
     * @param type The type to set.
     */
    public void setType(int type)
    {
        mType = type;
    }
}
