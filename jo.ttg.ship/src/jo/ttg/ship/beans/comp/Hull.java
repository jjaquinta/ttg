package jo.ttg.ship.beans.comp;

import jo.ttg.utils.DisplayUtils;
import jo.util.utils.MathUtils;

public class Hull extends ShipComponent implements TechLevelComponent, VolumeComponent
{
	public static final int HULL_OPEN_FRAME = 0;
	public static final int HULL_NEEDLE = 1;
	public static final int HULL_CONE = 2;
	public static final int HULL_CYLINDER = 3;
	public static final int HULL_BOX = 4;
	public static final int HULL_SPHERE = 5;
	public static final int HULL_DOME = 6;
	public static final int HULL_IRREGULAR = 7;
	public static final int HULL_PLANETOID = 8;
	public static final int HULL_BUFFERED_PLANETOID = 9;
	
	public static final String[] hullDescription = 
	{
		"Open Frame",
		"Needle",
		"Cone",
		"Cylinder",
		"Box",
		"Sphere",
		"Dome",
		"Irregular",
		"Planetoid",
		"Buffered Planetoid",
	};

	public static final int FRAME_UNSTREAMLINED = 0;
	public static final int FRAME_STREAMLINED = 1;
	public static final int FRAME_AIRFRAME = 2;
    
    public static final String[] streamliningDescription = 
    {
        "Unstreamlined",
        "Streamlined",
        "Airframe",
    };
	
	private double	mVolume;
	private int		mTechLevel;
	private static final int[] mTechLevelRange = { 5, 17 };
	public static final String configLabel = "Configuration";
	private int		mConfig;
	private static final int[] mConfigRange = { 0, 9 };
	private int		mStreamlining;
	private int		mArmor;
	
    /**
     *
     */

    public Hull()
    {
        super();
        mVolume = 1350.0;
        mTechLevel = 10;
        mConfig = HULL_BOX;
        mArmor = 40;
    }

    /**
     *
     */

    public String getName()
    {
        return "Hull";
    }

    /**
     *
     */

    public int getTechLevel()
    {
        return mTechLevel;
    }

	public int[] getTechLevelRange()
	{
		return mTechLevelRange;
	}

    /**
     *
     */

    public double getVolume()
    {
        return mVolume;
    }

	public double getUsableVolume()
	{
		double vol = mVolume;
		if (mConfig == HULL_PLANETOID)
			vol *= .8;
		else if (mConfig == HULL_BUFFERED_PLANETOID)
			vol *= .65;
		double environ = .005+.005+.003;
		if (mTechLevel >= 10)
			environ += .010 + .010;
		vol -= vol*environ;
		return vol;
	}

	public String getUsableVolumeDescription()
	{
		return DisplayUtils.formatTons((int)(getUsableVolume()/13.5));
	}
    /**
     *
     */

    public double getWeight()
    {
		double weight;
		double armourMod;
    	if (mConfig == HULL_PLANETOID)
    	{
    		weight = mVolume*1;
    		if (mArmor > 50)
    			armourMod = MathUtils.tableLookup(0, mArmor, 1, armourTable) - MathUtils.tableLookup(0, 50, 1, armourTable);
    		else
    			armourMod = 1.0; 
		}
		else if (mConfig == HULL_BUFFERED_PLANETOID)
		{
			weight = mVolume*1.75;
			if (mArmor > 56)
				armourMod = MathUtils.tableLookup(0, mArmor, 1, armourTable) - MathUtils.tableLookup(0, 56, 1, armourTable);
			else
				armourMod = 1.0; 
		}
		else
		{
			weight = MathUtils.tableLookup(0, mVolume, 1, hullTable);
	        switch (mConfig)
	        {
	        	case HULL_OPEN_FRAME:
	        		weight *= .5;
	        		break;
				case HULL_SPHERE:
					weight *= .8;
					break;
				case HULL_DOME:
				case HULL_IRREGULAR:
					weight *= .9;
					break;
	        }
			armourMod = MathUtils.tableLookup(0, mArmor, 1, armourTable);
			armourMod *= MathUtils.tableLookup(0, mTechLevel, 1, hullType);
		}
		double environ = .005+.005+.003;
		if (mTechLevel >= 10)
			environ += .020 + .020;		
        return weight*armourMod + environ*getUsableVolume();
    }

    /**
     *
     */

    public double getPower()
    {
		double environ = .001+.001+.002;
		if (mTechLevel >= 10)
			environ += .050 + .020;		
        return environ*getUsableVolume();
    }

    /**
     *
     */
	private double getHullPrice()
	{
		double price;
		double priceMod;
		if (mConfig == HULL_PLANETOID)
		{
			price = mVolume*85;
			if (mArmor > 50)
				priceMod = MathUtils.tableLookup(0, mArmor, 1, armourTable) - MathUtils.tableLookup(0, 50, 1, armourTable);
			else
				priceMod = 1.0; 
		}
		else if (mConfig == HULL_BUFFERED_PLANETOID)
		{
			price = mVolume*85;
			if (mArmor > 56)
				priceMod = MathUtils.tableLookup(0, mArmor, 1, armourTable) - MathUtils.tableLookup(0, 56, 1, armourTable);
			else
				priceMod = 1.0; 
		}
		else
		{
			price = MathUtils.tableLookup(0, mVolume, 2, hullTable)*1000000;
			switch (mConfig)
			{
				case HULL_OPEN_FRAME:
					price *= .5;
					break;
				case HULL_NEEDLE:
					price *= 1.2;
					if (mStreamlining == FRAME_AIRFRAME)
						price *= 1.5;
					break;
				case HULL_CONE:
					price *= 1.1;
					if (mStreamlining == FRAME_AIRFRAME)
						price *= 2.0;
					break;
				case HULL_BOX:
					price *= .6;
					if (mStreamlining == FRAME_STREAMLINED)
						price *= 1.5;
					break;
				case HULL_CYLINDER:
					if (mStreamlining == FRAME_STREAMLINED)
						price *= 1.2;
					else if (mStreamlining == FRAME_AIRFRAME)
						price *= 3.0;
					break;
				case HULL_SPHERE:
					price *= 1.5;
					break;
				case HULL_DOME:
					price *= 1.2;
					if (mStreamlining == FRAME_STREAMLINED)
						price *= 2.0;
					else if (mStreamlining == FRAME_AIRFRAME)
						price *= 3.0;
					break;
				case HULL_IRREGULAR:
					price *= .5;
					break;
			}
			priceMod = MathUtils.tableLookup(0, mArmor, 1, armourTable);
			priceMod *= MathUtils.tableLookup(0, mTechLevel, 2, hullType);
		}		
		return price*priceMod;
	}
	
	private double getEnvironPrice()
	{
		double environ = 10+300+200;
		if (mTechLevel >= 10)
			environ += 500 + 250;
		return environ*getUsableVolume();
	}
	
	private double getScoopsPrice()
	{
		if (mStreamlining == FRAME_UNSTREAMLINED)
			return 0;
		return 0.000075*getVolume();
	}

    public double getPrice()
    {
    	return getHullPrice() + getEnvironPrice() + getScoopsPrice();
    }
	
	public int getHullControlPoints()
	{
		return (int)(getHullPrice()/1000000.0*getTechLevel());
	}
	
	public int getEnvironControlPoints()
	{
		return (int)(getEnvironPrice()/1000000.0*getTechLevel());
	}

    public int getArmor()
    {
		if (mConfig == HULL_PLANETOID)
		{
			if (mArmor < 50)
				return 50;
		}
		else if (mConfig == HULL_BUFFERED_PLANETOID)
		{
			if (mArmor < 56)
				return 56;
		}
        return mArmor;
    }

	public int[] getArmorRange()
	{
		int[] ret = new int[2];
		ret[1] = 120;
		if (mConfig == HULL_PLANETOID)
			ret[0] = 50;
		else if (mConfig == HULL_BUFFERED_PLANETOID)
			ret[0] = 56;
		else
			ret[0] = 1;
		return ret;
	}

    public int getConfig()
    {
        return mConfig;
    }

	public int[] getConfigRange()
	{
		return mConfigRange;
	}
	
	public String getConfigDescription()
	{
		return hullDescription[mConfig];
	}

    public int getStreamlining()
    {
        return mStreamlining;
    }

	public String getStreamliningDescription()
	{
	    return streamliningDescription[mStreamlining];
	}

	public int[] getStreamliningRange()
	{
		int[] ret = new int[2];
		switch (mConfig)
		{
			case HULL_OPEN_FRAME:
			case HULL_IRREGULAR:
			case HULL_PLANETOID:
			case HULL_BUFFERED_PLANETOID:
				ret[0] = FRAME_UNSTREAMLINED;
				ret[1] = FRAME_UNSTREAMLINED;
				break;
			case HULL_NEEDLE:
			case HULL_CONE:
			case HULL_DOME:
				ret[0] = FRAME_STREAMLINED;
				ret[1] = FRAME_AIRFRAME;
				break;
			case HULL_CYLINDER:
				ret[0] = FRAME_UNSTREAMLINED;
				ret[1] = FRAME_AIRFRAME;
				break;
			case HULL_BOX:
				ret[0] = FRAME_UNSTREAMLINED;
				ret[1] = FRAME_STREAMLINED;
				break;
			case HULL_SPHERE:
				ret[0] = FRAME_STREAMLINED;
				ret[1] = FRAME_STREAMLINED;
				break;
		}
		return ret;
	}

    public void setArmor(int i)
    {
        mArmor = i;
    }

    public void setConfig(int i)
    {
        mConfig = i;
    }

    public void setStreamlining(int i)
    {
        mStreamlining = i;
    }

    public void setTechLevel(int i)
    {
        mTechLevel = i;
    }

    public void setVolume(double d)
    {
        mVolume = d;
    }
    
    public boolean getSpaceworthy()
    {
    	return mArmor >= 40;
    }

	private double[][] hullTable = 
	{
		{ 0.10 ,0.010 ,0.00040  },
		{ 0.25 ,0.025 ,0.00085  },
		{ 0.50 ,0.050 ,0.00120  },
		{ 0.75 ,0.075 ,0.00140  },
		{ 1.00 ,0.100 ,0.00160  },
		{ 1.25 ,0.125 ,0.00180  },
		{ 1.50 ,0.150 ,0.00200  },
		{ 1.75 ,0.175 ,0.00220  },
		{ 2.50 ,0.250 ,0.00240  },
		{ 3.37 ,0.400 ,0.00260  },
		{ 6.75 ,0.700 ,0.00280  },
		{ 10.12 ,1.100 ,0.00300  },
		{ 13.50 ,1.500 ,0.00330  },
		{ 27.00 ,2.200 ,0.00370  },
		{ 40.50 ,2.800 ,0.00420  },
		{ 54.00 ,3.500 ,0.00580  },
		{ 67.50 ,4.000 ,0.00650  },
		{ 81.00 ,4.600 ,0.00750  },
		{ 94.50 ,5.200 ,0.00850  },
		{ 108.00 ,5.700 ,0.01000  },
		{ 121.50 ,6.300 ,0.01370  },
		{ 135.00 ,6.800 ,0.01740  },
		{ 148.50 ,7.300 ,0.02110  },
		{ 162.00 ,7.800 ,0.02480  },
		{ 175.50 ,8.400 ,0.02850  },
		{ 189.00 ,8.800 ,0.03030  },
		{ 202.50 ,9.300 ,0.03300  },
		{ 216.00 ,9.800 ,0.03670  },
		{ 229.50 ,10.300 ,0.03840  },
		{ 243.00 ,10.800 ,0.04010  },
		{ 256.50 ,11.300 ,0.04280  },
		{ 270.00 ,11.800 ,0.04455  },
		{ 337.50 ,13.400 ,0.04970  },
		{ 405.00 ,16.200 ,0.05680  },
		{ 472.50 ,18.800 ,0.06210  },
		{ 540.00 ,21.600 ,0.06682  },
		{ 607.50 ,23.600 ,0.06990  },
		{ 675.00 ,26.300 ,0.07240  },
		{ 742.50 ,28.800 ,0.07940  },
		{ 810.00 ,31.600 ,0.08420  },
		{ 877.50 ,33.300 ,0.08950  },
		{ 945.00 ,35.000 ,0.09350  },
		{ 1012.50 ,36.400 ,0.10000  },
		{ 1080.00 ,37.800 ,0.10700  },
		{ 1147.50 ,38.500 ,0.11400  },
		{ 1215.00 ,39.000 ,0.12400  },
		{ 1282.50 ,39.500 ,0.12700  },
		{ 1350.00 ,40.000 ,0.134  },
		{ 2700.00 ,70.000 ,0.265  },
		{ 4050.00 ,100.000 ,0.400  },
		{ 5400.00 ,130.000 ,0.535  },
		{ 6750.00 ,160.000 ,0.665  },
		{ 8100.00 ,190.000 ,0.805  },
		{ 9450.00 ,220.000 ,0.935  },
		{ 10800.00 ,250.000 ,1.050  },
		{ 12150.00 ,280.000 ,1.200  },
		{ 13500.00 ,310.000 ,1.350  },
		{ 27000.00 ,600.000 ,2.700  },
		{ 40500.00 ,900.000 ,4.000  },
		{ 54000.00 ,1200.000 ,5.300  },
		{ 67500.00 ,1500.000 ,6.700  },
		{ 81000.00 ,1800.000 ,8.000  },
		{ 94500.00 ,2100.000 ,9.400  },
		{ 108000.00 ,2400.000 ,10.700  },
		{ 121500.00 ,2700.000 ,12.100  },
		{ 135000.00 ,3000.000 ,13.400  },
		{ 270000.00 ,6000.000 ,15.600  },
		{ 405000.00 ,8000.000 ,17.800  },
		{ 540000.00 ,10000.000 ,20.100  },
		{ 675000.00 ,11500.000 ,2.300  },
		{ 1010000.00 ,15000.000 ,44.600  },
		{ 1350000.00 ,18000.000 ,66.800  },
		{ 2700000.00 ,29000.000 ,89.100  },
		{ 4050000.00 ,38000.000 ,111.400  },
		{ 5400000.00 ,46000.000 ,166.700  },
		{ 6750000.00 ,54000.000 ,222.800  },
		{ 9450000.00 ,67000.000 ,445.500  },
		{ 12150000.00 ,79000.000 ,668.400  },
		{ 13500000.00 ,85000.000 ,891.000  },
	};

	private double[][] armourTable = 
	{
		{ 1,0.25 },
		{ 2,0.5 },
		{ 3,0.75 },
		{ 4,1 },
		{ 5,1.25 },
		{ 6,1.5 },
		{ 7,1.75 },
		{ 8,2 },
		{ 9,2.25 },
		{ 10,2.5 },
		{ 11,2.75 },
		{ 12,3 },
		{ 13,3.25 },
		{ 14,3.54 },
		{ 15,3.86 },
		{ 16,4.2 },
		{ 17,4.59 },
		{ 18,5 },
		{ 19,5.45 },
		{ 20,5.95 },
		{ 21,6.48 },
		{ 22,7.07 },
		{ 23,7.71 },
		{ 24,8.41 },
		{ 25,9.17 },
		{ 26,10 },
		{ 27,10.9 },
		{ 28,11.9 },
		{ 29,13 },
		{ 30,14.1 },
		{ 31,15.4 },
		{ 32,16.8 },
		{ 33,18.3 },
		{ 34,20 },
		{ 35,21.8 },
		{ 36,23.8 },
		{ 37,25.9 },
		{ 38,28.3 },
		{ 39,30.8 },
		{ 40,33 },
		{ 41,36.7 },
		{ 42,40 },
		{ 43,43.6 },
		{ 44,47.6 },
		{ 45,51.9 },
		{ 46,56.6 },
		{ 47,61.7 },
		{ 48,67.3 },
		{ 49,73.4 },
		{ 50,80 },
		{ 51,87.2 },
		{ 52,95.1 },
		{ 53,104 },
		{ 54,113 },
		{ 55,123 },
		{ 56,135 },
		{ 57,147 },
		{ 58,160 },
		{ 59,174 },
		{ 60,190 },
		{ 61,207 },
		{ 62,226 },
		{ 63,247 },
		{ 64,269 },
		{ 65,293 },
		{ 66,320 },
		{ 67,349 },
		{ 68,381 },
		{ 69,415 },
		{ 70,453 },
		{ 71,494 },
		{ 72,538 },
		{ 73,587 },
		{ 74,640 },
		{ 75,698 },
		{ 76,761 },
		{ 77,830 },
		{ 78,905 },
		{ 79,987 },
		{ 80,1080 },
		{ 81,1170 },
		{ 82,1280 },
		{ 83,1400 },
		{ 84,1520 },
		{ 85,1660 },
		{ 86,1810 },
		{ 87,1970 },
		{ 88,2150 },
		{ 89,2350 },
		{ 90,2560 },
		{ 91,2790 },
		{ 92,3040 },
		{ 93,3320 },
		{ 94,3620 },
		{ 95,3950 },
		{ 96,4310 },
		{ 97,4700 },
		{ 98,5120 },
		{ 99,5580 },
		{ 100,6090 },
		{ 101,6640 },
		{ 102,7240 },
		{ 103,7900 },
		{ 104,8610 },
		{ 105,9360 },
		{ 106,10200 },
		{ 107,11200 },
		{ 108,12200 },
		{ 109,13350 },
		{ 110,14500 },
		{ 111,15850 },
		{ 112,17200 },
		{ 113,18850 },
		{ 114,20500 },
		{ 115,22450 },
		{ 116,24400 },
		{ 117,26700 },
		{ 118,29000 },
		{ 119,31700 },
		{ 120,34400 },
	};
	
	private double[][] hullType =
	{
		{ 5,1.25,1 },
		{ 6,1,1 },
		{ 7,0.44,1.8 },
		{ 9,0.35,1.6 },
		{ 10,0.31,1.1 },
		{ 12,0.26,1 },
		{ 14,0.14,1 },
		{ 17,0.06,1.3 },
	};

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_HULL;
	}
}
