package jo.util.utils.obj;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BooleanUtils
{
    public static Object[] toArray(boolean[] booleanArray)
    {
        if (booleanArray == null)
            return null;
        Boolean[] objArray = new Boolean[booleanArray.length];
        for (int i = 0; i < booleanArray.length; i++)
            objArray[i] = new Boolean(booleanArray[i]);
        return objArray;
    }
    
    public static boolean parseBoolean(String v)
    {
        return parseBoolean(v, false);
    }

    public static boolean parseBoolean(Object v)
    {
        return parseBoolean(v, false);
    }

    public static boolean parseBoolean(String v, boolean def)
    {
        if (StringUtils.isTrivial(v))
            return def;
        return v.equalsIgnoreCase("true") || v.equalsIgnoreCase("yes") || v.equalsIgnoreCase("y") 
            || v.equalsIgnoreCase("1") || v.equalsIgnoreCase("-1");
    }

    public static boolean parseBoolean(Object v, boolean def)
    {
        if (v == null)
            return def;
        if (v instanceof Boolean)
            return ((Boolean)v).booleanValue();
        if (v instanceof String)
            return parseBoolean((String)v);
        if (v instanceof Number)
            return Math.abs(((Number)v).doubleValue()) < 0.0001;
        return def;
    }
    
    public static boolean[] fromBytes(byte[] bytes)
    {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            int len = ois.readInt();
            boolean[] ret = new boolean[len];
            for (int i = 0; i < len; i++)
                ret[i] = (ois.readInt() != 0);
            return ret;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static byte[] toBytes(boolean[] vals)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeInt(vals.length);
            for (int i = 0; i < vals.length; i++)
                oos.writeInt(vals[i] ? 1 : 0);
            oos.flush();
            return baos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
