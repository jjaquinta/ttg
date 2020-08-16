package jo.ttg.lbb.data.ship2;

import java.util.ArrayList;
import java.util.List;

public class ScenarioSide
{
	private String					mID;
	private String					mName;
	private List<Ship2Instance> 	mShips;
	
	public ScenarioSide()
	{
		mShips = new ArrayList<Ship2Instance>();
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

	public List<Ship2Instance> getShips()
	{
		return mShips;
	}

	public void setShips(List<Ship2Instance> ships)
	{
		mShips = ships;
	}
}
