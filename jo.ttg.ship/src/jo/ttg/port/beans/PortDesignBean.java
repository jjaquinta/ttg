package jo.ttg.port.beans;

import java.util.ArrayList;
import java.util.List;

public class PortDesignBean
{
    private PortSpecsBean          mSpec;
    private List<PortBuildingBean> mBuildings = new ArrayList<PortBuildingBean>();

    private double                 mMaxShipVolume;
    private double                 mMachineryVolume;
    private double                 mFuelVolume;
    private double                 mHabitationVolume;
    private double                 mWarehouseVolume;

    // utilities
    public PortDesignBean add(PortBuildingBean building)
    {
        mBuildings.add(building);
        for (PortItemBean item : building.getItems())
        {
            if (item.isMachinery())
                mMachineryVolume += item.getTotalVolume() * building.getNumber();
            if (item.isHabitation())
                mHabitationVolume += item.getTotalVolume() * building.getNumber();
        }
        return this;
    }
    
    public PortBuildingBean getFirstBuilding(int type)
    {
        for (PortBuildingBean building : mBuildings)
            if (building.getType() == type)
                return building;
        return null;
    }
    
    public List<PortBuildingBean> getAllBuildings(int type, String notes)
    {
        List<PortBuildingBean> items = new ArrayList<>();
        for (PortBuildingBean building : mBuildings)
            if ((building.getType() == type) && compareNotes(building.getNotes(), notes))
                items.add(building);
        return items;
    }
    
    private static boolean compareNotes(String notes1, String notes2)
    {
        if (notes2 == null)
            return true;
        if (notes1 == null)
            return false;
        int o = notes2.indexOf('*');
        if (o < 0)
            return notes1.equals(notes2);
        return notes1.startsWith(notes2.substring(0, o)) && notes1.endsWith(notes2.substring(o + 1));
    }
    
    public List<PortBuildingBean> getAllBuildings(int type)
    {
        return getAllBuildings(type, null);
    }
    
    public int getCount(int type)
    {
        int tot = 0;
        for (PortBuildingBean item : getAllBuildings(type))
            tot += item.getNumber();
        return tot;
    }

    // getters and setters

    public PortSpecsBean getSpec()
    {
        return mSpec;
    }

    public void setSpec(PortSpecsBean spec)
    {
        mSpec = spec;
    }

    public List<PortBuildingBean> getBuildings()
    {
        return mBuildings;
    }

    public void setBuildings(List<PortBuildingBean> buildings)
    {
        mBuildings = buildings;
    }

    public double getMachineryVolume()
    {
        return mMachineryVolume;
    }

    public void setMachineryVolume(double machineryVolume)
    {
        mMachineryVolume = machineryVolume;
    }

    public double getMaxShipVolume()
    {
        return mMaxShipVolume;
    }

    public void setMaxShipVolume(double maxShipVolume)
    {
        mMaxShipVolume = maxShipVolume;
    }

    public double getFuelVolume()
    {
        return mFuelVolume;
    }

    public void setFuelVolume(double fuelVolume)
    {
        mFuelVolume = fuelVolume;
    }

    public double getHabitationVolume()
    {
        return mHabitationVolume;
    }

    public void setHabitationVolume(double habitationVolume)
    {
        mHabitationVolume = habitationVolume;
    }

    public double getWarehouseVolume()
    {
        return mWarehouseVolume;
    }

    public void setWarehouseVolume(double warehouseVolume)
    {
        mWarehouseVolume = warehouseVolume;
    }
}
