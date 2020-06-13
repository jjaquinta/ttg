package jo.util.geom2d;

import jo.util.utils.MathUtils;

public class Point2D
{
	public double	x;
	public double	y;
	
	public Point2D()
	{
		x = 0;
		y = 0;
	}
	
	public Point2D(double _x, double _y)
	{
		x = _x;
		y = _y;
	}
	
	public Point2D(Point2D p)
	{
		x = p.x;
		y = p.y;
	}

    public String toString()
    {
        return "("+x+","+y+")";
    }
    
	public Point2D add(Point2D p)
	{
		return new Point2D(x + p.x, y + p.y);
	}
	
	public Point2D sub(Point2D p)
	{
		return new Point2D(x - p.x, y - p.y);
	}
	
	public void incr(Point2D p)
	{
		x += p.x;
		y += p.y;
	}
	
	public void decr(Point2D p)
	{
		x -= p.x;
		y -= p.y;
	}
	
	public Point2D mult(double scale)
	{
		return new Point2D(x*scale, y*scale);
	}
	
	public void scale(double scale)
	{
		x *= scale;
		y *= scale;
	}
	
	public double mag()
	{
		return Math.sqrt(x*x + y*y);
	}
	
	public void normalize()
	{
		double m = mag();
		if (m > 0)
			scale(1/m);
	}
	
	public Point2D normal()
	{
		Point2D n = new Point2D(this);
		n.normalize();
		return n;
	}
	
	public double dot(Point2D p)
	{
		return x*p.x + y*p.y;
	}
	
	public double dist(Point2D p)
	{
		return sub(p).mag();
	}
    
    public double dist(double x, double y)
    {
        return Point2DLogic.dist(this.x, this.y, x, y);
    }
	
	public void rotate(double theta)
	{
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		double nx = x*cosTheta + y*sinTheta;
		double ny = -x*sinTheta + y*cosTheta;
		x = nx;
		y = ny;
	}
	
	public Point2D rotation(double theta)
	{
		Point2D p = new Point2D(this);
		p.rotate(theta);
		return p;
	}
	
	public void rotate(Point2D around, double theta)
	{
		decr(around);
		rotate(theta);
		incr(around);
	}
	
	public Point2D rotation(Point2D around, double theta)
	{
		Point2D p = new Point2D(this);
		p.decr(around);
		p.rotate(theta);
		p.incr(around);
		return p;
	}
	
	public boolean equals(double x, double y)
	{
	    return MathUtils.equals(this.x, x) && MathUtils.equals(this.y, y);
	}
	
	public boolean equals(Object o2)
	{
	    if (o2 instanceof Point2D)
	        return Point2DLogic.equals(this, (Point2D)o2);
	    else
	        return super.equals(o2);
	}
}
