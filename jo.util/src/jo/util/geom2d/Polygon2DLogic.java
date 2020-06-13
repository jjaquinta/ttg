package jo.util.geom2d;

import java.util.ArrayList;
import java.util.List;

public class Polygon2DLogic
{
    // return the perimeter
    public static double perimeter(Polygon2D a) {
        double sum = 0.0;
        int N = a.size();
        for (int i = 0; i < N; i++)
            sum = sum + a.p(i).dist(a.p(i+1));
        return sum;
    }

    // return signed area of polygon
    public static double area(Polygon2D a) {
        return area(a.getPoints().toArray(new Point2D[0]));
    }
    
    public static double area(Point2D... a) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum = sum + (a[i].x * a[(i+1)%a.length].y) - (a[i].y * a[(i+1)%a.length].x);
        }
        return 0.5 * sum;
    }

    // return winding of polygon, + = cw, - = ccw
    public static int winding(Polygon2D a) {
        return winding(a.getPoints().toArray(new Point2D[0]));
    }
    
    public static int winding(Point2D... a) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum = sum + (a[(i+1)%a.length].x - a[i].x)*(a[(i+1)%a.length].y + a[i].y);
        }
        return (int)Math.signum(sum);
    }
    
    public static void optimize(Polygon2D poly)
    {
        for (int i = 0; i < poly.size(); i++)
        {
            Point2D p1 = poly.p(i+0);
            Point2D p2 = poly.p(i+1);
            Point2D p3 = poly.p(i+2);
            if (Point2DLogic.isColinear(p1, p2, p3))
            {
                poly.getPoints().remove((i + 1)%poly.getPoints().size());
                i--;
            }
        }
    }
    
    public static boolean contains(double x, double y, Polygon2D poly, List<Polygon2D> holes)
    {
        if (!contains(x,  y, poly))
            return false;
        for (Polygon2D hole : holes)
            if (contains(x, y, hole))
                return false;
        return true;
    }
    
    public static boolean contains(double x, double y, Polygon2D poly)
    {
        return contains(x, y, poly.getPoints().toArray(new Point2D[0]));
    }
    
    public static boolean contains(double x, double y, Point2D... poly)
    {
        int crossings = 0;
        for (int i = 0; i < poly.length; i++) 
        {
            int j = i + 1;
            Point2D pi = poly[i%poly.length];
            Point2D pj = poly[j%poly.length];
            boolean cond1 = (pi.y <= y) && (y < pj.y);
            boolean cond2 = (pj.y <= y) && (y < pi.y);
            if (cond1 || cond2) {
                // need to cast to double
                if (x < (pj.x - pi.x) * (y - pi.y) / (pj.y - pi.y) + pi.x)
                    crossings++;
            }
        }
        if (crossings % 2 == 1) return true;
        else                    return false; 
    }
    
    public static boolean contains(Polygon2D poly, Point2D p)
    {
        return contains(p.x, p.y, poly);
    }
    
    public static boolean contains(Polygon2D p1, Polygon2D p2)
    {
        for (Point2D p : p2.getPoints())
            if (!Polygon2DLogic.contains(p1, p))
                return false;
        return true;
    }
    
    public static boolean contains(Polygon2D p1, List<Polygon2D> h1, Polygon2D p2)
    {
        for (Point2D p : p2.getPoints())
            if (!Polygon2DLogic.contains(p1, p))
                return false;
        List<Line2D> p1l = new ArrayList<>();
        p1l.addAll(toSegs(p1));
        for (Polygon2D h : h1)
            p1l.addAll(toSegs(h));
        for (Line2D p2s : toSegs(p2))
            for (Line2D p1s : p1l)
                if (Line2DLogic.intersects(p2s, p1s) != null)
                    return false;
        return true;
    }
    
    public static boolean contains(List<Point2D> p1, List<Point2D> p2)
    {
        Point2D[] p1p = p1.toArray(new Point2D[0]);
        for (Point2D p : p2)
            if (!contains(p.x, p.y, p1p))
                return false;
        return true;
    }
    
    public static List<Polygon2D> triangulate(Polygon2D poly)
    {
        double[] data = new double[poly.size()*2];
        for (int i = 0; i < poly.size(); i++)
        {
            Point2D p = poly.p(i);
            data[i*2+0] = p.x;
            data[i*2+1] = p.y;
        }
        List<Integer> verts = Earcut.earcut(data);
        List<Polygon2D> tris = new ArrayList<Polygon2D>();
        for (int i = 0; i < verts.size(); i+= 3)
        {
            int v1 = verts.get(i);
            int v2 = verts.get(i+1);
            int v3 = verts.get(i+2);
            Polygon2D tri = new Polygon2D(poly.p(v1), poly.p(v2), poly.p(v3));
            tris.add(tri);
        }
        return tris;
    }

    private static int addData(double[] data, int off, Polygon2D poly)
    {
        for (int i = 0; i < poly.size(); i++)
        {
            Point2D p = poly.p(i);
            data[off++] = p.x;
            data[off++] = p.y;
        }
        return off;
    }
    
    public static List<Polygon2D> triangulate(Polygon2D poly, Polygon2D... holes)
    {
        List<Point2D> points = new ArrayList<>();
        points.addAll(poly.getPoints());
        int dsize = poly.size()*2;
        for (Polygon2D h : holes)
        {
            points.addAll(h.getPoints());
            dsize += h.size()*2;
        }
        double[] data = new double[dsize];
        int[] holeIndicies = new int[holes.length];
        int doff = addData(data, 0, poly);
        int hoff = 0;
        for (Polygon2D h : holes)
        {
            holeIndicies[hoff++] = doff/2;
            doff = addData(data, doff, h);
        }
        List<Integer> verts = Earcut.earcut(data, holeIndicies, 2);
        List<Polygon2D> tris = new ArrayList<Polygon2D>();
        for (int i = 0; i < verts.size(); i+= 3)
        {
            int v1 = verts.get(i);
            int v2 = verts.get(i+1);
            int v3 = verts.get(i+2);
            Polygon2D tri = new Polygon2D(points.get(v1), points.get(v2), points.get(v3));
            tris.add(tri);
        }
        return tris;
    }
    
    public static List<List<Point2D>> segsToLines(List<Line2D> segs)
    {
        List<List<Point2D>> loops = new ArrayList<>();
        List<List<Point2D>> lines = new ArrayList<>();
        //System.out.println(segs.size()+" line segments");
        for (Line2D seg : segs)
        {
            // check if matches current end
            for (List<Point2D> line : lines)
                if (extendLine(line, seg))
                {
                    seg = null;
                    break;
                }
            if (seg == null)
                continue;
            // must be new line
            List<Point2D> newLine = new ArrayList<>();
            newLine.add(seg.p1);
            newLine.add(seg.p2);
            lines.add(newLine);
            //System.out.println("New "+DumpLogic.toString(newLine));
        }
        // consolidate lines
        while (lines.size() > 0)
        {
            //System.out.println(lines.size()+" lines, "+loops.size()+" loops");
            boolean anyJoins = false;
            // look for loops
            anyJoins = collapseLoops(loops, lines, anyJoins);
            // look for connects
            anyJoins = connectSegs(lines, anyJoins);
            if (!anyJoins)
                break;
        }
        return loops;
    }

    private static boolean extendLine(List<Point2D> line, Line2D seg)
    {
        Point2D lineStart = line.get(0);
        Point2D lineEnd = line.get(line.size() - 1);
        if (Point2DLogic.equals(lineStart, seg.p1))
        {
            //System.out.print(DumpLogic.toString(line));
            //System.out.print(" +a "+seg.p1+","+seg.p2);
            line.add(0, seg.p2);
            //System.out.println("->"+DumpLogic.toString(line));
            return true;
        }
        else if (Point2DLogic.equals(lineStart, seg.p2))
        {
            //System.out.print(DumpLogic.toString(line));
            //System.out.print(" +b "+seg.p1+","+seg.p2);
            line.add(0, seg.p1);
            //System.out.println("->"+DumpLogic.toString(line));
            return true;
        }
        else if (Point2DLogic.equals(lineEnd, seg.p1))
        {
            //System.out.print(DumpLogic.toString(line));
            //System.out.print(" +c "+seg.p1+","+seg.p2);
            line.add(seg.p2);
            //System.out.println("->"+DumpLogic.toString(line));
            return true;
        }
        else if (Point2DLogic.equals(lineEnd, seg.p2))
        {
            //System.out.print(DumpLogic.toString(line));
            //System.out.print(" +d "+seg.p1+","+seg.p2);
            line.add(seg.p1);
            //System.out.println("->"+DumpLogic.toString(line));
            return true;
        }
        return false;
    }

    private static boolean connectSegs(List<List<Point2D>> lines,
            boolean anyJoins)
    {
        for (int i = 0; i < lines.size() - 1; i++)
        {
            List<Point2D> line1 = lines.get(i);
            for (int j = i + 1; j < lines.size(); j++)
            {
                List<Point2D> line2 = lines.get(j);
                if (Point2DLogic.equals(line1.get(line1.size()-1), line2.get(0)))
                {   // append line2 to line1
                    for (int k = 1; k < line2.size(); k++)
                        line1.add(line2.get(k));
                    lines.remove(j);
                    j--;
                    anyJoins = true;
                }
                else if (Point2DLogic.equals(line1.get(line1.size()-1), line2.get(line2.size() - 1)))
                {   // append the reverse of line2 to line1
                    for (int k = line2.size() - 2; k >= 0; k--)
                        line1.add(line2.get(k));
                    lines.remove(j);
                    j--;
                    anyJoins = true;
                }
                else if (Point2DLogic.equals(line1.get(0), line2.get(0)))
                {   // prepend reverse of line2 to line1
                    for (int k = 1; k < line2.size(); k++)
                        line1.add(0, line2.get(k));
                    lines.remove(j);
                    j--;
                    anyJoins = true;
                }
                else if (Point2DLogic.equals(line1.get(0), line2.get(line2.size() - 1)))
                {   // prepend line2 to line1
                    for (int k = line2.size() - 2; k >= 0; k--)
                        line1.add(0, line2.get(k));
                    lines.remove(j);
                    j--;
                    anyJoins = true;
                }
            }
        }
        return anyJoins;
    }

    private static boolean collapseLoops(List<List<Point2D>> loops,
            List<List<Point2D>> lines, boolean anyJoins)
    {
        for (int i = 0; i < lines.size(); i++)
        {
            List<Point2D> line1 = lines.get(i);
            if (Point2DLogic.equals(line1.get(0), line1.get(line1.size()-1)))
            {
                line1.remove(0);
                loops.add(line1);
                lines.remove(i);
                i--;
                anyJoins = true;
                //System.out.println("Loop size "+line1.size());
                //for (Point2D p : line1)
                //    System.out.print("  "+p.toString());
                //System.out.println();
            }
        }
        return anyJoins;
    }

    public static Polygon2D merge(Polygon2D p1, Polygon2D p2)
    {
        List<Line2D> segments = new ArrayList<>();
        for (int i = 0; i < p1.size(); i++)
        {
            Line2D seg = new Line2D(p1.p(i), p1.p(i+1));
            if (!p2.contains(seg))
                segments.add(seg);
        }
        for (int i = 0; i < p2.size(); i++)
        {
            Line2D seg = new Line2D(p2.p(i), p2.p(i+1));
            if (!p1.contains(seg))
                segments.add(seg);
        }
        List<List<Point2D>> lines = Polygon2DLogic.segsToLines(segments);
        while (lines.size() > 1)
        {
            int bestA = -1;
            int bestAend = -1;
            int bestB = -1;
            int bestBend = -1;
            double bestDist = -1;
            for (int i = 0; i < lines.size(); i++)
                for (int j = i + 1; j < lines.size(); j++)
                {
                    List<Point2D> line1 = lines.get(i);
                    List<Point2D> line2 = lines.get(j);
                    Point2D line1p1 = line1.get(0);
                    Point2D line1p2 = line1.get(line1.size() - 1);
                    Point2D line2p1 = line2.get(0);
                    Point2D line2p2 = line2.get(line1.size() - 1);
                    double d11t21 = line1p1.dist(line2p1);
                    if ((bestDist < 0) || (d11t21 < bestDist))
                    {
                        bestA = i;
                        bestAend = 1;
                        bestB = j;
                        bestBend = 1;
                        bestDist = d11t21;
                    }
                    double d11t22 = line1p1.dist(line2p2);
                    if (d11t22 < bestDist)
                    {
                        bestA = i;
                        bestAend = 1;
                        bestB = j;
                        bestBend = 2;
                        bestDist = d11t22;
                    }
                    double d12t21 = line1p2.dist(line2p1);
                    if (d12t21 < bestDist)
                    {
                        bestA = i;
                        bestAend = 2;
                        bestB = j;
                        bestBend = 1;
                        bestDist = d12t21;
                    }
                    double d12t22 = line1p2.dist(line2p2);
                    if (d12t22 < bestDist)
                    {
                        bestA = i;
                        bestAend = 2;
                        bestB = j;
                        bestBend = 2;
                        bestDist = d12t22;
                    }
                }
            List<Point2D> line1 = lines.get(bestA);
            List<Point2D> line2 = lines.get(bestB);
            lines.remove(bestB);
            if (bestAend == 1)
                if (bestBend == 1)
                {
                    for (int i = 2; i < line2.size(); i++)
                        line1.add(0, line2.get(i));
                }
                else
                {
                    for (int i = line2.size() - 2; i >= 0; i--)
                        line1.add(0, line2.get(i));
                }
            else
                if (bestBend == 1)
                {
                    for (int i = 2; i < line2.size(); i++)
                        line1.add(line2.get(i));
                }
                else
                {
                    for (int i = line2.size() - 2; i >= 0; i--)
                        line1.add(line2.get(i));
                }
        }
        Polygon2D merged = new Polygon2D();
        merged.setPoints(lines.get(0));
        return merged;
    }

    public static Rectangle2D bounds(Polygon2D poly)
    {
        return bounds(poly.getPoints().toArray(new Point2D[0]));
    }

    public static Rectangle2D bounds(Point2D... p)
    {
        Rectangle2D b = new Rectangle2D(p[0], p[1]);
        for (int i = 2; i < p.length; i++)
            b.extend(p[i]);
        return b;
    }
    
    public static List<Line2D> toSegs(Polygon2D p)
    {
        List<Line2D> segs = new ArrayList<>();
        for (int i = 0; i < p.size(); i++)
            segs.add(new Line2D(p.p(i), p.p(i+1)));
        return segs;
    }

    public static double distanceToEdge(double x, double y, Polygon2D perimiter,
            List<Polygon2D> holes)
    {
        List<Line2D> segs = toSegs(perimiter);
        for (Polygon2D h : holes)
            segs.addAll(toSegs(h));
        Double minDist = null;
        for (Line2D seg : segs)
        {
            double d = Line2DLogic.distanceToSegment(x, y, seg);
            if ((minDist == null) || (d < minDist))
                minDist = d;
        }
        return minDist;
    }
    
    public static Polygon2D shrink(Polygon2D large, double amnt)
    {
        return shrink(large, amnt, null);
    }
    public static Polygon2D shrink(Polygon2D large, double amnt, List<Line2D> lines)
    {
        int w = winding(large);     
        Polygon2D poly = new Polygon2D(large.getPoints());
        poly.setWinding(1);
        if (lines == null)
            lines = new ArrayList<>();
        // create initial segments
        lines.addAll(toSegs(poly));
        //System.out.println(lines.size()+" segments");
       // inflate
        for (Line2D seg : lines)
            Line2DLogic.translateLeft(seg, amnt);
        // join bits
        for (int i = 0; i < lines.size(); i++)
        {
            Line2D s1 = lines.get(i);
            Line2D s2 = lines.get((i+1)%lines.size());
            Point2D p = Line2DLogic.intersects(s1, s2);
            if (p == null)
            {
                lines.add(i+1, new Line2D(s1.p2, s2.p1));
                i++;
                //System.out.println(s1+" and "+s2+" form a gap, adding in extra");
            }
            else
            {
                s1.p2 = p;
                s2.p1 = p;
                //System.out.println(s1+" and "+s2+" intersect at "+p+", clipping");
            }
        }
        //System.out.println(lines.size()+" segments after join");
        // remove closed loops
        int[] cross = new int[2];
        for (;;)
        {
            Point2D p = findCrossedLines(lines, cross);
            if (p == null)
                break;
            //System.out.println(cross[0]+" crosses with "+cross[1]+" "+lines.get(cross[0])+" vs "+lines.get(cross[1])+" at "+p);
            double l1 = lineLength(lines, cross[0], cross[1]);
            double l2 = lineLength(lines, cross[1], cross[0]);
            if (l1 < l2)
                removeBetween(lines, cross[0], cross[1], p);
            else
                removeBetween(lines, cross[1], cross[0], p);
        }
        //System.out.println(lines.size()+" segments after loops");
        // convert to polygon
        Polygon2D small = new Polygon2D();
        for (Line2D seg : lines)
            small.getPoints().add(seg.p1);
        small.setWinding(w);
        return small;
    }
    
    private static void removeBetween(List<Line2D> lines, int p1, int p2, Point2D p)
    {
        Line2D l1 = lines.get(p1);
        Line2D l2 = lines.get(p2);
        l1.p2 = p;
        l2.p1 = p;
        if (p1 < p2)
            for (int k = p2 - 1; k > p1; k--)
                lines.remove(k);
        else
        {
            while (p1 + 1 < lines.size())
                lines.remove(p1 + 1);
            while (p2-- > 0)
                lines.remove(p2);
        }
    }
    
    private static double lineLength(List<Line2D> lines, int p1, int p2)
    {
        double len = 0;
        for (int i = p1; i != p2; i = (i + 1)%lines.size())
        {
            Line2D seg = lines.get(i);
            len += seg.length();
        }
        return len;
    }
    
    private static Point2D findCrossedLines(List<Line2D> lines, int[] cross)
    {
        for (int i = 0; i < lines.size(); i++)
        {
            Line2D s1 = lines.get(i);
            for (int j = 2; j < lines.size() - 2; j++)
            {
                Line2D s2 = lines.get((i+j)%lines.size());
                Point2D p = Line2DLogic.intersects(s1, s2);
                if (p != null)
                {
                    cross[0] = i;
                    cross[1] = (i+j)%lines.size();
                    return p;
                }
            }
        }
        return null;
    }
}
