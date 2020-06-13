/*
 * Created on Apr 11, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

public class BeanLogic
{   
    private static long nextOID = System.currentTimeMillis();
    
    public static synchronized long getNextOID()
    {
        return nextOID++;
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

    public static Map<String,Object> getValues(Map<String,PropertyDescriptor> descriptors, Bean bean)
    {
        Object[] args = new Object[0];
        Map<String,Object> ret = new HashMap<String,Object>();
        for (String name : descriptors.keySet())
        {
            PropertyDescriptor prop = (PropertyDescriptor)descriptors.get(name);
            try
            {
                Object val = prop.getReadMethod().invoke(bean, args);
                /*
                if (val instanceof Bean)
                {
                    HashMap subValues = getValues((Bean)val);
                    for (Iterator j = subValues.keySet().iterator(); j.hasNext(); )
                    {
                        String subName = (String)j.next();
                        Object subVal = subValues.get(subName);
                        ret.put(name+"."+subName, subVal);
                    }
                }
                else
                */
                    ret.put(name, val);
            }
            catch (Exception e)
            {
            }
        }
        return ret;
    }
    
    public static Map<String,Object> getValues(Bean bean)
    {
        if (bean == null)
            return new HashMap<String,Object>();
        try
        {
            return getValues(getDescriptors(bean.getClass()), bean);
        }
        catch (IntrospectionException e)
        {
            return new HashMap<String,Object>();
        }
    }
}
