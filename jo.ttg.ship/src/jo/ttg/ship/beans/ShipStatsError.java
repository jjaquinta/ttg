/*
 * Created on Dec 10, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.ship.beans;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ShipStatsError
{
	public static final int INFO = 0;
	public static final int NOT_ENOUGH_JUMP = 1;
	public static final int NOT_ENOUGH_JUMP_TECH = 2;
	public static final int NOT_ENOUGH_MAN = 3;
	public static final int TOO_MUCH_MAN = 4;
	public static final int NOT_ENOUGH_POW = 5;
	public static final int NOT_ENOUGH_FUEL_POW = 6;
	public static final int TOO_MANY_HULLS = 7;
	public static final int NO_HULL = 8;
	public static final int NOT_ENOUGH_HULL = 9;
	public static final int NOT_ENOUGH_FUEL_JUMP = 10;
	public static final int NO_COMPUTER = 11;
	public static final int NOT_ENOUGH_COMPUTER = 12;
	public static final int NOT_ENOUGH_STATEROOMS = 13;
	public static final int NOT_ENOUGH_COMPUTER_CONTROL = 14;
	public static final int NOT_ENOUGH_CONTROLS = 15;
	public static final int NOT_ENOUGH_AIRLOCKS = 16;
	public static final int NOT_ENOUGH_SEATS = 17;
	public static final int TOO_MANY_WEAPONS = 18;
	
	private static final String[] mErrorMessage =
	{
		"information",
		"Insufficient jump drive for minimal jump",
		"Insufficient tech level for desired jump",
		"Insufficient maneuver drive for minimal maneuver",
		"Maxmimum maneuver is 6",
		"Not enough power generated",
		"Not enough fuel for powerplant",
		"Too many hulls",
		"No hull detected",
		"Not enough space in hull",
		"Not enough fuel for jump",
		"No computer installed",
		"Three computers are needed for a starship",
		"Not enough staterooms",
		"Insufficiently powered computer",
		"Not enough control panels",
		"Not enough airlocks",
		"Not enough seats",
		"Too many weapons installed",
	};
	
	private int		mId;
	private String	mDescription;
	private double	mValue1;
	private double	mValue2;
	
	public ShipStatsError(int id, String desc)
	{
		mId = id;
		mDescription = desc;
	}
	
	public ShipStatsError(int id, String desc, double value1)
	{
		this(id, desc);
		mValue1 = value1;
	}
	
	public ShipStatsError(int id, String desc, double value1, double value2)
	{
		this(id, desc, value1);
		mValue2 = value2;
	}
	
	public String getErrorMessage()
	{
		return mErrorMessage[mId];
	}
	
	public String toString()
	{
        String txt = getErrorMessage();
        if (mDescription != null)
            txt += " ("+mDescription+")";
		return txt;
	}
	
	/**
	 * @return
	 */
	public String getDescription()
	{
		return mDescription;
	}

	/**
	 * @return
	 */
	public int getId()
	{
		return mId;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string)
	{
		mDescription = string;
	}

	/**
	 * @param i
	 */
	public void setId(int i)
	{
		mId = i;
	}

	/**
	 * @return
	 */
	public double getValue1()
	{
		return mValue1;
	}

	/**
	 * @return
	 */
	public double getValue2()
	{
		return mValue2;
	}

	/**
	 * @param d
	 */
	public void setValue1(double d)
	{
		mValue1 = d;
	}

	/**
	 * @param d
	 */
	public void setValue2(double d)
	{
		mValue2 = d;
	}

}
