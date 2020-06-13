package jo.ttg.beans.trade;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.URIBean;
import jo.ttg.utils.DisplayUtils;
import jo.util.beans.Bean;

public class CargoBean extends Bean implements URIBean
{
    /**
     * Cargo type natural resources
     * 
     * @see ttg.CargoBean#Type
     */
    public static final int      GT_NATURAL      = 0;
    /**
     * Cargo type processed resources
     * 
     * @see ttg.CargoBean#Type
     */
    public static final int      GT_PROCESSED    = 1;
    /**
     * Cargo type manufactured resources
     * 
     * @see ttg.CargoBean#Type
     */
    public static final int      GT_MANUFACTURED = 2;
    /**
     * Cargo type information
     * 
     * @see ttg.CargoBean#Type
     */
    public static final int      GT_INFORMATION  = 3;
    /**
     * Cargo type novelties
     * 
     * @see ttg.CargoBean#Type
     */
    public static final int      GT_NOVELTIES    = 4;

    public static final String[] GT_DESCRIPTION  = { "Natural", "Processed",
            "Manufactured", "Information", "Novelties", };

    /**
     * Cargo category organic
     * 
     * @see ttg.CargoBean#Phylum
     */
    public static final int      CP_ORGANIC      = 0;
    /**
     * Cargo category inorganic
     * 
     * @see ttg.CargoBean#Phylum
     */
    public static final int      CP_INORGANIC    = 1;
    /**
     * Cargo miscelaneous category
     * 
     * @see ttg.CargoBean#Phylum
     */
    public static final int      CP_DONTCARE     = 2;
    /**
     * Cargo category nonorganic
     * 
     * @see ttg.CargoBean#Phylum
     */
    public static final int      CP_NONORGANIC   = 3;
    /**
     * Cargo category noninorganic
     * 
     * @see ttg.CargoBean#Phylum
     */
    public static final int      CP_NONINORGANIC = 4;

    public static final String[] CP_DESCRIPTION  = { "Organic", "Inorganic",
            "Don't Care", "Non-Organic", "Non-Inorganic", };

    /**
     * Cargo class cargo
     * 
     * @see ttg.CargoBean#Class
     */
    public static final int      CC_CARGO        = 0;
    /**
     * Cargo class freight
     * 
     * @see ttg.CargoBean#Class
     */
    public static final int      CC_FREIGHT      = 1;
    /**
     * Cargo class special
     * 
     * @see ttg.CargoBean#Class
     */
    public static final int      CC_SPECIAL      = 2;

    private String               mOrigin;
    private String               mDestination;
    private int                  mPhylum;
    private int                  mType;
    private int                  mClassification;
    private String               mName;
    private boolean              mCor;
    private boolean              mFla;
    private boolean              mExp;
    private boolean              mRad;
    private boolean              mPer;
    private boolean              mFra;
    private int                  mLegality;
    private long                 mBaseValue;
    private int                  mQuantity;
    private DateBean             mBestBy;
    private DateBean             mAvailableFirst;
    private DateBean             mAvailableLast;
    private int                  mTechLevel;
    private String               mURI;
    private double               mPurchasePrice;
    private double               mSalePrice;

    public CargoBean()
    {
        mOrigin = null;
        mDestination = null;
        mPhylum = 0;
        mType = 0;
        mClassification = 0;
        mName = new String();
        mCor = false;
        mFla = false;
        mExp = false;
        mRad = false;
        mPer = false;
        mFra = false;
        mLegality = 0;
        mBaseValue = 0;
        mQuantity = 0;
        mBestBy = new DateBean();
        mAvailableFirst = new DateBean();
        mAvailableLast = new DateBean();
        mTechLevel = 0;
    }

    public String getWarnings()
    {
        StringBuffer ret = new StringBuffer();
        if (isCor())
            ret.append("Cor");
        if (isFla())
            ret.append("Fla");
        if (isExp())
            ret.append("Exp");
        if (isRad())
            ret.append("Rad");
        if (isPer())
            ret.append("Per");
        if (isFra())
            ret.append("Fra");
        return ret.toString();
    }

    public String toString()
    {
        return mName + " " + DisplayUtils.formatTons(mQuantity);
    }

    public String getOrigin()
    {
        return mOrigin;
    }

    public void setOrigin(String v)
    {
        mOrigin = v;
    }

    public String getDestination()
    {
        return mDestination;
    }

    public void setDestination(String v)
    {
        mDestination = v;
    }

    public int getPhylum()
    {
        return mPhylum;
    }

    public void setPhylum(int v)
    {
        mPhylum = v;
    }

    public int getType()
    {
        return mType;
    }

    public void setType(int v)
    {
        mType = v;
    }

    public int getClassification()
    {
        return mClassification;
    }

    public void setClassification(int v)
    {
        mClassification = v;
    }

    public java.lang.String getName()
    {
        return mName;
    }

    public void setName(java.lang.String v)
    {
        mName = v;
    }

    public boolean isCor()
    {
        return mCor;
    }

    public void setCor(boolean v)
    {
        mCor = v;
    }

    public boolean isFla()
    {
        return mFla;
    }

    public void setFla(boolean v)
    {
        mFla = v;
    }

    public boolean isExp()
    {
        return mExp;
    }

    public void setExp(boolean v)
    {
        mExp = v;
    }

    public boolean isRad()
    {
        return mRad;
    }

    public void setRad(boolean v)
    {
        mRad = v;
    }

    public boolean isPer()
    {
        return mPer;
    }

    public void setPer(boolean v)
    {
        mPer = v;
    }

    public boolean isFra()
    {
        return mFra;
    }

    public void setFra(boolean v)
    {
        mFra = v;
    }

    public int getLegality()
    {
        return mLegality;
    }

    public void setLegality(int v)
    {
        mLegality = v;
    }

    public long getBaseValue()
    {
        return mBaseValue;
    }

    public void setBaseValue(long v)
    {
        mBaseValue = v;
    }

    public int getQuantity()
    {
        return mQuantity;
    }

    public void setQuantity(int v)
    {
        mQuantity = v;
    }

    public DateBean getBestBy()
    {
        return mBestBy;
    }

    public void setBestBy(DateBean v)
    {
        mBestBy = v;
    }

    public DateBean getAvailableFirst()
    {
        return mAvailableFirst;
    }

    public void setAvailableFirst(DateBean v)
    {
        mAvailableFirst = v;
    }

    public DateBean getAvailableLast()
    {
        return mAvailableLast;
    }

    public void setAvailableLast(DateBean v)
    {
        mAvailableLast = v;
    }

    public int getTechLevel()
    {
        return mTechLevel;
    }

    public void setTechLevel(int v)
    {
        mTechLevel = v;
    }

    public String getURI()
    {
        return mURI;
    }

    public void setURI(String uRI)
    {
        mURI = uRI;
    }

    public double getPurchasePrice()
    {
        return mPurchasePrice;
    }

    public void setPurchasePrice(double purchasePrice)
    {
        mPurchasePrice = purchasePrice;
    }

    public double getSalePrice()
    {
        return mSalePrice;
    }

    public void setSalePrice(double salePrice)
    {
        mSalePrice = salePrice;
    }
}
