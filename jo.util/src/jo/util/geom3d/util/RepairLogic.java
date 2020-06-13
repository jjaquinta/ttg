package jo.util.geom3d.util;

import java.util.HashMap;
import java.util.Map;

import jo.util.geom3d.LineSegment3D;
import jo.util.geom3d.Mesh3D;
import jo.util.geom3d.Point3D;
import jo.util.geom3d.Triangle3D;

public class RepairLogic
{
    public static Mesh3D repair(Mesh3D broken)
    {
        Mesh3D fixed = new Mesh3D();
        fixed.append(broken);
        Map<Triangle3D,MeshLinkage> links = new HashMap<>();
        for (;;)
        {
            Triangle3D unlinked = updateLinkage(fixed, links);
            if (unlinked == null)
                break;
            repair(fixed, unlinked, links);
        }
        return fixed;
    }
    
    private static void repair(Mesh3D fixed, Triangle3D unlinked,
            Map<Triangle3D, MeshLinkage> links)
    {
        MeshLinkage link = links.get(unlinked);
        LineSegment3D edge = link.getOpenEdge();
        Point3D p3 = findClosestOpenVertex(edge, links);
        Triangle3D t = new Triangle3D(edge.getP1(), edge.getP2(), p3, unlinked.getN());
        fixed.append(t);
    }
    
    private static Point3D findClosestOpenVertex(LineSegment3D edge, Map<Triangle3D,MeshLinkage> links)
    {
        Point3D best = null;
        double bestd = 0;
        for (Triangle3D tri : links.keySet())
        {
            MeshLinkage link = links.get(tri);
            if ((link.mP31Tri == null) && (link.mP12Tri == null))
            {
                double d = bestDist(tri.getP1(), edge);
                if ((best == null) || (d < bestd))
                {
                    best = tri.getP1();
                    bestd = d;
                }
            }
            if ((link.mP12Tri == null) && (link.mP23Tri == null))
            {
                double d = bestDist(tri.getP2(), edge);
                if ((best == null) || (d < bestd))
                {
                    best = tri.getP2();
                    bestd = d;
                }
            }
            if ((link.mP23Tri == null) && (link.mP31Tri == null))
            {
                double d = bestDist(tri.getP3(), edge);
                if ((best == null) || (d < bestd))
                {
                    best = tri.getP3();
                    bestd = d;
                }
            }
        }
        return best;
    }
    
    private static double bestDist(Point3D p1, LineSegment3D line)
    {
        double d1 = p1.dist(line.getP1());
        double d2 = p1.dist(line.getP2());
        if (d1 < d2)
            return d1;
        else
            return d2;
    }

    private static Triangle3D updateLinkage(Mesh3D mesh, Map<Triangle3D,MeshLinkage> links)
    {
        Triangle3D unlinked = null;
        for (int i = 0; i < mesh.getMesh().size(); i++)
        {
            Triangle3D tri = mesh.getMesh().get(i);
            MeshLinkage link = links.get(tri);
            if (link == null)
            {
                link = new MeshLinkage();
                link.mTri = tri;
                links.put(tri, link);
            }
            if (link.mP12Tri == null)
                link.mP12Tri = findLink(mesh, tri.getP1(), tri.getP2(), tri);
            if (link.mP23Tri == null)
                link.mP23Tri = findLink(mesh, tri.getP2(), tri.getP3(), tri);
            if (link.mP31Tri == null)
                link.mP31Tri = findLink(mesh, tri.getP3(), tri.getP1(), tri);
            if (link.anyUnlinked())
                unlinked = tri;
        }
        return unlinked;
    }
    
    private static Triangle3D findLink(Mesh3D mesh, Point3D p1, Point3D p2, Triangle3D tri)
    {
        for (Triangle3D t : mesh.getMesh())
            if (t != tri)
                if (isLink(t, p1, p2))
                    return t;
        return null;
    }
    
    private static boolean isLink(Triangle3D tri, Point3D p1, Point3D p2)
    {
        if (tri.getP1().equals(p1))
            if (tri.getP2().equals(p2) || tri.getP3().equals(p2))
                return true;
        if (tri.getP2().equals(p1))
            if (tri.getP3().equals(p2) || tri.getP1().equals(p2))
                return true;
        if (tri.getP3().equals(p1))
            if (tri.getP1().equals(p2) || tri.getP2().equals(p2))
                return true;        
        return false;
    }
}

class MeshLinkage
{
    public Triangle3D mTri;
    public Triangle3D mP12Tri;
    public Triangle3D mP23Tri;
    public Triangle3D mP31Tri;
    public Point3D    mMiddle;
    
    public boolean anyUnlinked()
    {
        return (mP12Tri == null) || (mP23Tri == null) || (mP31Tri == null);
    }
    
    public LineSegment3D getOpenEdge()
    {
        if (mP12Tri == null)
            return new LineSegment3D(mTri.getP1(), mTri.getP2());
        if (mP23Tri == null)
            return new LineSegment3D(mTri.getP2(), mTri.getP3());
        if (mP31Tri == null)
            return new LineSegment3D(mTri.getP3(), mTri.getP1());
        return null;
    }
}
