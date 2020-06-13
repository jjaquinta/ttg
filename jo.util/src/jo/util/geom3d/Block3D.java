package jo.util.geom3d;

import java.util.List;

public class Block3D
{
	public double	x;
	public double	y;
	public double	z;
    public double   width;
    public double   height;
    public double   depth;
	
    // constructors
    
	public Block3D()
	{
		x = 0;
		y = 0;
		z = 0;
		width = 0;
        height = 0;
        depth = 0;
	}
	
	public Block3D(double _x, double _y, double _z, double _width, double _height, double _depth)
	{
		x = _x;
		y = _y;
		z = _z;
	}
	
	public Block3D(Block3D p)
	{
		x = p.x;
		y = p.y;
		z = p.z;
		width = p.width;
		height = p.height;
		depth = p.depth;
	}
    
    public Block3D(Point3D p1, Point3D p2)
    {
        x = Math.min(p1.x, p2.x);
        y = Math.min(p1.y, p2.y);
        z = Math.min(p1.z, p2.z);
        width = Math.abs(p1.x - p2.x);
        height = Math.abs(p1.y - p2.y);
        depth = Math.abs(p1.z - p2.z);
    }

	// return bounding block for mesh
	public Block3D(Mesh3D mesh)
	{
	    this(mesh.getMesh());
	}

    // return bounding block for mesh
    public Block3D(List<Triangle3D> mesh)
    {
        this();
        Point3D p = mesh.get(0).getP1(); // start with one point
        x = p.x;
        y = p.y;
        z = p.z;
        for (Triangle3D tri : mesh)
            extend(tri);
    }
	
	// utilities
	
	public String toString()
	{
	    return "("+x+","+y+","+z+")x("+width+","+height+","+depth+")";
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
	
	public void incr(Block3D p)
	{
		x += p.x;
		y += p.y;
		z += p.z;
	}
	
	public void decr(Block3D p)
	{
		x -= p.x;
		y -= p.y;
		z -= p.z;
	}
	
	public void scale(double scale)
	{
		x *= scale;
		y *= scale;
		z *= scale;
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
	
	public Block3D normal()
	{
		Block3D n = new Block3D(this);
		n.normalize();
		return n;
	}
	
	public double dot(Block3D p)
	{
		return x*p.x + y*p.y + z*p.z;
	}
	
	public Block3D cross(Block3D v2)
	{
    	Block3D v3 = new Block3D();
    	v3.x = y*v2.z - z*v2.y;
    	v3.y = z*v2.x - x*v2.z;
    	v3.z = x*v2.y - y*v2.x;
    	return v3;
	}

	public void set(Block3D p)
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
    
    public void extend(Triangle3D tri)
    {
        extend(tri.getP1());
        extend(tri.getP2());
        extend(tri.getP3());
    }
    
    public void extend(Point3D vec)
    {
        if (vec.x < getXlower())
            setXlower(vec.x);
        else if (vec.x > getXupper())
            setXupper(vec.x);
        if (vec.y < getYlower())
            setYlower(vec.y);
        else if (vec.y > getYupper())
            setYupper(vec.y);
        if (vec.z < getZlower())
            setZlower(vec.z);
        else if (vec.z > getZupper())
            setZupper(vec.z);
    }
    
    public double getXlower()
    {
        return x;
    }
    
    public double getYlower()
    {
        return y;
    }
    
    public double getZlower()
    {
        return z;
    }
    
    public void setXlower(double xl)
    {
        width = getXupper() - xl;
        x = xl;
    }

    public void setYlower(double yl)
    {
        height = getYupper() - yl;
        y = yl;
    }

    public void setZlower(double zl)
    {
        depth = getZupper() - zl;
        z = zl;
    }

    public double getXupper()
    {
        return x + width;
    }

    public double getYupper()
    {
        return y + height;
    }

    public double getZupper()
    {
        return z + depth;
    }
    
    public void setXupper(double xu)
    {
        width = xu - x;
    }
    
    public void setYupper(double yu)
    {
        height = yu - y;
    }
    
    public void setZupper(double zu)
    {
        depth = zu - z;
    }
    
    public Point3D getCenter()
    {
        return new Point3D(x + width/2, y + height/2, z + depth/2);
    }
    
    public boolean contains(Point3D p)
    {
        if (p.x < x || p.x > x + width)
            return false;
        if (p.y < y || p.y > y + height)
            return false;
        if (p.z < z || p.z > z + depth)
            return false;
        return true;
    }
	
	// getters and setters

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

    public double getWidth()
    {
        return width;
    }

    public void setWidth(double width)
    {
        this.width = width;
    }

    public double getHeight()
    {
        return height;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public double getDepth()
    {
        return depth;
    }

    public void setDepth(double depth)
    {
        this.depth = depth;
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
