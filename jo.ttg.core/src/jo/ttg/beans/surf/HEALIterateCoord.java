/*
 * Created on Aug 1, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.beans.surf;

import java.util.Iterator;
import java.util.NoSuchElementException;

import jo.util.heal.IHEALCoord;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HEALIterateCoord implements Iterator<IHEALCoord>
{
	int			mResolution;
	HEALCoord	mLast;
	
	public HEALIterateCoord(int resolution)
	{
		mResolution = resolution;
		mLast = null;
	}
	
	public boolean hasNext()
	{
		if (mLast == null)
			return true;
		if (mLast.getPixCoord(0) != 11)
			return true;
		for (int i = mResolution; i > 0; i--)
			if (mLast.getPixCoord(i) != 3)
				return true;
		return false;
	}
	
	public HEALCoord next() throws NoSuchElementException
	{
		if (mLast == null)
		{
			mLast = new HEALCoord(mResolution, 0);
		}
		else
		{
			for (int i = mResolution; i >= 0; i--)
			{
				int c = (int)mLast.getPixCoord(i);
				if (((i != 0) && (c < 3)) || ((i == 0) && (c < 11)))
				{
					mLast.setPixCoord(i, c + 1);
					break;
				}
				if (i == 11)
					throw new NoSuchElementException();
				mLast.setPixCoord(i, 0);
			}
		}
		return new HEALCoord(mLast);
	}
	
	public void remove()
	{
		throw new UnsupportedOperationException();
	}
}
