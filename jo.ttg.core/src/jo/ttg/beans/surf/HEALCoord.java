package jo.ttg.beans.surf;
import java.util.NoSuchElementException;
import java.util.Random;

import jo.ttg.beans.URIBean;
import jo.util.heal.HEALLogic;
import jo.util.heal.IHEALCoord;
import jo.util.heal.IHEALVector;

public class HEALCoord implements URIBean, IHEALCoord
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

    @Override
    public IHEALCoord next()
    {
        HEALCoord next = new HEALCoord(this);
        for (int i = mResolution; i >= 0; i--)
        {
            int c = (int)next.getPixCoord(i);
            if (((i != 0) && (c < 3)) || ((i == 0) && (c < 11)))
            {
                next.setPixCoord(i, c + 1);
                break;
            }
            if (i == 11)
                throw new NoSuchElementException();
            next.setPixCoord(i, 0);
        }
        return next;
    }

    @Override
    public IHEALCoord next(int dir)
    {
        switch (dir)
        {
            case HEALCoord.D_NORTHEAST:
                return doSimpleMove(this, HEALVector.V_NE);
            case HEALCoord.D_SOUTHEAST:
                return doSimpleMove(this, HEALVector.V_SE);
            case HEALCoord.D_SOUTHWEST:
                return doSimpleMove(this, HEALVector.V_SW);
            case HEALCoord.D_NORTHWEST:
                return doSimpleMove(this, HEALVector.V_NW);
            case HEALCoord.D_NORTH:
                return doComplicatedMove(this, HEALVector.V_NW);
            case HEALCoord.D_SOUTH:
                return doComplicatedMove(this, HEALVector.V_SE);
            case HEALCoord.D_EAST:
                return doComplicatedMove(this, HEALVector.V_NE);
            case HEALCoord.D_WEST:
                return doComplicatedMove(this, HEALVector.V_SW);
        }
        return null;
    }
    
    static HEALCoord doSimpleMove(HEALCoord o, int dir)
    {
        HEALVector v = new HEALVector(o, dir);
        return v.getNext();
    }
    
    static HEALCoord doComplicatedMove(HEALCoord o, int dir)
    {
        HEALVector v;
        if (o.getData()%2 == 0)
        {
            v = new HEALVector(o, dir);
            v = v.getNext();
            v.turnRight();
        }
        else
        {
            v = new HEALVector(o, (dir+1)%HEALVector.V_MAX);
            v = v.getNext();
            v.turnLeft();
        }
        return v.getNext();
    }

    @Override
    public IHEALVector makeVector(int dir)
    {
        return new HEALVector(this, dir);
    }

    @Override
    public double[][] getThetaPhiBox()
    {
        int[] pixCoords = new int[getResolution() + 1];
        for (int i = 0; i < pixCoords.length; i++)
            pixCoords[i] = (int)getPixCoord(i);
        double[][] ret = HEALLogic.getThetaPhiBox(getResolution(), pixCoords);
        return ret;
    }
}
