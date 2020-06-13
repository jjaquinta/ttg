package jo.util.utils;
import java.awt.Point;
import java.util.Iterator;

public class SquareIterator implements Iterator<Point>
{
    private Point mLower;
    private Point mUpper;
    private Point mDelta;
    private Point mNext;
    
    public SquareIterator(Point lower, Point upper)
    {
        if ((lower == null) || (upper == null))
            mNext = null;
        else
        {
            mLower = new Point(lower);
            mUpper = new Point(upper);
            normalize();
            mDelta = new Point(1, 1);
            mNext = new Point(mLower);
        }
    }

    public SquareIterator(Point lower, Point upper, Point delta)
    {
        if ((lower == null) || (upper == null))
            mNext = null;
        else
        {
            mLower = new Point(lower);
            mUpper = new Point(upper);
            normalize();
            mDelta = new Point(delta);
            mNext = new Point(mLower);
        }
    }

    private void normalize()
    {
        if (mUpper.x < mLower.x)
        {
            int tmp = mLower.x;
            mLower.x = mUpper.x;
            mUpper.x = tmp;
        }
        if (mUpper.y < mLower.y)
        {
            int tmp = mLower.y;
            mLower.y = mUpper.y;
            mUpper.y = tmp;
        }
    }
    
    @Override
    public boolean hasNext()
    {
        return mNext != null;
    }

    @Override
    public Point next()
    {
        if (mNext == null)
            return null;
        Point next = new Point(mNext);
        mNext.x += mDelta.x;
        if (mNext.x > mUpper.x)
        {
            mNext.x = mLower.x;
            mNext.y += mDelta.y;
            if (mNext.y > mUpper.y)
                mNext = null;
        }
        return next;
    }

    @Override
    public void remove()
    {
    }

}
