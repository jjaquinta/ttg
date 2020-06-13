package jo.util.heal.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.util.heal.GlobeChangeEvent;
import jo.util.heal.IGlobeChangeListener;
import jo.util.heal.IHEALCoord;
import jo.util.heal.IHEALGlobe;
import jo.util.heal.IHEALVector;

public class HEALGlobe<T> implements IHEALGlobe<T>
{
    private int     mResolution;
    private int     mSide;
    private Object[][]   mData;
    private List<IGlobeChangeListener<T>>   mListeners;
    private boolean     mBatchMode;
        
    public HEALGlobe(int resolution)
    {
        mResolution = resolution;
        mSide = (int)Math.pow(2, mResolution);
        mData = new Object[mSide*4][mSide*6];
        mListeners = new ArrayList<IGlobeChangeListener<T>>();
    }
    
    @Override
    public int getResolution()
    {
        return mResolution;
    }
    
    @Override
    public int size()
    {
        return (int)(Math.pow(4, mResolution)*12);
    }
    
    @Override
    public void clear()
    {
        setAll(null);
        fireGlobeChange(GlobeChangeEvent.CHANGE_GLOBE, null, null, null);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void set(IHEALCoord ord, T val)
    {
        T oldVal = get(ord);
        HEALCoord o = (HEALCoord)ord;
        mData[o.getX()][o.getY()] = val;
        T newVal = val;
        fireGlobeChange(GlobeChangeEvent.CHANGE_COORD, ord, oldVal, newVal);
    }
    
    public void setAll(T o)
    {
        setBatchMode(true);
        for (Iterator<IHEALCoord> i = coordsIterator(); i.hasNext(); )
            set(i.next(), o);
        setBatchMode(false);
        fireGlobeChange(GlobeChangeEvent.CHANGE_GLOBE, null, null, null);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public T get(IHEALCoord ord)
    {
        HEALCoord o = (HEALCoord)ord;
        return (T)mData[o.getX()][o.getY()];
    }
    
    public void remove(IHEALCoord ord)
    {
        set(ord, null);
    }
    
    @Override
    public Iterator<T> iterator()
    {
        return new HEALIterateContents();
    }
    
    @Override
    public Iterator<IHEALCoord> coordsIterator()
    {
        return new HEALIterateCoords();
    }
    
    @Override
    public void addGlobeChangeListener(IGlobeChangeListener<T> l)
    {
        synchronized (mListeners)
        {
            mListeners.add(l);
        }
    }
    
    @Override
    public void removeGlobeChangeListener(IGlobeChangeListener<T> l)
    {
        synchronized (mListeners)
        {
            mListeners.remove(l);
        }
    }
    
    private void fireGlobeChange(int id, IHEALCoord coord, T oldVal, T newVal)
    {
        if (mBatchMode)
            return;
        GlobeChangeEvent<T> ev = new GlobeChangeEvent<T>();
        ev.setGlobe(this);
        ev.setID(id);
        ev.setCoord(coord);
        ev.setOldValue(oldVal);
        ev.setNewValue(newVal);
        for (IGlobeChangeListener<T> l : mListeners)
            l.globeChanged(ev);
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
    
    public class HEALCoord implements IHEALCoord
    {
        private int mX;
        private int mY;
        
        // constructors
        
        public HEALCoord()
        {
            mX = 0;
            mY = 0;
        }
        
        public HEALCoord(int x, int y)
        {
            mX = x;
            mY = y;
        }
        
        @SuppressWarnings("unchecked")
        public HEALCoord(IHEALCoord ords)
        {
            HEALCoord o = (HEALCoord)ords;
            mX = o.mX;
            mY = o.mY;
        }
        
        // getters and setters

        public int getX()
        {
            return mX;
        }

        public void setX(int x)
        {
            mX = x;
        }

        public int getY()
        {
            return mY;
        }

        public void setY(int y)
        {
            mY = y;
        }
        
        // utilities
        public boolean isValid()
        {
            if (mY < 0)
                return false;
            if (mY < mSide)
                return (mX >= 0) && (mX < mSide);
            if (mY < mSide*2)
                return (mX >= 0) && (mX < mSide*2);
            if (mY < mSide*3)
                return (mX >= 0) && (mX < mSide*3);
            if (mY < mSide*4)
                return (mX >= mSide*1) && (mX < mSide*4);
            if (mY < mSide*5)
                return (mX >= mSide*2) && (mX < mSide*4);
            if (mY < mSide*6)
                return (mX >= mSide*3) && (mX < mSide*4);
            return false;
        }
        
        public void incr()
        {
            do
            {
                mX++;
                if (mX == mSide*4)
                {
                    mX = 0;
                    mY++;
                    if (mY == mSide*6)
                        mY = 0;
                }
            } while (!isValid());
        }
        
        @Override
        public IHEALCoord next()
        {
            HEALCoord n = new HEALCoord(this);
            n.incr();
            return n;
        }
        
        public int getSide()
        {
            return HEALGlobe.this.mSide;
        }
        
        public HEALCoord copy()
        {
            return new HEALCoord(this);
        }

        @Override
        public IHEALCoord next(int dir)
        {
            HEALGlobe<?>.HEALCoord next = (HEALGlobe<?>.HEALCoord)HEALGlobeDirLogic.next(this, dir);
            return next;
        }

        @Override
        public IHEALVector makeVector(int dir)
        {
            return new HEALVector(this, dir);
        }
        
        @Override
        public int hashCode()
        {
            long bits = 1L;
            bits = 31L * bits + (long)mX;
            bits = 31L * bits + (long)mY;
            return (int) (bits ^ (bits >> 32));
        }
        
        @Override
        public String toString()
        {
            return "["+mX+","+mY+"]";
        }
        
        @Override
        public double[][] getThetaPhiBox()
        {
            return HEALGlobePointLogic.getThetaPhiBox(getX(), getY(), getResolution());
        }
    }
    
    class HEALVector extends HEALCoord implements IHEALVector
    {
        private int mDirection;
        
        public HEALVector()
        {
        }

        public HEALVector(IHEALCoord o)
        {
            super(o);
        }

        public HEALVector(IHEALCoord o, int direction)
        {
            super(o);
            mDirection = direction;
        }

        public HEALVector(IHEALVector o)
        {
            super(o);
            mDirection = o.getDirection();
        }

        public int getDirection()
        {
            return mDirection;
        }

        public void setDirection(int direction)
        {
            mDirection = direction;
        }

        @Override
        public void turnLeft()
        {
            mDirection = (mDirection + D_MAX - 1)%D_MAX;
        }

        @Override
        public void turnRight()
        {
            mDirection = (mDirection + 1)%D_MAX;
        }

        @Override
        public IHEALVector move()
        {
            return HEALGlobeMoveLogic.move(this);
        }
    }
    
    class HEALIterateCoords implements Iterator<IHEALCoord>
    {
        private int mCount;
        private IHEALCoord mP = new HEALCoord();
        
        public HEALIterateCoords()
        {
            mCount = mSide*mSide*12;
            mP = new HEALCoord();
        }

        @Override
        public boolean hasNext()
        {
            return mCount > 0;
        }

        @Override
        public IHEALCoord next()
        {
            mP = mP.next();
            mCount--;
            return mP;
        }
    }

    class HEALIterateContents implements Iterator<T>    
    {
        private Iterator<IHEALCoord> mI;
        
        public HEALIterateContents()
        {
            mI = coordsIterator();
        }

        @Override
        public boolean hasNext()
        {
            return mI.hasNext();
        }

        @Override
        public T next()
        {
            return get(mI.next());
        }
        
    }
}

