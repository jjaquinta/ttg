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
public class StatsAtmBean extends Bean
{
    /**
      * Atmosphere Type Trace
      * @see ttg.TBodyWorld#AtType
      */
    public static final int AT_TRACE = 0;
    /**
      * Atmosphere Type Very Think
      * @see ttg.TBodyWorld#AtType
      */
    public static final int AT_VTHIN = 1;
    /**
      * Atmosphere Type Thin
      * @see ttg.TBodyWorld#AtType
      */
    public static final int AT_THIN = 2;
    /**
      * Atmosphere Type Standard
      * @see ttg.TBodyWorld#AtType
      */
    public static final int AT_STD = 3;
    /**
      * Atmosphere Type Dense
      * @see ttg.TBodyWorld#AtType
      */
    public static final int AT_DENSE = 4;
    /**
      * Atmosphere Type Very Dense
      * @see ttg.TBodyWorld#AtType
      */
    public static final int AT_VDENSE = 5;

    public static final String[] ATMOSPHERE_TYPE_DESC = {
        "Trace",
        "Very Thin",
        "Thin",
        "Standard",
        "Dense",
        "Very Dense",  
    };
    
    /**
      * Atmosphere not Tainted
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_NONE = 0;
    /**
      * Atmosphere Tainted with disease
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_DISEASE = 1;
    /**
      * Atmosphere Tainted with a gas mix
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_GASMIX = 2;
    /**
      * Atmosphere Tainted with high oxygen
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_HIGHOX = 3;
    /**
      * Atmosphere Tainted with pollutants
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_POLLUT = 4;
    /**
      * Atmosphere Tainted with sulfer
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_SULFUR = 5;
    /**
      * Atmosphere Tainted with low oxygen
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_LOWOX = 6;
    /**
      * Atmosphere Tainted with an irritant
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_IRRITANT = 7;
    /**
      * Atmosphere Tainted with something exotic
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_EXOTIC = 8;
    /**
      * Atmosphere Occasionally Tainted
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_OCCCOR = 9;
    /**
      * Atmosphere Tainted with something ocrrosive
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_COR = 10;
    /**
      * Atmosphere Tainted with radiation
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_RAD = 11;
    /**
      * Atmosphere Tainted with temperature
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_TEMP = 12;
    /**
      * Atmosphere Tainted with pressure
      * @see ttg.TBodyWorld#AtTaint
      */
    public static final int AT_PRESS = 13;

    public static final String[] ATMOSPHERE_TAINT_DESC = {
        "None",
        "Disease",
        "Gas Mix",
        "High Oxygen",
        "Pollutants",
        "Sulpher Compounds",
        "Low Oxygen",
        "Irritant",
        "Exotic",
        "Occasional Corrosive",
        "Corrosive",
        "Radiation",
        "Temperature",
        "Pressure",  
    };
    
    /**
      * Type of atmosphere
      */
    private int mType;
    /**
      * Type of atmospheric taint
      */
    private int mTaint;
    /**
      * Mean temperature
      */
    private double mTemperature;
    /**
      * Atmospheric pressure
      */
    private double mPressure;
    /**
     * Energy Adsorption
     */
   private double mEnergyAdsorption;
   /**
    * Greenhouse Effect
    */
   private double mGreenhouseEffect;
   /**
    * Temperature modifier for closest approach for orbital eccentricity
    */
   private double mClosestApproachMod;
   /**
    * Temperature modifier for furthest separation for orbital eccentricity
    */
   private double mFurthestSeparationMod;
    /**
      * Is native life present
      * Set to either 'Y' or 'N'.
      */
    private boolean mLife;

    // constructor

    public StatsAtmBean()
    {
        mType = 0;
        mTaint = 0;
        mTemperature = 0;
        mPressure = 0;
        mLife = false;
    }
    
    // utilities
    public String getAtmosDesc()
    {
        String desc = ATMOSPHERE_TYPE_DESC[mType];
        if (mTaint > 0)
            desc += ", "+ATMOSPHERE_TAINT_DESC[mTaint]+" taint";
        return desc;
    }
    
    // getters and setters

    /**
     * Returns the life.
     * @return boolean
     */
    public boolean isLife()
    {
        return mLife;
    }

    /**
     * Returns the pressure.
     * @return double
     */
    public double getPressure()
    {
        return mPressure;
    }

    /**
     * Returns the taint.
     * @return int
     */
    public int getTaint()
    {
        return mTaint;
    }

    /**
     * Returns the temperature.
     * @return double
     */
    public double getTemperature()
    {
        return mTemperature;
    }

    /**
     * Returns the type.
     * @return int
     */
    public int getType()
    {
        return mType;
    }

    /**
     * Sets the life.
     * @param life The life to set
     */
    public void setLife(boolean life)
    {
        mLife = life;
    }

    /**
     * Sets the pressure.
     * @param pressure The pressure to set
     */
    public void setPressure(double pressure)
    {
        mPressure = pressure;
    }

    /**
     * Sets the taint.
     * @param taint The taint to set
     */
    public void setTaint(int taint)
    {
        mTaint = taint;
    }

    /**
     * Sets the temperature.
     * @param temperature The temperature to set
     */
    public void setTemperature(double temperature)
    {
        mTemperature = temperature;
    }

    /**
     * Sets the type.
     * @param type The type to set
     */
    public void setType(int type)
    {
        mType = type;
    }

    public double getEnergyAdsorption()
    {
        return mEnergyAdsorption;
    }

    public void setEnergyAdsorption(double energyAdsorption)
    {
        mEnergyAdsorption = energyAdsorption;
    }

    public double getGreenhouseEffect()
    {
        return mGreenhouseEffect;
    }

    public void setGreenhouseEffect(double greenhouseEffect)
    {
        mGreenhouseEffect = greenhouseEffect;
    }

    public double getClosestApproachMod()
    {
        return mClosestApproachMod;
    }

    public void setClosestApproachMod(double closestApproachMod)
    {
        mClosestApproachMod = closestApproachMod;
    }

    public double getFurthestSeparationMod()
    {
        return mFurthestSeparationMod;
    }

    public void setFurthestSeparationMod(double furthestSeparationMod)
    {
        mFurthestSeparationMod = furthestSeparationMod;
    }

}
