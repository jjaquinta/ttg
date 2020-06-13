package jo.ttg.ship.beans.comp;

public class ControlHeadsUpHolo extends ControlComponent
{
	public double getCP()
	{
		return 200.0*getNumber();
	}

	public String getName()
	{
		return "Heads-up Holodisplay";
	}

	public int getTechLevel()
	{
		return 13;
	}

	public double getVolume()
	{
		return 1.0*getNumber();
	}

	public double getWeight()
	{
		return 0.5*getNumber()*(isECP() ? 1.5 : 1.0);
	}

	public double getPower()
	{
		return .020*getNumber();
	}

	public double getPrice()
	{
		return 100000*getNumber()*(isECP() ? 1.5 : 1.0);
	}
}
