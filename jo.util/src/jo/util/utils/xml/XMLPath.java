/*
 * Created on Mar 18, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils.xml;

import org.w3c.dom.Node;

/*
 * Yes, yes, yes. I Should use xpath...
 */

public class XMLPath
{
    public static String pathTo(Node node)
    {
        Node parent = node.getParentNode();
        if (parent == null)
            return "";
        int count = 0;
        for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling())
            if (n.getNodeName().equals(node.getNodeName()))
                if (n == node)
                    break;
                else
                    count++;
        String ret = pathTo(parent) + "/" + node.getNodeName();
        if (count > 0)
            ret += "[" + count + "]";
        return ret;
    }
    
    public static Node fromPath(Node root, String path)
    {
        if (path.startsWith("/"))
            path = path.substring(1);
        int o = path.indexOf("/");
        String seg;
        String rest;
        if (o >= 0)
        {
            seg = path.substring(0, o);
            rest = path.substring(o + 1);
        }
        else
        {
            seg = path;
            rest = null;
        }
        int count;
        o = seg.indexOf("[");
        if (o > 0)
        {
            count = Integer.parseInt(seg.substring(o + 1, seg.length() - 1));
            seg = seg.substring(0, o);
        }
        else
            count = 0;
        Node ret = null;
        for (Node n = root.getFirstChild(); n != null; n = n.getNextSibling())
            if (n.getNodeName().equals(seg))
                if (count-- == 0)
                {
                    ret = n;
                    break;
                }
        if ((ret != null) && (rest != null))
            ret = fromPath(ret, rest);
        return ret;
    }
}
