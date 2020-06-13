package jo.util.geom3d;

import jo.util.geom2d.Point2D;

public class Point3D extends Point2D
{
    public static final Point3D XP = new Point3D( 1, 0, 0);
    public static final Point3D XM = new Point3D(-1, 0, 0);
    public static final Point3D YP = new Point3D( 0, 1, 0);
    public static final Point3D YM = new Point3D( 0,-1, 0);
    public static final Point3D ZP = new Point3D( 0, 0, 1);
    public static final Point3D ZM = new Point3D( 0, 0,-1);
    
	public double	z;
	
	public Point3D()
	{
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Point3D(double _x, double _y, double _z)
	{
		x = _x;
		y = _y;
		z = _z;
	}
	
	public Point3D(Point3D p)
	{
		x = p.x;
		y = p.y;
		z = p.z;
	}
    
    // utilites
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Point3D)
        {
            Point3D p2 = (Point3D)obj;
            return Point3DLogic.equals(this, p2);
        }
        return false;
    }

	public String toString()
	{
	    return "("+x+","+y+","+z+")";
	}
    
    public String toIntString()
    {
        return "("+(int)x+","+(int)y+","+(int)z+")";
    }
    
    public float[] toFloatArray()
    {
        return new float[] { (float)x, (float)y, (float)z };
    }
    
    public double[] toDoubleArray()
    {
        return new double[] { (double)x, (double)y, (double)z };
    }
    
    @Override
    public int hashCode()
    {
        return Double.hashCode(x)^Double.hashCode(y)^Double.hashCode(z);
    }
    
    public int rawHashCode()
    {
        return super.hashCode();
    }
	
	public Point3D add(Point3D p)
	{
		return new Point3D(x + p.x, y + p.y, z + p.z);
	}
	
	public Point3D sub(Point3D p)
	{
		return new Point3D(x - p.x, y - p.y, z - p.z);
	}
	
	public void incr(Point3D p)
	{
		x += p.x;
		y += p.y;
		z += p.z;
	}
	
	public void decr(Point3D p)
	{
		x -= p.x;
		y -= p.y;
		z -= p.z;
	}
	
	public Point3D mult(double scale)
	{
		return new Point3D(x*scale, y*scale, z*scale);
	}
	
	public void scale(double scale)
	{
		x *= scale;
		y *= scale;
		z *= scale;
	}
    
    public void scale(Point3D scale)
    {
        x *= scale.x;
        y *= scale.y;
        z *= scale.z;
    }
	
	public double mag()
	{
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public void setMag(double mag)
	{
		normalize();
		scale(mag);
	}
	
	public void normalize()
	{
		double m = mag();
		if (m > 0)
			scale(1/m);
	}
	
	public Point3D normal()
	{
		Point3D n = new Point3D(this);
		n.normalize();
		return n;
	}
	
	public double dot(Point3D p)
	{
		return x*p.x + y*p.y + z*p.z;
	}
	
	public Point3D cross(Point3D v2)
	{
    	Point3D v3 = new Point3D();
    	v3.x = y*v2.z - z*v2.y;
    	v3.y = z*v2.x - x*v2.z;
    	v3.z = x*v2.y - y*v2.x;
    	return v3;
	}
	
	public double dist(Point3D p)
	{
		return sub(p).mag();
	}

    public double dist(double x, double y, double z)
    {
        return dist(new Point3D(x, y, z));
    }

	public void set(Point3D p)
	{
		x = p.x;
		y = p.y;
		z = p.z;
	}

	public void set(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getZ()
    {
        return z;
    }

    public void setZ(double z)
    {
        this.z = z;
    }

    public Point3D rotateAround(Point3D axis, double theta)
    {
        return Point3DLogic.rotateAround(this, axis, theta);
    }

	/*
	public void rotate(double theta)
	{
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		double nx = x*cosTheta + y*sinTheta;
		double ny = -x*sinTheta + y*cosTheta;
		x = nx;
		y = ny;
	}
	
	public Point3D rotation(double theta)
	{
		Point3D p = new Point3D(this);
		p.rotate(theta);
		return p;
	}
	
	public void rotate(Point3D around, double theta)
	{
		decr(around);
		rotate(theta);
		incr(around);
	}
	
	public Point3D rotation(Point3D around, double theta)
	{
		Point3D p = new Point3D(this);
		p.decr(around);
		p.rotate(theta);
		p.incr(around);
		return p;
	}
	*/
}
