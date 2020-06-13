/*
 * Created on Dec 18, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.beans.comp;

import jo.ttg.ship.beans.ShipStats;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ScreenMeson extends ScreenComponent implements HullDependantPower, TechLevelComponent
{
	private static final int S_SMALL = 0;
	private static final int S_MEDIUM = 1;
	private static final int S_LARGE = 2;
	
	private int	mTechLevel;
	private static final int[]	mTechLevelRange = { 12, 21 };
	private int	mSize;
	private static final int[][]	mSizeRange = 
	{
		{ S_SMALL, S_SMALL },
		{ S_SMALL, S_MEDIUM },
		{ S_SMALL, S_LARGE },
		{ S_SMALL, S_LARGE },
		{ S_SMALL, S_SMALL },
		{ S_SMALL, S_SMALL },
		{ S_SMALL, S_SMALL },
		{ S_SMALL, S_SMALL },
		{ S_SMALL, S_SMALL },
		{ S_SMALL, S_SMALL },
	};
	private static final String[]	mSizeDescription = { "class I", "class II", "class III" };
	
	/**
	 * 
	 */
	public ScreenMeson()
	{
		super();
		mTechLevel = 12;
		mSize = S_SMALL;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Meson Screen";
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return mTechLevel;
	}
	
	private static final int[] tlOff = {
		0, 1, 3, 6, 10, 11, 12, 13, 14, 15, 16,
	};

	private int getOff()
	{
		return tlOff[mTechLevel-12] + mSize;
	}

	/**
	 *
	 */
	private static final double[] volRange = {
		1220, 400, 610, 215, 270, 325, 270, 400, 400, 325, 375, 435, 485, 540, 595,
	};

	public double getVolume()
	{
		return volRange[getOff()];
	}

	/**
	 *
	 */
	private static final double[] weightRange = {
		910, 300, 460, 160, 230, 290, 245, 360, 360, 310, 340, 390, 460, 560, 640,
	};

	public double getWeight()
	{
		return weightRange[getOff()];
	}

	/**
	 *
	 */
	private static final double[] powRange = {
		.15, .3, .45, .6, .75, .9, 1.05, 1.2, 1.2, 1.5, 1.65, 1.8, 1.95, 2.1, 2.25,
	};

	public double getPower()
	{
		throw new IllegalArgumentException();
	}

	public double getPower(Hull h)
	{
		return powRange[getOff()]*h.getVolume();
	}

    public double getPower(ShipStats stats)
    {
        return powRange[getOff()]*stats.getDisplacement();
    }

	/**
	 *
	 */
	private static final double[] priceRange = {
		80, 50, 55, 40, 45, 50, 40, 50, 50, 50, 60, 70, 80, 90, 100,
	};

	public double getPrice()
	{
		return priceRange[getOff()]*1000000;
	}

	public int[] getTechLevelRange()
	{
		return mTechLevelRange;
	}

	public void setTechLevel(int i)
	{
		mTechLevel = i;
	}

	public int getSize()
	{
		return mSize;
	}

	public String getSizeDescription()
	{
		return mSizeDescription[mSize];
	}

	public int[] getSizeRange()
	{
		return mSizeRange[mTechLevel-12];
	}

	public void setSize(int i)
	{
		mSize = i;
	}


	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_SCREENS;
	}

	public int getFactor()
	{
		return getOff() + 1;
	}
}
