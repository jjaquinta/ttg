/*
 * Created on Jan 26, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.beans.adv;

import java.util.EventObject;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AdvEvent extends EventObject
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -5957636095204981743L;
    public static final int GAME_SAVED = 1;
    public static final int GAME_LOADED = 2;
    public static final int GAME_NEW = 3;
    public static final int CARGO_BUY = 4;
    public static final int CARGO_BUY_NO_SPACE = 5;
    public static final int CARGO_BUY_NO_MONEY = 6;
    public static final int FREIGHT_BUY = 7;
    public static final int FREIGHT_BUY_NO_SPACE = 8;
    public static final int FREIGHT_BUY_NO_MONEY = 9;
    public static final int PASSENGER_CONTRACT = 10;
    public static final int PASSENGER_CONTRACT_NO_BERTH = 11;
    public static final int PASSENGER_CONTRACT_NO_CABIN = 12;
    public static final int PASSENGER_CONTRACT_BUMPED = 13;
    public static final int PASSENGER_DISEMBARK = 20;
    public static final int PASSENGER_DISEMBARK_LEAVE = 21;
    public static final int PASSENGER_DISEMBARK_ABANDON = 22;
    public static final int CREW_HIRE = 14;
    public static final int CREW_HIRE_NO_CABIN = 15;
    public static final int FUEL_BUY = 16;
    public static final int FUEL_BUY_NO_MONEY = 17;
    public static final int FUEL_SCOOPED = 18;
    public static final int FUEL_SCOOPED_FAILED = 19;
    public static final int TIME_PASSES = 23;
    public static final int MONEY_DEBIT = 24;
    public static final int MONEY_CREDIT = 25;
    public static final int LOAN_PAY = 26;
    public static final int LOAN_UPCOMING = 27;
    public static final int LOAN_DUE = 28;
    public static final int LOAN_OVERDUE = 29;
    public static final int SHIP_JUMP_ENTER = 30;
    public static final int SHIP_JUMP_EXIT = 31;
    public static final int SHIP_DOCK = 32;
    public static final int SHIP_UNDOCK = 33;
    
    private int	mID;
    private Object	mNoun;
    private Object	mAdjective;
    
    public AdvEvent(Object src)
    {
        super(src);
    }
    
    public AdvEvent(Object src, int id)
    {
        super(src);
        setID(id);
    }
    
    public AdvEvent(Object src, int id, Object noun)
    {
        super(src);
        setID(id);
        setNoun(noun);
    }
    
    public AdvEvent(Object src, int id, Object noun, Object adjective)
    {
        super(src);
        setID(id);
        setNoun(noun);
        setAdjective(adjective);
    }
    
    public int getID()
    {
        return mID;
    }
    public void setID(int id)
    {
        mID = id;
    }
    public Object getAdjective()
    {
        return mAdjective;
    }
    public void setAdjective(Object adjective)
    {
        mAdjective = adjective;
    }
    public Object getNoun()
    {
        return mNoun;
    }
    public void setNoun(Object noun)
    {
        mNoun = noun;
    }
}
