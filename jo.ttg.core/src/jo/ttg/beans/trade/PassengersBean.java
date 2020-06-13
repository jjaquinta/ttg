package jo.ttg.beans.trade;

import jo.ttg.beans.DateBean;
import jo.util.beans.Bean;

public class PassengersBean extends Bean
{
    private long	mSeed;
    
    // High
    private int mHigh;
    public int getHigh()
    {
        return mHigh;
    }
    public void setHigh(int v)
    {
        mHigh = v;
    }

    // Middle
    private int mMiddle;
    public int getMiddle()
    {
        return mMiddle;
    }
    public void setMiddle(int v)
    {
        mMiddle = v;
    }

    // Low
    private int mLow;
    public int getLow()
    {
        return mLow;
    }
    public void setLow(int v)
    {
        mLow = v;
    }

    // Destination
    private String mDestination;
    public String getDestination()
    {
        return mDestination;
    }
    public void setDestination(String v)
    {
        mDestination = v;
    }

    // Origin
    private String mOrigin;
    public String getOrigin()
    {
        return mOrigin;
    }
    public void setOrigin(String v)
    {
        mOrigin = v;
    }

    // AvailableFirst
    private DateBean mAvailableFirst;
    public DateBean getAvailableFirst()
    {
        return mAvailableFirst;
    }
    public void setAvailableFirst(DateBean v)
    {
        mAvailableFirst = v;
    }

    // AvailableLast
    private DateBean mAvailableLast;
    public DateBean getAvailableLast()
    {
        return mAvailableLast;
    }
    public void setAvailableLast(DateBean v)
    {
        mAvailableLast = v;
    }


    // constructor
    public PassengersBean()
    {
        mHigh = 0;
        mMiddle = 0;
        mLow = 0;
        mSeed = 0;
        mOrigin = "";
        mDestination = "";
        mAvailableFirst = new DateBean();
        mAvailableLast = new DateBean();
    }
    public long getSeed()
    {
        return mSeed;
    }
    public void setSeed(long seed)
    {
        mSeed = seed;
    }
}
