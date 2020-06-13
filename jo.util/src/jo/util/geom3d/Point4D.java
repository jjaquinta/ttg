package jo.util.geom3d;

public class Point4D
{
    public double   w;
	public double	x;
	public double	y;
	public double	z;
	
	public Point4D()
	{
	    w = 0;
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Point4D(double _w, double _x, double _y, double _z)
	{
        w = _w;
		x = _x;
		y = _y;
		z = _z;
	}
	
	public Point4D(Point4D p)
	{
        w = p.w;
		x = p.x;
		y = p.y;
		z = p.z;
	}
	
	public String toString()
	{
	    return "("+w+","+x+","+y+","+z+")";
	}
    
    public String toIntString()
    {
        return "("+(int)w+","+(int)x+","+(int)y+","+(int)z+")";
    }
    
    public float[] toFloatArray()
    {
        return new float[] { (float)x, (float)y, (float)z, (float)w };
    }
    
    public double[] toDoubleArray()
    {
        return new double[] { (double)x, (double)y, (double)z, (double)w };
    }

	public Point4D add(Point4D p)
	{
		return new Point4D(w + p.w, x + p.x, y + p.y, z + p.z);
	}
	
	public Point4D sub(Point4D p)
	{
		return new Point4D(w - p.w, x - p.x, y - p.y, z - p.z);
	}
	
	public void incr(Point4D p)
	{
        w += p.w;
		x += p.x;
		y += p.y;
		z += p.z;
	}
	
	public void decr(Point4D p)
	{
        w -= p.w;
		x -= p.x;
		y -= p.y;
		z -= p.z;
	}
	
	public Point4D mult(double scale)
	{
		return new Point4D(w*scale, x*scale, y*scale, z*scale);
	}
	
	public void scale(double scale)
	{
        w *= scale;
		x *= scale;
		y *= scale;
		z *= scale;
	}
	
	public double mag()
	{
		return Math.sqrt(w*w + x*x + y*y + z*z);
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
	
	public Point4D normal()
	{
		Point4D n = new Point4D(this);
		n.normalize();
		return n;
	}
	
	public double dot(Point4D p)
	{
		return w*p.w + x*p.x + y*p.y + z*p.z;
	}
	
	public double dist(Point4D p)
	{
		return sub(p).mag();
	}

	public void set(Point4D p)
	{
        w = p.w;
		x = p.x;
		y = p.y;
		z = p.z;
	}

	public void set(double w, double x, double y, double z)
	{
        this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

    public double getW()
    {
        return w;
    }

    public void setW(double w)
    {
        this.w = w;
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
}
