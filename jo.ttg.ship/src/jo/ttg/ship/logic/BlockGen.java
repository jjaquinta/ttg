/*
 * Created on Jan 7, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.ship.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.ship.beans.ShipBlockBean;
import jo.ttg.ship.beans.comp.Airlock;
import jo.ttg.ship.beans.comp.CommoRadio;
import jo.ttg.ship.beans.comp.Computer;
import jo.ttg.ship.beans.comp.ControlComponent;
import jo.ttg.ship.beans.comp.ControlHeadsUp;
import jo.ttg.ship.beans.comp.ControlHeadsUpHolo;
import jo.ttg.ship.beans.comp.ControlLargeHolodisplay;
import jo.ttg.ship.beans.comp.FuelPurifier;
import jo.ttg.ship.beans.comp.FuelTank;
import jo.ttg.ship.beans.comp.Hull;
import jo.ttg.ship.beans.comp.JumpDrive;
import jo.ttg.ship.beans.comp.LowBerth;
import jo.ttg.ship.beans.comp.ManeuverDrive;
import jo.ttg.ship.beans.comp.Missile;
import jo.ttg.ship.beans.comp.NumberedComponent;
import jo.ttg.ship.beans.comp.PowerPlant;
import jo.ttg.ship.beans.comp.RangedComponent;
import jo.ttg.ship.beans.comp.SensorActiveEMS;
import jo.ttg.ship.beans.comp.SensorHighDensitometer;
import jo.ttg.ship.beans.comp.SensorLowDensitometer;
import jo.ttg.ship.beans.comp.SensorNeutrino;
import jo.ttg.ship.beans.comp.SensorPassiveEMS;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.ttg.ship.beans.comp.Stateroom;
import jo.ttg.ship.beans.comp.TurretMissile;
import jo.ttg.ship.beans.comp.TurretPulseLaser;
import jo.ttg.ship.beans.comp.TurretSandcaster;
import jo.ttg.ship.beans.comp.VolumeComponent;


/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BlockGen
{
    private static int[] mHullSizes = { 20, 40, 60, 80, 100, 200, 400, 500, 800, 1000 };
    
    private static Map<Integer,List<ShipBlockBean>> mBlocks = new HashMap<Integer,List<ShipBlockBean>>();
    
    public static ShipBlockBean getBlock(String name, int tl)
    {
        for (ShipBlockBean block : genBlocks(tl))
        {
            if (block.getName().equalsIgnoreCase(name))
                try
                {
                    return (ShipBlockBean)block.clone();
                }
                catch (CloneNotSupportedException e)
                {
                }
        }
        return null;
    }
    
    public static synchronized List<ShipBlockBean> genBlocks(int tl)
    {
        if (!mBlocks.containsKey(tl))
        {
            List<ShipBlockBean> block = new ArrayList<ShipBlockBean>();
            mBlocks.put(tl, block); 
	        genHulls(block, tl);
	        genBerths(block, tl);
	        genCommo(block, tl);
	        genWeapons(block, tl);
	        genControls(block, tl);
	        genPowerPlant(block, tl);
	        genDrives(block, tl);
	        genOther(block, tl);
        }        
        return mBlocks.get(tl);
    }
    
	public static void genPowerPlant(List<ShipBlockBean> list, int tl)
	{
	    if (tl < 9)
	        return;
		list.add(makeComponent("100MW Power Plant", findPowerPlant(100.0, tl), tl));
		list.add(makeComponent("200MW Power Plant", findPowerPlant(200.0, tl), tl));
		list.add(makeComponent("400MW Power Plant", findPowerPlant(400.0, tl), tl));
		list.add(makeComponent("800MW Power Plant", findPowerPlant(800.0, tl), tl));
	}
	
	private static PowerPlant findPowerPlant(double mw, int tl)
	{
	    PowerPlant ret = new PowerPlant();
		ShipModify.setTechLevel(ret, tl);
		ret.setVolume(mw*5);
		double lowV = 0;
		double highV = ret.getVolume();
	    ret.setVolume((lowV + highV)/2);
		for (;;)
		{
		    double delta = mw - ret.getPower();
		    if (delta < 0)
		        highV = ret.getVolume();
		    else
		        lowV = ret.getVolume();
		    ret.setVolume((lowV + highV)/2);
		    if (Math.abs(delta) < 1.0)
		        break;
		    if ((highV - lowV) < 1.0)
		        break;
		}
		return ret;
	}
    
	public static void genDrives(List<ShipBlockBean> list, int tl)
	{
	    if (tl < 9)
	        return;
		list.add(makeVolumeComponent("Jump Drive", new JumpDrive(), 1.0, tl));
		list.add(makeVolumeComponent("Maneuver Drive", new ManeuverDrive(), 1.0, tl));
	}
    
	public static void genOther(List<ShipBlockBean> list, int tl)
	{
		list.add(makeVolumeComponent("1t Fuel Tank", new FuelTank(), 1.0, tl));
		list.add(makeVolumeComponent("10t Fuel Tank", new FuelTank(), 10.0, tl));
		list.add(makeVolumeComponent("20t Fuel Tank", new FuelTank(), 20.0, tl));
		list.add(makeVolumeComponent("50t Fuel Tank", new FuelTank(), 50.0, tl));
		if (tl >= 8)
		{
		    list.add(makeVolumeComponent("Fuel Purifier", new FuelPurifier(), 1.0, tl));
		}
		list.add(makeComponent("Airlock", new Airlock(), tl));
	}
    
	public static void genHulls(List<ShipBlockBean> list, int tl)
	{
	    if (tl < 5)
	        return;
	    for (int i = 0; i < mHullSizes.length; i++)
	        list.add(genHull(mHullSizes[i], tl));
	}
	
	private static ShipBlockBean genHull(int tonnage, int tl)
	{
	    ShipBlockBean block = new ShipBlockBean();
	    block.setName(tonnage+"t Hull");
		// basics
		Hull h = new Hull();
		h.setArmor(40);
		h.setConfig(4);
		h.setStreamlining(1);
		ShipModify.setTechLevel(h, tl);
		h.setVolume(tonnage*13.5);
		block.getComponents().add(h);
		return block;
	}
	
	private static void genControls(List<ShipBlockBean> list, int tl)
	{
		if (tl >= 12)
		    list.add(makeNumberedComponent("Large Holodisplay", new ControlLargeHolodisplay(), 1 , tl));
		if (tl >= 13)
		    list.add(makeNumberedComponent("Head's Up Holodisplay", new ControlHeadsUpHolo(), 1, tl));
		if (tl >= 9)
		    list.add(makeNumberedComponent("Head's Up Display", new ControlHeadsUp(), 1, tl));
		if (tl >= 5)
		{
		    list.add(makeNumberedComponent("Control Panel", new ControlComponent(), 1, tl));
			list.add(makeNumberedComponent("Control Console", new ControlComponent(), 8, tl));
			list.add(makeNumberedComponent("Control Bank", new ControlComponent(), 24, tl));
			list.add(makeNumberedComponent("Computer", new Computer(), 3, tl));
			list.add(makeNumberedComponent("Minicomputer", new Computer(), 1, tl));
		}
	}
	
	private static ShipBlockBean makeRangedComponent(String name, RangedComponent c, int delta, int tl)
	{
	    ShipBlockBean block = new ShipBlockBean();
	    block.setName(name);
		ShipModify.setTechLevel(c, tl);
		c.setRange(c.getRangeRange()[1] - delta);
		block.getComponents().add(c);
		block.setSection(c.getSection());
		return block;
	}
	
	private static ShipBlockBean makeNumberedComponent(String name, NumberedComponent c, int amnt, int tl)
	{
	    ShipBlockBean block = new ShipBlockBean();
	    block.setName(name);
		ShipModify.setTechLevel(c, tl);
		c.setNumber(amnt);
		block.getComponents().add(c);
		block.setSection(c.getSection());
		return block;
	}
	
	private static ShipBlockBean makeVolumeComponent(String name, VolumeComponent c, double vol, int tl)
	{
	    ShipBlockBean block = new ShipBlockBean();
	    block.setName(name);
	    ShipModify.setTechLevel((ShipComponent)c, tl);
		c.setVolume(vol*13.5);
		block.getComponents().add((ShipComponent)c);
		block.setSection(((ShipComponent)c).getSection());
		return block;
	}
	
	private static ShipBlockBean makeComponent(String name, ShipComponent c, int tl)
	{
	    ShipBlockBean block = new ShipBlockBean();
	    block.setName(name);
		ShipModify.setTechLevel(c, tl);
		block.getComponents().add(c);
		block.setSection(c.getSection());
		return block;
	}
	
	private static void genBerths(List<ShipBlockBean> list, int tl)
	{
		list.add(makeNumberedComponent("Low Berth", new LowBerth(), 1, tl));
		list.add(makeNumberedComponent("Stateroom", new Stateroom(), 1, tl));
	}
	
	private static void genCommo(List<ShipBlockBean> list, int tl)
	{
		if (tl >= 5)
		    list.add(makeRangedComponent("Radio", new CommoRadio(), 1, tl));
		if (tl >= 10)
		{
		    list.add(makeRangedComponent("Active EMS", new SensorActiveEMS(), 1, tl));
			list.add(makeRangedComponent("Passive EMS", new SensorPassiveEMS(), 1, tl));
			list.add(makeComponent("Low Penetration Desitometer", new SensorLowDensitometer(), tl));
		}
		if (tl >= 11)
		{
			list.add(makeComponent("High Penetration Desitometer", new SensorHighDensitometer(), tl));
			list.add(makeComponent("Neutrino Sensor", new SensorNeutrino(), tl));
		}
	}
	
	private static void genWeapons(List<ShipBlockBean> list, int tl)
	{
		if (tl >= 7)
		{
		    list.add(makeNumberedComponent("Missile Turret", new TurretMissile(), 1, tl));
			list.add(makeNumberedComponent("Double Missile Turret", new TurretMissile(), 2, tl));
			list.add(makeNumberedComponent("Triple Missile Turret", new TurretMissile(), 3, tl));
			list.add(makeNumberedComponent("Sandcaster Turret", new TurretSandcaster(), 1, tl));
			list.add(makeNumberedComponent("Double Sandcaster Turret", new TurretSandcaster(), 2, tl));
			list.add(makeNumberedComponent("Triple Sandcaster Turret", new TurretSandcaster(), 3, tl));
			list.add(makeNumberedComponent("Pulse Laser Turret", new TurretPulseLaser(), 1, tl));
			list.add(makeNumberedComponent("Double Pulse Laser Turret", new TurretPulseLaser(), 2, tl));
			list.add(makeNumberedComponent("Triple Pulse Laser Turret", new TurretPulseLaser(), 3, tl));
		}
		if (tl >= 10)
		{
			list.add(makeNumberedComponent("Missile Storage", new Missile(), 30, tl));
		}
	}
	
	public static void main(String[] argv)
	{
	}
	
//	private static void save(ShipBlockBean block)
//	{
//		System.out.println(block.getName());
//		File f = new File("c:\\temp\\data\\ttg\\ship\\"
//			+StringUtils.trimAll(block.getName())
//			+".block.xml");
//		IOLogic.saveFile(f, block);
//	}
}
