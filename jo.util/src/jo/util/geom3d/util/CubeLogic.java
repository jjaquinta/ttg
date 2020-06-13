package jo.util.geom3d.util;

import jo.util.geom3d.Mesh3D;
import jo.util.geom3d.Point3D;
import jo.util.geom3d.Point3DLogic;
import jo.util.geom3d.Triangle3D;
import jo.util.geom3d.Triangle3DLogic;
import jo.util.utils.MathUtils;

public class CubeLogic
{    
    public static Mesh3D generateCube(Point3D center, double r, double gap)
    {
        Point3D topLeftUp       = center.add(new Point3D(-r, -r,  r));
        Point3D topLeftDown     = center.add(new Point3D(-r,  r,  r));
        Point3D topRightUp      = center.add(new Point3D( r, -r,  r));
        Point3D topRightDown    = center.add(new Point3D( r,  r,  r));
        Point3D bottomLeftUp    = center.add(new Point3D(-r, -r, -r));
        Point3D bottomLeftDown  = center.add(new Point3D(-r,  r, -r));
        Point3D bottomRightUp   = center.add(new Point3D( r, -r, -r));
        Point3D bottomRightDown = center.add(new Point3D( r,  r, -r));
        return generateCube(topLeftUp, topLeftDown, topRightUp, topRightDown,
                bottomLeftUp, bottomLeftDown, bottomRightUp, bottomRightDown,
                gap);
    }
    public static Mesh3D generateRect(Point3D from, Point3D to, double gap)
    {
        Point3D topLeftUp       = new Point3D(from.x, from.y,  to.z);
        Point3D topLeftDown     = new Point3D(from.x, to.y,  to.z);
        Point3D topRightUp      = new Point3D(to.x, from.y,  to.z);
        Point3D topRightDown    = new Point3D(to.x, to.y,  to.z);
        Point3D bottomLeftUp    = new Point3D(from.x, from.y, from.z);
        Point3D bottomLeftDown  = new Point3D(from.x, to.y, from.z);
        Point3D bottomRightUp   = new Point3D(to.x, from.y, from.z);
        Point3D bottomRightDown = new Point3D(to.x, to.y, from.z);
        return generateCube(topLeftUp, topLeftDown, topRightUp, topRightDown,
                bottomLeftUp, bottomLeftDown, bottomRightUp, bottomRightDown,
                gap);
    }
    public static Mesh3D generateCube(Point3D center, Point3D r, double gap)
    {
        Point3D topLeftUp       = center.add(new Point3D(-r.x, -r.y,  r.z));
        Point3D topLeftDown     = center.add(new Point3D(-r.x,  r.y,  r.z));
        Point3D topRightUp      = center.add(new Point3D( r.x, -r.y,  r.z));
        Point3D topRightDown    = center.add(new Point3D( r.x,  r.y,  r.z));
        Point3D bottomLeftUp    = center.add(new Point3D(-r.x, -r.y, -r.z));
        Point3D bottomLeftDown  = center.add(new Point3D(-r.x,  r.y, -r.z));
        Point3D bottomRightUp   = center.add(new Point3D( r.x, -r.y, -r.z));
        Point3D bottomRightDown = center.add(new Point3D( r.x,  r.y, -r.z));
        return generateCube(topLeftUp, topLeftDown, topRightUp, topRightDown,
                bottomLeftUp, bottomLeftDown, bottomRightUp, bottomRightDown,
                gap);
    }
    public static Mesh3D generateCube(Point3D topLeftUp, Point3D topLeftDown, Point3D topRightUp, Point3D topRightDown, 
            Point3D bottomLeftUp, Point3D bottomLeftDown, Point3D bottomRightUp, Point3D bottomRightDown, 
            double gap)
    {
        Point3D center = Point3DLogic.average(topLeftUp, topLeftDown, topRightUp, topRightDown, 
                bottomLeftUp, bottomLeftDown, bottomRightUp, bottomRightDown);
        Mesh3D mesh = new Mesh3D();
        // up face
        CubeLogic.subdivide(mesh, topLeftUp, bottomRightUp, topRightUp, center, gap);
        CubeLogic.subdivide(mesh, topLeftUp, bottomLeftUp, bottomRightUp, center, gap);
        // down face
        CubeLogic.subdivide(mesh, topLeftDown, topRightDown, bottomRightDown, center, gap);
        CubeLogic.subdivide(mesh, topLeftDown, bottomRightDown, bottomLeftDown, center, gap);
        // left face
        CubeLogic.subdivide(mesh, topLeftUp, topLeftDown, bottomLeftDown, center, gap);
        CubeLogic.subdivide(mesh, topLeftUp, bottomLeftDown, bottomLeftUp, center, gap);
        // right face
        CubeLogic.subdivide(mesh, topRightUp, bottomRightDown, topRightDown, center, gap);
        CubeLogic.subdivide(mesh, topRightUp, bottomRightUp, bottomRightDown, center, gap);
        // top face
        CubeLogic.subdivide(mesh, topLeftUp, topRightUp, topRightDown, center, gap);
        CubeLogic.subdivide(mesh, topLeftUp, topRightDown, topLeftDown, center, gap);
        // bottom face
        CubeLogic.subdivide(mesh, bottomLeftUp, bottomRightDown, bottomRightUp, center, gap);
        CubeLogic.subdivide(mesh, bottomLeftUp, bottomLeftDown, bottomRightDown, center, gap);
        return mesh;
    }

    public static void subdivide(Mesh3D mesh, Point3D p1, Point3D p2, Point3D p3, Point3D center, double gap)
    {
        double d1 = p1.dist(p2);
        double d2 = p2.dist(p3);
        double d3 = p3.dist(p1);
        double d = MathUtils.max(d1, d2, d3);
        //System.out.println(p1+" "+p2+" "+p3+", d="+d);
        if (d < gap)
        {
            Point3D t1 = new Point3D(p1);
            Point3D t2 = new Point3D(p2);
            Point3D t3 = new Point3D(p3);
            Triangle3D t = Triangle3DLogic.makeWRT(t1, t2, t3, center);
            mesh.getMesh().add(t);
        }
        else
        {
            Point3D mid12 = Point3DLogic.average(p1, p2);
            Point3D mid23 = Point3DLogic.average(p2, p3);
            Point3D mid31 = Point3DLogic.average(p3, p1);
            subdivide(mesh, p1, mid12, mid31, center, gap);
            subdivide(mesh, p2, mid23, mid12, center, gap);
            subdivide(mesh, p3, mid31, mid23, center, gap);
            subdivide(mesh, mid23, mid31, mid12, center, gap);
        }
    }
}
