package jo.ttg.ship.logic.plan.hull;

import jo.util.geom3d.Block3D;
import jo.util.geom3d.Point3D;

public class Cylinder3D extends Volume3D
{
    // constructors
    public Cylinder3D()
    {
        mAspectRatio.y *= 8;
    }
    
    // overrides
    @Override
    public double getVolume()
    {        
        return Math.PI*mAspectRatio.x*mAspectRatio.y*mAspectRatio.z;
    }
    @Override
    public Block3D getBounds()
    {
        return new Block3D(new Point3D(-mAspectRatio.x, -mAspectRatio.y, -mAspectRatio.z), new Point3D(mAspectRatio.x, mAspectRatio.y, mAspectRatio.z));
    }
    
    @Override
    public boolean isInside(Point3D p)
    {
        if ((p.y < -mAspectRatio.y) || (p.y > mAspectRatio.y))
            return false;
        double rx = p.x/mAspectRatio.x;
        double rz = p.z/mAspectRatio.z;
        double d = rx*rx + rz*rz;
        return d <= 1;
    }
    
    // getters and setters
}
