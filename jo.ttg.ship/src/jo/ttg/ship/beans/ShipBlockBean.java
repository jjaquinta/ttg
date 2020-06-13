/*
 * Created on Dec 15, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.beans;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.ship.beans.comp.ShipComponent;

/**
 * @author jgrant
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ShipBlockBean extends ShipComponent
{
    private String    mName;
    private int       mSection;
    private List<ShipComponent> mComponents;

    public ShipBlockBean()
    {
        mComponents = new ArrayList<ShipComponent>();
    }

    public String toString()
    {
        return mName;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String blockName)
    {
        mName = blockName;
    }

    public List<ShipComponent> getComponents()
    {
        return mComponents;
    }

    public void setComponents(List<ShipComponent> components)
    {
        mComponents = components;
    }

    /**
     * @return Returns the mSection.
     */
    public int getSection()
    {
        return mSection;
    }

    /**
     * @param section
     *            The mSection to set.
     */
    public void setSection(int section)
    {
        mSection = section;
    }

    @Override
    public int getTechLevel()
    {
        int ret = 0;
        for (ShipComponent c : mComponents)
            ret = Math.max(ret, c.getTechLevel());
        return ret;
    }

    @Override
    public double getVolume()
    {
        double ret = 0;
        for (ShipComponent c : mComponents)
            ret += c.getVolume();
        return ret;
    }

    @Override
    public double getWeight()
    {
        double ret = 0;
        for (ShipComponent c : mComponents)
            ret += c.getWeight();
        return ret;
    }

    @Override
    public double getPower()
    {
        double ret = 0;
        for (ShipComponent c : mComponents)
            ret += c.getPower();
        return ret;
    }

    @Override
    public double getPrice()
    {
        double ret = 0;
        for (ShipComponent c : mComponents)
            ret += c.getPrice();
        return ret;
    }
}