package jo.util.ui.swing.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class AsyncPropertyChangeListener implements PropertyChangeListener
{
    private PropertyChangeListener  mChain;    
    private List<PropertyChangeEvent>   mStack = new ArrayList<PropertyChangeEvent>();
    
    public AsyncPropertyChangeListener()
    {
    }
    
    public AsyncPropertyChangeListener(PropertyChangeListener chain)
    {
        mChain = chain;
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        synchronized (mStack)
        {
            mStack.add(evt);
        }
        Thread t = new Thread() { public void run() { 
            PropertyChangeEvent evt;
            synchronized (mStack)
            {
                evt = (PropertyChangeEvent)mStack.get(0);
                mStack.remove(0);
            }
            asyncPropertyChange(evt); } };
        t.start();
    }

    public void asyncPropertyChange(PropertyChangeEvent evt)
    {
        if (mChain != null)
            mChain.propertyChange(evt);
    }
}
