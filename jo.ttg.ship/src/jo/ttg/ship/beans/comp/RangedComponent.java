package jo.ttg.ship.beans.comp;

public abstract class RangedComponent extends SensorComponent
{
	public static final int R_DISTANT = 0;
	public static final int R_VERY_DISTANT = 1;
	public static final int R_REGIONAL = 2;
	public static final int R_CONTINENTAL = 3;
	public static final int R_PLANETARY = 4;
	public static final int R_FAR_ORBIT = 5;
	public static final int R_SYSTEM = 6;
	public static final int R_INTERPLANETARY = 7;
	public static final int R_SUBSTELLAR = 8;
	public static final int R_INTERSTELLAR = 9;
	
	private int	mRange;

    /**
     *
     */

    public RangedComponent()
    {
        super();
    }

	public int getRange()
	{
		return mRange;
	}

	public void setRange(int i)
	{
		mRange = i;
	}

	public String getRangeDescription()
	{
		switch (mRange)
		{
			case R_DISTANT:
				return "Distant";
			case R_VERY_DISTANT:
				return "Very Distant";
			case R_REGIONAL:
				return "Regional";
			case R_CONTINENTAL:
				return "Contenental";
			case R_PLANETARY:
				return "Planetary";
			case R_FAR_ORBIT:
				return "Far Orbit";
			case R_SYSTEM:
				return "System";
			case R_INTERPLANETARY:
				return "Interplanetary";
			case R_SUBSTELLAR:
				return "Substellar";
			case R_INTERSTELLAR:
				return "Interstellar";
		}
		return "???";
	}
	
	public abstract int[] getRangeRange();
}
