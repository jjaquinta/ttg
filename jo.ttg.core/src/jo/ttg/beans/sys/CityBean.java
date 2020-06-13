/**
 * Created on Sep 20, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package jo.ttg.beans.sys;

import jo.util.beans.Bean;

/**
 * @author jgrant
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class CityBean extends Bean
{
    /**
      * Name of city
      */
    private String name;
    /**
      * Population of city
      */
    private double pop;

    /**
     * Returns the name.
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the pop.
     * @return double
     */
    public double getPop()
    {
        return pop;
    }

    /**
     * Sets the name.
     * @param name The name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets the pop.
     * @param pop The pop to set
     */
    public void setPop(double pop)
    {
        this.pop = pop;
    }

}
