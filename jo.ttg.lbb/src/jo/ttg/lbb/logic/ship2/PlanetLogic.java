package jo.ttg.lbb.logic.ship2;

import jo.ttg.lbb.data.ship2.Planet;

public class PlanetLogic
{
	public static double getRadius(Planet p)
	{
		return p.getSize()*8;
	}
	
	public static double getGravAt(Planet p, double d)
	{
		return p.getDensity()*Math.pow(p.getSize(), 3)*8/Math.pow(d, 2);
	}
	
	public static double getMass(Planet p)
	{
		return p.getDensity()*Math.pow(getRadius(p)/8, 3);
	}
}
