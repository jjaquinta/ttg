package jo.util.geom3d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import jo.util.geom3d.Obj3D.IDirective;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.IntegerUtils;

public class Obj3DLogic
{
    public static Obj3D read(File f) throws IOException
    {
        Obj3D o = new Obj3D();
        BufferedReader rdr = new BufferedReader(new FileReader(f));
        List<Obj3D.IDirective> group = o.getD();
        for (;;)
        {
            String inbuf = rdr.readLine();
            if (inbuf == null)
                break;
            if (inbuf.startsWith("o "))
            {
                String fname = inbuf.substring(2).trim();
                group = new ArrayList<>();
                o.getO().put(fname, group);
            }
            else if (inbuf.startsWith("v "))
                o.getV().add(parse(inbuf.substring(2)));
            else if (inbuf.startsWith("vt "))
                o.getVT().add(parse(inbuf.substring(3)));
            else if (inbuf.startsWith("vn "))
                o.getVN().add(parse(inbuf.substring(3)));
            else if (inbuf.startsWith("f "))
                group.add(parseFace(o, inbuf.substring(2)));
            else
            {
                Obj3D.Misc line = o.new Misc();
                line.setCommand(inbuf);
                group.add(line);
            }
        }
        rdr.close();
        return o;
    }
    
    private static IDirective parseFace(Obj3D o, String inbuf)
    {
        Obj3D.Face face = o.new Face();
        for (StringTokenizer st = new StringTokenizer(inbuf, " "); st.hasMoreTokens(); )
        {
            Obj3D.FacePoint point = o.new FacePoint();
            face.getPoints().add(point);
            String token = st.nextToken();
            StringTokenizer st2 = new StringTokenizer(token, "/");
            int vIdx = IntegerUtils.parseInt(st2.nextToken());
            point.setV(o.getV().get(vIdx - 1));
            if (st2.hasMoreTokens())
            {
                int vtIdx = IntegerUtils.parseInt(st2.nextToken());
                point.setVT(o.getVT().get(vtIdx - 1));
                if (st2.hasMoreTokens())
                {
                    int vnIdx = IntegerUtils.parseInt(st2.nextToken());
                    point.setVN(o.getVN().get(vnIdx - 1));
                }
            }
        }
        return face;
    }

    private static Point3D parse(String txt)
    {
        StringTokenizer st = new StringTokenizer(txt, " ");
        Point3D p = new Point3D();
        if (st.hasMoreTokens())
            p.x = DoubleUtils.parseDouble(st.nextToken());
        if (st.hasMoreTokens())
            p.y = DoubleUtils.parseDouble(st.nextToken());
        if (st.hasMoreTokens())
            p.z = DoubleUtils.parseDouble(st.nextToken());
        return p;
    }

    public static void write(Obj3D o, File f) throws IOException
    {
        BufferedWriter wtr = new BufferedWriter(new FileWriter(f));
        List<IDirective> globals = new ArrayList<>();
        globals.addAll(o.getD());
        // global non-face statements
        while (globals.size() > 0)
        {
            IDirective d = globals.get(0);
            if (d instanceof Obj3D.Face)
                break;
            if (d instanceof Obj3D.Misc)
            {
                wtr.write(((Obj3D.Misc)d).getCommand());
                wtr.newLine();
            }
            globals.remove(0);
        }
        // vertexes
        for (int i = 0; i < o.getV().size(); i++)
        {
            Point3D p = o.getV().get(i);
            wtr.write("v "+p.x+" "+p.y+" "+p.z);
            wtr.newLine();
        }
        // vertex textures
        for (int i = 0; i < o.getVT().size(); i++)
        {
            Point3D p = o.getVT().get(i);
            wtr.write("vt "+p.x+" "+p.y);
            wtr.newLine();
        }
        // vertex normals
        for (int i = 0; i < o.getVN().size(); i++)
        {
            Point3D p = o.getVN().get(i);
            wtr.write("vn "+p.x+" "+p.y+" "+p.z);
            wtr.newLine();
        }
        // global face statements
        writeDirectives(wtr, o, globals);
        for (String objName : o.getO().keySet())
        {
            wtr.write("o "+objName);
            wtr.newLine();
            writeDirectives(wtr, o, o.getO().get(objName));
        }
        wtr.close();
    }

    public static Obj3D split(Obj3D o, String objectName)
    {
        Obj3D child = new Obj3D();
        child.getD().addAll(o.getD());
        List<IDirective> ds = o.getO().get(objectName);
        for (IDirective d : ds)
        {
            if (d instanceof Obj3D.Face)
            {
                Obj3D.Face face = (Obj3D.Face)d;
                for (Obj3D.FacePoint fp : face.getPoints())
                {
                    if (child.getV().indexOf(fp.getV()) < 0)
                        child.getV().add(fp.getV());
                    if ((child.getVT() != null) && (child.getVT().indexOf(fp.getVT()) < 0))
                        child.getVT().add(fp.getVT());
                    if ((child.getVN() != null) && (child.getVN().indexOf(fp.getVN()) < 0))
                        child.getVN().add(fp.getVN());
                }
            }
            child.getD().add(d);
        }        
        return child;
    }

    private static void writeDirectives(BufferedWriter wtr, Obj3D o, List<IDirective> directives) throws IOException
    {
        for (IDirective d : directives)
        {
            if (d instanceof Obj3D.Face)
                wtr.write(toString(o, (Obj3D.Face)d));
            if (d instanceof Obj3D.Misc)
                wtr.write(((Obj3D.Misc)d).getCommand());
            wtr.newLine();
        }

    }
    
    private static String toString(Obj3D o, Obj3D.Face f)
    {
        StringBuffer sb = new StringBuffer("f ");
        for (Obj3D.FacePoint fp : f.getPoints())
            sb.append(toString(o, fp));
        return sb.toString();
    }
    
    private static String toString(Obj3D o, Obj3D.FacePoint fp)
    {
        StringBuffer sb = new StringBuffer(" ");
        int vIdx = o.getV().indexOf(fp.getV());
        sb.append(String.valueOf(vIdx+1));
        if (fp.getVT() != null)
        {
            sb.append("/");
            int vtIdx = o.getVT().indexOf(fp.getVT());
            sb.append(String.valueOf(vtIdx+1));
            if (fp.getVN() != null)
            {
                sb.append("/");
                int vnIdx = o.getVN().indexOf(fp.getVN());
                sb.append(String.valueOf(vnIdx+1));
            }
        }
        return sb.toString();
    }
    
    public static Block3D getBounds(Obj3D o)
    {
        Block3D b = null;
        for (Point3D v : o.getV())
            if (b == null)
                b = new Block3D(v, new Point3D());
            else
                b.extend(v);
        return b;
    }
    
    public static Point3D getCenter(Obj3D o)
    {
        Block3D b = getBounds(o);
        return b.getCenter();
    }

    public static void scale(Obj3D o, double scale)
    {
        for (Point3D p : o.getV())
            p.scale(scale);
    }
    
    public static void extrude(Obj3D o, double dist)
    {
        Point3D center = getCenter(o);
        Set<String> allEdges = new HashSet<>();
        Set<String> interiorEdges = new HashSet<>();
        findEdges(o, allEdges, interiorEdges);
        Map<Point3D, Point3D> cacheV = new HashMap<Point3D, Point3D>();
        Map<Point3D, Point3D> cacheVN = new HashMap<Point3D, Point3D>();
        int faceCount = o.getD().size();
        addBottom(o, dist, center, cacheV, cacheVN, faceCount);
        addEdge(o, dist, center, cacheV, cacheVN, interiorEdges, faceCount);
    }

    private static void addEdge(Obj3D o, double dist, Point3D center,
            Map<Point3D, Point3D> cacheV, Map<Point3D, Point3D> cacheVN,
            Set<String> interiorEdges,
            int faceCount)
    {
        for (int i = 0; i < faceCount; i++)
        {
            IDirective d = o.getD().get(i);
            if (d instanceof Obj3D.Face)
            {
                Obj3D.Face outerFace = (Obj3D.Face)d;
                for (int j = 0; j < outerFace.getPoints().size(); j++)
                {
                    Obj3D.FacePoint e1 = (Obj3D.FacePoint)outerFace.getPoints().get(j);
                    Obj3D.FacePoint e2 = (Obj3D.FacePoint)outerFace.getPoints().get((j+1)%outerFace.getPoints().size());
                    String id = makeEdge(e1, e2);
                    if (interiorEdges.contains(id))
                        continue;
                    Obj3D.Face edgeFace = o.new Face();
                    Obj3D.FacePoint p1 = o.new FacePoint();
                    p1.setV(e2.getV());
                    p1.setVN(e2.getVN());
                    p1.setVT(e2.getVT());
                    Obj3D.FacePoint p2 = o.new FacePoint();
                    p2.setV(e1.getV());
                    p2.setVN(e1.getVN());
                    p2.setVT(e1.getVT());
                    Obj3D.FacePoint p3 = o.new FacePoint();
                    p3.setV(projectV(e1.getV(), center, dist, cacheV));
                    p3.setVN(flipVN(e1.getVN(), center, dist, cacheVN));
                    p3.setVT(e1.getVT());
                    Obj3D.FacePoint p4 = o.new FacePoint();
                    p4.setV(projectV(e2.getV(), center, dist, cacheV));
                    p4.setVN(flipVN(e2.getVN(), center, dist, cacheVN));
                    p4.setVT(e2.getVT());

                    edgeFace.getPoints().add(p1);
                    edgeFace.getPoints().add(p2);
                    edgeFace.getPoints().add(p3);
                    edgeFace.getPoints().add(p4);
                    o.getD().add(edgeFace);
                    addIfNew(o, edgeFace);
                }
            }
        }
    }

    private static void addBottom(Obj3D o, double dist, Point3D center,
            Map<Point3D, Point3D> cacheV, Map<Point3D, Point3D> cacheVN,
            int faceCount)
    {
        for (int i = 0; i < faceCount; i++)
        {
            IDirective d = o.getD().get(i);
            if (d instanceof Obj3D.Face)
            {
                Obj3D.Face outerFace = (Obj3D.Face)d;
                Obj3D.Face innerFace = o.new Face();
                for (int j = 0; j < outerFace.getPoints().size(); j++)
                {
                    Obj3D.FacePoint outerPoint = (Obj3D.FacePoint)outerFace.getPoints().get(j);
                    Obj3D.FacePoint innerPoint = o.new FacePoint();
                    innerPoint.setV(projectV(outerPoint.getV(), center, dist, cacheV));
                    innerPoint.setVT(outerPoint.getVT());
                    innerPoint.setVN(flipVN(outerPoint.getVN(), center, dist, cacheVN));
                    innerFace.getPoints().add(0, innerPoint);                    
                }
                o.getD().add(innerFace);
                addIfNew(o, innerFace);
            }
        }
    }

    private static void addIfNew(Obj3D o, Obj3D.Face face)
    {
        for (Obj3D.FacePoint p : face.getPoints())
        {
            addIfNew(o.getV(), p.getV());
            addIfNew(o.getVN(), p.getVN());
        }
    }

    private static void addIfNew(List<Point3D> points, Point3D point)
    {
        if (points.indexOf(point) < 0)
            points.add(point);
    }

    private static Point3D projectV(Point3D v, Point3D center, double dist,
            Map<Point3D, Point3D> cacheV)
    {
        if (cacheV.containsKey(v))
            return cacheV.get(v);
        Point3D delta = Point3DLogic.sub(center, v);
        delta.setMag(dist);
        Point3D newV = Point3DLogic.add(v, delta);
        cacheV.put(v, newV);
        return newV;
    }

    private static Point3D flipVN(Point3D vn, Point3D center, double dist,
            Map<Point3D, Point3D> cacheVN)
    {
        if (cacheVN.containsKey(vn))
            return cacheVN.get(vn);
        Point3D newVN = Point3DLogic.mult(vn, -1);
        cacheVN.put(vn, newVN);
        return newVN;
    }

    protected static void findEdges(Obj3D o, Set<String> allEdges,
            Set<String> interiorEdges)
    {
        for (IDirective d : o.getD())
            if (d instanceof Obj3D.Face)
            {
                Obj3D.Face f = (Obj3D.Face)d;
                for (int i = 0; i < f.getPoints().size(); i++)
                {
                    String e = makeEdge(f.getPoints().get(i), f.getPoints().get((i+1)%f.getPoints().size()));
                    if (allEdges.contains(e))
                        interiorEdges.add(e);
                    else
                        allEdges.add(e);
                }
            }
    }
    
    private static String makeEdge(Obj3D.FacePoint p1, Obj3D.FacePoint p2)
    {
        int i1 = p1.getV().hashCode();
        int i2 = p2.getV().hashCode();
        if (i1 < i2)
            return i1+"$"+i2;
        else
            return i2+"$"+i1;
    }
}
