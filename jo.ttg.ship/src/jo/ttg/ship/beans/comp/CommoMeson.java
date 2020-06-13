package jo.ttg.ship.beans.comp;

public class CommoMeson extends RangedComponent implements TechLevelComponent
{
	private int	mTechLevel;
	private int[] mTechLevelRange1 = { 15, 18 };
	private int[] mTechLevelRange2 = { 15, 20 };
	private int[] mRangeRange = { R_REGIONAL, R_SYSTEM };
	
	/**
	 *
	 */

	public CommoMeson()
	{
		super();
		mTechLevel = 15;
		setRange(R_SYSTEM);
	}

	public int[] getRangeRange()
	{
		return mRangeRange;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Meson";
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return mTechLevel;
	}

	public int[] getTechLevelRange()
	{
		if (getRange() == R_REGIONAL)
			return mTechLevelRange1;
		return mTechLevelRange2;
	}

	/**
	 *
	 */

	public double getVolume()
	{
		return getWeight();
	}

	/**
	 *
	 */

	public double getWeight()
	{
		return mesonWeight[getRange() - mRangeRange[0]][mTechLevel - 15];
	}

	/**
	 *
	 */

	public double getPower()
	{
		return getWeight()/10;
	}

	/**
	 *
	 */

	public double getPrice()
	{
		double mod;
		if (mTechLevel == 15)
			mod = 4;
		else if (mTechLevel == 16)
			mod = 2;
		else
			mod = 1;
		return mesonPrice[getRange()-mRangeRange[0]]*mod;
	}

	public void setTechLevel(int d)
	{
		mTechLevel = d;
	}

	private static double[][] mesonWeight = 
	{
		{ 0.5, 0.3, 0.1, 0.1, 0, 0, 0 },
		{ 2.0, 1.5, 1.0, 0.5, 0.1, 0.1, 0.1 },
		{ 30.0, 16.0, 9.0, 5.0, 0.2, 0.2 },
		{ 150.0, 80.0, 45.0, 25.0, 15.0, 15.0 },
		{ 500.0, 220.0, 160.0, 85.0, 50.0, 50.0 },
	};
	private static double[] mesonPrice = { 250000, 1000000, 2500000, 5000000, 20000000 };

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_COMMO;
	}
}
