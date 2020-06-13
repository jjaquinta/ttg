package jo.ttg.ship.beans.comp;

import jo.util.utils.MathUtils;

public class Avionics extends ShipComponent implements TechLevelComponent
{
	private int mTechLevel;
	private int[] mTechLevelRange = { 9, 11 };

    /**
     *
     */

    public Avionics()
    {
        super();
    }

    /**
     *
     */

    public String getName()
    {
        return "Avionics";
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
		return mTechLevelRange;
	}

    /**
     *
     */

    public double getVolume()
    {
        return MathUtils.tableLookup(0, mTechLevel, 1, avionicsTable);
    }

    /**
     *
     */

    public double getWeight()
    {
		return MathUtils.tableLookup(0, mTechLevel, 2, avionicsTable);
    }

    /**
     *
     */

    public double getPower()
    {
		return MathUtils.tableLookup(0, mTechLevel, 3, avionicsTable);
    }

    /**
     *
     */

    public double getPrice()
    {
		return MathUtils.tableLookup(0, mTechLevel, 4, avionicsTable);
    }

	/**
	 *
	 */

	public double getNOE()
	{
		return MathUtils.tableLookup(0, mTechLevel, 5, avionicsTable);
	}

	private static final double[][] avionicsTable =
	{
		{ 8,0.4,0.05,0.2,10,120 },
		{ 9,0.4,0.04,0.2,11,130 },
		{ 10,0.3,0.04,0.15,12,140 },
		{ 11,0.3,0.03,0.15,13,150 },
		{ 12,0.2,0.03,0.1,14,160 },
		{ 13,0.2,0.02,0.1,15,170 },
		{ 14,0.1,0.02,0.05,16,180 },
		{ 15,0.1,0.02,0.05,17,190 },
		{ 16,0.05,0.03,0.03,18,200 },
		{ 17,0.05,0.03,0.03,19,250 },
		{ 18,0.02,0.04,0.02,20,300 },
		{ 19,0.02,0.04,0.02,22,350 },
		{ 20,0.01,0.05,0.01,24,400 },
		{ 21,0.01,0.06,0.01,28,450 },
	};
    public void setTechLevel(int i)
    {
        mTechLevel = i;
    }

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_LOCO;
	}

}
