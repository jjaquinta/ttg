package jo.ttg.core.ui.swing.ship.sys.twod;

import jo.ttg.beans.sys.BodyBean;
import jo.util.geom3d.Point3D;

public class TransformedBody
{
    private BodyBean    mBody;
    private Point3D     mLocation;
    private double      mDist;
    private double      mTheta;
    private double      mPhi;
    private double      mX;
    private double      mY;
    private boolean     mInFieldOfView;
    private double      mApparentRadius;
    
    public BodyBean getBody()
    {
        return mBody;
    }
    public void setBody(BodyBean body)
    {
        mBody = body;
    }
    public Point3D getLocation()
    {
        return mLocation;
    }
    public void setLocation(Point3D location)
    {
        mLocation = location;
    }
    public double getTheta()
    {
        return mTheta;
    }
    public void setTheta(double theta)
    {
        mTheta = theta;
    }
    public double getPhi()
    {
        return mPhi;
    }
    public void setPhi(double phi)
    {
        mPhi = phi;
    }
    public double getApparentRadius()
    {
        return mApparentRadius;
    }
    public void setApparentRadius(double apparentRadius)
    {
        mApparentRadius = apparentRadius;
    }
    public double getDist()
    {
        return mDist;
    }
    public void setDist(double dist)
    {
        mDist = dist;
    }
    public double getX()
    {
        return mX;
    }
    public void setX(double x)
    {
        mX = x;
    }
    public double getY()
    {
        return mY;
    }
    public void setY(double y)
    {
        mY = y;
    }
    public boolean isInFieldOfView()
    {
        return mInFieldOfView;
    }
    public void setInFieldOfView(boolean inFieldOfView)
    {
        mInFieldOfView = inFieldOfView;
    }
}
