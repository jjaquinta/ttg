package jo.util.geom3d;

public class Plane3D 
{
	private Point3D	mR; // position
	private Point3D	mN; // normal
	
	public Plane3D()
	{
		mR = new Point3D();
		mN = new Point3D(0, 0, 1);
	}
	
	public Plane3D(Point3D r, Point3D n)
	{
		this();
		mR.set(r);
		mN.set(n);
		mN.normalize();
	}
	
	public Plane3D(Point3D n, double radius)
	{
		this();
		mN.set(n);
		mN.normalize();
		mR.set(mN);
		mR.scale(radius);
	}
	
	public Plane3D(Plane3D p)
	{
		this(p.getR(), p.getN());
	}
	
	public String toString()
	{
		return mR+"||"+mN;
	}
	
	public double dist(Point3D p)
	{
		return Math.abs(p.sub(mR).dot(mN));
	}
	
	public Point3D getR() {
		return mR;
	}
	public void setR(Point3D r) {
		mR = r;
	}
	public Point3D getN() {
		return mN;
	}
	public void setN(Point3D n) {
		mN = n;
	}
}
