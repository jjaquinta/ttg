/*
 * Created on Mar 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.beans.war;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PlayerMessage
{
	public static final int PLAYERORDER = 1;
	public static final int CANTMOVE = 2;
	public static final int NEWOWNER = 3;
	public static final int SHIPDAMAGED = 4;
	public static final int SHIPDESTROYED = 5;
	public static final int SHIPMISSED = 6;
	public static final int COMBATSTART = 7;
	public static final int COMBATEND = 8;
	public static final int DIDREPAIR = 9;
	public static final int CANTREPAIR = 10;
	public static final int GAMEOVER = 11;
	public static final int YOULOSE = 12;
	public static final int NEWSHIP = 13;
	public static final int ENDOFTURN = 14;
	
	private int mID;
	private Object	mArg1;
	private Object	mArg2;
	
	public String toString()
	{
		switch (mID)
		{
			case PLAYERORDER:
				return "PlayerOrder";
			case CANTMOVE:
				return "Can't move";
			case NEWOWNER:
				return "New Owner";
			case SHIPDAMAGED:
				return "Ship Damaged";
			case SHIPDESTROYED:
				return "Ship Destroyed";
			case SHIPMISSED:
				return "Ship Missed";
			case COMBATSTART:
				return "Combat Start";
			case COMBATEND:
				return "Combat End";
			case DIDREPAIR:
				return "Did Repair";
			case CANTREPAIR:
				return "Can't Repair";
			case GAMEOVER:
				return "Game Over";
			case YOULOSE:
				return "You Lose";
		}
		return "PlayerMessage[id="+mID+", arg1="+mArg1+", arg2="+mArg2+"]";
	}
	
	/**
	 * @return
	 */
	public Object getArg1()
	{
		return mArg1;
	}

	/**
	 * @return
	 */
	public int getID()
	{
		return mID;
	}

	/**
	 * @param object
	 */
	public void setArg1(Object object)
	{
		mArg1 = object;
	}

	/**
	 * @param i
	 */
	public void setID(int i)
	{
		mID = i;
	}

	/**
	 * @return
	 */
	public Object getArg2()
	{
		return mArg2;
	}

	/**
	 * @param object
	 */
	public void setArg2(Object object)
	{
		mArg2 = object;
	}

}
