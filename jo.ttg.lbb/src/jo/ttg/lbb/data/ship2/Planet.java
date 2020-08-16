package jo.ttg.lbb.data.ship2;

import jo.util.geom3d.Point3D;

public class Planet
{
	private String	mID;
	private String	mName;
	private Point3D	mLocation;
	private Point3D mVelocity;
	private int		mSize; // UPP digit
	private double	mDensity;
	private boolean	mAtmosphere;
	
	public Planet()
	{
		mLocation = new Point3D();
		mVelocity = new Point3D();
	}
	
	public Point3D getLocation()
	{
		return mLocation;
	}
	public void setLocation(Point3D location)
	{
		mLocation = location;
	}
	public Point3D getVelocity()
	{
		return mVelocity;
	}
	public void setVelocity(Point3D velocity)
	{
		mVelocity = velocity;
	}
	public int getSize()
	{
		return mSize;
	}
	public void setSize(int size)
	{
		mSize = size;
	}
	public double getDensity()
	{
		return mDensity;
	}
	public void setDensity(double density)
	{
		mDensity = density;
	}

	public String getID()
	{
		return mID;
	}

	public void setID(String iD)
	{
		mID = iD;
	}

	public String getName()
	{
		return mName;
	}

	public void setName(String name)
	{
		mName = name;
	}

	public boolean isAtmosphere()
	{
		return mAtmosphere;
	}

	public void setAtmosphere(boolean atmosphere)
	{
		mAtmosphere = atmosphere;
	} 
}
