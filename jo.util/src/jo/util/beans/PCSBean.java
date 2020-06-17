package jo.util.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import jo.util.utils.BeanUtils;

public class PCSBean extends Bean
{
    public static boolean debug = false;

    private boolean mSuspendNotifications;
    private List<PropertyChangeListener>   mGenericListeners;
    private Map<String,List<PropertyChangeListener>>     mSpecificListerners;

    // constructor
    public PCSBean()
    {
        mSuspendNotifications = false;
        mGenericListeners = new ArrayList<PropertyChangeListener>();
        mSpecificListerners = new HashMap<String, List<PropertyChangeListener>>();
    }
    
    private String shortName(Object o)
    {
        Class<?> c;
        if (o instanceof Class)
            c = (Class<?>)o;
        else
            c = o.getClass();
        String ret = c.getName();
        int off = ret.lastIndexOf(".");
        return ret.substring(off+1);
    }
    
    // listeners
	public void addPropertyChangeListener(String prop, PropertyChangeListener pcl)
	{
        if (pcl == null)
            System.out.println("addPropertyChangeListener - WTF???");
        synchronized (mSpecificListerners)
        {
            List<PropertyChangeListener> listeners = mSpecificListerners.get(prop);
            if (listeners == null)
            {
                listeners = new ArrayList<PropertyChangeListener>();
                mSpecificListerners.put(prop, listeners);
            }
            listeners.add(pcl);
        }
        if (debug)
            System.out.println(shortName(pcl)+" listening for chagnes to "+prop+" on "+shortName(this));
	}
    public void addPropertyChangeListener(java.beans.PropertyChangeListener pcl)
    {
        if (pcl == null)
            System.out.println("addPropertyChangeListener - wtf???");
        synchronized (mGenericListeners)
        {
            mGenericListeners.add(pcl);
        }
        if (debug)
            System.out.println(shortName(pcl)+" listening for chagnes on "+shortName(this));
    }
    public void removePropertyChangeListener(java.beans.PropertyChangeListener pcl)
    {
        synchronized (mGenericListeners)
        {
            mGenericListeners.remove(pcl);
        }
        synchronized (mSpecificListerners)
        {
            for (List<PropertyChangeListener> listeners : mSpecificListerners.values())
                listeners.remove(pcl);
        }
        if (debug)
            System.out.println(shortName(pcl)+" no longer listening for chagnes on "+shortName(this));
    }
    public void removePropertyChangeListener(String prop)
    {
        synchronized (mSpecificListerners)
        {
            mSpecificListerners.remove(prop);
        }
    }
    
    private PropertyChangeEvent queueEvent;

	protected void queuePropertyChange(String name, Object oldVal, Object newVal)
	{
		queueEvent = new PropertyChangeEvent(this, name, oldVal, newVal);
	}

	protected void queuePropertyChange(String name, int oldVal, int newVal)
	{
		queueEvent = new PropertyChangeEvent(this, name, new Integer(oldVal), new Integer(newVal));
	}

	protected void queuePropertyChange(String name, long oldVal, long newVal)
	{
		queueEvent = new PropertyChangeEvent(this, name, new Long(oldVal), new Long(newVal));
	}

	protected void queuePropertyChange(String name, double oldVal, double newVal)
	{
		queueEvent = new PropertyChangeEvent(this, name, new Double(oldVal), new Double(newVal));
	}

	protected void queuePropertyChange(String name, boolean oldVal, boolean newVal)
	{
		queueEvent = new PropertyChangeEvent(this, name, new Boolean(oldVal), new Boolean(newVal));
	}
	
    private PropertyChangeListener[] getPropertyChangeListeners(String prop)
    {
        Object[] list1;
        synchronized (mGenericListeners)
        {
            list1 = mGenericListeners.toArray();
        }
        Object[] list2;
        synchronized (mSpecificListerners)
        {
            List<PropertyChangeListener> listeners = mSpecificListerners.get(prop);
            if (listeners != null)
                list2 = listeners.toArray();
            else
                list2 = new Object[0];
        }
        PropertyChangeListener[] ret = new PropertyChangeListener[list1.length + list2.length];
        System.arraycopy(list1, 0, ret, 0, list1.length);
        System.arraycopy(list2, 0, ret, list1.length, list2.length);
        return ret;
    }
    
	protected void firePropertyChange()
	{
        PropertyChangeListener[] pcls = getPropertyChangeListeners(queueEvent.getPropertyName());
        PropertyChangeEvent ev = queueEvent;
        queueEvent = null;
        for (int i = 0; i < pcls.length; i++)
        {
            if (debug)
                System.out.println(shortName(pcls[i])+" hears change on "+shortName(this)+" for "+queueEvent.getPropertyName());
            if (!mSuspendNotifications)
                pcls[i].propertyChange(ev);
        }
	}
	
	public void fireMonotonicPropertyChange(String name, Object val)
	{
	    queuePropertyChange(name, null, val);
	    firePropertyChange();
	}
    
    public void fireMonotonicPropertyChange(String name)
    {
        Object val = BeanUtils.get(this, name);
        fireMonotonicPropertyChange(name, val);
    }

    public boolean isSuspendNotifications()
    {
        return mSuspendNotifications;
    }

    public void setSuspendNotifications(boolean suspendNotifications)
    {
        mSuspendNotifications = suspendNotifications;
    }
    
    public void listen(String prop, final BiConsumer<Object, Object> op)
    {
        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                op.accept(evt.getOldValue(), evt.getNewValue());
            }
        });
    }
}
