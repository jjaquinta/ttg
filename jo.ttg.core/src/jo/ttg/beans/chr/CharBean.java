/*
 * Created on Dec 6, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.beans.chr;

import jo.ttg.beans.HashBean;
import jo.util.beans.PCSBean;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CharBean extends PCSBean
{
	static public final int P_PILOT    = 1;
	static public final int P_NAV      = 2;
	static public final int P_ENGINEER = 3;
	static public final int P_STEWARD  = 4;
	static public final int P_MEDIC    = 5;
	static public final int P_GUNNER   = 6;
	
	static public final String S_STR = "Str";
	static public final String S_DEX = "Dex";
	static public final String S_END = "End";
	static public final String S_INT = "Int";
	static public final String S_EDU = "Edu";
	static public final String S_SOC = "Soc";
	static public final String S_AGE = "Age";
	static public final String S_RANK = "Rank";
	static public final String S_TERMS = "Terms";
	static public final String S_MONEY = "Cr";

	static final String[] sSkills = {
		"",
		"Pilot",
		"Navigator",
		"Engineer",
		"Steward",
		"Medic",
		"Gunner",
	};


	private HashBean mSkills;
	private String  mService;
	private String  mTitle;
	private String  mName;
	private int[]   mUpp;
	private double  mMoney;
	private double  mSalary;
	private int     mTerms;
	private int     mAge;
	private int     mRank;
	private int     mPosition;
	private boolean	mMale;
	private String	mBestSkill;
	private String	mSecondBestSkill;

	// constructor

	public CharBean()
	{
		mSkills = new HashBean();
		mService = null;
		mUpp = new int[6];
	}
	
	// utils
	
	public int getStat(String what)
	{
		if (what == null)
			return 0;
		if (mSkills.containsKey(what))
			return (Integer)mSkills.get(what);
		if (what.equals(S_STR))
			return mUpp[0];
		if (what.equals(S_DEX))
			return mUpp[1];
		if (what.equals(S_END))
			return mUpp[2];
		if (what.equals(S_INT))
			return mUpp[3];
		if (what.equals(S_EDU))
			return mUpp[4];
		if (what.equals(S_SOC))
			return mUpp[5];
		return 0;
	}
	
	public int getSkill(String what)
	{
		if (what == null)
			return 0;
		if (isStat(what))
			return getStat(what)/4;
		Integer i = (Integer)mSkills.get(what);
		if (i == null)
			return -1;
		return i.intValue();
	}
    
    public int getBaseSkill(String what)
    {
        Integer i = (Integer)mSkills.get(what);
        if (i == null)
            return -1;
        return i.intValue();
    }
	
	public int getSkill(int what)
	{
		Integer i = (Integer)mSkills.get(sSkills[what]);
		if (i == null)
			return 0;
		return i.intValue();
	}

	public void setSkill(String what, int amnt)
	{
		mSkills.put(what, new Integer(amnt));
	}

	// gets & sets

	/**
	 * @return
	 */
	public int getAge()
	{
		return mAge;
	}

	/**
	 * @return
	 */
	public boolean isMale()
	{
		return mMale;
	}

	/**
	 * @return
	 */
	public double getMoney()
	{
		return mMoney;
	}

	/**
	 * @return
	 */
	public String getName()
	{
		return mName;
	}

	/**
	 * @return
	 */
	public int getPosition()
	{
		return mPosition;
	}

	/**
	 * @return
	 */
	public int getRank()
	{
		return mRank;
	}

	/**
	 * @return
	 */
	public double getSalary()
	{
		return mSalary;
	}

	/**
	 * @return
	 */
	public String getService()
	{
		return mService;
	}

	/**
	 * @return
	 */
	public HashBean getSkills()
	{
		return mSkills;
	}

	/**
	 * @return
	 */
	public int getTerms()
	{
		return mTerms;
	}

	/**
	 * @return
	 */
	public String getTitle()
	{
		return mTitle;
	}

	/**
	 * @return
	 */
	public int[] getUpp()
	{
		return mUpp;
	}

	/**
	 * @param i
	 */
	public void setAge(int i)
	{
		mAge = i;
	}

	/**
	 * @param b
	 */
	public void setMale(boolean b)
	{
		mMale = b;
	}

	/**
	 * @param d
	 */
	public void setMoney(double d)
	{
		mMoney = d;
	}

	/**
	 * @param string
	 */
	public void setName(String string)
	{
		mName = string;
	}

	/**
	 * @param i
	 */
	public void setPosition(int i)
	{
		mPosition = i;
	}

	/**
	 * @param i
	 */
	public void setRank(int i)
	{
		mRank = i;
	}

	/**
	 * @param d
	 */
	public void setSalary(double d)
	{
		mSalary = d;
	}

	/**
	 * @param string
	 */
	public void setService(String string)
	{
		mService = string;
	}

	/**
	 * @param map
	 */
	public void setSkills(HashBean map)
	{
		mSkills = map;
	}

	/**
	 * @param i
	 */
	public void setTerms(int i)
	{
		mTerms = i;
	}

	/**
	 * @param string
	 */
	public void setTitle(String string)
	{
		mTitle = string;
	}

	/**
	 * @param is
	 */
	public void setUpp(int[] is)
	{
		mUpp = is;
	}

	/**
	 * @return
	 */
	public String getBestSkill()
	{
		return mBestSkill;
	}

	/**
	 * @return
	 */
	public String getSecondBestSkill()
	{
		return mSecondBestSkill;
	}

	/**
	 * @param string
	 */
	public void setBestSkill(String string)
	{
		mBestSkill = string;
	}

	/**
	 * @param string
	 */
	public void setSecondBestSkill(String string)
	{
		mSecondBestSkill = string;
	}
	
	public static boolean isStat(String what)
	{
	    return S_STR.equals(what) || S_DEX.equals(what) || S_END.equals(what)
	    	|| S_INT.equals(what) || S_EDU.equals(what) || S_SOC.equals(what);
	}
    
    public static int getWhichStat(String what)
    {
        if (S_STR.equals(what))
            return 0;
        if (S_DEX.equals(what))
            return 1;
        if (S_END.equals(what))
            return 2;
        if (S_INT.equals(what))
            return 3;
        if (S_EDU.equals(what))
            return 4;
        if (S_SOC.equals(what))
            return 5;
        return -1;
    }
    
    public String getFirstName()
    {
        int o = getName().indexOf(' ');
        if (o >= 0)
            return getName().substring(0, o);
        else
            return "";
    }
    
    public String getLastName()
    {
        int o = getName().indexOf(' ');
        if (o >= 0)
            return getName().substring(o + 1);
        else
            return getName();
    }
}
