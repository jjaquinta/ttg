package jo.util.geom3d.util;

import jo.util.geom3d.Block3D;
import jo.util.geom3d.Mesh3D;
import jo.util.geom3d.Point3D;
import jo.util.geom3d.Point3DLogic;
import jo.util.geom3d.Triangle3D;
import jo.util.utils.MathUtils;

public class TwistLogic
{
    
    public static Mesh3D twist(Mesh3D inmesh, int axis, double degrees, double start, double end)
    {
        Block3D bounds = new Block3D(inmesh);
        Mesh3D outmesh = new Mesh3D();
        double bottom = 0;
        double top = 1;
        if (axis == 0)
        {
            bottom = bounds.x;
            top = bounds.x + bounds.width;
        }
        else if (axis == 1)
        {
            bottom = bounds.y;
            top = bounds.y + bounds.height;
        }
        else if (axis == 2)
        {
            bottom = bounds.z;
            top = bounds.z + bounds.depth;
        }
        double low = bottom + (top - bottom)*(start/100);
        double high = bottom + (top - bottom)*(end/100);
        for (Triangle3D intri : inmesh.getMesh())
        {
            Point3D p1 = twist(axis, degrees, low, high, intri.getP1());
            Point3D p2 = twist(axis, degrees, low, high, intri.getP2());
            Point3D p3 = twist(axis, degrees, low, high, intri.getP3());
            Triangle3D outtri = new Triangle3D(p1, p2, p3);
            outmesh.append(outtri);
        }
        return outmesh;
    }    
    
    private static Point3D twist(int axis, double degrees, double low, double high, Point3D p)
    {
        double angle = degrees/180.0*Math.PI; // to radians
        Point3D ang = new Point3D();
        if (axis == 0)
            ang.x = calcAng(p.x, low, high, angle);
        else if (axis == 1)
            ang.y = calcAng(p.y, low, high, angle);
        else if (axis == 2)
            ang.z = calcAng(p.z, low, high, angle);
        Point3D d = Point3DLogic.rotate(p, ang);
        return d;
    }

    private static double calcAng(double val, double low, double high, double angle)
    {
        if (val < low)
            return 0;
        else if (val > high)
            return angle;
        else
            return MathUtils.interpolate(val, low, high, 0, angle);
    }
}
