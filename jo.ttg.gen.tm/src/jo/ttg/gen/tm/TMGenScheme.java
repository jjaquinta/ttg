package jo.ttg.gen.tm;

import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.beans.uni.UniverseBean;
import jo.ttg.gen.imp.ImpGenScheme;
import jo.ttg.logic.mw.MainWorldLogic;

public class TMGenScheme extends ImpGenScheme
{
    private int mXLow;
    private int mXHigh;
    private int mYLow;
    private int mYHigh;

    public TMGenScheme()
	{
		initialize();
	}
    public TMGenScheme(int xlow, int ylow, int xhigh, int yhigh)
    {
        mXLow = xlow;
        mXHigh = xhigh;
        mYLow = ylow;
        mYHigh = yhigh;
        initialize();
    }
    
    public static TMGenScheme newInstanceSpinward()
    {
        return new TMGenScheme(0, 0, 1, 1);
    }
    
    public static TMGenScheme newInstanceKnownWorld()
    {
        return new TMGenScheme(0, 0, 7, 5);
    }
    
    public static TMGenScheme newInstanceEverything()
    {
        return new TMGenScheme(-99, -99, 99, 99);
    }
    
	protected void initialize()
	{
		mGeneratorUniverse = new TMGenUniverse(this);
		mGeneratorSector = new TMGenSector(this);
 		mGeneratorSubSector = new TMGenSubSector(this);
		mGeneratorMainWorld = new TMGenMainWorld(this);
		mGeneratorPassengers = super.getGeneratorPassengers();
		mGeneratorCargo = super.getGeneratorCargo();
		initializeData();
	}

    public void resetData()
    {
    	initialize();
    }
	
	TMUniverseBean	mUniverse;
    
    private void initializeData()
    {
		mUniverse = new TMUniverseBean(this, mXLow, mYLow, mXHigh, mYHigh);
        mUniverse.setName("TravellerMap");
    }
	
    private static void test(TMGenScheme gen)
    {
        long tick0 = System.currentTimeMillis();
        UniverseBean uni = gen.getGeneratorUniverse().generateUniverse();
        SectorBean[] secs = uni.getSectors();
        System.out.println("  Sectors: "+secs.length);
        for (int i = 0; i < secs.length; i++)
        {
            System.out.print("    #"+i+", ub="+secs[i].getUpperBound());
            System.out.print(", name="+secs[i].getName());
            System.out.println();
            SubSectorBean[] subs = secs[i].getSubSectors();
            for (int j = 0; j < subs.length; j++)
            {
                System.out.println("      SubSector:"+subs[j].getName());
                MainWorldBean[] worlds = subs[j].getMainWorlds();
                for (int k = 0; k < worlds.length; k++)
                    System.out.println("        "+MainWorldLogic.getExportLine(worlds[k]));
            }
        }
        long tick1 = System.currentTimeMillis();
        System.gc();
        System.out.println("  Time: "+(tick1-tick0));
    }

    public static void main(String[] argv)
    {
		TMGenScheme gen;
        System.out.println("spinward:");
        gen = TMGenScheme.newInstanceSpinward();
        //test(gen);
        System.out.println("known world:");
        gen = TMGenScheme.newInstanceKnownWorld();
        //test(gen);
        System.out.println("everything:");
        gen = TMGenScheme.newInstanceEverything();
        test(gen);
        /* test 1 
        OrdBean ords = new OrdBean();
        SectorBean sec = gen.getGeneratorSector().generateSector(ords);
        System.out.println("Sector "+sec.getName());
        SubSectorBean[] subs = sec.getSubSectors();
        for (int i = 0; i < subs.length; i++)
        {
            System.out.println("  SubSector:"+subs[i].getName());
            MainWorldBean[] worlds = subs[i].getMainWorlds();
            for (int j = 0; j < worlds.length; j++)
                System.out.println("    "+MainWorldLogic.getExportLine(worlds[j]));
        }
        */
        /* test2
        OrdBean size = gen.getSectorSize();
        for (int j = -1; j <= 1; j++)
        {
            for (int i = -1; i <= 1; i++)
            {
                ords.set(i*size.getX(), j*size.getY(), 0);
                SectorBean sec = gen.getGeneratorSector().generateSector(ords);
                System.out.print("  "+sec.getName());
            }
            System.out.println();
        }
        */
        /* test3
        UniverseBean uni = gen.getGeneratorUniverse().generateUniverse();
        MainWorldBean home;
        do
        {
            ords.setX(ords.getX()+1);
            home = uni.findMainWorld(ords);
        } while (home == null);
        System.out.println("Home:"+home.exportLine());
        System.out.println(BeanUtil.toXML(home, "mainworld"));
        */
        /* test4
        UniverseBean uni = gen.getGeneratorUniverse().generateUniverse();
        MainWorldBean home;
        do
        {
            ords.setX(ords.getX()+1);
            home = uni.findMainWorld(ords);
        } while (home == null);
        SubSectorBean sub = uni.findSubSector(ords);
        System.out.println(BeanUtil.toXML(sub, "subsector"));
        */
        /* test5
        UniverseBean uni = gen.getGeneratorUniverse().generateUniverse();
        MainWorldBean home;
        do
        {
            ords.setX(ords.getX()+1);
            home = uni.findMainWorld(ords);
        } while (home == null);
        SectorBean sec = uni.findSector(ords);
        System.out.println(BeanUtil.toXML(sec, "sector"));
        */
        /* test6
        ords.setX(9999);
        UniverseBean uni = gen.getGeneratorUniverse().generateUniverse();
        SectorBean sec = (SectorBean)uni.findSector(ords);
System.out.println(ords+"->"+sec.getUpperBound());
        sec = (SectorBean)uni.findByURI("sec://"+ords.toString());
        List mws = sec.findMainWorlds(sec.getUpperBound(), sec.getLowerBound());
        System.out.println("#worlds =  "+mws.size());
        */
        /* test7 
        UniverseBean uni = gen.getGeneratorUniverse().generateUniverse();
        List mws = uni.findMainWorlds(new OrdBean(0,0,0), new OrdBean(32, 40, 1));
        System.out.println(mws.size()+" worlds.");
        MainWorldBean home;
        for (Iterator i = mws.iterator(); i.hasNext(); )
        {
            MainWorldBean mw = (MainWorldBean)i.next();
            System.out.println("World:"+mw.getExportLine());
            //System.out.println("Generating system.");
            ttg.gen.GenSystem genSys = gen.getGeneratorSystem();
            SystemBean sys = genSys.generateSystem(mw.getOrds());
            //System.out.println("Generated system.");
        }
        */
    }
}