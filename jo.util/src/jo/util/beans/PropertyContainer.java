/*
 * Created on Feb 25, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.beans;

import java.util.Iterator;

public interface PropertyContainer
{
    public Object getProp(String propName);
    public void setProp(String propName, Object value);
    public Iterator<String> getKeys();
}
