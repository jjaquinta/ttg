/*
 * Created on Jan 13, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.beans.adv;

import jo.util.beans.Bean;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UNIDInst extends Bean
{
	public static final int SHIP = 0;
	public static final int CARGO = 1;
	public static final int STAFF = 2;
	
	private long	mExpiryDate;
	private int		mType;
	/**
	 * @return
	 */
	public long getExpiryDate()
	{
		return mExpiryDate;
	}

	/**
	 * @return
	 */
	public int getType()
	{
		return mType;
	}

	/**
	 * @param l
	 */
	public void setExpiryDate(long l)
	{
		mExpiryDate = l;
	}

	/**
	 * @param i
	 */
	public void setType(int i)
	{
		mType = i;
	}

}
