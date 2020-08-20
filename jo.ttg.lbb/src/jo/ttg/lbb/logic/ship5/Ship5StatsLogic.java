package jo.ttg.lbb.logic.ship5;

import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.ttg.lbb.data.ship5.Ship5Design.Ship5DesignLaunchTube;
import jo.ttg.lbb.data.ship5.Ship5Design.Ship5DesignSubCraft;
import jo.ttg.lbb.data.ship5.Ship5Stats;

public class Ship5StatsLogic
{
    public static Ship5Stats getStats(Ship5Design design)
    {
        Ship5Stats stats = new Ship5Stats();
        stats.setDesign(design);
        return updateStats(stats);
    }
    public static Ship5Stats updateStats(Ship5Stats stats)
    {
        Ship5Design design = stats.getDesign();
        doErrorChecking(stats, design);
        doFuelUsage(stats, design);
        doSpaceUsage(stats, design);
        doEnergyUsage(stats, design);
        doCosts(stats, design);
        doCrew(stats, design);
        doCapacityChecking(stats, design);
        return stats;
    }
    private static void doCapacityChecking(Ship5Stats stats, Ship5Design design)
    {
        int fuelRequired = stats.getTotalFuelUsage();
        if (fuelRequired > design.getFuelTankage())
            stats.getErrors().add("Fuel required is "+fuelRequired+", which exceedes the fuel available of "+design.getFuelTankage());
        int energyRequired = stats.getTotalEnergyUsage();
        if (energyRequired > stats.getEnergyProduced())
            stats.getErrors().add("Energy required is "+energyRequired+", which exceedes the energy produced of "+stats.getEnergyProduced());
        int spaceRequired = stats.getTotalSpaceUsage();
        if (spaceRequired > design.getHullTonnage())
            stats.getErrors().add("Space required is "+spaceRequired+", which exceedes the space available of "+design.getHullTonnage());
        stats.fireMonotonicPropertyChange("errors", stats.getErrors());
    }
    private static void doCosts(Ship5Stats stats, Ship5Design design)
    {
        stats.getCostUsage().clear();
        stats.getCostUsage().put("Hull", stats.getHullCost());
        if (design.getManeuverDriveNumber() > 0)
            stats.getCostUsage().put("Maneuver Driver", stats.getDriveManeuverCost());
        if (design.getJumpDriveNumber() > 0)
            stats.getCostUsage().put("Jump Driver", stats.getDriveJumpCost());
        if (design.getPowerPlantNumber() > 0)
            stats.getCostUsage().put("Power Plant", stats.getDrivePowerPlantCost());
        if (design.isFuelScoops())
            stats.getCostUsage().put("Fuel Scoops", stats.getFuelScoopsCost());
        if (design.isFuelPurification())
            stats.getCostUsage().put("Fuel Purification", stats.getFuelPurificationCost());
        stats.getCostUsage().put("Bridge", stats.getBridgeCost());
        if (design.getBridgeAuxiliaryCount() > 0)
            stats.getCostUsage().put("Auxiliary Bridge", stats.getBridgeAuxCost());
        if (design.getComputerCode() > Ship5Design.COMPUTER_NONE)
            stats.getCostUsage().put("Computer", stats.getComputerCost());
        if (design.getArmorFactors() > 0)
            stats.getCostUsage().put("Armor", stats.getArmorCost());
        if (design.getMajorWeapon() != Ship5Design.MAJOR_NONE) 
            stats.getCostUsage().put("Major Weapon", stats.getMajorCost());
        if (design.getBays100Meson() > 0)
            stats.getCostUsage().put("100t Meson Bays", stats.getBay100MesonCost());
        if (design.getBays100Particle() > 0)
            stats.getCostUsage().put("100t ParticleAccelerator Bays", stats.getBay100ParticleCost());
        if (design.getBays100Repulsor() > 0)
            stats.getCostUsage().put("100t Repulsor Bays", stats.getBay100RepulsorCost());
        if (design.getBays100Missile() > 0)
            stats.getCostUsage().put("100t Missile Bays", stats.getBay100MissileCost());
        if (design.getBays50Meson() > 0)
            stats.getCostUsage().put("50t Meson Bays", stats.getBay50MesonCost());
        if (design.getBays50Particle() > 0)
            stats.getCostUsage().put("50t ParticleAccelerator Bays", stats.getBay50ParticleCost());
        if (design.getBays50Repulsor() > 0)
            stats.getCostUsage().put("50t Repulsor Bays", stats.getBay50RepulsorCost());
        if (design.getBays50Missile() > 0)
            stats.getCostUsage().put("50t Missile Bays", stats.getBay50MissileCost());
        if (design.getBays50Plasma() > 0)
            stats.getCostUsage().put("50t Plasma Bays", stats.getBay50PlasmaCost());
        if (design.getBays50Fusion() > 0)
            stats.getCostUsage().put("50t Fusion Bays", stats.getBay50FusionCost());
        if (design.getTurretMissile() > 0)
            stats.getCostUsage().put("Missile Turrets", stats.getTurretMissileCost());
        if (design.getTurretBeamLaser() > 0)
            stats.getCostUsage().put("Beam Laser Turrets", stats.getTurretBeamLaserCost());
        if (design.getTurretPulseLaser() > 0)
            stats.getCostUsage().put("Pulse Laser Turrets", stats.getTurretPulseLaserCost());
        if (design.getTurretPlasmaGun() > 0)
            stats.getCostUsage().put("Plasma Gun Turrets", stats.getTurretPlasmaGunCost());
        if (design.getTurretFusionGun() > 0)
            stats.getCostUsage().put("Fusion Gun Turrets", stats.getTurretFusionGunCost());
        if (design.getTurretSandcaster() > 0)
            stats.getCostUsage().put("Sandcaster Turrets", stats.getTurretSandcasterCost());
        if (design.getTurretParticle() > 0)
            stats.getCostUsage().put("Particle Accelerator Turrets", stats.getTurretParticleCost());
        if (design.getBarbetteParticle() > 0)
            stats.getCostUsage().put("Particle Accelerator Barbettes", stats.getBarbetteParticleCost());
        if (design.getScreenNuclearCode() > '0')
            stats.getCostUsage().put("Nuclear Damper Screens", stats.getScreenNuclearCost());
        if (design.getScreenMesonCode() > '0')
            stats.getCostUsage().put("Meson Screens", stats.getScreenMesonCost());
        if (design.getScreenForceCode() > '0')
            stats.getCostUsage().put("Force Screens", stats.getScreenForceCost());
        for (Ship5DesignSubCraft subCraft : design.getSubCraft())
        {
            String name = getSubCraftName(subCraft);
            stats.getCostUsage().put("Hanger for "+name, Ship5DesignLogic.getHangerCost(subCraft.getHullTonnage(), 
                    subCraft.getQuantity(), design.getHullTonnage()));
        }
        for (Ship5DesignLaunchTube launchTube : design.getLaunchTubes())
        {
            String name = getLaunchTubeName(launchTube);
            stats.getCostUsage().put(name, Ship5DesignLogic.getLaunchTubeCost(launchTube.getCapacity(), 
                    launchTube.getQuantity()));
        }
        if (design.getStaterooms() > 0)
            stats.getCostUsage().put("Staterooms", design.getStaterooms()*500000L);
        if (design.getLowBerths() > 0)
            stats.getCostUsage().put("Low Berths", design.getLowBerths()*50000L);
        if (design.getEmergencyLowBerths() > 0)
            stats.getCostUsage().put("Emergency Low Berths", design.getEmergencyLowBerths()*100000L);
        stats.fireMonotonicPropertyChange("costUsage", stats.getCostUsage());
    }
    private static void doEnergyUsage(Ship5Stats stats, Ship5Design design)
    {
        stats.getEnergyUsage().clear();
        if (design.getComputerCode() > Ship5Design.COMPUTER_NONE)
            stats.getEnergyUsage().put("Computer", stats.getComputerEnergy());
        if (design.getMajorWeapon() != Ship5Design.MAJOR_NONE) 
            stats.getEnergyUsage().put("Major Weapon", stats.getMajorEnergy());
        if (design.getBays100Meson() > 0)
            stats.getEnergyUsage().put("100t Meson Bays", stats.getBay100MesonEnergy());
        if (design.getBays100Particle() > 0)
            stats.getEnergyUsage().put("100t ParticleAccelerator Bays", stats.getBay100ParticleEnergy());
        if (design.getBays100Repulsor() > 0)
            stats.getEnergyUsage().put("100t Repulsor Bays", stats.getBay100RepulsorEnergy());
        if (design.getBays100Missile() > 0)
            stats.getEnergyUsage().put("100t Missile Bays", stats.getBay100MissileEnergy());
        if (design.getBays50Meson() > 0)
            stats.getEnergyUsage().put("50t Meson Bays", stats.getBay50MesonEnergy());
        if (design.getBays50Particle() > 0)
            stats.getEnergyUsage().put("50t ParticleAccelerator Bays", stats.getBay50ParticleEnergy());
        if (design.getBays50Repulsor() > 0)
            stats.getEnergyUsage().put("50t Repulsor Bays", stats.getBay50RepulsorEnergy());
        if (design.getBays50Missile() > 0)
            stats.getEnergyUsage().put("50t Missile Bays", stats.getBay50MissileEnergy());
        if (design.getBays50Plasma() > 0)
            stats.getEnergyUsage().put("50t Plasma Bays", stats.getBay50PlasmaEnergy());
        if (design.getBays50Fusion() > 0)
            stats.getEnergyUsage().put("50t Fusion Bays", stats.getBay50FusionEnergy());
        if (design.getTurretMissile() > 0)
            stats.getEnergyUsage().put("Missile Turrets", stats.getTurretMissileEnergy());
        if (design.getTurretBeamLaser() > 0)
            stats.getEnergyUsage().put("Beam Laser Turrets", stats.getTurretBeamLaserEnergy());
        if (design.getTurretPulseLaser() > 0)
            stats.getEnergyUsage().put("Pulse Laser Turrets", stats.getTurretPulseLaserEnergy());
        if (design.getTurretPlasmaGun() > 0)
            stats.getEnergyUsage().put("Plasma Gun Turrets", stats.getTurretPlasmaGunEnergy());
        if (design.getTurretFusionGun() > 0)
            stats.getEnergyUsage().put("Fusion Gun Turrets", stats.getTurretFusionGunEnergy());
        if (design.getTurretSandcaster() > 0)
            stats.getEnergyUsage().put("Sandcaster Turrets", stats.getTurretSandcasterEnergy());
        if (design.getTurretParticle() > 0)
            stats.getEnergyUsage().put("Particle Accelerator Turrets", stats.getTurretParticleEnergy());
        if (design.getBarbetteParticle() > 0)
            stats.getEnergyUsage().put("Particle Accelerator Barbettes", stats.getBarbetteParticleEnergy());
        if (design.getScreenNuclearCode() > '0')
            stats.getEnergyUsage().put("Nuclear Damper Screens", stats.getScreenNuclearEnergy());
        if (design.getScreenMesonCode() > '0')
            stats.getEnergyUsage().put("Meson Screens", stats.getScreenMesonEnergy());
        stats.fireMonotonicPropertyChange("energyUsage", stats.getEnergyUsage());
    }
    private static void doSpaceUsage(Ship5Stats stats, Ship5Design design)
    {
        stats.getSpaceUsage().clear();
        if (design.getHullConfigurationCode() == Ship5Design.CONFIG_PLANETOID)
            stats.getSpaceUsage().put("Planetoid Buffering", design.getHullTonnage()*20/100);
        else if (design.getHullConfigurationCode() == Ship5Design.CONFIG_BUFFERED_PLANETOID)
            stats.getSpaceUsage().put("Planetoid Buffering", design.getHullTonnage()*35/100);
        if (design.getManeuverDriveNumber() > 0)
            stats.getSpaceUsage().put("Maneuver Drive", stats.getDriveManeuverVolume());
        if (design.getJumpDriveNumber() > 0)
            stats.getSpaceUsage().put("Jump Drive", stats.getDriveJumpVolume());
        if (design.getPowerPlantNumber() > 0)
            stats.getSpaceUsage().put("Power Plant", stats.getDrivePowerPlantVolume());
        stats.getSpaceUsage().put("Fuel", design.getFuelTankage());
        if (design.isFuelPurification())
            stats.getSpaceUsage().put("Fuel Purification", stats.getFuelPurificationVolume());
        stats.getSpaceUsage().put("Bridge", stats.getBridgeVolume());
        if (design.getBridgeAuxiliaryCount() > 0)
            stats.getSpaceUsage().put("Auxiliary Bridge", stats.getBridgeAuxVolume());
        if (design.getComputerCode() > Ship5Design.COMPUTER_NONE)
            stats.getSpaceUsage().put("Computer", stats.getComputerVolume());
        if (design.getArmorFactors() > 0)
            stats.getSpaceUsage().put("Armor", stats.getArmorVolume());
        if (design.getMajorWeapon() != Ship5Design.MAJOR_NONE) 
            stats.getSpaceUsage().put("Major Weapon", stats.getMajorVolume());
        if (design.getBays100Meson() > 0)
            stats.getSpaceUsage().put("100t Meson Bays", design.getBays100Meson()*100);
        if (design.getBays100Particle() > 0)
            stats.getSpaceUsage().put("100t ParticleAccelerator Bays", design.getBays100Particle()*100);
        if (design.getBays100Repulsor() > 0)
            stats.getSpaceUsage().put("100t Repulsor Bays", design.getBays100Repulsor()*100);
        if (design.getBays100Missile() > 0)
            stats.getSpaceUsage().put("100t Missile Bays", design.getBays100Missile()*100);
        if (design.getBays50Meson() > 0)
            stats.getSpaceUsage().put("50t Meson Bays", design.getBays50Meson()*50);
        if (design.getBays50Particle() > 0)
            stats.getSpaceUsage().put("50t ParticleAccelerator Bays", design.getBays50Particle()*50);
        if (design.getBays50Repulsor() > 0)
            stats.getSpaceUsage().put("50t Repulsor Bays", design.getBays50Repulsor()*50);
        if (design.getBays50Missile() > 0)
            stats.getSpaceUsage().put("50t Missile Bays", design.getBays50Missile()*50);
        if (design.getBays50Plasma() > 0)
            stats.getSpaceUsage().put("50t Plasma Bays", design.getBays50Plasma()*50);
        if (design.getBays50Fusion() > 0)
            stats.getSpaceUsage().put("50t Fusion Bays", design.getBays50Fusion()*50);
        // TODO: turret volumes. Depends on how split into groups
        if (design.getScreenNuclearCode() > '0')
            stats.getSpaceUsage().put("Nuclear Damper Screens", stats.getScreenNuclearVolume());
        if (design.getScreenMesonCode() > '0')
            stats.getSpaceUsage().put("Meson Screens", stats.getScreenMesonVolume());
        if (design.getScreenForceCode() > '0')
            stats.getSpaceUsage().put("Force Screens", stats.getScreenForceVolume());
        for (Ship5DesignSubCraft subCraft : design.getSubCraft())
        {
            String name = getSubCraftName(subCraft);
            stats.getSpaceUsage().put("Hanger for "+name, Ship5DesignLogic.getHangerVolume(subCraft.getHullTonnage(), 
                    subCraft.getQuantity(), design.getHullTonnage()));
        }
        for (Ship5DesignLaunchTube launchTube : design.getLaunchTubes())
        {
            String name = getLaunchTubeName(launchTube);
            stats.getSpaceUsage().put(name, Ship5DesignLogic.getLaunchTubeVolume(launchTube.getCapacity(), 
                    launchTube.getQuantity()));
        }
        if (design.getStaterooms() > 0)
            stats.getSpaceUsage().put("Staterooms", design.getStaterooms()*4);
        if (design.getLowBerths() > 0)
            stats.getSpaceUsage().put("Low Berths", design.getLowBerths()/2);
        if (design.getEmergencyLowBerths() > 0)
            stats.getSpaceUsage().put("Emergency Low Berths", design.getEmergencyLowBerths());
        stats.fireMonotonicPropertyChange("spaceUsage", stats.getSpaceUsage());
    }
    private static void doFuelUsage(Ship5Stats stats, Ship5Design design)
    {
        stats.getFuelUsage().clear();
        if (design.getJumpDriveNumber() > 0)
            stats.getFuelUsage().put("Jump Drive", stats.getFuelJumpVolume());
        if (design.getPowerPlantNumber() > 0)
            stats.getFuelUsage().put("Power Plant", stats.getFuelPowerPlantVolume());
        stats.fireMonotonicPropertyChange("fuelUsage", stats.getFuelUsage());
    }
    private static void doErrorChecking(Ship5Stats stats, Ship5Design design)
    {
        stats.getErrors().clear();
        int tlForManeuver = stats.getDriveManeuverMinimumTechLevel();
        if (tlForManeuver > design.getTechLevel())
            stats.getErrors().add("A minimum tech level of "+tlForManeuver+" is needed for maneuver drive number "+design.getManeuverDriveNumber()+", ship tech level is "+design.getTechLevel());
        int tlForJump = stats.getDriveJumpMinimumTechLevel();
        if (tlForJump > design.getTechLevel())
            stats.getErrors().add("A minimum tech level of "+tlForJump+" is needed for jump drive number "+design.getJumpDriveNumber()+", ship tech level is "+design.getTechLevel());
        if (design.getJumpDriveNumber() > design.getPowerPlantNumber())
            stats.getErrors().add("A minimum power plant of "+design.getJumpDriveNumber()+" is needed for jump drive "+design.getJumpDriveNumber()+", ship power plant is "+design.getPowerPlantNumber());
        if (design.getManeuverDriveNumber() > design.getPowerPlantNumber())
            stats.getErrors().add("A minimum power plant of "+design.getManeuverDriveNumber()+" is needed for maneuver drive "+design.getManeuverDriveNumber()+", ship power plant is "+design.getPowerPlantNumber());
        if (stats.getComputerTechLevel() > design.getTechLevel())
            stats.getErrors().add("A computer of "+design.getComputerCode()+" requires a tech level of "+stats.getComputerTechLevel()+", ship tech level is "+design.getTechLevel());
        if (stats.getHullCode() > stats.getComputerHull())
            stats.getErrors().add("A computer of "+design.getComputerCode()+" can support a hull of code "+stats.getComputerHull()+", ship hull is "+stats.getHullCode());
        if ((design.getHullConfigurationCode() == Ship5Design.CONFIG_DISPERSED) && (design.getArmorFactors() > 0))
            stats.getErrors().add("A dispersed hull cannot have additional armor factors.");
        if ((design.getHullConfigurationCode() == Ship5Design.CONFIG_BUFFERED_PLANETOID) && (design.getArmorFactors() > 0))
            stats.getErrors().add("A buffered planetoid hull cannot have additional armor factors.");
        if (design.getArmorFactors() > design.getTechLevel())
            stats.getErrors().add("Armor factors of "+design.getArmorFactors()+" exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getMajorWeapon() != Ship5Design.MAJOR_NONE) && stats.getMajorTechLevel() > design.getTechLevel())
            stats.getErrors().add("Major wepaon of type "+design.getMajorWeapon()+" and code "+design.getMajorCode()+" requires tech level "+stats.getMajorTechLevel()+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getBays100Meson() > 0) && (design.getTechLevel() < 13))
            stats.getErrors().add("100t Meson Bay requries tech level "+13+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getBays100Particle() > 0) && (design.getTechLevel() < 8))
            stats.getErrors().add("100t Particle Accelerator Bay requries tech level "+8+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getBays100Repulsor() > 0) && (design.getTechLevel() < 10))
            stats.getErrors().add("100t Repulsor Bay requries tech level "+10+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getBays100Missile() > 0) && (design.getTechLevel() < 7))
            stats.getErrors().add("100t Missile Bay requries tech level "+7+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getBays50Meson() > 0) && (design.getTechLevel() < 15))
            stats.getErrors().add("50t Meson Bay requries tech level "+15+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getBays50Particle() > 0) && (design.getTechLevel() < 10))
            stats.getErrors().add("50t Particle Accelerator Bay requries tech level "+10+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getBays50Repulsor() > 0) && (design.getTechLevel() < 14))
            stats.getErrors().add("50t Repulsor Bay requries tech level "+14+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getBays50Missile() > 0) && (design.getTechLevel() < 10))
            stats.getErrors().add("50t Missle Bay requries tech level "+10+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getBays50Plasma() > 0) && (design.getTechLevel() < 10))
            stats.getErrors().add("50t Plasma Bay requries tech level "+10+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getBays50Fusion() > 0) && (design.getTechLevel() < 12))
            stats.getErrors().add("50t Fusion Bay requries tech level "+12+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getTurretMissile() > 0) && (design.getTechLevel() < 7))
            stats.getErrors().add("Missile Turrets requrie tech level "+7+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getTurretBeamLaser() > 0) && (design.getTechLevel() < 7))
            stats.getErrors().add("Beam Laser Turrets requrie tech level "+7+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getTurretPulseLaser() > 0) && (design.getTechLevel() < 7))
            stats.getErrors().add("Pulse Laser Turrets requrie tech level "+7+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getTurretPlasmaGun() > 0) && (design.getTechLevel() < 10))
            stats.getErrors().add("Plasma Gun Turrets requrie tech level "+10+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getTurretFusionGun() > 0) && (design.getTechLevel() < 12))
            stats.getErrors().add("Fusion Gun Turrets requrie tech level "+12+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getTurretSandcaster() > 0) && (design.getTechLevel() < 7))
            stats.getErrors().add("Sandcaster Turrets requrie tech level "+7+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getTurretParticle() > 0) && (design.getTechLevel() < 15))
            stats.getErrors().add("Particle Accelerator Turrets requrie tech level "+15+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getBarbetteParticle() > 0) && (design.getTechLevel() < 14))
            stats.getErrors().add("Particle Accelerator Barbettes requrie tech level "+14+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getScreenNuclearCode() > 0) && (design.getTechLevel() < Ship5DesignLogic.screenNuclearTechLevel(design.getScreenNuclearCode())))
            stats.getErrors().add("Nuclear Damper Screens of factor "+design.getScreenNuclearCode()+" requrie tech level "+Ship5DesignLogic.screenNuclearTechLevel(design.getScreenNuclearCode())+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getScreenMesonCode() > 0) && (design.getTechLevel() < Ship5DesignLogic.screenMesonTechLevel(design.getScreenMesonCode())))
            stats.getErrors().add("Meson Screens of factor "+design.getScreenMesonCode()+" requrie tech level "+Ship5DesignLogic.screenMesonTechLevel(design.getScreenNuclearCode())+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getScreenForceCode() > 0) && (design.getTechLevel() < Ship5DesignLogic.screenForceTechLevel(design.getScreenForceCode())))
            stats.getErrors().add("Force Screens of factor "+design.getScreenForceCode()+" requrie tech level "+Ship5DesignLogic.screenForceTechLevel(design.getScreenForceCode())+" which exceedes tech level of ship "+design.getTechLevel()+".");
        if ((design.getTurretBeamLaser() > 0) && (design.getTurretPulseLaser() > 0))
            stats.getErrors().add("Pulse Laser Turrets and Beam Laser Turrents may not be installed on the same ship.");
        if ((design.getTurretPlasmaGun() > 0) && (design.getTurretFusionGun() > 0))
            stats.getErrors().add("Fusion Gun Turrets and Plasma Gun Turrets maay not be installed on the same ship.");
        if ((design.getHullConfigurationCode() == Ship5Design.CONFIG_PLANETOID) && (design.getHullTonnage() < 100))
            stats.getErrors().add("Planetoid hulls cannot be used for small craft.");
        if ((design.getHullConfigurationCode() == Ship5Design.CONFIG_BUFFERED_PLANETOID) && (design.getHullTonnage() < 100))
            stats.getErrors().add("Buffered Planetoid hulls cannot be used for small craft.");
    }
    private static String getSubCraftName(Ship5DesignSubCraft subCraft)
    {
        String name = subCraft.getShipName()+" ("+subCraft.getHullTonnage()+"t)";
        if (subCraft.getQuantity() > 1)
            name += " x"+subCraft.getQuantity();
        return name;
    }
    private static String getLaunchTubeName(Ship5DesignLaunchTube launchTube)
    {
        String name = "Launch Tube ("+launchTube.getCapacity()+"t)";
        if (launchTube.getQuantity() > 1)
            name += " x"+launchTube.getQuantity();
        return name;
    }
    private static void doCrew(Ship5Stats stats, Ship5Design design)
    {
        if (design.getHullTonnage() <= 1000)
            doCrewBook2(stats, design);
        else
            doCrewBook5(stats, design);
        stats.fireMonotonicPropertyChange("crew");
    }
    private static void doCrewBook2(Ship5Stats stats, Ship5Design design)
    {
        // TODO - book 2
        stats.setCrewCommand(1); // pilot
        stats.setCrewExecutive(0);
        stats.setCrewComputer(0);
        if (design.getHullTonnage() > 200)
            stats.setCrewNavigation(1);
        else
            stats.setCrewNavigation(0);
        stats.setCrewMedical(0);
        stats.setCrewCommunications(0);
        stats.setCrewSupport(0);
        if (design.getHullTonnage() > 200)
        {
            int tonsOfDrives = stats.getDriveManeuverVolume() + stats.getDriveJumpVolume() + stats.getDrivePowerPlantVolume();
            int engineers = tonsOfDrives/35;
            stats.setCrewChiefEngineer(1);
            stats.setCrewEngineer(engineers - 1);
        }
        else
        {
            stats.setCrewChiefEngineer(0);
            stats.setCrewEngineer(0);
        }
        stats.setCrewSecondEngineer(0);
        stats.setCrewEngineerOfficer(0);
        stats.setCrewEngineerPettyOfficer(0);
        stats.setCrewChiefGunner(0);
        stats.setCrewGunnerPettyOfficer(0);
        stats.setCrewGunner(design.getTurretWeaponCount());
        stats.setCrewFlightControlOfficer(0);
        stats.setCrewFlightMaintenence(0);
        stats.setCrewFlightCrew(0);
        stats.setCrewFlightDriver(0);
        stats.setCrewService(0);
        if ((design.getHullTonnage() >= 200) && (design.getJumpDriveNumber() > 0))
            stats.setCrewMedical(1);
        else
            stats.setCrewMedical(0);
    }
    private static void doCrewBook5(Ship5Stats stats, Ship5Design design)
    {
        // command
        stats.setCrewCommand(1);
        stats.setCrewExecutive(1);
        stats.setCrewComputer(1);
        stats.setCrewNavigation(2);
        stats.setCrewMedical(1);
        stats.setCrewCommunications(1);
        int totalCommand = Math.max(11, design.getHullTonnage()*5/10000);
        totalCommand -= stats.getCrewCommand() - stats.getCrewExecutive() - stats.getCrewComputer() - stats.getCrewNavigation()
                - stats.getCrewMedical() - stats.getCrewCommunications();
        stats.setCrewSupport(totalCommand);
        // engineering
        int tonsOfDrives = stats.getDriveManeuverVolume() + stats.getDriveJumpVolume() + stats.getDrivePowerPlantVolume();
        int totalEngineering = Math.max(1, tonsOfDrives/100);
        stats.setCrewChiefEngineer(1);
        stats.setCrewSecondEngineer(1);
        stats.setCrewEngineerOfficer(totalEngineering*10/100);
        stats.setCrewEngineerPettyOfficer(totalEngineering*20/100);
        totalEngineering -= stats.getCrewChiefEngineer() - stats.getCrewSecondEngineer() 
                - stats.getCrewEngineerOfficer() - stats.getCrewEngineerPettyOfficer();
        stats.setCrewEngineer(totalEngineering);
        // gunnery
        if (design.isAnyWeapons() || design.isAnyScreens())
        {
            stats.setCrewChiefGunner(1);
            int pettyOfficers = 0;
            if (design.isAnyMesonWeapons())
                pettyOfficers++;
            if (design.isAnyEnergyWeapons())
                pettyOfficers++;
            if (design.isAnyParticleWeapons())
                pettyOfficers++;
            if (design.isAnyLaserWeapons())
                pettyOfficers++;
            if (design.isAnyMissileWeapons())
                pettyOfficers++;
            if (design.isAnySandWeapons())
                pettyOfficers++;
            if (design.isAnyRepulsorWeapons())
                pettyOfficers++;
            stats.setCrewGunnerPettyOfficer(pettyOfficers);
            int gunners = 0;
            if (design.getMajorWeapon() != Ship5Design.MAJOR_NONE)
                gunners += stats.getMajorVolume()/100;
            gunners += design.getBayWeaponCount()*2;
            // TODO: 1 per turret battery
            gunners += 4*design.getScreenCount();
            stats.setCrewGunner(gunners);
        }
        else
        {
            stats.setCrewChiefGunner(0);
            stats.setCrewGunnerPettyOfficer(0);
            stats.setCrewGunner(0);
        }
        // flight
        if (design.getSubCraft().size() > 0)
        {
            stats.setCrewFlightControlOfficer(1);
            for (Ship5DesignSubCraft subCraft : design.getSubCraft())
            {
                if (subCraft.isVehicle())
                    stats.setCrewFlightDriver(stats.getCrewFlightDriver() + subCraft.getQuantity());
                else
                {
                stats.setCrewFlightMaintenence(stats.getCrewFlightMaintenence() + subCraft.getQuantity());
                stats.setCrewFlightCrew(stats.getCrewFlightCrew() + subCraft.getQuantity()*subCraft.getCrew());
                }
            }
        }
        else
        {
            stats.setCrewFlightControlOfficer(0);
            stats.setCrewFlightMaintenence(0);
            stats.setCrewFlightCrew(0);
            stats.setCrewFlightDriver(0);
        }
        // service
        if (design.getCrewTroops() == 0)
            stats.setCrewService(design.getHullTonnage()*3/1000);
        else
            stats.setCrewService(design.getHullTonnage()*2/1000);
        int soFar = stats.getCrewCommandCount() + stats.getCrewEngineeringCount() + stats.getCrewGunneryCount() + stats.getCrewFlightCount()
            + stats.getCrewService() + design.getCrewTroops() + stats.getCrewFozenWatch();
        stats.setCrewMedical(soFar/240);
    }
}
