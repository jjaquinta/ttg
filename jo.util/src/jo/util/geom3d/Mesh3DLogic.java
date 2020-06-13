package jo.util.geom3d;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jo.util.geom2d.Polygon2DLogic;
import jo.util.geom3d.util.CubeLogic;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.FloatUtils;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;
import jo.util.utils.xml.XMLUtils;

public class Mesh3DLogic
{
    public static void writeSTL(File file, Mesh3D mesh) throws IOException
    {
        System.out.print(file.getName());
        int todo = mesh.getMesh().size();
        int done = 0;
        int lastpc = 100;
        byte[] header = new byte[80];
        for (int i = 0; i < header.length; i++)
            header[i] = ' ';
        // assume binary
        DataOutputStream os = new DataOutputStream(new FileOutputStream(file));
        os.write(header);
        os.writeInt(Integer.reverseBytes(mesh.getMesh().size()));
        for (Triangle3D tri : mesh.getMesh())
        {
            if (Thread.interrupted())
                break;
            writeFloat(os, (float)tri.getN().x);
            writeFloat(os, (float)tri.getN().y);
            writeFloat(os, (float)tri.getN().z);
            writeFloat(os, (float)tri.getP1().x);
            writeFloat(os, (float)tri.getP1().y);
            writeFloat(os, (float)tri.getP1().z);
            writeFloat(os, (float)tri.getP2().x);
            writeFloat(os, (float)tri.getP2().y);
            writeFloat(os, (float)tri.getP2().z);
            writeFloat(os, (float)tri.getP3().x);
            writeFloat(os, (float)tri.getP3().y);
            writeFloat(os, (float)tri.getP3().z);
            os.writeShort(0);
            done++;
            int pc = done*100/todo;
            if (pc != lastpc)
            {
                System.out.print(".");
                if ((pc%10 == 0))
                    System.out.print(pc);
                if (pc == 50)
                    System.out.println();
                lastpc = pc;
            }
        }
        os.close();
        System.out.println();
    }
    
    private static void writeFloat(DataOutputStream os, float f) throws IOException
    {
        os.writeInt(Integer.reverseBytes(Float.floatToIntBits(f)));
    }
    public static Mesh3D readSTL(File file) throws IOException
    {
        // assume binary
        DataInputStream is = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        byte[] header = new byte[80];
        is.mark(128);
        is.read(header);
        String h = new String(header);
        if (h.indexOf("solid") >= 0)
        {
            is.reset();
            return readSTLAscii(is);
        }
        else
            return readSTLBinary(is);
    }
    
    private static Mesh3D readSTLAscii(DataInputStream is) throws IOException
    {
        BufferedReader rdr = new BufferedReader(new InputStreamReader(is, "utf-8"));
        Mesh3D stl = new Mesh3D();
        for (;;)
        {
            String inbuf = rdr.readLine();
            if (inbuf == null)
                break;
            inbuf = inbuf.trim();
            if (inbuf.startsWith("solid"))
                continue;
            if (inbuf.startsWith("facet"))
            {
                Point3D n = readPoint(inbuf, 2);
                rdr.readLine(); // outer loop
                Point3D t1 = readPoint(rdr.readLine(), 1); // vertex
                Point3D t2 = readPoint(rdr.readLine(), 1); // vertex
                Point3D t3 = readPoint(rdr.readLine(), 1); // vertex
                rdr.readLine(); // end loop
                rdr.readLine(); // end facet
                stl.getMesh().add(new Triangle3D(t1, t2, t3, n));
            }
        }
        return stl;
    }
    
    private static Point3D readPoint(String inbuf, int skip)
    {
        StringTokenizer st = new StringTokenizer(inbuf, " \t");
        for (int i = 0; i < skip; i++)
            st.nextToken();
        Point3D p = new Point3D();
        p.x = DoubleUtils.parseDouble(st.nextToken());
        p.y = DoubleUtils.parseDouble(st.nextToken());
        p.z = DoubleUtils.parseDouble(st.nextToken());
        return p;
    }
    
    private static Mesh3D readSTLBinary(DataInputStream is) throws IOException
    {
        Mesh3D stl = new Mesh3D();
        long numTri = Integer.reverseBytes(is.readInt());
        System.out.println(numTri+" triangles");
        for (int i = 0; i < numTri; i++)
        {
            Triangle3D tri = new Triangle3D();
            tri.getN().x = readFloat(is);
            tri.getN().y = readFloat(is);
            tri.getN().z = readFloat(is);
            tri.getP1().x = readFloat(is);
            tri.getP1().y = readFloat(is);
            tri.getP1().z = readFloat(is);
            tri.getP2().x = readFloat(is);
            tri.getP2().y = readFloat(is);
            tri.getP2().z = readFloat(is);
            tri.getP3().x = readFloat(is);
            tri.getP3().y = readFloat(is);
            tri.getP3().z = readFloat(is);
            is.readUnsignedShort(); // attribute
            stl.getMesh().add(tri);
        }
        is.close();
        return stl;
    }
    
    private static float readFloat(DataInputStream is) throws IOException
    {
        return Float.intBitsToFloat(Integer.reverseBytes(is.readInt()));
    }
    
    public static Mesh3D increseResolution(Mesh3D in, double gap)
    {
        Mesh3D out = new Mesh3D();
        Point3D dummy = new Point3D();
        for (Triangle3D tin : in.getMesh())
        {
            int mark = out.getMesh().size();
            CubeLogic.subdivide(out, tin.getP1(), tin.getP2(), tin.getP3(), dummy, gap);
            for (int i = mark; i < out.getMesh().size(); i++)
                out.getMesh().get(i).setN(tin.getN());
        }
        return out;
    }

    // slow method
    public static boolean contains(Mesh3D m, Point3D p)
    {
        int count = 0;
        for (Triangle3D t : m.getMesh())
            if ((t.getP1().z > p.z) && (t.getP2().z > p.z) && (t.getP3().z > p.z))
                if (Polygon2DLogic.contains(p.x, p.y, t.getP1(), t.getP2(), t.getP3()))
                    count++;
        return (count%2) == 1;
    }
    
    public static boolean contains(Mesh3D m, Triangle3D t)
    {
        return contains(m, t.getP1()) || contains(m, t.getP2()) || contains(m, t.getP3());
    }

    private static Map<String, Point4D> mGeometryMaterials = new HashMap<String, Point4D>();
    private static Map<String, Point4D> mLibraryMaterials = new HashMap<String, Point4D>();
    private static Map<String, Point4D> mLibraryEffects = new HashMap<String, Point4D>();
    private static Map<String, Mesh3D> mGeometries = new HashMap<String, Mesh3D>();

    public static Mesh3D readDAE(File f) throws IOException
    {
        InputStream is = new FileInputStream(f);
        Mesh3D mesh = readDAE(is);
        is.close();
        return mesh;
    }
    
    public static Mesh3D readDAE(InputStream is)
    {
        Mesh3D dae = new Mesh3D();
        Document doc = XMLUtils.readStream(is);
        System.out.println("Indexing");
        indexLibraryEffects(doc);
        indexLibraryMaterials(doc);
        indexGeometryMaterials(doc);
        indexGeometries(doc);
        System.out.println("Indexing scenes");
        for (Node g : XMLUtils.findNodes(doc, "COLLADA/library_visual_scenes/visual_scene"))
        {
            System.out.println("Scene "+XMLUtils.getAttribute(g, "id"));
            scanScene(g, dae);
        }
        System.out.println("Created "+dae.getMesh().size()+" objects");
        return dae;
    }

    private static void scanScene(Node n, Mesh3D dae)
    {
        // TODO: parse matrix?
        for (Node ig : XMLUtils.findNodes(n, "instance_geometry"))
        {
            String id = XMLUtils.getAttribute(ig, "url");
            if (id.startsWith("#"))
                id = id.substring(1);
            if (mGeometries.containsKey(id))
                dae.append(mGeometries.get(id));
            else
                System.out.println("  Unknown geometry "+id);
        }
        for (Node nn : XMLUtils.findNodes(n, "node"))
            scanScene(nn, dae);
    }
    
    private static void indexGeometries(Document doc)
    {
        System.out.println("Indexing geometries");
        for (Node g : XMLUtils.findNodes(doc, "COLLADA/library_geometries/geometry"))
        {
            String geoid = XMLUtils.getAttribute(g, "id");
            System.out.println("Geometry "+geoid);
            for (Node t : XMLUtils.findNodes(g, "mesh/triangles"))
            {
                //System.out.println("Triangles");
                int triCount = IntegerUtils.parseInt(XMLUtils.getAttribute(t, "count"));
                Point4D color = getColor(t);
                if (color == null)
                {
                    System.out.println("no color");
                    color = new Point4D(.9f, .9f, .9f, .9f);
                }
                Map<Integer,String> offsetToSemantic = new HashMap<Integer, String>();
                Map<String,List<Short>> semanticToTriangles = new HashMap<String, List<Short>>();
                Map<String,List<Point3D>> semanticToFloats = new HashMap<String, List<Point3D>>();
                for (Node input : XMLUtils.findNodes(t, "input"))
                {
                    String semantic = XMLUtils.getAttribute(input, "semantic");
                    int offset = IntegerUtils.parseInt(XMLUtils.getAttribute(input, "offset"));
                    String source = XMLUtils.getAttribute(input, "source").substring(1);
                    Node src = findNodeWithValue(t.getParentNode(), "vertices", "id", source);
                    if (src != null)
                    {
                        Node i = XMLUtils.findFirstNode(src, "input");
                        source = XMLUtils.getAttribute(i, "source").substring(1);
                    }
                    List<Point3D> positions = findFloatArray(t.getParentNode(), source);
                    if (positions == null)
                    {
                        System.out.println("  no position source, sematic="+semantic+", offset="+offset+", source="+source);
                        continue;
                    }
                    offsetToSemantic.put(offset, semantic);
                    semanticToTriangles.put(semantic, new ArrayList<Short>());
                    semanticToFloats.put(semantic, positions);
                }
                Node p = XMLUtils.findFirstNode(t, "p");
                if (p == null)
                {
                    System.out.println("  no p node");
                    continue;
                }
                //List<Short> triangles = new ArrayList<Short>();
                String triTxt = XMLUtils.getText(p);
                StringTokenizer st = new StringTokenizer(triTxt, " \r\n\t");
                int count = st.countTokens();                        
                if (count != triCount*3*offsetToSemantic.size()) 
                {
                    System.err.println("Odd count of triangles! Expected "+triCount+" -> "+triCount*3*offsetToSemantic.size()+", got "+count);
                    System.err.println(triTxt);
                    System.err.println("--------------------------------------------------");
                    continue;
                }
                for (int i = 0; i < triCount*3; i++)
                {
                    for (int offset = 0; offset < offsetToSemantic.size(); offset++)
                    {
                        String sem = offsetToSemantic.get(offset);
                        semanticToTriangles.get(sem).add(Short.parseShort(st.nextToken()));
                    }
                }
                List<Point3D> oVerts = new ArrayList<Point3D>();
                List<Point3D> oNorms = new ArrayList<Point3D>();
                List<Short> oTris = new ArrayList<Short>();
                Map<String,Integer> combos = new HashMap<String,Integer>();
                for (int i = 0; i < triCount*3; i++)
                {
                    int vv = semanticToTriangles.get("VERTEX").get(i);
                    int nn = vv;
                    if (semanticToTriangles.containsKey("NORMAL"))
                        nn = semanticToTriangles.get("NORMAL").get(i);
                    String id = vv+"-"+nn;
                    //System.out.println("  "+id);
                    if (combos.containsKey(id))
                        oTris.add(combos.get(id).shortValue());
                    else
                    {
                        if (vv >= semanticToFloats.get("VERTEX").size())
                        {
                            System.out.println("Bad position index "+vv+" (out of "+semanticToFloats.get("VERTEX").size()+")");
                            continue;
                        }
                        Point3D vvp = semanticToFloats.get("VERTEX").get(vv);
                        oVerts.add(vvp);
                        if (semanticToTriangles.containsKey("NORMAL"))
                        {
                            if (nn >= semanticToFloats.get("NORMAL").size())
                            {
                                System.out.println("Bad normal index "+nn+" (out of "+semanticToFloats.get("NORMALS").size()+")");
                                continue;
                            }
                            Point3D nnp = semanticToFloats.get("NORMAL").get(nn);
                            oNorms.add(nnp);
                        }
                        int idx = oVerts.size();
                        combos.put(id, idx);
                        oTris.add((short)idx);
                    }
                }
                /*
                o.setVertices(oVerts);                
                if (semanticToTriangles.containsKey("NORMAL"))
                    o.setNormals(oNorms);                
                o.setIndices(oTris);
                //System.out.println("Converted to verts="+oVerts.size()+", norms="+oNorms.size()+", tris="+oTris.size());
                if (color != null)
                {
                    List<Point4D> colors = new ArrayList<Point4D>();
                    for (int i = 0; i < oVerts.size(); i++)
                        colors.add(color);
                    o.setColors(colors);
                }
                System.out.println("  added");
                dae.add(o);
                */
                Mesh3D geo = new Mesh3D();
                for (int i = 0; i < oTris.size(); i += 3)
                {
                    short v0 = oTris.get(i+0);
                    short v1 = oTris.get(i+1);
                    short v2 = oTris.get(i+2);
                    Point3D p0 = oVerts.get(v0-1);
                    Point3D p1 = oVerts.get(v1-1);
                    Point3D p2 = oVerts.get(v2-1);
                    Triangle3D tri = new Triangle3D(p0, p1, p2);
                    geo.append(tri);
                }
                mGeometries.put(geoid, geo);
            }
        }
    }
    
    private static Point4D getColor(Node t)
    {
        String geomID = XMLUtils.getAttribute(t.getParentNode().getParentNode(), "id");
        if (StringUtils.isTrivial(geomID))
            return null;
        String material = XMLUtils.getAttribute(t, "material");
        if (StringUtils.isTrivial(material))
            return null;
        return mGeometryMaterials.get(geomID+"$"+material);
    }
    
    private static List<Point3D> findFloatArray(Node parent, String id)
    {
        Node src = findNodeWithValue(parent, "source", "id", id);
        if (src == null)
            return null;
        Node fsrc = XMLUtils.findFirstNode(src, "float_array");
        if (fsrc == null)
            return null;
        Node asrc = XMLUtils.findFirstNode(src, "technique_common/accessor");
        if (asrc == null)
            return null;
        int stride = IntegerUtils.parseInt(XMLUtils.getAttribute(asrc, "stride"));
        List<Point3D> floats = new ArrayList<Point3D>();
        StringTokenizer st = new StringTokenizer(XMLUtils.getText(fsrc), " \r\n\t");
        while (st.countTokens() > 0)
        {
            Point3D p = new Point3D();
            p.x = FloatUtils.parseFloat(st.nextToken());
            p.y = FloatUtils.parseFloat(st.nextToken());
            if (stride == 3)
                p.z = FloatUtils.parseFloat(st.nextToken());
            floats.add(p);
        }
        if (st.countTokens() != 0)
        {
            System.err.println("Odd numer of tokens for "+XMLUtils.getText(fsrc));
        }
        return floats;
    }

    /*
    private static String findInputSource(Node parent, String semantic)
    {
        Node i = findNodeWithValue(parent, "input", "semantic", semantic);
        if (i != null)
        {
            String source = XMLUtils.getAttribute(i, "source");
            if (source.startsWith("#"))
                source = source.substring(1);
            return source;
        }
        return null;
    }
    */

    private static Node findNodeWithValue(Node parent, String nodeName, String attrName, String attrValue)
    {
        for (Node i : XMLUtils.findAllNodesRecursive(parent, nodeName))
        {
            String s = XMLUtils.getAttribute(i, attrName);
            if (attrValue.equals(s))
                return i;
        }
        return null;
    }
    
    private static void indexGeometryMaterials(Document doc)
    {
        mGeometryMaterials.clear();
        for (Node ig : XMLUtils.findNodes(doc, "COLLADA/library_visual_scenes/visual_scene/node/instance_geometry"))
        {
            String geomID = XMLUtils.getAttribute(ig, "url");
            if (StringUtils.isTrivial(geomID))
                continue;
            if (geomID.startsWith("#"))
                geomID = geomID.substring(1);
            for (Node im : XMLUtils.findAllNodesRecursive(ig, "instance_material"))
            {
                String symbol = XMLUtils.getAttribute(im, "symbol");
                String target = XMLUtils.getAttribute(im, "target");
                if (target.startsWith("#"))
                    target = target.substring(1);
                if (!mLibraryMaterials.containsKey(target))
                    continue;
                Point4D effect = mLibraryMaterials.get(target);
                if (effect == null)
                    continue;
                mGeometryMaterials.put(geomID+"$"+symbol, effect);
            }            
        }
    }
    
    private static void indexLibraryMaterials(Document doc)
    {
        mLibraryMaterials.clear();
        for (Node e : XMLUtils.findNodes(doc, "COLLADA/library_materials/material"))
        {
            String id = XMLUtils.getAttribute(e, "id");
            if (StringUtils.isTrivial(id))
                continue;
            Node ie = XMLUtils.findFirstNode(e, "instance_effect");
            if (ie == null)
                continue;
            String effectID = XMLUtils.getAttribute(ie, "url");
            if (StringUtils.isTrivial(effectID))
                continue;
            if (effectID.startsWith("#"))
                effectID = effectID.substring(1);
            Point4D effect = mLibraryEffects.get(effectID);
            if (effect == null)
                continue;
            mLibraryMaterials.put(id, effect);
        }
    }
    
    private static void indexLibraryEffects(Document doc)
    {
        mLibraryEffects.clear();
        for (Node e : XMLUtils.findNodes(doc, "COLLADA/library_effects/effect"))
        {
            String id = XMLUtils.getAttribute(e, "id");
            if (StringUtils.isTrivial(id))
                continue;
            Node d = XMLUtils.findFirstNodeRecursive(e, "diffuse");
            if (d == null)
                continue;
            Node c = XMLUtils.findFirstNodeRecursive(d, "color");
            if (c == null)
                continue;
            StringTokenizer st = new StringTokenizer(XMLUtils.getText(c).trim(), " \t\r\n");
            if (st.countTokens() != 4)
                continue;
            Point4D color = new Point4D();
            color.x = FloatUtils.parseFloat(st.nextToken());
            color.y = FloatUtils.parseFloat(st.nextToken());
            color.z = FloatUtils.parseFloat(st.nextToken());
            color.w = FloatUtils.parseFloat(st.nextToken());
            mLibraryEffects.put(id, color);
        }
    }
    
    public static void normalize(Mesh3D mesh)
    {
        List<Point3D> unique = new ArrayList<>();
        for (Triangle3D t : mesh.getMesh())
        {
            t.setN(Triangle3DLogic.makeNormal(t.getP1(), t.getP2(), t.getP3()));
            Point3D p = Point3DLogic.find(unique, t.getP1());
            if (p == null)
                unique.add(t.getP1());
            else
                t.setP1(p);
            p = Point3DLogic.find(unique, t.getP2());
            if (p == null)
                unique.add(t.getP2());
            else
                t.setP2(p);
            p = Point3DLogic.find(unique, t.getP3());
            if (p == null)
                unique.add(t.getP3());
            else
                t.setP3(p);
        }
    }
}

