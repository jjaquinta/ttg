package jo.ttg.beans.sys;

import jo.util.beans.Bean;

/**
 * @author Joseph Jaquinta
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class StatsGovBranchBean extends Bean
{
    /**
      * No Government Branch Organization
      * @see ttg.TBodyWorld#GovOrg
      */
    public static final int GO_NONE = 0;
    /**
      * Government Branch Organized by Demos
      * @see ttg.TBodyWorld#GovOrg
      */
    public static final int GO_DEMOS = 1;
    /**
      * Government Branch Organized by Elite Council
      * @see ttg.TBodyWorld#GovOrg
      */
    public static final int GO_ECOUNCIL = 2;
    /**
      * Government Branch Organized by Ruler
      * @see ttg.TBodyWorld#GovOrg
      */
    public static final int GO_RULER = 3;
    /**
      * Government Branch Organized by Council
      * @see ttg.TBodyWorld#GovOrg
      */
    public static final int GO_SCOUNCIL = 4;
    
    public static final String[] ORGANIZATION_DESCRIPTION = {
         "No Organization",
         "Organized by Demos",
         "Organized by Elite Council",
         "Organized by Ruler",
         "Organized by Council",
    };

    private boolean mLegislative;
    private boolean mExecutive;
    private boolean mJudicial;
    private int mOrganization;

    public StatsGovBranchBean()
    {
        mLegislative = false;
        mExecutive = false;
        mJudicial = false;
        mOrganization = GO_NONE;
    }

    /**
     * Returns the executive.
     * @return boolean
     */
    public boolean isExecutive()
    {
        return mExecutive;
    }

    /**
     * Returns the judicial.
     * @return boolean
     */
    public boolean isJudicial()
    {
        return mJudicial;
    }

    /**
     * Returns the legislative.
     * @return boolean
     */
    public boolean isLegislative()
    {
        return mLegislative;
    }

    /**
     * Sets the executive.
     * @param executive The executive to set
     */
    public void setExecutive(boolean executive)
    {
        mExecutive = executive;
    }

    /**
     * Sets the judicial.
     * @param judicial The judicial to set
     */
    public void setJudicial(boolean judicial)
    {
        mJudicial = judicial;
    }

    /**
     * Sets the legislative.
     * @param legislative The legislative to set
     */
    public void setLegislative(boolean legislative)
    {
        mLegislative = legislative;
    }

    /**
     * Returns the organization.
     * @return int
     */
    public int getOrganization()
    {
        return mOrganization;
    }

    /**
     * Sets the organization.
     * @param organization The organization to set
     */
    public void setOrganization(int organization)
    {
        mOrganization = organization;
    }

    public static String getOrganizationDescription(int value)
    {
        return ORGANIZATION_DESCRIPTION[value];
    }

    public String getOrganizationDescription()
    {
        return getOrganizationDescription(getOrganization());
    }
    
    public String getCoverage()
    {
        StringBuffer coverage = new StringBuffer();
        if (isExecutive())
            coverage.append("Executive");
        if (isLegislative())
        {
            if (coverage.length() > 0)
                coverage.append(", ");
            coverage.append("Legislative");
        }
        if (isJudicial())
        {
            if (coverage.length() > 0)
                coverage.append(", ");
            coverage.append("Judicial");
        }
        return coverage.toString();
    }
}
