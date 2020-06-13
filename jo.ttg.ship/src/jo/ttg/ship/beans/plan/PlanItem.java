package jo.ttg.ship.beans.plan;

import org.json.simple.JSONObject;

import jo.util.utils.obj.IntegerUtils;

public class PlanItem
{
    private int mType;
    private int mNumber;
    private double mVolume; // in cubic meters
    private String  mNotes;
    
    // constructors
    
    public PlanItem()
    {    
        mNumber = 1;
    }
    
    public PlanItem(int type, double volume)
    {
        mType = type;
        mVolume = volume;
    }
    
    public PlanItem(int type, double volume, int number, String notes)
    {
        mType = type;
        mVolume = volume;
        mNumber = number;
        mNotes = notes;
    }
    
    public PlanItem(PlanItem pi)
    {
        mType = pi.mType;
        mVolume = pi.mVolume;
        mNumber = pi.mNumber;
        mNotes = pi.mNotes;
    }

    public PlanItem(JSONObject json)
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
        int vol = (int)mVolume;
        if (mNumber > 1)
        {
            text += "x"+mNumber;
            vol *= mNumber;
        }
        text += " ("+vol+")";
        return text;
    }
    
    public int getQuantity()
    {
        if (mNumber == 0)
            return 1;
        else
            return mNumber;
    }
    
    // I/O
    
    public JSONObject toJSON()
    {
        JSONObject json = new JSONObject();      
        json.put("type", mType);
        json.put("number", mNumber);
        json.put("volume", mVolume);
        if (mNotes != null)
            json.put("notes", mNotes);
        return json;
    }
    
    public void fromJSON(JSONObject json)
    {
        mType = IntegerUtils.parseInt(json.get("type"));
        mNumber = IntegerUtils.parseInt(json.get("number"));
        mVolume = IntegerUtils.parseInt(json.get("volume"));
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
}
