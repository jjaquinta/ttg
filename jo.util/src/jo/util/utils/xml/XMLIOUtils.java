package jo.util.utils.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jo.util.beans.Bean;
import jo.util.utils.xml.handlers.ArrayIOHandler;
import jo.util.utils.xml.handlers.ArrayListIOHandler;
import jo.util.utils.xml.handlers.DefaultIOHandler;
import jo.util.utils.xml.handlers.HashMapIOHandler;
import jo.util.utils.xml.handlers.HashSetIOHandler;
import jo.util.utils.xml.handlers.Int2DArrayIOHandler;
import jo.util.utils.xml.handlers.Object2DArrayIOHandler;
import jo.util.utils.xml.handlers.RandomIOHandler;
import jo.util.utils.xml.handlers.SimpleIOHandler;

import org.w3c.dom.Node;

public class XMLIOUtils
{
    private static List<IXMLIOHandler> HANDLERS = new ArrayList<IXMLIOHandler>();
    static
    {
        addHandler(new DefaultIOHandler());
        addHandler(new Int2DArrayIOHandler());
        addHandler(new ArrayIOHandler());
        addHandler(new ArrayListIOHandler());
        addHandler(new HashMapIOHandler());
        addHandler(new RandomIOHandler());
        addHandler(new HashSetIOHandler());
        addHandler(new Object2DArrayIOHandler());
    }
    
    public static void addHandler(IXMLIOHandler handler)
    {
        Class<?> handlerClass = handler.getHandledClass();
        for (int i = 0; i < HANDLERS.size(); i++)
        {
            IXMLIOHandler h = (IXMLIOHandler)HANDLERS.get(i);
            Class<?> hClass = h.getHandledClass();
            if (hClass.isAssignableFrom(handlerClass))
            {
                HANDLERS.add(i, handler);
                return;
            }
        }
        HANDLERS.add(handler);
    }
    
    public static IXMLIOHandler getHandler(Object obj)
    {
        Class<?> objClass = obj.getClass();
//        if (objClass.isArray())
//            System.out.println("Array! "+objClass.getCanonicalName());
        for (IXMLIOHandler h  : HANDLERS)
        {
            Class<?> hClass = h.getHandledClass();
            if (hClass.isAssignableFrom(objClass))
                return h;
        }
        return null;
    }
    
    public static IXMLIOHandler getHandler(String tagName)
    {
        for (IXMLIOHandler h  : HANDLERS)
        {
            if (h.getTagName().equals(tagName))
                return h;
        }
        return null;
    }

    private static Set<Class<?>> SIMPLE_TYPES = new HashSet<Class<?>>();
    static
    {
        addSimpleType(String.class);
        addSimpleType(Integer.class);
        addSimpleType(Boolean.class);
        addSimpleType(Double.class);
        addSimpleType(Long.class);
        addSimpleType(Float.class);
        addSimpleType(Character.class);
        addSimpleType(int.class);
        addSimpleType(boolean.class);
        addSimpleType(double.class);
        addSimpleType(long.class);
        addSimpleType(float.class);
        addSimpleType(char.class);
    };
    
    public static void addSimpleType(Class<?> c)
    {
        SIMPLE_TYPES.add(c);
        addHandler(new SimpleIOHandler(c));
    }
    
    public static boolean isSimpleType(Class<?> c)
    {
        return SIMPLE_TYPES.contains(c);
    }
    
    public static Node toXML(Node parent, Bean b)
    {
        return toXML(parent, b, new HashMap<Object,Object>());
    }

    public static Node toXML(Node parent, Object obj, Map<Object,Object> map)
    {
        if (!isPolymorphic(obj))
        {
            Integer ref = (Integer)map.get(obj);
            if (ref != null)
            {            
                Node n = XMLEditUtils.addElement(parent, "reference");
                XMLEditUtils.addAttribute(n, "ref", ref.toString());
                //System.out.println("ref:"+ref+"<-"+obj.getClass().getName()+" "+parent.getNodeName());
                return n;
            }
        }
        Integer ref;
        if (map.containsKey(obj))
            ref = (Integer)map.get(obj);
        else
        {
            ref = new Integer(map.size());
            map.put(obj, ref);
        }
        //System.out.println(ref+"->"+obj+" ("+obj.getClass().getName()+")");
        IXMLIOHandler h = getHandler(obj);
        if (h == null)
            return null;
        Node ret = h.toXML(parent, obj, map);
        XMLEditUtils.addAttribute(ret, "ref", ref.toString());
        return ret;
    }
    
    public static Object fromXML(Node parent, ClassLoader loader)
    {
        Map<Object,Object> map = new HashMap<Object, Object>();
        Object ret = fromXML(parent, map, loader);
        @SuppressWarnings("unchecked")
        List<XMLIOTreeRef> refs = (List<XMLIOTreeRef>)map.get("refs");
        if (refs != null)
            for (XMLIOTreeRef ref : refs)
            {
                Object obj = map.get(ref.getKey());
                // hack to get around arraylist problem
                if ((obj instanceof ArrayList<?>) && (((ArrayList<?>)obj).size() == 0))
                    obj = new ArrayList<Object>();
                ref.getResolver().resolve(ref, obj);
            }
        return ret;
    }

    public static Object fromXML(Node bean, Map<Object,Object> map, ClassLoader loader)
    {
        String key = XMLUtils.getAttribute(bean, "ref");
        if (bean.getNodeName().equals("reference"))
        {
            Object ret = map.get(key);
            if (ret == null)
            {
                XMLIOTreeRef ref = new XMLIOTreeRef(key);
                @SuppressWarnings("unchecked")
                List<XMLIOTreeRef> refs = (List<XMLIOTreeRef>)map.get("refs");
                if (refs == null)
                {
                    refs = new ArrayList<XMLIOTreeRef>();
                    map.put("refs", refs);
                }
                refs.add(ref);
                return ref;
            }
            else
            {
                // hack to get around arraylist problem
                if ((ret instanceof ArrayList<?>) && (((ArrayList<?>)ret).size() == 0))
                    ret = new ArrayList<Object>();
                return ret;
            }
        }
        IXMLIOHandler h = getHandler(bean.getNodeName());
        Object ret = h.fromXML(bean, map, loader);
        map.put(key, ret);
        //System.out.println(key+"->"+ret.toString());
        return ret;
    }
    
    private static boolean isPolymorphic(Object obj)
    {
        if (obj instanceof Collection)
            return true;
        if (obj instanceof Map)
            return true;
        return false;
    }
}
