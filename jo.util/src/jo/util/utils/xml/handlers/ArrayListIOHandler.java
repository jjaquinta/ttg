package jo.util.utils.xml.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jo.util.utils.xml.IXMLIOHandler;
import jo.util.utils.xml.IXMLIOTreeRefResolver;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLIOTreeRef;
import jo.util.utils.xml.XMLIOUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;

public class ArrayListIOHandler implements IXMLIOHandler, IXMLIOTreeRefResolver
{
    public Class<?> getHandledClass()
    {
        return ArrayList.class;
    }
    
    @SuppressWarnings("unchecked")
    public Node toXML(Node parent, Object obj, Map<Object,Object> map)
    {
        Node n = XMLEditUtils.addElement(parent, getTagName());
        for (Object child : (List<Object>)obj)
            XMLIOUtils.toXML(n, child, map);
        return n;
    }

    public Object fromXML(Node bean, Map<Object,Object> map, ClassLoader loader)
    {
        List<Object> list = new ArrayList<Object>();
        for (Node n : XMLUtils.findNodes(bean, "*"))
        {
            Object val = XMLIOUtils.fromXML(n, map, loader);
            if (val instanceof XMLIOTreeRef)
            {
                XMLIOTreeRef ref = (XMLIOTreeRef)val;
                ref.setArg1(list);
                ref.setArg2(new Integer(list.size()));
                ref.setResolver(this);
            }
            list.add(val);
        }
        return list;
    }

    public String getTagName()
    {
        return "arrayList";
    }

    public void resolve(XMLIOTreeRef ref, Object val)
    {
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>)ref.getArg1();
        Integer off = (Integer)ref.getArg2();
        //System.out.println("Fixup: ref "+ref.getKey()+", array #"+off+"<-"+val);
        list.remove(off.intValue());
        list.add(off.intValue(), val);
    }

}
