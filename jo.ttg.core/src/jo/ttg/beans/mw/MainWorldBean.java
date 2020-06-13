package jo.ttg.beans.mw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.HashBean;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.PropertiesBean;
import jo.ttg.beans.URIBean;
import jo.util.beans.Bean;

public class MainWorldBean extends Bean implements PropertiesBean, URIBean
{
    public String toString()
    {
        return mName;
    }
    
    private OrdBean mOrds;
    private java.lang.String mName;
    private PopulatedStatsBean mPopulatedStats;
    private long mLocalSeed;
    private long mSubSeed;
    private long mSecSeed;
    private int mNumGiants;
    private int mNumBelts;
    private int mPopDigit;
    private List<StarDeclBean> mStars;
    private HashBean mProperties;

    /**
     * @return Returns the localSeed.
     */
    public long getLocalSeed()
    {
        return mLocalSeed;
    }
    /**
     * @param localSeed The localSeed to set.
     */
    public void setLocalSeed(long localSeed)
    {
        mLocalSeed = localSeed;
    }
    /**
     * @return Returns the name.
     */
    public java.lang.String getName()
    {
        return mName;
    }
    /**
     * @param name The name to set.
     */
    public void setName(java.lang.String name)
    {
        mName = name;
    }
    /**
     * @return Returns the numBelts.
     */
    public int getNumBelts()
    {
        return mNumBelts;
    }
    /**
     * @param numBelts The numBelts to set.
     */
    public void setNumBelts(int numBelts)
    {
        mNumBelts = numBelts;
    }
    /**
     * @return Returns the numGiants.
     */
    public int getNumGiants()
    {
        return mNumGiants;
    }
    /**
     * @param numGiants The numGiants to set.
     */
    public void setNumGiants(int numGiants)
    {
        mNumGiants = numGiants;
    }
    /**
     * @return Returns the ords.
     */
    public OrdBean getOrds()
    {
        return mOrds;
    }
    /**
     * @param ords The ords to set.
     */
    public void setOrds(OrdBean ords)
    {
        mOrds = ords;
    }
    /**
     * @return Returns the popDigit.
     */
    public int getPopDigit()
    {
        return mPopDigit;
    }
    /**
     * @param popDigit The popDigit to set.
     */
    public void setPopDigit(int popDigit)
    {
        mPopDigit = popDigit;
    }
    /**
     * @return Returns the populatedStats.
     */
    public PopulatedStatsBean getPopulatedStats()
    {
        return mPopulatedStats;
    }
    /**
     * @param populatedStats The populatedStats to set.
     */
    public void setPopulatedStats(PopulatedStatsBean populatedStats)
    {
        mPopulatedStats = populatedStats;
    }
    /**
     * @return Returns the properties.
     */
    public HashBean getProperties()
    {
        return mProperties;
    }
    /**
     * @param properties The properties to set.
     */
    public void setProperties(HashBean properties)
    {
        mProperties = properties;
    }
    /**
     * @return Returns the secSeed.
     */
    public long getSecSeed()
    {
        return mSecSeed;
    }
    /**
     * @param secSeed The secSeed to set.
     */
    public void setSecSeed(long secSeed)
    {
        mSecSeed = secSeed;
    }
    /**
     * @return Returns the subSeed.
     */
    public long getSubSeed()
    {
        return mSubSeed;
    }
    /**
     * @param subSeed The subSeed to set.
     */
    public void setSubSeed(long subSeed)
    {
        mSubSeed = subSeed;
    }
    // Stars
    public StarDeclBean[] getStars()
    {
        StarDeclBean[] ret =
            new StarDeclBean[mStars.size()];
        mStars.toArray(ret);
        return ret;
    }
    public void setStars(StarDeclBean[] v)
    {
        mStars.clear();
        if (v != null)
            for (int i = 0; i < v.length; i++)
                mStars.add(v[i]);
    }
    public StarDeclBean getStars(int index)
    {
        return mStars.get(index);
    }
    public void setStars(int index, StarDeclBean v)
    {
        mStars.set(index, v);
    }
    public void addStars(StarDeclBean v)
    {
        mStars.add(v);
    }
    public Iterator<StarDeclBean> getStarsIterator()
    {
        return mStars.iterator();
    }

    // constructor
    public MainWorldBean()
    {
        mName = "";
        mPopulatedStats = new PopulatedStatsBean();
        mLocalSeed = 0;
        mSubSeed = 0;
        mSecSeed = 0;
        mNumGiants = 0;
        mNumBelts = 0;
        mPopDigit = 0;
        mStars = new ArrayList<StarDeclBean>();
        mProperties = new HashBean();
    }

    public String getURI()
    {
        return "mw://"+getOrds().toURIString();
    }
}
