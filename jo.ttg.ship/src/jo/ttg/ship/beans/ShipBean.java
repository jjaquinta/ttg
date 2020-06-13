package jo.ttg.ship.beans;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.ship.beans.comp.ShipComponent;
import jo.util.beans.PCSBean;

public class ShipBean extends PCSBean
{
    public static final String SCHEME = "shipmt://";
    
	private List<ShipComponent>	mComponents;
	private String		mName;
	private String		mType;
	private String		mNotes;
	
	public ShipBean()
	{
		mComponents = new ArrayList<ShipComponent>();
		mName = "";
		mType = "";
		mNotes = "";
	}
	
	public String toString()
	{
		return getName();
	}
	
	public void fireComponentChange()
	{
		queuePropertyChange("components", false, true);
		firePropertyChange();
	}
	
    public List<ShipComponent> getComponents()
    {
        return mComponents;
    }

    public void setComponents(List<ShipComponent> list)
    {
        mComponents = list;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String string)
    {
        mName = string;
    }

    public String getType()
    {
        return mType;
    }

    public void setType(String string)
    {
        mType = string;
    }

	/**
	 * @return
	 */
	public String getNotes()
	{
		return mNotes;
	}

	/**
	 * @param string
	 */
	public void setNotes(String string)
	{
		mNotes = string;
	}

}
