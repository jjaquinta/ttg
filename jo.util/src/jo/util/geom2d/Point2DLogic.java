package jo.util.geom2d;

import java.util.List;

public class Point2DLogic
{
	public static double EPSILON = 0.0001; // r/w, so individual apps can adjust to scale
	
	public static Point2D incr(Point2D v1, Point2D v2)
	{
		v1.x += v2.x;
		v1.y += v2.y;
		return v1;
	}
	public static Point2D add(Point2D v1, Point2D v2)
	{
		Point2D v0 = new Point2D(v1);
		return incr(v0, v2);
	}
	public static Point2D decr(Point2D v1, Point2D v2)
	{
		v1.x -= v2.x;
		v1.y -= v2.y;
		return v1;
	}
	public static Point2D sub(Point2D v1, Point2D v2)
	{
		Point2D v0 = new Point2D(v1);
		return decr(v0, v2);
	}
	public static void multBy(Point2D v, double mag)
	{
		v.x *= mag;
		v.y *= mag;
	}
	public static void divBy(Point2D v, double mag)
	{
		if (mag != 0)
			multBy(v, 1/mag);
	}
	public static Point2D mult(Point2D v, double mag)
	{
		Point2D v2 = new Point2D(v);
		multBy(v2, mag);
		return v2;
	}
	public static Point2D div(Point2D v, double mag)
	{
		Point2D v2 = new Point2D(v);
		divBy(v2, mag);
		return v2;
	}
	
	public static Point2D average(Point2D... p)
	{
	    Point2D a = new Point2D();
	    for (int i = 0; i < p.length; i++)
	        a.incr(p[i]);
	    a.scale(1.0/p.length);
	    return a;
	}
	
	public static double dot(Point2D v1, Point2D v2)
	{
		return v1.x*v2.x + v1.y*v2.y;
	}
	public static double mag(Point2D v)
	{
		return Math.sqrt(dot(v, v));
	}
	public static void makeNorm(Point2D v)
	{
		divBy(v, mag(v)); 
	}
	public static Point2D norm(Point2D v)
	{
		Point2D v2 = new Point2D(v);
		makeNorm(v2);
		return v2;
	}
	public static double dist(Point2D v1, Point2D v2)
	{
		return mag(sub(v1, v2));
	}
    public static double dist(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
    }
	public static boolean equals(Point2D v1, Point2D v2)
	{
		return Math.abs(v1.x - v2.x) + Math.abs(v1.y - v2.y) < EPSILON;
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
	public static void makeLength(Point2D v, double l)
	{
		makeNorm(v);
		multBy(v, l);
	}
	
	private static void rot(double[] ords, int i1, int i2, double theta)
	{
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
        double x = cosTheta*ords[i1] - sinTheta*ords[i2];
        double y = sinTheta*ords[i1] + cosTheta*ords[i2];
        ords[i1] = x;
		ords[i2] = y;
	}
    public static void rotate(Point2D v, Point2D ang)
    {
    	double[] ords = new double[3];
    	ords[0] = v.x;
    	ords[1] = v.y;
    	rot(ords, 1, 2, ang.x);
    	rot(ords, 2, 0, ang.y);
    	v.x = ords[0];
    	v.y = ords[1];
    }
    
    /*
     * m = the point
     * l1, l2 = two points defining the line
     */
    public static double distPointToLine(Point2D m, Point2D l1, Point2D l2)
    {
    	Point2D v = l2.sub(l1);
    	v.normalize();
    	Point2D direct = m.sub(l1);
    	Point2D projected = v.mult(direct.dot(v));
    	double d = direct.sub(projected).mag();
    	return d;
    }
    
    /*
     * m = the point
     * l1, l2 = two points defining the line
     */
    public static double distPointToLineSegment(Point2D m, Point2D l1, Point2D l2)
    {
    	Point2D v = l2.sub(l1);
    	v.normalize();
    	Point2D direct = m.sub(l1);
    	double param = direct.dot(v);
    	if (param < 0)
    		return m.dist(l1);
    	if (param > l1.dist(l2))
    		return m.dist(l2);
    	Point2D projected = v.mult(param);
    	double d = direct.sub(projected).mag();
    	return d;
    }

    public static Point2D between(Point2D p1, Point2D p2, double pc)
    {
        Point2D p = new Point2D();
        p.x = (p2.x*pc + p1.x*(1-pc));
        p.y = (p2.y*pc + p1.y*(1-pc));
        return p;
    }
    public static boolean isColinear(Point2D p0, Point2D p1, Point2D p2)
    {   // https://www.sanfoundry.com/java-program-check-if-given-set-three-points-lie-single-line-or-not/
        double s = (p2.y - p1.y) * p0.x + (p1.x - p2.x) * p0.y + (p2.x * p1.y - p1.x * p2.y);
        return Math.abs(s) < EPSILON;
    }
    public static Rectangle2D bounds(List<Point2D> list)
    {
        Rectangle2D r = new Rectangle2D(list.get(0), list.get(0));
        for (int i = 1; i < list.size(); i++)
        {
            Point2D p = list.get(i);
            if (p.x < r.p1.x)
                r.p1.x = p.x;
            if (p.x > r.p2.x)
                r.p2.x = p.x;
            if (p.y < r.p1.y)
                r.p1.y = p.y;
            if (p.y > r.p2.y)
                r.p2.y = p.y;
        }
        return r;
    }
}
