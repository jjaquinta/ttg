/*
 * Created on Feb 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.beans.chr;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class ServiceStatsBean
{
	private String mService;
	private int mEnlist;
	private int mSurvival;
	private int mCommission;
	private int mPromotion;
	private int mReenlist;
	private String mEnlistMod1;
	private String mEnlistMod2;
	private String mSurvivalMod;
	private String mCommissionMod;
	private String mPromotionMod;
	private String[] mTitles;
	private String[] mMusterBenefits;
	private String[] mMusterMoney;
	private String[][] mSkills;
	private String[] mRankSkills;
	
    public int getCommission()
    {
        return mCommission;
    }
    public void setCommission(int commission)
    {
        mCommission = commission;
    }
    public String getCommissionMod()
    {
        return mCommissionMod;
    }
    public void setCommissionMod(String commissionMod)
    {
        mCommissionMod = commissionMod;
    }
    public int getEnlist()
    {
        return mEnlist;
    }
    public void setEnlist(int enlist)
    {
        mEnlist = enlist;
    }
    public String getEnlistMod1()
    {
        return mEnlistMod1;
    }
    public void setEnlistMod1(String enlistMod1)
    {
        mEnlistMod1 = enlistMod1;
    }
    public String getEnlistMod2()
    {
        return mEnlistMod2;
    }
    public void setEnlistMod2(String enlistMod2)
    {
        mEnlistMod2 = enlistMod2;
    }
    public String[] getMusterBenefits()
    {
        return mMusterBenefits;
    }
    public void setMusterBenefits(String[] musterBenefits)
    {
        mMusterBenefits = musterBenefits;
    }
    public String[] getMusterMoney()
    {
        return mMusterMoney;
    }
    public void setMusterMoney(String[] musterMoney)
    {
        mMusterMoney = musterMoney;
    }
    public int getPromotion()
    {
        return mPromotion;
    }
    public void setPromotion(int promotion)
    {
        mPromotion = promotion;
    }
    public String getPromotionMod()
    {
        return mPromotionMod;
    }
    public void setPromotionMod(String promotionMod)
    {
        mPromotionMod = promotionMod;
    }
    public String[] getRankSkills()
    {
        return mRankSkills;
    }
    public void setRankSkills(String[] rankSkills)
    {
        mRankSkills = rankSkills;
    }
    public int getReenlist()
    {
        return mReenlist;
    }
    public void setReenlist(int reenlist)
    {
        mReenlist = reenlist;
    }
    public String getService()
    {
        return mService;
    }
    public void setService(String service)
    {
        mService = service;
    }
    public String[][] getSkills()
    {
        return mSkills;
    }
    public void setSkills(String[][] skills)
    {
        mSkills = skills;
    }
    public int getSurvival()
    {
        return mSurvival;
    }
    public void setSurvival(int survival)
    {
        mSurvival = survival;
    }
    public String getSurvivalMod()
    {
        return mSurvivalMod;
    }
    public void setSurvivalMod(String survivalMod)
    {
        mSurvivalMod = survivalMod;
    }
    public String[] getTitles()
    {
        return mTitles;
    }
    public void setTitles(String[] titles)
    {
        mTitles = titles;
    }
}
