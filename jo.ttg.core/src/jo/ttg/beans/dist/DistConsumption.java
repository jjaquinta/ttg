/*
 * Created on Dec 9, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.beans.dist;

import jo.ttg.beans.DateBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DistConsumption
{
    private DateBean mTime;
    private double	mFuel;
    private String	mOrigin;
    private String	mDestination;
    private boolean	mJumpCheckNeeded;
    
    public DistConsumption()
    {
        mOrigin = "";
        mDestination = "";
        mTime = new DateBean();
        mJumpCheckNeeded = false;
    }
    
    /**
     * @return Returns the destination.
     */
    public String getDestination()
    {
        return mDestination;
    }
    /**
     * @param destination The destination to set.
     */
    public void setDestination(String destination)
    {
        mDestination = destination;
    }
    /**
     * @return Returns the fuel.
     */
    public double getFuel()
    {
        return mFuel;
    }
    /**
     * @param fuel The fuel to set.
     */
    public void setFuel(double fuel)
    {
        mFuel = fuel;
    }
    /**
     * @return Returns the origin.
     */
    public String getOrigin()
    {
        return mOrigin;
    }
    /**
     * @param origin The origin to set.
     */
    public void setOrigin(String origin)
    {
        mOrigin = origin;
    }
    /**
     * @return Returns the time.
     */
    public DateBean getTime()
    {
        return mTime;
    }
    /**
     * @param time The time to set.
     */
    public void setTime(DateBean time)
    {
        mTime = time;
    }

	public boolean isJumpCheckNeeded() {
		return mJumpCheckNeeded;
	}

	public void setJumpCheckNeeded(boolean jumpCheckNeeded) {
		mJumpCheckNeeded = jumpCheckNeeded;
	}
}
