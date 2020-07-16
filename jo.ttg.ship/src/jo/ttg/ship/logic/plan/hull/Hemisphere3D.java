package jo.ttg.ship.logic.plan.hull;

import jo.util.geom3d.Block3D;
import jo.util.geom3d.Point3D;

public class Hemisphere3D extends Volume3D
{
    // constructors
    public Hemisphere3D()
    {
    }
    
    // overrides
    @Override
    public double getVolume()
    {        
        return 4.0/3.0*Math.PI*mAspectRatio.x*mAspectRatio.y*mAspectRatio.z/2;
    }
    @Override
    public Block3D getBounds()
    {
        return new Block3D(new Point3D(-mAspectRatio.x, -mAspectRatio.y, -mAspectRatio.z), new Point3D(mAspectRatio.x, mAspectRatio.y, 0));
    }
    
    @Override
    public boolean isInside(Point3D p)
    {
        if (p.z > 0)
            return false;
        double d = Math.pow(p.x/mAspectRatio.x,2) + Math.pow(p.y/mAspectRatio.y,2) + Math.pow(p.z/mAspectRatio.z,2);
        return d <= 1;
    }
    
    // getters and setters
}
