package jo.ttg.beans.surf;

import jo.util.heal.IHEALCoord;

public class SurfaceAnnotationBean
{
    public static final int MEGACITY = 1;
    public static final int CITY = 2;
    public static final int TOWN = 3;
    public static final int VILLAGE = 4;
    public static final int OCEAN = 5;
    public static final int LAKE = 6;
    public static final int LAKES = 7;
    public static final int CONTINENT = 8;
    public static final int ISLAND = 9;
    public static final int ISLANDS = 10;
    public static final int DESERT = 11;
    public static final int CRATER = 12;
    
    private IHEALCoord  mLocation;
    private String      mName;
    private int         mType;
    
    // constructors
    
    public SurfaceAnnotationBean()
    {        
    }
    
    public SurfaceAnnotationBean(IHEALCoord location, int type, String name)
    {        
        mLocation = location;
        mType = type;
        mName = name;
    }
    
    // utilities

    public String getDescription()
    {
        switch (getType())
        {
            case SurfaceAnnotationBean.MEGACITY:
                return "Captial "+getName();
            case SurfaceAnnotationBean.CITY:
                return "City of "+getName();
            case SurfaceAnnotationBean.TOWN:
                return "Town of "+getName();
            case SurfaceAnnotationBean.CONTINENT:
                return "Continent "+getName();
            case SurfaceAnnotationBean.ISLAND:
                return "Island of "+getName();
            case SurfaceAnnotationBean.ISLANDS:
                return "The "+getName()+" Islands";
            case SurfaceAnnotationBean.OCEAN:
                return getName()+" Ocean";
            case SurfaceAnnotationBean.LAKE:
                return "Lake "+getName();
            case SurfaceAnnotationBean.LAKES:
                return "The "+getName()+" Lakes";
            case SurfaceAnnotationBean.DESERT:
                return getName()+" Desert";
            case SurfaceAnnotationBean.CRATER:
                return getName()+" Crater";
        }
        return getName();
    }

    
    // getters and setters
    
    public IHEALCoord getLocation()
    {
        return mLocation;
    }
    public void setLocation(IHEALCoord location)
    {
        mLocation = location;
    }
    public String getName()
    {
        return mName;
    }
    public void setName(String name)
    {
        mName = name;
    }
    public int getType()
    {
        return mType;
    }
    public void setType(int type)
    {
        mType = type;
    }
}
