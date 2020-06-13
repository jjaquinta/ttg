package org.json.simple;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import jo.util.utils.io.ResourceUtils;
import jo.util.utils.obj.BooleanUtils;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.FloatUtils;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.LongUtils;
import jo.util.utils.obj.StringUtils;

public class JSONUtils
{
    public static final JSONParser PARSER = new JSONParser();
    
    @SuppressWarnings("rawtypes") 
    public static JSONObject toJSON(Map map)
    {
        JSONObject json = new JSONObject();
        for (Object key : map.keySet())
        {
            Object value = map.get(key);
            if (value instanceof IJSONAble)
                json.put(key.toString(), ((IJSONAble)value).toJSON());
            else
                json.put(key.toString(), value);
        }
        return json;
    }

    @SuppressWarnings("unchecked")
    public static JSONArray toJSON(Object[] items)
    {
        JSONArray json = new JSONArray();
        for (int i = 0; i < items.length; i++)
        {
            Object value = items[i];
            if (value instanceof IJSONAble)
                json.add(((IJSONAble)value).toJSON());
            else
                json.add(value);
        }
        return json;
    }
    
    @SuppressWarnings("rawtypes") 
    public static void fromJSON(Map map, JSONObject json)
    {
        fromJSON(map, json, null);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" }) 
    public static void fromJSON(Map map, JSONObject json, Class template)
    {
        if (json == null)
            return;
        for (Object key : json.keySet())
        {
            Object value = json.get(key);
            if (value instanceof JSONObject)
            {
                try
                {
                    Object v = template.newInstance();
                    ((IJSONAble)v).fromJSON((JSONObject)value);
                    map.put(key, v);
                }
                catch (Exception e)
                {
                    throw new IllegalStateException("Error trying to convert "+value+" to a "+template.getName(), e);
                }
            }
            else if (value instanceof JSONArray)
            {
                JSONArray arr = (JSONArray)value;
                if (isStringArray(arr))
                    map.put(key, toStringArray(arr));
                else if (isNumberArray(arr))
                    map.put(key, toIntArray(arr));
                else if (isObjectArray(arr))
                    map.put(key, toObjectArray(arr));
                else
                    throw new IllegalArgumentException("Can't handle array "+arr.toJSONString());
            }
            else
                map.put(key, value);
        }
    }

    public static JSONObject readJSONString(String json)
    {
        try
        {
            return (JSONObject)PARSER.parse(json);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    public static JSONObject readJSON(File file) throws IOException
    {
        return readJSONRaw(file);
    }

    public static JSONObject readJSON(String uri) throws IOException
    {
        if (uri.startsWith("file://"))
            return readJSON(new File(uri.substring(7)));
        else
            return readJSONRaw(uri);
    }

    private static JSONObject readJSONRaw(Object root)
    {
        JSONObject doc = null;
        try
        {
            Reader rdr = getReader(root);
            try
            {
                doc = (JSONObject)PARSER.parse(rdr);
                if (doc.containsKey("include"))
                {
                    List<String> includes = new ArrayList<>();
                    Object incs = doc.get("include");
                    doc.remove("include");
                    if (incs instanceof String)
                        includes.add((String)incs);
                    else if (incs instanceof JSONArray)
                        for (Object inc : ((JSONArray)incs))
                            includes.add((String)inc);
                    for (String incName : includes)
                    {
                        Object incFile = getRelative(root, incName);
                        JSONObject incModel = readJSONRaw(incFile);
                        merge(doc, incModel);
                    }
                }
            }
            catch (ParseException e)
            {
                System.err.println("Model file '"+root+"' does not have valid JSON data in it");
                e.printStackTrace();
                return null;
            }
            rdr.close();
        }
        catch (IOException e)
        {
            System.err.println("Error reading model file '"+root+"'");
            e.printStackTrace();
            return null;
        }
        return doc;
    }
    
    private static Reader getReader(Object root) throws IOException
    {
        if (root instanceof File)
            //return new FileReader((File)root);
            return new InputStreamReader(new FileInputStream((File)root), "utf-8");
        else if (root instanceof String)
        {
            String uri = (String)root;
            if (uri.startsWith("resource://"))
                return new InputStreamReader(ResourceUtils.loadSystemResourceStream(uri.substring(11)), "utf-8");
            URL u = new URL(uri);
            return new InputStreamReader(u.openStream(), "utf-8");
        }
        else
            throw new IllegalArgumentException("Unknown model root: "+root.getClass().getName());
    }
    
    private static Object getRelative(Object root, String rel) throws IOException
    {
        if (root instanceof File)
            return new File(((File)root).getParentFile(), rel);
        else if (root instanceof String)
        {
            String uri = (String)root;
            int o = uri.lastIndexOf('/');
            if (o >= 0)
                uri = uri.substring(0, o);
            uri = uri + "/"+rel;
            for (;;)
            {
                o = uri.indexOf("/../");
                if (o < 0)
                    break;
                int s = uri.lastIndexOf('/', o - 1);
                if (s < 0)
                    break;
                uri = uri.substring(0, s) + uri.substring(o + 3);
            }
            return uri;
        }
        else
            throw new IllegalArgumentException("Unknown model root: "+root.getClass().getName());
    }
    
    @SuppressWarnings("unchecked")
    public static void merge(Object target, Object src)
    {
        if ((target instanceof JSONArray) && (src instanceof JSONArray))
        {
            JSONArray aTarget = (JSONArray)target;
            JSONArray aSrc = (JSONArray)src;
            for (Object o : aSrc)
                aTarget.add(o);
        }
        else if ((target instanceof JSONObject) && (src instanceof JSONObject))
        {
            JSONObject oTarget = (JSONObject)target;
            JSONObject oSrc = (JSONObject)src;
            for (String key : oSrc.keySet())
            {
                if (oTarget.containsKey(key))
                    merge(oTarget.get(key), oSrc.get(key));
                else
                    oTarget.put(key, oSrc.get(key));
            }
        }
    }

    public static void writeJSON(File file, JSONObject json) throws IOException
    {
        OutputStreamWriter wtr = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
        wtr.write(json.toJSONString());
        wtr.close();
    }

    public static Object get(Object json, String path)
    {
        if (json == null)
            return null;
        if (path == null)
            return null;
        int o = path.indexOf(".");
        if (o < 0)
        {
            if (json instanceof JSONObject)
                return ((JSONObject)json).get(path);
            else if (json instanceof JSONArray)
                return ((JSONArray)json).get(IntegerUtils.parseInt(path));
            else
                return null;
        }
        Object parent = get(json, path.substring(0, o));
        if (parent == null)
            return null;
        return get(parent, path.substring(o+1));
    }

    public static JSONObject getObject(JSONObject json, String path)
    {
        return (JSONObject)get(json, path);        
    }

    public static JSONArray getArray(JSONObject json, String path)
    {
        return (JSONArray)get(json, path);        
    }

    public static String getString(JSONObject json, String path)
    {
        return (String)get(json, path);        
    }

    public static int getInt(JSONObject json, String path)
    {
        return IntegerUtils.parseInt(get(json, path));        
    }

    public static long getLong(JSONObject json, String path)
    {
        return LongUtils.parseLong(get(json, path));        
    }

    public static double getDouble(JSONObject json, String path)
    {
        return DoubleUtils.parseDouble(get(json, path));        
    }

    public static float getFloat(JSONObject json, String path)
    {
        return FloatUtils.parseFloat(get(json, path));        
    }

    public static boolean getBoolean(JSONObject json, String path)
    {
        return BooleanUtils.parseBoolean(get(json, path));        
    }
    
    public static Properties getProperties(JSONObject json, String path)
    {
        Properties props = new Properties();
        JSONObject jparameters = JSONUtils.getObject(json, path);
        if (jparameters != null)
        {
            for (String key : jparameters.keySet())
                if (jparameters.get(key) instanceof String)
                    props.put(key, jparameters.get(key));      
        }
        return props;
    }

    
    public static void getFromData(Object bean, JSONObject data, String[] keyNames)
    {
        if (bean == null)
            return;
        Class<?> toClass = bean.getClass();
        BeanInfo toClassInfo;
        try
        {
            toClassInfo = Introspector.getBeanInfo(toClass);
        }
        catch (IntrospectionException e1)
        {
            e1.printStackTrace();
            return;
        }
        PropertyDescriptor[] toProps = toClassInfo.getPropertyDescriptors();
        getFromDataInternal(bean, data, keyNames, toProps);
    }    
    public static void getFromData(Object bean, JSONObject data, String keyName)
    {
        if (bean == null)
            return;
        Class<?> toClass = bean.getClass();
        BeanInfo toClassInfo;
        try
        {
            toClassInfo = Introspector.getBeanInfo(toClass);
        }
        catch (IntrospectionException e1)
        {
            e1.printStackTrace();
            return;
        }
        PropertyDescriptor[] toProps = toClassInfo.getPropertyDescriptors();
        getFromDataInternal(bean, data, keyName, toProps);
    }

    private static void getFromDataInternal(Object bean,
            JSONObject data, String[] keyNames,
            PropertyDescriptor[] toProps)
    {
        for (String keyName : keyNames)
            getFromDataInternal(bean, data, keyName, toProps);
    }
    
    private static final int[] mIntArray = new int[0];
    
    private static void getFromDataInternal(Object bean,
            JSONObject data, String keyName,
            PropertyDescriptor[] toProps)
    {
        Object val = data.get(keyName);
        if (val == null)
            return;
        for (int j = 0; j < toProps.length; j++)
        {
            if (!keyName.equalsIgnoreCase(toProps[j].getName()))
                continue;
            Class<?> type = toProps[j].getPropertyType();
            try
            {
                if ((type == Integer.class) || (type == int.class))
                    toProps[j].getWriteMethod().invoke(bean, IntegerUtils.parseInt(val));
                else if ((type == Long.class) || (type == long.class))
                    toProps[j].getWriteMethod().invoke(bean, LongUtils.parseLong(val));
                else if ((type == Double.class) || (type == double.class))
                    toProps[j].getWriteMethod().invoke(bean, DoubleUtils.parseDouble(val));
                else if ((type == Float.class) || (type == float.class))
                    toProps[j].getWriteMethod().invoke(bean, FloatUtils.parseFloat(val));
                else if ((type == Short.class) || (type == short.class))
                    toProps[j].getWriteMethod().invoke(bean, ((Number)val).shortValue());
                else if ((type == Byte.class) || (type == byte.class))
                    toProps[j].getWriteMethod().invoke(bean, ((Number)val).byteValue());
                else if ((type == Boolean.class) || (type == boolean.class))
                    toProps[j].getWriteMethod().invoke(bean, BooleanUtils.parseBoolean(val));
                else if ((type == mIntArray.getClass()))
                    toProps[j].getWriteMethod().invoke(bean, IntegerUtils.toArray(StringUtils.toStringArray(val.toString(), ";")));
                else
                    toProps[j].getWriteMethod().invoke(bean, val.toString());
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
            {               
            }
            
        }
    }

    
    public static void setToData(Object bean, JSONObject data, String[] keyNames)
    {
        if (bean == null)
            return;
        Class<?> toClass = bean.getClass();
        BeanInfo toClassInfo;
        try
        {
            toClassInfo = Introspector.getBeanInfo(toClass);
        }
        catch (IntrospectionException e1)
        {
            e1.printStackTrace();
            return;
        }
        PropertyDescriptor[] toProps = toClassInfo.getPropertyDescriptors();
        setToDataInternal(bean, data, keyNames, toProps);
    }    
    public static void setToData(Object bean, JSONObject data, String keyName)
    {
        if (bean == null)
            return;
        Class<?> toClass = bean.getClass();
        BeanInfo toClassInfo;
        try
        {
            toClassInfo = Introspector.getBeanInfo(toClass);
        }
        catch (IntrospectionException e1)
        {
            e1.printStackTrace();
            return;
        }
        PropertyDescriptor[] toProps = toClassInfo.getPropertyDescriptors();
        setToDataInternal(bean, data, keyName, toProps);
    }

    private static void setToDataInternal(Object bean,
            JSONObject data, String[] keyNames,
            PropertyDescriptor[] toProps)
    {
        for (String keyName : keyNames)
            setToDataInternal(bean, data, keyName, toProps);
    }
    
    private static void setToDataInternal(Object bean,
            JSONObject data, String keyName,
            PropertyDescriptor[] toProps)
    {
        for (int j = 0; j < toProps.length; j++)
        {
            if (!keyName.equalsIgnoreCase(toProps[j].getName()))
                continue;
            Class<?> type = toProps[j].getPropertyType();
            try
            {
                Object val = toProps[j].getReadMethod().invoke(bean);
                if (val == null)
                    continue;
                String sval = String.valueOf(val);
                if (StringUtils.isTrivial(sval))
                    continue;
                if ((type == Integer.class) || (type == int.class))
                    data.put(keyName, val);
                else if ((type == Long.class) || (type == long.class))
                    data.put(keyName, val);
                else if ((type == Double.class) || (type == double.class))
                    data.put(keyName, val);
                else if ((type == Float.class) || (type == float.class))
                    data.put(keyName, val);
                else if ((type == Short.class) || (type == short.class))
                    data.put(keyName, val);
                else if ((type == Byte.class) || (type == byte.class))
                    data.put(keyName, val);
                else if (type == mIntArray.getClass())
                    data.put(keyName, StringUtils.fromStringArray(IntegerUtils.toArray((int[])val), ";"));
                else
                    data.put(keyName, sval);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
            {               
            }
            
        }
    }

    public static List<JSONObject> arrayToObjects(JSONArray a)
    {
        List<JSONObject> objs = new ArrayList<>();
        for (Object o : a)
            objs.add((JSONObject)o);
        return objs;
    }
    
    public static boolean isTypeArray(JSONArray arr, Class<?> type)
    {
        for (Object o : arr)
            if ((o == null) || !type.isAssignableFrom(o.getClass()))
                return false;
        return true;
    }
    
    public static boolean isStringArray(JSONArray arr)
    {
        return isTypeArray(arr, String.class);
    }
    
    public static boolean isNumberArray(JSONArray arr)
    {
        return isTypeArray(arr, Number.class);
    }
    
    public static boolean isObjectArray(JSONArray arr)
    {
        return isTypeArray(arr, JSONObject.class);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(JSONArray arr, T[] a)
    {
        for (int i = 0; i < arr.size(); i++)
            a[i] = (T)arr.get(i);
        return a;
    }
    
    public static String[] toStringArray(JSONArray arr)
    {
        if (arr == null)
            return null;
        return toArray(arr, new String[arr.size()]);
    }
    
    public static int[] toIntArray(JSONArray arr)
    {
        return IntegerUtils.toArray(arr.toArray());
    }
    
    public static JSONObject[] toObjectArray(JSONArray arr)
    {
        return toArray(arr, new JSONObject[arr.size()]);
    }
    
    public static String toFormattedString(JSONObject json)
    {
        StringBuffer sb = new StringBuffer();
        if (json != null)
            doFormattedString(sb, 0, json);
        return sb.toString();
    }
    
    private static void doFormattedString(StringBuffer sb, int indent, Object json)
    {
        if (json instanceof JSONObject)
        {
            JSONObject obj = (JSONObject)json;
            String[] keys = obj.keySet().toArray(new String[0]);
            Arrays.sort(keys);
            sb.append("{\n");
            for (int i = 0; i < keys.length; i++)
            {
                String key = keys[i];
                indent(sb, indent+2);
                sb.append("\""+key+"\":");
                doFormattedString(sb, indent+4, obj.get(key));
                if (i + 1 < keys.length)
                    sb.append(",");
                sb.append("\n");
            }
            indent(sb, indent);
            sb.append("}");
        }
        else if (json instanceof JSONArray)
        {
            JSONArray arr = (JSONArray)json;
            sb.append("[ ");
            for (int i = 0; i < arr.size(); i++)
            {
                Object val = arr.get(i);
                doFormattedString(sb, indent+4, val);
                if (i + 1 < arr.size())
                    sb.append(", ");
            }
            sb.append(" ]");
        }
        else if (json == null)
            sb.append("null");
        else if (json instanceof String)
            sb.append("\""+json.toString()+"\"");
        else
            sb.append(json.toString());
    }
    
    private static void indent(StringBuffer sb, int indent)
    {
        for (int i = 0; i < indent; i++)
            sb.append(" ");
    }
    
    @SuppressWarnings("unchecked")
    public static Object deepCopy(Object ori)
    {
        if (ori instanceof JSONObject)
        {
            JSONObject o = (JSONObject)ori;
            JSONObject copy = new JSONObject();
            for (String key : o.keySet())
                copy.put(key, deepCopy(o.get(key)));
            return copy;
        }
        else if (ori instanceof JSONArray)
        {
            JSONArray a = (JSONArray)ori;
            JSONArray copy = new JSONArray();
            for (int i = 0; i < a.size(); i++)
                copy.add(deepCopy(a.get(i)));
            return copy;
        }
        else
            return ori;
    }
    
    public static void replace(JSONObject json, CharSequence target, CharSequence replacement)
    {
        for (String key : json.keySet().toArray(new String[0]))
        {
            Object val = json.get(key);
            if (val instanceof String)
            {
                val = ((String)val).replace(target, replacement);
                json.put(key,  val);
            }
            else if (val instanceof JSONObject)
                replace((JSONObject)val, target, replacement);
            else if (val instanceof JSONArray)
                replace((JSONArray)val, target, replacement);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void replace(JSONArray json, CharSequence target, CharSequence replacement)
    {
        for (int i = 0; i < json.size(); i++)
        {
            Object val = json.get(i);
            if (val instanceof String)
            {
                val = ((String)val).replace(target, replacement);
                json.remove(i);
                json.add(i, val);
            }
            else if (val instanceof JSONObject)
                replace((JSONObject)val, target, replacement);
            else if (val instanceof JSONArray)
                replace((JSONArray)val, target, replacement);
        }
    }

    @SuppressWarnings("unchecked")
    public static JSONArray makeArray(Object... values)
    {
        JSONArray arr = new JSONArray();
        for (Object v : values)
            arr.add(v);
        return arr;
    }
}
