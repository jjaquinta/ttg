package jo.ttg.ship.logic;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.ShipBlockBean;
import jo.ttg.ship.beans.comp.Airlock;
import jo.ttg.ship.beans.comp.Bunk;
import jo.ttg.ship.beans.comp.ControlHeadsUp;
import jo.ttg.ship.beans.comp.ControlHeadsUpHolo;
import jo.ttg.ship.beans.comp.ControlLargeHolodisplay;
import jo.ttg.ship.beans.comp.FuelPurifier;
import jo.ttg.ship.beans.comp.FuelTank;
import jo.ttg.ship.beans.comp.JumpDrive;
import jo.ttg.ship.beans.comp.LowBerth;
import jo.ttg.ship.beans.comp.ManeuverDrive;
import jo.ttg.ship.beans.comp.NumberedComponent;
import jo.ttg.ship.beans.comp.PowerPlant;
import jo.ttg.ship.beans.comp.Seat;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.ttg.ship.beans.comp.Stateroom;
import jo.ttg.ship.beans.comp.TurretBeamLaser;
import jo.ttg.ship.beans.comp.TurretDisintegrator;
import jo.ttg.ship.beans.comp.TurretFG;
import jo.ttg.ship.beans.comp.TurretMissile;
import jo.ttg.ship.beans.comp.TurretPA;
import jo.ttg.ship.beans.comp.TurretPG;
import jo.ttg.ship.beans.comp.TurretPulseLaser;
import jo.ttg.ship.beans.comp.TurretSandcaster;
import jo.ttg.ship.beans.comp.VolumeComponent;


public class ShipModify
{
	public static void remove(ShipBean ship, ShipComponent comp)
	{	    
        if (comp == null)
            return;
		ship.getComponents().remove(comp);
		ship.fireComponentChange();
	}

	public static void add(ShipBean ship, ShipComponent comp)
	{
		ship.getComponents().add(comp);
		ship.fireComponentChange();
	}

	public static void remove(ShipBean ship, ShipBlockBean comp)
	{	    
		ship.getComponents().remove(comp);
		ship.fireComponentChange();
	}

	public static void add(ShipBean ship, ShipBlockBean comp)
	{
		ship.getComponents().add(comp);
		ship.fireComponentChange();
	}

	/**
	 * @param mShip
	 * @param newComps
	 */
	public static void replace(ShipBean ship, List<ShipComponent> newComps)
	{
		ship.getComponents().clear();
		ship.getComponents().addAll(newComps);
		ship.fireComponentChange();
	}

	public static void modify(ShipBean ship, ShipComponent comp)
	{
		ship.fireComponentChange();
	}

	/**
	 * @param comp
	 * @param i
	 */
	public static void setTechLevel(ShipComponent comp, int tl)
	{
		Method set = null;
		Method getRange = null;
		Method[] m = comp.getClass().getMethods();
		for (int i = 0; i < m.length; i++)
		{
			String name = m[i].getName();
			if (name.equals("setTechLevel"))
			{
				set = m[i];
				if (getRange != null)
					break;
			}
			else if (name.equals("getTechLevelRange"))
			{
				getRange = m[i];
				if (set != null)
					break;
			}
		}
		if (getRange != null)
		{
			try
			{
				int[] range = (int[])getRange.invoke(comp, new Object[0]);
				if (tl < range[0])
					tl = range[0];
				else if (tl > range[1])
					tl = range[1];
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (set != null)
		{
			Object[] args = new Object[1];
			args[0] = new Integer(tl);
			try
			{
				set.invoke(comp, args);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
    
    public static void coalesce(ShipBean ship)
    {
        coalesceByVolume(ship, FuelTank.class);
        coalesceByVolume(ship, FuelPurifier.class);
        coalesceByVolume(ship, JumpDrive.class);
        coalesceByVolume(ship, ManeuverDrive.class);
        coalesceByVolume(ship, PowerPlant.class);
        coalesceByNumber(ship, TurretMissile.class);
        coalesceByNumber(ship, TurretBeamLaser.class);
        coalesceByNumber(ship, TurretDisintegrator.class);
        coalesceByNumber(ship, TurretFG.class);
        coalesceByNumber(ship, TurretPA.class);
        coalesceByNumber(ship, TurretPG.class);
        coalesceByNumber(ship, TurretPulseLaser.class);
        coalesceByNumber(ship, TurretSandcaster.class);
        coalesceByNumber(ship, Airlock.class);
        coalesceByNumber(ship, Bunk.class);
        coalesceByNumber(ship, Seat.class);
        coalesceByNumber(ship, Stateroom.class);
        coalesceByNumber(ship, LowBerth.class);
        coalesceByNumber(ship, ControlHeadsUp.class);
        coalesceByNumber(ship, ControlHeadsUpHolo.class);
        coalesceByNumber(ship, ControlLargeHolodisplay.class);
    }
    
    private static void coalesceByVolume(ShipBean ship, Class<?> type)
    {
        List<ShipComponent> comps = ShipLogic.findComponents(ship, type);
        if (comps.size() <= 1)
            return;
        Map<Integer,ShipComponent> byTech = new HashMap<Integer,ShipComponent>();
        for (ShipComponent comp : comps)
        {
            ShipComponent first = (ShipComponent)byTech.get(comp.getTechLevel());
            if (first != null)
            {
                ((VolumeComponent)first).setVolume(((ShipComponent)first).getVolume() + comp.getVolume());
                ShipModify.remove(ship, comp);
            }
            else
                byTech.put(comp.getTechLevel(), comp);
        }
    }
    
    private static void coalesceByNumber(ShipBean ship, Class<?> type)
    {
        Object[] comps = ShipLogic.findComponents(ship, type).toArray();
        if (comps.length <= 1)
            return;
        Map<Integer,ShipComponent> byTech = new HashMap<Integer,ShipComponent>();
        for (int i = 0; i < comps.length; i++)
        {
            ShipComponent comp = (ShipComponent)comps[i];
            ShipComponent first = (ShipComponent)byTech.get(comp.getTechLevel());
            if (first != null)
            {
                ((NumberedComponent)first).setNumber(((NumberedComponent)first).getNumber() + ((NumberedComponent)comp).getNumber());
                ShipModify.remove(ship, comp);
            }
            else
                byTech.put(comp.getTechLevel(), comp);
        }
    }
}
