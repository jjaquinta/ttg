/*
 * Created on Apr 11, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.beans;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CSVLogic
{
    public static void toCSV(Writer wtr, Class<?> beanClass, Collection<Bean> beans) throws IOException, IntrospectionException
    {
        Map<String,PropertyDescriptor> descriptors = BeanLogic.getDescriptors(beanClass);
        String[] columns = new String[descriptors.size()];
        descriptors.keySet().toArray(columns);
        wtr.write(toCSVHeader(columns));
        wtr.write("\r\n");
        for (Bean bean : beans)
        {
            wtr.write(toCSVLine(columns, descriptors, bean));
            wtr.write("\r\n");
        }
        wtr.flush();
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
    
    public static String toCSVLine(String[] columns, Map<String,PropertyDescriptor> descriptors, Bean bean)
    {
        Map<String,Object> vals = BeanLogic.getValues(descriptors, bean);
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
    
    public static String toCSVLine(String[] values)
    {
        StringBuffer outbuf = new StringBuffer();
        for (int i = 0; i < values.length; i++)
        {
            if (outbuf.length() > 0)
                outbuf.append(",");
            outbuf.append("\"");
            if (values[i] != null)
                outbuf.append(escape(values[i]));
            outbuf.append("\"");
        }
        return outbuf.toString();
    }
    
    private static String escape(String str)
    {
        int o = 0;
        for (;;)
        {
            o = str.indexOf("\"", o);
            if (o < 0)
                break;
            str = str.substring(0, o) + "\\" + str.substring(o);
            o += 2;
        }
        o = 0;
        for (;;)
        {
            o = str.indexOf(",", o);
            if (o < 0)
                break;
            str = str.substring(0, o) + "\\" + str.substring(o);
            o += 2;
        }
        return str;
    }

    public static Collection<Bean> fromCVS(Reader r, Class<?> beanClass) throws IOException, IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        List<Bean> ret = new ArrayList<Bean>();
        Map<String,PropertyDescriptor> descriptors = BeanLogic.getDescriptors(beanClass);
        BufferedReader rdr = new BufferedReader(r);
        String hdr = rdr.readLine();
        if (hdr == null)
            return ret;
        String[] columns = fromCSVHeader(hdr);
        for (;;)
        {
            String inbuf = rdr.readLine();
            if (inbuf == null)
                break;
            Bean bean = fromCSVLine(columns, beanClass, descriptors, inbuf);
            ret.add(bean);
        }
        return ret;
    }
    
    public static String[] fromCSVHeader(String inbuf)
    {
        List<String> columns = new ArrayList<String>();
        StringBuffer outbuf = new StringBuffer();
        char[] c = inbuf.toCharArray();
        for (int i = 0; i < c.length; i++)
        {
            if (c[i] == '\"')
                ;
            else if (c[i] == '\\')
                outbuf.append(c[++i]);
            else if (c[i] == ',')
            {
                columns.add(outbuf.toString());
                outbuf.setLength(0);
            }
            else
                outbuf.append(c[i]);
        }
        columns.add(outbuf.toString());
        String[] ret = new String[columns.size()];
        columns.toArray(ret);
        return ret;
    }
    
    public static Bean fromCSVLine(String[] columns, Class<?> beanClass, Map<String,PropertyDescriptor> descriptors, String inbuf) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        String[] vals = fromCSVHeader(inbuf);
        Bean ret = (Bean)beanClass.newInstance();
        for (int i = 0; i < columns.length; i++)
        {
            PropertyDescriptor pd = (PropertyDescriptor)descriptors.get(columns[i]);
            Class<?> valClass = pd.getReadMethod().getReturnType();
            String valName = valClass.getName();
            Object[] val = new Object[1];
            if (valClass == String.class)
                val[0] = vals[i];
            else if (vals[i].length() == 0)
                val[0] = null;
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
            pd.getWriteMethod().invoke(ret, val);
        }
        return ret;
    }
}
