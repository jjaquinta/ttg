package jo.ttg.port.beans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PortItemBean
{
    // pads
    public static final int                  APRON            = 0;
    public static final int                  MAINTENENCE      = 1;
    public static final int                  FIRE_SUPPRESSION = 2;
    // fuel
    public static final int                  FUEL_TANK        = 100;
    public static final int                  FUEL_PUMP        = 101;
    public static final int                  FUEL_REFINER     = 102;
    // office
    public static final int                  CUBICLE          = 200;
    public static final int                  BRIEFING_ROOM    = 201;
    public static final int                  TOILET           = 202;
    public static final int                  SUPPLIES         = 203;
    // warehouse
    public static final int                  WAREHOUSE        = 300;
    // concourse
    public static final int                  WAITING_ROOM     = 400;
    public static final int                  REFRESHMENTS     = 401;
    public static final int                  DUTY_FREE        = 402;
    public static final int                  CUSTOMS          = 403;
    public static final int                  SHOP             = 404;
    // central
    public static final int                  ATRIUM           = 500;
    public static final int                  BOOKING          = 501;
    // connections
    public static final int                  ROAD             = 600;
    public static final int                  GANGWAY          = 601;

    public static final Map<Integer, String> NAMES            = new HashMap<>();
    static
    {
        NAMES.put(APRON, "Landing Apron");
        NAMES.put(MAINTENENCE, "Maintenence");
        NAMES.put(FIRE_SUPPRESSION, "Fire Suppression");
        NAMES.put(FUEL_TANK, "Fuel Tank");
        NAMES.put(FUEL_PUMP, "Fuel Pump");
        NAMES.put(FUEL_REFINER, "Fuel Refiner");
        NAMES.put(CUBICLE, "Cubicle");
        NAMES.put(BRIEFING_ROOM, "Briefing Room");
        NAMES.put(TOILET, "Toilet");
        NAMES.put(SUPPLIES, "Supplies");
        NAMES.put(WAREHOUSE, "Warehouse");
        NAMES.put(WAITING_ROOM, "Waiting Room");
        NAMES.put(REFRESHMENTS, "Refreshments");
        NAMES.put(DUTY_FREE, "Duty Free");
        NAMES.put(CUSTOMS, "Customs");
        NAMES.put(SHOP, "Shop");
        NAMES.put(ATRIUM, "Atrium");
        NAMES.put(BOOKING, "Booking");
        NAMES.put(ROAD, "Road");
        NAMES.put(GANGWAY, "Gangway");
    };

    public static final Set<Integer> MACHINERY = new HashSet<>();
    static
    {
        MACHINERY.add(MAINTENENCE);
        MACHINERY.add(FIRE_SUPPRESSION);
        MACHINERY.add(FUEL_PUMP);
        MACHINERY.add(FUEL_REFINER);
    }

    public static final Set<Integer> HABITATION = new HashSet<>();
    static
    {
        HABITATION.add(CUBICLE);
        HABITATION.add(BRIEFING_ROOM);
        HABITATION.add(TOILET);
        HABITATION.add(WAITING_ROOM);
        HABITATION.add(REFRESHMENTS);
        HABITATION.add(DUTY_FREE);
        HABITATION.add(CUSTOMS);
        HABITATION.add(SHOP);
        HABITATION.add(BOOKING);
        HABITATION.add(ATRIUM);
    }

    public static final double DEFAULT_ASPECT_RATIO = 2;
    public static final Map<Integer, Double> ASPECT_RATIO = new HashMap<>();
    static
    {
        ASPECT_RATIO.put(GANGWAY, 8.0);
    }

    private PortBuildingBean mBuilding;
    private int              mType;
    private int              mNumber = 1;
    private double           mVolume;    // in cubic meters
    private String           mNotes;

    // constructors

    public PortItemBean()
    {
    }

    public PortItemBean(PortBuildingBean building, int type, double volume)
    {
        mBuilding = building;
        mType = type;
        mVolume = volume;
    }

    public PortItemBean(PortBuildingBean building, int type, double volume, int number)
    {
        mBuilding = building;
        mType = type;
        mVolume = volume;
        mNumber = number;
    }

    public PortItemBean(PortBuildingBean building, int type, double volume, int number, String notes)
    {
        mBuilding = building;
        mType = type;
        mVolume = volume;
        mNumber = number;
        mNotes = notes;
    }

    public PortItemBean(PortItemBean pi)
    {
        mBuilding = pi.mBuilding;
        mType = pi.mType;
        mVolume = pi.mVolume;
        mNumber = pi.mNumber;
        mNotes = pi.mNotes;
    }

    // utilities
    @Override
    public String toString()
    {
        String text = NAMES.get(mType);
        if (mNotes != null)
            text += "-" + mNotes;
        int vol = (int)mVolume;
        if (mNumber > 1)
        {
            text += "x" + mNumber;
            vol *= mNumber;
        }
        text += " (" + vol + ")";
        return text;
    }

    public int getQuantity()
    {
        if (mNumber == 0)
            return 1;
        else
            return mNumber;
    }

    public boolean isMachinery()
    {
        return MACHINERY.contains(mType);
    }

    public boolean isHabitation()
    {
        return HABITATION.contains(mType);
    }

    public double getTotalVolume()
    {
        return getVolume() * getNumber();
    }

    public static double getTotalVolume(List<PortItemBean> items)
    {
        double tot = 0;
        for (PortItemBean item : items)
            tot += item.getTotalVolume();
        return tot;
    }

    public static double getTotalInstanceVolume(List<PortItemInstance> items)
    {
        double tot = 0;
        for (PortItemInstance item : items)
            tot += item.getItem().getVolume();
        return tot;
    }
    
    public static double getAspectRatio(int type)
    {
        return ASPECT_RATIO.getOrDefault(type, DEFAULT_ASPECT_RATIO);
    }
    
    public double getAspectRatio()
    {
        return PortItemBean.getAspectRatio(mType);
    }

    // getters and setters

    public int getType()
    {
        return mType;
    }

    public void setType(int type)
    {
        mType = type;
    }

    public double getVolume()
    {
        return mVolume;
    }

    public void setVolume(double volume)
    {
        mVolume = volume;
    }

    public String getNotes()
    {
        return mNotes;
    }

    public void setNotes(String notes)
    {
        mNotes = notes;
    }

    public int getNumber()
    {
        return mNumber;
    }

    public void setNumber(int number)
    {
        mNumber = number;
    }

    public PortBuildingBean getBuilding()
    {
        return mBuilding;
    }

    public void setBuilding(PortBuildingBean building)
    {
        mBuilding = building;
    }
}
