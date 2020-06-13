package jo.ttg.beans.surf;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import jo.util.heal.GlobeChangeEvent;
import jo.util.heal.IGlobeChangeListener;
import jo.util.heal.IHEALCoord;
import jo.util.heal.IHEALGlobe;

public class HEALGlobe<T> implements IHEALGlobe<T>
{
    private int     mResolution;
    private Map<Long,Object>    mData;
    private List<IGlobeChangeListener<T>>   mListeners;
    private boolean     mBatchMode;
        
    public HEALGlobe(int resolution)
    {
        mResolution = resolution;
        mData = new Hashtable<Long,Object>(size());
        mListeners = new ArrayList<IGlobeChangeListener<T>>();
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
    
    public void set(IHEALCoord iord, T o)
    {
        HEALCoord ord = (HEALCoord)iord;
        Object oldVal = mData.get(ord.getMaskedData());
        mData.put(ord.getMaskedData(), o);
        Object newVal = o;
        fireGlobeChange(GlobeChangeEvent.CHANGE_COORD, ord, oldVal, newVal);
    }
    
    public void setAll(Object o)
    {
        for (Iterator<IHEALCoord> i = coordsIterator(); i.hasNext(); )
        {
            HEALCoord c = (HEALCoord)i.next();
            mData.put(c.getMaskedData(), o);
        }
        fireGlobeChange(GlobeChangeEvent.CHANGE_GLOBE, null, null, null);
    }
    
    @SuppressWarnings("unchecked")
    public T get(IHEALCoord iord)
    {
        HEALCoord ord = (HEALCoord)iord;
        return (T)mData.get(ord.getMaskedData());
    }
    
    public void remove(HEALCoord ord)
    {
        Object oldVal = mData.get(ord.getMaskedData());
        mData.remove(new Long(ord.getMaskedData()));
        fireGlobeChange(GlobeChangeEvent.CHANGE_COORD, ord, oldVal, null);
    }
    
    public Iterator<T> iterator()
    {
        return new HEALIterateContents(this);
    }
    
    public Iterator<IHEALCoord> coordsIterator()
    {
        return new HEALIterateCoord(mResolution);
    }
    
    public void addGlobeChangeListener(IGlobeChangeListener<T> l)
    {
        synchronized (mListeners)
        {
            mListeners.add(l);
        }
    }
    
    public void removeGlobeChangeListener(IGlobeChangeListener<T> l)
    {
        synchronized (mListeners)
        {
            mListeners.remove(l);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void fireGlobeChange(int id, HEALCoord coord, Object oldVal, Object newVal)
    {
        if (mBatchMode)
            return;
        GlobeChangeEvent<T> ev = new GlobeChangeEvent<>();
        ev.setGlobe(this);
        ev.setID(id);
        ev.setCoord((IHEALCoord)coord);
        ev.setOldValue((T)oldVal);
        ev.setNewValue((T)newVal);
        Object[] l = mListeners.toArray();
        for (int i = 0; i < l.length; i++)
            ((IGlobeChangeListener<T>)l[i]).globeChanged(ev);
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

    class HEALIterateContents implements Iterator<T>
    {
        HEALGlobe<T>        mGlobe;
        HEALCoord           mLast;
        HEALIterateCoord    mIterator;
        
        public HEALIterateContents(HEALGlobe<T> globe)
        {
            mGlobe = globe;
            mIterator = new HEALIterateCoord(globe.getResolution());
            mLast = null;
        }
        
        public boolean hasNext()
        {
            return mIterator.hasNext();
        }
        
        public T next() throws NoSuchElementException
        {
            mLast = mIterator.next();
            return mGlobe.get((IHEALCoord)mLast);
        }
        
        public void remove()
        {
            if (mLast == null)
                throw new IllegalStateException();
            mGlobe.remove(mLast);
        }
    }

}

