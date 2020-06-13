package jo.util.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.util.utils.obj.ByteUtils;

public class SparseMatrix2D<T>
{
    private Map<Long,T> mMatrix;
    private Point       mLower;
    private Point       mUpper;
    
    public SparseMatrix2D()
    {
        mMatrix = new HashMap<Long,T>();
        mLower = null;
        mUpper = null;
    }
    
    public SparseMatrix2D(SparseMatrix2D<T> original)
    {
        this();
        set(original);
        mLower = new Point();
        mUpper = new Point();
        original.getBounds(mLower, mUpper);
    }
    
    public void addAll(SparseMatrix2D<T> original)
    {
        for (Iterator<Point> i = original.iteratorNonNull(); i.hasNext(); )
        {
            Point p = i.next();
            set(p, original.get(p));
        }
    }
    
    public void set(SparseMatrix2D<T> original)
    {
        mMatrix.clear();
        addAll(original);
    }
    
    public void set(int x, int y, T val)
    {
        long idx = toHashCode(x, y);
        if (val == null)
            mMatrix.remove(idx);
        else
            mMatrix.put(idx,  val);
        if (val != null)
        {
            if (mLower == null)
                mLower = new Point(x, y);
            else
            {
                mLower.x = Math.min(mLower.x, x);
                mLower.y = Math.min(mLower.y, y);
            }
            if (mUpper == null)
                mUpper = new Point(x, y);
            else
            {
                mUpper.x = Math.max(mUpper.x, x);
                mUpper.y = Math.max(mUpper.y, y);
            }
        }
    }
    
    public T get(int x, int y)
    {
        long idx = toHashCode(x, y);
        return mMatrix.get(idx);        
    }
    
    public boolean contains(int x, int y)
    {
        return get(x, y) != null;
    }
    
    public void set(Point v, T val)
    {
        set(v.x, v.y, val);
    }
    
    public T get(Point v)
    {
        return get(v.x, v.y);
    }
    
    public boolean contains(Point v)
    {
        return get(v.x, v.y) != null;
    }
    
    public void getBounds(Point lower, Point upper)
    {
        if (mLower != null)
        {
            lower.x = mLower.x;
            lower.y = mLower.y;
        }
        if (mUpper != null)
        {
            upper.x = mUpper.x;
            upper.y = mUpper.y;
        }
    }
    
    public Iterator<Point> iterator()
    {
        return new SquareIterator(mLower, mUpper);
    }
    
    public Iterator<Point> iteratorNonNull()
    {
        List<Point> points = new ArrayList<Point>();
        for (Long l : mMatrix.keySet())
        {
            Point p = fromHashCode(l);
            points.add(p);
        }
        return points.iterator();
    }
    
    public int size()
    {
        return mMatrix.size();
    }
    
    public static long toHashCode(int x, int y)
    {
        byte[] buffer = new byte[6];
        ByteUtils.toBytes((int)x, buffer, 0);
        ByteUtils.toBytes((int)y, buffer, 4);
        return ByteUtils.toLong(buffer);
    }
    
    public static Point fromHashCode(long hash)
    {
        byte[] buffer = new byte[8];
        ByteUtils.toBytes(hash, buffer);
        int x = ByteUtils.toInt(buffer, 0);
        int y = ByteUtils.toInt(buffer, 4);
        Point p = new Point(x, y);
        return p;
    }
}
