package jo.ttg.port.beans;

import jo.util.beans.PCSBean;

public class PortSpecsBean extends PCSBean
{
    private int    mSmallPads;          // up to 1000t
    private int    mMediumPads;         // up to 5000t
    private int    mLargePads;          // up to 25000t
    private double mFuelStorage  = 1.0; // 1.0 = 100% = 10% of tonnage of ship
                                        // pads
    private double mFuelRefinery = 1.0; // 1.0 = 100% = can refine entire fuel
                                        // supply
    // in 24 hours
    private double mWarehousing  = 1.0; // 1.0 = 100% = 20% of tonnage of ship
                                        // pads
    private double mHotel        = 1.0; // 1.0 = 100% = 1 bed per 25t of ship
                                        // pads

    // utilities
    public int getPads()
    {
        return mSmallPads + mMediumPads + mLargePads;
    }
    
    // getters and setters
    
    public int getSmallPads()
    {
        return mSmallPads;
    }

    public void setSmallPads(int smallPads)
    {
        mSmallPads = smallPads;
    }

    public int getMediumPads()
    {
        return mMediumPads;
    }

    public void setMediumPads(int mediumPads)
    {
        mMediumPads = mediumPads;
    }

    public int getLargePads()
    {
        return mLargePads;
    }

    public void setLargePads(int largePads)
    {
        mLargePads = largePads;
    }

    public double getFuelStorage()
    {
        return mFuelStorage;
    }

    public void setFuelStorage(double fuelStorage)
    {
        mFuelStorage = fuelStorage;
    }

    public double getFuelRefinery()
    {
        return mFuelRefinery;
    }

    public void setFuelRefinery(double fuelRefinery)
    {
        mFuelRefinery = fuelRefinery;
    }

    public double getWarehousing()
    {
        return mWarehousing;
    }

    public void setWarehousing(double warehousing)
    {
        mWarehousing = warehousing;
    }

    public double getHotel()
    {
        return mHotel;
    }

    public void setHotel(double hotel)
    {
        mHotel = hotel;
    }
}
