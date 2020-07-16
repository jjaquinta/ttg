package jo.ttg.ship.logic.plan.hull;

import jo.util.geom3d.Block3D;
import jo.util.geom3d.Point3D;

public class Sphere3D extends Volume3D
{
    // constructors
    public Sphere3D()
    {
    }
    
    // overrides
    @Override
    public double getVolume()
    {        
        return 4.0/3.0*Math.PI*mAspectRatio.x*mAspectRatio.y*mAspectRatio.z;
    }
    @Override
    public Block3D getBounds()
    {
        return new Block3D(new Point3D(-mAspectRatio.x, -mAspectRatio.y, -mAspectRatio.z), new Point3D(mAspectRatio.x, mAspectRatio.y, mAspectRatio.z));
    }
    
    @Override
    public boolean isInside(Point3D p)
    {
        double rx = p.x/mAspectRatio.x;
        double ry = p.y/mAspectRatio.y;
        double rz = p.z/mAspectRatio.z;
        double d = rx*rx + ry*ry + rz*rz;
        return d <= 1;
    }
    
    // getters and setters
}
