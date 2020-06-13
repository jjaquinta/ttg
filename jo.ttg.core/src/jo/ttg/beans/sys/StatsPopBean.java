package jo.ttg.beans.sys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.util.beans.Bean;

/**
 * @author Joseph Jaquinta
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class StatsPopBean extends Bean
{
    /**
      * Progressive Attitude not set
      * @see ttg.TBodyWorld#ProgAtt
      */
    public static final int PA_UNSET = 0;
    /**
      * Radical Progressive Attitude
      * @see ttg.TBodyWorld#ProgAtt
      */
    public static final int PA_RADICAL = 1;
    /**
      * Progressive Progressive Attitude
      * @see ttg.TBodyWorld#ProgAtt
      */
    public static final int PA_PROGRESSIVE = 2;
    /**
      * Conservative Progressive Attitude
      * @see ttg.TBodyWorld#ProgAtt
      */
    public static final int PA_CONSERVATIVE = 3;
    /**
      * Reactionary Progressive Attitude
      * @see ttg.TBodyWorld#ProgAtt
      */
    public static final int PA_REACTIONARY = 4;
    
    public static final String[] PROGRESSIVE_ATTITUDE_DESC = {
        "None",
        "Radical",
        "Progressive",
        "Conservative",
        "Reactionary",        
    };

    /**
      * Progressive Action not set
      * @see ttg.TBodyWorld#ProgAct
      */
    public static final int PC_UNSET = 0;
    /**
      * Enterprising Progressive Action
      * @see ttg.TBodyWorld#ProgAct
      */
    public static final int PC_ENTERPRISING = 1;
    /**
      * Advancing Progressive Action
      * @see ttg.TBodyWorld#ProgAct
      */
    public static final int PC_ADVANCING = 2;
    /**
      * Indifferent Progressive Action
      * @see ttg.TBodyWorld#ProgAct
      */
    public static final int PC_INDIFFERENT = 3;
    /**
      * Stagnant Progressive Action
      * @see ttg.TBodyWorld#ProgAct
      */
    public static final int PC_STAGNANT = 4;
    
    public static final String[] PROGRESSIVE_ACTION_DESC = {
        "None",
        "Enterprising",
        "Advancing",
        "Indifferent",
        "Stagnant",
    };

    /**
      * Aggressive Attitude not set
      * @see ttg.TBodyWorld#AggrAtt
      */
    public static final int AA_UNSET = 0;
    /**
      * Expansionistic Aggressive Attitude
      * @see ttg.TBodyWorld#AggrAtt
      */
    public static final int AA_EXPANSIONISTIC = 1;
    /**
      * Compettitive Aggressive Attitude
      * @see ttg.TBodyWorld#AggrAtt
      */
    public static final int AA_COMPETITIVE = 2;
    /**
      * Unaggressive Aggressive Attitude
      * @see ttg.TBodyWorld#AggrAtt
      */
    public static final int AA_UNAGRESSIVE = 3;
    /**
      * Passive Aggressive Attitude
      * @see ttg.TBodyWorld#AggrAtt
      */
    public static final int AA_PASSIVE = 4;
    
    public static final String[] AGGRESSIVE_ATTITUDE_DESC = {
        "None",
        "Expansionistic",
        "Competitive",
        "Unaggressive",
        "Passive",
    };

    /**
      * Agressive Action not set
      * @see ttg.TBodyWorld#AggrAct
      */
    public static final int AC_UNSET = 0;
    /**
      * Militant Agressive Action
      * @see ttg.TBodyWorld#AggrAct
      */
    public static final int AC_MILITANT = 1;
    /**
      * Neutral Agressive Action
      * @see ttg.TBodyWorld#AggrAct
      */
    public static final int AC_NEUTRAL = 2;
    /**
      * Peaceable Agressive Action
      * @see ttg.TBodyWorld#AggrAct
      */
    public static final int AC_PEACEABLE = 3;
    /**
      * Conciliatory Agressive Action
      * @see ttg.TBodyWorld#AggrAct
      */
    public static final int AC_CONCILIATORY = 4;
    
    public static final String[] AGGRESSIVE_ACTION_DESC = {
        "None",
        "Militant",
        "Neutral",
        "Peaceable",
        "Conciliatory",
    };

    /**
      * Global Extensiveness not set
      * @see ttg.TBodyWorld#GlobExt
      */
    public static final int GE_UNSET = 0;
    /**
      * Monolithic Global Extensiveness
      * @see ttg.TBodyWorld#GlobExt
      */
    public static final int GE_MONOLITHIC = 1;
    /**
      * Hormonious Global Extensiveness
      * @see ttg.TBodyWorld#GlobExt
      */
    public static final int GE_HARMONIOUS = 2;
    /**
      * Discordant Global Extensiveness
      * @see ttg.TBodyWorld#GlobExt
      */
    public static final int GE_DISCORDANT = 3;
    /**
      * Fragmented Global Extensiveness
      * @see ttg.TBodyWorld#GlobExt
      */
    public static final int GE_FRAGMENTED = 4;
    
    public static final String[] GLOBAL_EXTENSIVENESS = {
        "None",
        "Monolithic",
        "Harmonious",
        "Discordant",
        "Fragmented",        
    };

    /**
      * Interplanetary Attitude not set
      * @see ttg.TBodyWorld#IntrExt
      */
    public static final int IE_UNSET = 0;
    /**
      * Xenophilic Interplanetary Attitude
      * @see ttg.TBodyWorld#IntrExt
      */
    public static final int IE_XENOPHILIC = 1;
    /**
      * Friendly Interplanetary Attitude
      * @see ttg.TBodyWorld#IntrExt
      */
    public static final int IE_FRIENDLY = 2;
    /**
      * Aloof Interplanetary Attitude
      * @see ttg.TBodyWorld#IntrExt
      */
    public static final int IE_ALOOF = 3;
    /**
      * Xenophobic Interplanetary Attitude
      * @see ttg.TBodyWorld#IntrExt
      */
    public static final int IE_XENOPHOBIC = 4;
    
    public static final String[] INTERPLANETARY_ATTITUDE = {
        "None",
        "Xenophilic",
        "Friendly",
        "Aloof",
        "Xenophobic",  
    };
    
    /**
      * Total population
      */
    private double mTotalPop;
    /**
      * Number of very large cities
      */
    private long mPop2;
    /**
      * Number of cities
      */
    private long mPop3;
    /**
      * Number of towns
      */
    private long mPop4;
    /**
      * Number of villages
      */
    private long mPop5;
    /**
      * Number of people not living in cities
      */
    private long mRuralPop;
    /**
      * Type for Progressive Attitude
      */
    private int mProgAtt;
    /**
      * Type for Progressive Action
      */
    private int mProgAct;
    /**
      * Type for Aggressive Attitude
      */
    private int mAggrAtt;
    /**
      * Type for Aggressive Action
      */
    private int mAggrAct;
    /**
      * Type for Global Extensiveness
      */
    private int mGlobExt;
    /**
      * Type for Interplanetary Attitude
      */
    private int mIntrExt;

    // constructor

    public StatsPopBean()
    {
        mTotalPop = 0;
        mPop2 = 0;
        mPop3 = 0;
        mPop4 = 0;
        mPop5 = 0;
        mRuralPop = 0;
        mProgAtt = 0;
        mProgAct = 0;
        mAggrAtt = 0;
        mAggrAct = 0;
        mGlobExt = 0;
        mIntrExt = 0;
        mCustoms = new ArrayList<String>();
        mCities = new ArrayList<CityBean>();
    }
    
    // utilities
    public String getAggrDesc()
    {
        return AGGRESSIVE_ACTION_DESC[mAggrAct] + "/" + AGGRESSIVE_ATTITUDE_DESC[mAggrAtt];
    }
    
    public String getProgDesc()
    {
        return PROGRESSIVE_ACTION_DESC[mProgAct] + "/" + PROGRESSIVE_ATTITUDE_DESC[mProgAtt];
    }
    
    public String getExtDesc()
    {
        return GLOBAL_EXTENSIVENESS[mGlobExt] + "/" + INTERPLANETARY_ATTITUDE[mIntrExt];
    }
    
    // getters and setters

    /**
     * Returns the aggrAct.
     * @return int
     */
    public int getAggrAct()
    {
        return mAggrAct;
    }

    /**
     * Returns the aggrAtt.
     * @return int
     */
    public int getAggrAtt()
    {
        return mAggrAtt;
    }

    /**
     * Returns the globExt.
     * @return int
     */
    public int getGlobExt()
    {
        return mGlobExt;
    }

    /**
     * Returns the intrExt.
     * @return int
     */
    public int getIntrExt()
    {
        return mIntrExt;
    }

    /**
     * Returns the pop2.
     * @return long
     */
    public long getPop2()
    {
        return mPop2;
    }

    /**
     * Returns the pop3.
     * @return long
     */
    public long getPop3()
    {
        return mPop3;
    }

    /**
     * Returns the pop4.
     * @return long
     */
    public long getPop4()
    {
        return mPop4;
    }

    /**
     * Returns the pop5.
     * @return long
     */
    public long getPop5()
    {
        return mPop5;
    }

    /**
     * Returns the progAct.
     * @return int
     */
    public int getProgAct()
    {
        return mProgAct;
    }

    /**
     * Returns the progAtt.
     * @return int
     */
    public int getProgAtt()
    {
        return mProgAtt;
    }

    /**
     * Returns the ruralPop.
     * @return long
     */
    public long getRuralPop()
    {
        return mRuralPop;
    }

    /**
     * Returns the totalPop.
     * @return double
     */
    public double getTotalPop()
    {
        return mTotalPop;
    }

    /**
     * Sets the aggrAct.
     * @param aggrAct The aggrAct to set
     */
    public void setAggrAct(int aggrAct)
    {
        mAggrAct = aggrAct;
    }

    /**
     * Sets the aggrAtt.
     * @param aggrAtt The aggrAtt to set
     */
    public void setAggrAtt(int aggrAtt)
    {
        mAggrAtt = aggrAtt;
    }

    /**
     * Sets the globExt.
     * @param globExt The globExt to set
     */
    public void setGlobExt(int globExt)
    {
        mGlobExt = globExt;
    }

    /**
     * Sets the intrExt.
     * @param intrExt The intrExt to set
     */
    public void setIntrExt(int intrExt)
    {
        mIntrExt = intrExt;
    }

    /**
     * Sets the pop2.
     * @param pop2 The pop2 to set
     */
    public void setPop2(long pop2)
    {
        mPop2 = pop2;
    }

    /**
     * Sets the pop3.
     * @param pop3 The pop3 to set
     */
    public void setPop3(long pop3)
    {
        mPop3 = pop3;
    }

    /**
     * Sets the pop4.
     * @param pop4 The pop4 to set
     */
    public void setPop4(long pop4)
    {
        mPop4 = pop4;
    }

    /**
     * Sets the pop5.
     * @param pop5 The pop5 to set
     */
    public void setPop5(long pop5)
    {
        mPop5 = pop5;
    }

    /**
     * Sets the progAct.
     * @param progAct The progAct to set
     */
    public void setProgAct(int progAct)
    {
        mProgAct = progAct;
    }

    /**
     * Sets the progAtt.
     * @param progAtt The progAtt to set
     */
    public void setProgAtt(int progAtt)
    {
        mProgAtt = progAtt;
    }

    /**
     * Sets the ruralPop.
     * @param ruralPop The ruralPop to set
     */
    public void setRuralPop(long ruralPop)
    {
        this.mRuralPop = ruralPop;
    }

    /**
     * Sets the totalPop.
     * @param totalPop The totalPop to set
     */
    public void setTotalPop(double totalPop)
    {
        mTotalPop = totalPop;
    }

    // Cities
    private List<CityBean> mCities;
    public CityBean[] getCities()
    {
        return mCities.toArray(new CityBean[0]);
    }
    public void setCities(CityBean[] v)
    {
        mCities.clear();
        for (int i = 0; i < v.length; i++)
            mCities.add(v[i]);
    }
    public CityBean getCities(int index)
    {
        return mCities.get(index);
    }
    public void setCities(int index, CityBean v)
    {
        mCities.set(index, v);
    }
    public Iterator<CityBean> getCitiesIterator()
    {
        return mCities.iterator();
    }
    public void addCities(CityBean v)
    {
        mCities.add(v);
    }

    // Customs
    private List<String> mCustoms;
    public String[] getCustoms()
    {
        return mCustoms.toArray(new String[0]);
    }
    public void setCustoms(String[] v)
    {
        mCustoms.clear();
        for (int i = 0; i < v.length; i++)
            mCustoms.add(v[i]);
    }
    public String getCustoms(int index)
    {
        return mCustoms.get(index);
    }
    public void setCustoms(int index, String v)
    {
        mCustoms.set(index, v);
    }
    public Iterator<String> getCustomsIterator()
    {
        return mCustoms.iterator();
    }
    public void addCustoms(String v)
    {
        mCustoms.add(v);
    }
}
