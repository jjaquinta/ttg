/*
 * Created on Jan 6, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.beans;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.chr.CharBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PassengerBean extends CharBean
{
    public static final int PASSAGE_LOW = 1;
    public static final int PASSAGE_MIDDLE = 2;
    public static final int PASSAGE_HIGH = 3;
    
    private String	mOrigin;
    private String	mDestination;
	private int	mPassage;
	private DateBean	mBoarded;
	
	public PassengerBean()
	{
	    mBoarded = new DateBean();
	}
	
    public int getPassage()
    {
        return mPassage;
    }
    public void setPassage(int job)
    {
        mPassage = job;
    }
    public String getDestination()
    {
        return mDestination;
    }
    public void setDestination(String destination)
    {
        mDestination = destination;
    }
    public String getOrigin()
    {
        return mOrigin;
    }
    public void setOrigin(String origin)
    {
        mOrigin = origin;
    }
    public DateBean getBoarded()
    {
        return mBoarded;
    }
    public void setBoarded(DateBean boarded)
    {
        mBoarded = boarded;
    }
}
