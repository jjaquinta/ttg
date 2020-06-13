package jo.util.geom3d.util;

import jo.util.geom3d.Mesh3D;
import jo.util.geom3d.Mesh3DLogic;
import jo.util.geom3d.Triangle3D;
import jo.util.geom3d.Triangle3DLogic;

public class SetOpsLogic
{
    public static Mesh3D union(Mesh3D m1, Mesh3D m2)
    {
        Mesh3D u = new Mesh3D();
        for (Triangle3D t1 : m1.getMesh())
            if (!Mesh3DLogic.contains(m2, t1))
                u.append(t1);
        for (Triangle3D t2 : m2.getMesh())
            if (!Mesh3DLogic.contains(m1, t2))
                u.append(t2);
        return u;
    }

    public static Mesh3D intersection(Mesh3D m1, Mesh3D m2)
    {
        Mesh3D i = new Mesh3D();
        for (Triangle3D t1 : m1.getMesh())
            if (Mesh3DLogic.contains(m2, t1))
                i.append(Triangle3DLogic.invert(t1));
        for (Triangle3D t2 : m2.getMesh())
            if (Mesh3DLogic.contains(m1, t2))
                i.append(Triangle3DLogic.invert(t2));
        return i;
    }

    public static Mesh3D subtract(Mesh3D m1, Mesh3D m2)
    {
        Mesh3D i = new Mesh3D();
        for (Triangle3D t1 : m1.getMesh())
            if (!Mesh3DLogic.contains(m2, t1))
                i.append(t1);
        for (Triangle3D t2 : m2.getMesh())
            if (Mesh3DLogic.contains(m1, t2))
                i.append(Triangle3DLogic.invert(t2));
        return i;
    }
}
