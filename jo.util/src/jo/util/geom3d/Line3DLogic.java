package jo.util.geom3d;

public class Line3DLogic {
	// make a line from two points
	public static Line3D fromPoints(Point3D p1, Point3D p2)
	{
		return new Line3D(p1, p2.sub(p1));
	}

	public static Point3D[] closestPoints(Line3D l1, Line3D l2)
	{
		Point3D p21 = l2.getP().sub(l1.getP());
		Point3D m = l2.getN().cross(l1.getN());
		double m2 = m.dot(m);
		if (m2 == 0)
			return null;
		Point3D r = p21.cross(m.mult(1/m2));
		double t1 = r.dot(l2.getN());
		Point3D q1 = l1.getP().add(l1.getN().mult(t1));
		double t2 = r.dot(l1.getN());
		Point3D q2 = l2.getP().add(l2.getN().mult(t2));
		return new Point3D[] { q1, q2 };
	}
	
	public static double dist(Line3D l, Point3D p)
	{
    	Point3D direct = p.sub(l.getP());
    	Point3D projected = l.getN().mult(direct.dot(l.getN()));
    	double d = direct.sub(projected).mag();
    	return d;
	}
	
	public static double dist(Line3D l1, Line3D l2)
	{
		Point3D[] closest = closestPoints(l1, l2);
		if (closest == null)
			return dist(l1, l2.getP()); // parallel
		double dist = closest[0].dist(closest[1]);
		return dist;
	}
	
	public static Point3D intersect(Line3D l1, Line3D l2)
	{
		Point3D[] closest = closestPoints(l1, l2);
		if (closest == null)
			return null;
		double dist = closest[0].dist(closest[1]);
		if (dist > Point3DLogic.EPSILON)
			return null;
		return closest[0];
	}

    public static Line3D fromSegment(LineSegment3D segment)
    {
        return fromPoints(segment.getP1(), segment.getP2());
    }
}
