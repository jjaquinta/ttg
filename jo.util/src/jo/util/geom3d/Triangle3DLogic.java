package jo.util.geom3d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.util.utils.MathUtils;

public class Triangle3DLogic
{
    public static Triangle3D makeWRT(Point3D p1, Point3D p2, Point3D p3, Point3D wrt)
    {
        Point3D n = Point3DLogic.average(p1, p2, p3);
        n.sub(wrt);
        return new Triangle3D(p1, p2, p3, n);
    }

    public static Triangle3D makeClockwise(Point3D t1, Point3D t2, Point3D t3)
    {
        Point3D v1 = Point3DLogic.sub(t2, t1);
        Point3D v2 = Point3DLogic.sub(t3, t2);
        Point3D n = Point3DLogic.cross(v1, v2);
        return new Triangle3D(t1, t2, t3, n);
    }
    
    public static Map<LineSegment3D, Triangle3D[]> findInteriorSegments(List<Triangle3D> tris, int start, int length)
    {
        Map<LineSegment3D, Triangle3D[]> segs = new HashMap<>();
        if (length < 0)
            length = tris.size() - start;
        for (int i = start; i < start + length; i++)
        {
            Triangle3D t = tris.get(i);
            addSegment(segs, new LineSegment3D(t.getP1(), t.getP2()), t);
            addSegment(segs, new LineSegment3D(t.getP2(), t.getP3()), t);
            addSegment(segs, new LineSegment3D(t.getP3(), t.getP1()), t);
        }
        // clean out edges
        for (LineSegment3D s : segs.keySet().toArray(new LineSegment3D[0]))
        {
            if (segs.get(s)[1] == null)
                segs.remove(s);
        }
        return segs;
    }

    private static void addSegment(Map<LineSegment3D, Triangle3D[]> segs,
            LineSegment3D seg, Triangle3D t)
    {
        for (LineSegment3D s : segs.keySet())
            if (s.equals(seg))
            {
                Triangle3D[] ts = segs.get(s);
                ts[1] = t;
                return;
            }
        segs.put(seg, new Triangle3D[] { t, null });
    }

    public static Triangle3D rotatePoints(Triangle3D tri, int dir)
    {
        if (dir < 0)
            return new Triangle3D(tri.getP2(), tri.getP3(), tri.getP1(), tri.getN());
        else if (dir > 0)
            return new Triangle3D(tri.getP3(), tri.getP1(), tri.getP2(), tri.getN());
        else
            return new Triangle3D(tri.getP1(), tri.getP2(), tri.getP3(), tri.getN());
    }

    public static Point3D makeNormal(Point3D p1, Point3D p2, Point3D p3)
    {
        Point3D a = p2.sub(p1);
        Point3D b = p3.sub(p1);
        Point3D n = a.cross(b);
        n.normalize();
        return n;
    }

    public static Triangle3D invert(Triangle3D t1)
    {
        Triangle3D t2 = new Triangle3D(
                t1.getP1(), t1.getP3(), t1.getP2(),
                t1.getN().mult(-1));
        return t2;
    }
    
    // https://courses.cs.washington.edu/courses/csep557/10au/lectures/triangle_intersection.pdf
    public static Point3D intersect(Triangle3D tri, Line3D line)
    {   
        Point3D P = line.getP();
        Point3D D = line.getN();
        Point3D A = tri.getP1();
        Point3D B = tri.getP2();
        Point3D C = tri.getP3();
        Point3D n = tri.getN(); // normal to triangle (and plane of triangle)
        double d = Point3DLogic.dot(A, n);
        double denom = Point3DLogic.dot(n, D);
        if (MathUtils.equals(denom, 0))
            return null;
        double t = (d - Point3DLogic.dot(n, P))/denom;
        Point3D Q = Point3DLogic.add(P, Point3DLogic.mult(D, t));
        // is Q inside triangle
        if (Point3DLogic.sub(B, A).cross(Point3DLogic.sub(Q, A)).dot(n) < 0)
            return null;
        if (Point3DLogic.sub(C, B).cross(Point3DLogic.sub(Q, B)).dot(n) < 0)
            return null;
        if (Point3DLogic.sub(A, C).cross(Point3DLogic.sub(Q, C)).dot(n) < 0)
            return null;
        return Q;
    }
}
