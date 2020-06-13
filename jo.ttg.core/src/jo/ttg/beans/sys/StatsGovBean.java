package jo.ttg.beans.sys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.util.beans.Bean;

/**
 * @author Joseph Jaquinta
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class StatsGovBean extends Bean
{
    /**
      * Government organizational composition
      * Goverments are composed of up to three branches. Each branch contains one or more of
      * Legislative, Executive and Judcial. This is a bitfield of those values.
      */

    // Branches
    private List<StatsGovBranchBean> mBranches;
    public StatsGovBranchBean[] getBranches()
    {
        return mBranches.toArray(new StatsGovBranchBean[0]);
    }
    public void setBranches(StatsGovBranchBean[] v)
    {
        mBranches.clear();
        for (int i = 0; i < v.length; i++)
            mBranches.add(v[i]);
    }
    public StatsGovBranchBean getBranches(int index)
    {
        return mBranches.get(index);
    }
    public void setBranches(int index, StatsGovBranchBean v)
    {
        mBranches.set(index, v);
    }
    public Iterator<StatsGovBranchBean> getBranchesIterator()
    {
        return mBranches.iterator();
    }
    public void addBranches(StatsGovBranchBean v)
    {
        mBranches.add(v);
    }
    /**
      * God view
      */
    private int mGodView;
    /**
      * Spiritual Aim
      */
    private int mSpiritualAim;
    /**
      * Devotion Required
      */
    private int mDevotionRequired;
    /**
      * Religious Organization
      */
    private int mReligiousOrganisation;
    /**
      * Liturgical Formality
      */
    private int mLiturgicalFormality;
    /**
      * Missionary Fervour
      */
    private int mMissionaryFervour;

    // constructor

    public StatsGovBean()
    {
        mGodView = -1;
        mSpiritualAim = -1;
        mDevotionRequired = -1;
        mReligiousOrganisation = -1;
        mLiturgicalFormality = -1;
        mMissionaryFervour = -1;
        mBranches = new ArrayList<StatsGovBranchBean>();
    }

    /**
     * Returns the devotionRequired.
     * @return int
     */
    public int getDevotionRequired()
    {
        return mDevotionRequired;
    }

    /**
     * Returns the godView.
     * @return int
     */
    public int getGodView()
    {
        return mGodView;
    }

    /**
     * Returns the liturgicalFormality.
     * @return int
     */
    public int getLiturgicalFormality()
    {
        return mLiturgicalFormality;
    }

    /**
     * Returns the missionaryFervour.
     * @return int
     */
    public int getMissionaryFervour()
    {
        return mMissionaryFervour;
    }

    /**
     * Returns the religiousOrganisation.
     * @return int
     */
    public int getReligiousOrganisation()
    {
        return mReligiousOrganisation;
    }

    /**
     * Returns the spiritualAim.
     * @return int
     */
    public int getSpiritualAim()
    {
        return mSpiritualAim;
    }

    /**
     * Sets the devotionRequired.
     * @param devotionRequired The devotionRequired to set
     */
    public void setDevotionRequired(int devotionRequired)
    {
        mDevotionRequired = devotionRequired;
    }

    /**
     * Sets the godView.
     * @param godView The godView to set
     */
    public void setGodView(int godView)
    {
        mGodView = godView;
    }

    /**
     * Sets the liturgicalFormality.
     * @param liturgicalFormality The liturgicalFormality to set
     */
    public void setLiturgicalFormality(int liturgicalFormality)
    {
        mLiturgicalFormality = liturgicalFormality;
    }

    /**
     * Sets the missionaryFervour.
     * @param missionaryFervour The missionaryFervour to set
     */
    public void setMissionaryFervour(int missionaryFervour)
    {
        mMissionaryFervour = missionaryFervour;
    }

    /**
     * Sets the religiousOrganisation.
     * @param religiousOrganisation The religiousOrganisation to set
     */
    public void setReligiousOrganisation(int religiousOrganisation)
    {
        mReligiousOrganisation = religiousOrganisation;
    }

    /**
     * Sets the spiritualAim.
     * @param spiritualAim The spiritualAim to set
     */
    public void setSpiritualAim(int spiritualAim)
    {
        mSpiritualAim = spiritualAim;
    }

    public static final String[] SPIRITUAL_AIM_DESC = {
        "Worshipers are a chosen elite who deserve to dominate.",
        "Worshipers will be rewarded in this life.",
        "Worshipers will be saved from some imminent disaster.",
        "Reincarnation with a karma doctrine.",
        "Reincarnation is accomplished via personal choice for the next vessell.",
        "Statistical reincarnation causes a return in a merit awarded form.",
        "Worshippers will be received into paradise when they die.",
        "Worshippers will avoid being condemned to a place of eternal punishment.",
        "Ethical and moral standards are their own reward.",
        "Believers perform charitable acts to build a better society.",
        "Believers seek to promote peace and harmony for all.",
        "Believers seek to expand knowledge through inquiry and speculation.",
        "Believers seek to preserve the wisdom of the past.",
        "Believers seek to improve their own lives by by self-discipline.",
        "Hedonism is the only proper purpose in life.",
        "There is no purpose in life and nothing is to be gained by living.",
    };
    /**
     * Text description of Spiritual Aim
     * @return text description
     */
    public String getSpiritualAimDescription() { return getSpiritualAimDescription(mSpiritualAim); }
    /**
     * Text description of Spiritual Aim
     * @param val Spiritual Aim constant
     * @return text description
     */
    static public String getSpiritualAimDescription(int val)
    {
        if (val < 0)
            return "";
        return SPIRITUAL_AIM_DESC[val];
    }

    public static final String[] RELIGIOUS_ORGANIZATION_DESC = {
        "Theocracy",
        "Rigid hierarchy with centralized authority",
        "Rigid hierarchy with regional authority",
        "Rigid hierarchy with planetary authority",
        "Rigid hierarchy with local authority",
        "Loose hierarchy with centrailzed authority",
        "Loose hierarchy with regional authority",
        "Loose hierarchy with planetary authority",
        "Loose hierarchy with local authority",
        "Loose hierarchy with individual authority",
        "No organization above regional level",
        "No organization above planetary level",
        "No organization above local level",
        "Local organization without regulations",
        "Loose highly informal organization",
        "No organization at all",        
    };
    
    /**
     * Text description of Religious Organisation
     * @return text description
     */
    public String getReligiousOrganisationDesc() { return getReligiousOrganisationDesc(mReligiousOrganisation); }
    /**
     * Text description of Religious Organisation
     * @param val Religious Organisation constant
     * @return text description
     */
    static public String getReligiousOrganisationDesc(int val)
    {
        if (val < 0)
            return "";
        return RELIGIOUS_ORGANIZATION_DESC[val];
    }
    public static final String[] MISSIONARY_FERVOUR_DESC = {
        "Zealous conversion of all",
        "Zealous conversion of limited races",
        "Zealous of same race",
        "Active conversion of all",
        "Active conversion of limited races",
        "Active conversion of same race",
        "Ordinary conversion of all",
        "Ordinary conversion of limited races",
        "Ordinary conversion of same race",
        "Occasional",
        "Highly infrequent",        
    };
    /**
     * Text description of Missionary Fervour
     * @return text description
     */
    public String getMissionaryFervourDesc() { return getMissionaryFervourDesc(mMissionaryFervour); }
    /**
     * Text description of Missionary Fervour
     * @param val Missionary Fervour constant
     * @return text description
     */
    static public String getMissionaryFervourDesc(int val)
    {
        if (val < 0)
            return "";
        return MISSIONARY_FERVOUR_DESC[val];
    }
    public static final String[] LITURGICAL_FORMALITY_DESC = {
        "Word of God is pronounced by living oracles",
        "Holy writings accessible by high clergy",
        "Holy writings accessibly by clergy",
        "Services conducted by rote in obscure religious language",
        "Services conducted by rote in common language",
        "Very formal church service",
        "Formal church service and limited teaching",
        "Rituals are combined with moderate teaching",
        "Rituals are combined with open teaching",
        "Communal teaching with limited ritual",
        "Formal study groups of church writings",
        "Formal study groups investigating wider applications of church writings",
        "Informal study groups on limited topics",
        "Informal liturgy with minor restraints",
        "Open exchange of ideas in conversational setting",
        "Religion not discussed",
    };
    public String getLiturgicalFormalityDesc() { return getLiturgicalFormalityDesc(mLiturgicalFormality); }
    /**
     * Text description of Liturgical Formality
     * @param val Liturgical Formality constant
     * @return text description
     */
    static public String getLiturgicalFormalityDesc(int val)
    {
        if (val < 0)
            return "";
        return LITURGICAL_FORMALITY_DESC[val];
    }
    public static final String[] GOD_VIEW_DESC = {
        "Animism",
        "Polytheistic animism",
        "Polytheism",
        "Rational polytheism",
        "Dualism",
        "Interactive Monotheism",
        "Influential Monotheism",
        "Crisis Monotheism",
        "Remote Monotheism",
        "Deism",
        "Pantheism",
        "Agnosticism",
        "Rational Atheism",
        "Skeptical Atheism",
        "Atheism",
        "Philosophical Atheism",
    };
    /**
     * Text description of God View
     * @return text description
     */
    public String getGodViewDesc() { return getGodViewDesc(mGodView); }
    /**
     * Text description of God View
     * @param val god view constant
     * @return text description
     */
    static public String getGodViewDesc(int val)
    {
        if (val < 0)
            return "";
        return GOD_VIEW_DESC[val];
    }
    public static final String[] DEVOTION_REQUIRED_DESC = {
        "Constant",
        "Several times per hour",
        "Hourly",
        "Several times per day",
        "Daily",
        "Several days per week",
        "Weekly",
        "Semi-weekly",
        "Monthly",
        "Semi-monthly",
        "Quarterly",
        "Bi-annually",
        "Yearly",
        "Several times during life",
        "At least once before death",
        "None",
    };
    public String getDevotionRequiredDesc() { return getDevotionRequiredDesc(mDevotionRequired); }
    /**
     * Text description of Devotion Required
     * @param val devotion required constant
     * @return text description
     */
    static public String getDevotionRequiredDesc(int val)
    {
        if (val < 0)
            return "";
        return DEVOTION_REQUIRED_DESC[val];
    }
}
