package jo.ttg.ship.beans.comp;

public class Seat extends AccomodationComponent
{
	public static final int S_NONE = 0; 
	public static final int S_CRAMPED = 1; 
	public static final int S_ADEQUATE = 2; 
	public static final int S_ROOMY = 3;
	
	private int mSize;
	private int[] mSizeRange = { S_NONE, S_ROOMY };
	private String[] mSizeDescription = { "None", "Cramped", "Adequate", "Roomy" };
	
	public Seat()
	{
		super();
		mSize = S_ROOMY;
	}
	
    public String getName()
    {
        return "Seat";
    }

    public int getTechLevel()
    {
        return 0;
    }

    public double getVolume()
    {
    	double base = mSize + 1.0;
        return base*getNumber();
    }

    public double getWeight()
    {
        return .02*getNumber();
    }

    public double getPower()
    {
        return 0;
    }

    public double getPrice()
    {
        return 100*getNumber();
    }

    public int getSize()
    {
        return mSize;
    }

    public int[] getSizeRange()
    {
        return mSizeRange;
    }

	public String getSizeDescription()
	{
		return mSizeDescription[mSize];
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
		return S_ACCOM;
	}
}
