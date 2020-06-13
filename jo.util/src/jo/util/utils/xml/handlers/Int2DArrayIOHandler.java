package jo.util.utils.xml.handlers;

import java.util.Map;
import java.util.StringTokenizer;

import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.xml.IXMLIOHandler;
import jo.util.utils.xml.IXMLIOTreeRefResolver;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLIOTreeRef;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class Int2DArrayIOHandler implements IXMLIOHandler, IXMLIOTreeRefResolver
{
    private int[][] mSample = new int[0][0];
    
    public Class<?> getHandledClass()
    {
        return mSample.getClass();
    }

    public String getTagName()
    {
        return "int2d";
    }
    
    public Node toXML(Node parent, Object obj, Map<Object,Object> map)
    {
        Node n = XMLEditUtils.addElement(parent, getTagName());
        int[][] arr = (int[][])obj;
        XMLEditUtils.addAttribute(n, "length1", String.valueOf(arr.length));
        XMLEditUtils.addAttribute(n, "length2", String.valueOf(arr[0].length));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++)
        {
            if (i > 0)
                sb.append(";");
            for (int j = 0; j < arr[i].length; j++)
            {
                if (j > 0)
                    sb.append(",");
                sb.append(arr[i][j]);
            }
        }
        Text text = n.getOwnerDocument().createTextNode(sb.toString());
        n.appendChild(text);
        return n;
    }

    public Object fromXML(Node bean, Map<Object,Object> map, ClassLoader loader)
    {
        int length1 = IntegerUtils.parseInt(XMLUtils.getAttribute(bean, "length1"));
        int length2 = IntegerUtils.parseInt(XMLUtils.getAttribute(bean, "length2"));
        int[][] arr = new int[length1][length2];
        String txt = XMLUtils.getText(bean);
        int idx1 = 0;
        for (StringTokenizer st1 = new StringTokenizer(txt, ";"); st1.hasMoreTokens(); idx1++)
        {
            int idx2 = 0;
            for (StringTokenizer st2 = new StringTokenizer(st1.nextToken(), ","); st2.hasMoreTokens(); idx2++)
            {
                arr[idx1][idx2] = Integer.parseInt(st2.nextToken());
            }
        }
        return arr;
    }

    public void resolve(XMLIOTreeRef ref, Object val)
    {
    }
}
