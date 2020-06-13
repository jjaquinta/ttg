/*
 * Created on Sep 25, 2005
 *
 */
package jo.util.utils.obj;

import jo.util.utils.FormatUtils;

public class LongUtils
{

    public static String format(long v, int w)
    {
        if (w < 0)
            return FormatUtils.rightJustify(String.valueOf(v), -w);
        else
            return FormatUtils.leftJustify(String.valueOf(v), w);
    }
    
    public static long[] dup(long[] arr)
    {
        long[] ret = new long[arr.length];
        System.arraycopy(arr, 0, ret, 0, arr.length);
        return ret;
    }

    public static long parseLong(Object object)
    {
        if (object == null)
            return 0L;
        if (object instanceof Number)
            return ((Number)object).longValue();
        try
        {
            return Long.parseLong(object.toString());
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }

    public static Object[] toArray(long[] longArray)
    {
        if (longArray == null)
            return null;
        Long[] objArray = new Long[longArray.length];
        for (int i = 0; i < longArray.length; i++)
            objArray[i] = new Long(longArray[i]);
        return objArray;
    }
    
    public static long sgn(long l)
    {
        if (l < 0)
            return -l;
        else
            return l;
    }
}
