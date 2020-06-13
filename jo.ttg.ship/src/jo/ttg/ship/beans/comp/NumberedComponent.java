package jo.ttg.ship.beans.comp;

public abstract class NumberedComponent extends ShipComponent
{
	private int	mNumber;
	
    /**
     *
     */

    public NumberedComponent()
    {
        super();
        mNumber = 1;
    }
    public int getNumber()
    {
        return mNumber;
    }

    public void setNumber(int i)
    {
        mNumber = i;
    }

}
