package jo.ttg.port.logic.plan;

import java.util.List;

import javax.vecmath.Point3i;

import jo.ttg.port.beans.PortCube;
import jo.ttg.port.beans.PortItemInstance;
import jo.ttg.port.beans.PortPlanBean;

public class PerimeterWalker
{
    private PortPlanBean    mPlan;
    private PortCube mBounds;
    
    private Point3i mAt;
    private int     mSide; // 0 = XM, 1 = YM, 2 = XP, 3 = YP
    
    // constructors
    public PerimeterWalker(PortPlanBean plan, PortCube bounds)
    {
        mPlan = plan;
        mBounds = bounds;
        mAt = new Point3i(mBounds.getX(), mBounds.getY() + mBounds.getDY(), mBounds.getZ());
        mSide = 0;
    }
    
    public int getFrontageLeft()
    {
        if (mSide == 0)
            return mAt.getY() - mBounds.getY();
        if (mSide == 1)
            return mBounds.getX() - mAt.getX();
        if (mSide == 2)
            return mBounds.getY() - mAt.getY();
        if (mSide == 3)
            return mAt.getX() - mBounds.getX();
        throw new IllegalStateException("Not on edge!");
    }
    
    public void advance()
    {
        if (mSide == 0)
            if (mBounds.isOnYM(mAt.getY()))
                mSide = 1;
            else
                mAt.setY(mAt.getY() - 1);
        else if (mSide == 1)
            if (mBounds.isOnXP(mAt.getX()))
                mSide = 2;
            else
                mAt.setX(mAt.getX() + 1);
        else if (mSide == 2)
            if (mBounds.isOnYP(mAt.getY()))
                mSide = 3;
            else
                mAt.setY(mAt.getY() + 1);
        else if (mSide == 3)
            if (mBounds.isOnXM(mAt.getX()))
                mSide = 0;
            else
                mAt.setX(mAt.getX() - 1);
    }
    
    public void advance(int num)
    {
        while (num-- > 0)
            advance();
    }
    
    private void findSpaceFor(int side)
    {
        for (;;)
        {
            int frontage = getFrontageLeft();
            if (frontage >= side)
                return;
            advance();
        }
    }
    
    public void place(PortItemInstance item)
    {
        double ar = item.getItem().getAspectRatio();
        int ss = (int)Math.sqrt(item.getItem().getVolume()/6.25/ar);
        int ls = (int)(ss*ar);
        findSpaceFor(ss);
        int x = mAt.getX();
        int y = mAt.getY();
        int dx = ((mSide == 0) || (mSide == 2)) ? ls : ss;
        int dy = ((mSide == 1) || (mSide == 3)) ? ls : ss;
        if (mSide == 0)
        {
            x -= dx;
            y -= dy - 1;
            advance(dy);
        }
        else if (mSide == 1)
        {
            y -= dy;
            advance(dx);
        }
        else if (mSide == 2)
        {
            x++;
            advance(dy);
        }
        else if (mSide == 3)
        {
            x -= dx - 1;
            y++;
            advance(dx);
        }
        mPlan.setArea(x, y, mAt.getZ(), dx, dy, 1, item);
    }
    
    public void place(List<PortItemInstance> item)
    {
        for (PortItemInstance i : item)
            place(i);
    }
    
    public void placeFirst(List<List<PortItemInstance>> items)
    {
        if (items.size() == 0)
            return;
        place(items.get(0));
        items.remove(0);
    }
        
    // getters and setters

    public PortPlanBean getPlan()
    {
        return mPlan;
    }

    public void setPlan(PortPlanBean plan)
    {
        mPlan = plan;
    }

    public PortCube getBounds()
    {
        return mBounds;
    }

    public void setBounds(PortCube bounds)
    {
        mBounds = bounds;
    }

    public Point3i getAt()
    {
        return mAt;
    }

    public void setAt(Point3i at)
    {
        mAt = at;
    }
}
