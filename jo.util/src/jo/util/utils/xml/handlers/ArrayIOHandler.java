package jo.util.utils.xml.handlers;

import java.util.Iterator;
import java.util.Map;

import jo.util.utils.xml.IXMLIOHandler;
import jo.util.utils.xml.IXMLIOTreeRefResolver;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLIOTreeRef;
import jo.util.utils.xml.XMLIOUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;

public class ArrayIOHandler implements IXMLIOHandler, IXMLIOTreeRefResolver
{
    private Class<?> mOurClass;
    
    public ArrayIOHandler()
    {
        Object[] demo = new Object[0];
        mOurClass = demo.getClass();
    }
    
    public Class<?> getHandledClass()
    {
        return mOurClass;
    }
    
    public Node toXML(Node parent, Object obj, Map<Object,Object> map)
    {
        Object[] array = (Object[])obj;
        Node n = XMLEditUtils.addElement(parent, getTagName());
        XMLEditUtils.addAttribute(n, "size", String.valueOf(array.length));
        for (int i = 0; i < array.length; i++)
        {
            Object child = array[i];
            XMLIOUtils.toXML(n, child, map);
        }
        return n;
    }

    public Object fromXML(Node bean, Map<Object,Object> map, ClassLoader loader)
    {
        int size = Integer.parseInt(XMLUtils.getAttribute(bean, "size"));
        Object[] list = new Object[size];
        int idx = 0;
        for (Iterator<Node> i = XMLUtils.findNodes(bean, "*").iterator(); i.hasNext(); idx++)
        {
            Node n = i.next();
            Object val = XMLIOUtils.fromXML(n, map, loader);
            if (val instanceof XMLIOTreeRef)
            {
                XMLIOTreeRef ref = (XMLIOTreeRef)val;
                ref.setArg1(list);
                ref.setArg2(new Integer(idx));
                ref.setResolver(this);
            }
            list[idx] = val;
        }
        return list;
    }

    public String getTagName()
    {
        return "array";
    }

    public void resolve(XMLIOTreeRef ref, Object val)
    {
        Object[] list = (Object[])ref.getArg1();
        Integer off = (Integer)ref.getArg2();
        //System.out.println("Fixup: ref "+ref.getKey()+", array #"+off+"<-"+val);
        list[off] = val;
    }

}
