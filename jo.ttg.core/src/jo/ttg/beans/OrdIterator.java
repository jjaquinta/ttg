/*
 * Created on Dec 4, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.beans;

import java.util.Iterator;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OrdIterator implements Iterator<OrdBean>
{
	private OrdBean	mStart;
	private OrdBean	mStop;
	private OrdBean mAt;
	private long	mDX;
	private long	mDY;
	private boolean	mDone;
		
	public OrdIterator(OrdBean start, OrdBean stop)
	{
		mStart = start;
		mStop = stop;
		mAt = new OrdBean(mStart.getX(), mStart.getY(), 0);
		mDone = false;
		if (mStart.getX() <= mStop.getX())
			mDX = 1;
		else
			mDX = -1;
		if (mStart.getY() <= mStop.getY())
			mDY = 1;
		else
			mDY = -1;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove()
	{
	}
	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext()
	{
		return !mDone;
	}
	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public OrdBean next()
	{
	    OrdBean ret = new OrdBean(mAt);
		mAt.setX(mAt.getX() + mDX);
		if (mAt.getX() == mStop.getX())
		{
			mAt.setX(mStart.getX());
			mAt.setY(mAt.getY() + mDY);
			if (mAt.getY() == mStop.getY())
				mDone = true;
		}
		return ret;
	}
}
