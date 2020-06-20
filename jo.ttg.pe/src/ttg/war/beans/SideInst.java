package ttg.war.beans;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SideInst
{
	private Side			mSide;
	private int				mIndex;
	private Color			mColor1;
	private Color			mColor2;
	private int				mVictoryPoints;
	private List<WorldInst>		mWorlds;
	private List<ShipInst>		mShips;
	private PlayerInterface	mPlayer;
	private int				mResources;
	
	public SideInst()
	{
		mWorlds = new ArrayList<>();
		mShips = new ArrayList<>();
	}
	
    public Color getColor1()
    {
        return mColor1;
    }

    public Color getColor2()
    {
        return mColor2;
    }

    public Side getSide()
    {
        return mSide;
    }

    public int getVictoryPoints()
    {
        return mVictoryPoints;
    }

    public void setColor1(Color color)
    {
        mColor1 = color;
    }

    public void setColor2(Color color)
    {
        mColor2 = color;
    }

    public void setSide(Side side)
    {
        mSide = side;
    }

    public void setVictoryPoints(int i)
    {
        mVictoryPoints = i;
    }

    public List<WorldInst> getWorlds()
    {
        return mWorlds;
    }

    public void setWorlds(List<WorldInst> list)
    {
        mWorlds = list;
    }

    public List<ShipInst> getShips()
    {
        return mShips;
    }

    public void setShips(List<ShipInst> list)
    {
        mShips = list;
    }

    public PlayerInterface getPlayer()
    {
        return mPlayer;
    }

    public void setPlayer(PlayerInterface interface1)
    {
        mPlayer = interface1;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return mSide.toString();
	}

    public int getResources()
    {
        return mResources;
    }

    public void setResources(int i)
    {
        mResources = i;
    }

	/**
	 * @return
	 */
	public int getIndex()
	{
		return mIndex;
	}

	/**
	 * @param i
	 */
	public void setIndex(int i)
	{
		mIndex = i;
	}

}
