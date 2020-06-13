package jo.ttg.ship.beans.comp;

public class ControlLargeHolodisplay extends ControlComponent
{
	public double getCP()
	{
		return 1500.0*getNumber();
	}

	public String getName()
	{
		return "Large Holodisplay";
	}

	public int getTechLevel()
	{
		return 12;
	}

	public double getVolume()
	{
		return 2.0*getNumber();
	}

	public double getWeight()
	{
		return 1.0*getNumber()*(isECP() ? 1.5 : 1.0);
	}

	public double getPower()
	{
		return .050*getNumber();
	}

	public double getPrice()
	{
		return 500000*getNumber()*(isECP() ? 1.5 : 1.0);
	}
}
