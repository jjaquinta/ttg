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
public class DistCapabilities
{
    private int mJumpRange;
    private double	mAcceleration;
    private DateBean	mDate;
    private double	mVolume;
    private double	mFuelPerMinute;
    private boolean	mJumpCheckPassed;
    
    public DistCapabilities()
    {
        mJumpRange = 1;
        mAcceleration = 1;
        mDate = new DateBean();
    }
    
    /**
     * @return Returns the acceleration.
     */
    public double getAcceleration()
    {
        return mAcceleration;
    }
    /**
     * @param acceleration The acceleration to set.
     */
    public void setAcceleration(double acceleration)
    {
        mAcceleration = acceleration;
    }
    /**
     * @return Returns the jumpRange.
     */
    public int getJumpRange()
    {
        return mJumpRange;
    }
    /**
     * @param jumpRange The jumpRange to set.
     */
    public void setJumpRange(int jumpRange)
    {
        mJumpRange = jumpRange;
    }
    /**
     * @return Returns the date.
     */
    public DateBean getDate()
    {
        return mDate;
    }
    /**
     * @param date The date to set.
     */
    public void setDate(DateBean date)
    {
        mDate = date;
    }
    public double getVolume()
    {
        return mVolume;
    }
    public void setVolume(double volume)
    {
        mVolume = volume;
    }
    public double getFuelPerMinute()
    {
        return mFuelPerMinute;
    }
    public void setFuelPerMinute(double fuelPerMinute)
    {
        mFuelPerMinute = fuelPerMinute;
    }

	public boolean isJumpCheckPassed() {
		return mJumpCheckPassed;
	}

	public void setJumpCheckPassed(boolean jumpCheckPassed) {
		mJumpCheckPassed = jumpCheckPassed;
	}
}
