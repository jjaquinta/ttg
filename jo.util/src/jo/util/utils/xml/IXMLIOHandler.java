package jo.util.utils.xml;

import java.util.Map;

import org.w3c.dom.Node;

public interface IXMLIOHandler
{
    public Class<?> getHandledClass();
    public String getTagName();
    public Node toXML(Node parent, Object obj, Map<Object,Object> map);
    public Object fromXML(Node bean, Map<Object,Object> map, ClassLoader loader);
}
