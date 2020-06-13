/*
 * Created on Oct 25, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import jo.util.utils.obj.BooleanUtils;
import jo.util.utils.obj.StringUtils;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;

public class MapUtils {
    
    private static Logger logger = Logger.getLogger(MapUtils.class.getPackage().getName());     

    public static boolean isPropertyEqual(Properties property1, Properties property2)
    {
        if (property1 == null && property2 == null)
            return true;
        if ((property1 == null && property2 != null) || (property2 == null && property1 != null))
            return false;
        if (property1.isEmpty() && property2.isEmpty())
            return true;
        if (property1.size() != property2.size())
        {
            logger.finer("Size1:" +property1.size());           
            logger.finer("Size2:" +property2.size());           
            return false; 
        }
        for (Object key : property1.keySet())
        {
            Object value1 = property1.get(key);
            Object value2 = property2.get(key);
            if (value2 == null)
            {
                if (!StringUtils.isTrivial((String)value1))
                {
                    logger.finest("Key:"+ key+" is not in original property.");
                    logger.finest("Key:" +key);
                    logger.finest("Value1=" +value1);
                    logger.finest("Value2 is null");
                    return false;
                }
            }
            else if (!value2.equals(value1))
            {
                logger.finest("Values are not equal:");
                logger.finest("Key:" + key.toString());
                logger.finest("Value1:" + value1.toString());
                logger.finest("Value2:" + value2.toString());
                return false;
            }
        }
        return true;
    }
    
    public static void store(Map<Object, Object> metadata , Properties source)
    {
        store(metadata, source, "");
    }
    
    public static void store(Map<Object, Object> metadata, Properties source, String prefix)
    {
        logger.fine("Storing "+prefix+" properties into metadata.");
        for (Enumeration<Object> e = source.keys(); e.hasMoreElements(); ) 
        {
            String key = (String)e.nextElement();
            String value = (String)source.getProperty(key);
            
            if(!key.equals("orig_Text"))
                logger.finest("Storing Key:" +prefix+key+":: Value:"+value+":");
            metadata.put(prefix+key, source.getProperty(key));
        }
    }

    public static Properties load(Properties source)
    {
        return load(source, "");
    }
    
    public static Properties load(Map<Object, Object> metadata, String prefix)
    {
        logger.fine("Loading "+prefix+" properties from metadata.");

        Properties targetProperty = new Properties();   
        for (Object key : metadata.keySet())
        {
            Object val = metadata.get(key);
            if (!(key instanceof String) || !(val instanceof String))
                continue;
            String k = (String)key;
            if (!StringUtils.isTrivial(prefix) && !k.startsWith(prefix))
                continue;

            String id = k.substring(prefix.length(), k.length());
            if (val instanceof String)
            {
                if(!key.equals("orig_Text"))
                    logger.finest("Loading Key:"+key+": Value:"+val);
                targetProperty.put(id, (String)val);
            }
        }
        return targetProperty;
    }
    
    public static void clear(Map<Object, Object> metadata)
    {
        clear(metadata, "");
    }
    
    public static void clear(Map<Object, Object> metadata, String prefix)
    {
        for (Object key : metadata.keySet().toArray())
        {
            if(key instanceof String)
            {
                if( ((String)key).startsWith(prefix))
                    metadata.remove(key);
            }
        }
    }

    public static void dump(Map<?, ?> m, String prefix)
    {
        dump(m, prefix, System.out);
    }

    public static void dump(Map<?, ?> m, String prefix, OutputStream os)
    {
        try
        {
            Object[] keys = m.keySet().toArray();
            Arrays.sort(keys);
            for (Object key : keys)
            {
                Object v = m.get(key);
                if (v == null)
                {
                    os.write((prefix+key+"=<null>").getBytes("utf8"));
                    os.write("\r\n".getBytes("utf8"));
                }
                else
                {
                    String val = v.toString();
                    if (val.length() > 60)
                        val = val.substring(0, 60)+"...";
                    int o = val.indexOf('\n');
                    if (o >= 0)
                        val = val.substring(0, o)+"...";
                    os.write((prefix+key+"='"+val+"'").getBytes("utf8"));
                    os.write("\r\n".getBytes("utf8"));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void copy(Map dest, Map src)
    {
        for (Object k : src.keySet())
        {
            Object v = src.get(k);
            dest.put(k, v);
        }
    }    

    public static void toXML(Map<?,?> p, String nodeName, Node parent)
    {
        Node params = XMLEditUtils.addElement(parent, nodeName);
        for (Object k : p.keySet())
        {
            Object v = p.get(k);
            if (v == null)
                continue;
            if (!(k instanceof String))
                logger.warning("Attempt to write map to XML with key of type '"+k.getClass().getName()+"'");
            Node param = XMLEditUtils.addElement(params, "param");
            XMLEditUtils.addAttribute(param, "name", k.toString());
            XMLEditUtils.addAttribute(param, "type", v.getClass().getName());
            if (v instanceof String[])
                for (int i = 0; i < ((String[])v).length; i++)
                    XMLEditUtils.addTextTag(param, "element", escapeString(((String[])v)[i]));
            else if (v instanceof Properties)
                for (Object pName : ((Properties)v).keySet())
                {
                    String val = ((Properties)v).getProperty((String)pName);
                    if (val != null)
                    {
                        Node prop = XMLEditUtils.addElement(param, "property");
                        XMLEditUtils.addAttribute(prop, "key", (String)pName);
                        XMLEditUtils.addAttribute(prop, "value", escapeString(val));
                    }
                }
            else if (v instanceof String)
            {
                XMLEditUtils.addText(param, escapeString((String)v));
            }
            else if (v instanceof Boolean)
                XMLEditUtils.addText(param, escapeString(v.toString()));
            else if (v instanceof Long)
                XMLEditUtils.addText(param, escapeString(v.toString()));
            else if (v instanceof Integer)
                XMLEditUtils.addText(param, escapeString(v.toString()));
            else if (v instanceof Map<?,?>)
                toXML((Map<?,?>)v, "map", param);
            else
            {
                logger.warning("Attempt to write map to XML with value of type '"+v.getClass().getName()+"' (key="+k+")");
                XMLEditUtils.addText(param, v.toString());
            }
        }
    }

    public static void fromXML(Map<Object,Object> p, Node params)
    {
        if (params == null)
            return;
        for (Node param : XMLUtils.findNodes(params, "param"))
        {
            String name = XMLUtils.getAttribute(param, "name");
            String type = XMLUtils.getAttribute(param, "type");
            if (type.equals("[Ljava.lang.String;"))
            {
                List<String> strings = new ArrayList<String>();
                for (Node element : XMLUtils.findNodes(param, "element"))
                    strings.add(unescapeString(XMLUtils.getText(element)));
                p.put(name, strings.toArray(new String[0]));
            }
            else if (type.equals("java.util.Properties"))
            {
                Properties props = new Properties();
                for (Node prop : XMLUtils.findNodes(param, "property"))
                {
                    String key = XMLUtils.getAttribute(prop, "key");
                    String val = unescapeString(XMLUtils.getAttribute(prop, "value"));
                    props.put(key, val);
                }
                p.put(name, props);
            }
            else if (type.equals("java.util.HashMap"))
            {
                Map<Object,Object> map = new HashMap<Object,Object>();
                Node m = XMLUtils.findFirstNode(param, "map");
                fromXML(map, m);
                p.put(name, map);
            }
            else if (type.equals("java.lang.String"))
                p.put(name, unescapeString(XMLUtils.getText(param)));
            else if (type.equals("java.lang.Boolean"))
                p.put(name, BooleanUtils.parseBoolean(XMLUtils.getText(param)));
            else if (type.equals("java.lang.Integer"))
                p.put(name, Integer.parseInt(XMLUtils.getText(param)));
            else if (type.equals("java.lang.Long"))
                p.put(name, Long.parseLong(XMLUtils.getText(param)));
            else
                throw new IllegalArgumentException("I don't know how to read a "+type);
        }
    }

    private static String escapeString(String txt)
    {
        txt = txt.replace("\r", "&xd;");
        txt = txt.replace("\n", "&xa;");
        return txt;
    }

    private static String unescapeString(String txt)
    {
        txt = txt.replace("&xd;", "\r");
        txt = txt.replace("&xa;", "\n");
        return txt;
    }

    public static Object getKey(Map<?,?> map, Object val)
    {
        for (Object key : map.keySet())
        {
            Object v = map.get(key);
            if (v == val)
                return key;
        }
        return null;
    }
    
    public static Properties load(File pfile)
    {
        Properties props = new Properties();
        try
        {
            props.load(new FileInputStream(pfile));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return props;
    }
    
    public static void save(Properties props, File pfile)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(pfile);
            props.store(fos, "");
            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}