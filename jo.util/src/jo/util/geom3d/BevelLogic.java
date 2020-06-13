package jo.util.geom3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.util.geom2d.Point2D;
import jo.util.geom2d.Point2DLogic;
import jo.util.geom2d.Polygon2D;
import jo.util.geom2d.Polygon2DLogic;
import jo.util.geom2d.Rectangle2D;
import jo.util.utils.MathUtils;

public class BevelLogic
{
    public static final int NONE = 0;
    public static final int SHARP = 1;
    public static final int DOME = 2;
    public static final int DIP = 3;
    public static final int VALLEY = 4;
    
    public static final int PIECE_EDGE = 0;
    public static final int BOUNDARY_EDGE = 1;

    public static Mesh3D bevel(Polygon2D perimiter, List<Polygon2D> holes,
            int bevelType, double baseHeight, double bevelHeight, double resolution, int edgeType)
    {
        Mesh3D mesh = new Mesh3D();
        Rectangle2D bounds = Polygon2DLogic.bounds(perimiter);

        // calculate grid points
        double limit = resolution;
        Point2D[][] gridPoints = new Point2D[(int)Math.ceil(bounds.height()/limit)][(int)Math.ceil(bounds.width()/limit)];
        Map<Point2D, Double> gridDistance = new HashMap<>();
        double maxDist = 0;
        for (int ix = 0; ix*limit < bounds.width(); ix++)
        {
            double x = bounds.p1.x + ix*limit;
            for (int iy = 0; iy*limit < bounds.height(); iy++)
            {
                double y = bounds.p1.y + iy*limit;
                if (!Polygon2DLogic.contains(x,  y, perimiter, holes))
                    continue;
                Point2D m = new Point2D(x, y);
                gridPoints[iy][ix] = m;
                double d;
                if (edgeType == BOUNDARY_EDGE)
                    d = Point2DLogic.dist(x, y, bounds.midX(), bounds.midY());
                else // if (edgeType == PIECE_EDGE)
                    d = Polygon2DLogic.distanceToEdge(x, y, perimiter, holes);
                gridDistance.put(m, d);
                maxDist = Math.max(maxDist, d);
            }
        }
        // calculate grid boxes
        List<Polygon2D> boxes = new ArrayList<>();
        for (int y = 0; y < gridPoints.length - 1; y++)
        {
            for (int x = 0; x < gridPoints[y].length - 1; x++)
            {
                if ((gridPoints[y][x] == null) || (gridPoints[y+1][x] == null) || (gridPoints[y][x+1] == null) || (gridPoints[y+1][x+1] == null))
                    continue;
                Polygon2D square = new Polygon2D();
                square.getPoints().add(gridPoints[y][x]);
                square.getPoints().add(gridPoints[y][x+1]);
                square.getPoints().add(gridPoints[y+1][x+1]);
                square.getPoints().add(gridPoints[y+1][x]);
                if (!Polygon2DLogic.contains(perimiter, holes, square))
                    continue;
                boxes.add(square);
            }
        }
        holes.addAll(boxes);

        // calculate max radius
        double radius = calculateRadius(perimiter, gridPoints);
        System.out.println("Radius="+radius);
        
        List<Polygon2D> tris = Polygon2DLogic.triangulate(perimiter, holes.toArray(new Polygon2D[0]));
        for (Polygon2D tri : tris)
        {
            Triangle3D t = makeTriangle(bevelType, baseHeight,
                    bevelHeight, gridDistance, maxDist, tri.p(0), tri.p(1), tri.p(2));
            mesh.append(t);
        }
        for (Polygon2D box : boxes)
        {
            Triangle3D t1 = makeTriangle(bevelType, baseHeight,
                    bevelHeight, gridDistance, maxDist, box.p(0), box.p(1), box.p(2));
            mesh.append(t1);
            Triangle3D t2 = makeTriangle(bevelType, baseHeight,
                    bevelHeight, gridDistance, maxDist, box.p(2), box.p(3), box.p(0));
            mesh.append(t2);
        }
        return mesh;
    }

    private static Triangle3D makeTriangle(int bevelType, double baseHeight,
            double bevelHeight,
            Map<Point2D, Double> gridDistance, double maxDist, Point2D p1,
            Point2D p2, Point2D p3)
    {
        Point3D t1 = makePoint(p1, baseHeight, bevelHeight, bevelType, maxDist, gridDistance);
        Point3D t2 = makePoint(p2, baseHeight, bevelHeight, bevelType, maxDist, gridDistance);
        Point3D t3 = makePoint(p3, baseHeight, bevelHeight, bevelType, maxDist, gridDistance);
        Triangle3D t = new Triangle3D(t1, t2, t3);
        return t;
    }
    
    private static Point3D makePoint(Point2D p2, double baseHeight, double bevelHeight, int bevelType, double radius,
            Map<Point2D, Double> gridDistance)
    {
        Point3D p3 = new Point3D(p2.x, p2.y, baseHeight);
        for (Point2D p : gridDistance.keySet())
            if (p.equals(p2))
            {
                double r = gridDistance.get(p);
                double h;
                switch (bevelType)
                {
                    case DOME:
                        h = MathUtils.interpolateSin(r, 0, radius, 0, bevelHeight);
                        break;
                    case DIP:
                        h = MathUtils.interpolateCos(r, radius, 0, 0, bevelHeight);
                        break;
                    case SHARP:
                        h = MathUtils.interpolate(r, 0, radius, 0, bevelHeight);
                        break;
                    case VALLEY:
                        h = MathUtils.interpolate(r, radius, 0, 0, bevelHeight);
                        break;
                    default:
                        h = 0;
                        break;
                }
                p3.z += h;
                break;
            }
        return p3;
    }

    private static double calculateRadius(Polygon2D perimiter, Point2D[][] grid)
    {
        double diameter = 0;
        for (int i = 0; i < perimiter.size() - 1; i++)
        {
            Point2D p1 = perimiter.p(i);
            for (int j = i + 1; j < perimiter.size(); j++)
            {
                Point2D p2 = perimiter.p(j);
                double d = p1.dist(p2);
                diameter = Math.max(diameter, d);
            }
        }
        double radius = diameter/2;
        return radius;
    }
}
