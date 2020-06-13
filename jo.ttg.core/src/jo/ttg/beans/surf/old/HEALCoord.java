package jo.ttg.beans.surf.old;
import java.util.Random;

import jo.ttg.beans.URIBean;

public class HEALCoord implements URIBean
{
	public static final int D_NORTH     = 0;
	public static final int D_NORTHEAST = 1;
	public static final int D_EAST      = 2;
	public static final int D_SOUTHEAST = 3;
	public static final int D_SOUTH     = 4;
	public static final int D_SOUTHWEST = 5;
	public static final int D_WEST      = 6;
	public static final int D_NORTHWEST = 7;
	public static final int D_MAX       = 8;

	private int		mResolution;
	private long	mData;
	
	public int getResolution()
	{
		return mResolution;
	}
	
	public long getData()
	{
		return mData;
	}
	
	public long getMaskedData()
	{
		return getData()&getMask();
	}
	
	public void setResolution(int v)
	{
		mResolution = v;
	}
	
	public void setData(long v)
	{
		mData = v;
	}
	
	public HEALCoord()
	{
		this(0 ,0);
	}
	
	public HEALCoord(HEALCoord c)
	{
		this(c.getResolution(), c.getData());
	}
	
	public HEALCoord(int resolution, long data)
	{
		mResolution = resolution;
		mData = data;
	}
	
	public HEALCoord(int resolution, Random rnd)
	{
		this(resolution, 0);
		setPixCoord(0, Math.abs(rnd.nextInt())%12);
		for (int i = 1; i <= resolution; i++)
			setPixCoord(i, Math.abs(rnd.nextInt())%4);
	}
	
	long getMask()
	{
		return ~(-1L<<(4+mResolution*2));
	}
	
	public long getPixCoord(int resolution)
	{
		if (resolution == 0)
			return mData&0xf;
		else
			return ((mData>>(resolution*2+2))&0x3);
	}
	
	public long getPixCoord()
	{
		return getPixCoord(mResolution);
	}
	
	public void setPixCoord(int resolution, long val)
	{
		if (resolution == 0)
		{
			mData &= ~0xf;
			mData |= val&0xf;
		}
		else
		{
			mData &= ~(0x3<<(resolution*2+2));
			mData |= ((val&0x3)<<(resolution*2+2));
		}
	}

	public void setPixCoord(long val)
	{
		setPixCoord(mResolution, val);
	}
	
	public HEALCoord getParent()
	{
		return new HEALCoord(mResolution - 1, mData);
	}
	
	public int hashCode()
	{
		return (int)getMaskedData();
	}
	
	public boolean equals(Object o)
	{
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof HEALCoord))
            return false;
		try
		{
			HEALCoord c = (HEALCoord)o;
			boolean ret = ((c.getMaskedData() == getMaskedData()) && (c.getResolution() == getResolution()));
			//if (ret)
			//	System.out.println(toString()+"=="+c.toString());
			//else
			//	System.out.println(toString()+"<>"+c.toString());
			return ret;
		}
		catch (ClassCastException e)
		{
			return false;
		}
	}
	
	public String toString()
	{
		return "HEALCoord[resolution="+mResolution+",data="+Long.toHexString(mData)+"]";
	}

    public String getURI()
    {
        return "healpix://"+Long.toHexString(mData)+"?resolution="+mResolution;
    }
}
