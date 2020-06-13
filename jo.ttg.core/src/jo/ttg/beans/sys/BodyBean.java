package jo.ttg.beans.sys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jo.ttg.beans.HashBean;
import jo.ttg.beans.URIBean;
import jo.ttg.utils.ConvUtils;
import jo.util.beans.Bean;
import jo.util.html.URIBuilder;
import jo.util.utils.obj.DoubleUtils;

abstract public class BodyBean extends Bean implements URIBean
{
    public static final String SCHEME = "body://";
    /**
      * Body Type Unset
      * Used only during system generation
      * @see ttg.beans.sys.BodyBean#getType
      */
    public final static int BT_UNSET = 0;
    /**
      * Body Type World
      * @see ttg.beans.sys.BodyBean#getType
      */
    public final static int BT_WORLD = 1;
    /**
      * Body Type Star
      * @see ttg.beans.sys.BodyBean#getType
      */
    public final static int BT_STAR = 2;
    /**
      * Body Type Gas Giant
      * @see ttg.beans.sys.BodyBean#getType
      */
    public final static int BT_GIANT = 3;
    /**
      * Body Type Planetoids
      * @see ttg.beans.sys.BodyBean#getType
      */
    public final static int BT_TOIDS = 4;
    /**
      * Body Type Empty
      * Used only during system generation
      * @see ttg.beans.sys.BodyBean#getType
      */
    public final static int BT_EMPTY = 5;
    /**
     * Body Type Special
     * For user defined bodies
     * @see ttg.beans.sys.BodyBean#getType
     */
   public final static int BT_SPECIAL = 6;

   // members
   private int mType;
   private double mOrbit;
   private double mOrbitalRadius;
   private double mDiameter;
   private double mDensity;
   private double mMass;
   private double mEccentricity;
   private double mTilt;
   private boolean mMainworld;
   private String mName = new String();
   private long mSeed;
   private SystemBean mSystem;
   private BodyBean mPrimary;
   private List<BodyBean> mSatelites = new ArrayList<BodyBean>();
   private HashBean mProperties;

   // constructor
   public BodyBean()
   {
   }
   
   public String toString()
   {
       return mName;
   }
   
   public abstract String getOneLineDesc();
   
    // Type
    public int getType()
    {
        return mType;
    }
    public void setType(int v)
    {
        mType = v;
    }

    // Orbit
    public double getOrbit()
    {
        return mOrbit;
    }
    public void setOrbit(double v)
    {
        mOrbit = v;
        setOrbitalRadius(convOrbitToRadius(getOrbit()));
    }

    // OrbitalRadius
    public double getOrbitalRadius()
    {
        return mOrbitalRadius;
    }
    public void setOrbitalRadius(double v)
    {
        mOrbitalRadius = v;
    }

    // Diameter in AU
    public double getDiameter()
    {
        return mDiameter;
    }
    public void setDiameter(double v)
    {
        mDiameter = v;
    }

    // Density
    public double getDensity()
    {
        return mDensity;
    }
    public void setDensity(double v)
    {
        mDensity = v;
    }

    // Mass in solar masses
    public double getMass()
    {
        return mMass;
    }
    public void setMass(double v)
    {
        mMass = v;
    }

    // Eccentricity
    public double getEccentricity()
    {
        return mEccentricity;
    }
    public void setEccentricity(double v)
    {
        mEccentricity = v;
    }

    // Tilt
    public double getTilt()
    {
        return mTilt;
    }
    public void setTilt(double v)
    {
        mTilt = v;
    }

    // Mainworld
    public boolean isMainworld()
    {
        return mMainworld;
    }
    public void setMainworld(boolean v)
    {
        mMainworld = v;
    }

    // Name
    public String getName()
    {
        return mName;
    }
    public void setName(String v)
    {
        mName = v;
    }

    // Seed
    public long getSeed()
    {
        return mSeed;
    }
    public void setSeed(long v)
    {
        mSeed = v;
        setOID(v);
    }

    // System
    public SystemBean getSystem()
    {
        return mSystem;
    }
    public void setSystem(SystemBean v)
    {
        mSystem = v;
    }

    // Primary
    public BodyBean getPrimary()
    {
        return mPrimary;
    }
    public void setPrimary(BodyBean v)
    {
        mPrimary = v;
    }

    // Satelites
    public BodyBean[] getSatelites()
    {
        BodyBean[] ret = new BodyBean[mSatelites.size()];
        mSatelites.toArray(ret);
        return ret;
    }
    public void setSatelites(BodyBean[] v)
    {
        if (v != null)
            for (int i = 0; i < v.length; i++)
                mSatelites.add(v[i]);
    }
    public BodyBean getSatelites(int index)
    {
        return mSatelites.get(index);
    }
    public void setSatelites(int index, BodyBean v)
    {
        mSatelites.set(index, v);
    }
    public Iterator<BodyBean> getSatelitesIterator()
    {
        return mSatelites.iterator();
    }
    public void addSatelites(BodyBean v)
    {
        v.setPrimary(this);
        for (int i = 0; i < mSatelites.size(); i++)
        {
            BodyBean b = mSatelites.get(i);
            if (b.getOrbitalRadius() > v.getOrbitalRadius())
            {
                mSatelites.add(i, v);
                v = null;
                break;
            }
        }
        if (v != null)
            mSatelites.add(v);
    }
    public void removeSatelites(BodyBean v)
    {
        mSatelites.remove(v);
    }
    public boolean isAnySatelites()
    {
        return mSatelites.size() > 0;
    }


    // utils

    public BodyBean getRoot()
    {
      BodyBean ret = this;
      while (ret.getPrimary() != null)
          ret = ret.getPrimary();
      return ret;
    }
    
    public BodyBean getFirstSatelite()
    {
        if (mSatelites.size() == 0)
            return null;
        return mSatelites.get(0);
    }
    
    public BodyBean getLastSatelite()
    {
        if (mSatelites.size() == 0)
            return null;
        return mSatelites.get(mSatelites.size() - 1);
    }

    public int getDepth()
    {
      int ret = 0;
      for (BodyBean b = this.getPrimary(); b != null; b = b.getPrimary())
          ret++;
      return ret;
    }

    public BodyStarBean getStar()
    {
        for (BodyBean p = getPrimary(); p != null; p = p.getPrimary())
            if (p instanceof BodyStarBean)
                return (BodyStarBean)p;
        return null;
    }

    public String getZone()
    {
        for (BodyBean p = this; p.getPrimary() != null; p = p.getPrimary())
            if (p.getPrimary() instanceof BodyStarBean)
                return ((BodyStarBean)(p.getPrimary())).getZone(p.getOrbit());
        return " ";
    }

    public boolean isOuterZone()
    {
        for (BodyBean p = this; p.getPrimary() != null; p = p.getPrimary())
            if (p.getPrimary() instanceof BodyStarBean)
                return ((BodyStarBean)(p.getPrimary())).isOuterZone(p.getOrbit());
        return true;
    }

    public boolean isInnerZone()
    {
        for (BodyBean p = this; p.getPrimary() != null; p = p.getPrimary())
            if (p.getPrimary() instanceof BodyStarBean)
                return ((BodyStarBean)(p.getPrimary())).isInnerZone(p.getOrbit());
        return false;
    }

    public boolean isHabitableZone()
    {
        for (BodyBean p = this; p.getPrimary() != null; p = p.getPrimary())
            if (p.getPrimary() instanceof BodyStarBean)
                return ((BodyStarBean)(p.getPrimary())).isHabitableZone(p.getOrbit());
        return false;
    }

    /**
     * Calculate temperature at specific orbit
     * Calculates temperature for a planet in this position with an albedeo
     * of .3 and a greenhouse effect of 1.1.
     * @return temperature in Kelvins
     * @see BodyBean#getTemperatureAt(double,double,double)
     */
    public double getTemperature()
    {
        return getPrimary().getTemperatureAt(mOrbit, 0.3, 1.1);
    }

    /**
     * Calculate temperature at specific orbit
     * Calculates temperature for a planet in this position with an albedeo
     * of .3 and a greenhouse effect of 1.1.
     * @param Orb orbit in orbit numbers
     * @return temperature in Kelvins
     * @see BodyBean#getTemperatureAt(double,double,double)
     */
    public double getTemperatureAt(double Orb)
    {
        return getTemperatureAt(Orb, 0.3, 1.1);
    }
    /**
     * Calculate temperature at specific orbit
     * Calculates temperature for a planet in this position with the given albedeo
     * and greenhouse effect.
     * @param Orb orbit in orbit numbers
     * @param Albedeo 0 to 1
     * @param Greenhouse 1 = normal
     * @return temperature in Kelvins
     * @see BodyBeanStar#getTemperatureAt(double,double,double)
     */
    public double getTemperatureAt(double Orb, double Albedeo, double Greenhouse)
    {
        // default temperature is temperature of primary
        return getPrimary().getTemperatureAt(mOrbit, Albedeo, Greenhouse);
    }

    public Iterator<BodyBean> getAllSatelitesIterator()
    {
        return getAllSatelites().iterator();
    }
    
    public Collection<BodyBean> getAllSatelites()
    {
        Set<BodyBean> ret = new HashSet<BodyBean>();
        ret.add(this);
        for (BodyBean child : mSatelites)
            ret.addAll(child.getAllSatelites());
        return ret;
    }
    
    public BodyBean findBody(double orbit)
    {
        for (Iterator<BodyBean> i = getSatelitesIterator(); i.hasNext(); )
        {
            BodyBean b = i.next();
            if (Math.abs(b.getOrbit() - orbit) < .01)
                return b;
        }
        return null;
    }

    /**
     * Period around primary
     * This function calculates the time it takes, in days, for this
     * body to orbit around its primary.
     *
     * @return period in days
     */
    public double getOrbitalPeriod()
    {
        if (getPrimary() != null)
            return getPrimary().getOrbitalPeriod(convOrbitToRadius(getOrbit()));
        return 0.0;
    }
    /**
     * Period around this
     * This function calculates the time it takes, in days, for an object
     * at the given radius to orbit around this body.
     *
     * @param Radius distance, in AU, to calculate period for.
     * @return period in days
     */
    public double getOrbitalPeriod(double radius)
    {
        return getOrbitalPeriod(radius, getMass());
    }
        
	public String getURI()
	{
		StringBuffer uri = new StringBuffer();
		Object par = getPrimary();
		if (par instanceof SystemBean)
			uri.append(((SystemBean)par).getURI());
		else if (par != null)
			uri.append(((BodyBean)par).getURI());
		else
		    uri.append(SCHEME+getSystem().getOrds().toURIString());
		uri.append('/');
		uri.append(URIBuilder.encode(getName()));
		return uri.toString();
	}
    
	public String getPath()
	{
		StringBuffer path = new StringBuffer();
		for (BodyBean b = this; b != null; b = b.getPrimary())
		{
		    if (path.length() > 0)
		        path.insert(0, "/");
		    path.insert(0, b.getName());
		}
		return path.toString();
	}
	
	public double getRadiusFromPrimaryInDiameters()
	{
		if (mPrimary == null)
			return 0;
		if (DoubleUtils.equals(mPrimary.getDiameter(), 0.0))
			return 0;
		return mOrbitalRadius/mPrimary.getDiameter();
	}
    
    // period and placement functions
    /**
     * Period around this
     * This function calculates the time it takes, in days, for an object
     * with the given radius and mass to orbit around this body.
     *
     * @param Radius distance, in AU, to calculate period for.
     * @param Mass mass, in Solar Masses, to calculate period for.
     * @return period in days
     */
    static public double    getOrbitalPeriod(double radius, double mass)
    {
        return Math.pow(radius*radius*radius/mass, .5)*365.25;
    }
 
    // calculations
    /**
     * Works out derived density.
     * Works for arbitrary calculations.
     *
     * @return calculated density
     */
    static public double calcDensityFromMassAndDiameter(
        double Mass,
        double Diameter)
    {
        double ret, r;
        r = ConvUtils.convAUToM(Diameter) / 2.0;
        ret = 4.0 / 3.0 * 3.14159 * r * r * r;
        // ret now in m^3
        ret = Mass / ret;
        return ret;
    }

    /**
     * Works out derived mass.
     * Works for arbitrary calculations.
     *
     * @return calculated mass
     */
    static public double  calcMassFromDensityAndDiameter(double density, double diameter)
    {
        double  ret, r;
        r = ConvUtils.convAUToM(diameter)/2.0;
        ret = 4.0/3.0*Math.PI*r*r*r;
        // ret now in m^3
        ret *= density;
        // convert to solar masses
        return ConvUtils.convMTToSM(ret);
    }

    /**
     * Convert orbital number to AU
     * This functions converts an arbitrary orbital number to a distance in AU.
     *
     * @return radius in AU
     * @see BodyBean#Orbit
     * @see BodyBean#OrbitalRadius
     */
    static public double convOrbitToRadius(double Orbit)
    {
        if (Orbit >= 0.0)
            return 0.2 + 0.4 * Math.pow(2, Orbit - 2.0);
        return Orbit * 0.2 + 0.2;
    }

    /**
     * Convert AU to orbital number
     * This functions converts an arbitrary distance in AU to an orbital number.
     *
     * @param Radius in AU
     * @return orbital number
     * @see BodyBean#Orbit
     * @see BodyBean#OrbitalRadius
     */
    static public double convRadiusToOrbit(double Radius)
    {
        if (Radius > 0.3)
            return Math.log((Radius - 0.2) / 0.4) / Math.log(2.0) + 2.0;
        return Radius / 0.2 - 1.0;
    }

    public HashBean getProperties()
    {
        return mProperties;
    }

    public void setProperties(HashBean properties)
    {
        mProperties = properties;
    }
}

class BodyBeanIterator implements Iterator<BodyBean>
{
    BodyBean    mAt;
    BodyBean    mRoot;

    public BodyBeanIterator(BodyBean root)
    {
        mRoot = root;
    }

    public boolean hasNext()
    {
        if (mAt == null)
            return mRoot != null;
        return getNext() != null;
    }

    public BodyBean next()
    {
        mAt = getNext();
        if (mAt == null)
            throw new java.util.NoSuchElementException();
        return mAt;
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Next body
     * This function is used to loop through the bodies of a system
     * with respect to a specific body. Only children of the given
     * body are stepped through.
     *
     * @param wrt body whose children to iterate through.
     * @return next body or null if no more.
     */
    public BodyBean getNext()
    {
        if (mAt == null)
            return mRoot;
        BodyBean[] c = mAt.getSatelites();
        if (c.length > 0)
            return c[0];
        BodyBean ret = getNextSibling(mAt);
        if (ret != null)
            return ret;
        for (ret = mAt.getPrimary(); ret != null; ret = ret.getPrimary())
            if (ret == mRoot)
                break;
            else if (getNextSibling(ret) != null)
                return getNextSibling(ret);
        return null;
    }

    public BodyBean getNextSibling(BodyBean b)
    {
        BodyBean parent = b.getPrimary();
        if (parent == null)
            return null;
        BodyBean[] c = parent.getSatelites();
        for (int i = 0; i < c.length - 1; i++)
            if (c[i] == b)
                return c[i+1];
        return null;
    }
}