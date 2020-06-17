package ttg.beans.war;

public class Ship
{
	private int		mSide;
	private String	mName;
	private int		mAttack;
	private int		mDefense;
	private int		mCapacity;
	private int		mJump;
	
    public int getAttack()
    {
        return mAttack;
    }

    public int getCapacity()
    {
        return mCapacity;
    }

    public int getDefense()
    {
        return mDefense;
    }

    public int getJump()
    {
        return mJump;
    }

    public void setAttack(int i)
    {
        mAttack = i;
    }

    public void setCapacity(int i)
    {
        mCapacity = i;
    }

    public void setDefense(int i)
    {
        mDefense = i;
    }

    public void setJump(int i)
    {
        mJump = i;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String string)
    {
        mName = string;
    }

    public int getSide()
    {
        return mSide;
    }

    public void setSide(int side)
    {
        mSide = side;
    }

    /**
     *
     */

    public String toString()
    {
        return getName();
    }

}
