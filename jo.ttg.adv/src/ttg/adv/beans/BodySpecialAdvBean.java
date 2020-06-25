/*
 * Created on Dec 19, 2004
 *
 */
package ttg.adv.beans;

import jo.ttg.beans.mw.UPPPorBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.trade.GoodBean;

/**
 * @author Jo
 *
 */
public class BodySpecialAdvBean extends BodySpecialBean
{
    private int	mDemand;
    private int	mProduction;
    private GoodBean	mDemandGood;
    private GoodBean	mProductionGood;
    
    public BodySpecialAdvBean()
    {
    }

    /* (non-Javadoc)
     * @see ttg.beans.sys.BodySpecialBean#getOneLineDesc()
     */
    public String getOneLineDesc()
    {
        switch (getSubType())
        {
            case BodySpecialBean.ST_STARPORT:
            case BodySpecialBean.ST_SPACEPORT:
                return getName()+" (type "+((char)((UPPPorBean)getSpecialInfo()).getValue())+")";
        }
        return getName();
    }
    /**
     * @return Returns the demand.
     */
    public int getDemand()
    {
        return mDemand;
    }
    /**
     * @param demand The demand to set.
     */
    public void setDemand(int demand)
    {
        mDemand = demand;
    }
    /**
     * @return Returns the production.
     */
    public int getProduction()
    {
        return mProduction;
    }
    public GoodBean getDemandGood()
    {
        return mDemandGood;
    }
    public void setDemandGood(GoodBean demandGood)
    {
        mDemandGood = demandGood;
    }
    public GoodBean getProductionGood()
    {
        return mProductionGood;
    }
    public void setProductionGood(GoodBean productionGood)
    {
        mProductionGood = productionGood;
    }
    /**
     * @param production The production to set.
     */
    public void setProduction(int production)
    {
        mProduction = production;
    }
}
