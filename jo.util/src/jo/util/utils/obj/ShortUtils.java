/*
 * Created on Sep 25, 2005
 *
 */
package jo.util.utils.obj;

import java.nio.ByteBuffer;

public class ShortUtils
{
    public static short[] dup(short[] arr)
    {
        short[] ret = new short[arr.length];
        System.arraycopy(arr, 0, ret, 0, arr.length);
        return ret;
    }

    public static short parseShort(Object obj)
    {
        if (obj == null)
            return 0;
        if (obj instanceof Number)
            return ((Number)obj).shortValue();
        return parseShort(obj.toString());
    }
    
    public static short parseShort(String str)
    {
        try
        {
            if (str == null)
                return 0;
            str = str.trim();
            if (str.startsWith("+"))
                str = str.substring(1);
            int o = str.indexOf('.');
            if (o >= 0)
                str = str.substring(0, o);
            return Short.parseShort(str);
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }

    public static short[] toArray(Object[] objArray)
    {
        if (objArray == null)
            return null;
        short[] shortArray = new short[objArray.length];
        for (int i = 0; i < objArray.length; i++)
            if (objArray[i] == null)
                shortArray[i] = 0;
            else if (objArray[i] instanceof Number)
                shortArray[i] = ((Number)objArray[i]).shortValue();
            else
                shortArray[i] = parseShort(objArray[i].toString());
        return shortArray;
    }

    public static Object[] toArray(short[] shortArray)
    {
        if (shortArray == null)
            return null;
        Short[] objArray = new Short[shortArray.length];
        for (int i = 0; i < shortArray.length; i++)
            objArray[i] = new Short(shortArray[i]);
        return objArray;
    }
    
    public static short[] fromBytes(byte[] bytes)
    {
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.rewind();
        short[] dst = new short[bytes.length/2];
        bb.asShortBuffer().get(dst);
        return dst;
    }
    
    public static byte[] toBytes(short[] shortArray)
    {
        ByteBuffer bb = ByteBuffer.allocate(4 + (shortArray.length * 2));
        for (short val : shortArray)
            bb.putShort(val);
        return bb.array();
    }
}
