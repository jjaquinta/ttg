/*
 * Created on Sep 27, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils.obj;


public class FloatUtils
{
    public static final float EPSILON = 0.0001f;
    public static final float PI = (float)Math.PI;   

    public static float parseFloat(Object o)
    {
    	if (o == null)
    		return 0;
    	if (o instanceof Number)
    		return ((Number)o).floatValue();
    	return parseFloat(o.toString());
    }

    public static float parseFloat(String str)
    {
        try
        {
        	int o = StringUtils.indexNotOf(str, "-+0123456789.");
        	if (o >= 0)
        		str = str.substring(0, o);
            return Float.parseFloat(str);
        }
        catch (Exception ex)
        {
            return 0;
        }
    }

    public static Object[] toArray(float[] floatArray)
    {
        if (floatArray == null)
            return null;
        Float[] objArray = new Float[floatArray.length];
        for (int i = 0; i < floatArray.length; i++)
            objArray[i] = Float.valueOf(floatArray[i]);
        return objArray;
    }

    public static boolean greaterThan(float a, float b)
    {
        return a - b > EPSILON;
    }

    public static boolean lessThan(float a, float b)
    {
        return b - a > EPSILON;
    }

    public static boolean equals(float a, float b)
    {
        return Math.abs(a - b) < EPSILON;
    }

    public static float min(float v1, float v2)
    {
        if (v1 < v2)
            return v1;
        else
            return v2;
    }

    public static float max(float v1, float v2)
    {
        if (v1 < v2)
            return v2;
        else
            return v1;
    }

    public static float tan(float f)
    {
        return (float)Math.tan(f);
    }

    public static String toString(float[] arr)
    {
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < arr.length; i++)
        {
            if (i > 0)
                sb.append(", ");
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}
