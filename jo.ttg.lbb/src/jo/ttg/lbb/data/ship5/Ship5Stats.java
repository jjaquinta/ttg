package jo.ttg.lbb.data.ship5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.lbb.logic.ship5.Ship5DesignLogic;
import jo.util.beans.PCSBean;

public class Ship5Stats extends PCSBean
{
    private Ship5Design mDesign;
    private List<String> mErrors = new ArrayList<String>();
    private Map<String,Integer> mSpaceUsage = new HashMap<>();
    private Map<String,Integer> mFuelUsage = new HashMap<>();
    private Map<String,Long> mCostUsage = new HashMap<>();
    private Map<String,Integer> mEnergyUsage = new HashMap<>();
    private int mCrewCommand;
    private int mCrewExecutive;
    private int mCrewComputer;
    private int mCrewNavigation;
    private int mCrewMedicalOfficer;
    private int mCrewCommunications;
    private int mCrewSupport;
    private int mCrewChiefEngineer;
    private int mCrewSecondEngineer;
    private int mCrewEngineer;
    private int mCrewEngineerOfficer;
    private int mCrewEngineerPettyOfficer;
    private int mCrewChiefGunner;
    private int mCrewGunnerOfficer;
    private int mCrewGunnerPettyOfficer;
    private int mCrewGunner;
    private int mCrewFlightControlOfficer;
    private int mCrewFlightMaintenence;
    private int mCrewFlightCrew;
    private int mCrewFlightDriver;
    private int mCrewService;
    private int mCrewFozenWatch;
    private int mCrewMedical;
    private int mCrewPassengers;

    // calculated values
    public int getTotalSpaceUsage()
    {
        int tot = 0;
        for (Integer v : mSpaceUsage.values())
            tot += v;
        return tot;
    }
    
    public int getTotalFuelUsage()
    {
        int tot = 0;
        for (Integer v : mFuelUsage.values())
            tot += v;
        return tot;
    }
    
    public int getTotalEnergyUsage()
    {
        int tot = 0;
        for (Integer v : mEnergyUsage.values())
            tot += v;
        return tot;
    }
    
    public long getTotalCost()
    {
        long tot = 0;
        for (Long v : mCostUsage.values())
            tot += v;
        return tot;
    }
    
    public char getHullCode()
    {
        return Ship5DesignLogic.hullTonnageToCode(mDesign.getHullTonnage());
    }

    public String getHullConfigurationName()
    {
        return Ship5DesignLogic.hullConfigurationToName(mDesign.getHullConfigurationCode());
    }

    public String getHullConfigurationStreamlined()
    {
        return Ship5DesignLogic.hullConfigurationToStreamlined(mDesign.getHullConfigurationCode());
    }

    public double getHullConfigurationPriceModifier()
    {
        return Ship5DesignLogic.hullConfigurationPriceModifier(mDesign.getHullConfigurationCode());
    }
    
    public long getHullCost()
    {
        return Ship5DesignLogic.hullCost(mDesign.getHullTonnage(), mDesign.getHullConfigurationCode());
    }

    public int getDriveManeuverMinimumTechLevel()
    {
        return Ship5DesignLogic.driveManeuverMinimumTechLevel(mDesign.getManeuverDriveNumber());
    }

    public int getDriveJumpMinimumTechLevel()
    {
        return Ship5DesignLogic.driveJumpMinimumTechLevel(mDesign.getJumpDriveNumber());
    }
    
    public int getDriveManeuverVolumePercent()
    {
        return Ship5DesignLogic.driveManeuverVolumePercent(mDesign.getManeuverDriveNumber());
    }
    
    public int getDriveJumpVolumePercent()
    {
        return Ship5DesignLogic.driveJumpVolumePercent(mDesign.getJumpDriveNumber());
    }
    
    public int getDriveManeuverVolume()
    {
        return getDriveManeuverVolumePercent()*mDesign.getHullTonnage()/100;
    }
    
    public int getDriveJumpVolume()
    {
        return getDriveJumpVolumePercent()*mDesign.getHullTonnage()/100;
    }
    
    public int getDrivePowerPlantVolumePercent()
    {
        return Ship5DesignLogic.drivePowerVolumePercent(mDesign.getTechLevel());
    }
    
    public int getDrivePowerPlantVolume()
    {
        return getDrivePowerPlantVolumePercent()*mDesign.getPowerPlantNumber()*mDesign.getHullTonnage()/100;
    }
    
    public int getFuelJumpPercent()
    {
        return Ship5DesignLogic.fuelJumpVolumePercent(mDesign.getJumpDriveNumber());
    }
    
    public int getFuelJumpVolume()
    {
        return Ship5DesignLogic.fuelJumpVolumePercent(mDesign.getJumpDriveNumber())*mDesign.getHullTonnage()/100;
    }
    
    public int getFuelPowerPlantVolume()
    {
        return 1*mDesign.getHullTonnage()/100;
    }

    public long getDriveManeuverCostPerTon()
    {
        return Ship5DesignLogic.driveManeuverCostPerTon(mDesign.getManeuverDriveNumber());
    }

    public long getDriveJumpCostPerTon()
    {
        return Ship5DesignLogic.driveJumpCostPerTon(mDesign.getJumpDriveNumber());
    }

    public long getDrivePowerCostPerTon()
    {
        return Ship5DesignLogic.drivePowerCostPerTon(mDesign.getPowerPlantNumber());
    }

    public long getDriveManeuverCost()
    {
        return getDriveManeuverCostPerTon()*getDriveManeuverVolume();
    }

    public long getDriveJumpCost()
    {
        return getDriveJumpCostPerTon()*getDriveJumpVolume();
    }

    public long getDrivePowerPlantCost()
    {
        return getDrivePowerCostPerTon()*getDrivePowerPlantVolume();
    }

    public long getFuelScoopsCostPerTon()
    {
        return Ship5DesignLogic.fuelScoopsCostPerTon();
    }

    public long getFuelScoopsCost()
    {
        return getFuelScoopsCostPerTon()*mDesign.getHullTonnage();
    }

    public double getFuelPurificationPercentOfFuel()
    {
        return Ship5DesignLogic.fuelPurificationPercentOfFuel(mDesign.getTechLevel());
    }
    
    public int getFuelPurificationMinimumSize()
    {
        return Ship5DesignLogic.fuelPurificationMinimumSize(mDesign.getTechLevel());
    }
    
    public long getFuelPurificationCostPerTon()
    {
        return Ship5DesignLogic.fuelPurificationCostPerTon(mDesign.getTechLevel());
    }
    
    public int getFuelPurificationVolume()
    {
        int vol = (int)(getFuelPurificationPercentOfFuel()*mDesign.getFuelTankage());
        return Math.max(vol, getFuelPurificationMinimumSize());
    }
    
    public long getFuelPurificationCost()
    {
        return getFuelPurificationVolume()*getFuelPurificationCostPerTon();
    }
    
    public int getBridgeVolume()
    {
        return Ship5DesignLogic.bridgeVolume(mDesign.getHullTonnage());
    }
    
    public long getBridgeCost()
    {
        return Ship5DesignLogic.bridgeCost(mDesign.getHullTonnage());
    }
    
    public int getBridgeAuxVolume()
    {
        return mDesign.getBridgeAuxiliaryCount()*getBridgeVolume();
    }
    
    public long getBridgeAuxCost()
    {
        return mDesign.getBridgeAuxiliaryCount()*getBridgeCost();
    }
    
    public int getEnergyProduced()
    {
        return Ship5DesignLogic.energyProduced(mDesign.getHullTonnage(), mDesign.getPowerPlantNumber());
    }
    
    public int getAgility()
    {
        int excessEnergy = getEnergyProduced() - getTotalEnergyUsage();
        if (excessEnergy > 0)
            return excessEnergy/100;
        else
            return 0;
    }

    public long getComputerCost()
    {
        return Ship5DesignLogic.computerCost(mDesign.getComputerCode());
    }

    public int getComputerVolume()
    {
        return Ship5DesignLogic.computerVolume(mDesign.getComputerCode());
    }

    public char getComputerHull()
    {
        return Ship5DesignLogic.computerHull(mDesign.getComputerCode());
    }

    public int getComputerTechLevel()
    {
        return Ship5DesignLogic.computerTechLevel(mDesign.getComputerCode());
    }

    public int getComputerEnergy()
    {
        return Ship5DesignLogic.computerEnergy(mDesign.getComputerCode());
    }

    public int getArmorVolumePercent()
    {
        return Ship5DesignLogic.armorVolumePercent(mDesign.getArmorFactors(), mDesign.getTechLevel());
    }

    public long getArmorCostPerTon()
    {
        return Ship5DesignLogic.armorCostPerTon(mDesign.getArmorFactors());
    }

    public int getArmorVolume()
    {
        return getArmorVolumePercent()*mDesign.getHullTonnage()/100;
    }

    public long getArmorCost()
    {
        return getArmorVolume()*getArmorCostPerTon();
    }
    
    public int getArmorEffective()
    {
        if (mDesign.getHullConfigurationCode() == Ship5Design.CONFIG_BUFFERED_PLANETOID)
            return 6;
        if (mDesign.getHullConfigurationCode() == Ship5Design.CONFIG_PLANETOID)
            return 3 + mDesign.getArmorFactors();
        else
            return mDesign.getArmorFactors();
    }

    public int getMajorPAVolume()
    {
        return Ship5DesignLogic.majorPAVolume(mDesign.getMajorCode());
    }

    public int getMajorPATechLevel()
    {
        return Ship5DesignLogic.majorPATechLevel(mDesign.getMajorCode());
    }

    public long getMajorPACost()
    {
        return Ship5DesignLogic.majorPACost(mDesign.getMajorCode());
    }

    public int getMajorPAEnergy()
    {
        return Ship5DesignLogic.majorPAEnergy(mDesign.getMajorCode());
    }

    public int getMajorMesonVolume()
    {
        return Ship5DesignLogic.majorMesonVolume(mDesign.getMajorCode());
    }

    public int getMajorMesonTechLevel()
    {
        return Ship5DesignLogic.majorMesonTechLevel(mDesign.getMajorCode());
    }

    public long getMajorMesonCost()
    {
        return Ship5DesignLogic.majorMesonCost(mDesign.getMajorCode());
    }

    public int getMajorMesonEnergy()
    {
        return Ship5DesignLogic.majorMesonEnergy(mDesign.getMajorCode());
    }

    public int getMajorVolume()
    {
        if (mDesign.getMajorWeapon() == Ship5Design.MAJOR_PARTICLE)
            return getMajorPAVolume();
        else if (mDesign.getMajorWeapon() == Ship5Design.MAJOR_MESON)
            return getMajorMesonVolume();
        return 0;
    }

    public int getMajorTechLevel()
    {
        if (mDesign.getMajorWeapon() == Ship5Design.MAJOR_PARTICLE)
            return getMajorPATechLevel();
        else if (mDesign.getMajorWeapon() == Ship5Design.MAJOR_MESON)
            return getMajorMesonTechLevel();
        return 0;
    }

    public long getMajorCost()
    {
        if (mDesign.getMajorWeapon() == Ship5Design.MAJOR_PARTICLE)
            return getMajorPACost();
        else if (mDesign.getMajorWeapon() == Ship5Design.MAJOR_MESON)
            return getMajorMesonCost();
        return 0;
    }

    public int getMajorEnergy()
    {
        if (mDesign.getMajorWeapon() == Ship5Design.MAJOR_PARTICLE)
            return getMajorPAEnergy();
        else if (mDesign.getMajorWeapon() == Ship5Design.MAJOR_MESON)
            return getMajorMesonEnergy();
        return 0;
    }

    public int getBay100MesonEnergy()
    {
        return mDesign.getBays100Meson()*Ship5DesignLogic.bay100MesonEnergy();
    }

    public int getBay100ParticleEnergy()
    {
        return mDesign.getBays100Particle()*Ship5DesignLogic.bay100ParticleEnergy();
    }

    public int getBay100RepulsorEnergy()
    {
        return mDesign.getBays100Repulsor()*Ship5DesignLogic.bay100RepulsorEnergy();
    }

    public int getBay100MissileEnergy()
    {
        return mDesign.getBays100Missile()*Ship5DesignLogic.bay100MissileEnergy();
    }

    public int getBay50MesonEnergy()
    {
        return mDesign.getBays50Meson()*Ship5DesignLogic.bay50MesonEnergy();
    }

    public int getBay50ParticleEnergy()
    {
        return mDesign.getBays50Particle()*Ship5DesignLogic.bay50ParticleEnergy();
    }

    public int getBay50RepulsorEnergy()
    {
        return mDesign.getBays50Repulsor()*Ship5DesignLogic.bay50RepulsorEnergy();
    }

    public int getBay50MissileEnergy()
    {
        return mDesign.getBays50Missile()*Ship5DesignLogic.bay50MissileEnergy();
    }

    public int getBay50PlasmaEnergy()
    {
        return mDesign.getBays50Plasma()*Ship5DesignLogic.bay50PlasmaEnergy();
    }

    public int getBay50FusionEnergy()
    {
        return mDesign.getBays50Fusion()*Ship5DesignLogic.bay50FusionEnergy();
    }

    public long getBay100MesonCost()
    {
        return mDesign.getBays100Meson()*Ship5DesignLogic.bay100MesonCost();
    }

    public long getBay100ParticleCost()
    {
        return mDesign.getBays100Particle()*Ship5DesignLogic.bay100ParticleCost();
    }

    public long getBay100RepulsorCost()
    {
        return mDesign.getBays100Repulsor()*Ship5DesignLogic.bay100RepulsorCost();
    }

    public long getBay100MissileCost()
    {
        return mDesign.getBays100Missile()*Ship5DesignLogic.bay100MissileCost();
    }

    public long getBay50MesonCost()
    {
        return mDesign.getBays50Meson()*Ship5DesignLogic.bay50MesonCost();
    }

    public long getBay50ParticleCost()
    {
        return mDesign.getBays50Particle()*Ship5DesignLogic.bay50ParticleCost();
    }

    public long getBay50RepulsorCost()
    {
        return mDesign.getBays50Repulsor()*Ship5DesignLogic.bay50RepulsorCost();
    }

    public long getBay50MissileCost()
    {
        return mDesign.getBays50Missile()*Ship5DesignLogic.bay50MissileCost();
    }

    public long getBay50PlasmaCost()
    {
        return mDesign.getBays50Plasma()*Ship5DesignLogic.bay50PlasmaCost();
    }

    public long getBay50FusionCost()
    {
        return mDesign.getBays50Fusion()*Ship5DesignLogic.bay50FusionCost();
    }

    public long getTurretMissileCost()
    {
        return mDesign.getTurretMissile()*Ship5DesignLogic.turretMissileCost();
    }

    public long getTurretBeamLaserCost()
    {
        return mDesign.getTurretBeamLaser()*Ship5DesignLogic.turretBeamLaserCost();
    }

    public long getTurretPulseLaserCost()
    {
        return mDesign.getTurretPulseLaser()*Ship5DesignLogic.turretPulseLaserCost();
    }

    public long getTurretPlasmaGunCost()
    {
        return mDesign.getTurretPlasmaGun()*Ship5DesignLogic.turretPlasmaGunCost();
    }

    public long getTurretFusionGunCost()
    {
        return mDesign.getTurretFusionGun()*Ship5DesignLogic.turretFusionGunCost();
    }

    public long getTurretSandcasterCost()
    {
        return mDesign.getTurretSandcaster()*Ship5DesignLogic.turretSandcasterCost();
    }

    public long getTurretParticleCost()
    {
        return mDesign.getTurretParticle()*Ship5DesignLogic.turretParticleCost();
    }

    public long getBarbetteParticleCost()
    {
        return mDesign.getBarbetteParticle()*Ship5DesignLogic.barbetteParticleCost();
    }

    public int getTurretMissileEnergy()
    {
        return mDesign.getTurretMissile()*Ship5DesignLogic.turretMissileEnergy();
    }

    public int getTurretBeamLaserEnergy()
    {
        return mDesign.getTurretBeamLaser()*Ship5DesignLogic.turretBeamLaserEnergy();
    }

    public int getTurretPulseLaserEnergy()
    {
        return mDesign.getTurretPulseLaser()*Ship5DesignLogic.turretPulseLaserEnergy();
    }

    public int getTurretPlasmaGunEnergy()
    {
        return mDesign.getTurretPlasmaGun()*Ship5DesignLogic.turretPlasmaGunEnergy();
    }

    public int getTurretFusionGunEnergy()
    {
        return mDesign.getTurretFusionGun()*Ship5DesignLogic.turretFusionGunEnergy();
    }

    public int getTurretSandcasterEnergy()
    {
        return mDesign.getTurretSandcaster()*Ship5DesignLogic.turretSandcasterEnergy();
    }

    public int getTurretParticleEnergy()
    {
        return mDesign.getTurretParticle()*Ship5DesignLogic.turretParticleEnergy();
    }

    public int getBarbetteParticleEnergy()
    {
        return mDesign.getBarbetteParticle()*Ship5DesignLogic.barbetteParticleEnergy();
    }

    public int getScreenNuclearVolume()
    {
        return Ship5DesignLogic.screenNuclearVolume(mDesign.getScreenNuclearCode());
    }

    public int getScreenNuclearEnergy()
    {
        return Ship5DesignLogic.screenNuclearEnergy(mDesign.getScreenNuclearCode());
    }

    public int getScreenMesonVolume()
    {
        return Ship5DesignLogic.screenMesonVolume(mDesign.getScreenMesonCode());
    }

    public int getScreenMesonEnergy()
    {
        return Ship5DesignLogic.screenMesonEnergy(mDesign.getScreenMesonCode(), mDesign.getHullTonnage());
    }

    public int getScreenForceVolume()
    {
        return Ship5DesignLogic.screenForceVolume(mDesign.getScreenForceCode());
    }

    public long getScreenNuclearCost()
    {
        return Ship5DesignLogic.screenNuclearCost(mDesign.getScreenNuclearCode());
    }

    public long getScreenMesonCost()
    {
        return Ship5DesignLogic.screenMesonCost(mDesign.getScreenMesonCode());
    }

    public long getScreenForceCost()
    {
        return Ship5DesignLogic.screenForceCost(mDesign.getScreenForceCode());
    }
    
    // getters and setters

    public Ship5Design getDesign()
    {
        return mDesign;
    }

    public void setDesign(Ship5Design design)
    {
        queuePropertyChange("design", mDesign, design);
        mDesign = design;
        firePropertyChange();
    }

    public List<String> getErrors()
    {
        return mErrors;
    }

    public void setErrors(List<String> errors)
    {
        queuePropertyChange("errors", mErrors, errors);
        mErrors = errors;
        firePropertyChange();
    }

    public Map<String, Integer> getSpaceUsage()
    {
        return mSpaceUsage;
    }

    public void setSpaceUsage(Map<String, Integer> spaceUsage)
    {
        queuePropertyChange("spaceUsage", mSpaceUsage, spaceUsage);
        mSpaceUsage = spaceUsage;
        firePropertyChange();
    }

    public Map<String, Integer> getFuelUsage()
    {
        return mFuelUsage;
    }

    public void setFuelUsage(Map<String, Integer> fuelUsage)
    {
        queuePropertyChange("fuelUsage", mFuelUsage, fuelUsage);
        mFuelUsage = fuelUsage;
        firePropertyChange();
    }

    public Map<String, Long> getCostUsage()
    {
        return mCostUsage;
    }

    public void setCostUsage(Map<String, Long> costUsage)
    {
        queuePropertyChange("costUsage", mCostUsage, costUsage);
        mCostUsage = costUsage;
        firePropertyChange();
    }

    public Map<String, Integer> getEnergyUsage()
    {
        return mEnergyUsage;
    }

    public void setEnergyUsage(Map<String, Integer> energyUsage)
    {
        queuePropertyChange("Integer> energyUsage", mEnergyUsage, energyUsage);
        mEnergyUsage = energyUsage;
        firePropertyChange();
    }

    public int getCrewCommand()
    {
        return mCrewCommand;
    }

    public void setCrewCommand(int crewCommand)
    {
        queuePropertyChange("crewCommand", mCrewCommand, crewCommand);
        mCrewCommand = crewCommand;
        firePropertyChange();
    }

    public int getCrewExecutive()
    {
        return mCrewExecutive;
    }

    public void setCrewExecutive(int crewExecutive)
    {
        queuePropertyChange("crewExecutive", mCrewExecutive, crewExecutive);
        mCrewExecutive = crewExecutive;
        firePropertyChange();
    }

    public int getCrewComputer()
    {
        return mCrewComputer;
    }

    public void setCrewComputer(int crewComputer)
    {
        queuePropertyChange("crewComputer", mCrewComputer, crewComputer);
        mCrewComputer = crewComputer;
        firePropertyChange();
    }

    public int getCrewNavigation()
    {
        return mCrewNavigation;
    }

    public void setCrewNavigation(int crewNavigation)
    {
        queuePropertyChange("crewNavigation", mCrewNavigation, crewNavigation);
        mCrewNavigation = crewNavigation;
        firePropertyChange();
    }

    public int getCrewMedicalOfficer()
    {
        return mCrewMedicalOfficer;
    }

    public void setCrewMedicalOfficer(int crewMedicalOfficer)
    {
        queuePropertyChange("crewMedicalOfficer", mCrewMedicalOfficer, crewMedicalOfficer);
        mCrewMedicalOfficer = crewMedicalOfficer;
        firePropertyChange();
    }

    public int getCrewCommunications()
    {
        return mCrewCommunications;
    }

    public void setCrewCommunications(int crewCommunications)
    {
        queuePropertyChange("crewCommunications", mCrewCommunications, crewCommunications);
        mCrewCommunications = crewCommunications;
        firePropertyChange();
    }

    public int getCrewSupport()
    {
        return mCrewSupport;
    }

    public void setCrewSupport(int crewSupport)
    {
        queuePropertyChange("crewSupport", mCrewSupport, crewSupport);
        mCrewSupport = crewSupport;
        firePropertyChange();
    }

    public int getCrewChiefEngineer()
    {
        return mCrewChiefEngineer;
    }

    public void setCrewChiefEngineer(int crewChiefEngineer)
    {
        queuePropertyChange("crewChiefEngineer", mCrewChiefEngineer, crewChiefEngineer);
        mCrewChiefEngineer = crewChiefEngineer;
        firePropertyChange();
    }

    public int getCrewSecondEngineer()
    {
        return mCrewSecondEngineer;
    }

    public void setCrewSecondEngineer(int crewSecondEngineer)
    {
        queuePropertyChange("crewSecondEngineer", mCrewSecondEngineer, crewSecondEngineer);
        mCrewSecondEngineer = crewSecondEngineer;
        firePropertyChange();
    }

    public int getCrewEngineer()
    {
        return mCrewEngineer;
    }

    public void setCrewEngineer(int crewEngineer)
    {
        queuePropertyChange("crewEngineer", mCrewEngineer, crewEngineer);
        mCrewEngineer = crewEngineer;
        firePropertyChange();
    }

    public int getCrewEngineerOfficer()
    {
        return mCrewEngineerOfficer;
    }

    public void setCrewEngineerOfficer(int crewEngineerOfficer)
    {
        queuePropertyChange("crewEngineerOfficer", mCrewEngineerOfficer, crewEngineerOfficer);
        mCrewEngineerOfficer = crewEngineerOfficer;
        firePropertyChange();
    }

    public int getCrewEngineerPettyOfficer()
    {
        return mCrewEngineerPettyOfficer;
    }

    public void setCrewEngineerPettyOfficer(int crewEngineerPettyOfficer)
    {
        queuePropertyChange("crewEngineerPettyOfficer", mCrewEngineerPettyOfficer, crewEngineerPettyOfficer);
        mCrewEngineerPettyOfficer = crewEngineerPettyOfficer;
        firePropertyChange();
    }

    public int getCrewChiefGunner()
    {
        return mCrewChiefGunner;
    }

    public void setCrewChiefGunner(int crewChiefGunner)
    {
        queuePropertyChange("crewChiefGunner", mCrewChiefGunner, crewChiefGunner);
        mCrewChiefGunner = crewChiefGunner;
        firePropertyChange();
    }

    public int getCrewGunnerOfficer()
    {
        return mCrewGunnerOfficer;
    }

    public void setCrewGunnerOfficer(int crewGunnerOfficer)
    {
        queuePropertyChange("crewGunnerOfficer", mCrewGunnerOfficer, crewGunnerOfficer);
        mCrewGunnerOfficer = crewGunnerOfficer;
        firePropertyChange();
    }

    public int getCrewGunnerPettyOfficer()
    {
        return mCrewGunnerPettyOfficer;
    }

    public void setCrewGunnerPettyOfficer(int crewGunnerPettyOfficer)
    {
        queuePropertyChange("crewGunnerPettyOfficer", mCrewGunnerPettyOfficer, crewGunnerPettyOfficer);
        mCrewGunnerPettyOfficer = crewGunnerPettyOfficer;
        firePropertyChange();
    }

    public int getCrewGunner()
    {
        return mCrewGunner;
    }

    public void setCrewGunner(int crewGunner)
    {
        queuePropertyChange("crewGunner", mCrewGunner, crewGunner);
        mCrewGunner = crewGunner;
        firePropertyChange();
    }

    public int getCrewFlightControlOfficer()
    {
        return mCrewFlightControlOfficer;
    }

    public void setCrewFlightControlOfficer(int crewFlightControlOfficer)
    {
        queuePropertyChange("crewFlightControlOfficer", mCrewFlightControlOfficer, crewFlightControlOfficer);
        mCrewFlightControlOfficer = crewFlightControlOfficer;
        firePropertyChange();
    }

    public int getCrewFlightMaintenence()
    {
        return mCrewFlightMaintenence;
    }

    public void setCrewFlightMaintenence(int crewFlightMaintenence)
    {
        queuePropertyChange("crewFlightMaintenence", mCrewFlightMaintenence, crewFlightMaintenence);
        mCrewFlightMaintenence = crewFlightMaintenence;
        firePropertyChange();
    }

    public int getCrewFlightCrew()
    {
        return mCrewFlightCrew;
    }

    public void setCrewFlightCrew(int crewFlightCrew)
    {
        queuePropertyChange("crewFlightCrew", mCrewFlightCrew, crewFlightCrew);
        mCrewFlightCrew = crewFlightCrew;
        firePropertyChange();
    }

    public int getCrewFlightDriver()
    {
        return mCrewFlightDriver;
    }

    public void setCrewFlightDriver(int crewFlightDriver)
    {
        queuePropertyChange("crewFlightDriver", mCrewFlightDriver, crewFlightDriver);
        mCrewFlightDriver = crewFlightDriver;
        firePropertyChange();
    }

    public int getCrewService()
    {
        return mCrewService;
    }

    public void setCrewService(int crewService)
    {
        queuePropertyChange("crewService", mCrewService, crewService);
        mCrewService = crewService;
        firePropertyChange();
    }

    public int getCrewFozenWatch()
    {
        return mCrewFozenWatch;
    }

    public void setCrewFozenWatch(int crewFozenWatch)
    {
        queuePropertyChange("crewFozenWatch", mCrewFozenWatch, crewFozenWatch);
        mCrewFozenWatch = crewFozenWatch;
        firePropertyChange();
    }

    public int getCrewCommandCount()
    {
        return getCrewCommand() + getCrewExecutive() + getCrewComputer() + getCrewNavigation() 
            + getCrewMedicalOfficer() + getCrewCommunications() + getCrewSupport();
    }

    public int getCrewEngineeringCount()
    {
        return getCrewChiefEngineer() + getCrewSecondEngineer() + getCrewEngineer() + getCrewEngineerOfficer() + getCrewEngineerPettyOfficer();
    }

    public int getCrewGunneryCount()
    {
        return getCrewChiefGunner() + getCrewGunnerOfficer() + getCrewGunnerPettyOfficer() + getCrewGunner();
    }

    public int getCrewFlightCount()
    {
        return getCrewFlightControlOfficer() + getCrewFlightMaintenence() + getCrewFlightCrew() + getCrewFlightDriver();
    }

    public int getCrewMedical()
    {
        return mCrewMedical;
    }

    public void setCrewMedical(int crewMedical)
    {
        queuePropertyChange("crewMedical", mCrewMedical, crewMedical);
        mCrewMedical = crewMedical;
        firePropertyChange();
    }

    public int getCrewPassengers()
    {
        return mCrewPassengers;
    }

    public void setCrewPassengers(int crewPassengers)
    {
        queuePropertyChange("crewPassengers", mCrewPassengers, crewPassengers);
        mCrewPassengers = crewPassengers;
        firePropertyChange();
    }
}

