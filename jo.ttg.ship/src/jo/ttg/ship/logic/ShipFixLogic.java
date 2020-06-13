/*
 * Created on Dec 11, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.ship.logic;

import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.ShipStats;
import jo.ttg.ship.beans.ShipStatsError;
import jo.ttg.ship.beans.comp.Airlock;
import jo.ttg.ship.beans.comp.Computer;
import jo.ttg.ship.beans.comp.ControlComponent;
import jo.ttg.ship.beans.comp.ControlHeadsUp;
import jo.ttg.ship.beans.comp.ControlHeadsUpHolo;
import jo.ttg.ship.beans.comp.ControlLargeHolodisplay;
import jo.ttg.ship.beans.comp.FuelPurifier;
import jo.ttg.ship.beans.comp.FuelTank;
import jo.ttg.ship.beans.comp.PowerPlant;
import jo.ttg.ship.beans.comp.Seat;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.ttg.ship.beans.comp.Stateroom;


/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ShipFixLogic
{
	public static boolean canBeFixed(ShipBean ship, ShipStatsError err)
	{
		if (err == null)
			return false;
		switch (err.getId())
		{
			case ShipStatsError.NOT_ENOUGH_FUEL_JUMP:
			case ShipStatsError.NOT_ENOUGH_POW:
			case ShipStatsError.NOT_ENOUGH_FUEL_POW:
			case ShipStatsError.NOT_ENOUGH_STATEROOMS:
			case ShipStatsError.NO_COMPUTER:
			case ShipStatsError.NOT_ENOUGH_COMPUTER:
			case ShipStatsError.NOT_ENOUGH_CONTROLS:
            case ShipStatsError.NOT_ENOUGH_AIRLOCKS:
			case ShipStatsError.NOT_ENOUGH_SEATS:
				return true;
		}
		return false;
	}
	
	public static void fix(ShipBean ship, ShipStats stats, ShipStatsError err)
	{
		switch (err.getId())
		{
			case ShipStatsError.NOT_ENOUGH_FUEL_JUMP:
				fixNotEnoughJumpFuel(ship, stats, err);
				break;
			case ShipStatsError.NOT_ENOUGH_POW:
				fixNotEnoughPower(ship, stats, err);
				break;
			case ShipStatsError.NOT_ENOUGH_FUEL_POW:
				fixNotEnoughPowerFuel(ship, stats, err);
				break;
			case ShipStatsError.NOT_ENOUGH_STATEROOMS:
				fixNotEnoughStaterooms(ship, stats, err);
				break;
			case ShipStatsError.NO_COMPUTER:
			case ShipStatsError.NOT_ENOUGH_COMPUTER:
				fixNoComputer(ship, stats, err);
				break;
			case ShipStatsError.NOT_ENOUGH_CONTROLS:
				fixNotEnoughControls(ship, stats, err);
				break;
			case ShipStatsError.NOT_ENOUGH_AIRLOCKS:
				fixNotEnoughAirlocks(ship, stats, err);
				break;
            case ShipStatsError.NOT_ENOUGH_SEATS:
                fixNotEnoughSeats(ship, stats, err);
                break;
		}
	}
	
	

    /**
     * @param ship
     * @param stats
     * @param err
     */
    private static void fixNotEnoughSeats(ShipBean ship, ShipStats stats, ShipStatsError err)
    {
        Seat comp = (Seat)findComponent(ship, ".Seat");
        if (comp == null)
        {
            comp = new Seat();
            comp.setSize(Seat.S_ROOMY);
            ShipModify.add(ship, comp); 
        }
        comp.setNumber((int)(err.getValue1()));
        ShipModify.modify(ship, comp);  
    }

	/**
	 * @param ship
	 * @param stats
	 * @param err
	 */
	private static void fixNotEnoughAirlocks(ShipBean ship, ShipStats stats, ShipStatsError err)
	{
		Airlock comp = new Airlock();
		ShipModify.add(ship, comp);	
	}

	/**
	 * @param ship
	 * @param stats
	 * @param err
	 */
	private static void fixNotEnoughControls(ShipBean ship, ShipStats stats, ShipStatsError err)
	{
		double todo = err.getValue2();
		if ((todo > 1500.0) && (stats.getTechLevel() >= 12))
		{
			ControlLargeHolodisplay lhd = new ControlLargeHolodisplay();
			lhd.setNumber((int)Math.floor(todo/1500.0));
			ShipModify.add(ship, lhd);	
			todo -= lhd.getCP();
		}
		if ((todo > 200.0) && (stats.getTechLevel() >= 13))
		{
			ControlHeadsUpHolo huh = new ControlHeadsUpHolo();
			huh.setNumber((int)Math.floor(todo/200.0));
			ShipModify.add(ship, huh);	
			todo -= huh.getCP();
		}
		if ((todo > 50.0) && (stats.getTechLevel() >= 9))
		{
			ControlHeadsUp hu = new ControlHeadsUp();
			hu.setNumber((int)Math.floor(todo/50.0));
			ShipModify.add(ship, hu);	
			todo -= hu.getCP();
		}
		if (todo > 0)
		{
			ControlComponent comp = new ControlComponent();
			if (stats.getTechLevel() < 5)
				comp.setTechLevel(5);
			else if (stats.getTechLevel() > 13)
				comp.setTechLevel(13);
			else
				comp.setTechLevel(stats.getTechLevel());
			comp.setNumber(1);
			double baseCP = comp.getCP();
			comp.setNumber((int)Math.ceil(todo/baseCP));
			ShipModify.add(ship, comp);	
			todo -= comp.getCP();
		}
	}

	/**
	 * @param ship
	 * @param stats
	 * @param err
	 */
	private static void fixNoComputer(ShipBean ship, ShipStats stats, ShipStatsError err)
	{
		Computer comp = (Computer)findComponent(ship, ".Computer");
		if (comp == null)
		{
			comp = new Computer();
			comp.setTechLevel(stats.getTechLevel());
			ShipModify.add(ship, comp);	
		}
		comp.setNumber(3);
		ShipModify.modify(ship, comp);	
	}

	/**
	 * @param ship
	 * @param err
	 */
	private static void fixNotEnoughStaterooms(ShipBean ship, ShipStats stats, ShipStatsError err)
	{
		Stateroom comp = (Stateroom)findComponent(ship, ".Stateroom");
		if (comp == null)
		{
			comp = new Stateroom();
			ShipModify.add(ship, comp);	
		}
		comp.setNumber((int)(err.getValue1()));
		ShipModify.modify(ship, comp);	
	}

	private static void fixNotEnoughPowerFuel(ShipBean ship, ShipStats stats, ShipStatsError err)
    {
		FuelTank comp = (FuelTank)findComponent(ship, ".FuelTank");
		if (comp == null)
		{
			comp = new FuelTank();
			ShipModify.add(ship, comp);	
		}
		comp.setVolume(comp.getVolume() + Math.ceil(err.getValue1()));
		ShipModify.modify(ship, comp);	
    }

    public static void fixNotEnoughPowerFuel(ShipBean ship, ShipStats stats, ShipStatsError err, int days)
    {
        FuelTank comp = (FuelTank)findComponent(ship, ".FuelTank");
        if (comp == null)
        {
            comp = new FuelTank();
            ShipModify.add(ship, comp); 
        }
        comp.setVolume(comp.getVolume() + err.getValue1()/30.0*days);
        ShipModify.modify(ship, comp);  
    }

    private static void fixNotEnoughPower(ShipBean ship, ShipStats stats, ShipStatsError err)
    {
		PowerPlant comp = (PowerPlant)findComponent(ship, ".PowerPlant");
		if (comp == null)
		{
			comp = new PowerPlant();
			comp.setTechLevel(stats.getTechLevel());
			ShipModify.add(ship, comp);	
		}
		while (comp.getPower() < err.getValue1())
			comp.setVolume(comp.getVolume() + 1);
		ShipModify.modify(ship, comp);	
    }

    /**
	 * @param ship
	 * @param err
	 */
	private static void fixNotEnoughJumpFuel(ShipBean ship, ShipStats stats, ShipStatsError err)
	{
		FuelTank comp = (FuelTank)findComponent(ship, ".FuelTank");
		if (comp == null)
		{
			comp = new FuelTank();
			ShipModify.add(ship, comp);	
		}
		comp.setVolume(Math.ceil(err.getValue1()));
		ShipModify.modify(ship, comp);	
	}
	
	private static ShipComponent findComponent(ShipBean ship, String className)
	{
		Class<?> target = null;
		if (!className.startsWith("."))
			try
			{
				target = Class.forName(className);
			}
			catch (ClassNotFoundException e)
			{
			}
		for (ShipComponent comp : ShipLogic.getComponentList(ship))
		{
			if (target != null)
			{
				if (target.isAssignableFrom(comp.getClass()))
					return comp;
			}
			else
			{
				String name = comp.getClass().getName();
				if (name.endsWith(className))
					return comp;
			}
		}
		return null;
	}

	/**
	 * @param mShip
	 * @param mStats
	 */
	public static void trimPower(ShipBean ship, ShipStats stats)
	{
		PowerPlant pp = (PowerPlant)findComponent(ship, ".PowerPlant");
		if (pp == null)
			return;
		if (stats.getPowerConsumed() > stats.getPowerProduced() + 1)
		{
			// increase power plant size
			while (pp.getPower() < stats.getPowerConsumed())
				pp.setVolume(pp.getVolume() + 1);
		}
		else if (stats.getPowerConsumed() < stats.getPowerProduced() - 1)
		{
			// decrease power plant size
			while (pp.getPower() > stats.getPowerConsumed())
				pp.setVolume(pp.getVolume() - 1);
			pp.setVolume(pp.getVolume() + 1);
		}
		ShipModify.modify(ship, pp);	
	}

	/**
	 * @param mShip
	 * @param mStats
	 */
	public static void trimControls(ShipBean ship, ShipStats stats)
	{
		ControlComponent cc = (ControlComponent)findComponent(ship, "ttg.beans.ship.comp.ControlComponent");
		double delta = stats.getControlNeeded() - stats.getControlProvided();
		if (delta > 1)
		{
			double target = cc.getCP() + delta;
			while (cc.getCP() < target)
				cc.setNumber(cc.getNumber() + 1);
		}
		else if (delta < -1)
		{
			double target = cc.getCP() + delta;
			while (cc.getCP() > target)
				cc.setNumber(cc.getNumber() - 1);
			cc.setNumber(cc.getNumber() + 1);
		}
		ShipModify.modify(ship, cc);	
	}

	/**
	 * @param mShip
	 * @param mStats
	 * @param d
	 */
	public static void setDuration(ShipBean ship, ShipStats stats, int hours)
	{
		FuelTank tank = (FuelTank)findComponent(ship, ".FuelTank");
		if (tank == null)
			return;
		//System.out.println("Target="+hours);
		double delta = 13.5;
		int dir = 0;
		while (stats.getDurationHours() != hours)
		{
			int thisdir = 0;
			if (stats.getDurationHours() > hours)
			{
				tank.setVolume(tank.getVolume() - delta);
				thisdir = -1;
			}
			else if (stats.getDurationHours() < hours)
			{
				tank.setVolume(tank.getVolume() + delta);
				thisdir = +1;
			}
			if (dir == -thisdir)
				delta /= 2.0;
			dir = thisdir;
			stats = ShipReport.report(ship);
			//System.out.println(" "+stats.getDurationHours());
		}
		ShipModify.modify(ship, tank);	
	}

	/**
	 * @param mShip
	 * @param mStats
	 * @param d
	 */
	public static void setFuelPurifier(ShipBean ship, ShipStats stats, int hours)
	{
		FuelPurifier purifier = (FuelPurifier)findComponent(ship, ".FuelPurifier");
		if (purifier == null)
			return;
		//System.out.println("Target="+hours);
		double delta = 13.5;
		int dir = 0;
		while (stats.getPurificationTime() != hours)
		{
			int thisdir = 0;
			if (stats.getPurificationTime() == 0)
			{
				purifier.setVolume(1.0);
				thisdir = 0;
			}
			else if (stats.getPurificationTime() > hours)
			{
				purifier.setVolume(purifier.getVolume() + delta);
				thisdir = +1;
			}
			else if (stats.getPurificationTime() < hours)
			{
                if (purifier.getVolume() <= 1.0)
                    break;
				purifier.setVolume(purifier.getVolume() - delta);
				thisdir = -1;
			}
			if (dir == -thisdir)
				delta /= 2.0;
			dir = thisdir;
			stats = ShipReport.report(ship);
			//System.out.println(" "+stats.getPurificationTime());
		}
		ShipModify.modify(ship, purifier);	
	}

	/**
	 * @param mShip
	 * @param mStats
	 * @param d
	 */
	public static void setPassengers(ShipBean ship, ShipStats stats, int num)
	{
		Stateroom rooms = (Stateroom)findComponent(ship, ".Stateroom");
		if (rooms == null)
			return;
		//System.out.println("Target="+num);
		while (stats.getPassengers() != num)
		{			
			if (stats.getPassengers() < num)
				rooms.setNumber(rooms.getNumber() + 1);
			else
				rooms.setNumber(rooms.getNumber() - 1);
			stats = ShipReport.report(ship);
			//System.out.println(stats.getPassengers());			
		}
		ShipModify.modify(ship, rooms);	
	}

	/**
	 * @param mShip
	 * @param mStats
	 * @param d
	 */
	public static void setAgility(ShipBean ship, ShipStats stats, int num)
	{
		PowerPlant pp = (PowerPlant)findComponent(ship, ".PowerPlant");
		if (pp == null)
			return;
		//System.out.println("Target="+num);
        int noThrash = 9;
		while ((stats.getAgility() != num) && (noThrash-- > 0))
		{			
			if (stats.getAgility() < num)
				pp.setVolume(pp.getVolume() + 1);
			else
				pp.setVolume(pp.getVolume() - 1);
			stats = ShipReport.report(ship);
			//System.out.println(stats.getAgility());			
		}
		ShipModify.modify(ship, pp);	
	}
}
