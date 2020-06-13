package jo.util.geom3d.util;

import jo.util.geom3d.Point3D;

public class TetrahedronLogic
{
    /*
     * https://keisan.casio.com/exec/system/1329962711
     */
    public static double volume(Point3D p1, Point3D p2, Point3D p3, Point3D p4)
    {
        double a1 = p1.dist(p2);
        double a2 = p1.dist(p3);
        double a3 = p1.dist(p4);
        double a4 = p2.dist(p3);
        double a5 = p2.dist(p4);
        double a6 = p3.dist(p4);
        return volume(a1, a2, a3, a4, a5, a6);
    }
    
    public static double volume(double a1, double a2, double a3, double a4, double a5, double a6) 
    {
        double a12 = a1*a1;
        double a22 = a2*a2;
        double a32 = a3*a3;
        double a42 = a4*a4;
        double a52 = a5*a5;
        double a62 = a6*a6;
        double t1 = a12*a52*(a22 + a32 + a42 + a62 - a12 - a52);
        double t2 = a22*a62*(a12 + a32 + a42 + a52 - a22 - a62);
        double t3 = a32*a42*(a12 + a22 + a52 + a62 - a32 - a42);
        double t4 = a12*a22*a42 + a22*a32*a52 + a12*a32*a62 + a42*a52*a62;
        double v2 = (t1 + t2 + t3 - t4)/144;
        double v = Math.sqrt(v2);
        return v;
    }
}
