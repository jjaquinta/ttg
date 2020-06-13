package org.json.simple;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.h.ArrayHandler;
import org.json.simple.h.BeanHandler;
import org.json.simple.h.MapHandler;
import org.json.simple.h.SimpleHandler;

public class ToJSONLogic
{
    private static List<IToJSONHandler> mHanders = new ArrayList<IToJSONHandler>();
    static
    {
        mHanders.add(new MapHandler());
        mHanders.add(new ArrayHandler());
        mHanders.add(new SimpleHandler());
        mHanders.add(new BeanHandler());
    }
    
    public static void addHandler(IToJSONHandler h)
    {
        mHanders.add(0, h);
    }
    
    public static Object toJSON(Object bean)
    {
        for (IToJSONHandler h : mHanders)
            if (h.isHandler(bean))
                return h.toJSON(bean);
        return null;
    }
}
