package jo.util.utils.xml.handlers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jo.util.utils.xml.IXMLIOHandler;
import jo.util.utils.xml.IXMLIOTreeRefResolver;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLIOTreeRef;
import jo.util.utils.xml.XMLIOUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;

public class HashSetIOHandler implements IXMLIOHandler, IXMLIOTreeRefResolver
{
    public Class<?> getHandledClass()
    {
        return HashSet.class;
    }
    
    @SuppressWarnings("unchecked")
    public Node toXML(Node parent, Object obj, Map<Object,Object> map)
    {
        Node n = XMLEditUtils.addElement(parent, getTagName());
        for (Object child : (Set<Object>)obj)
            XMLIOUtils.toXML(n, child, map);
        return n;
    }

    public Object fromXML(Node bean, Map<Object,Object> map, ClassLoader loader)
    {
        Set<Object> list = new HashSet<Object>();
        for (Node n : XMLUtils.findNodes(bean, "*"))
        {
            Object val = XMLIOUtils.fromXML(n, map, loader);
            if (val instanceof XMLIOTreeRef)
            {
                XMLIOTreeRef ref = (XMLIOTreeRef)val;
                ref.setArg1(list);
                ref.setResolver(this);
            }
            list.add(val);
        }
        return list;
    }

    public String getTagName()
    {
        return "hashSet";
    }

    public void resolve(XMLIOTreeRef ref, Object val)
    {
        @SuppressWarnings("unchecked")
        Set<Object> list = (Set<Object>)ref.getArg1();
        //System.out.println("Fixup: ref "+ref.getKey()+", array #"+off+"<-"+val);
        list.add(val);
    }

}
