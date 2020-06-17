package jo.ttg.gen;

import jo.ttg.beans.OrdBean;

public interface IGenScheme
{
    public OrdBean getSectorSize();
    
    public IGenUniverse  getGeneratorUniverse ();
    public IGenSector    getGeneratorSector   ();
    public IGenSubSector getGeneratorSubSector();
    public IGenMainWorld getGeneratorMainWorld();
    public IGenSystem    getGeneratorSystem();
    public IGenSurface   getGeneratorSurface();

    public IGenPassengers getGeneratorPassengers();
    public IGenCargo 	getGeneratorCargo();
    public IGenLanguage getGeneratorLanguage();
    
	public void nearestSec(OrdBean o);
	public void nearestSub(OrdBean o);    
	public double distance(OrdBean o1, OrdBean o2);
}