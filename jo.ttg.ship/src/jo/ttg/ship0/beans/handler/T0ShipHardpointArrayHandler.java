package jo.ttg.ship0.beans.handler;

import java.util.Iterator;
import java.util.Map;

import jo.ttg.ship0.beans.T0ShipHardpoint;
import jo.util.utils.FormatUtils;
import jo.util.utils.xml.IXMLIOHandler;
import jo.util.utils.xml.IXMLIOTreeRefResolver;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLIOTreeRef;
import jo.util.utils.xml.XMLIOUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;

public class T0ShipHardpointArrayHandler implements IXMLIOHandler, IXMLIOTreeRefResolver
{
    private T0ShipHardpoint[]  mReferenceObject;
    
    public T0ShipHardpointArrayHandler()
    {
        mReferenceObject = new T0ShipHardpoint[0];
    }

    public Class<?> getHandledClass()
    {
        return mReferenceObject.getClass();
    }

    public String getTagName()
    {
        return "t0ShipHardpointArray";
    }

    @SuppressWarnings("rawtypes")
    public Object fromXML(Node bean, Map<Object,Object> map, ClassLoader loader)
    {
        int length = FormatUtils.parseInt(XMLUtils.getAttribute(bean, "length"));
        T0ShipHardpoint[] hp = new T0ShipHardpoint[length];
        int idx = 0;
        for (Iterator i = XMLUtils.findNodes(bean, "*").iterator(); i.hasNext(); idx++)
        {
            Node n2 = (Node)i.next();
            Object val = XMLIOUtils.fromXML(n2, map, loader);
            if (val instanceof XMLIOTreeRef)
            {
                XMLIOTreeRef ref = (XMLIOTreeRef)val;
                ref.setArg1(hp);
                ref.setArg2(String.valueOf(idx));
                ref.setResolver(this);
            }
            else
                hp[idx] = (T0ShipHardpoint)val;
        }
        return hp;
    }

    public Node toXML(Node parent, Object obj, Map<Object,Object> map)
    {
        T0ShipHardpoint[] hp = (T0ShipHardpoint[])obj;
        Node n = XMLEditUtils.addElement(parent, getTagName());
        XMLEditUtils.addAttribute(n, "length", String.valueOf(hp.length));
        for (int i = 0; i < hp.length; i++)
        {
            XMLIOUtils.toXML(parent, hp[i], map);
        }
        return n;
    }

    public void resolve(XMLIOTreeRef ref, Object val)
    {
        T0ShipHardpoint[] list = (T0ShipHardpoint[])ref.getArg1();
        String loc = (String)ref.getArg2();
        int i = FormatUtils.parseInt(loc);
        //System.out.println("Fixup: ref "+ref.getKey()+", array #"+off+"<-"+val);
        list[i] = (T0ShipHardpoint)val;
    }
}
