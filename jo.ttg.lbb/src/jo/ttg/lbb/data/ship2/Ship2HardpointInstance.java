package jo.ttg.lbb.data.ship2;

public class Ship2HardpointInstance
{
	private String					mName;
	private Ship2HardpointDesign	mDesign;
    private boolean					mDamage;
    private int						mMissiles;
    private int						mSand;
	private int						mGunnerSkill;
	private boolean					mGunnerReloading;
	private boolean					mFireSand;
	private Ship2Instance			mTarget;
 
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		Ship2HardpointInstance hp = new Ship2HardpointInstance();
		hp.setName(mName);
		hp.setDesign(mDesign);
		hp.setDamage(mDamage);
		hp.setMissiles(mMissiles);
		hp.setSand(mSand);
		hp.setGunnerSkill(mGunnerSkill);
		hp.setGunnerReloading(mGunnerReloading);
		hp.setFireSand(mFireSand);
		hp.setTarget(mTarget);
		return hp;
	}
	public Ship2HardpointDesign getDesign()
	{
		return mDesign;
	}
	public void setDesign(Ship2HardpointDesign design)
	{
		mDesign = design;
	}
	public boolean isDamage()
	{
		return mDamage;
	}
	public void setDamage(boolean damage)
	{
		mDamage = damage;
	}
	public int getMissiles()
	{
		return mMissiles;
	}
	public void setMissiles(int missiles)
	{
		mMissiles = missiles;
	}
	public int getSand()
	{
		return mSand;
	}
	public void setSand(int sand)
	{
		mSand = sand;
	}
	public int getGunnerSkill()
	{
		return mGunnerSkill;
	}
	public void setGunnerSkill(int gunnerSkill)
	{
		mGunnerSkill = gunnerSkill;
	}
	public Ship2Instance getTarget()
	{
		return mTarget;
	}
	public void setTarget(Ship2Instance target)
	{
		mTarget = target;
	}
	public boolean isGunnerReloading()
	{
		return mGunnerReloading;
	}
	public void setGunnerReloading(boolean gunnerReloading)
	{
		mGunnerReloading = gunnerReloading;
	}
	public boolean isFireSand()
	{
		return mFireSand;
	}
	public void setFireSand(boolean fireSand)
	{
		mFireSand = fireSand;
	}
	public String getName()
	{
		return mName;
	}
	public void setName(String name)
	{
		mName = name;
	}
}
