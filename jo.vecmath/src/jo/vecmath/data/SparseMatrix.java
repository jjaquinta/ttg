package jo.vecmath.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3i;

import jo.util.utils.obj.ByteUtils;

public class SparseMatrix<T>
{
    private Map<Long,T> mMatrix;
    private Point3i     mLower;
    private Point3i     mUpper;
    
    public SparseMatrix()
    {
        mMatrix = new HashMap<Long,T>();
        mLower = null;
        mUpper = null;
    }
    
    public SparseMatrix(SparseMatrix<T> original)
    {
        this();
        set(original);
        mLower = new Point3i();
        mUpper = new Point3i();
        original.getBounds(mLower, mUpper);
    }
    
    public void addAll(SparseMatrix<T> original)
    {
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext(); )
        {
            Point3i p = i.next();
            set(p, original.get(p));
        }
    }
    
    public void set(SparseMatrix<T> original)
    {
        mMatrix.clear();
        addAll(original);
    }
    
    public void set(int x, int y, int z, T val)
    {
        long idx = toHashCode(x, y, z);
        if (val == null)
            mMatrix.remove(idx);
        else
            mMatrix.put(idx,  val);
        if (val != null)
        {
            if (mLower == null)
                mLower = new Point3i(x, y, z);
            else
            {
                mLower.x = Math.min(mLower.x, x);
                mLower.y = Math.min(mLower.y, y);
                mLower.z = Math.min(mLower.z, z);
            }
            if (mUpper == null)
                mUpper = new Point3i(x, y, z);
            else
            {
                mUpper.x = Math.max(mUpper.x, x);
                mUpper.y = Math.max(mUpper.y, y);
                mUpper.z = Math.max(mUpper.z, z);
            }
        }
    }
    
    public T get(int x, int y, int z)
    {
        long idx = toHashCode(x, y, z);
        return mMatrix.get(idx);        
    }
    
    public boolean contains(int x, int y, int z)
    {
        return get(x, y, z) != null;
    }
    
    public void set(Point3i v, T val)
    {
        set(v.x, v.y, v.z, val);
    }
    
    public T get(Point3i v)
    {
        return get(v.x, v.y, v.z);
    }
    
    public boolean contains(Point3i v)
    {
        return get(v.x, v.y, v.z) != null;
    }
    
    public void getBounds(Point3i lower, Point3i upper)
    {
        if (mLower != null)
            lower.set(mLower);
        if (mUpper != null)
            upper.set(mUpper);
    }
    
    public void getHorizontalBounds(Point3i lower, Point3i upper)
    {
        boolean first = true;
        for (int x = mLower.x; x <= mUpper.x; x++)
            for (int y = mLower.y; y <= mUpper.y; y++)
                if (get(x, y, lower.z) != null)
                    if (first)
                    {
                        lower.x = x;
                        lower.y = y;
                        upper.x = x;
                        upper.y = y;
                        first = false;
                    }
                    else
                    {
                        lower.x = Math.min(lower.x, x);
                        lower.y = Math.min(lower.y, y);
                        upper.x = Math.max(upper.x, x);
                        upper.y = Math.max(upper.y, y);
                    }
    }
    
    public Iterator<Point3i> iterator()
    {
        return new CubeIterator(mLower, mUpper);
    }
    
    public Iterator<Point3i> iteratorNonNull()
    {
        List<Point3i> points = new ArrayList<Point3i>();
        for (Long l : mMatrix.keySet())
        {
            Point3i p = fromHashCode(l);
            points.add(p);
        }
        return points.iterator();
    }
    
    public int size()
    {
        return mMatrix.size();
    }
    
    public static long toHashCode(int x, int y, int z)
    {
        byte[] buffer = new byte[8];
        buffer[0] = 0;
        ByteUtils.toBytes((short)x, buffer, 1);
        ByteUtils.toBytes((short)y, buffer, 3);
        ByteUtils.toBytes((short)z, buffer, 5);
        buffer[7] = 1;
        return ByteUtils.toLong(buffer);
    }
    
    public static Point3i fromHashCode(long hash)
    {
        byte[] buffer = new byte[8];
        ByteUtils.toBytes(hash, buffer);
        short x = ByteUtils.toShort(buffer, 1);
        short y = ByteUtils.toShort(buffer, 3);
        short z = ByteUtils.toShort(buffer, 5);
        Point3i p = new Point3i(x, y, z);
        return p;
    }
}
