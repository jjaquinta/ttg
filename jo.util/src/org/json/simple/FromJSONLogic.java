package org.json.simple;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.h.ArrayHandler;
import org.json.simple.h.BeanHandler;
import org.json.simple.h.MapHandler;
import org.json.simple.h.SimpleHandler;

public class FromJSONLogic
{
    private static List<IFromJSONHandler> mHanders = new ArrayList<IFromJSONHandler>();
    static
    {
        mHanders.add(new MapHandler());
        mHanders.add(new ArrayHandler());
        mHanders.add(new SimpleHandler());
        mHanders.add(new BeanHandler());
    }
    
    public static void addHandler(IFromJSONHandler h)
    {
        mHanders.add(0, h);
    }
    
    public static Object fromJSON(Object json, Class<?> hint)
    {
        for (IFromJSONHandler h : mHanders)
            if (h.isHandler(json, hint))
                return h.fromJSON(json, hint);
        return null;
    }
    
    public static void fromJSONInto(Object json, Object bean)
    {
        for (IFromJSONHandler h : mHanders)
            if (h.isHandler(json, bean.getClass()))
            {
                h.fromJSONInto(json, bean);
                break;
            }
    }
}
