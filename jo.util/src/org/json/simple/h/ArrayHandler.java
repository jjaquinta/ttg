package org.json.simple.h;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.FromJSONLogic;
import org.json.simple.IFromJSONHandler;
import org.json.simple.IToJSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.ToJSONLogic;

import jo.util.utils.obj.FloatUtils;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.LongUtils;

public class ArrayHandler implements IToJSONHandler, IFromJSONHandler
{
    @Override
    public boolean isHandler(Object val)
    {
        if (val instanceof Object[][])
            return true;
        else if (val instanceof Object[])
            return true;
        else if (val instanceof long[])
            return true;
        else if (val instanceof int[])
            return true;
        else if (val instanceof String[])
            return true;
        else if (val instanceof Collection<?>)
            return true;
        return false;
    }

    @Override
    public Object toJSON(Object val)
    {
        if (val instanceof Object[][])
            return toJSONArray((Object[])val);
        else if (val instanceof Object[])
            return toJSONArray((Object[])val);
        else if (val instanceof long[])
            return toJSONArray((long[])val);
        else if (val instanceof int[])
            return toJSONArray(IntegerUtils.toArray((int[])val));
        else if (val instanceof String[])
            return toJSONArray((String[])val);
        else if (val instanceof Collection<?>)
            return toJSONArray(((Collection<?>)val).toArray());
        else
            return null;
    }

    @SuppressWarnings("unchecked")
    private static JSONArray toJSONArray(Object[] arr)
    {
        JSONArray json = new JSONArray();
        for (Object o : arr)
        {
            if (o == null)
                json.add(null);
            else
                json.add(ToJSONLogic.toJSON(o));
        }
        return json;
    }
    @SuppressWarnings("unchecked")
    private JSONArray toJSONArray(long[] beans)
    {
        JSONArray json = new JSONArray();
        for (long bean : beans)
            json.add(bean);
        return json;
    }

    @Override
    public boolean isHandler(Object json, Class<?> propType)
    {
        if (!(json instanceof JSONArray))
            return false;
        if (propType == int[].class)
            return true;
        else if (propType == long[].class)
            return true;
        else if (propType == float[].class)
            return true;
        else if (propType == String[].class)
            return true;
        else if (propType == Object[].class)
            return true;
        else if (Collection.class.isAssignableFrom(propType))
            return true;
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void fromJSONInto(Object jsonPropValue, Object bean)
    {
        Class<?> propType = bean.getClass();
        if (propType == int[].class)
        {
            int[] newVal = toIntArray((JSONArray)jsonPropValue);
            int[] oldVal = (int[])bean;
            System.arraycopy(newVal, 0, oldVal, 0, Math.min(newVal.length, oldVal.length));
        }
        else if (propType == long[].class)
        {
            long[] newVal = toLongArray((JSONArray)jsonPropValue);
            long[] oldVal = (long[])bean;
            System.arraycopy(newVal, 0, oldVal, 0, Math.min(newVal.length, oldVal.length));
        }
        else if (propType == float[].class)
        {
            float[] newVal = toFloatArray((JSONArray)jsonPropValue);
            float[] oldVal = (float[])bean;
            System.arraycopy(newVal, 0, oldVal, 0, Math.min(newVal.length, oldVal.length));
        }
        else if (propType == String[].class)
        {
            String[] newVal = toStringArray((JSONArray)jsonPropValue);
            String[] oldVal = (String[])bean;
            System.arraycopy(newVal, 0, oldVal, 0, Math.min(newVal.length, oldVal.length));
        }
        else if (propType == Object[].class)
        {
            Object[] newVal = toObjectArray((JSONArray)jsonPropValue);
            Object[] oldVal = (Object[])bean;
            System.arraycopy(newVal, 0, oldVal, 0, Math.min(newVal.length, oldVal.length));
        }
        else if ((propType == ArrayList.class) || (propType == HashSet.class) || (propType == Collection.class))
        {
            Collection newVal = toCollection((JSONArray)jsonPropValue, propType);
            Collection oldVal = (Collection)bean;
            oldVal.clear();
            oldVal.addAll(newVal);
        }
        else
            throw new IllegalStateException();
    }

    @Override
    public Object fromJSON(Object jsonPropValue, Class<?> propType)
    {
        if (propType == int[].class)
            return toIntArray((JSONArray)jsonPropValue);
        else if (propType == long[].class)
            return toLongArray((JSONArray)jsonPropValue);
        else if (propType == float[].class)
            return toFloatArray((JSONArray)jsonPropValue);
        else if (propType == String[].class)
            return toStringArray((JSONArray)jsonPropValue);
        else if (propType == Object[].class)
            return toObjectArray((JSONArray)jsonPropValue);
        else if (Collection.class.isAssignableFrom(propType))
        {
            return toCollection((JSONArray)jsonPropValue, propType);
        }
        throw new IllegalStateException();
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Collection toCollection(JSONArray jsonArray, Class<?> propType)
    {
        Collection ret;
        try
        {
            if ((propType == List.class) || (propType == Collection.class))
                ret = new ArrayList<>();
            else if ((propType == Set.class))
                ret = new HashSet<>();
            else
                ret = (Collection)propType.newInstance();
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
        for (int i = 0; i < jsonArray.size(); i++)
            ret.add(FromJSONLogic.fromJSON(jsonArray.get(i), null));
        return ret;
    }
    private static Object[] toObjectArray(JSONArray jsonArray)
    {
        Object[] ret = new Object[jsonArray.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = FromJSONLogic.fromJSON(jsonArray.get(i), null);
        return ret;
    }
    private static String[] toStringArray(JSONArray jsonArray)
    {
        String[] ret = new String[jsonArray.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = jsonArray.get(i).toString();
        return ret;
    }
    private static int[] toIntArray(JSONArray jsonArray)
    {
        int[] ret = new int[jsonArray.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = IntegerUtils.parseInt(jsonArray.get(i));
        return ret;
    }
    private static long[] toLongArray(JSONArray jsonArray)
    {
        long[] ret = new long[jsonArray.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = LongUtils.parseLong(jsonArray.get(i));
        return ret;
    }
    private static float[] toFloatArray(JSONArray jsonArray)
    {
        float[] ret = new float[jsonArray.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = FloatUtils.parseFloat(jsonArray.get(i));
        return ret;
    }
}
