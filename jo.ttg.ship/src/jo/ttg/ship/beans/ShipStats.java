package jo.ttg.ship.beans;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.utils.DisplayUtils;

public class ShipStats
{
	private List<ShipStatsError>	mErrors;
	private List<ShipStatsWeapon>	mWeapons;
	private List<ShipStatsWeapon>  mDefense;
	private String 		mCraftName;
	private String 		mCraftType;
	private int			mTechLevel;
	private String 		mCost;
	private double		mRawCost;
	private int 		mHullInoperative;
	private int 		mHullDestroyed;
	private double 		mDisplacement;
	private String 		mConfig;
	private int			mArmorClass;
	private String 		mArmor;
	private String 		mLoadedWeight;
	private String 		mUnloadedWeight;
	private int 		mPowerInoperative;
	private int 		mPowerDestroyed;
	private String 		mPower;
	private double		mPowerProduced;
	private double		mPowerConsumed;
	private int 		mDurationDays;
	private int 		mDurationShifts;
	private int 		mDurationHours;
	private int 		mManeuverInoperative;
	private int 		mManeuverDestroyed;
	private double		mManeuver;
	private int 		mJumpInoperative;
	private int 		mJumpDestroyed;
	private int			mJump;
	private String 		mNOESpeed;
	private String 		mCruiseSpeed;
	private String 		mTopSpeed;
	private String 		mVaccuumSpeed;
	private int			mAgility;
	private int			mEmergencyAgility;
	private String 		mRadio;
	private String 		mPassiveEMS;
	private String 		mActiveEMS;
	private String 		mEMSJammer;
	private String		mDensitometer;
	private String		mNeutrino;
	private String		mActiveObjectScan;
	private String		mActiveObjectPinpoint;
	private String		mPassiveObjectScan;
	private String		mPassiveObjectPinpoint;
	private String		mPassiveEnergyScan;
	private String		mPassiveEnergyPinpoint;
	private int			mCrewBridge;
	private int			mCrewEngineering;
	private int			mCrewMaintenence;
	private int			mCrewGunners;
	private int			mCrewCommand;
	private int			mCrewSteward;
	private int			mCrewMedical;
	private int			mCrew;
	private int			mPassengers;
	private int			mAirlocks;
	private int			mStaterooms;
    private int         mSeats;
	private int			mLowBerths;
	private String		mComputer;
	private int			mComputerModel;
	private String		mPanels;
	private String		mSpecial;
	private String		mSubordinateCraft;
	private double      mHangerVolume;
	private int			mFuel;
	private double		mCargo;
	private int			mDefDM;
	private boolean		mFuelScoops;
	private int			mHardpoints;
	private int			mUsedHardpoints;
	private int			mPurificationTime;
	private int			mBatteryRounds;
	private double		mControlProvided;
	private double		mControlNeeded;
	private int			mNuclearDamperFactor;
	private int			mMesonFactor;
	private int			mBlackGlobeFactor;
	private int			mProtonFactor;
	private int			mWhiteGlobeFactor;
	
	private ShipTotals  mTotals;
	
	public ShipStats()
	{
		mErrors = new ArrayList<ShipStatsError>();
		mWeapons = new ArrayList<ShipStatsWeapon>();
		mDefense = new ArrayList<ShipStatsWeapon>();
	}
	
	public String sCraftID()
	{
		StringBuffer ret = new StringBuffer();
		ret.append(mCraftName);
		if (mCraftType != null && mCraftType.length() > 0)
		{
			ret.append(", Type ");
			ret.append(mCraftType);
		}
		ret.append(", TL ");
		ret.append(mTechLevel);
		ret.append(", ");
		ret.append(mCost);
		return ret.toString();
	}
	
	public String sHull()
	{
		StringBuffer ret = new StringBuffer();
		ret.append(mHullInoperative);
		ret.append("/");
		ret.append(mHullDestroyed);
		ret.append(", Disp=");
		ret.append(DisplayUtils.formatVolume(mDisplacement));
		ret.append(", Config=");
		ret.append(mConfig);
		ret.append(", Armor=");
		ret.append(mArmor);
		ret.append(", Loaded=");
		ret.append(mLoadedWeight);
		ret.append(", Unloaded=");
		ret.append(mUnloadedWeight);
		return ret.toString();
	}
	
	public String sPower()
	{
		StringBuffer ret = new StringBuffer();
		ret.append(mPowerInoperative);
		ret.append("/");
		ret.append(mPowerDestroyed);
		ret.append(", Fusion=");
		ret.append(mPower);
		ret.append(", Duration=");
		if (mDurationDays <= 1)
		{
			ret.append(mDurationHours);
			ret.append(" hours");
		}
		else
		{
			ret.append(mDurationDays);
			ret.append("/");
			ret.append(mDurationShifts);
		}
		return ret.toString();
	}
	
	public String sLoco()
	{
		StringBuffer ret = new StringBuffer();
		if (mJump > 0)
		{
			ret.append(mJumpInoperative);
			ret.append("/");
			ret.append(mJumpDestroyed);
			ret.append(", Jump=");
			ret.append(mJump);
			ret.append(", ");
		}
		ret.append(mManeuverInoperative);
		ret.append("/");
		ret.append(mManeuverDestroyed);
		ret.append(", Maneuver=");
		ret.append(mManeuver);
		ret.append(", NOE=");
		ret.append(mNOESpeed);
		ret.append(", Cruise=");
		ret.append(mCruiseSpeed);
		ret.append(", Top=");
		ret.append(mTopSpeed);
		ret.append(", Vaccuum=");
		ret.append(mVaccuumSpeed);
		ret.append(", Agility=");
		ret.append(mAgility);
		if (mEmergencyAgility != mAgility)
		{
			ret.append(", EmergencyAgility=");
			ret.append(mEmergencyAgility);
		}
		return ret.toString();
	}
	
	public String sCommo()
	{
		StringBuffer ret = new StringBuffer();
		ret.append("Radio=");
		ret.append(mRadio);
		return ret.toString();
	}
	
	public String sSensors()
	{
		StringBuffer ret = new StringBuffer();
		if (mPassiveEMS.length() > 0)
		{
			ret.append(", PasEMS=");
			ret.append(mPassiveEMS);
		}
		if (mActiveEMS.length() > 0)
		{
			ret.append(", ActEMS=");
			ret.append(mActiveEMS);
		}
		if (mEMSJammer.length() > 0)
		{
			ret.append(", EMSJam=");
			ret.append(mEMSJammer);
		}
		if (mDensitometer.length() > 0)
		{
			ret.append(", Densitometer=");
			ret.append(mDensitometer);
		}
		if (mNeutrino.length() > 0)
		{
			ret.append(", Neutrino=");
			ret.append(mNeutrino);
		}
		if (mActiveObjectScan.length() > 0)
		{
			ret.append(", ActObjScan=");
			ret.append(mActiveObjectScan);
		}
		if (mActiveObjectPinpoint.length() > 0)
		{
			ret.append(", ActObjPin=");
			ret.append(mActiveObjectPinpoint);
		}
		if (mPassiveObjectScan.length() > 0)
		{
			ret.append(", PasObjScan=");
			ret.append(mPassiveObjectScan);
		}
		if (mPassiveObjectPinpoint.length() > 0)
		{
			ret.append(", PasObjPin=");
			ret.append(mPassiveObjectPinpoint);
		}
		if (mPassiveEnergyScan.length() > 0)
		{
			ret.append(", PasEngScan=");
			ret.append(mPassiveEnergyScan);
		}
		if (mPassiveEnergyPinpoint.length() > 0)
		{
			ret.append(", PasEngPin=");
			ret.append(mPassiveEnergyPinpoint);
		}
		String sret = ret.toString();
		if (sret.startsWith(", "))
			sret = sret.substring(2);
		return sret;
	}

	/**
	 * @return
	 */
	public String sOff()
	{
		StringBuffer ret = new StringBuffer();
		if (getHardpoints() > 0)
		{
			ret.append("Hardpoints=");
			ret.append(getHardpoints());
		}
		sWeapons(ret, mWeapons);
		return ret.toString().trim();
	}

	/**
	 * @return
	 */
	public String sDef()
	{
		StringBuffer ret = new StringBuffer();
		ret.append("DefDM=");
		if (getDefDM() > 0)
			ret.append("+");
		ret.append(getDefDM());
		if (getNuclearDamperFactor() > 0)
		{
			ret.append(", NucDamp=");
			ret.append(getNuclearDamperFactor());
		}
		if (getMesonFactor() > 0)
		{
			ret.append(", Meson=");
			ret.append(getMesonFactor());
		}
		if (getBlackGlobeFactor() > 0)
		{
			ret.append(", BlkGlb=");
			ret.append(getBlackGlobeFactor());
		}
		if (getProtonFactor() > 0)
		{
			ret.append(", Proton=");
			ret.append(getProtonFactor());
		}
		if (getWhiteGlobeFactor() > 0)
		{
			ret.append(", WhtGlb=");
			ret.append(getWhiteGlobeFactor());
		}
        sWeapons(ret, mDefense);
		return ret.toString().trim();
	}

    private void sWeapons(StringBuffer ret, List<ShipStatsWeapon> stuff)
    {
        if (stuff.size() > 0)
        {
        	if (ret.length() > 0)
        		ret.append("\n");
        	StringBuffer line1 = new StringBuffer();
        	StringBuffer line2 = new StringBuffer();
        	StringBuffer line3 = new StringBuffer();
        	for (ShipStatsWeapon ssw :stuff)
        	{
        		line1.append(ssw.sLine1());
        		line2.append(ssw.sLine2());
        		line3.append(ssw.sLine3());
        		if (line1.length() > 60)
        		{
        			ret.append(line1.toString());
        			ret.append("\n");
        			line1.setLength(0);
        			ret.append(line2.toString());
        			ret.append("\n");
        			line2.setLength(0);
        			ret.append(line3.toString());
        			ret.append("\n");
        			line3.setLength(0);
        		}
        		line1.append("  ");
        		line2.append("  ");
        		line3.append("  ");
        	}
        	if (line1.length() > 0)
        	{
        		ret.append(line1.toString());
        		ret.append("\n");
        		ret.append(line2.toString());
        		ret.append("\n");
        		ret.append(line3.toString());
        		ret.append("\n");
        	}
        }
    }

	/**
	 * @return
	 */
	public String sControl()
	{
		StringBuffer ret = new StringBuffer();
		if (getComputer().length() > 0)
		{
			ret.append("Computer=");
			ret.append(getComputer());
			ret.append(", ");
		}
		if (getPanels().length() > 0)
		{
			ret.append("Panels=");
			ret.append(getPanels());
		}
		if (getSpecial().length() > 0)
		{
			ret.append("Special=");
			ret.append(getSpecial());
		}
		ret.append("Environ=basic env, basic ls, extend ls");
		if (getTechLevel() >= 10)
			ret.append(", grav plates, inertial comp");
		return ret.toString();
	}


	/**
	 * @return
	 */
	public String sAccomm()
	{
		StringBuffer ret = new StringBuffer();
		ret.append("Crew=");
		ret.append(getCrew());
		ret.append(", (");
		if (getCrewEngineering() > 0)
		{
			ret.append("Engineering=");
			ret.append(getCrewEngineering());
			ret.append(", ");
		}
		if (getCrewMaintenence() > 0)
		{
			ret.append("Maintenence=");
			ret.append(getCrewMaintenence());
			ret.append(", ");
		}
		if (getCrewBridge() > 0)
		{
			ret.append("Bridge=");
			ret.append(getCrewBridge());
			ret.append(", ");
		}
		if (getCrewGunners() > 0)
		{
			ret.append("Gunners=");
			ret.append(getCrewGunners());
			ret.append(", ");
		}
		if (getCrewCommand() > 0)
		{
			ret.append("Command=");
			ret.append(getCrewCommand());
			ret.append(", ");
		}
		if (getCrewSteward() > 0)
		{
			ret.append("Steward=");
			ret.append(getCrewSteward());
			ret.append(", ");
		}
		if (getCrewMedical() > 0)
		{
			ret.append("Medical=");
			ret.append(getCrewMedical());
			ret.append(", ");
		}
		ret.setLength(ret.length() - 2);
		ret.append("), ");
		if (getAirlocks() > 0)
		{
			ret.append("Airlocks=");
			ret.append(getAirlocks());
			ret.append(", ");
		}
		if (getStaterooms() > 0)
		{
			ret.append("Staterooms=");
			ret.append(getStaterooms());
			ret.append(", ");
		}
		if (getLowBerths() > 0)
		{
			ret.append("Berths=");
			ret.append(getLowBerths());
			ret.append(", ");
		}
		if (getPassengers() > 0)
		{
			ret.append("HighPsg=");
			ret.append(getPassengers());
			ret.append(", ");
		}
		if (getSubordinateCraft().length() > 0)
		{
			ret.append("SubCraft=");
			ret.append(getSubordinateCraft());
		}
		ret.setLength(ret.length() - 2);
		return ret.toString();
	}

	/**
	 * @return
	 */
	public String sOther()
	{
		StringBuffer ret = new StringBuffer();
		if (getCargo() > 0)
		{
			ret.append("Cargo=");
			ret.append(DisplayUtils.formatVolume(getCargo()));
			ret.append(", ");
		}
        if (getHangerVolume() > 0)
        {
            ret.append("Hanger=");
            ret.append(DisplayUtils.formatVolume(getHangerVolume()));
            ret.append(", ");
        }
		if (getFuel() > 0)
		{
			ret.append("Fuel=");
			ret.append(DisplayUtils.formatVolume(getFuel()));
			ret.append(", ");
		}
		if (getPurificationTime() > 0)
		{
			ret.append("Fuel Purifier (");
			ret.append(getPurificationTime());
			ret.append(" hours), ");
		}
		if (isFuelScoops())
		{
			ret.append("Fuel Scoops, ");
		}
		if (getBatteryRounds() > 0)
		{
			ret.append("Missile Magazine=");
			ret.append(getBatteryRounds());
			ret.append(" batt rnds, ");
		}
		if (ret.length() > 0)
			ret.setLength(ret.length() - 2);
		return ret.toString();
	}
	
	public void addError(int id, String desc)
	{
		mErrors.add(new ShipStatsError(id, desc));
	}
	public void addError(int id, String desc, double v1)
	{
		mErrors.add(new ShipStatsError(id, desc, v1));
	}
	public void addError(int id, String desc, double v1, double v2)
	{
		mErrors.add(new ShipStatsError(id, desc, v1, v2));
	}
    public String getCost()
    {
        return mCost;
    }

    public String getCraftName()
    {
        return mCraftName;
    }

    public String getCraftType()
    {
        return mCraftType;
    }

    public List<ShipStatsError> getErrors()
    {
        return mErrors;
    }

    public int getHullDestroyed()
    {
        return mHullDestroyed;
    }

    public int getHullInoperative()
    {
        return mHullInoperative;
    }

    public int getTechLevel()
    {
        return mTechLevel;
    }

    public void setCost(String string)
    {
        mCost = string;
    }

    public void setCraftName(String string)
    {
        mCraftName = string;
    }

    public void setCraftType(String string)
    {
        mCraftType = string;
    }

    public void setErrors(List<ShipStatsError> list)
    {
        mErrors = list;
    }

    public void setHullDestroyed(int string)
    {
        mHullDestroyed = string;
    }

    public void setHullInoperative(int string)
    {
        mHullInoperative = string;
    }

    public void setTechLevel(int string)
    {
        mTechLevel = string;
    }

    public int getAgility()
    {
        return mAgility;
    }

    public String getCruiseSpeed()
    {
        return mCruiseSpeed;
    }

    public int getDurationDays()
    {
        return mDurationDays;
    }

    public int getDurationShifts()
    {
        return mDurationShifts;
    }

    public int getJump()
    {
        return mJump;
    }

    public int getJumpDestroyed()
    {
        return mJumpDestroyed;
    }

    public int getJumpInoperative()
    {
        return mJumpInoperative;
    }

    public String getLoadedWeight()
    {
        return mLoadedWeight;
    }

    public double getManeuver()
    {
        return mManeuver;
    }

    public int getManeuverDestroyed()
    {
        return mManeuverDestroyed;
    }

    public int getManeuverInoperative()
    {
        return mManeuverInoperative;
    }

    public String getNOESpeed()
    {
        return mNOESpeed;
    }

    public String getPower()
    {
        return mPower;
    }

    public int getPowerDestroyed()
    {
        return mPowerDestroyed;
    }

    public int getPowerInoperative()
    {
        return mPowerInoperative;
    }

    public String getRadio()
    {
        return mRadio;
    }

    public String getTopSpeed()
    {
        return mTopSpeed;
    }

    public String getUnloadedWeight()
    {
        return mUnloadedWeight;
    }

    public void setAgility(int string)
    {
        mAgility = string;
    }

    public void setCruiseSpeed(String string)
    {
        mCruiseSpeed = string;
    }

    public void setDurationDays(int string)
    {
        mDurationDays = string;
    }

    public void setDurationShifts(int string)
    {
        mDurationShifts = string;
    }

    public void setJump(int string)
    {
        mJump = string;
    }

    public void setJumpDestroyed(int string)
    {
        mJumpDestroyed = string;
    }

    public void setJumpInoperative(int string)
    {
        mJumpInoperative = string;
    }

    public void setLoadedWeight(String string)
    {
        mLoadedWeight = string;
    }

    public void setManeuver(double string)
    {
        mManeuver = string;
    }

    public void setManeuverDestroyed(int string)
    {
        mManeuverDestroyed = string;
    }

    public void setManeuverInoperative(int string)
    {
        mManeuverInoperative = string;
    }

    public void setNOESpeed(String string)
    {
        mNOESpeed = string;
    }

    public void setPower(String string)
    {
        mPower = string;
    }

    public void setPowerDestroyed(int string)
    {
        mPowerDestroyed = string;
    }

    public void setPowerInoperative(int string)
    {
        mPowerInoperative = string;
    }

    public void setRadio(String string)
    {
        mRadio = string;
    }

    public void setTopSpeed(String string)
    {
        mTopSpeed = string;
    }

    public void setUnloadedWeight(String string)
    {
        mUnloadedWeight = string;
    }

    public String getArmor()
    {
        return mArmor;
    }

    public String getConfig()
    {
        return mConfig;
    }

    public double getDisplacement()
    {
        return mDisplacement;
    }

    public void setArmor(String string)
    {
        mArmor = string;
    }

    public void setConfig(String string)
    {
        mConfig = string;
    }

    public void setDisplacement(double string)
    {
        mDisplacement = string;
    }
	/**
	 * @return
	 */
	public String getActiveEMS()
	{
		return mActiveEMS;
	}

	/**
	 * @return
	 */
	public String getActiveObjectPinpoint()
	{
		return mActiveObjectPinpoint;
	}

	/**
	 * @return
	 */
	public String getActiveObjectScan()
	{
		return mActiveObjectScan;
	}

	/**
	 * @return
	 */
	public String getDensitometer()
	{
		return mDensitometer;
	}

	/**
	 * @return
	 */
	public String getEMSJammer()
	{
		return mEMSJammer;
	}

	/**
	 * @return
	 */
	public String getNeutrino()
	{
		return mNeutrino;
	}

	/**
	 * @return
	 */
	public String getPassiveEMS()
	{
		return mPassiveEMS;
	}

	/**
	 * @return
	 */
	public String getPassiveEnergyPinpoint()
	{
		return mPassiveEnergyPinpoint;
	}

	/**
	 * @return
	 */
	public String getPassiveEnergyScan()
	{
		return mPassiveEnergyScan;
	}

	/**
	 * @return
	 */
	public String getPassiveObjectPinpoint()
	{
		return mPassiveObjectPinpoint;
	}

	/**
	 * @return
	 */
	public String getPassiveObjectScan()
	{
		return mPassiveObjectScan;
	}

	/**
	 * @return
	 */
	public String getVaccuumSpeed()
	{
		return mVaccuumSpeed;
	}

	/**
	 * @param string
	 */
	public void setActiveEMS(String string)
	{
		mActiveEMS = string;
	}

	/**
	 * @param string
	 */
	public void setActiveObjectPinpoint(String string)
	{
		mActiveObjectPinpoint = string;
	}

	/**
	 * @param string
	 */
	public void setActiveObjectScan(String string)
	{
		mActiveObjectScan = string;
	}

	/**
	 * @param string
	 */
	public void setDensitometer(String string)
	{
		mDensitometer = string;
	}

	/**
	 * @param string
	 */
	public void setEMSJammer(String string)
	{
		mEMSJammer = string;
	}

	/**
	 * @param string
	 */
	public void setNeutrino(String string)
	{
		mNeutrino = string;
	}

	/**
	 * @param string
	 */
	public void setPassiveEMS(String string)
	{
		mPassiveEMS = string;
	}

	/**
	 * @param string
	 */
	public void setPassiveEnergyPinpoint(String string)
	{
		mPassiveEnergyPinpoint = string;
	}

	/**
	 * @param string
	 */
	public void setPassiveEnergyScan(String string)
	{
		mPassiveEnergyScan = string;
	}

	/**
	 * @param string
	 */
	public void setPassiveObjectPinpoint(String string)
	{
		mPassiveObjectPinpoint = string;
	}

	/**
	 * @param string
	 */
	public void setPassiveObjectScan(String string)
	{
		mPassiveObjectScan = string;
	}

	/**
	 * @param string
	 */
	public void setVaccuumSpeed(String string)
	{
		mVaccuumSpeed = string;
	}

	/**
	 * @return
	 */
	public int getCrew()
	{
		return mCrew;
	}

	/**
	 * @return
	 */
	public int getCrewBridge()
	{
		return mCrewBridge;
	}

	/**
	 * @return
	 */
	public int getCrewCommand()
	{
		return mCrewCommand;
	}

	/**
	 * @return
	 */
	public int getCrewEngineering()
	{
		return mCrewEngineering;
	}

	/**
	 * @return
	 */
	public int getCrewGunners()
	{
		return mCrewGunners;
	}

	/**
	 * @return
	 */
	public int getCrewMaintenence()
	{
		return mCrewMaintenence;
	}

	/**
	 * @return
	 */
	public int getCrewMedical()
	{
		return mCrewMedical;
	}

	/**
	 * @return
	 */
	public int getCrewSteward()
	{
		return mCrewSteward;
	}

	/**
	 * @param i
	 */
	public void setCrew(int i)
	{
		mCrew = i;
	}

	/**
	 * @param i
	 */
	public void setCrewBridge(int i)
	{
		mCrewBridge = i;
	}

	/**
	 * @param i
	 */
	public void setCrewCommand(int i)
	{
		mCrewCommand = i;
	}

	/**
	 * @param i
	 */
	public void setCrewEngineering(int i)
	{
		mCrewEngineering = i;
	}

	/**
	 * @param i
	 */
	public void setCrewGunners(int i)
	{
		mCrewGunners = i;
	}

	/**
	 * @param i
	 */
	public void setCrewMaintenence(int i)
	{
		mCrewMaintenence = i;
	}

	/**
	 * @param i
	 */
	public void setCrewMedical(int i)
	{
		mCrewMedical = i;
	}

	/**
	 * @param i
	 */
	public void setCrewSteward(int i)
	{
		mCrewSteward = i;
	}

	/**
	 * @return
	 */
	public int getAirlocks()
	{
		return mAirlocks;
	}

	/**
	 * @return
	 */
	public int getLowBerths()
	{
		return mLowBerths;
	}

	/**
	 * @return
	 */
	public int getStaterooms()
	{
		return mStaterooms;
	}

	/**
	 * @param i
	 */
	public void setAirlocks(int i)
	{
		mAirlocks = i;
	}

	/**
	 * @param i
	 */
	public void setLowBerths(int i)
	{
		mLowBerths = i;
	}

	/**
	 * @param i
	 */
	public void setStaterooms(int i)
	{
		mStaterooms = i;
	}

    public int getPassengers()
    {
        return mPassengers;
    }

    public void setPassengers(int i)
    {
        mPassengers = i;
    }

    public String getComputer()
    {
        return mComputer;
    }

    public void setComputer(String string)
    {
        mComputer = string;
    }

    public String getPanels()
    {
        return mPanels;
    }

    public String getSpecial()
    {
        return mSpecial;
    }

    public void setPanels(String string)
    {
        mPanels = string;
    }

    public void setSpecial(String string)
    {
        mSpecial = string;
    }

    public String getSubordinateCraft()
    {
        return mSubordinateCraft;
    }

    public void setSubordinateCraft(String string)
    {
        mSubordinateCraft = string;
    }

    public double getCargo()
    {
        return mCargo;
    }

    public int getFuel()
    {
        return mFuel;
    }

    public void setCargo(double i)
    {
        mCargo = i;
    }

    public void setFuel(int i)
    {
        mFuel = i;
    }

	/**
	 * @return
	 */
	public int getEmergencyAgility()
	{
		return mEmergencyAgility;
	}

	/**
	 * @param i
	 */
	public void setEmergencyAgility(int i)
	{
		mEmergencyAgility = i;
	}

	/**
	 * @return
	 */
	public int getDefDM()
	{
		return mDefDM;
	}

	/**
	 * @param i
	 */
	public void setDefDM(int i)
	{
		mDefDM = i;
	}

	/**
	 * @return
	 */
	public boolean isFuelScoops()
	{
		return mFuelScoops;
	}

	/**
	 * @param b
	 */
	public void setFuelScoops(boolean b)
	{
		mFuelScoops = b;
	}

    public int getHardpoints()
    {
        return mHardpoints;
    }

    public void setHardpoints(int i)
    {
        mHardpoints = i;
    }

    public List<ShipStatsWeapon> getDefense()
    {
        return mDefense;
    }

    public List<ShipStatsWeapon> getWeapons()
    {
        return mWeapons;
    }

    public int getPurificationTime()
    {
        return mPurificationTime;
    }

    public void setPurificationTime(int i)
    {
        mPurificationTime = i;
    }

    public int getDurationHours()
    {
        return mDurationHours;
    }

    public void setDurationHours(int i)
    {
        mDurationHours = i;
    }

    public int getBatteryRounds()
    {
        return mBatteryRounds;
    }

    public void setBatteryRounds(int i)
    {
        mBatteryRounds = i;
    }

	/**
	 * @return
	 */
	public double getPowerConsumed()
	{
		return mPowerConsumed;
	}

	/**
	 * @return
	 */
	public double getPowerProduced()
	{
		return mPowerProduced;
	}

	/**
	 * @param d
	 */
	public void setPowerConsumed(double d)
	{
		mPowerConsumed = d;
	}

	/**
	 * @param d
	 */
	public void setPowerProduced(double d)
	{
		mPowerProduced = d;
	}

	/**
	 * @return
	 */
	public double getControlNeeded()
	{
		return mControlNeeded;
	}

	/**
	 * @return
	 */
	public double getControlProvided()
	{
		return mControlProvided;
	}

	/**
	 * @param d
	 */
	public void setControlNeeded(double d)
	{
		mControlNeeded = d;
	}

	/**
	 * @param d
	 */
	public void setControlProvided(double d)
	{
		mControlProvided = d;
	}

	/**
	 * @return
	 */
	public int getUsedHardpoints()
	{
		return mUsedHardpoints;
	}

	/**
	 * @param i
	 */
	public void setUsedHardpoints(int i)
	{
		mUsedHardpoints = i;
	}

	/**
	 * @return
	 */
	public int getBlackGlobeFactor()
	{
		return mBlackGlobeFactor;
	}

	/**
	 * @return
	 */
	public int getMesonFactor()
	{
		return mMesonFactor;
	}

	/**
	 * @return
	 */
	public int getNuclearDamperFactor()
	{
		return mNuclearDamperFactor;
	}

	/**
	 * @return
	 */
	public int getProtonFactor()
	{
		return mProtonFactor;
	}

	/**
	 * @return
	 */
	public int getWhiteGlobeFactor()
	{
		return mWhiteGlobeFactor;
	}

	/**
	 * @param i
	 */
	public void setBlackGlobeFactor(int i)
	{
		mBlackGlobeFactor = i;
	}

	/**
	 * @param i
	 */
	public void setMesonFactor(int i)
	{
		mMesonFactor = i;
	}

	/**
	 * @param i
	 */
	public void setNuclearDamperFactor(int i)
	{
		mNuclearDamperFactor = i;
	}

	/**
	 * @param i
	 */
	public void setProtonFactor(int i)
	{
		mProtonFactor = i;
	}

	/**
	 * @param i
	 */
	public void setWhiteGlobeFactor(int i)
	{
		mWhiteGlobeFactor = i;
	}

    public double getRawCost()
    {
        return mRawCost;
    }
    public void setRawCost(double rawCost)
    {
        mRawCost = rawCost;
    }
    public int getComputerModel()
    {
        return mComputerModel;
    }
    public void setComputerModel(int computerModel)
    {
        mComputerModel = computerModel;
    }
    /**
     * @return Returns the armorClass.
     */
    public int getArmorClass()
    {
        return mArmorClass;
    }
    /**
     * @param armorClass The armorClass to set.
     */
    public void setArmorClass(int armorClass)
    {
        mArmorClass = armorClass;
    }

    public int getSeats()
    {
        return mSeats;
    }

    public void setSeats(int seats)
    {
        mSeats = seats;
    }

    public double getHangerVolume()
    {
        return mHangerVolume;
    }

    public void setHangerVolume(double hangerVolume)
    {
        mHangerVolume = hangerVolume;
    }

    public ShipTotals getTotals()
    {
        return mTotals;
    }

    public void setTotals(ShipTotals totals)
    {
        mTotals = totals;
    }
}
