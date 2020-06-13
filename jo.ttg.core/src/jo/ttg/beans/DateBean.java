package jo.ttg.beans;

import jo.util.beans.Bean;
import jo.util.utils.obj.StringUtils;

public class DateBean extends Bean
{
    public static final int ONE_MINUTE = 1;
    public static final int ONE_HOUR = ONE_MINUTE*60;
    public static final int ONE_SHIFT = ONE_HOUR*8;
    public static final int ONE_DAY = ONE_HOUR*24;
    public static final int ONE_WEEK = ONE_DAY*7;
    public static final int TWO_WEEKS = ONE_WEEK*2;
    public static final int ONE_MONTH = ONE_WEEK*4;
    public static final int TWO_MONTHS = ONE_MONTH*2;
    public static final int ONE_YEAR = ONE_WEEK*52;

    public DateBean(Bean b)
	{
		super(b);
	}
	
	public DateBean(DateBean d)
	{
	    setMinutes(d.getMinutes());
	}
	
	public void set(DateBean d)
	{
	    setMinutes(d.getMinutes());
	}
	
	@Override
	public String toString()
	{
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtils.zeroPrefix(getYear(), 4));
        sb.append('-');
        sb.append(StringUtils.zeroPrefix(getDay(), 3));
        if ((getHour() > 0) && (getMinute() > 0))
        {
            sb.append(' ');
            sb.append(StringUtils.zeroPrefix(getHour(), 2));
            sb.append(':');
            sb.append(StringUtils.zeroPrefix(getMinute(), 2));
        }
        return sb.toString();
	}
	
    public String toElapsedString()
    {
        StringBuffer sb = new StringBuffer();
        boolean doit = false;
        if (getYear() > 0 || doit)
        {
            sb.append(getYear());
            sb.append("y ");
            doit = true;
        }
        if (getDay() > 0 || doit)
        {
            sb.append(getDay());
            sb.append("d ");
            doit = true;
        }
        if (getHour() > 0 || doit)
        {
            sb.append(getHour());
            sb.append("h ");
            doit = true;
        }
        if (getMinute() > 0 || doit)
        {
            sb.append(getMinute());
            sb.append("m");
            doit = true;
        }
        return sb.toString();
    }
    
    // Year
    private int mYear;
    public int getYear()
    {
        return mYear;
    }
    public void setYear(int v)
    {
        mYear = v;
    }

    // Day
    private int mDay;
    public int getDay()
    {
        return mDay;
    }
    public void setDay(int v)
    {
        mDay = v;
		round();
    }

    // Hour
    private int mHour;
    public int getHour()
    {
        return mHour;
    }
    public void setHour(int v)
    {
        mHour = v;
		round();
    }

    // Minute
    private int mMinute;
    public int getMinute()
    {
        return mMinute;
    }
    public void setMinute(int v)
    {
        mMinute = v;
		round();
    }

    // Second
    private int mSecond;
    public int getSecond()
    {
        return mSecond;
    }
    public void setSecond(int v)
    {
        mSecond = v;
        round();
    }


    // constructor
    public DateBean()
    {
        mYear = 0;
        mDay = 0;
        mHour = 0;
        mMinute = 0;
        mSecond = 0;
    }
    
    public DateBean(int minutes)
    {
        this();
        setMinutes(minutes);
    }
    
    public long getDays()
    {
		return getYear()*365 + getDay();
    }
    
    public int getMinutes()
    {
		return getMinute() + (getHour() + (getDay() + getYear()*365)*24)*60;
    }
    
    public void setMinutes(int min)
    {
        mYear = 0;
        mDay = 0;
        mHour = 0;
        mMinute = 0;
        mSecond = 0;
        setMinute(min);
    }
    
    public long getSeconds()
    {
        return getSecond() + (getMinute() + (getHour() + (getDay() + getYear()*365L)*24L)*60L)*60L;
    }
    
    public void setSeconds(long secs)
    {
        mSecond = (int)(secs%60);
        secs /= 60;
        mMinute = (int)(secs%60);
        secs /= 60;
        mHour = (int)(secs%24);
        secs /= 24;
        mDay = (int)(secs%365);
        secs /= 365;
        mYear = (int)secs;
    }
    
    private void round()
    {
    	if (mSecond >= 60)
    	{
    		mMinute += mSecond/60;
    		mSecond = mSecond%60;
    	}
    	if (mSecond < 0)
    	{
    		mMinute -= ((-mSecond) + 59)/60;
    		mSecond = 60 + mSecond%60;
    	}
		if (mMinute >= 60)
		{
			mHour += mMinute/60;
			mMinute = mMinute%60;
		}
		if (mMinute < 0)
		{
			mHour -= ((-mMinute) + 59)/60;
			mMinute = 60 + mMinute%60;
		}
		if (mHour >= 24)
		{
			mDay += mHour/24;
			mHour = mHour%24;
		}
		if (mHour < 0)
		{
			mDay -= ((-mHour) + 23)/24;
			mHour = 24 + mHour%24;
		}
		if (mDay >= 365)
		{
			mYear += mDay/365;
			mDay = mDay%365;
		}
		if (mDay < 0)
		{
			mYear -= ((-mDay) + 364)/365;
			mDay = 365 + mDay%365;
		}
    }
}
