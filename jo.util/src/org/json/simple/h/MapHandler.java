package org.json.simple.h;

import java.util.Map;

import org.json.simple.IFromJSONHandler;
import org.json.simple.IToJSONHandler;
import org.json.simple.JSONObject;
import org.json.simple.ToJSONLogic;

public class MapHandler implements IToJSONHandler, IFromJSONHandler
{
    private static final String MAP_CLASS = "$mapclass";

    @Override
    public boolean isHandler(Object val)
    {
        return val instanceof Map<?,?>;
    }

    @Override
    public Object toJSON(Object val)
    {
        return toJSON((Map<?,?>)val);
    }

    private JSONObject toJSON(Map<?,?> map)
    {
        JSONObject json = new JSONObject();
        json.put(MAP_CLASS, map.getClass().getName());
        for (Object key : map.keySet())
        {
            Object bean = map.get(key);
            json.put(key.toString(), ToJSONLogic.toJSON(bean));
        }
        return json;
    }

    @Override
    public boolean isHandler(Object json, Class<?> hint)
    {
        if (!(json instanceof JSONObject))
            return false;
        if (hint != null)
            return Map.class.isAssignableFrom(hint);
        JSONObject o = (JSONObject)json;
        return o.containsKey(MAP_CLASS);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void fromJSONInto(Object json, Object bean)
    {
        try
        {
            JSONObject o = (JSONObject)json;
            Map map = (Map)bean;
            for (Object key : o.keySet())
            {
                if (MAP_CLASS.equals(key))
                    continue;
                Object value = o.get(key);
                map.put(key, fromJSON(value, null));
            }
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
    }
        
    @Override
    public Object fromJSON(Object json, Class<?> hint)
    {
        try
        {
            JSONObject o = (JSONObject)json;
            if (hint == null)
                hint = (Class<?>)Class.forName(o.getString(MAP_CLASS));
            @SuppressWarnings("rawtypes")
            Map map = (Map)hint.newInstance();
            fromJSONInto(json, map);
            return map;
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
    }
}
