package jo.ttg.ship.beans;

import jo.ttg.ship.beans.comp.Avionics;
import jo.ttg.ship.beans.comp.CommoRadio;
import jo.ttg.ship.beans.comp.Computer;
import jo.ttg.ship.beans.comp.Hull;
import jo.ttg.ship.beans.comp.SensorActiveEMS;
import jo.ttg.ship.beans.comp.SensorEMSJammer;
import jo.ttg.ship.beans.comp.SensorHighDensitometer;
import jo.ttg.ship.beans.comp.SensorLowDensitometer;
import jo.ttg.ship.beans.comp.SensorNeutrino;
import jo.ttg.ship.beans.comp.SensorPassiveEMS;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.ttg.ship.beans.comp.Weapon;

public class ShipTotals
{
    public ShipBean            ship;
    public Hull                hull;
    public Avionics            avionics;
    public CommoRadio              radio;
    public SensorActiveEMS     actEMS;
    public SensorPassiveEMS    passEMS;
    public SensorEMSJammer     emsJammer;
    public SensorHighDensitometer  highDens;
    public SensorLowDensitometer   lowDens;
    public SensorNeutrino      neutrino;
    public Computer            computer;
    
    public double          volume;
    public double          weight;
    public double          powerVolume;
    public double          powerUse;
    public double          powerAux;
    public double          powerGen;
    public double          powerFuelUsage;
    public double          powerFuelTankage;
    public double          fuelTankage;
    public double          fuelPurificationRate;
    public double          cargoSpace;
    public double          cost;
    public boolean         ecp;
    public int             tech;
    public int             jump;
    public double          jumpVolume;
    public int             jumpMinTech;
    public double          jumpFuelVolume;
    public double          manVolume;
    public int[]           sectionCP = new int[ShipComponent.mSectionDescriptions.length];
    public int             totalCP;
    public int             staterooms;
    public int             berths;
    public int             airlocks;
    public int             computerNum;
    public double          controlProvided;
    public int             seats;
    public int             missileSalvo;
    public Weapon          missile;
    public int             missileNum;
    public int             missileTech;
    public Weapon          beamLaser;
    public int             beamLaserNum;
    public int             beamLaserTech;
    public Weapon          pulseLaser;
    public int             pulseLaserNum;
    public int             pulseLaserTech;
    public Weapon          sandcaster;
    public int             sandcasterNum;
    public int             sandcasterTech;
    public Weapon          plasmaGun;
    public int             plasmaGunNum;
    public int             plasmaGunTech;
    public Weapon          fusionGun;
    public int             fusionGunNum;
    public int             fusionGunTech;
    public Weapon          disintegrator;
    public int             disintegratorNum;
    public Weapon          particleAccelerator;
    public int             particleAcceleratorNum;
    public int             particleAcceleratorTech;
    public int             missileMagazine;
    public double          usedHardpoints;
    public int             nuclearDamperFactor;
    public int             mesonFactor;
    public int             blackGlobeFactor;
    public int             protonFactor;
    public int             whiteGlobeFactor;
    public double          hangerVolume;
    
    public String          panels = "";
    public String          special = "";
    public String          subcraft = "";
}