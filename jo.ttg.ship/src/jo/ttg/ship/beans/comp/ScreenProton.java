/*
 * Created on Dec 18, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.beans.comp;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ScreenProton extends ScreenComponent implements TechLevelComponent
{
	private static final int S_SMALL = 0;
	private static final int S_MEDIUM = 1;
	private static final int S_LARGE = 2;
	
	private int	mTechLevel;
	private static final int[]	mTechLevelRange = { 19, 21 };
	private int	mSize;
	private static final int[][]	mSizeRange = 
	{
		{ S_SMALL, S_SMALL },
		{ S_SMALL, S_MEDIUM },
		{ S_SMALL, S_LARGE },
	};
	private static final String[]	mSizeDescription = { "class I", "class II", "class III" };
	
	/**
	 * 
	 */
	public ScreenProton()
	{
		super();
		mTechLevel = 19;
		mSize = S_SMALL;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Proton Screen";
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return mTechLevel;
	}
	
	private static final int[] tlOff = {
		0, 1, 3,
	};

	private int getOff()
	{
		return tlOff[mTechLevel-19] + mSize;
	}

	/**
	 *
	 */
	private static final double[] volRange = {
		1350, 410, 550, 620, 270, 325,
	};

	public double getVolume()
	{
		return volRange[getOff()];
	}

	/**
	 *
	 */
	private static final double[] weightRange = {
		700, 190, 240, 300, 430, 165,
	};

	public double getWeight()
	{
		return weightRange[getOff()];
	}

	/**
	 *
	 */
	private static final double[] powRange = {
		25000, 50000, 75000, 100000, 125000, 150000,
	};

	public double getPower()
	{
		return powRange[getOff()];
	}

	/**
	 *
	 */
	private static final double[] priceRange = {
		75, 60, 70, 45, 55, 60,
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
		return mSizeRange[mTechLevel-19];
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
