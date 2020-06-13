package jo.ttg.ship.beans.comp;

import jo.ttg.ship.beans.ShipStats;

public class ManeuverDrive extends ShipComponent implements TechLevelComponent, VolumeComponent
{
	private double mVolume;
	private int mTechLevel;
	private int[] mTechLevelRange = { 9, 11 };
	private double[] mManeuverRange = { 0, 6 };
	
	/**
	 *
	 */

	public ManeuverDrive()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Maneuver Drive";
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
		return mVolume;
	}

	/**
	 *
	 */

	public double getWeight()
	{
		if (mTechLevel < 11)
			return mVolume/13.5*27.0;
		else
			return mVolume/13.5*35.0;
	}

	/**
	 *
	 */

	public double getPower()
	{
		if (mTechLevel < 11)
			return mVolume/13.5*65.0;
		else
			return mVolume/13.5*70.0;
	}

	/**
	 *
	 */

	public double getPrice()
	{
		if (mTechLevel < 11)
			return mVolume/13.5*500000.0;
		else
			return mVolume/13.5*700000.0;
	}

	public void setTechLevel(int i)
	{
		mTechLevel = i;
	}

	public void setVolume(double d)
	{
		mVolume = d;
	}

	/**
	 *
	 */

	public double getManeuver(ShipStats s)
	{
		if (s.getDisplacement() <= 0)
			return 0;
		double pc = getVolume()/s.getDisplacement();
		double man = (pc+0.01)/0.03;
		return man;
	}

	public void setManeuver(double v, ShipStats s)
	{
		if (s.getDisplacement() <= 0)
			return;
		if (v == 0)
			setVolume(0);
		else
		{
			double pc = (v*3-1)*.01;
			setVolume(pc*s.getDisplacement());
		}
	}

	public double[] getManeuverRange()
	{
		return mManeuverRange;
	}


	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_LOCO;
	}
}
