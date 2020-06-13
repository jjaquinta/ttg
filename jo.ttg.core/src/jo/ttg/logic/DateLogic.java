/*
 * Created on Jul 19, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic;

import java.util.StringTokenizer;

import jo.ttg.beans.DateBean;
import jo.util.utils.obj.IntegerUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DateLogic
{
	public static long toDays(DateBean date)
	{
		return date.getDay() + 365*date.getYear();
	}
	public static long toHours(DateBean date)
	{
		return date.getHour() + 24*toDays(date);
	}
	public static long toMinutes(DateBean date)
	{
		return date.getMinute() + 60*toHours(date);
	}
	
	public static boolean earlierThan(DateBean date1, DateBean date2)
	{
		return toMinutes(date1) < toMinutes(date2);
	}
    
    public static boolean earlierThanOrEqual(DateBean date1, DateBean date2)
    {
        return toMinutes(date1) <= toMinutes(date2);
    }
	
	public static boolean laterThan(DateBean date1, DateBean date2)
	{
		return toMinutes(date1) > toMinutes(date2);
	}
    
    public static boolean laterThanOrEqual(DateBean date1, DateBean date2)
    {
        return toMinutes(date1) > toMinutes(date2);
    }
    public static void incrementMinutes(DateBean date, int interval)
    {
        date.setMinutes(date.getMinutes() + interval);
    }
    public static void incrementMinutes(DateBean date, DateBean interval)
    {
        incrementMinutes(date, interval.getMinutes());
    }
    public static void incrementSeconds(DateBean date, int interval)
    {
        date.setSeconds(date.getSeconds() + interval);
    }
    
    public static String toString(DateBean date)
    {
        return date.toString();
    }
    
    public static void fromString(String txt, DateBean date)
    {
        StringTokenizer st = new StringTokenizer(txt, "- :");
        date.setMinutes(0);
        if (st.hasMoreTokens())
        {
            date.setYear(IntegerUtils.parseInt(st.nextToken()));
            if (st.hasMoreTokens())
            {
                date.setDay(IntegerUtils.parseInt(st.nextToken()));
                if (st.hasMoreTokens())
                {
                    date.setHour(IntegerUtils.parseInt(st.nextToken()));
                    if (st.hasMoreTokens())
                    {
                        date.setMinute(IntegerUtils.parseInt(st.nextToken()));
                    }
                }
            }
        }
    }
    
    public static DateBean fromString(String txt)
    {
        DateBean date = new DateBean();
        fromString(txt, date);
        return date;
    }
}
