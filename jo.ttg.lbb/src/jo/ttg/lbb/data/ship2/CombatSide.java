package jo.ttg.lbb.data.ship2;

import java.util.ArrayList;
import java.util.List;

public class CombatSide
{
	private ScenarioSide			mScenarioSide;;
	private String					mName;
	private List<Ship2Instance> 	mShips;
	private List<Missile>			mMissiles;
	
	public CombatSide()
	{
		mShips = new ArrayList<Ship2Instance>();
		mMissiles = new ArrayList<Missile>();
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

	public ScenarioSide getScenarioSide()
	{
		return mScenarioSide;
	}

	public void setScenarioSide(ScenarioSide scenarioSide)
	{
		mScenarioSide = scenarioSide;
	}

	public List<Missile> getMissiles()
	{
		return mMissiles;
	}

	public void setMissiles(List<Missile> missiles)
	{
		mMissiles = missiles;
	}
}
