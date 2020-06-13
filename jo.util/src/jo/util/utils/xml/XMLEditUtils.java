/*
 * Created on Feb 27, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class XMLEditUtils
{
    public static Node addTextTag(Node parent, String name, String value)
    {
        Node node = parent.getOwnerDocument().createElement(name);
        parent.appendChild(node);
        Text text = parent.getOwnerDocument().createTextNode(value);
        node.appendChild(text);
        return node;
    }

    public static Node addText(Node parent, String value)
    {
        Text text = parent.getOwnerDocument().createTextNode(value);
        parent.appendChild(text);
        return text;
    }

    public static Element addElement(Node parent, String name)
    {
        Element node;
        if (parent.getOwnerDocument() != null)
            node = parent.getOwnerDocument().createElement(name);
        else if (parent instanceof Document)
            node = ((Document)parent).createElement(name);
        else
            return null;
        parent.appendChild(node);
        return node;
    }

    public static Attr addAttribute(Node parent, String name, String value)
    {
        Attr node = parent.getOwnerDocument().createAttribute(name);
        node.setValue(value);
        parent.getAttributes().setNamedItem(node);
        return node;
    }

    public static Node duplicate(Node node, Document ownerDocument)
    {
        if (node instanceof Text)
            return ownerDocument.createTextNode(node.getNodeValue());
        Node newNode = ownerDocument.createElement(node.getNodeName());
        NamedNodeMap attribs = node.getAttributes();
        for (int i = 0; i < attribs.getLength(); i++)
        {
            Node attrib = attribs.item(i);
            addAttribute(newNode, attrib.getNodeName(), attrib.getNodeValue());
        }
        for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling())
        {
            Node newN = duplicate(n, ownerDocument);
            newNode.appendChild(newN);
        }
        return newNode;
    }

    public static void removeAllChildren(Node node)
    {
        while (node.getFirstChild() != null)
            node.removeChild(node.getFirstChild());
    }
    
    public static void setInnerXML(Node node, String xml)
    {
        removeAllChildren(node);
        Document doc = XMLUtils.readString("<html>"+xml+"</html>"); // must give root element
        if ((doc != null) && (doc.getFirstChild() != null))
        {
            for (Node n = doc.getFirstChild().getFirstChild(); n != null; n = n.getNextSibling())
                node.appendChild(duplicate(n, node.getOwnerDocument()));
        }
    }

    public static void copyAttributes(Node from, Node to)
    {
        NamedNodeMap map = from.getAttributes();
        for (int i = 0; i < map.getLength(); i++)
        {
            Node attr = map.item(i);
            addAttribute(to, attr.getNodeName(), attr.getNodeValue());
        }
    }
    
    public static void merge(File file1, String point1, File file2, String point2, File file3)
    {
        Document doc1 = XMLUtils.readFile(file1);
        Document doc2 = XMLUtils.readFile(file2);
        List<Node> points1 = XMLUtils.findNodes(doc1, point1);
        List<Node> points2 = XMLUtils.findNodes(doc2, point2);
        for (int i = 0; i < Math.min(points1.size(), points2.size()); i++)
        {
            Node node1 = points1.get(i);
            Node node2 = points2.get(i);
            Node dup1 = duplicate(node1, node2.getOwnerDocument());
            node2.getParentNode().replaceChild(dup1, node2);
        }
        XMLUtils.writeFile(doc2.getFirstChild(), file3);
    }

    public static void replaceText(Node node, String text)
    {
        for (;;)
        {
            Node n = node.getFirstChild();
            if (n == null)
                break;
            node.removeChild(n);
        }
        Text t = node.getOwnerDocument().createTextNode(text);
        node.appendChild(t);
    }

    public static Node addElementAndAttributes(Node parent, String name, String... atts)
    {
        Node n = addElement(parent, name);
        for (int i = 0; i < atts.length; i += 2)
            addAttribute(n, atts[i], atts[i+1]);
        return n;
    }
    
    public static void addAtEndOfGroup(Node parent, Node newChild)
    {
        addAtEndOfGroup(parent, newChild.getNodeName(), newChild);
    }
    
    public static void addAtEndOfGroup(Node parent, String childType, Node newChild)
    {
        List<Node> peers = XMLUtils.findNodes(parent, childType);
        if (peers.size() == 0)
            parent.appendChild(newChild);
        else
        {
            Node lastPeer = peers.get(peers.size() - 1);
            if (lastPeer.getNextSibling() == null)
                parent.appendChild(newChild);
            else
                parent.insertBefore(newChild, lastPeer.getNextSibling());
        }
        
    }
}
