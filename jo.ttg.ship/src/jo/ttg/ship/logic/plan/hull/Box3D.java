package jo.ttg.ship.logic.plan.hull;

import jo.util.geom3d.Block3D;
import jo.util.geom3d.Point3D;

public class Box3D extends Volume3D
{
    // constructors
    public Box3D()
    {
        mAspectRatio.x *= 3;
        mAspectRatio.y *= 4;
        mAspectRatio.z *= 5;
    }
    
    // overrides
    @Override
    public double getVolume()
    {        
        return mAspectRatio.x*2*mAspectRatio.y*2*mAspectRatio.z*2;
    }
    @Override
    public Block3D getBounds()
    {
        return new Block3D(new Point3D(-mAspectRatio.x, -mAspectRatio.y, -mAspectRatio.z), new Point3D(mAspectRatio.x, mAspectRatio.y, mAspectRatio.z));
    }
    
    @Override
    public boolean isInside(Point3D p)
    {
        if ((p.x < -mAspectRatio.x) || (p.x > mAspectRatio.x))
            return false;
        if ((p.y < -mAspectRatio.y) || (p.y > mAspectRatio.y))
            return false;
        if ((p.z < -mAspectRatio.z) || (p.z > mAspectRatio.z))
            return false;
        return true;
    }
    
    // getters and setters
}
