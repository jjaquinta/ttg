package jo.ttg.gen;

import jo.ttg.beans.OrdBean;

public interface IGenScheme
{
    public OrdBean getSectorSize();
    
    public IGenUniverse  getGeneratorUniverse ();
    public void          setGeneratorUniverse (IGenUniverse uni);
    public IGenSector    getGeneratorSector   ();
    public void          setGeneratorSector   (IGenSector sec);
    public IGenSubSector getGeneratorSubSector();
    public void          setGeneratorSubSector(IGenSubSector sub);
    public IGenMainWorld getGeneratorMainWorld();
    public void          setGeneratorMainWorld(IGenMainWorld mw);
    public IGenSystem    getGeneratorSystem();
    public void          setGeneratorSystem(IGenSystem sys);
    public IGenSurface   getGeneratorSurface();
    public void          setGeneratorSurface(IGenSurface surf);

    public IGenPassengers getGeneratorPassengers();
    public void           setGeneratorPassengers(IGenPassengers pass);
    public IGenCargo 	getGeneratorCargo();
    public void         setGeneratorCargo(IGenCargo cargo);
    public IGenLanguage getGeneratorLanguage();
    public void         setGeneratorLanguage(IGenLanguage lang);
    
	public void nearestSec(OrdBean o);
	public void nearestSub(OrdBean o);    
	public double distance(OrdBean o1, OrdBean o2);
}