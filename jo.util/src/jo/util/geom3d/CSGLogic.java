package jo.util.geom3d;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CSGLogic 
{
	public static void getInteresectingLines(Poly3D poly)
	{
		List<Plane3D> planes = poly.getPlanes();
		List<Line3D> lines = poly.getLines();
		for (int i = 0; i < planes.size() - 1; i++)
		{
			Plane3D p1 = planes.get(i);
			for (int j = i + 1; j < planes.size(); j++)
			{
				Plane3D p2 = planes.get(j);
				Line3D l = Plane3DLogic.intersect(p1, p2);
				if (l != null)
				{
					lines.add(l);
					add(poly, p1, p2);
					add(poly, p2, p1);
					add(poly, p2, l);
					add(poly, p1, l);
					add(poly, l, p1);
					add(poly, l, p2);
				}
			}
		}
	}
	
	private static void add(Poly3D poly, Plane3D plane1, Plane3D plane2)
	{
		List<Plane3D> planes = poly.getPlanesForPlanes().get(plane1);
		if (planes == null)
		{
			planes = new ArrayList<Plane3D>();
			poly.getPlanesForPlanes().put(plane1, planes);
		}
		planes.add(plane2);
	}
	
	private static void add(Poly3D poly, Plane3D plane, Line3D line)
	{
		List<Line3D> lines = poly.getLinesForPlanes().get(plane);
		if (lines == null)
		{
			lines = new ArrayList<Line3D>();
			poly.getLinesForPlanes().put(plane, lines);
		}
		lines.add(line);
	}
	
	private static void add(Poly3D poly, Line3D line, Plane3D plane)
	{
		List<Plane3D> planes = poly.getPlanesForLines().get(line);
		if (planes == null)
		{
			planes = new ArrayList<Plane3D>();
			poly.getPlanesForLines().put(line, planes);
		}
		planes.add(plane);
	}
	
	private static Point3D add(Poly3D poly, Plane3D plane, Point3D point)
	{
		List<Point3D> points = poly.getPointsForPlanes().get(plane);
		if (points == null)
		{
			points = new ArrayList<Point3D>();
			poly.getPointsForPlanes().put(plane, points);
		}
		/*
		for (Point3D p2 : points)
			if (Point3DLogic.equals(p2, point))
				return p2;
		for (Point3D p2 : poly.getPoints())
			if (Point3DLogic.equals(p2, point))
			{
				point = p2;
				break;
			}
			*/
		poly.getPoints().add(point);
		points.add(point);
		return point;
	}
	
	public static void getInteresectingPoints(Poly3D poly)
	{
		for (Plane3D plane : poly.getPlanes())
		{
			List<Line3D> lines = poly.getLinesForPlanes().get(plane);
			System.out.println("  plane has "+lines.size()+" lines");
			for (int i = 0; i < lines.size() - 1; i++)
			{
				Line3D l1 = lines.get(i);
				for (int j = i + 1; j < lines.size(); j++)
				{
					Line3D l2 = lines.get(j);
					Point3D p = Line3DLogic.intersect(l1, l2);
					if (p != null)
						add(poly, plane, p);
				}
			}
	        List<Point3D> points = poly.getPointsForPlanes().get(plane);
            System.out.println("  plane has "+points.size()+" raw points");
            cullPoints(points);
            System.out.println("  plane has "+points.size()+" cooked points");
		}
	}
	
	private static void cullPoints(List<Point3D> points)
    {
        for (int i = 0; i < points.size() - 1; i++)
        {
            Point3D p1 = points.get(i);
            for (int j = points.size() - 1; j > i; j--)
            {
                Point3D p2 = points.get(j);
                if (Point3DLogic.equals(p1, p2))
                    points.remove(j);
            }
        }
        
    }

    public static void getInteresectingPointsOld(Poly3D poly)
	{
		List<Line3D> lines = poly.getLines();
		List<Point3D> points = poly.getPoints();
		List<Integer> count = new ArrayList<Integer>();
		for (int i = 0; i < lines.size() - 1; i++)
		{
			Line3D l1 = lines.get(i);
			for (int j = i + 1; j < lines.size(); j++)
			{
				Line3D l2 = lines.get(j);
				Point3D p = Line3DLogic.intersect(l1, l2);
				if (p != null)
				{
					int k = Points3DLogic.equals(p, points);
					if (k < 0)
					{
						points.add(p);
						count.add(1);
					}
					else
					{
						int tot = count.get(k) + 1;
						count.remove(k);
						count.add(k, tot);
					}
				}
			}
		}
		for (int i = points.size() - 1; i >= 0; i--)
			if (count.get(i) < 3)
				points.remove(i);
	}
	
	public static void getPolygons(Poly3D poly)
	{
		for (Plane3D plane : poly.getPlanes())
		{
			List<Point3D> planePoints = poly.getPointsForPlanes().get(plane);
			// eliminate points cut off by plane
			for (Plane3D plane2 : poly.getPlanesForPlanes().get(plane))
			{
				for (Iterator<Point3D> i = planePoints.iterator(); i.hasNext(); )
					if (Plane3DLogic.whichSide(plane2, i.next()) > 0)
						i.remove();
			}
			if (planePoints.size() == 0)
				continue;
			Point3D closest = Points3DLogic.getClosest(new Point3D(), planePoints);
			Point3D center = plane.getR();
			List<Integer> polyAngles = new ArrayList<Integer>();
			Points3DLogic.sortByAngleAround(center, closest, planePoints, plane.getN(), polyAngles);
			poly.getPolys().add(planePoints);
			poly.getCenters().add(center);
			poly.getAngles().add(polyAngles);
		}		
	}
	public static void getPolygonsOld(Poly3D poly)
	{
		List<Plane3D> planes = poly.getPlanes();
		List<Point3D> points = poly.getPoints();
		List<Point3D> centers = poly.getCenters();
		List<List<Integer>> angles = poly.getAngles();
		List<List<Point3D>> polys = poly.getPolys();
		// eliminate points cut off by plane
		for (Plane3D plane : planes)
		{
			for (Iterator<Point3D> i = points.iterator(); i.hasNext(); )
				if (Plane3DLogic.whichSide(plane, i.next()) > 0)
					i.remove();
		}
		for (Plane3D plane : planes)
		{
			List<Point3D> planePoints = Points3DLogic.getPointsOnPlane(plane, points);
			if (planePoints.size() == 0)
				continue;
			// remove all collinear ones but for nearest to origin
//			for (int i = 0; i < planePoints.size() - 1; i++)
//				for (int j = i + 1; j < planePoints.size(); j++)
//				{
//					Point3D p1 = planePoints.get(i);
//					Point3D p2 = planePoints.get(j);
//					List<Point3D> line = new ArrayList<Point3D>();
//					line.add(p1);
//					line.add(p2);
//					for (int k = j + 1; k < planePoints.size(); k++)
//					{
//						Point3D p3 = planePoints.get(k);
//						if (Point3DLogic.isCollinear(p1, p2, p3))
//							line.add(p3);
//					}
//					if (line.size() <= 2)
//						continue;
//					System.out.println("Found "+line.size()+" colinear");
//					Point3D k1 = Points3DLogic.getClosest(new Point3D(), line);
//					line.remove(k1);
//					Point3D k2 = Points3DLogic.getClosest(new Point3D(), line);
//					planePoints.remove(k1);
//					planePoints.removeAll(line);
//					planePoints.add(k1);
//					planePoints.add(k2);
//					i = -1;
//					System.out.println("there are "+planePoints.size()+" left");
//					break;
//				}
			Point3D closest = Points3DLogic.getClosest(new Point3D(), planePoints);
			Point3D center = plane.getR(); //Points3DLogic.getCenter(new Point3D(), planePoints);
			List<Integer> polyAngles = new ArrayList<Integer>();
			Points3DLogic.sortByAngleAround(center, closest, planePoints, plane.getN(), polyAngles);
			// eliminate poitns at same angle
//			for (int i = 0; i < planePoints.size() - 1; i++)
//			{
//				int a1 = polyAngles.get(i);
//				int a2 = polyAngles.get(i+1);
//				if (a1 == a2)
//				{
//					double d1 = planePoints.get(i).dist(center);
//					double d2 = planePoints.get(i+1).dist(center);
//					if (d1 < d2)
//					{
//						planePoints.remove(i+1);
//						polyAngles.remove(i+1);
//					}
//					else
//					{
//						planePoints.remove(i);
//						polyAngles.remove(i);
//					}
//					i--;
//				}
//			}
			polys.add(planePoints);
			if (centers != null)
				centers.add(center);
			if (angles != null)
				angles.add(polyAngles);
		}
	}
	
	public static List<Plane3D> makeDodecahedron(List<Plane3D> dodecahedron)
	{
		double phi = (1.0 + Math.sqrt(5.0))/2.0;
		double rad2 = 1.36/Math.sqrt(2+phi); 
		if (dodecahedron == null)
			dodecahedron = new ArrayList<Plane3D>();
		dodecahedron.add(new Plane3D(new Point3D(1, 0, phi), rad2));
		dodecahedron.add(new Plane3D(new Point3D(1, 0, -phi), rad2));
		dodecahedron.add(new Plane3D(new Point3D(-1, 0, phi), rad2));
		dodecahedron.add(new Plane3D(new Point3D(-1, 0, -phi), rad2));
		dodecahedron.add(new Plane3D(new Point3D(0, phi, 1), rad2));
		dodecahedron.add(new Plane3D(new Point3D(0, -phi, 1), rad2));
		dodecahedron.add(new Plane3D(new Point3D(0, phi, -1), rad2));
		dodecahedron.add(new Plane3D(new Point3D(0, -phi, -1), rad2));
		dodecahedron.add(new Plane3D(new Point3D(phi, 1, 0), rad2));
		dodecahedron.add(new Plane3D(new Point3D(-phi, 1, 0), rad2));
		dodecahedron.add(new Plane3D(new Point3D(phi, -1, 0), rad2));
		dodecahedron.add(new Plane3D(new Point3D(-phi, -1, 0), rad2));
		return dodecahedron;
	}

	public static List<Plane3D> makeSnubDodecahedron(List<Plane3D> snubdodecahedron)
	{
		double RAD1=1/1.0096; // triangle
		double RAD2=1/1.0586; // pentagon 
		if (snubdodecahedron == null)
			snubdodecahedron = new ArrayList<Plane3D>();
		snubdodecahedron.add(new Plane3D(new Point3D(-0.9820, 0.1298, -0.1952), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.9796, -0.0959, 0.2249), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.7658, 0.3127, -0.5789), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.7481, 0.0528, -0.6759), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.7342, -0.5109, 0.4683), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.6928, -0.7328, 0.0482), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.5620, 0.7496, 0.3762), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.5193, -0.6644, 0.5551), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.5174, 0.7112, -0.4958), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.4863, -0.8008, -0.3762), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.4222, -0.3127, 0.8621), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.4222, -0.6783, -0.6173), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.3588, -0.5624, 0.7578), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.3171, 0.9567, -0.0584), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.3128, 0.8679, 0.4101), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.2655, 0.8272, -0.5144), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.1867, 0.9745, 0.1864), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.1624, 0.9535, -0.2893), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.0940, -0.6461, 0.7700), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.0865, 0.7227, -0.6996), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.0321, -0.9445, -0.3552), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.0049, -0.8272, 0.5789), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.0909, 0.9881, 0.1864), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.1854, 0.7506, -0.6493), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.1867, -0.9745, -0.1864), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.2267, 0.8943, 0.4101), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.2504, 0.3570, -0.9106), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.2655, -0.8272, 0.5144), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.3588, 0.5624, -0.7578), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.3891, 0.1166, -0.9243), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.4119, 0.5248, 0.7578), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.4434, -0.7455, -0.5167), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.4863, 0.8008, 0.3762), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.5816, 0.6107, 0.5551), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.5953, 0.8065, 0.1206), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.5998, 0.5156, -0.6275), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.6328, -0.1609, 0.7700), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.6537, 0.6597, -0.3960), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.6570, 0.1157, 0.7578), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.6621, -0.5624, 0.5144), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.7658, -0.3127, 0.5789), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.7934, 0.6216, 0.0584), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.8102, -0.4944, -0.3442), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.9154, -0.1792, 0.3864), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.9228, -0.2406, -0.3315), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.9528, -0.3030, 0.1403), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.9997, 0.1335, -0.0459), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.9962, 0.1421, 0.0821), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.9497, -0.3325, 0.0821), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.9137, 0.3761, 0.2074), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.8862, 0.3523, -0.3315), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.8234, -0.5461, 0.2074), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.8193, 0.3589, 0.4683), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.6959, -0.6953, -0.2272), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.6383, 0.5511, 0.5551), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.5760, 0.4974, -0.6634), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.5713, -0.4572, -0.6957), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.5417, -0.0076, -0.8520), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.4748, 0.2249, 0.8621), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.4610, 0.4822, 0.7578), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.4507, -0.2701, -0.8621), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.2855, -0.9378, -0.2413), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.2444, 0.1157, 0.9727), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.2173, -0.1609, 0.9727), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.1878, -0.3147, -0.9407), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.0309, -0.0943, -1.0047), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.0132, 0.2701, 0.9727), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.1396, -0.9961, 0.0866), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.2319, 0.1390, 0.9727), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.2444, -0.1157, -0.9727), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.3399, -0.9141, 0.2611), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.3471, -0.3562, -0.8786), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.4314, -0.6461, 0.6448), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.4314, -0.8721, -0.2696), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.4507, 0.2701, 0.8621), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.5888, -0.3508, -0.7414), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.6383, -0.5511, -0.5551), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.8234, 0.5461, -0.2074), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.9296, 0.2949, -0.2611), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(0.9962, -0.1421, -0.0821), RAD1));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.3210, -0.9793, 0.2420), RAD2));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.0349, 0.7158, 0.7792), RAD2));
		snubdodecahedron.add(new Plane3D(new Point3D(0.0349, -0.7158, -0.7792), RAD2));
		snubdodecahedron.add(new Plane3D(new Point3D(0.3210, 0.9793, -0.2420), RAD2));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.9296, -0.3067, -0.4031), RAD2));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.7528, 0.7409, -0.0711), RAD2));
		snubdodecahedron.add(new Plane3D(new Point3D(0.7528, -0.7409, 0.0711), RAD2));
		snubdodecahedron.add(new Plane3D(new Point3D(0.9296, 0.3067, 0.4031), RAD2));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.8078, -0.0791, 0.6796), RAD2));
		snubdodecahedron.add(new Plane3D(new Point3D(-0.2319, 0.3474, -0.9727), RAD2));
		snubdodecahedron.add(new Plane3D(new Point3D(0.2319, -0.3474, 0.9727), RAD2));
		snubdodecahedron.add(new Plane3D(new Point3D(0.8078, 0.0791, -0.6796), RAD2));
		return snubdodecahedron;
	}
	
	public static List<Plane3D> makeCube(List<Plane3D> cube)
	{
		if (cube == null)
			cube = new ArrayList<Plane3D>();
		cube.add(new Plane3D(new Point3D(1, 0, 0), 1));
		cube.add(new Plane3D(new Point3D(-1, 0, 0), 1));
		cube.add(new Plane3D(new Point3D(0, 1, 0), 1));
		cube.add(new Plane3D(new Point3D(0, -1, 0), 1));
		cube.add(new Plane3D(new Point3D(0, 0, 1), 1));
		cube.add(new Plane3D(new Point3D(0, 0, -1), 1));
		return cube;
	}
	
	public static void makeBrilliant(Poly3D poly)
	{
		// http://www.gemologyproject.com/wiki/index.php?title=Faceting
		int index = 96;
		int[] girdle = new int[] { 3, 9, 15, 21, 27, 33, 39, 45, 51, 57, 63, 69, 75, 81, 87, 93, } ;
		int[][] pavilion = {
				{ 4500, 3, 9, 15, 21, 27, 33, 39, 45, 51, 57, 63, 69, 75, 81, 87, 93, },	
				{ 4300, 96, 12, 24, 36, 48, 60, 72, 84, },	
		};
		int[][] crown = {
				{ 4700, 3, 9, 15, 21, 27, 33, 39, 45, 51, 57, 63, 69, 75, 81, 87, 93, },	
				{ 4200, 96, 12, 24, 36, 48, 60, 72, 84, },	
				{ 2700, 6, 18, 30, 42, 54, 66, 78, 90 },	
		};
		// cut it
		addCuts(poly, 90, girdle, index, 1.0, 0);
		addCuts(poly, pavilion[0][0]/100.0, pavilion[0], index, 1.0, 0.2);
		addCuts(poly, pavilion[1][0]/100.0, pavilion[1], index, 1.02, 0.2);
		addCuts(poly, 180-crown[0][0]/100.0, crown[0], index, 1.0, -0.2);
		addCuts(poly, 180-crown[1][0]/100.0, crown[1], index, 1.02, -0.2);
		
		// top and tail
		Plane3D tail = new Plane3D();
		tail.getN().set(0, 0, 1);
		tail.getR().set(0, 0, 2);
		poly.getPlanes().add(tail);
		Plane3D top = new Plane3D();
		top.getN().set(0, 0, -1);
		top.getR().set(0, 0, -0.55);
		poly.getPlanes().add(top);
	}
	
	private static void addCuts(Poly3D poly, double angle, int[] stops, int index, double radius, double dz)
	{
		for (int r : stops)
			if (r <= index)
				addCut(poly, angle, r, index, radius, dz);
	}
	
	private static void addCut(Poly3D poly, double angle, int stop, int index, double radius, double dz)
	{
		Plane3D cut = new Plane3D();
		cut.getR().set(radius, 0, 0);
		cut.getN().set(0, 0, 1);
		Point3D rotR = new Point3D(0, 0, 2*Math.PI*stop/index);
		Point3D rotN = new Point3D(0, Math.PI*angle/180.0, 2*Math.PI*stop/index);
		Point3DLogic.rotateBy(cut.getR(), rotR);
		Point3DLogic.rotateBy(cut.getN(), rotN);
		cut.getR().z += dz;
		poly.getPlanes().add(cut);
	}
	
	public static final void main(String[] argv)
	{
		Poly3D mPoly = new Poly3D();
		//CSGLogic.makeCube(mPoly.getPlanes());
		//CSGLogic.makeDodecahedron(mPoly.getPlanes());
		CSGLogic.makeSnubDodecahedron(mPoly.getPlanes());
		//CSGLogic.makeBrilliant(mPoly);
		System.out.println((new Date()).toString());
		System.out.println(mPoly.getPlanes().size()+" Planes:");
		//for (Plane3D p : mPoly.getPlanes())
		///	System.out.println("  "+p);
		CSGLogic.getInteresectingLines(mPoly);
		System.out.println((new Date()).toString());
		System.out.println(mPoly.getLines().size()+" Lines:");
		//for (Line3D l : mPoly.getLines())
		//	System.out.println("  "+l);
		CSGLogic.getInteresectingPoints(mPoly);
		System.out.println((new Date()).toString());
		System.out.println(mPoly.getPoints().size()+" Points:");
		//for (Point3D p : mPoly.getPoints())
		//	System.out.println("  "+p);
		CSGLogic.getPolygons(mPoly);
		System.out.println((new Date()).toString());
		System.out.println(mPoly.getPolys().size()+" Polygons:");
		for (List<Point3D> poly : mPoly.getPolys())
		{
			System.out.print("  "+poly.size()+":");
			for (Point3D p : poly)
				System.out.print(" "+p);
			System.out.println();
		}
		/*
		System.out.println("Sunflow:");
		int triangles = 0;
		Set<Point3D> ps = new HashSet<Point3D>();
		for (List<Point3D> poly : mPoly.getPolys())
		{
			triangles += poly.size() - 2;
			ps.addAll(poly);
		}
		List<Point3D> points = new ArrayList<Point3D>();
		points.addAll(ps);
		System.out.println("object {");
		System.out.println("	  shader Glass");
		System.out.println("	  type mesh");
		System.out.println("	  name polySurfac");
		System.out.println("	  "+triangles+" "+points.size());
		for (Point3D p : points)
		{
			Point3D n = p.normal();
			System.out.println("	v "+p.x*10+" "+p.y*10+" "+p.z*10+"    "+n.x+" "+n.y+" "+n.z+"    0 0");
		}
		for (List<Point3D> poly : mPoly.getPolys())
		{
			for (int i = 0; i < poly.size() - 2; i++)
			{
				Point3D p1 = poly.get(i+0);
				Point3D p2 = poly.get(i+1);
				Point3D p3 = poly.get(i+2);
				int i1 = points.indexOf(p1);
				int i2 = points.indexOf(p2);
				int i3 = points.indexOf(p3);
				System.out.println("	t "+i1+" "+i2+" "+i3);
			}
		}
		System.out.println("}");
		*/
	}
}
