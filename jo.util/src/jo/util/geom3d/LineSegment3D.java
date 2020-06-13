package jo.util.geom3d;

public class LineSegment3D
{
    private Point3D mP1;
    private Point3D mP2;

    public LineSegment3D()
    {
        mP1 = new Point3D();
        mP2 = new Point3D(0, 0, 1);
    }

    public LineSegment3D(Point3D p1, Point3D p2)
    {
        this();
        mP1.set(p1);
        mP2.set(p2);
    }

    public LineSegment3D(LineSegment3D l)
    {
        this(l.getP1(), l.getP2());
    }
    
    // utilites
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof LineSegment3D)
        {
            LineSegment3D line2 = (LineSegment3D)obj;
            return (mP1.equals(line2.mP1) && mP2.equals(line2.mP2))
                    || (mP1.equals(line2.mP2) && mP2.equals(line2.mP1));
        }
        return false;
    }

    public String toString()
    {
        return mP1.toString() + "--" + mP2.toString();
    }

    public double length()
    {
        return mP1.dist(mP2);
    }
    
    public Point3D getP1()
    {
        return mP1;
    }

    public void setP1(Point3D p1)
    {
        mP1 = p1;
    }

    public Point3D getP2()
    {
        return mP2;
    }

    public void setP2(Point3D p2)
    {
        mP2 = p2;
    }
}
