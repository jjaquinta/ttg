package jo.ttg.beans.mw;

import jo.util.beans.Bean;

public class EconomicsBean extends Bean
{
    // Resources
    private int mResources;
    public int getResources()
    {
        return mResources;
    }
    public void setResources(int v)
    {
        mResources = v;
    }

    // Labour
    private int mLabour;
    public int getLabour()
    {
        return mLabour;
    }
    public void setLabour(int v)
    {
        mLabour = v;
    }

    // Infrastructure
    private int mInfrastructure;
    public int getInfrastructure()
    {
        return mInfrastructure;
    }
    public void setInfrastructure(int v)
    {
        mInfrastructure = v;
    }

    // Culture
    private int mCulture;
    public int getCulture()
    {
        return mCulture;
    }
    public void setCulture(int v)
    {
        mCulture = v;
    }

    // BaseDemand
    private int mBaseDemand;
    public int getBaseDemand()
    {
        return mBaseDemand;
    }
    public void setBaseDemand(int v)
    {
        mBaseDemand = v;
    }

    // TotalDemand
    private int mTotalDemand;
    public int getTotalDemand()
    {
        return mTotalDemand;
    }
    public void setTotalDemand(int v)
    {
        mTotalDemand = v;
    }

    // Imports
    private int mImports;
    public int getImports()
    {
        return mImports;
    }
    public void setImports(int v)
    {
        mImports = v;
    }

    // Exports
    private int mExports;
    public int getExports()
    {
        return mExports;
    }
    public void setExports(int v)
    {
        mExports = v;
    }

    // GWP
    private double mGWP;
    public double getGWP()
    {
        return mGWP;
    }
    public void setGWP(double v)
    {
        mGWP = v;
    }

    // ResourcesAvailable
    private double mResourcesAvailable;
    public double getResourcesAvailable()
    {
        return mResourcesAvailable;
    }
    public void setResourcesAvailable(double v)
    {
        mResourcesAvailable = v;
    }

    // ResourcesExploitable
    private double mResourcesExploitable;
    public double getResourcesExploitable()
    {
        return mResourcesExploitable;
    }
    public void setResourcesExploitable(double v)
    {
        mResourcesExploitable = v;
    }

    // LabourFactor
    private double mLabourFactor;
    public double getLabourFactor()
    {
        return mLabourFactor;
    }
    public void setLabourFactor(double v)
    {
        mLabourFactor = v;
    }

    // BaseTax
    private double mBaseTax;
    public double getBaseTax()
    {
        return mBaseTax;
    }
    public void setBaseTax(double v)
    {
        mBaseTax = v;
    }

    // SocialTax
    private double mSocialTax;
    public double getSocialTax()
    {
        return mSocialTax;
    }
    public void setSocialTax(double v)
    {
        mSocialTax = v;
    }

    // TaxRate
    private double mTaxRate;
    public double getTaxRate()
    {
        return mTaxRate;
    }
    public void setTaxRate(double v)
    {
        mTaxRate = v;
    }

    // GovernmentalBudget
    private double mGovernmentalBudget;
    public double getGovernmentalBudget()
    {
        return mGovernmentalBudget;
    }
    public void setGovernmentalBudget(double v)
    {
        mGovernmentalBudget = v;
    }

    // CivilianExpenses
    private double mCivilianExpenses;
    public double getCivilianExpenses()
    {
        return mCivilianExpenses;
    }
    public void setCivilianExpenses(double v)
    {
        mCivilianExpenses = v;
    }

    // MilitaryExpenses
    private double mMilitaryExpenses;
    public double getMilitaryExpenses()
    {
        return mMilitaryExpenses;
    }
    public void setMilitaryExpenses(double v)
    {
        mMilitaryExpenses = v;
    }

    // ImperialRevenue
    private double mImperialRevenue;
    public double getImperialRevenue()
    {
        return mImperialRevenue;
    }
    public void setImperialRevenue(double v)
    {
        mImperialRevenue = v;
    }


    // constructor
    public EconomicsBean()
    {
        mResources = 0;
        mLabour = 0;
        mInfrastructure = 0;
        mCulture = 0;
        mBaseDemand = 0;
        mTotalDemand = 0;
        mImports = 0;
        mExports = 0;
        mGWP = 0;
        mResourcesAvailable = 0;
        mResourcesExploitable = 0;
        mLabourFactor = 0;
        mBaseTax = 0;
        mSocialTax = 0;
        mTaxRate = 0;
        mGovernmentalBudget = 0;
        mCivilianExpenses = 0;
        mMilitaryExpenses = 0;
        mImperialRevenue = 0;
    }
}
