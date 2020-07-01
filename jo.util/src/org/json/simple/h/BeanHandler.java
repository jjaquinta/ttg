package org.json.simple.h;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.FromJSONLogic;
import org.json.simple.IFromJSONHandler;
import org.json.simple.IToJSONHandler;
import org.json.simple.JSONObject;
import org.json.simple.ToJSONLogic;

public class BeanHandler implements IToJSONHandler, IFromJSONHandler
{
    public static final String BEAN_CLASS = "$beanclass";
    @Override
    public boolean isHandler(Object o)
    {
        return true;
    }

    @Override
    public Object toJSON(Object bean)
    {
        return doToJSON(bean);
    }

    public static JSONObject doToJSON(Object bean)
    {
        return doToJSON(bean, null);
    }

    public static JSONObject doToJSON(Object bean, Set<String> skips)
    {
        return doToJSON(bean, skips, null, null);
    }

    public static JSONObject doToJSON(Object bean, Set<String> skips, List<String> first, List<String> last)
    {
        JSONObject json = new JSONObject();
        json.put(BEAN_CLASS, bean.getClass().getName());
        try
        {
            Map<String,PropertyDescriptor> candidates = new HashMap<>();
            BeanInfo beanClassInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] beanProps = beanClassInfo.getPropertyDescriptors();
            for (int i = 0; i < beanProps.length; i++)
            {
                String propName = beanProps[i].getName();
                if ((skips != null) && skips.contains(propName))
                    continue;
                Class<?> propType = beanProps[i].getPropertyType();
                if (propType == null)
                    continue;
                Method read = beanProps[i].getReadMethod();
                Method write = beanProps[i].getWriteMethod();
                if ((read != null) && (write != null))
                    candidates.put(propName, beanProps[i]);
            }
            // pre-pass
            if (first != null)
                for (String propName : first)
                    if (candidates.containsKey(propName))
                    {
                        doToJSONField(candidates.get(propName), bean, json);
                        candidates.remove(propName);
                    }
            // main-pass
            for (String propName : candidates.keySet().toArray(new String[0]))
                if ((last == null) || !last.contains(propName))
                {
                    doToJSONField(candidates.get(propName), bean, json);
                    candidates.remove(propName);
                }
            // post-pass
            if (last != null)
                for (String propName : last)
                    if (candidates.containsKey(propName))
                    {
                        doToJSONField(candidates.get(propName), bean, json);
                        candidates.remove(propName);
                    }
        }
        catch (IntrospectionException e1)
        {
            e1.printStackTrace();
        }
        return json;
    }

    private static void doToJSONField(PropertyDescriptor propDesc, Object bean, JSONObject json)
    {
        try
        {
            String propName = propDesc.getName();
            Object val = propDesc.getReadMethod().invoke(bean);
            if (val != null)
            {
                System.out.println("Serializing "+bean.hashCode()+"#"+bean.getClass().getName()+"."+propName);
                Object jval = ToJSONLogic.toJSON(val);
                if (jval != null)
                    json.put(propName, jval);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isHandler(Object json, Class<?> hint)
    {
        if (!(json instanceof JSONObject))
            return false;
        if (hint != null)
            return true;
        JSONObject o = (JSONObject)json;
        return o.containsKey(BEAN_CLASS);
    }

    @Override
    public void fromJSONInto(Object o, Object bean)
    {
        doFromJSONInto(o, bean);
    }
    
    public static void doFromJSONInto(Object o, Object bean)
    {
        doFromJSONInto(o, bean, null);
    }
    
    public static void doFromJSONInto(Object o, Object bean, Set<String> skips)
    {
        try
        {
            JSONObject json = (JSONObject)o;
            BeanInfo beanClassInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] beanProps = beanClassInfo.getPropertyDescriptors();
            for (int i = 0; i < beanProps.length; i++)
            {
                String propName = beanProps[i].getName();
                if ((skips != null) && skips.contains(propName))
                    continue;
                String jsonPropName = findJSONPropName(json, propName);
                if (jsonPropName == null)
                    continue;
                Object jsonPropValue = json.get(jsonPropName);
                Class<?> propType = beanProps[i].getPropertyType();
                if (propType == null)
                    continue;
                Method read = beanProps[i].getReadMethod();
                Method write = beanProps[i].getWriteMethod();
                if ((read != null) && (write != null))
                    try
                    {
                        Object propValue = FromJSONLogic.fromJSON(jsonPropValue, propType);
                        write.invoke(bean, propValue);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
            }
        }
        catch (Exception e1)
        {
            throw new IllegalStateException(e1);
        }
    }

    @Override
    public Object fromJSON(Object o, Class<?> hint)
    {
        try
        {
            JSONObject json = (JSONObject)o;
            if (hint == null)
                hint = (Class<?>)Class.forName(json.getString(BEAN_CLASS));
            Object bean = hint.newInstance();
            fromJSONInto(json, bean);
            return bean;
        }
        catch (Exception e1)
        {
            throw new IllegalStateException(e1);
        }
    }
    private static String findJSONPropName(JSONObject json, String propName)
    {
        if (json.containsKey(propName))
            return propName;
        for (String key : json.keySet())
            if (key.equalsIgnoreCase(propName))
                return key;
        return null;
    }
}
