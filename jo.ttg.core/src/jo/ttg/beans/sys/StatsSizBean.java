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
public class StatsSizBean extends Bean
{
    /**
      * Core Type Heavy
      * @see ttg.TBodyWorld#Core
      */
    public static final int WC_HEAVY = 0;
    /**
      * Core Type Molten
      * @see ttg.TBodyWorld#Core
      */
    public static final int WC_MOLTEN = 1;
    /**
      * Core Type Rocky
      * @see ttg.TBodyWorld#Core
      */
    public static final int WC_ROCKY = 2;
    /**
      * Core Type Icy
      * @see ttg.TBodyWorld#Core
      */
    public static final int WC_ICY = 3;

    public static final String[] WORLD_CORE_DESC = {
        "Heavy Core",
        "Molten Core",
        "Rocky Body",
        "Icy Body",  
    };

    /**
      * Type of planetary core
      */
    private int mCore;
    /**
      * Length of day in hours
      */
    private double mDay; /*  in hours  */
    /**
      * Seismic stress factor
      */
    private double mSeismicStress;

    // constructor

    public StatsSizBean()
    {
        mCore = 0;
        mSeismicStress = 0;
    }
    
    // utilities
    
    public String getCoreDesc()
    {
        return WORLD_CORE_DESC[getCore()];
    }
    
    // getters and setters

    /**
     * Returns the core.
     * @return int
     */
    public int getCore()
    {
        return mCore;
    }

    /**
     * Returns the day.
     * @return double
     */
    public double getDay()
    {
        return mDay;
    }

    /**
     * Returns the seismicStress.
     * @return double
     */
    public double getSeismicStress()
    {
        return mSeismicStress;
    }

    /**
     * Sets the core.
     * @param core The core to set
     */
    public void setCore(int core)
    {
        mCore = core;
    }

    /**
     * Sets the day.
     * @param day The day to set
     */
    public void setDay(double day)
    {
        mDay = day;
    }

    /**
     * Sets the seismicStress.
     * @param seismicStress The seismicStress to set
     */
    public void setSeismicStress(double seismicStress)
    {
        mSeismicStress = seismicStress;
    }

}
