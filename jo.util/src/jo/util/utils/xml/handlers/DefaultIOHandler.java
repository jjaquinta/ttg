package jo.util.utils.xml.handlers;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.StringTokenizer;

import jo.util.utils.xml.IXMLIOHandler;
import jo.util.utils.xml.IXMLIOTreeRefResolver;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLIOTreeRef;
import jo.util.utils.xml.XMLIOUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class DefaultIOHandler implements IXMLIOHandler, IXMLIOTreeRefResolver
{
    public Class<?> getHandledClass()
    {
        return Object.class;
    }

    public String getTagName()
    {
        return "object";
    }
    
    public Node toXML(Node parent, Object obj, Map<Object,Object> map)
    {
        Node n = XMLEditUtils.addElement(parent, getTagName());
        String className = obj.getClass().getName();
        XMLEditUtils.addAttribute(n, "class", className);
        if (className.startsWith("["))
            addArrayValues(n, className, obj);
        else
        {
            try
            {
                addProps(n, obj, map);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return n;
    }
    
    private void addArrayValues(Node n, String className, Object obj)
    {
        if (className.equals("[I"))
        {
            int[] val = (int[])obj;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < val.length; i++)
            {
                if (sb.length() > 0)
                    sb.append(",");
                sb.append(val[i]);
            }
            Text text = n.getOwnerDocument().createTextNode(sb.toString());
            n.appendChild(text);
        }
        else if (className.equals("[[I"))
        {
            int[][] val = (int[][])obj;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < val.length; i++)
            {
                if (sb.length() > 0)
                    sb.append(";");
                for (int j = 0; j < val[i].length; j++)
                {
                    if (sb.length() > 0)
                        sb.append(",");
                    sb.append(val[i][j]);
                }
            }
            Text text = n.getOwnerDocument().createTextNode(sb.toString());
            n.appendChild(text);
        }
        else if (className.equals("[Z"))
        {
            boolean[] val = (boolean[])obj;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < val.length; i++)
            {
                if (sb.length() > 0)
                    sb.append(",");
                sb.append(val[i]);
            }
            Text text = n.getOwnerDocument().createTextNode(sb.toString());
            n.appendChild(text);
        }
        else
        {
            System.out.println("WARNING: unsupported array type: "+className);
        }
    }
    
    protected void addProps(Node n, Object obj, Map<Object,Object> map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IntrospectionException
    {
        BeanInfo info = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] props = info.getPropertyDescriptors();
        for (int i = 0; i < props.length; i++)
        {
            if (skipProp(props[i]))
                continue;
            if (XMLIOUtils.isSimpleType(props[i].getPropertyType()))
                addSimpleProp(n, obj, props[i]);
            else
                addComplicatedProp(n, obj, map, props[i]);
        }
    }
    
    protected boolean skipProp(PropertyDescriptor prop)
    {
        if ((prop.getWriteMethod() == null) || (prop.getReadMethod() == null))
            return true;
        return false;
    }
    
    public static void addComplicatedProp(Node n, Object obj, Map<Object,Object> map, PropertyDescriptor prop) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        String key = prop.getName();
        Object val = prop.getReadMethod().invoke(obj, new Object[0]);
        if (val == null)
            return;
        Node pn = XMLEditUtils.addElement(n, "property");
        XMLEditUtils.addAttribute(pn, "name", key);
        if (val != null)
            XMLIOUtils.toXML(pn, val, map);
    }
    
    public static void addSimpleProp(Node n, Object obj, PropertyDescriptor prop) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        String key = prop.getName();
        Object val = prop.getReadMethod().invoke(obj, new Object[0]);
        if (val != null)
            XMLEditUtils.addAttribute(n, key, val.toString());
    }

    public Object fromXML(Node bean, Map<Object,Object> map, ClassLoader loader)
    {
        Object obj = null;
        try
        {
            String className = XMLUtils.getAttribute(bean, "class");
            if (className.startsWith("["))
                obj = extractArray(className, bean);
            else
            {
                Class<?> objClass = loader.loadClass(className);
                obj = objClass.newInstance();
                extractProps(obj, bean, map, loader);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return obj;
    }
    
    private Object extractArray(String className, Node bean)
    {
        String txt = XMLUtils.getText(bean);
        if (className.equals("[[I"))
        {
            StringTokenizer st1 = new StringTokenizer(txt, ";");
            int[][] val = new int[st1.countTokens()][];
            for (int i = 0; st1.hasMoreTokens(); i++)
            {
                String txt2 = st1.nextToken();
                StringTokenizer st2 = new StringTokenizer(txt2, ",");
                val[i] = new int[st2.countTokens()];
                for (int j = 0; st2.hasMoreTokens(); j++)
                    val[i][j] = Integer.parseInt(st2.nextToken());
            }
            return val;
        }
        else if (className.equals("[I"))
        {
            StringTokenizer st = new StringTokenizer(txt, ",");
            int[] val = new int[st.countTokens()];
            for (int i = 0; st.hasMoreTokens(); i++)
                val[i] = Integer.parseInt(st.nextToken());
            return val;
        }
        else if (className.equals("[Z"))
        {
            StringTokenizer st = new StringTokenizer(txt, ",");
            boolean[] val = new boolean[st.countTokens()];
            for (int i = 0; st.hasMoreTokens(); i++)
                val[i] = Boolean.parseBoolean(st.nextToken());
            return val;
        }
        System.out.println("WARNING: unsupported array type: "+className);
        return null;
    }

    private void extractProps(Object obj, Node bean, Map<Object,Object> map, ClassLoader loader) throws IntrospectionException, IllegalAccessException, InvocationTargetException, SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException
    {
        BeanInfo info = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] props = info.getPropertyDescriptors();
        // complicated props
        for (Node pn : XMLUtils.findNodes(bean, "property"))
        {
            String key = XMLUtils.getAttribute(pn, "name");
            Object val = XMLIOUtils.fromXML(XMLUtils.findFirstNode(pn, "*"), map, loader);
            for (int j = 0; j < props.length; j++)
                if (props[j].getName().equals(key))
                {
                    if (val instanceof XMLIOTreeRef)
                    {
                        XMLIOTreeRef ref = (XMLIOTreeRef)val;
                        ref.setArg1(props[j]);
                        ref.setArg2(obj);
                        ref.setResolver(this);
                    }
                    else
                    {
                        Object[] args = new Object[] { val };
                        if (!props[j].getPropertyType().isAssignableFrom(val.getClass()))
                            System.err.println("Writing a "+val.getClass().getName()+", to property "+props[j].getName()+", type "+props[j].getPropertyType().getName());
                        props[j].getWriteMethod().invoke(obj, args);
                    }
                    break;
                }
        }
        // simple props
        for (int i = 0; i < props.length; i++)
        {
            if (!XMLIOUtils.isSimpleType(props[i].getPropertyType()))
                continue;
            String key = props[i].getName();
            if (XMLUtils.hasAttribute(bean, key))
            {
                Object val = XMLUtils.getAttribute(bean, key);
                Object[] args = new Object[] { convert(val, props[i].getPropertyType()) };
                try
                {
                    props[i].getWriteMethod().invoke(obj, args);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private Object convert(Object from, Class<?> to) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        if (from.getClass() == to)
            return from;
        String toName = to.getName();
        if (toName.equals("long"))
            return new Long(from.toString());
        if (toName.equals("int"))
            return new Integer(from.toString());
        if (toName.equals("boolean"))
            return new Boolean(from.toString());
        if (toName.equals("double"))
            return new Double(from.toString());
        if (toName.equals("float"))
            return new Float(from.toString());
        if (toName.equals("char"))
            return new Character(from.toString().charAt(0));
        Class<?>[] profile = new Class<?>[] { String.class };
        Constructor<?> c = to.getConstructor(profile);
        return c.newInstance(new Object[] { from.toString() });
    }

    public void resolve(XMLIOTreeRef ref, Object val)
    {
        PropertyDescriptor prop = (PropertyDescriptor)ref.getArg1();
        Object obj = ref.getArg2();
        //System.out.println("Fixup ref: "+ref.getKey()+", "+obj+"."+prop.getName()+"<-"+val);
        Object[] args = new Object[] { val };
        try
        {
            prop.getWriteMethod().invoke(obj, args);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
