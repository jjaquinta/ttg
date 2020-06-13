package jo.util.geom3d.util;

import jo.util.geom2d.Point2D;
import jo.util.geom2d.Point2DSpline;
import jo.util.geom3d.Block3D;
import jo.util.geom3d.Mesh3D;
import jo.util.geom3d.Point3D;
import jo.util.geom3d.Point3DLogic;
import jo.util.geom3d.Triangle3D;
import jo.util.utils.MathUtils;

public class TransformLogic
{
    /*
    public static Transform3D generate(Point3D p1a, Point3D p2a, Point3D p3a, Point3D p4a, Point3D p1b, Point3D p2b, Point3D p3b, Point3D p4b)
    {
        Transform3D t = new Transform3D();
        
        //
        p1a.x*t.xx + p1a.y*t.xy + p1a.z*t.xz + t.xo = p1b.x;
        p1a.x*t.yx + p1a.y*t.yy + p1a.z*t.yz + t.yo = p1b.y;
        p1a.x*t.zx + p1a.y*t.zy + p1a.z*t.zz + t.zo = p1b.z;
        p2a.x*t.xx + p2a.y*t.xy + p2a.z*t.xz + t.xo = p2b.x;
        p2a.x*t.yx + p2a.y*t.yy + p2a.z*t.yz + t.yo = p2b.y;
        p2a.x*t.zx + p2a.y*t.zy + p2a.z*t.zz + t.zo = p2b.z;
        p3a.x*t.xx + p3a.y*t.xy + p3a.z*t.xz + t.xo = p3b.x;
        p3a.x*t.yx + p3a.y*t.yy + p3a.z*t.yz + t.yo = p3b.y;
        p3a.x*t.zx + p3a.y*t.zy + p3a.z*t.zz + t.zo = p3b.z;
        p4a.x*t.xx + p4a.y*t.xy + p4a.z*t.xz + t.xo = p4b.x;
        p4a.x*t.yx + p4a.y*t.yy + p4a.z*t.yz + t.yo = p4b.y;
        p4a.x*t.zx + p4a.y*t.zy + p4a.z*t.zz + t.zo = p4b.z;
    }
    */
    
    public static Mesh3D scale(Mesh3D inmesh, int axis, double[] scale, double[] position, int[] interpolation)
    {
        return scale(inmesh, axis, 3, 3, scale, position, interpolation);
    }
    
    public static Mesh3D scale(Mesh3D inmesh, int axis, int axis2, int axis3, double[] scale, double[] position, int[] interpolation)
    {
        if ((axis2 == 3) && (axis3 == 3))
        {
            axis2 = (axis + 1)%3;
            axis3 = (axis + 2)%3;
        }
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
        double[] breaks = new double[position.length];
        for (int i = 0; i < position.length; i++)
            breaks[i] = MathUtils.interpolate(position[i], 0, 100, bottom, top);
        Point2DSpline sp = null;
        if (interpolation[0] == 3)
        {
            Point2D[] canonicalPoints = new Point2D[breaks.length];
            for (int i = 0; i < canonicalPoints.length; i++)
                canonicalPoints[i] = new Point2D(breaks[i], scale[i]);
            sp = new Point2DSpline(canonicalPoints);
        }
        for (Triangle3D intri : inmesh.getMesh())
        {
            Point3D p1 = scale(axis, axis2, axis3, breaks, scale, interpolation, intri.getP1(), sp);
            Point3D p2 = scale(axis, axis2, axis3, breaks, scale, interpolation, intri.getP2(), sp);
            Point3D p3 = scale(axis, axis2, axis3, breaks, scale, interpolation, intri.getP3(), sp);
            Triangle3D outtri = new Triangle3D(p1, p2, p3);
            outmesh.append(outtri);
        }
        return outmesh;
    }    
    
    private static Point3D scale(int axis, int axis2, int axis3, double[] breaks, double[] scale, int[] interpolation, Point3D p, Point2DSpline sp)
    {
        Point3D d = new Point3D(p);
        if (axis == 0)
        {
            double m = calcMult(p.x, breaks, scale, interpolation, sp)/100.0;
            if ((axis2 == 1) || (axis3 == 1))
                d.y *= m;
            if ((axis2 == 2) || (axis3 == 2))
                d.z *= m;
        }
        else if (axis == 1)
        {
            double m = calcMult(p.y, breaks, scale, interpolation, sp)/100.0;
            if ((axis2 == 0) || (axis3 == 0))
                d.x *= m;
            if ((axis2 == 2) || (axis3 == 2))
                d.z *= m;
        }
        else if (axis == 2)
        {
            double m = calcMult(p.z, breaks, scale, interpolation, sp)/100.0;
            if ((axis2 == 0) || (axis3 == 0))
                d.x *= m;
            if ((axis2 == 1) || (axis3 == 1))
                d.y *= m;
        }
        return d;
    }

    private static double calcMult(double val, double[] breaks, double[] scale, int[] interpolation, Point2DSpline sp)
    {
        if (val < breaks[0])
            return scale[0];
        else if (val > breaks[breaks.length-1])
            return scale[breaks.length-1];
        else 
            for (int i = 0; i < breaks.length-1; i++)
                if ((val >= breaks[i]) && (val <= breaks[i+1]))
                    if (interpolation[i] == 1)
                        return MathUtils.interpolateSin(val, breaks[i], breaks[i+1], scale[i], scale[i+1]);
                    else if (interpolation[i] == 2)
                        return MathUtils.interpolateCos(val, breaks[i], breaks[i+1], scale[i], scale[i+1]);
                    else if (interpolation[i] == 3)
                        return sp.spline(val).y;
                    else
                        return MathUtils.interpolate(val, breaks[i], breaks[i+1], scale[i], scale[i+1]);
        
        return 1.0;
    }
    
    public static Mesh3D translate(Mesh3D inmesh, double dx, double dy, double dz)
    {
        return translate(inmesh, new Point3D(dx, dy, dz));
    }
    
    public static Mesh3D translate(Mesh3D inmesh, Point3D d)
    {
        Mesh3D outmesh = new Mesh3D();
        for (Triangle3D intri : inmesh.getMesh())
        {
            Point3D p1 = Point3DLogic.add(d, intri.getP1());
            Point3D p2 = Point3DLogic.add(d, intri.getP2());
            Point3D p3 = Point3DLogic.add(d, intri.getP3());
            Triangle3D outtri = new Triangle3D(p1, p2, p3);
            outmesh.append(outtri);
        }
        return outmesh;
    }    
    
    public static Mesh3D rotate(Mesh3D inmesh, double dx, double dy, double dz)
    {
        return translate(inmesh, new Point3D(dx, dy, dz));
    }
    
    public static Mesh3D rotate(Mesh3D inmesh, Point3D d)
    {
        Mesh3D outmesh = new Mesh3D();
        for (Triangle3D intri : inmesh.getMesh())
        {
            Point3D p1 = Point3DLogic.rotate(d, intri.getP1());
            Point3D p2 = Point3DLogic.rotate(d, intri.getP2());
            Point3D p3 = Point3DLogic.rotate(d, intri.getP3());
            Triangle3D outtri = new Triangle3D(p1, p2, p3);
            outmesh.append(outtri);
        }
        return outmesh;
    }    
}
