package jo.util.utils.xml.handlers;

import java.lang.reflect.Constructor;
import java.util.Map;

import jo.util.utils.xml.IXMLIOHandler;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;

public class SimpleIOHandler implements IXMLIOHandler
{
    private Class<?>   mHandledClass;
    private String  mTagName;
    
    public SimpleIOHandler(Class<?> handledClass)
    {
        mHandledClass = handledClass;
        mTagName = mHandledClass.getName();
        int o = mTagName.lastIndexOf('.');
        if (o >= 0)
            mTagName = mTagName.substring(o + 1);
    }

    public Class<?> getHandledClass()
    {
        return mHandledClass;
    }
    
    public String getTagName()
    {
        return mTagName;
    }
    
    public Node toXML(Node parent, Object obj, Map<Object,Object> map)
    {
        Node n = XMLEditUtils.addElement(parent, getTagName());
        XMLEditUtils.addAttribute(n, "value", obj.toString());
        return n;
    }

    public Object fromXML(Node bean, Map<Object,Object> map, ClassLoader loader)
    {
        try
        {
            Constructor<?> constructor = getHandledClass().getConstructor(new Class<?>[]{ String.class });
            return constructor.newInstance(new Object[] { XMLUtils.getAttribute(bean, "value")});
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
