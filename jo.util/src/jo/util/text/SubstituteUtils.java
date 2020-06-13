/*
 * Created on Mar 2, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.text;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import jo.util.utils.ExprUtils;
import jo.util.utils.ExpressionEvaluationException;

public class SubstituteUtils
{
    public static String substitute(String text, Properties props, String prefix, String suffix)
    {
        int at = 0;
        for (;;)
        {
            int start = text.indexOf(prefix, at);
            if (start < 0)
                break;
            int end = text.indexOf(suffix, at + prefix.length());
            if (end < 0)
                break;
            String key = text.substring(start + prefix.length(), end);
            String value = props.getProperty(key, key);
            text = text.substring(0, start) + value + text.substring(end + suffix.length());
            at = start;
        }
        return text;
    }
    
    public static String substitute(String text, Properties props)
    {
        return substitute(text, props, "<%", "%>");
    }

    public static String substituteExpr(String text, Map<String,Object> props, String prefix, String suffix)
    {
        int at = 0;
        for (;;)
        {
            int start = text.indexOf(prefix, at);
            if (start < 0)
                break;
            int end = text.indexOf(suffix, at + prefix.length());
            if (end < 0)
                break;
            String expr = text.substring(start + prefix.length(), end);
            if (expr.startsWith("LOOP:"))
            {
                int k = expr.indexOf(':', 5);
                String key = expr.substring(5, k);
                String prop = expr.substring(k + 1);
                String endTag = prefix+"ENDLOOP:"+key+suffix;
                int loopEnd = text.indexOf(endTag, end);
                String textToChange = text.substring(end + suffix.length(), loopEnd);
                String value = evalLoop(props, key, prop, textToChange, prefix, suffix);
                text = text.substring(0, start) + value + text.substring(loopEnd + endTag.length());
                at = start + value.length();
            }
            else
            {
                String value = evalExpression(props, expr);
                text = text.substring(0, start) + value + text.substring(end + suffix.length());
                at = start;
            }
        }
        return text;
    }

    private static String evalLoop(Map<String,Object> props, String key, String prop, String textToChange, String prefix, String suffix)
    {
        StringBuffer ret = new StringBuffer();
        Object iterator = props.get(key);
        if (iterator == null)
            return "";
        if (iterator instanceof Collection<?>)
            iterator = ((Collection<?>)iterator).iterator();
        if (iterator instanceof Iterator)
        {
            Iterator<?> i = (Iterator<?>)iterator;
            while (i.hasNext())
            {
                props.put(prop, i.next());
                ret.append(substituteExpr(textToChange, props, prefix, suffix));
            }
        }
        else if (iterator instanceof Object[])
        {
            Object[] arr  = (Object[])iterator;
            for (int i = 0; i < arr.length; i++)
            {
                props.put(prop, arr[i]);
                ret.append(substituteExpr(textToChange, props, prefix, suffix));
            }
        }
        return ret.toString();
    }

    /**
     * @param props
     * @param expr
     * @return
     */
    private static String evalExpression(Map<String,Object> props, String expr)
    {
        String value = "";
        try
        {
            Object o = ExprUtils.evalObject(expr, props);
            if (o != null)
                value = o.toString();
        }
        catch (ExpressionEvaluationException e)
        {
            e.printStackTrace();
        }
        return value;
    }
    
    public static String substituteExpr(String text, Map<String,Object> props)
    {
        return substituteExpr(text, props, "<%", "%>");
    }
}
