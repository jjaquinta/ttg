package jo.util.geom3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poly3D {
	private List<Plane3D> mPlanes;
	private List<Line3D> mLines;
	private List<Point3D> mPoints;
	private List<List<Point3D>> mPolys;
	private List<Point3D> mCenters;
	private List<List<Integer>> mAngles;
	private Map<Plane3D,List<Plane3D>> mPlanesForPlanes;
	private Map<Line3D,List<Plane3D>> mPlanesForLines;
	private Map<Plane3D, List<Line3D>> mLinesForPlanes;
	private Map<Plane3D, List<Point3D>> mPointsForPlanes;
	
	public Poly3D()
	{
		mPlanes = new ArrayList<Plane3D>();
		mLines = new ArrayList<Line3D>();
		mPoints = new ArrayList<Point3D>();
		mPolys = new ArrayList<List<Point3D>>();
		mCenters = new ArrayList<Point3D>();
		mAngles = new ArrayList<List<Integer>>();
		mPlanesForPlanes = new HashMap<Plane3D, List<Plane3D>>();
		mPlanesForLines = new HashMap<Line3D, List<Plane3D>>();
		mLinesForPlanes = new HashMap<Plane3D, List<Line3D>>();
		mPointsForPlanes = new HashMap<Plane3D, List<Point3D>>();
	}
	
	public List<Plane3D> getPlanes() {
		return mPlanes;
	}
	public void setPlanes(List<Plane3D> planes) {
		mPlanes = planes;
	}
	public List<Line3D> getLines() {
		return mLines;
	}
	public void setLines(List<Line3D> lines) {
		mLines = lines;
	}
	public List<Point3D> getPoints() {
		return mPoints;
	}
	public void setPoints(List<Point3D> points) {
		mPoints = points;
	}
	public List<List<Point3D>> getPolys() {
		return mPolys;
	}
	public void setPolys(List<List<Point3D>> polys) {
		mPolys = polys;
	}
	public List<Point3D> getCenters() {
		return mCenters;
	}
	public void setCenters(List<Point3D> centers) {
		mCenters = centers;
	}
	public List<List<Integer>> getAngles() {
		return mAngles;
	}
	public void setAngles(List<List<Integer>> angles) {
		mAngles = angles;
	}

	public Map<Line3D, List<Plane3D>> getPlanesForLines() {
		return mPlanesForLines;
	}

	public void setPlanesForLines(Map<Line3D, List<Plane3D>> planesForLines) {
		mPlanesForLines = planesForLines;
	}

	public Map<Plane3D, List<Line3D>> getLinesForPlanes() {
		return mLinesForPlanes;
	}

	public void setLinesForPlanes(Map<Plane3D, List<Line3D>> linesForPlanes) {
		mLinesForPlanes = linesForPlanes;
	}

	public Map<Plane3D, List<Point3D>> getPointsForPlanes() {
		return mPointsForPlanes;
	}

	public void setPointsForPlanes(Map<Plane3D, List<Point3D>> pointsForPlanes) {
		mPointsForPlanes = pointsForPlanes;
	}

	public Map<Plane3D, List<Plane3D>> getPlanesForPlanes() {
		return mPlanesForPlanes;
	}

	public void setPlanesForPlanes(Map<Plane3D, List<Plane3D>> planesForPlanes) {
		mPlanesForPlanes = planesForPlanes;
	}

}
