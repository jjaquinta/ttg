package jo.ttg.beans.surf.old;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import jo.ttg.beans.URIBean;

public class HEALGlobe implements URIBean
{
    private String  mURI;
    private int     mResolution;
    private Map<Long,Object>    mData;
    private List<GlobeChangeListener>   mListeners;
    private boolean     mBatchMode;
        
    public HEALGlobe(int resolution)
    {
        mResolution = resolution;
        mData = new Hashtable<Long,Object>(size());
        mListeners = new ArrayList<GlobeChangeListener>();
    }
    
    public int getResolution()
    {
        return mResolution;
    }
    
    public int size()
    {
        return (int)(Math.pow(4, mResolution)*12);
    }
    
    public void clear()
    {
        mData.clear();
        fireGlobeChange(GlobeChangeEvent.CHANGE_GLOBE, null, null, null);
    }
    
    public void set(HEALCoord ord, Object o)
    {
        Object oldVal = mData.get(ord.getMaskedData());
        mData.put(ord.getMaskedData(), o);
        Object newVal = o;
        fireGlobeChange(GlobeChangeEvent.CHANGE_COORD, ord, oldVal, newVal);
    }
    
    public void setAll(Object o)
    {
        for (Iterator<HEALCoord> i = coordsIterator(); i.hasNext(); )
        {
            HEALCoord c = i.next();
            mData.put(c.getMaskedData(), o);
        }
        fireGlobeChange(GlobeChangeEvent.CHANGE_GLOBE, null, null, null);
    }
    
    public Object get(HEALCoord ord)
    {
        return mData.get(ord.getMaskedData());
    }
    
    public void remove(HEALCoord ord)
    {
        Object oldVal = mData.get(ord.getMaskedData());
        mData.remove(new Long(ord.getMaskedData()));
        fireGlobeChange(GlobeChangeEvent.CHANGE_COORD, ord, oldVal, null);
    }
    
    public Iterator<Object> iterator()
    {
        return new HEALIterateContents(this);
    }
    
    public Iterator<HEALCoord> coordsIterator()
    {
        return new HEALIterateCoord(mResolution);
    }
    
    public void addGlobeChangeListener(GlobeChangeListener l)
    {
        synchronized (mListeners)
        {
            mListeners.add(l);
        }
    }
    
    public void removeGlobeChangeListener(GlobeChangeListener l)
    {
        synchronized (mListeners)
        {
            mListeners.remove(l);
        }
    }
    
    private void fireGlobeChange(int id, HEALCoord coord, Object oldVal, Object newVal)
    {
        if (mBatchMode)
            return;
        GlobeChangeEvent ev = new GlobeChangeEvent();
        ev.setGlobe(this);
        ev.setID(id);
        ev.setCoord(coord);
        ev.setOldValue(oldVal);
        ev.setNewValue(newVal);
        Object[] l = mListeners.toArray();
        for (int i = 0; i < l.length; i++)
            ((GlobeChangeListener)l[i]).globeChanged(ev);
    }
    /**
     * @return
     */
    public boolean isBatchMode()
    {
        return mBatchMode;
    }

    /**
     * @param b
     */
    public void setBatchMode(boolean b)
    {
        mBatchMode = b;
        if (!mBatchMode)
            fireGlobeChange(GlobeChangeEvent.CHANGE_GLOBE, null, null, null);
    }

    public String getURI()
    {
        return mURI;
    }

    public void setURI(String uRI)
    {
        mURI = uRI;
    }

}

class HEALIterateContents implements Iterator<Object>
{
    HEALGlobe           mGlobe;
    HEALCoord           mLast;
    HEALIterateCoord    mIterator;
    
    public HEALIterateContents(HEALGlobe globe)
    {
        mGlobe = globe;
        mIterator = new HEALIterateCoord(globe.getResolution());
        mLast = null;
    }
    
    public boolean hasNext()
    {
        return mIterator.hasNext();
    }
    
    public Object next() throws NoSuchElementException
    {
        mLast = mIterator.next();
        return mGlobe.get(mLast);
    }
    
    public void remove()
    {
        if (mLast == null)
            throw new IllegalStateException();
        mGlobe.remove(mLast);
    }
}

