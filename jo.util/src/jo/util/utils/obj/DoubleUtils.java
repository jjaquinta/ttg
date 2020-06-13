/*
 * Created on Sep 27, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils.obj;


public class DoubleUtils
{
    public static final double EPSILON = 0.0001;
    
    /**
     * Format a number as a string.
     * @param Num
     * @param siz
     * @param dpts
     * @return String
     */
    public static String format(double Num, int siz, int dpts)
    {
        //System.out.print("sNum("+String.valueOf(Num/7)+", "+String.valueOf(siz)+", "+String.valueOf(dpts)+")=");
        String s = String.valueOf(Num);
        String exp;
        int o;
        o = s.indexOf('E');
        if (o >= 0)
        {
            exp = s.substring(o);
            s = s.substring(0, o);
        }
        else
            exp = null;
        o = s.indexOf('.');
        if (o == 0)
            s = s.substring(0, o);
        else if (o > 0)
        {
            if (s.length() >= o + dpts + 1)
                s = s.substring(0, o + dpts + 1);
        }
        if (exp != null)
            s += exp;
        if (siz > 0)
        {
            s = "            " + s;
            s = s.substring(s.length() - siz);
        }
        else if (siz < -1)
        {
            s = s + "            ";
            s = s.substring(0, -siz);
        }
        //System.out.println(s);
        return s;
    }

    public static double parseDouble(Object object)
    {
        if (object == null)
            return 0L;
        if (object instanceof Number)
            return ((Number)object).doubleValue();
        String str = object.toString();
        try
        {
        	int o = StringUtils.indexNotOf(str, "-+0123456789.");
        	if (o >= 0)
        		str = str.substring(0, o);
            return Double.parseDouble(str);
        }
        catch (Exception ex)
        {
            return 0;
        }
    }

    public static Object[] toArray(double[] doubleArray)
    {
        if (doubleArray == null)
            return null;
        Double[] objArray = new Double[doubleArray.length];
        for (int i = 0; i < doubleArray.length; i++)
            objArray[i] = new Double(doubleArray[i]);
        return objArray;
    }

    public static boolean greaterThan(double a, double b)
    {
        return a - b > EPSILON;
    }

    public static boolean lessThan(double a, double b)
    {
        return b - a > EPSILON;
    }

    public static boolean equals(double a, double b)
    {
        return Math.abs(a - b) < EPSILON;
    }

    public static String toString(double[] array)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < array.length; i++)
        {
            if (i > 0)
                sb.append(", ");
            sb.append(array[i]);
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static String toString(double d)
    {
        String txt = String.valueOf(d);
        if (txt.endsWith(".0"))
            txt = txt.substring(0, txt.length() - 2);
        return txt;
    }
    
    public static double roundToSignificantFigures(double num, int n) 
    {
        if (num == 0)
            return 0;
        if (n == 0)
            return 0;

        final double d = Math.ceil(Math.log10(num < 0 ? -num: num));
        final int power = n - (int) d;

        final double magnitude = Math.pow(10, power);
        final long shifted = Math.round(num*magnitude);
        return shifted/magnitude;
    }
}