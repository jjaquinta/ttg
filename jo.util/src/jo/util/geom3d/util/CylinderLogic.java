package jo.util.geom3d.util;

import java.util.ArrayList;
import java.util.List;

import jo.util.geom3d.Mesh3D;
import jo.util.geom3d.Point3D;
import jo.util.geom3d.Point3DLogic;
import jo.util.geom3d.Triangle3D;
import jo.util.geom3d.Triangle3DLogic;
import jo.util.utils.MathUtils;

public class CylinderLogic
{    
    public static Mesh3D generateCylinder(Point3D bottom, Point3D top, double radius, double gap, boolean endcaps)
    {
        Point3D unitAxis = Point3DLogic.sub(top, bottom);
        unitAxis.normalize();
        Point3D radialAxis = Point3DLogic.rightAngle(unitAxis);
        radialAxis.normalize();
        radialAxis.scale(radius);
        List<Point3D> upperRim = new ArrayList<Point3D>();
        List<Point3D> lowerRim = new ArrayList<Point3D>();
        double circ = 2*Math.PI*1;
        int increments = (int)Math.ceil(circ/gap);
        double angle = Math.PI*2/increments;
        for (double a = -Math.PI; a < Math.PI; a += angle)
        {
            Point3D radialT = Point3DLogic.rotateAround(radialAxis, unitAxis, a);
            Point3D upper = Point3DLogic.add(top, radialT);
            Point3D lower = Point3DLogic.add(bottom, radialT);
            upperRim.add(upper);
            lowerRim.add(lower);
        }
        Mesh3D mesh = makeCylinder(endcaps, top, bottom, upperRim, lowerRim);
        return mesh;
    }

    public static Mesh3D generateUnitCylinder(double gap, boolean endcaps)
    {
        Point3D top = new Point3D(0, 0, 1);
        Point3D bottom = new Point3D(0, 0, -1);
        List<Point3D> upperRim = new ArrayList<Point3D>();
        List<Point3D> lowerRim = new ArrayList<Point3D>();
        double circ = 2*Math.PI*1;
        int increments = (int)Math.ceil(circ/gap);
        double angle = Math.PI*2/increments;
        for (double a = -Math.PI; a < Math.PI; a += angle)
        {
            double x = Math.cos(a);
            double y = Math.sin(a);
            Point3D upper = new Point3D(x, y, 1);
            Point3D lower = new Point3D(x, y, -1);
            upperRim.add(upper);
            lowerRim.add(lower);
        }
        Mesh3D mesh = makeCylinder(endcaps, top, bottom, upperRim, lowerRim);
        return mesh;
    }

    private static Mesh3D makeCylinder(boolean endcaps, Point3D top,
            Point3D bottom, List<Point3D> upperRim, List<Point3D> lowerRim)
    {
        Mesh3D mesh = new Mesh3D();
        for (int i = 0; i < upperRim.size(); i++)
        {
            Point3D p1u = upperRim.get(i);
            Point3D p1l = lowerRim.get(i);
            Point3D p2u = upperRim.get((i+1)%upperRim.size());
            Point3D p2l = lowerRim.get((i+1)%lowerRim.size());
            // create sides
            mesh.append(new Triangle3D(p2u, p1u, p1l));
            mesh.append(new Triangle3D(p2u, p1l, p2l));
            if (endcaps)
            {
                mesh.append(new Triangle3D(p1u, p2u, top));
                mesh.append(new Triangle3D(p2l, p1l, bottom));
            }
        }
        return mesh;
    }

    public static void subdivide(Mesh3D mesh, Point3D p1, Point3D p2, Point3D p3, Point3D center, double radius, double gap)
    {
        double d1 = p1.dist(p2);
        double d2 = p2.dist(p3);
        double d3 = p3.dist(p1);
        double d = MathUtils.max(d1, d2, d3);
        //System.out.println(p1+","+p2+","+p3+", d="+d1+","+d2+","+d3+" ["+d+"]");
        if (d < gap/radius)
        {
            Point3D t1 = new Point3D(p1);
            t1.normalize();
            t1.scale(radius);
            t1.incr(center);
            Point3D t2 = new Point3D(p2);
            t2.normalize();
            t2.scale(radius);
            t2.incr(center);
            Point3D t3 = new Point3D(p3);
            t3.normalize();
            t3.scale(radius);
            t3.incr(center);
            Triangle3D t = Triangle3DLogic.makeWRT(t1, t2, t3, center);
            mesh.getMesh().add(t);
        }
        else
        {
            Point3D mid12 = Point3DLogic.average(p1, p2);
            mid12.normalize();
            Point3D mid23 = Point3DLogic.average(p2, p3);
            mid23.normalize();
            Point3D mid31 = Point3DLogic.average(p3, p1);
            mid31.normalize();
            subdivide(mesh, p1, mid12, mid31, center, radius, gap);
            subdivide(mesh, p2, mid23, mid12, center, radius, gap);
            subdivide(mesh, p3, mid31, mid23, center, radius, gap);
            subdivide(mesh, mid23, mid31, mid12, center, radius, gap);
        }
    }
    
    public static Point3D[] ISO_VERTS = new Point3D[] {
            new Point3D(0.0,-1.0,-1.618033988749895),
            new Point3D(0.0,1.0,-1.618033988749895),
            new Point3D(0.0,-1.0,1.618033988749895),
            new Point3D(0.0,1.0,1.618033988749895),
            new Point3D(-1.0,-1.618033988749895,0.0),
            new Point3D(1.0,-1.618033988749895,0.0),
            new Point3D(-1.0,1.618033988749895,0.0),
            new Point3D(1.0,1.618033988749895,0.0),
            new Point3D(-1.618033988749895,0.0,-1.0),
            new Point3D(-1.618033988749895,0.0,1.0),
            new Point3D(1.618033988749895,0.0,-1.0),
            new Point3D(1.618033988749895,0.0,1.0)    
    };
    public static int[][] ISO_TRIS = new int[][] {
        // top
        new int[] { 4, 8, 0 },
        new int[] { 5, 4, 0 },
        new int[] { 10, 5, 0 },
        new int[] { 1, 10, 0 },
        new int[] { 8, 1, 0 },
        //side1
        new int[] { 4, 9, 8 },
        new int[] { 5, 2, 4 },
        new int[] { 10, 11, 5 },
        new int[] { 1, 7, 10 },
        new int[] { 8, 6, 1 },
        //side2
        new int[] { 8, 9, 6 },
        new int[] { 4, 2, 9 },
        new int[] { 7, 1, 6 },
        new int[] { 11, 10, 7 },
        new int[] { 2, 5, 11 },
        //bottom
        new int[] { 3, 11, 7 },
        new int[] { 11, 3, 2 },
        new int[] { 3, 9, 2 },
        new int[] { 3, 6, 9 },
        new int[] { 7, 6, 3 },
    };
    
    public static Mesh3D generateGeodesic(Point3D center, double radius, int frequency)
    {
        Point3D points[] = ISO_VERTS;
        Mesh3D mesh = new Mesh3D();
        for (int i = 0; i < ISO_TRIS.length; i++)
            geodesic(mesh, points[ISO_TRIS[i][0]], points[ISO_TRIS[i][1]], points[ISO_TRIS[i][2]], center, radius, frequency);
        return mesh;
    }
    private static void geodesic(Mesh3D mesh, Point3D p1, Point3D p2, Point3D p3, Point3D center, double radius, int frequency)
    {
        for (int strip = 0; strip < frequency; strip++)
        {
            Point3D topLeft = Point3DLogic.between(p1, p3, strip/(double)frequency);
            Point3D botLeft = Point3DLogic.between(p1, p3, (strip+1)/(double)frequency);
            Point3D topRight = Point3DLogic.between(p1, p2, strip/(double)frequency);
            Point3D botRight = Point3DLogic.between(p1, p2, (strip+1)/(double)frequency);
            int tris = strip*2 + 1;
            Point3D[] tops = new Point3D[strip+1];
            if (tops.length == 1)
                tops[0] = topLeft;
            else
                for (int i = 0; i < tops.length; i++)
                    tops[i] = Point3DLogic.between(topLeft, topRight, i/(double)(tops.length - 1));
            for (int i = 0; i < tops.length; i++)
                tops[i].scale(radius/tops[i].mag());
            Point3D[] bots = new Point3D[strip + 2];
            for (int i = 0; i < bots.length; i++)
            {
                bots[i] = Point3DLogic.between(botLeft, botRight, i/(double)(bots.length - 1));
                bots[i].scale(radius/bots[i].mag());
            }
            for (int tri = 0; tri < tris; tri++)
            {
                if (tri%2 == 0)
                {
                    Point3D top = tops[tri/2];
                    Point3D left = bots[tri/2];
                    Point3D right = bots[tri/2+1];
                    mesh.append(new Triangle3D(top, right, left));
                }
                else
                {
                    Point3D top = bots[tri/2+1];
                    Point3D left = tops[tri/2+1];
                    Point3D right = tops[tri/2];
                    mesh.append(new Triangle3D(top, right, left));
                }
            }
        }
    }
}
