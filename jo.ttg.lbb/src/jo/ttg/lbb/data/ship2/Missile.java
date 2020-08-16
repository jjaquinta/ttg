package jo.ttg.lbb.data.ship2;

import jo.util.geom3d.Point3D;

public class Missile
{
	private String			mName;
	private Point3D			mLocation;
	private Point3D			mVelocity;
	private int				mTurns;
	private Ship2Instance	mTarget;
	
	public Missile()
	{
		mLocation = new Point3D();
		mVelocity = new Point3D();
	}
	
	public String getName()
	{
		return mName;
	}
	public void setName(String iD)
	{
		mName = iD;
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
	public int getTurns()
	{
		return mTurns;
	}
	public void setTurns(int turns)
	{
		mTurns = turns;
	}
	public Ship2Instance getTarget()
	{
		return mTarget;
	}
	public void setTarget(Ship2Instance target)
	{
		mTarget = target;
	}
}
