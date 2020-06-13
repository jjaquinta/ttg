/*
 * Created on Jun 27, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.util.beans.Bean;
import jo.util.utils.BeanUtils;

import org.w3c.dom.Node;

public class XMLBeanUtils
{
    public static Node toXML(Node parent, Bean b)
    {
        return toXML(parent, b, new HashMap<Object,Object>());
    }
    
    public static Node toXML(Node parent, Bean b, Map<Object,Object> map)
    {
        Node n = XMLEditUtils.addElement(parent, "bean");
        Integer ref = (Integer)map.get(b);
        if (ref != null)
        {            
            XMLEditUtils.addAttribute(parent, "ref", ref.toString());
            return n;
        }
        ref = new Integer(map.size());
        map.put(b, ref);
        //System.out.println(ref+"->"+b+" ("+b.getClass().getName()+")");
        XMLEditUtils.addAttribute(n, "class", b.getClass().getName());
        XMLEditUtils.addAttribute(n, "ref", ref.toString());
        List<String> props = BeanUtils.getProperties(b);
        for (String key : props)
        {
            Object val = BeanUtils.get(b, key);
            Node pn = XMLEditUtils.addElement(n, "property");
            XMLEditUtils.addAttribute(pn, "name", key);
            if (val != null)
            {
                if (val instanceof Bean)
                {
                    XMLEditUtils.addAttribute(pn, "value", "#bean");
                    toXML(pn, (Bean)val, map);
                }
                else if (val instanceof Bean[])
                {
                    Bean[] list = (Bean[])val;
                    XMLEditUtils.addAttribute(pn, "value", "#beanarray");
                    for (int j = 0; j < list.length; j++)
                    {
                        Bean obj = list[j];
                        toXML(pn, (Bean)obj, map);
                    }
                }
                else if (val instanceof boolean[])
                {
                    boolean[] list = (boolean[])val;
                    XMLEditUtils.addAttribute(pn, "value", "#booleanarray");
                    for (int j = 0; j < list.length; j++)
                    {
                        Object obj = list[j];
                        Node elem = XMLEditUtils.addElement(pn, "object");
                        XMLEditUtils.addAttribute(elem, "class", obj.getClass().getName());
                        XMLEditUtils.addAttribute(elem, "value", obj.toString());
                    }
                }
                else if (val instanceof int[])
                {
                    int[] list = (int[])val;
                    XMLEditUtils.addAttribute(pn, "value", "#intarray");
                    for (int j = 0; j < list.length; j++)
                    {
                        Object obj = list[j];
                        Node elem = XMLEditUtils.addElement(pn, "object");
                        XMLEditUtils.addAttribute(elem, "class", obj.getClass().getName());
                        XMLEditUtils.addAttribute(elem, "value", obj.toString());
                    }
                }
                else if (val instanceof List)
                {
                    List<?> list = (List<?>)val;
                    XMLEditUtils.addAttribute(pn, "value", "#arraylist");
                    for (Iterator<?> j = list.iterator(); j.hasNext(); )
                    {
                        Object obj = j.next();
                        if (obj instanceof Bean)
                            toXML(pn, (Bean)obj, map);
                        else
                        {
                            Node elem = XMLEditUtils.addElement(pn, "object");
                            XMLEditUtils.addAttribute(elem, "class", obj.getClass().getName());
                            XMLEditUtils.addAttribute(elem, "value", obj.toString());
                        }
                    }
                }
                else
                    XMLEditUtils.addAttribute(pn, "value", val.toString());
            }
        }
        return n;
    }
    
    public static Node toXML(Node parent, Collection<Bean> beans)
    {
        Node root = XMLEditUtils.addElement(parent, "beanContainer");
        for (Bean b  : beans)
            toXML(root, b);
        return root;
    }
    
    public static Bean fromXML(Node bean, ClassLoader loader) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = XMLUtils.getAttribute(bean, "class");
        Class<?> c = loader.loadClass(className);
        Bean b = (Bean)c.newInstance();
        List<Node> props = XMLUtils.findNodes(bean, "property");
        for (Node prop : props)
        {
            String key = XMLUtils.getAttribute(prop, "name");
            Object val = XMLUtils.getAttribute(prop, "value");
            if (val.equals("#bean"))
            {
                val =  fromXML(XMLUtils.findFirstNode(prop, "bean"), loader);
            }
            else if (val.equals("#beanarray"))
            {
                List<Node> objects = XMLUtils.findNodes(prop, "bean");
                Bean[] list = new Bean[objects.size()];
                for (int j = 0; j < list.length; j++)
                    list[j] = fromXML((Node)objects.get(j), loader);
                val = list;   
            }
            else if (val.equals("#booleanarray"))
            {
                List<Node> objects = XMLUtils.findNodes(prop, "object");
                boolean[] list = new boolean[objects.size()];
                for (int j = 0; j < list.length; j++)
                    list[j] = XMLUtils.getAttribute((Node)objects.get(j), "value").equals("true");
                val = list;   
            }
            else if (val.equals("#intarray"))
            {
                List<Node> objects = XMLUtils.findNodes(prop, "object");
                int[] list = new int[objects.size()];
                for (int j = 0; j < list.length; j++)
                    list[j] = Integer.parseInt(XMLUtils.getAttribute((Node)objects.get(j), "value"));
                val = list;   
            }
            else if (val.equals("#arraylist"))
            {
                
            }
            BeanUtils.set(b, key, val);
        }
        return b;
    }
    
    public static List<Bean> fromXMLContainer(Node beanContainer, ClassLoader loader) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        List<Bean> ret = new ArrayList<Bean>();
        List<Node> beans = XMLUtils.findNodes(beanContainer, "bean");
        for (Node bean : beans)
            ret.add(fromXML(bean, loader));
        return ret;
    }
}
