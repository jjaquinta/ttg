/**
 * Created on Oct 16, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package jo.ttg.beans.sys;

import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.logic.mw.UPPLogic;

/**
 * @author jgrant
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class BodyWorldBean extends BodyBean implements BodyPopulated
{
    /**
      * Mainworld statistics for populated belt.
      */
    private PopulatedStatsBean mPopulatedStats;

    private StatsPorBean    mStatsPor;
    private StatsSizBean    mStatsSiz;
    private StatsAtmBean    mStatsAtm;
    private StatsHydBean    mStatsHyd;
    private StatsPopBean    mStatsPop;
    private StatsGovBean    mStatsGov;
    private StatsLawBean    mStatsLaw;
    private StatsTecBean    mStatsTec;

    public BodyWorldBean()
    {
        mPopulatedStats = new PopulatedStatsBean();
        mStatsPor = new StatsPorBean();
        mStatsSiz = new StatsSizBean();
        mStatsAtm = new StatsAtmBean();
        mStatsHyd = new StatsHydBean();
        mStatsPop = new StatsPopBean();
        mStatsGov = new StatsGovBean();
        mStatsLaw = new StatsLawBean();
        mStatsTec = new StatsTecBean();
        setType(BT_WORLD);
    }

    /**
     * Returns the mPopulatedStats.
     * @return PopulatedStatsBean
     */
    public PopulatedStatsBean getPopulatedStats()
    {
        return mPopulatedStats;
    }

    /**
     * Returns the mStatsAtm.
     * @return StatsAtmBean
     */
    public StatsAtmBean getStatsAtm()
    {
        return mStatsAtm;
    }

    /**
     * Returns the mStatsGov.
     * @return StatsGovBean
     */
    public StatsGovBean getStatsGov()
    {
        return mStatsGov;
    }

    /**
     * Returns the mStatsHyd.
     * @return StatsHydBean
     */
    public StatsHydBean getStatsHyd()
    {
        return mStatsHyd;
    }

    /**
     * Returns the mStatsLaw.
     * @return StatsLawBean
     */
    public StatsLawBean getStatsLaw()
    {
        return mStatsLaw;
    }

    /**
     * Returns the mStatsPop.
     * @return StatsPopBean
     */
    public StatsPopBean getStatsPop()
    {
        return mStatsPop;
    }

    /**
     * Returns the mStatsPor.
     * @return StatsPorBean
     */
    public StatsPorBean getStatsPor()
    {
        return mStatsPor;
    }

    /**
     * Returns the mStatsSiz.
     * @return StatsSizBean
     */
    public StatsSizBean getStatsSiz()
    {
        return mStatsSiz;
    }

    /**
     * Returns the mStatsTec.
     * @return StatsTecBean
     */
    public StatsTecBean getStatsTec()
    {
        return mStatsTec;
    }

    /**
     * Sets the mPopulatedStats.
     * @param mPopulatedStats The mPopulatedStats to set
     */
    public void setPopulatedStats(PopulatedStatsBean mPopulatedStats)
    {
        this.mPopulatedStats = mPopulatedStats;
    }

    /**
     * Sets the mStatsAtm.
     * @param mStatsAtm The mStatsAtm to set
     */
    public void setStatsAtm(StatsAtmBean mStatsAtm)
    {
        this.mStatsAtm = mStatsAtm;
    }

    /**
     * Sets the mStatsGov.
     * @param mStatsGov The mStatsGov to set
     */
    public void setStatsGov(StatsGovBean mStatsGov)
    {
        this.mStatsGov = mStatsGov;
    }

    /**
     * Sets the mStatsHyd.
     * @param mStatsHyd The mStatsHyd to set
     */
    public void setStatsHyd(StatsHydBean mStatsHyd)
    {
        this.mStatsHyd = mStatsHyd;
    }

    /**
     * Sets the mStatsLaw.
     * @param mStatsLaw The mStatsLaw to set
     */
    public void setStatsLaw(StatsLawBean mStatsLaw)
    {
        this.mStatsLaw = mStatsLaw;
    }

    /**
     * Sets the mStatsPop.
     * @param mStatsPop The mStatsPop to set
     */
    public void setStatsPop(StatsPopBean mStatsPop)
    {
        this.mStatsPop = mStatsPop;
    }

    /**
     * Sets the mStatsPor.
     * @param mStatsPor The mStatsPor to set
     */
    public void setStatsPor(StatsPorBean mStatsPor)
    {
        this.mStatsPor = mStatsPor;
    }

    /**
     * Sets the mStatsSiz.
     * @param mStatsSiz The mStatsSiz to set
     */
    public void setStatsSiz(StatsSizBean mStatsSiz)
    {
        this.mStatsSiz = mStatsSiz;
    }

    /**
     * Sets the mStatsTec.
     * @param mStatsTec The mStatsTec to set
     */
    public void setStatsTec(StatsTecBean mStatsTec)
    {
        this.mStatsTec = mStatsTec;
    }
    
    public String toString()
    {
        return getName()+" "+getPopulatedStats().toString();
    }
    /**
     * One line description of belt
     * Generate a one line description
     * @return belt description
     * @see ttg.TBody#sOneLine
     */
    public String getOneLineDesc()
    {
        return (getName() + "              ").substring(0, 14)
            + " "
            + UPPLogic.getUPPDesc(mPopulatedStats.getUPP())
            + " "
            + mPopulatedStats.getBasesDesc()
            + " "
            + UPPLogic.getTradeCodesDesc(mPopulatedStats.getUPP())
            + " "
            + String.valueOf(mPopulatedStats.getTravelZone())
            + "00"
            /*+ TUtil.sNum(mPopulatedStats.getPopDigit())*/;
    }
}
