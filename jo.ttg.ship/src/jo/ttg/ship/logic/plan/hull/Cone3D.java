package jo.ttg.ship.logic.plan.hull;

import jo.util.geom3d.Block3D;
import jo.util.geom3d.Point3D;
import jo.util.utils.MathUtils;

public class Cone3D extends Volume3D
{
    // constructors
    public Cone3D()
    {
        mAspectRatio.y *= 8;
    }
    
    // overrides
    @Override
    public double getVolume()
    {        
        return Math.PI*mAspectRatio.x*mAspectRatio.y*mAspectRatio.z/3;
    }
    @Override
    public Block3D getBounds()
    {
        return new Block3D(new Point3D(-mAspectRatio.x, -mAspectRatio.y, -mAspectRatio.z), new Point3D(mAspectRatio.x, 0, mAspectRatio.z));
    }
    
    @Override
    public boolean isInside(Point3D p)
    {
        if ((p.y < -mAspectRatio.y) || (p.y > 0))
            return false;
        double r = MathUtils.interpolate(p.y, -mAspectRatio.y, 0, 0, 1);
        double rx = p.x/mAspectRatio.x;
        double rz = p.z/mAspectRatio.z;
        double d = rx*rx + rz*rz;
        return d <= r*r;
    }
    
    // getters and setters
}
