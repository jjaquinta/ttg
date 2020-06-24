package jo.util.utils.xml;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import jo.util.utils.BeanUtils;
import jo.util.utils.io.ResourceUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XMLTransUtils
{
    public static String transform(String input, Map<String,Object> variables)
    {
        Document inputDocument = XMLUtils.readString(input);
        Document outputDocument = transform(inputDocument, variables);
        return XMLUtils.writeString(outputDocument.getFirstChild(), false);
    }
    
    public static Document transform(Document input, Map<String,Object> variables)
    {
        Document output = XMLUtils.newDocument();
        for (Node n = input.getFirstChild(); n != null; n = n.getNextSibling())
        {
            transform(n, output, variables);
        }
        return output;
    }
    
    public static void transform(Node input, Node output, Map<String,Object> variables)
    {
        if (input instanceof Attr)
        {
            if (output instanceof Element)
                ((Element)output).setAttribute(((Attr)input).getName(), ((Attr)input).getValue());
        }
        else if (input instanceof Comment)
        {
            output.appendChild(getOwnerDocument(output).createComment(((Comment)input).getNodeValue()));
        }
        else if (input instanceof Element)
        {
            if (input.getNodeName().startsWith("ttg:"))
                transformCommand((Element)input, output, variables);
            else
            {
                Element ele = getOwnerDocument(output).createElement(((Element)input).getNodeName());
                output.appendChild(ele);
                if (input.hasAttributes())
                {
                    NamedNodeMap nnm = input.getAttributes();
                    for (int i = 0; i < nnm.getLength(); i++)
                    {
                        Node nn = nnm.item(i);
                        ele.setAttribute(nn.getNodeName(), nn.getNodeValue());
                    }
                }
                for (Node n = input.getFirstChild(); n != null; n = n.getNextSibling())
                    transform(n, ele, variables);
            }
        }
        else if (input instanceof EntityReference)
        {
            output.appendChild(getOwnerDocument(output).createEntityReference(((EntityReference)input).getNodeValue()));
        }
        else if (input instanceof ProcessingInstruction)
        {
            output.appendChild(getOwnerDocument(output).createProcessingInstruction(((ProcessingInstruction)input).getTarget(), ((ProcessingInstruction)input).getData()));
        }
        else if (input instanceof Text)
        {
            output.appendChild(getOwnerDocument(output).createTextNode(((Text)input).getNodeValue()));
        }
    }
    
    private static Document getOwnerDocument(Node n)
    {
        if (n instanceof Document)
            return (Document)n;
        else
            return n.getOwnerDocument();
    }
    
    private static void transformCommand(Element ele, Node output, Map<String,Object> variables)
    {
        String cmd = ele.getNodeName();
        if (cmd.equals("ttg:write"))
            transformWriteCommand(ele, output, variables);
        else if (cmd.equals("ttg:iterate"))
            transformIterateCommand(ele, output, variables);
        else if (cmd.equals("ttg:equals"))
            transformEqualsCommand(ele, output, variables, true);
        else if (cmd.equals("ttg:notEquals"))
            transformEqualsCommand(ele, output, variables, false);
        else if (cmd.equals("ttg:greaterThan"))
            transformGreaterThanCommand(ele, output, variables, true);
        else if (cmd.equals("ttg:greaterThanOrEquals"))
            transformGreaterThanOrEqualsCommand(ele, output, variables, true);
        else if (cmd.equals("ttg:LessThan"))
            transformGreaterThanOrEqualsCommand(ele, output, variables, false);
        else if (cmd.equals("ttg:lessThanOrEquals"))
            transformGreaterThanCommand(ele, output, variables, false);
        else if (cmd.equals("ttg:include"))
            transformInclude(ele, output, variables);
    }
    
    private static Object getAttributeValue(Element ele, String attr, Map<String,Object> variables)
    {
        String key = XMLUtils.getAttribute(ele, attr);
        if (key == null)
            return null;
        Object val = BeanUtils.get(variables, key);
        if (val == null)
            return key;
        return val;
    }
    
    private static int sgn(double d)
    {
        if (d < 0)
            return -1;
        else if (d > 0)
            return 1;
        else
            return 0;
    }
    
    private static int compareAttributes(Element ele, String attr1, String attr2, Map<String,Object> variables)
    {
        Object val1 = getAttributeValue(ele, attr1, variables);
        Object val2 = getAttributeValue(ele, attr2, variables);
        if ((val1 == null) && (val2 == null))
            return 0;
        if (val1 == null)
            return 1;
        if (val2 == null)
            return -1;
        if ((val1 instanceof Number) && (val2 instanceof Number))
        {
            return sgn(((Number)val1).doubleValue() - ((Number)val2).doubleValue());
        }
        String s1 = val1.toString();
        String s2 = val2.toString();
        try
        {
            double d1 = Double.parseDouble(s1);
            double d2 = Double.parseDouble(s2);
            return sgn(d1 - d2);
        }
        catch (NumberFormatException e)
        {
            return s1.compareTo(s2);
        }
    }
    
    private static void transformWriteCommand(Element ele, Node output, Map<String,Object> variables)
    {
        Object val = getAttributeValue(ele, "name", variables);
        if (val != null)
            output.appendChild(getOwnerDocument(output).createTextNode(val.toString()));
    }
   
    private static void copyChildren(Element ele, Node output, Map<String,Object> variables, boolean beforeElse)
    {
        boolean copy = beforeElse;
        for (Node n = ele.getFirstChild(); n != null; n = n.getNextSibling())
        {
            String cmd = n.getNodeName();
            if (cmd.equals("ttg:else"))
                copy = !copy;
            else if (copy)
                transform(n, output, variables);
        }
    }
    
    private static void transformEqualsCommand(Element ele, Node output, Map<String,Object> variables, boolean equals)
    {
        boolean rval = (compareAttributes(ele, "name", "value", variables) == 0);
        copyChildren(ele, output, variables, rval == equals);
    }
    
    private static void transformGreaterThanCommand(Element ele, Node output, Map<String,Object> variables, boolean equals)
    {
        boolean rval = (compareAttributes(ele, "name", "value", variables) > 0);
        copyChildren(ele, output, variables, rval == equals);
    }
    
    private static void transformGreaterThanOrEqualsCommand(Element ele, Node output, Map<String,Object> variables, boolean equals)
    {
        boolean rval = (compareAttributes(ele, "name", "value", variables) >= 0);
        copyChildren(ele, output, variables, rval == equals);
    }
    
    private static void transformIterateCommand(Element ele, Node output, Map<String,Object> variables)
    {
        String key = XMLUtils.getAttribute(ele, "name");
        Object val = BeanUtils.get(variables, key);
        Object[] arr = null;
        if (val instanceof Object[])
            arr = (Object[])val;
        else if (val instanceof Collection)
            arr = ((Collection<?>)val).toArray();
        if (arr == null)
            return;
        String id = XMLUtils.getAttribute(ele, "id");
        for (int i = 0; i < arr.length; i++)
        {
            variables.put(id, arr[i]);
            for (Node n = ele.getFirstChild(); n != null; n = n.getNextSibling())
                transform(n, ele, variables);
        }
        variables.remove(id);
    }
    
    private static void transformInclude(Element ele, Node output, Map<String,Object> variables)
    {
        String key = XMLUtils.getAttribute(ele, "href");
        String root = (String)variables.get("xml.root");
        if (root != null)
            key = root + key;
        InputStream is;
        if (variables.containsKey("xml.root.ref"))
            is = ResourceUtils.loadSystemResourceStream(key, (Class<?>)variables.get("xml.root.ref"));
        else
            is = ResourceUtils.loadSystemResourceStream(key);
        Document doc = XMLUtils.readStream(is);
        for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
        {
            transform(n, output, variables);
        }
    }
}
