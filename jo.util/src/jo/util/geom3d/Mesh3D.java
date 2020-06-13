package jo.util.geom3d;

import java.util.ArrayList;
import java.util.List;

public class Mesh3D
{
    private List<Triangle3D>    mMesh = new ArrayList<>();
    
    // constructors
    public Mesh3D()
    {        
    }
    
    public Mesh3D(List<Triangle3D> tris)
    {
        for (Triangle3D tri : tris)
            append(tri);
    }
    
    public Mesh3D(Mesh3D mesh)
    {
        this(mesh.getMesh());
    }
    
    // utilities
    
    public void append(Mesh3D m2)
    {
        append(m2.getMesh());
    }
    
    public void append(List<Triangle3D> tris)
    {
        for (Triangle3D tri : tris)
            append(tri);
    }

    public void append(Triangle3D tri)
    {
        mMesh.add(new Triangle3D(tri));
    }
    
    public void translate(Point3D delta)
    {
        for (Triangle3D t : mMesh)
            t.translate(delta);
    }
    
    public void scale(Point3D delta)
    {
        for (Triangle3D t : mMesh)
            t.scale(delta);
    }
    
    public void scale(double m)
    {
        for (Triangle3D t : mMesh)
            t.scale(m);
    }
    
    public void rotate(double rx, double ry, double rz)
    {
        for (Triangle3D t : mMesh)
            t.rotate(rx, ry, rz);
    }
    
    public void rotateAround(Point3D axis, double theta)
    {
        for (Triangle3D t : mMesh)
            t.rotateAround(axis, theta);
    }
    // getters and setters

    public List<Triangle3D> getMesh()
    {
        return mMesh;
    }

    public void setMesh(List<Triangle3D> mesh)
    {
        mMesh = mesh;
    }
}
