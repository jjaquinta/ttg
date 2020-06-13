package jo.util.geom3d.util;

import jo.util.geom3d.Mesh3D;
import jo.util.geom3d.Point3D;
import jo.util.geom3d.Triangle3D;
import jo.util.noise.Noise;

public class PerturbLogic
{
    
    public static Mesh3D perturbOutwards(Mesh3D inmesh, int seed, double radius, double density, double perturb)
    {
        Mesh3D outmesh = new Mesh3D();
        Noise n = new Noise(seed);
        for (Triangle3D intri : inmesh.getMesh())
        {
            Point3D p1 = perturbPoint(intri.getP1(), n, radius, density, perturb);
            Point3D p2 = perturbPoint(intri.getP2(), n, radius, density, perturb);
            Point3D p3 = perturbPoint(intri.getP3(), n, radius, density, perturb);
            Triangle3D outtri = new Triangle3D(p1, p2, p3, intri.getN());
            outmesh.append(outtri);
        }
        return outmesh;
    }    
    
    private static Point3D perturbPoint(Point3D p, Noise n, double radius, double density, double perturb)
    {
        Point3D d = new Point3D(p);
        d.scale(1.0/radius);
        d.scale(density);
        double r = n.noise(d.x, d.y, d.z);
        double m = 1 + r*perturb*2/100;
        m *= radius;
        d.x = p.x*m;
        d.y = p.y*m;
        d.z = p.z*m;
        return d;
    }

}
