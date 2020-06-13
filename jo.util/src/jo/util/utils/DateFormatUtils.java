package jo.util.utils;

import java.util.Calendar;

public class DateFormatUtils 
{
	private static Calendar CAL = null;
	
	private static void makeCal()
	{
		if (CAL == null)
			CAL = Calendar.getInstance();
	}

	public static String yyyymmdd(long ticks)
	{
		makeCal();
		CAL.setTimeInMillis(ticks);
		return FormatUtils.zeroPrefix(CAL.get(Calendar.YEAR), 4)
			+ "-"
			+ FormatUtils.zeroPrefix(CAL.get(Calendar.MONTH), 2)
			+ "-"
			+ FormatUtils.zeroPrefix(CAL.get(Calendar.DAY_OF_MONTH), 2)
			;
	}

	public static String hhmmss(long ticks)
	{
		makeCal();
		CAL.setTimeInMillis(ticks);
		return FormatUtils.zeroPrefix(CAL.get(Calendar.HOUR_OF_DAY), 2)
			+ ":"
			+ FormatUtils.zeroPrefix(CAL.get(Calendar.MINUTE), 2)
			+ ":"
			+ FormatUtils.zeroPrefix(CAL.get(Calendar.SECOND), 2)
			;
	}
}
