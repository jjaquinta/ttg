package jo.util.utils.xml.handlers;

import java.util.Iterator;
import java.util.Map;

import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.xml.IXMLIOHandler;
import jo.util.utils.xml.IXMLIOTreeRefResolver;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLIOTreeRef;
import jo.util.utils.xml.XMLIOUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;

public class Object2DArrayIOHandler implements IXMLIOHandler, IXMLIOTreeRefResolver
{
    private Object[][] mSample = new Object[0][0];
    
    public Class<?> getHandledClass()
    {
        return mSample.getClass();
    }

    public String getTagName()
    {
        return "obj2d";
    }
    
    public Node toXML(Node parent, Object obj, Map<Object,Object> map)
    {
        Node n = XMLEditUtils.addElement(parent, getTagName());
        Object[][] arr = (Object[][])obj;
        XMLEditUtils.addAttribute(n, "length1", String.valueOf(arr.length));
        XMLEditUtils.addAttribute(n, "length2", String.valueOf(arr[0].length));
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[i].length; j++)
                XMLIOUtils.toXML(n, arr[i][j], map);
        return n;
    }

    public Object fromXML(Node bean, Map<Object,Object> map, ClassLoader loader)
    {
        int length1 = IntegerUtils.parseInt(XMLUtils.getAttribute(bean, "length1"));
        int length2 = IntegerUtils.parseInt(XMLUtils.getAttribute(bean, "length2"));
        Object[][] arr = new Object[length1][length2];
        Iterator<Node> ns = XMLUtils.findNodes(bean, "*").iterator();
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[i].length; j++)
            {
                Object val = XMLIOUtils.fromXML(ns.next(), map, loader);
                if (val instanceof XMLIOTreeRef)
                {
                    XMLIOTreeRef ref = (XMLIOTreeRef)val;
                    ref.setArg1(arr);
                    ref.setArg2(i*length2 + j);
                    ref.setResolver(this);
                }
                arr[i][j] = val;
            }
        return arr;
    }

    public void resolve(XMLIOTreeRef ref, Object val)
    {
        Object[][] arr = (Object[][])ref.getArg1();
        int ij = (Integer)ref.getArg2();
        int i = ij/arr[0].length;
        int j = ij%arr[0].length;
        arr[i][j] = val;
    }
}
