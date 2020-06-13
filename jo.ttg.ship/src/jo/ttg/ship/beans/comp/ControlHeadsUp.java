package jo.ttg.ship.beans.comp;

public class ControlHeadsUp extends ControlComponent
{
	public double getCP()
	{
		return 50.0*getNumber();
	}

	public String getName()
	{
		return "Heads-up Display";
	}

	public int getTechLevel()
	{
		return 9;
	}

	public double getVolume()
	{
		return .5*getNumber();
	}

	public double getWeight()
	{
		return .2*getNumber()*(isECP() ? 1.5 : 1.0);
	}

	public double getPower()
	{
		return .005*getNumber();
	}

	public double getPrice()
	{
		return 20000*getNumber()*(isECP() ? 1.5 : 1.0);
	}
}
