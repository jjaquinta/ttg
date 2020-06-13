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
public class ScreenNuclearDamper extends ScreenComponent implements TechLevelComponent
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
	public ScreenNuclearDamper()
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
		return "Nuclear Damper";
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
		675, 540, 270, 110, 165, 160, 135, 200, 270, 160, 190, 215, 245, 280, 295,
	};

	public double getVolume()
	{
		return volRange[getOff()];
	}

	/**
	 *
	 */
	private static final double[] weightRange = {
		740, 590, 300, 120, 150, 180, 145, 220, 300, 175, 210, 230, 250, 270, 290,
	};

	public double getWeight()
	{
		return weightRange[getOff()];
	}

	/**
	 *
	 */
	private static final double[] powRange = {
		2500, 5000, 7500, 10000, 12500, 15000, 17500, 20000, 22500, 25000, 27500, 30000, 32500, 35000, 37500
	};

	public double getPower()
	{
		return powRange[getOff()];
	}

	/**
	 *
	 */
	private static final double[] priceRange = {
		50, 40, 45, 30, 35, 38, 30, 40, 50, 45, 50, 55, 60, 65, 70,
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
