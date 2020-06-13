/*
 * Created on Dec 15, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.logic;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.ShipBlockBean;
import jo.ttg.ship.beans.comp.Hull;
import jo.ttg.ship.beans.comp.ShipComponent;


/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ShipLogic
{
    public static List<ShipComponent> getComponentList(ShipBean ship)
    {
        List<ShipComponent> ret = new ArrayList<ShipComponent>();
        for (ShipComponent c : ship.getComponents())
        {
            if (c instanceof ShipBlockBean)
                ret.addAll(((ShipBlockBean)c).getComponents());
            else
                ret.add(c);
        }
        return ret;
    }
    
    public static ShipComponent findComponent(ShipBean ship, Class<?> type)
    {
        for (ShipComponent comp : ShipLogic.getComponentList(ship))
            if (type.isAssignableFrom(comp.getClass()))
                return comp;
        return null;
    }
    
    public static List<ShipComponent> findComponents(ShipBean ship, Class<?> type)
    {
        List<ShipComponent> ret = new ArrayList<ShipComponent>();
        for (ShipComponent comp : ShipLogic.getComponentList(ship))
            if (type.isAssignableFrom(comp.getClass()))
                ret.add(comp);
        return ret;
    }
    
    public static List<ShipComponent> findComponents(ShipBean ship, int section)
    {
        List<ShipComponent> ret = new ArrayList<ShipComponent>();
        for (ShipComponent comp : ShipLogic.getComponentList(ship))
            if (comp.getSection() == section)
                ret.add(comp);
        return ret;
    }
    
    public static double totalVolume(List<ShipComponent> comps)
    {
        double ret = 0;
        for (ShipComponent comp : comps)
            if (!(comp instanceof Hull))
                ret += comp.getVolume();
        return ret;
    }
    
    public static double totalCost(List<ShipComponent> comps)
    {
        double cost = 0;
        for (ShipComponent comp : comps)
        {
            if (comp instanceof ShipBlockBean)
                cost += totalCost((ShipBlockBean)comp);
            else
                cost += comp.getPrice();
        }
        return cost;
    }
    
    public static double totalCost(ShipBlockBean block)
    {
        double cost = 0;
        for (ShipComponent comp : block.getComponents())
        {
            if (comp instanceof ShipBlockBean)
                cost += totalCost((ShipBlockBean)comp);
            else
                cost += comp.getPrice();
        }
        return cost;
    }

    public static int maxTechLevel(ShipBean ship)
    {
        int tl = 0;
        for (ShipComponent comp : getComponentList(ship))
        {
            if (comp instanceof ShipBlockBean)
                tl = Math.max(maxTechLevel((ShipBlockBean)comp), tl);
            else
                tl = Math.max(comp.getTechLevel(), tl);
        }
        return tl;
    }
    
    public static int maxTechLevel(ShipBlockBean block)
    {
        int tl = 0;
        for (ShipComponent comp : block.getComponents())
        {
            if (comp instanceof ShipBlockBean)
                tl = Math.max(maxTechLevel((ShipBlockBean)comp), tl);
            else 
                tl = Math.max(comp.getTechLevel(), tl);
        }
        return tl;
    }
}
