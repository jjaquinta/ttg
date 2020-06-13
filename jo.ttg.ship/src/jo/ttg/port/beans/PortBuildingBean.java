package jo.ttg.port.beans;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.util.utils.obj.IntegerUtils;

public class PortBuildingBean
{
    public static final int LANDING_PAD = 0;
    public static final int FUEL_TANK = 1;
    public static final int FUEL_REFINERY = 2;
    public static final int WAREHOUSE = 3;
    public static final int CONCOURSE = 4;
    public static final int CENTRAL = 5;
    public static final int GANGWAY = 6;
    public static final int ROAD = 7;
    
    public static final String[] NAMES = {
            "Landing Pad",
            "Fuel Tank",
            "Fuel Refinery",
            "Warehouse",
            "Concourse",
            "Central",
            "Gangway",
            "Road",
    };
    
    private int mType;
    private int mNumber = 1;
    private String  mNotes;
    private List<PortItemBean> mItems = new ArrayList<>();
    
    // constructors
    
    public PortBuildingBean()
    {    
        mNumber = 1;
    }
    
    public PortBuildingBean(int type)
    {
        mType = type;
    }
    
    public PortBuildingBean(int type, int number, String notes)
    {
        mType = type;
        mNumber = number;
        mNotes = notes;
    }
    
    public PortBuildingBean(PortBuildingBean pi)
    {
        mType = pi.mType;
        mNumber = pi.mNumber;
        mNotes = pi.mNotes;
    }

    public PortBuildingBean(JSONObject json)
    {
        fromJSON(json);
    }
    
    // utilities
    @Override
    public String toString()
    {
        String text = ShipSquareBean.NAMES[mType];
        if (mNotes != null)
            text += "-"+mNotes;
        if (mNumber > 1)
            text += "x"+mNumber;
        return text;
    }
    
    public int getQuantity()
    {
        if (mNumber == 0)
            return 1;
        else
            return mNumber;
    }
    
    public double getUnitVolume()
    {
        double v = 0;
        for (PortItemBean item : mItems)
            v += item.getTotalVolume();
        return v;
    }
    
    public double getTotalVolume()
    {
        return getUnitVolume()*getNumber();
    }
    
    public PortBuildingBean add(PortItemBean item)
    {
        mItems.add(item);
        return this;
    }
    
    public PortItemBean add(int type, double volume, int number)
    {
        if (number < 0)
            return null;
        PortItemBean item = new PortItemBean(this, type, volume, number);
        add(item);
        return item;
    }
    
    public PortItemBean getFirstItem(int type)
    {
        for (PortItemBean item : mItems)
            if (item.getType() == type)
                return item;
        return null;
    }
    
    public List<PortItemBean> getAllItems(int type)
    {
        List<PortItemBean> items = new ArrayList<>();
        for (PortItemBean item : mItems)
            if (item.getType() == type)
                items.add(item);
        return items;
    }
    
    public List<PortItemInstance> getAllItemInstances(int type)
    {
        List<PortItemInstance> items = new ArrayList<>();
        for (PortItemBean item : mItems)
            if (item.getType() == type)
                for (int inst = 1; inst < item.getNumber(); inst++)
                    items.add(new PortItemInstance(item, inst));
        return items;
    }
    
    public int getCount(int type)
    {
        int tot = 0;
        for (PortItemBean item : getAllItems(type))
            tot += item.getNumber();
        return tot;
    }
    
    // I/O
    
    public JSONObject toJSON()
    {
        JSONObject json = new JSONObject();      
        json.put("type", mType);
        json.put("number", mNumber);
        if (mNotes != null)
            json.put("notes", mNotes);
        return json;
    }
    
    public void fromJSON(JSONObject json)
    {
        mType = IntegerUtils.parseInt(json.get("type"));
        mNumber = IntegerUtils.parseInt(json.get("number"));
        mNotes = json.getString("notes");
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

    public List<PortItemBean> getItems()
    {
        return mItems;
    }

    public void setItems(List<PortItemBean> items)
    {
        mItems = items;
    }
}
