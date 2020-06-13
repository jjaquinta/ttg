package jo.util.geom2d;

public class Line2D
{
	public Point2D	p1;
	public Point2D	p2;
	
	public Line2D()
	{
		p1 = new Point2D();
		p2 = new Point2D();
	}
	
	public Line2D(Point2D _p1, Point2D _p2)
	{
		p1 = new Point2D(_p1);
		p2 = new Point2D(_p2);
	}
	
	public Line2D(Line2D l)
	{
		p1 = new Point2D(l.p1);
		p2 = new Point2D(l.p2);
	}
	
	// utilities
	
	@Override
	public String toString()
	{
	    return "["+p1+" -- "+p2+"]";
	}
	
	public double length()
	{
	    return p1.dist(p2);
	}
	
	public boolean equals(Line2D l2)
	{
	    return (Point2DLogic.equals(p1, l2.p1) && Point2DLogic.equals(p2, l2.p2))
	            || (Point2DLogic.equals(p1, l2.p2) && Point2DLogic.equals(p2, l2.p1));
	}
}
