/*
 * Created on Jun 28, 2005
 *
 */
package jo.util.utils;

/**
 * @author Jo
 *
 */
public class ParseUtils
{

    public static int parseInt(Object str, int def)
    {
        try
        {
            return Integer.parseInt(str.toString().trim());
        }
        catch (Exception e)
        {
            return def;
        }
    }

    public static int parseInt(Object str)
    {
        return parseInt(str, 0);
    }

    public static long parseLong(String str)
    {
        try
        {
            return Long.parseLong(str.trim());
        }
        catch (Exception e)
        {
            return 0L;
        }
    }
    
    public static int extractInt(String str)
    {
        StringBuffer sb = new StringBuffer();
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++)
            if (Character.isDigit(c[i]))
                sb.append(c[i]);
        return parseInt(sb.toString());
    }
    
    public static double parseDouble(String str)
    {
        try
        {
            return Double.parseDouble(str.trim());
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    
    public static double parsePercent(String pc)
    {
        pc = pc.trim();
        if (pc.endsWith("%"))
            pc = pc.substring(0, pc.length() - 1);
        return parseDouble(pc);
    }
    
    public static int parseMoney(String dosh)
    {
        dosh = dosh.trim();
        if (dosh.startsWith(FormatUtils.CURRENCY_SYMBOL))
            dosh = dosh.substring(1);
        return parseInt(dosh);
    }

}
