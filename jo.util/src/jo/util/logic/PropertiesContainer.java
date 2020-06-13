/*
 * Created on Feb 25, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import jo.util.beans.PropertyContainer;


public class PropertiesContainer implements PropertyContainer
{
    private Properties  mProps;
    
    public PropertiesContainer(Properties props)
    {
        mProps = props;
    }
    
    public Object getProp(String propName)
    {
        return mProps.get(propName);
    }

    public void setProp(String propName, Object value)
    {
        mProps.put(propName, value);        
    }

    public Iterator<String> getKeys()
    {
        List<String> keys = new ArrayList<String>();
        for (Object key : mProps.keySet())
            keys.add((String)key);
        return keys.iterator();
    }

}
