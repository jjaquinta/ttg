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
public class StatsHydBean extends Bean
{

    /**
      * Hydrosphere not tainted
      * @see ttg.TBodyWorld#HyTaint
      */
    public static final int HT_NONE = 0;
    /**
      * Hydrosphere Tainted
      * @see ttg.TBodyWorld#HyTaint
      */
    public static final int HT_TAINT = 1;
    /**
      * Hydrosphere Tainted with whatever the atmosphere is tainted with
      * @see ttg.TBodyWorld#HyTaint
      */
    public static final int HT_ATMOS = 2;

    public static final String[] HYDROSPHERE_TAINT = {
        "None",
        "Tainted Liquid Water",
        "Atmosphere Related Chemical Mix",  
    };

    /**
      * Percentage of surface covered with water
      */
    private int mWaterPercent;
    /**
      * Number of continental plates
      */
    private int mNumPlates;
    /**
      * Number of major landmasses
      * If WaterPC is &gt; 50, then this is the number of major continents.
      * If WaterPC is &lt; 50, then this is the number of major oceans.
      */
    private int mMajor;
    /**
      * Number of minor landmasses
      * If WaterPC is &gt; 50, then this is the number of minor continents.
      * If WaterPC is &lt; 50, then this is the number of minor oceans.
      */
    private int mMinor;
    /**
      * Number of large islands
      * If WaterPC is &gt; 50, then this is the number of large islands.
      * If WaterPC is &lt; 50, then this is the number of small seas.
      */
    private int mIslands;
    /**
      * Number of achepeligos
      * If WaterPC is &gt; 50, then this is the number of achepeligos.
      * If WaterPC is &lt; 50, then this is the number of scattered lakes.
      */
    private int mArchepeligos;
    /**
      * Type of taint for the ocean
      */
    private int mTaint;
    /**
      * Number of volcanos
      */
    private int mNumVolcanos;
    /**
      * Resource presence
      * If the value is positive, the indexed resource is present.
      * If it is negative or zero it is not. The actual value is
      * by how much the roll was made. So it might be used to judge
      * how much of that resource is present.
      */
    private StatsHydResourcesBean mResources;
    /**
      * Is weather control is used on this world
      * Set to either 'Y' or 'N'.
      */
    private boolean mWeatherControl;

    // constructor

    public StatsHydBean()
    {
        mWaterPercent = 0;
        mNumPlates = 0;
        mMajor = 0;
        mMinor = 0;
        mIslands = 0;
        mArchepeligos = 0;
        mTaint = 0;
        mNumVolcanos = 0;
        mResources = new StatsHydResourcesBean();
        mWeatherControl = false;
    }
    
    // utilities
    public String getTypeDesc()
    {
        if (mTaint == 0)
            return "Liquid Water";
        else
            return HYDROSPHERE_TAINT[mTaint];
    }

    public String getMajorDesc()
    {
        return (mWaterPercent < 50) ? "Major Oceans" : "Major Continents";
    }

    public String getMinorDesc()
    {
        return (mWaterPercent < 50) ? "Minor Oceans" : "Minor Continents";
    }

    public String getIslandsDesc()
    {
        return (mWaterPercent < 50) ? "Small Lakes" : "Islands";
    }

    public String getArchipeligosDesc()
    {
        return (mWaterPercent < 50) ? "ScatteredLakes" : "Archipeligos";
    }

    // getters and setters
    
    /**
     * Returns the archepeligos.
     * @return int
     */
    public int getArchepeligos()
    {
        return mArchepeligos;
    }

    /**
     * Returns the hyTaint.
     * @return int
     */
    public int getTaint()
    {
        return mTaint;
    }

    /**
     * Returns the islands.
     * @return int
     */
    public int getIslands()
    {
        return mIslands;
    }

    /**
     * Returns the major.
     * @return int
     */
    public int getMajor()
    {
        return mMajor;
    }

    /**
     * Returns the minor.
     * @return int
     */
    public int getMinor()
    {
        return mMinor;
    }

    /**
     * Returns the numPlates.
     * @return int
     */
    public int getNumPlates()
    {
        return mNumPlates;
    }

    /**
     * Returns the numVolcanos.
     * @return int
     */
    public int getNumVolcanos()
    {
        return mNumVolcanos;
    }

    /**
     * Returns the resources.
     * @return StatsHydResourcesBean
     */
    public StatsHydResourcesBean getResources()
    {
        return mResources;
    }

    /**
     * Returns the waterPercent.
     * @return int
     */
    public int getWaterPercent()
    {
        return mWaterPercent;
    }

    /**
     * Returns the weatherControl.
     * @return boolean
     */
    public boolean isWeatherControl()
    {
        return mWeatherControl;
    }

    /**
     * Sets the archepeligos.
     * @param archepeligos The archepeligos to set
     */
    public void setArchepeligos(int archepeligos)
    {
        mArchepeligos = archepeligos;
    }

    /**
     * Sets the hyTaint.
     * @param hyTaint The hyTaint to set
     */
    public void setTaint(int hyTaint)
    {
        mTaint = hyTaint;
    }

    /**
     * Sets the islands.
     * @param islands The islands to set
     */
    public void setIslands(int islands)
    {
        mIslands = islands;
    }

    /**
     * Sets the major.
     * @param major The major to set
     */
    public void setMajor(int major)
    {
        mMajor = major;
    }

    /**
     * Sets the minor.
     * @param minor The minor to set
     */
    public void setMinor(int minor)
    {
        mMinor = minor;
    }

    /**
     * Sets the numPlates.
     * @param numPlates The numPlates to set
     */
    public void setNumPlates(int numPlates)
    {
        mNumPlates = numPlates;
    }

    /**
     * Sets the numVolcanos.
     * @param numVolcanos The numVolcanos to set
     */
    public void setNumVolcanos(int numVolcanos)
    {
        mNumVolcanos = numVolcanos;
    }

    /**
     * Sets the resources.
     * @param resources The resources to set
     */
    public void setResources(StatsHydResourcesBean resources)
    {
        mResources = resources;
    }

    /**
     * Sets the waterPercent.
     * @param waterPercent The waterPercent to set
     */
    public void setWaterPercent(int waterPercent)
    {
        mWaterPercent = waterPercent;
    }

    /**
     * Sets the weatherControl.
     * @param weatherControl The weatherControl to set
     */
    public void setWeatherControl(boolean weatherControl)
    {
        mWeatherControl = weatherControl;
    }

}
