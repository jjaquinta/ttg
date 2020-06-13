package jo.util.utils.xml.handlers;

import java.util.Map;
import java.util.Random;

import jo.util.utils.FormatUtils;
import jo.util.utils.xml.IXMLIOHandler;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;

public class RandomIOHandler implements IXMLIOHandler
{
    public Class<?> getHandledClass()
    {
        return Random.class;
    }
    
    public Node toXML(Node parent, Object obj, Map<Object,Object> map)
    {
        Node n = XMLEditUtils.addElement(parent, getTagName());
        XMLEditUtils.addAttribute(n, "seed", String.valueOf(((Random)obj).nextLong()));
        return n;
    }

    public Object fromXML(Node bean, Map<Object,Object> map, ClassLoader loader)
    {
        long seed = FormatUtils.parseLong(XMLUtils.getAttribute(bean, "seed"));
        return new Random(seed);
    }

    public String getTagName()
    {
        return "javaUtilRandom";
    }

}
