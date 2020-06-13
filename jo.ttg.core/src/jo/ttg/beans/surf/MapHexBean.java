/*
 * Created on Jan 27, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.beans.surf;

import jo.util.heal.IHEALCoord;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MapHexBean
{
	public static final int TYPE_COVER     = 0;
	public static final int TYPE_ELEVATION = 1;
	public static final int TYPE_PLATE     = 2;
	public static final int TYPE_PLATEMOVE = 3;

	// terrain cover constants
	public static final int C_UNSET    = -1;
	public static final int C_WATER    = 0;
	public static final int C_TUNDRA   = 2;
	public static final int C_MTNS     = 3;
	public static final int C_ROUGH    = 4;
	public static final int C_OPEN     = 5;
	public static final int C_DESERT   = 6;
	public static final int C_FOREST   = 7;
	public static final int C_JUNGLE   = 8;
	public static final int C_DEEP     = 9;
	public static final int C_SICE     = 10;
	public static final int C_WICE     = 11;
	public static final int C_VOLC     = 12;
	public static final int C_CITY     = 13;
	public static final int C_ISLAND   = 14;
	public static final int C_LAKE     = 15;

	// plate movement constants
	public static final int M_NONE         = 0;
	public static final int M_CONVERGING   = 1;
	public static final int M_TRANSVERSING = 2;
	public static final int M_DIVERGING    = 3;

	// elevation constants
	public static final int G_WATER		= 1;
	public static final int G_LAND		= 2;
	public static final int G_ISLANDS	= 3;
	public static final int G_LAKES		= 4;
	
	// Location
	private IHEALCoord	mLocation;
	public IHEALCoord getLocation()
	{
		return mLocation;
	}
	public void setLocation(IHEALCoord v)
	{
		mLocation = v;
	}

	// Cover
	private int mCover;
	public int getCover()
	{
		return mCover;
	}
	public void setCover(int v)
	{
		mCover = v;
	}

	// Elevation
	private int mElevation;
	public int getElevation()
	{
		return mElevation;
	}
	public void setElevation(int v)
	{
		mElevation = v;
	}

    // Altitude
    private double mAltitude;
    public double getAltitude()
    {
        return mAltitude;
    }
    public void setAltitude(double v)
    {
        mAltitude = v;
    }

	// Plate
	private int mPlate;
	public int getPlate()
	{
		return mPlate;
	}
	public void setPlate(int v)
	{
		mPlate = v;
	}

	// Plate
	private int mPlateMove;
	public int getPlateMove()
	{
		return mPlateMove;
	}
	public void setPlateMove(int v)
	{
		mPlateMove = v;
	}

	// constructor
	public MapHexBean()
	{
		mCover = 0;
		mElevation = 0;
		mPlate = 0;
	}

	// utils

	public int getByType(int type)
	{
		switch (type)
		{
			case TYPE_COVER:
				return getCover();
			case TYPE_ELEVATION:
				return getElevation();
			case TYPE_PLATE:
				return getPlate();
		}
		return 0;
	}

	public void setByType(int type, int value)
	{
		switch (type)
		{
			case TYPE_COVER:
				setCover(value);
				break;
			case TYPE_ELEVATION:
				setElevation(value);
				break;
			case TYPE_PLATE:
				setPlate(value);
				break;
		}
	}
}
