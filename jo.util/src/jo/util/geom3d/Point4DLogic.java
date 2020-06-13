package jo.util.geom3d;

public class Point4DLogic
{
	public static double EPSILON = 0.0001; // r/w, so individual apps can adjust to scale
	
	public static Point4D incr(Point4D v1, Point4D v2)
	{
        v1.w += v2.w;
		v1.x += v2.x;
		v1.y += v2.y;
		v1.z += v2.z;
		return v1;
	}
	public static Point4D add(Point4D v1, Point4D v2)
	{
		Point4D v0 = new Point4D(v1);
		return incr(v0, v2);
	}
	public static Point4D decr(Point4D v1, Point4D v2)
	{
        v1.w -= v2.w;
		v1.x -= v2.x;
		v1.y -= v2.y;
		v1.z -= v2.z;
		return v1;
	}
	public static Point4D sub(Point4D v1, Point4D v2)
	{
		Point4D v0 = new Point4D(v1);
		return decr(v0, v2);
	}
	public static void multBy(Point4D v, double mag)
	{
        v.w *= mag;
		v.x *= mag;
		v.y *= mag;
		v.z *= mag;
	}
	public static void divBy(Point4D v, double mag)
	{
		if (mag != 0)
			multBy(v, 1/mag);
	}
	public static Point4D mult(Point4D v, double mag)
	{
		Point4D v2 = new Point4D(v);
		multBy(v2, mag);
		return v2;
	}
	public static Point4D div(Point4D v, double mag)
	{
		Point4D v2 = new Point4D(v);
		divBy(v2, mag);
		return v2;
	}
	
	public static double dot(Point4D v1, Point4D v2)
	{
		return v1.w*v2.w + v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
	}
	public static double mag(Point4D v)
	{
		return Math.sqrt(dot(v, v));
	}
	public static void makeNorm(Point4D v)
	{
		divBy(v, mag(v)); 
	}
	public static Point4D norm(Point4D v)
	{
		Point4D v2 = new Point4D(v);
		makeNorm(v2);
		return v2;
	}
	public static double dist(Point4D v1, Point4D v2)
	{
		return mag(sub(v1, v2));
	}
	public static boolean equals(Point4D v1, Point4D v2)
	{
		return Math.abs(v1.w - v2.w) + Math.abs(v1.x - v2.x) + Math.abs(v1.y - v2.y) + Math.abs(v1.z - v2.z) < EPSILON;
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
	public static void makeLength(Point4D v, double l)
	{
		makeNorm(v);
		multBy(v, l);
	}
	
	// http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
	public static Point3D quatToEuler(Point4D q)
	{
	    double heading;
        double attitude;
        double bank;
        double d = q.x*q.y + q.z*q.w;
        if (Point3DLogic.equals(d, 0.5))
        {
            heading = 2*Math.atan2(q.x,q.w);
            attitude = 0;
            bank = 0;
        }
        else if (Point3DLogic.equals(d, -0.5))
        {
            heading = -2*Math.atan2(q.x,q.w);
            attitude = 0;
            bank = 0;
        }
        else
        {
    	    heading = Math.atan2(2*q.y*q.w-2*q.x*q.z , 1 - 2*q.y*q.y - 2*q.z*q.z);
    	    attitude = Math.asin(2*q.x*q.y + 2*q.z*q.w);
    	    bank = Math.atan2(2*q.x*q.w-2*q.y*q.z , 1 - 2*q.x*q.x - 2*q.z*q.z);
        }
        return new Point3D(attitude, bank, heading);
	}
    
	// http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm
    public static Point4D eulerToQuat(Point3D euler)
    {
        double heading = euler.z;
        double attitude = euler.x;
        double bank = euler.y;
        double c1 = Math.cos(heading / 2);
        double c2 = Math.cos(attitude / 2);
        double c3 = Math.cos(bank / 2);
        double s1 = Math.sin(heading / 2);
        double s2 = Math.sin(attitude / 2);
        double s3 = Math.sin(bank / 2);
        Point4D Q = new Point4D();
        Q.w = c1*c2*c3 - s1*s2*s3;
        Q.x = s1*s2*c3 + c1*c2*s3;
        Q.y = s1*c2*c3 + c1*s2*s3;
        Q.z = c1*s2*c3 - s1*c2*s3;
        return Q;
    }
    
    // http://stackoverflow.com/questions/6325689/rotate-3d-euler-point-using-quaternions-to-avoid-gimbal-lock
    public static Point3D rotate(Point3D D, Point4D Q)
    {
        Point4D F = new Point4D();
        F.w = Q.w*0  - Q.x*D.x - Q.y*D.y - Q.z*D.z;
        F.x = Q.w*D.x + Q.x*0  + Q.y*D.z - Q.z*D.y;
        F.y = Q.w*D.y - Q.x*D.z + Q.y*0  + Q.z*D.x;
        F.z = Q.w*D.z + Q.x*D.y - Q.y*D.x + Q.z*0;
        Point3D C = new Point3D();
        C.x = F.w*Q.x - F.x*Q.w + F.y*Q.z - F.z*Q.y;
        C.y = F.w*Q.y - F.x*Q.z - F.y*Q.w + F.z*Q.x;
        C.z = F.w*Q.z + F.x*Q.y - F.y*Q.x - F.z*Q.w;
        return C;
    }
    
    // http://www.cprogramming.com/tutorial/3d/quaternions.html
    public static Point4D mult(Point4D q1, Point4D q2)
    {
        double w = (q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z);
        double x = (q1.w*q2.x + q1.x*q2.w + q1.y*q2.z - q1.z*q2.y);
        double y = (q1.w*q2.y - q1.x*q2.z + q1.y*q2.w + q1.z*q2.x);
        double z = (q1.w*q2.z + q1.x*q2.y - q1.y*q2.x + q1.z*q2.w);
        return new Point4D(w, x, y, z);
    }
    
    // "add" two rotations
    public static Point4D rotate(Point4D q1, Point4D q2)
    {
        double w = (q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z);
        double x = (q1.w*q2.x + q1.x*q2.w + q1.y*q2.z - q1.z*q2.y);
        double y = (q1.w*q2.y - q1.x*q2.z + q1.y*q2.w + q1.z*q2.x);
        double z = (q1.w*q2.z + q1.x*q2.y - q1.y*q2.x + q1.z*q2.w);
        Point4D p = new Point4D(w, x, y, z);
        makeNorm(p);
        return p;
    }
}
