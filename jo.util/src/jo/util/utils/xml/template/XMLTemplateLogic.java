package jo.util.utils.xml.template;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import jo.util.utils.ArrayUtils;
import jo.util.utils.ExprUtils;
import jo.util.utils.ExpressionEvaluationException;
import jo.util.utils.obj.StringUtils;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XMLTemplateLogic
{
    public static Document genrateTemplate(Document template, Map<String,Object> vars)
    {
        Document gen = XMLUtils.newDocument();
        copyChildren(template, gen, vars);
        return gen;
    }

    private static void copyChildren(Node template, Node gen, Map<String,Object> vars)
    {
        for (Node n = template.getFirstChild(); n != null; n = n.getNextSibling())
        {
            duplicateChild(template, n, gen, vars);
        }        
    }
    
    private static void duplicateChild(Node templateParent, Node templateChild, Node genParent, Map<String,Object> vars)
    {
        String name = templateChild.getNodeName();
        //System.out.println(name);
        if (name.equals("#text"))
        {
            String txt = templateChild.getNodeValue();
            try
            {
                txt = ExprUtils.expandText(txt, vars);
            }
            catch (ExpressionEvaluationException e)
            {
                e.printStackTrace();
            }
            genParent.appendChild(genParent.getOwnerDocument().createTextNode(txt));
        }
        else if (name.equals("template:if"))
        {
            handleIf(templateChild, genParent, vars);
        }
        else if (name.equals("template:repeat"))
        {
            handleRepeat(templateChild, genParent, vars);
        }
        else
        {
            duplicateNode(templateChild, genParent, vars);
        }
    }

    private static void handleRepeat(Node templateChild, Node genParent, Map<String,Object> vars)
    {
        StringTokenizer iterates = new StringTokenizer(XMLUtils.getAttribute(templateChild, "iterate"), ",");
        StringTokenizer variables = new StringTokenizer(XMLUtils.getAttribute(templateChild, "var"), ",");
        String skip = XMLUtils.getAttribute(templateChild, "skip");
        Object[][] iterators;
        String[] variableNames = ArrayUtils.toStringArray(variables);
        iterators = new Object[iterates.countTokens()][];
        for (int i = 0; i < iterators.length; i++)
            try
            {
                iterators[i] = ArrayUtils.toArray(ExprUtils.evalObject(iterates.nextToken(), vars));
            }
            catch (ExpressionEvaluationException e)
            {
                e.printStackTrace();
            }
        if (iterators.length == 0)
             return;
        if (iterators[0].length == 0)
            return;
        if (!StringUtils.isTrivial(skip))
        {
            int stride = Integer.parseInt(skip);
            Object[][] newIterators = new Object[iterators.length][(iterators[0].length - 1)/stride + 1];
            for (int j = 0; j < newIterators[0].length; j++)
            {
                int l = Math.min(stride, iterators[0].length - j*stride);
                for (int k = 0; k < newIterators.length; k++)
                    newIterators[k][j] = ArrayUtils.dup(iterators[k], j*stride, l);
            }
            iterators = newIterators;
        }
        Map<String,Object> newVars = new HashMap<String,Object>(vars);
        for (int i = 0; i < iterators[0].length; i++)
        {
            for (int j = 0; j < iterators.length; j++)
                newVars.put(variableNames[j], iterators[j][i]);
            copyChildren(templateChild, genParent, newVars);
        }
    }

    private static void handleIf(Node templateChild, Node genParent, Map<String,Object> vars)
    {
        try
        {
            boolean doit = ExprUtils.evalBoolean(XMLUtils.getAttribute(templateChild, "expr"), vars);
            for (Node n = templateChild.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeName().equals("template:else"))
                    doit = !doit;
                else if (doit)
                    duplicateChild(templateChild, n, genParent, vars);
            }
        }
        catch (ExpressionEvaluationException e)
        {
            e.printStackTrace();
        }
    }

    private static void duplicateNode(Node templateChild, Node genParent, Map<String,Object> vars)
    {
        String name = templateChild.getNodeName();
        Node genChild = XMLEditUtils.addElement(genParent, name);
        duplicateAttributes(templateChild, genChild, vars);
        copyChildren(templateChild, genChild, vars);
    }
    
    private static void duplicateAttributes(Node template, Node gen, Map<String,Object> vars)
    {
        for (int i = 0; i < template.getAttributes().getLength(); i++)
        {
            Node attr = template.getAttributes().item(i);
            String key = attr.getNodeName();
            String val = attr.getNodeValue();
            try
            {
                val = ExprUtils.expandText(val, vars);
            }
            catch (ExpressionEvaluationException e)
            {
                e.printStackTrace();
            }
            XMLEditUtils.addAttribute(gen, key, val);
        }
    }
}
