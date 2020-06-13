/*
 * Created on Dec 18, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.logic;

import jo.ttg.ship.beans.ShipBlockBean;
import jo.ttg.ship.beans.comp.Hull;
import jo.ttg.ship.beans.comp.HullDependantPower;
import jo.ttg.ship.beans.comp.HullDependantPrice;
import jo.ttg.ship.beans.comp.HullDependantVolume;
import jo.ttg.ship.beans.comp.HullDependantWeight;
import jo.ttg.ship.beans.comp.ShipComponent;


/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ComponentLogic
{
	public static double getPrice(ShipComponent comp, Hull h)
	{
		if ((comp instanceof HullDependantPrice) && (h != null))
			return ((HullDependantPrice)comp).getPrice(h);
		else if (comp instanceof ShipBlockBean)
		{
		    double ret = 0;
		    for (ShipComponent c : ((ShipBlockBean)comp).getComponents())
		        ret += getPrice(c, h);
		    return ret;
		}
        else
            return ((ShipComponent)comp).getPrice();
	}
	public static double getWeight(ShipComponent comp, Hull h)
	{
		if ((comp instanceof HullDependantWeight) && (h != null))
			return ((HullDependantWeight)comp).getWeight(h);
        else if (comp instanceof ShipBlockBean)
        {
            double ret = 0;
            for (ShipComponent c : ((ShipBlockBean)comp).getComponents())
                ret += getWeight(c, h);
            return ret;
        }
		else
			return ((ShipComponent)comp).getWeight();
	}
	public static double getVolume(ShipComponent comp, Hull h)
	{
		if ((comp instanceof HullDependantVolume) && (h != null))
			return ((HullDependantVolume)comp).getVolume(h);
        else if (comp instanceof ShipBlockBean)
        {
            double ret = 0;
            for (ShipComponent c : ((ShipBlockBean)comp).getComponents())
                ret += getVolume(c, h);
            return ret;
        }
		else 
			return ((ShipComponent)comp).getVolume();
	}
	public static double getPower(ShipComponent comp, Hull h)
	{
		if ((comp instanceof HullDependantPower) && (h != null))
			return ((HullDependantPower)comp).getPower(h);
        else if (comp instanceof ShipBlockBean)
        {
            double ret = 0;
            for (ShipComponent c : ((ShipBlockBean)comp).getComponents())
                ret += getPower(c, h);
            return ret;
        }
			return ((ShipComponent)comp).getPower();
	}
	public static int getTechLevel(ShipComponent comp)
	{
        if (comp instanceof ShipBlockBean)
        {
            int ret = 0;
            for (ShipComponent c : ((ShipBlockBean)comp).getComponents())
                ret = Math.max(ret, getTechLevel(c));
            return ret;
        }
        else
			return ((ShipComponent)comp).getTechLevel();
	}
	public static int getControlPoints(ShipComponent comp)
	{
        if (comp instanceof ShipBlockBean)
        {
            int ret = 0;
            for (ShipComponent c : ((ShipBlockBean)comp).getComponents())
                ret += getControlPoints(c);
            return ret;
        }
        else
			return comp.getControlPoints();
	}
	public static String getName(ShipComponent comp)
	{
		return ((ShipComponent)comp).getName();
	}
	public static int getSection(ShipComponent comp)
	{
		return ((ShipComponent)comp).getSection();
	}
}
