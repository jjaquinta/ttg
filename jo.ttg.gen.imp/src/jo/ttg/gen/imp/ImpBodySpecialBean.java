package jo.ttg.gen.imp;

import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.trade.GoodBean;

public class ImpBodySpecialBean extends BodySpecialBean
{
    private int mDemand;
    private int mProduction;
    private GoodBean    mDemandGood;
    private GoodBean    mProductionGood;

    @Override
    public String getOneLineDesc()
    {        
        return getName();
    }

    public int getDemand()
    {
        return mDemand;
    }

    public void setDemand(int demand)
    {
        mDemand = demand;
    }

    public GoodBean getDemandGood()
    {
        return mDemandGood;
    }

    public void setDemandGood(GoodBean demandGood)
    {
        mDemandGood = demandGood;
    }

    public int getProduction()
    {
        return mProduction;
    }

    public void setProduction(int production)
    {
        mProduction = production;
    }

    public GoodBean getProductionGood()
    {
        return mProductionGood;
    }

    public void setProductionGood(GoodBean productionGood)
    {
        mProductionGood = productionGood;
    }

}
