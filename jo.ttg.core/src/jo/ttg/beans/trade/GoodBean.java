package jo.ttg.beans.trade;

import jo.util.beans.Bean;

public class GoodBean extends Bean
{
    // Chance
    private int mChance;
    public int getChance()
    {
        return mChance;
    }
    public void setChance(int v)
    {
        mChance = v;
    }

    // Type
    private int mType;
    public int getType()
    {
        return mType;
    }
    public void setType(int v)
    {
        mType = v;
    }

    // Phylum
    private int mPhylum;
    public int getPhylum()
    {
        return mPhylum;
    }
    public void setPhylum(int v)
    {
        mPhylum = v;
    }

    // Name
    private java.lang.String mName;
    public java.lang.String getName()
    {
        return mName;
    }
    public void setName(java.lang.String v)
    {
        mName = v;
    }

    // ChanceCor
    private int mChanceCor;
    public int getChanceCor()
    {
        return mChanceCor;
    }
    public void setChanceCor(int v)
    {
        mChanceCor = v;
    }

    // ChanceFla
    private int mChanceFla;
    public int getChanceFla()
    {
        return mChanceFla;
    }
    public void setChanceFla(int v)
    {
        mChanceFla = v;
    }

    // ChanceExp
    private int mChanceExp;
    public int getChanceExp()
    {
        return mChanceExp;
    }
    public void setChanceExp(int v)
    {
        mChanceExp = v;
    }

    // ChanceRad
    private int mChanceRad;
    public int getChanceRad()
    {
        return mChanceRad;
    }
    public void setChanceRad(int v)
    {
        mChanceRad = v;
    }

    // ChancePer
    private int mChancePer;
    public int getChancePer()
    {
        return mChancePer;
    }
    public void setChancePer(int v)
    {
        mChancePer = v;
    }

    // ChanceFra
    private int mChanceFra;
    public int getChanceFra()
    {
        return mChanceFra;
    }
    public void setChanceFra(int v)
    {
        mChanceFra = v;
    }

    // Legality
    private int mLegality;
    public int getLegality()
    {
        return mLegality;
    }
    public void setLegality(int v)
    {
        mLegality = v;
    }

    // BaseValue
    private long mBaseValue;
    public long getBaseValue()
    {
        return mBaseValue;
    }
    public void setBaseValue(long v)
    {
        mBaseValue = v;
    }

	// TechLevel
	private int mTechLevel;
	public int getTechLevel()
	{
		return mTechLevel;
	}
	public void setTechLevel(int v)
	{
		mTechLevel = v;
	}

    public boolean applicable(int phylum)
    {
        switch (phylum)
        {
            case CargoBean.CP_DONTCARE:
                return true;
            case CargoBean.CP_ORGANIC:
                return getPhylum() == CargoBean.CP_ORGANIC;
            case CargoBean.CP_INORGANIC:
                return getPhylum() == CargoBean.CP_INORGANIC;
            case CargoBean.CP_NONORGANIC:
                return getPhylum() != CargoBean.CP_ORGANIC;
            case CargoBean.CP_NONINORGANIC:
                return getPhylum() != CargoBean.CP_INORGANIC;
        }
        return false;
    }

    // constructor
    public GoodBean()
    {
        mChance = 0;
        mType = 0;
        mPhylum = 0;
        mName = new java.lang.String();
        mChanceCor = 0;
        mChanceFla = 0;
        mChanceExp = 0;
        mChanceRad = 0;
        mChancePer = 0;
        mChanceFra = 0;
        mLegality = 0;
        mBaseValue = 0;
        mTechLevel = 0;
    }
}
