package jo.ttg.ship.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.ship.beans.comp.Airlock;
import jo.ttg.ship.beans.comp.Avionics;
import jo.ttg.ship.beans.comp.Bay100Disintegrator;
import jo.ttg.ship.beans.comp.Bay100JumpDamper;
import jo.ttg.ship.beans.comp.Bay100MesonGun;
import jo.ttg.ship.beans.comp.Bay100Missile;
import jo.ttg.ship.beans.comp.Bay100PA;
import jo.ttg.ship.beans.comp.Bay100Repulsor;
import jo.ttg.ship.beans.comp.Bay100Tractors;
import jo.ttg.ship.beans.comp.Bay50Disintegrator;
import jo.ttg.ship.beans.comp.Bay50FusionGun;
import jo.ttg.ship.beans.comp.Bay50JumpDamper;
import jo.ttg.ship.beans.comp.Bay50MesonGun;
import jo.ttg.ship.beans.comp.Bay50Missile;
import jo.ttg.ship.beans.comp.Bay50PA;
import jo.ttg.ship.beans.comp.Bay50PlasmaGun;
import jo.ttg.ship.beans.comp.Bay50Repulsor;
import jo.ttg.ship.beans.comp.Bay50Tractors;
import jo.ttg.ship.beans.comp.Bunk;
import jo.ttg.ship.beans.comp.CommoMaser;
import jo.ttg.ship.beans.comp.CommoMeson;
import jo.ttg.ship.beans.comp.CommoRadio;
import jo.ttg.ship.beans.comp.Computer;
import jo.ttg.ship.beans.comp.ControlComponent;
import jo.ttg.ship.beans.comp.FuelPurifier;
import jo.ttg.ship.beans.comp.FuelTank;
import jo.ttg.ship.beans.comp.Hull;
import jo.ttg.ship.beans.comp.JumpDrive;
import jo.ttg.ship.beans.comp.LaunchTubes;
import jo.ttg.ship.beans.comp.LowBerth;
import jo.ttg.ship.beans.comp.ManeuverDrive;
import jo.ttg.ship.beans.comp.Missile;
import jo.ttg.ship.beans.comp.PowerPlant;
import jo.ttg.ship.beans.comp.ScreenBlackGlobe;
import jo.ttg.ship.beans.comp.ScreenMeson;
import jo.ttg.ship.beans.comp.ScreenNuclearDamper;
import jo.ttg.ship.beans.comp.ScreenOptimizedBlackGlobe;
import jo.ttg.ship.beans.comp.ScreenOptimizedMeson;
import jo.ttg.ship.beans.comp.ScreenOptimizedProton;
import jo.ttg.ship.beans.comp.ScreenOptimizedWhiteGlobe;
import jo.ttg.ship.beans.comp.ScreenProton;
import jo.ttg.ship.beans.comp.ScreenWhiteGlobe;
import jo.ttg.ship.beans.comp.Seat;
import jo.ttg.ship.beans.comp.SensorActiveEMS;
import jo.ttg.ship.beans.comp.SensorEMSJammer;
import jo.ttg.ship.beans.comp.SensorHighDensitometer;
import jo.ttg.ship.beans.comp.SensorLowDensitometer;
import jo.ttg.ship.beans.comp.SensorNeutrino;
import jo.ttg.ship.beans.comp.SensorPassiveEMS;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.ttg.ship.beans.comp.SpinalDisintegrator;
import jo.ttg.ship.beans.comp.SpinalJumpProjector;
import jo.ttg.ship.beans.comp.SpinalMesonGun;
import jo.ttg.ship.beans.comp.SpinalPA;
import jo.ttg.ship.beans.comp.Stateroom;
import jo.ttg.ship.beans.comp.SubordinateCraftBay;
import jo.ttg.ship.beans.comp.TechLevelComponent;
import jo.ttg.ship.beans.comp.TurretBeamLaser;
import jo.ttg.ship.beans.comp.TurretDisintegrator;
import jo.ttg.ship.beans.comp.TurretFG;
import jo.ttg.ship.beans.comp.TurretMissile;
import jo.ttg.ship.beans.comp.TurretPG;
import jo.ttg.ship.beans.comp.TurretPulseLaser;
import jo.ttg.ship.beans.comp.TurretSandcaster;


public class ComponentChoiceLogic
{
    private static final Class<?>[] compClasses = 
    {
         Airlock.class,
         Avionics.class,
         Bay100Disintegrator.class,
         Bay100JumpDamper.class,
         Bay100MesonGun.class,
         Bay100Missile.class,
         Bay100PA.class,
         Bay100Repulsor.class,
         Bay100Tractors.class,
         Bay50Disintegrator.class,
         Bay50FusionGun.class,
         Bay50JumpDamper.class,
         Bay50MesonGun.class,
         Bay50Missile.class,
         Bay50PA.class,
         Bay50PlasmaGun.class,
         Bay50Repulsor.class,
         Bay50Tractors.class,
         Bunk.class,
         Computer.class,
         ControlComponent.class,
         FuelPurifier.class,
         FuelTank.class,
         Hull.class,
         JumpDrive.class,
         LaunchTubes.class,
         LowBerth.class,
         ManeuverDrive.class,
         Missile.class,
         PowerPlant.class,
         //CommoLaser.class,
         CommoMaser.class,
         CommoMeson.class,
         CommoRadio.class,
         ScreenBlackGlobe.class,
         ScreenMeson.class,
         ScreenNuclearDamper.class,
         ScreenOptimizedBlackGlobe.class,
         ScreenOptimizedMeson.class,
         ScreenOptimizedProton.class,
         ScreenOptimizedWhiteGlobe.class,
         ScreenProton.class,
         ScreenWhiteGlobe.class,
         Seat.class,
         SensorActiveEMS.class,
         SensorEMSJammer.class,
         SensorHighDensitometer.class,
         SensorLowDensitometer.class,
         SensorNeutrino.class,
         SensorPassiveEMS.class,
         SpinalDisintegrator.class,
         SpinalJumpProjector.class,
         SpinalMesonGun.class,
         SpinalPA.class,
         Stateroom.class,
         SubordinateCraftBay.class,
         TurretBeamLaser.class,
         TurretBeamLaser.class,
         TurretDisintegrator.class,
         TurretFG.class,
         TurretMissile.class,
         TurretPG.class,
         TurretPulseLaser.class,
         TurretSandcaster.class,
    };

	public static List<ShipComponent> getAdminComponents()
	{
		List<ShipComponent> ret = new ArrayList<ShipComponent>();
		for (int i = 0; i < compClasses.length; i++)
		{
			try
			{
				ret.add((ShipComponent)compClasses[i].newInstance());
			}
			catch (Exception e)
			{
			}
		}
		return ret;	
	}
	
	public static List<ShipComponent> getTechLevelComponents(int tl)
	{
		List<ShipComponent> ret = new ArrayList<ShipComponent>();
		for (int i = 0; i < compClasses.length; i++)
		{
			try
			{
				ShipComponent comp = (ShipComponent)compClasses[i].newInstance();
				if (comp instanceof TechLevelComponent)
				{
					TechLevelComponent tlComp = (TechLevelComponent)comp;
					int[] range = tlComp.getTechLevelRange();
					if (tl >= range[0])
					{
						if (tl <= range[1])
							tlComp.setTechLevel(tl);
						else
							tlComp.setTechLevel(range[1]);
						ret.add((ShipComponent)tlComp);
					}
				}
				else
				{
					if (comp.getTechLevel() <= tl)
						ret.add(comp);
				}
			}
			catch (Exception e)
			{
			}
		}
		return ret;	
	}
    
    public static List<ShipComponent> getForSaleComponents(char port, int tl)
    {
        List<ShipComponent> ret = new ArrayList<ShipComponent>();
        if ((port != 'A') && (port != 'B') && (port != 'F'))
            return ret;
        ret.addAll(getTechLevelComponents(tl));
        if (port != 'A')
            for (Iterator<ShipComponent> i = ret.iterator(); i.hasNext(); )
            {
                if (i.next() instanceof JumpDrive)
                    i.remove();
            }
        return ret;
    }
}
