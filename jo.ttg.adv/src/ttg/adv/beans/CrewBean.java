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
public class CrewBean extends CharBean
{
	public static final int			JOB_BRIDGE = 1;
	public static final int			JOB_ENGINEERING = 2;
	public static final int			JOB_MAINTENENCE = 3;
	public static final int			JOB_GUNNER = 4;
	public static final int			JOB_COMMAND = 5;
	public static final int			JOB_STEWARD = 6;
	public static final int			JOB_MEDICAL = 7;

	private int			mJob;
	private DateBean	mDateOfHire;
	
	public CrewBean()
	{
	    mDateOfHire = new DateBean();
	}
	
    public int getJob()
    {
        return mJob;
    }
    public void setJob(int job)
    {
        mJob = job;
    }
    public DateBean getDateOfHire()
    {
        return mDateOfHire;
    }
    public void setDateOfHire(DateBean dateOfHire)
    {
        mDateOfHire = dateOfHire;
    }
}
