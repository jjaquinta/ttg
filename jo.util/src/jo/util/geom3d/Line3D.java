package jo.util.geom3d;

public class Line3D 
{
	private Point3D	mP;
	private Point3D mN;
	
	public Line3D()
	{
		mP = new Point3D();
		mN = new Point3D(0, 0, 1);
	}
	
	public Line3D(Point3D p, Point3D n)
	{
		this();
		mP.set(p);
		mN.set(n);
		mN.normalize();
	}
	
	public Line3D(Line3D l)
	{
		this(l.getP(), l.getN());
	}
	
	// utilities
	
	@Override
	public boolean equals(Object obj)
	{
	    if (obj instanceof Line3D)
	    {
	        Line3D l2 = (Line3D)obj;
	        return mP.equals(l2.getP()) && mN.equals(l2.getN());
	    }
	    return false;
	}
	
	public String toString()
	{
		return mP.toString()+"--"+mN.toString();
	}
	
    public double dist(Point3D m)
    {
    	Point3D direct = m.sub(mP);
    	Point3D projected = mN.mult(direct.dot(mN));
    	double d = direct.sub(projected).mag();
    	return d;
    }

	
	public Point3D getP() {
		return mP;
	}
	public void setP(Point3D p) {
		mP = p;
	}
	public Point3D getN() {
		return mN;
	}
	public void setN(Point3D n) {
		mN = n;
	}
}
