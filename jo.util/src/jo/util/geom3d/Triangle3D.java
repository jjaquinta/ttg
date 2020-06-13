package jo.util.geom3d;

public class Triangle3D 
{
	private Point3D	mP1;
    private Point3D mP2;
    private Point3D mP3;
	private Point3D mN;
	
	// constructors
	
	public Triangle3D()
	{
		mP1 = new Point3D();
        mP2 = new Point3D();
        mP3 = new Point3D();
		mN = new Point3D(0, 0, 1);
	}
	
	public Triangle3D(Point3D p1, Point3D p2, Point3D p3, Point3D n)
	{
		this();
		mP1.set(p1);
        mP2.set(p2);
        mP3.set(p3);
		mN.set(n);
		mN.normalize();
	}
    
    public Triangle3D(Point3D p1, Point3D p2, Point3D p3)
    {
        this();
        mP1.set(p1);
        mP2.set(p2);
        mP3.set(p3);
        mN = Triangle3DLogic.makeNormal(mP1, mP2, mP3);
    }
	
	public Triangle3D(Triangle3D l)
	{
		this(l.getP1(), l.getP2(), l.getP3(), l.getN());
	}
	
	// utilities
	
	public String toString()
	{
		return mP1.toString()+"--"+mP2.toString()+"--"+mP3.toString()+"!"+mN.toString();
	}
	
	public void translate(Point3D delta)
	{
	    mP1.incr(delta);
        mP2.incr(delta);
        mP3.incr(delta);
	}
	
	public void scale(double m)
	{
	    mP1.scale(m);
        mP2.scale(m);
        mP3.scale(m);
	}

    public void scale(Point3D m)
    {
        mP1.scale(m);
        mP2.scale(m);
        mP3.scale(m);
    }
    
    public void rotate(double rx, double ry, double rz)
    {
        rotateAround(new Point3D(0,0,1), rz);
        rotateAround(new Point3D(0,1,0), ry);
        rotateAround(new Point3D(1,0,0), rx);
    }
    
    public void rotateAround(Point3D axis, double theta)
    {
        if (theta == 0)
            return;
        mN = mN.rotateAround(new Point3D(), theta);
        mP1 = mP1.rotateAround(axis, theta);
        mP2 = mP2.rotateAround(axis, theta);
        mP3 = mP3.rotateAround(axis, theta);
    }
    
    public double perimiter()
    {
        return mP1.dist(mP2) + mP2.dist(mP3) + mP3.dist(mP1);
    }
    
    public double maxEdge()
    {
        return Math.max(mP1.dist(mP2), Math.max(mP2.dist(mP3), mP3.dist(mP1)));
    }
	
	// getters and setters
	
	public Point3D getP1() {
		return mP1;
	}
	public void setP1(Point3D p) {
		mP1 = p;
	}
    public Point3D getP2() {
        return mP2;
    }
    public void setP2(Point3D p) {
        mP2 = p;
    }
    public Point3D getP3() {
        return mP3;
    }
    public void setP3(Point3D p) {
        mP3 = p;
    }
	public Point3D getN() {
		return mN;
	}
	public void setN(Point3D n) {
		mN = n;
	}
}
