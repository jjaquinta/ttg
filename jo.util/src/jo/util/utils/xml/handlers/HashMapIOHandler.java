package jo.util.utils.xml.handlers;

import java.util.HashMap;
import java.util.Map;

import jo.util.utils.xml.IXMLIOHandler;
import jo.util.utils.xml.IXMLIOTreeRefResolver;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLIOTreeRef;
import jo.util.utils.xml.XMLIOUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;

public class HashMapIOHandler implements IXMLIOHandler, IXMLIOTreeRefResolver
{
    public Class<?> getHandledClass()
    {
        return HashMap.class;
    }
    
    public Node toXML(Node parent, Object obj, Map<Object,Object> map)
    {
        Node n = XMLEditUtils.addElement(parent, getTagName());
        @SuppressWarnings("unchecked")
        Map<Object,Object> ret = (Map<Object,Object>)obj;
        for (Object key : ret.keySet())
        {
            Object value = ret.get(key);
            Node entry = XMLEditUtils.addElement(n, "element");
            Node keyEntry = XMLEditUtils.addElement(entry, "key");
            XMLIOUtils.toXML(keyEntry, key, map);
            Node valueEntry = XMLEditUtils.addElement(entry, "value");
            XMLIOUtils.toXML(valueEntry, value, map);
        }
        return n;
    }

    public Object fromXML(Node bean, Map<Object,Object> map, ClassLoader loader)
    {
        Map<Object,Object> ret = new HashMap<Object,Object>();
        for (Node entry : XMLUtils.findNodes(bean, "element"))
        {
            Node keyEntry = XMLUtils.findFirstNode(entry, "key");
            Node keyValue = XMLUtils.findFirstNode(keyEntry, "*");
            Object key = XMLIOUtils.fromXML(keyValue, map, loader);
            if (key instanceof XMLIOTreeRef)
            {
                XMLIOTreeRef ref = (XMLIOTreeRef)key;
                ref.setArg1(ret);
                ref.setArg2("key");
                ref.setResolver(this);
            }
            Node valueEntry = XMLUtils.findFirstNode(entry, "value");
            Node valueValue = XMLUtils.findFirstNode(valueEntry, "*");
            Object value = XMLIOUtils.fromXML(valueValue, map, loader);
            if (value instanceof XMLIOTreeRef)
            {
                XMLIOTreeRef ref = (XMLIOTreeRef)value;
                ref.setArg1(ret);
                ref.setArg2("val");
                ref.setResolver(this);
            }
            ret.put(key, value);
        }
        return ret;
    }

    public String getTagName()
    {
        return "hashMap";
    }

    public void resolve(XMLIOTreeRef ref, Object val)
    {
        @SuppressWarnings("unchecked")
        Map<Object,Object> map = (Map<Object,Object>)ref.getArg1();
        String op = (String)ref.getArg2();
        if (op.equals("key"))
        {
            Object mapVal = map.get(ref);
            //System.out.println("Fixup: ref "+ref.getKey()+", key<-"+val+", val="+mapVal);
            map.remove(ref);
            map.put(val, mapVal);
        }
        else
        {
            for (Object key : map.keySet())
            {
                Object mapVal = map.get(key);
                if (mapVal == ref)
                {
                    //System.out.println("Fixup: ref "+ref.getKey()+", key="+key+", val<-"+val);
                    map.put(key, val);
                    break;
                }
            }
        }
    }

}
