package jo.util.geom3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Points3DLogic {

    public static Point3D getCenter(List<Point3D> points)
    {
        return getCenter(points.toArray(new Point3D[0]));
    }
	public static Point3D getCenter(Point3D... points)
	{
		Point3D total = new Point3D();
		for (Point3D p2 : points)
			total.incr(p2);
		total.scale(1.0/points.length);
		return total;
	}

	public static Point3D getClosest(Point3D p, List<Point3D> points)
	{
		Point3D closest = null;
		double closestDist = -1;
		for (Point3D p2 : points)
		{
			double d = p2.dist(p);
			if ((closest == null) || (d < closestDist))
			{
				closest = p2;
				closestDist = d;
			}
		}
		return closest;
	}

	public static List<Point3D> getPointsOnPlane(Plane3D plane, List<Point3D> points)
	{
		List<Point3D> onPlane = new ArrayList<Point3D>();
		for (Point3D p : points)
			if (Point3DLogic.isZero(plane.dist(p)))
				onPlane.add(p);
		return onPlane;
	}

	public static int equals(Point3D p, List<Point3D> points)
	{
		for (int i = 0; i < points.size(); i++)
			if (Point3DLogic.equals(p, points.get(i)))
				return i;
		return -1;
	}

	public static void sortByAngleAround(final Point3D center, final Point3D closest,
			List<Point3D> points, final Point3D normal, List<Integer> angles) {
		Collections.sort(points, new Comparator<Point3D>() {
			@Override
			public int compare(Point3D o1, Point3D o2) {
				double a1 = angleBetween(o1);
				double a2 = angleBetween(o2);
				int ret = Point3DLogic.sgn(a1 - a2);
				if (ret == 0)
				{
					double d1 = o1.dist(center);
					double d2 = o2.dist(center);
					return Point3DLogic.sgn(d2 - d1);
				}
				return ret;
			}
			private double angleBetween(Point3D p2)
			{
				double a;
				if (normal != null)
					a = Point3DLogic.signedAngleBetween(closest.sub(center), p2.sub(center), normal);
				else
					a = Point3DLogic.angleBetween(center, closest, p2);
				if (a < 0)
					a += Math.PI*2;
				return a;
			}
		});
		System.out.println("Sorted points: (closest="+closest+", center="+center+", normal="+normal+")");
		for (Point3D p : points)
		{
			double a;
			if (normal != null)
				a = Point3DLogic.signedAngleBetween(closest.sub(center), p.sub(center), normal);
			else
				a = Point3DLogic.angleBetween(center, closest, p);
			if (a < 0)
				a += Math.PI*2;
			System.out.println("  "+a+" "+p);
			if (angles != null)
				angles.add((int)(a/Math.PI*180));
		}
	}

}
