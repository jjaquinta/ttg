package jo.util.geom3d;

import java.util.List;

public class Point3DLogic
{
	public static double EPSILON = 0.0001; // r/w, so individual apps can adjust to scale
	
	public static Point3D incr(Point3D v1, Point3D v2)
	{
		v1.x += v2.x;
		v1.y += v2.y;
		v1.z += v2.z;
		return v1;
	}
	public static Point3D add(Point3D v1, Point3D v2)
	{
		Point3D v0 = new Point3D(v1);
		return incr(v0, v2);
	}
	public static Point3D decr(Point3D v1, Point3D v2)
	{
		v1.x -= v2.x;
		v1.y -= v2.y;
		v1.z -= v2.z;
		return v1;
	}
	public static Point3D sub(Point3D v1, Point3D v2)
	{
		Point3D v0 = new Point3D(v1);
		return decr(v0, v2);
	}
	public static void multBy(Point3D v, double mag)
	{
		v.x *= mag;
		v.y *= mag;
		v.z *= mag;
	}
	public static void divBy(Point3D v, double mag)
	{
		if (mag != 0)
			multBy(v, 1/mag);
	}
	public static Point3D mult(Point3D v, double mag)
	{
		Point3D v2 = new Point3D(v);
		multBy(v2, mag);
		return v2;
	}
	public static Point3D div(Point3D v, double mag)
	{
		Point3D v2 = new Point3D(v);
		divBy(v2, mag);
		return v2;
	}
	
	public static double dot(Point3D v1, Point3D v2)
	{
		return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
	}
	public static double mag(Point3D v)
	{
		return Math.sqrt(dot(v, v));
	}
	public static void makeNorm(Point3D v)
	{
		divBy(v, mag(v)); 
	}
	public static Point3D norm(Point3D v)
	{
		Point3D v2 = new Point3D(v);
		makeNorm(v2);
		return v2;
	}
	public static double dist(Point3D v1, Point3D v2)
	{
		return mag(sub(v1, v2));
	}
	public static boolean equals(Point3D v1, Point3D v2)
	{
		return Math.abs(v1.x - v2.x) + Math.abs(v1.y - v2.y) + Math.abs(v1.z - v2.z) < EPSILON;
	}
	public static boolean equals(double d1, double d2)
	{
		return isZero(d1 - d2);
	}
	public static boolean isZero(double d)
	{
		return Math.abs(d) < EPSILON;
	}
	public static int sgn(double d)
	{
		if (isZero(d))
			return 0;
		else if (d < 0)
			return -1;
		else
			return 1;
	}
	public static void makeLength(Point3D v, double l)
	{
		makeNorm(v);
		multBy(v, l);
	}
	public static boolean isCollinear(Point3D a, Point3D b, Point3D c)
	{
		double d = a.sub(b).cross(b.sub(c)).mag();
		return isZero(d);
	}
	
	private static void rot(double[] ords, int i1, int i2, double theta)
	{
	    if (theta == 0)
	        return;
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
        double x = cosTheta*ords[i1] - sinTheta*ords[i2];
        double y = sinTheta*ords[i1] + cosTheta*ords[i2];
        ords[i1] = x;
		ords[i2] = y;
	}
    public static void rotateBy(Point3D v, Point3D ang)
    {
        rotateBy(v, ang.x, ang.y, ang.z);
    }
    public static void rotateBy(Point3D v, double angx, double angy, double angz)
    {
    	double[] ords = new double[3];
    	ords[0] = v.x;
    	ords[1] = v.y;
    	ords[2] = v.z;
    	rot(ords, 1, 2, angx);
    	rot(ords, 2, 0, angy);
    	rot(ords, 0, 1, angz);
    	v.x = ords[0];
    	v.y = ords[1];
    	v.z = ords[2];
    }
    public static Point3D rotate(Point3D v, Point3D ang)
    {
        return rotate(v, ang.x, ang.y, ang.z);
    }
    public static Point3D rotate(Point3D v, double angx, double angy, double angz)
    {
        double[] ords = new double[3];
        ords[0] = v.x;
        ords[1] = v.y;
        ords[2] = v.z;
        rot(ords, 1, 2, angx);
        rot(ords, 2, 0, angy);
        rot(ords, 0, 1, angz);
        return new Point3D(ords[0], ords[1], ords[2]);
    }
    
    public static Point3D rotateNew(Point3D v, double angx, double angy, double angz)
    {
        return rotate(v, angx, angy, angz);
    }
    public static Point3D cross(Point3D v1, Point3D v2)
    {
    	Point3D v3 = new Point3D();
    	v3.x = v1.y*v2.z - v1.z*v2.y;
    	v3.y = v1.z*v2.x - v1.x*v2.z;
    	v3.z = v1.x*v2.y - v1.y*v2.x;
    	return v3;
    }
    
    /*
     * m = the point
     * l1, l2 = two points defining the line
     */
    public static double distPointToLine(Point3D m, Point3D l1, Point3D l2)
    {
    	Point3D v = l2.sub(l1);
    	v.normalize();
    	Point3D direct = m.sub(l1);
    	Point3D projected = v.mult(direct.dot(v));
    	double d = direct.sub(projected).mag();
    	return d;
    }
    
    /*
     * m = the point
     * l1, l2 = two points defining the line
     */
    public static double distPointToLineSegment(Point3D m, Point3D l1, Point3D l2)
    {
    	Point3D v = l2.sub(l1);
    	v.normalize();
    	Point3D direct = m.sub(l1);
    	double param = direct.dot(v);
    	if (param < 0)
    		return m.dist(l1);
    	if (param > l1.dist(l2))
    		return m.dist(l2);
    	Point3D projected = v.mult(param);
    	double d = direct.sub(projected).mag();
    	return d;
    }
    
    public static double signedAngleBetween(Point3D v1, Point3D v2, Point3D norm)
    {
    	double ang = angleBetween(v1, v2);
    	Point3D v3 = norm.cross(v1);
    	double sgn = v3.dot(v2);
    	if (sgn < -EPSILON)
    		ang = -ang;
    	return ang;
    }
    
    public static double angleBetween(Point3D p1, Point3D p2)
    {
    	double n = p1.dot(p2);
    	double d = p1.mag()*p2.mag();
    	double acos = n/d;
    	if (acos < -1)	// round off stuff
    		acos = -1;
    	else if (acos > 1)
    		acos = 1;
    	double a = Math.acos(acos);
    	if (Double.isNaN(a))
    		System.out.println("!!! Can't compute angle between "+p1+" and "+p2+", n="+n+", d="+d+", acos="+acos+", a="+a);
    	return a;
    }
    
    public static double angleBetween(Point3D o, Point3D p1, Point3D p2)
    {
    	return angleBetween(p1.sub(o), p2.sub(o));
    }
    
    // return euler rotational vector that will rotate (1,0,0) to the direction vector given 
    public static Point3D dirToRot(Point3D v)
    {
        if (equals(v.x, 0) && equals(v.y, 0))
        {
            if (equals(v.z, 0))
                return new Point3D();
            else if (v.z < 0)
                return new Point3D(0, Math.PI/2, 0);
            else
                return new Point3D(0, -Math.PI/2, 0);
        }
        double xy = Math.sqrt(v.x*v.x + v.y*v.y);
        double ay = -Math.atan2(v.z, xy);
        double az = Math.atan2(v.y, v.x);
        return new Point3D(0, ay, az);
    }
    public static Point3D between(Point3D p1, Point3D p2, double pc)
    {
        Point3D p = new Point3D();
        p.x = between(p1.x, p2.x, pc);
        p.y = between(p1.y, p2.y, pc);
        p.z = between(p1.z, p2.z, pc);
        return p;
    }
    public static double between(double p1, double p2, double pc)
    {
        return (p2*pc + p1*(1-pc));
    }
    public static double manhattanDist(Point3D p1, Point3D p2)
    {
        double dist = 0;
        dist += Math.abs(p1.x - p2.x);
        dist += Math.abs(p1.y - p2.y);
        dist += Math.abs(p1.z - p2.z);
        return dist;
    }
    public static Point3D rotateAround(Point3D vec, Point3D axis, double theta)
    {
        double x = vec.x;
        double y = vec.y;
        double z = vec.z;
        Point3D naxis = axis.normal();
        double u = naxis.x;
        double v = naxis.y;
        double w = naxis.z;
        double xPrime = u*(u*x + v*y + w*z)*(1d - Math.cos(theta)) 
                + x*Math.cos(theta)
                + (-w*y + v*z)*Math.sin(theta);
        double yPrime = v*(u*x + v*y + w*z)*(1d - Math.cos(theta))
                + y*Math.cos(theta)
                + (w*x - u*z)*Math.sin(theta);
        double zPrime = w*(u*x + v*y + w*z)*(1d - Math.cos(theta))
                + z*Math.cos(theta)
                + (-v*x + u*y)*Math.sin(theta);
        Point3D ret = new Point3D(xPrime, yPrime, zPrime);
        return ret;
    }
    public static Point3D average(Point3D... ps)
    {
        Point3D a = new Point3D();
        for (Point3D p : ps)
        {
            a.x += p.x;
            a.y += p.y;
            a.z += p.z;
        }
        a.scale(1.0/ps.length);
        return a;
    }
    public static Point3D rightAngle(Point3D a)
    {
        /*
        Point3D tmp1 = new Point3D(0,0,1);
        Point3D tmp2 = new Point3D(0,0,-1);
        if (Point3DLogic.equals(a.normal(), tmp1) || Point3DLogic.equals(a.normal(), tmp2))
            tmp1 = new Point3D(0,1,0);
        Point3D spoke = Point3DLogic.cross(a, tmp1);
        return spoke;
        */
        Point3D tmp = new Point3D();
        if (Math.abs(a.x) < Math.abs(a.y))
            if (Math.abs(a.x) < Math.abs(a.z))
                tmp.x = 1;
            else
                tmp.z = 1;
        else
            if (Math.abs(a.y) < Math.abs(a.z))
                tmp.y = 1;
            else
                tmp.z = 1;
        Point3D spoke = Point3DLogic.cross(a, tmp);
        return spoke;
    }
    
    // https://math.stackexchange.com/questions/180418/calculate-rotation-matrix-to-align-vector-a-to-vector-b-in-3d
    public static Matrix3D rotBetween1(Point3D a, Point3D b)
    {
        Matrix3D i = new Matrix3D();
        if (a.equals(b))
            return i;
        if (a.equals(b.mult(-1)))
        {
            i.scale(-1);
            return i;
        }
        Point3D v = a.cross(b);
        double s = v.mag();
        double c = a.dot(b);
        Matrix3D vx = new Matrix3D(
                0, -v.z, v.y,
                v.z, 0, -v.x,
                -v.y, v.x, 0
                );
        Matrix3D v2 = new Matrix3D(vx);
        v2.mult(vx);
        v2.scale((1 - c)/(s*s));
        Matrix3D r = new Matrix3D(i);
        r.add(vx);
        r.add(v2);
        return r;
    }
    public static Matrix3D rotBetween(Point3D a, Point3D b)
    {
        if (a.equals(b))
            return new Matrix3D();
        double sin = a.cross(b).mag();
        double cos = a.dot(b);
        Matrix3D G = new Matrix3D(
                cos, -sin, 0,
                sin, cos, 0,
                0, 0, 1
                );
        Point3D u = a;
        Point3D v = b.sub(a.mult(cos)).normal();
        Point3D w = b.cross(a);
        Matrix3D Fm = new Matrix3D(
                u.x, u.y, u.z,
                v.x, v.y, v.z,
                w.x, w.y, w.z
                );
        Matrix3D F = Fm.inverse();
        Matrix3D ret = new Matrix3D(Fm);
        ret.mult(G);
        ret.mult(F);
        return ret;
    }
    
    public static void addIfUnique(List<Point3D> points, Point3D point)
    {
        if (contains(points, point))
            return;
        points.add(point);
    }
    
    public static boolean contains(List<Point3D> points, Point3D point)
    {
        return find(points, point) != null;
    }
    
    public static Point3D find(List<Point3D> points, Point3D point)
    {
        for (Point3D p : points)
            if (p.equals(point))
                return p;
        return null;
    }
}
