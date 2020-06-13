package jo.util.logic;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CSVLogic
{
    public static void toCSV(File f, Class<?> beanClass, Collection<?> beans) throws IOException, IntrospectionException
    {
        FileWriter wtr = new FileWriter(f);
        toCSV(wtr, beanClass, beans);
        wtr.close();
    }
    public static void toCSV(Writer wtr, Class<?> beanClass, Collection<?> beans) throws IOException, IntrospectionException
    {
        Map<String,PropertyDescriptor> descriptors = getDescriptors(beanClass);
        String[] columns = new String[descriptors.size()];
        descriptors.keySet().toArray(columns);
        wtr.write(toCSVHeader(columns));
        wtr.write("\r\n");
        for (Iterator<?> i = beans.iterator(); i.hasNext(); )
        {
            Object bean = i.next();
            wtr.write(toCSVLine(columns, descriptors, bean));
            wtr.write("\r\n");
        }
        wtr.flush();
    }
    
    public static void toCSV(File f, Collection<Collection<?>> lines) throws IOException
    {
        FileWriter wtr = new FileWriter(f);
        toCSV(wtr, lines);
        wtr.close();
    }
    public static void toCSV(Writer wtr, Collection<Collection<?>> lines) throws IOException
    {
        for (Collection<?> line : lines)
        {
            wtr.write(toCSVLine(line));
            wtr.write("\r\n");
        }
        wtr.flush();
    }
    
    public static String toCSVLine(Collection<?> line)
    {
        StringBuffer outbuf = new StringBuffer();
        for (Iterator<?> i = line.iterator(); i.hasNext(); )
        {
            if (outbuf.length() > 0)
                outbuf.append(",");
            Object val = i.next();
            if (val instanceof Number)
                outbuf.append(val.toString());
            else
            {
                outbuf.append("\"");
                if (val != null)
                    outbuf.append(escape(val.toString()));
                outbuf.append("\"");
            }
        }
        return outbuf.toString();
    }
    
    public static String toCSVHeader(String[] columns)
    {
        StringBuffer outbuf = new StringBuffer();
        for (int i = 0; i < columns.length; i++)
        {
            if (outbuf.length() > 0)
                outbuf.append(",");
            outbuf.append("\"");
            outbuf.append(escape(columns[i]));
            outbuf.append("\"");
        }
        return outbuf.toString();
    }
    
    public static String toCSVLine(String[] columns, Map<String,PropertyDescriptor> descriptors, Object bean)
    {
        Map<String,Object> vals = getValues(descriptors, bean);
        StringBuffer outbuf = new StringBuffer();
        for (int i = 0; i < columns.length; i++)
        {
            if (outbuf.length() > 0)
                outbuf.append(",");
            Object val = vals.get(columns[i]);
            outbuf.append("\"");
            if (val != null)
                outbuf.append(escape(val.toString()));
            outbuf.append("\"");
        }
        return outbuf.toString();
    }
    
    private static String escape(String str)
    {
        StringBuffer ret = new StringBuffer();
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++)
            if (c[i] == '\"')
                ret.append("\\\"");
            //else if (c[i] == ',')
            //    ret.append("\\,");
            else if (c[i] == '\t')
                ret.append("\\t");
            else if (c[i] == '\b')
                ret.append("\\b");
            else if (c[i] == '\r')
                ret.append("\\r");
            else if (c[i] == '\n')
                ret.append("\\n");
        /*
            else if ((c[i] < 0x20) || (c[i] >= 0x80))
            {
                ret.append("\\u");
                String hex = Integer.toHexString(c[i]);
                while (hex.length() < 4)
                    hex = "0" + hex;
                ret.append(hex);
            }
            */
            else
                ret.append(c[i]);
        return ret.toString();
    }

    public static Collection<Object> fromCSV(File f, Class<?> beanClass) throws IOException, IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        FileReader rdr = new FileReader(f);
        Collection<Object> ret = fromCSV(rdr, beanClass);
        rdr.close();
        return ret;
    }
    public static Collection<Object> fromCSV(Reader r, Class<?> beanClass) throws IOException, IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        List<Object> ret = new ArrayList<Object>();
        Map<String,PropertyDescriptor> descriptors = getDescriptors(beanClass);
        BufferedReader rdr = new BufferedReader(r);
        PropertyDescriptor[] columns = fromCSVHeader(rdr.readLine(), descriptors);
        for (;;)
        {
            String inbuf = rdr.readLine();
            if (inbuf == null)
                break;
            Object bean = fromCSVLine(columns, beanClass, inbuf);
            ret.add(bean);
        }
        return ret;
    }
    
    public static String[] splitCSVLine(String inbuf)
    {
        List<String> columns = new ArrayList<String>();
        StringBuffer outbuf = new StringBuffer();
        char[] c = inbuf.toCharArray();
        boolean eclipse = false;
        for (int i = 0; i < c.length; i++)
        {
            if (c[i] == '\"')
                eclipse = !eclipse;
            else if (c[i] == '\\')
            {
                char v = c[++i];
                if (v == 't')
                    outbuf.append('\t');
                else if (v == 'r')
                    outbuf.append('\r');
                else if (v == 'n')
                    outbuf.append('\n');
                else if (v == 'b')
                    outbuf.append('\b');
                else if (v == 'u')
                {
                    StringBuffer hexbuf = new StringBuffer();
                    hexbuf.append(c[++i]);
                    hexbuf.append(c[++i]);
                    hexbuf.append(c[++i]);
                    hexbuf.append(c[++i]);
                    v = (char)Integer.parseInt(hexbuf.toString(), 16);
                    outbuf.append(v);
                }
                else
                    outbuf.append(v);
            }
            else if (c[i] == ',')
            {
                if (!eclipse)
                {
                    columns.add(outbuf.toString());
                    outbuf.setLength(0);
                }
                else
                    outbuf.append(c[i]);
            }
            else
                outbuf.append(c[i]);
        }
        columns.add(outbuf.toString());
        String[] ret = new String[columns.size()];
        columns.toArray(ret);
        return ret;
    }
    
    public static PropertyDescriptor[] fromCSVHeader(String inbuf, Map<String,PropertyDescriptor> descriptors)
    {
        String[] columns = splitCSVLine(inbuf);
        PropertyDescriptor[] ret = new PropertyDescriptor[columns.length];
        for (int i = 0; i < columns.length; i++)
        {
            ret[i] = descriptors.get(columns[i]);
            if (ret[i] == null)
            {
                ret[i] = descriptors.get(columns[i].toLowerCase());
                if (ret[i] == null)
                {
                    String stripped = strip(columns[i]);
                    ret[i] = descriptors.get(stripped);
                    if (ret[i] == null)
                        ret[i] = descriptors.get(stripped.toLowerCase());
                }
            }
            if (ret[i] != null)
            {
                if (ret[i].getReadMethod() == null)
                    ret[i] = null;
                else if (ret[i].getWriteMethod() == null)
                    ret[i] = null;
            }
            if (ret[i] == null)
                throw new IllegalStateException("Cannot handle column "+columns[i]);
        }
        return ret;
    }
    
    public static Object fromCSVLine(PropertyDescriptor[] columns, Class<?> beanClass, String inbuf) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        String[] vals = splitCSVLine(inbuf);
        if (vals.length != columns.length)
        {
            System.out.println("CVSLogic: WARNING: expected "+columns.length+" columns, got "+vals.length);
            System.out.println("   "+inbuf);
        }
        Object ret = beanClass.newInstance();
        for (int i = 0; i < columns.length; i++)
        {
            if (columns[i] == null)
                continue;
            Class<?> valClass = columns[i].getReadMethod().getReturnType();
            String valName = valClass.getName();
            Object[] val = new Object[1];
            if (valClass == String.class)
                val[0] = vals[i];
            else if (vals[i].length() == 0)
                val[0] = 0;
            else if ((valClass == Integer.class) || valName.equals("int"))
                val[0] = new Integer(vals[i]);
            else if ((valClass == Double.class) || valName.equals("double"))
                val[0] = new Double(vals[i]);
            else if ((valClass == Long.class) || valName.equals("long"))
                val[0] = new Long(vals[i]);
            else if ((valClass == Float.class) || valName.equals("float"))
                val[0] = new Float(vals[i]);
            else if ((valClass == Boolean.class) || valName.equals("boolean"))
                val[0] = new Boolean(vals[i]);
            else if ((valClass == Character.class) || valName.equals("char"))
                val[0] = new Character(vals[i].charAt(0));
            else
                throw new IllegalArgumentException("Cannot handle class '"+valClass+"'");
            //System.out.println("Invoking "+columns[i].getWriteMethod()+" on "+ret.getClass().getName()+" with "+val[0].getClass().getName());
            columns[i].getWriteMethod().invoke(ret, val);
        }
        return ret;
    }

    public static Map<String,PropertyDescriptor> getDescriptors(Class<?> beanClass) throws IntrospectionException
    {
        Map<String,PropertyDescriptor> ret = new HashMap<String,PropertyDescriptor>();
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
        PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < props.length; i++)
        {
            if ((props[i].getReadMethod() == null)
                || (props[i].getWriteMethod() == null))
                continue; // must be r/w
            String name = props[i].getName();
            ret.put(name.toLowerCase(), props[i]);
        }
        return ret;
    }

    public static Map<String,Object> getValues(Map<String,PropertyDescriptor> descriptors, Object bean)
    {
        Object[] args = new Object[0];
        Map<String,Object> ret = new HashMap<String,Object>();
        for (String name : descriptors.keySet())
        {
            PropertyDescriptor prop = (PropertyDescriptor)descriptors.get(name);
            try
            {
                Object val = prop.getReadMethod().invoke(bean, args);
                ret.put(name, val);
            }
            catch (Exception e)
            {
            }
        }
        return ret;
    }
    
    private static String strip(String text)
    {
        char[] c = text.toCharArray();
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < c.length; i++)
            if (ret.length() == 0)
            {
                if (Character.isJavaIdentifierStart(c[i]))
                    ret.append(c[i]);
            }
            else
            {
                if (Character.isJavaIdentifierPart(c[i]))
                    ret.append(c[i]);
            }
        return ret.toString();
    }
}
