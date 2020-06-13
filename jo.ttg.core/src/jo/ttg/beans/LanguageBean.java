package jo.ttg.beans;

import jo.util.beans.Bean;

public class LanguageBean extends Bean
{
    // LanguageStatus
    private LanguageStatsBean mLanguageStats;
    public LanguageStatsBean getLanguageStats()
    {
        return mLanguageStats;
    }
    public void setLanguageStats(LanguageStatsBean v)
    {
        mLanguageStats = v;
    }

    // Alliegence
    private java.lang.String mAlliegence;
    public java.lang.String getAlliegence()
    {
        return mAlliegence;
    }
    public void setAlliegence(java.lang.String v)
    {
        mAlliegence = v;
    }

    // Alliegence
    private java.lang.String mName;
    public java.lang.String getName()
    {
        return mName;
    }
    public void setName(java.lang.String v)
    {
        mName = v;
    }

    // constructor
    public LanguageBean()
    {
        mName = "";
        mAlliegence = "";
        mLanguageStats = new LanguageStatsBean();
    }
}
