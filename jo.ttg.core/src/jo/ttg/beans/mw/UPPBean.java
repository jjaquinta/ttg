package jo.ttg.beans.mw;

import jo.util.beans.Bean;

public class UPPBean extends Bean
{
    // trade code offsets
    public final static int TC_AG = 0;
    public final static int TC_AS = 1;
    public final static int TC_BA = 2;
    public final static int TC_DE = 3;
    public final static int TC_FL = 4;
    public final static int TC_HI = 5;
    public final static int TC_IC = 6;
    public final static int TC_IN = 7;
    public final static int TC_LO = 8;
    public final static int TC_NA = 9;
    public final static int TC_NI = 10;
    public final static int TC_PO = 11;
    public final static int TC_RI = 12;
    public final static int TC_VA = 13;
    public final static int TC_WA = 14;
    
    public static final int[] TRADE_CODES = {
        TC_AG,
        TC_AS,
        TC_BA,
        TC_DE,
        TC_FL,
        TC_HI,
        TC_IC,
        TC_IN,
        TC_LO,
        TC_NA,
        TC_NI,
        TC_PO,
        TC_RI,
        TC_VA,
        TC_WA,
    };

    private long    mTradeCodes;

    public boolean isAg()
    {
        return isTradeCode(TC_AG);
    }
    public boolean isAs()
    {
        return isTradeCode(TC_AS);
    }
    public boolean isBa()
    {
        return isTradeCode(TC_BA);
    }
    public boolean isDe()
    {
        return isTradeCode(TC_DE);
    }
    public boolean isFl()
    {
        return isTradeCode(TC_FL);
    }
    public boolean isHi()
    {
        return isTradeCode(TC_HI);
    }
    public boolean isIc()
    {
        return isTradeCode(TC_IC);
    }
    public boolean isIn()
    {
        return isTradeCode(TC_IN);
    }
    public boolean isLo()
    {
        return isTradeCode(TC_LO);
    }
    public boolean isNa()
    {
        return isTradeCode(TC_NA);
    }
    public boolean isNi()
    {
        return isTradeCode(TC_NI);
    }
    public boolean isPo()
    {
        return isTradeCode(TC_PO);
    }
    public boolean isRi()
    {
        return isTradeCode(TC_RI);
    }
    public boolean isVa()
    {
        return isTradeCode(TC_VA);
    }
    public boolean isWa()
    {
        return isTradeCode(TC_WA);
    }
    public boolean isTradeCode(int n)
    {
        return ((mTradeCodes & (1 << n)) != 0);
    }

    // Port
    private UPPPorBean mPort;
    public UPPPorBean getPort()
    {
        return mPort;
    }
    public void setPort(UPPPorBean v)
    {
        mPort = v;
    }

    // Size
    private UPPSizBean mSize;
    public UPPSizBean getSize()
    {
        return mSize;
    }
    public void setSize(UPPSizBean v)
    {
        mSize = v;
    }

    // Atmos
    private UPPAtmBean mAtmos;
    public UPPAtmBean getAtmos()
    {
        return mAtmos;
    }
    public void setAtmos(UPPAtmBean v)
    {
        mAtmos = v;
    }

    // Hydro
    private UPPHydBean mHydro;
    public UPPHydBean getHydro()
    {
        return mHydro;
    }
    public void setHydro(UPPHydBean v)
    {
        mHydro = v;
    }

    // Pop
    private UPPPopBean mPop;
    public UPPPopBean getPop()
    {
        return mPop;
    }
    public void setPop(UPPPopBean v)
    {
        mPop = v;
    }

    // Gov
    private UPPGovBean mGov;
    public UPPGovBean getGov()
    {
        return mGov;
    }
    public void setGov(UPPGovBean v)
    {
        mGov = v;
    }

    // Law
    private UPPLawBean mLaw;
    public UPPLawBean getLaw()
    {
        return mLaw;
    }
    public void setLaw(UPPLawBean v)
    {
        mLaw = v;
    }

    // Tech
    private UPPTecBean mTech;
    public UPPTecBean getTech()
    {
        return mTech;
    }
    public void setTech(UPPTecBean v)
    {
        mTech = v;
    }


    // constructor
    public UPPBean()
    {
        mPort = new UPPPorBean();
        mSize = new UPPSizBean();
        mAtmos = new UPPAtmBean();
        mHydro = new UPPHydBean();
        mPop = new UPPPopBean();
        mGov = new UPPGovBean();
        mLaw = new UPPLawBean();
        mTech = new UPPTecBean();
    }
    /**
     * @return Returns the tradeCodes.
     */
    public long getTradeCodes()
    {
        return mTradeCodes;
    }
    /**
     * @param tradeCodes The tradeCodes to set.
     */
    public void setTradeCodes(long tradeCodes)
    {
        mTradeCodes = tradeCodes;
    }
}
