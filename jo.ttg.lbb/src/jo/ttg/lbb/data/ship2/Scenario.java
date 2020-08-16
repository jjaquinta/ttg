package jo.ttg.lbb.data.ship2;

import java.util.ArrayList;
import java.util.List;

public class Scenario
{
	private String	mID;
	private String	mName;
	private List<Ship2Design> 	mDesigns;
	private List<ScenarioSide> 	mSides;
	private List<Planet>		mPlanets;
	
	public Scenario()
	{
		mDesigns = new ArrayList<Ship2Design>();
		mSides = new ArrayList<ScenarioSide>();
		mPlanets = new ArrayList<Planet>();
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

	public List<Ship2Design> getDesigns()
	{
		return mDesigns;
	}

	public void setDesigns(List<Ship2Design> designs)
	{
		mDesigns = designs;
	}

	public List<ScenarioSide> getSides()
	{
		return mSides;
	}

	public void setSides(List<ScenarioSide> sides)
	{
		mSides = sides;
	}

	public List<Planet> getPlanets()
	{
		return mPlanets;
	}

	public void setPlanets(List<Planet> planets)
	{
		mPlanets = planets;
	}
}
