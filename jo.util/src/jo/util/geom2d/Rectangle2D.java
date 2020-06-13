package jo.util.geom2d;

public class Rectangle2D
{
	public Point2D	p1;
	public Point2D	p2;
	
	// constructors
	
	public Rectangle2D()
	{
		p1 = new Point2D();
		p2 = new Point2D();
	}
	
	public Rectangle2D(Point2D _p1, Point2D _p2)
	{
		p1 = new Point2D(_p1);
		p2 = new Point2D(_p2);
		normalize();
	}
	
	public Rectangle2D(Rectangle2D l)
	{
		p1 = new Point2D(l.p1);
		p2 = new Point2D(l.p2);
        normalize();
	}
	
	// utilities
	
	@Override
	public String toString()
	{
	    return "["+p1+" -- "+p2+"]";
	}
	
	public void normalize()
	{
	    if (p1.x > p2.x)
	    {
	        double x = p1.x;
	        p1.x = p2.x;
	        p2.x = x;
	    }
        if (p1.y > p2.y)
        {
            double y = p1.y;
            p1.y = p2.y;
            p2.y = y;
        }
	}
	
	public double width()
	{
	    return Math.abs(p2.x - p1.x);
	}
    
    public double height()
    {
        return Math.abs(p2.y - p1.y);
    }
    
    public double area()
    {
        return width()*height();
    }
    
    public void extend(Point2D p)
    {
        if (p.x < p1.x)
            p1.x = p.x;
        if (p.x > p2.x)
            p2.x = p.x;
        if (p.y < p1.y)
            p1.y = p.y;
        if (p.y > p2.y)
            p2.y = p.y;
    }
    
    public double left()
    {
        return p1.x;
    }
    
    public double right()
    {
        return p2.x;
    }
    
    public double top()
    {
        return p1.y;
    }
    
    public double bottonm()
    {
        return p2.y;
    }
    
    public double midX()
    {
        return (p1.x + p2.x)/2;
    }
    
    public double midY()
    {
        return (p1.y + p2.y)/2;
    }
}
