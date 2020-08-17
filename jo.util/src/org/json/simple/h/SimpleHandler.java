package org.json.simple.h;

import org.json.simple.IFromJSONHandler;
import org.json.simple.IJSONAble;
import org.json.simple.IToJSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jo.util.utils.obj.BooleanUtils;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.FloatUtils;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.LongUtils;

public class SimpleHandler implements IToJSONHandler, IFromJSONHandler
{
    @Override
    public boolean isHandler(Object o)
    {
        if (o == null)
            return false;
        if ((o instanceof JSONArray) || (o instanceof JSONObject))
            return true;
        if ((o instanceof IJSONAble))
            return true;
        Class<?> clazz = o.getClass();
        return clazz.isPrimitive() || clazz.getName().startsWith("java.lang.");
    }

    @Override
    public Object toJSON(Object o)
    {
        if ((o instanceof IJSONAble))
            return ((IJSONAble)o).toJSON();
        if (o instanceof Character)
            return o.toString();
        return o;
    }

    @Override
    public boolean isHandler(Object json, Class<?> propType)
    {
        if (json == null)
            return false;
        if ((propType != null) && IJSONAble.class.isAssignableFrom(propType) && (json instanceof JSONObject))
            return true;
        if ((propType == String.class) || (json instanceof String))
            return true;
        else if ((propType == int.class) || (propType == Integer.class) || (json instanceof Integer))
            return true;
        else if ((propType == long.class) || (propType == Long.class) || (json instanceof Long))
            return true;
        else if ((propType == float.class) || (propType == Float.class) || (json instanceof Float))
            return true;
        else if ((propType == double.class) || (propType == Double.class) || (json instanceof Double))
            return true;
        else if ((propType == boolean.class) || (propType == Boolean.class) || (json instanceof Boolean))
            return true;
        return false;
    }

    @Override
    public Object fromJSON(Object json, Class<?> propType)
    {
        if ((propType != null) && IJSONAble.class.isAssignableFrom(propType))
        {
            IJSONAble obj;
            try
            {
                obj = (IJSONAble)propType.newInstance();
            }
            catch (Exception e)
            {
                throw new IllegalStateException(e);
            }
            obj.fromJSON((JSONObject)json);
            return obj;
        }
        if ((propType == String.class) || (json instanceof String))
            return json.toString();
        else if ((propType == int.class) || (propType == Integer.class) || (json instanceof Integer))
            return IntegerUtils.parseInt(json);
        else if ((propType == long.class) || (propType == Long.class) || (json instanceof Long))
            return LongUtils.parseLong(json);
        else if ((propType == float.class) || (propType == Float.class) || (json instanceof Float))
            return FloatUtils.parseFloat(json);
        else if ((propType == double.class) || (propType == Double.class) || (json instanceof Double))
            return DoubleUtils.parseDouble(json);
        else if ((propType == boolean.class) || (propType == Boolean.class) || (json instanceof Boolean))
            return BooleanUtils.parseBoolean(json);
        else
            throw new IllegalStateException();
    }

    @Override
    public void fromJSONInto(Object json, Object bean)
    {
        if (bean instanceof IJSONAble)
        {
            IJSONAble obj = (IJSONAble)bean;
            obj.fromJSON((JSONObject)json);
        }
        throw new IllegalStateException("Cannot update sipmle objects in place");
    }

}
