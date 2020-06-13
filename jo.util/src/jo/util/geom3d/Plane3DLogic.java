package jo.util.geom3d;

public class Plane3DLogic 
{
	// make a plane from a point, and two vectors originating at that point
	public static Plane3D fromPointAndVectors(Point3D p, Point3D v1, Point3D v2)
	{
		Point3D norm = v1.cross(v2);
		return new Plane3D(p, norm);
	}

	// make a plane from three points
	public static Plane3D fromPoints(Point3D p1, Point3D p2, Point3D p3)
	{
		return fromPointAndVectors(p1, p2.sub(p1), p3.sub(p1));
	}
	
	public static Line3D intersect(Plane3D p1, Plane3D p2)
	{
		double h1 = p1.getR().dot(p1.getN());
		double h2 = p2.getR().dot(p2.getN());
		double n1dotn2 = p1.getN().dot(p2.getN());
		if (Math.abs(Math.abs(n1dotn2) - 1) < Point3DLogic.EPSILON)
			return null;
		double c1 = (h1 - h2*n1dotn2)/(1 - n1dotn2*n1dotn2);
		double c2 = (h2 - h1*n1dotn2)/(1 - n1dotn2*n1dotn2);
		Point3D p = p1.getN().mult(c1).add(p2.getN().mult(c2));
		Point3D n = p1.getN().cross(p2.getN());
		return new Line3D(p, n);
	}
	
	public static double angle(Plane3D p1, Plane3D p2)
	{
		double cosa = p1.getN().dot(p2.getN());
		return Math.acos(cosa);
	}
	
	public static int whichSide(Plane3D plane, Point3D point)
	{
		double dot = point.sub(plane.getR()).dot(plane.getN());
		return Point3DLogic.sgn(dot);
	}
}
