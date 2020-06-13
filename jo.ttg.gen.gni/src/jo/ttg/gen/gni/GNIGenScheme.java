package jo.ttg.gen.gni;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.LinkBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.beans.uni.UniverseBean;
import jo.ttg.gen.imp.ImpGenMainWorld;
import jo.ttg.gen.imp.ImpGenScheme;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.util.utils.io.ResourceUtils;
import jo.util.utils.obj.IntegerUtils;

public class GNIGenScheme extends ImpGenScheme
{
    public static final OrdBean LUNION = new OrdBean(20,23,0); 
    public static final OrdBean GRAM = new OrdBean(11,22,0); 
    public static final OrdBean GLISTEN = new OrdBean(19,35,0); 
    public static final OrdBean REGINA = new OrdBean(18,9,0); 
    public static final OrdBean RHYLANOR = new OrdBean(26,15,0); 
    
	protected static String X_LABELS = "12345ABCDEFGHIJKLMNOPQRST"; 
    protected static String Y_LABELS = "67890abcdefghijklmnopq";
    protected static long X_ORIGIN = -7; 
    protected static long Y_ORIGIN = -9;
	
	private int	mXLow;
	private int	mXHigh;
	private int	mYLow;
	private int	mYHigh;
	
	static final String  RESOURCE_PATH = "jo/ttg/gen/gni/data/";

	public GNIGenScheme(long xlow, long ylow, long xhigh, long yhigh)
	{
		mXLow = (int)(xlow - X_ORIGIN);
		mXHigh = (int)(xhigh - X_ORIGIN);
		mYLow = (int)(ylow - Y_ORIGIN);
		mYHigh = (int)(yhigh - Y_ORIGIN);
		initialize();
	}
	
	public static GNIGenScheme newInstanceSpinward()
	{
		return new GNIGenScheme(0, 0, 1, 1);
	}
	
	public static GNIGenScheme newInstanceKnownWorld()
	{
		return new GNIGenScheme(0, 0, 7, 5);
	}
	
	public static GNIGenScheme newInstanceEverything()
	{
		return new GNIGenScheme(X_ORIGIN, Y_ORIGIN, X_ORIGIN + X_LABELS.length(), Y_ORIGIN + Y_LABELS.length());
	}
	
	protected void initialize()
	{
		mGeneratorUniverse = new GNIGenUniverse(this);
		mGeneratorSector = new GNIGenSector(this);
 		mGeneratorSubSector = new GNIGenSubSector(this);
		mGeneratorMainWorld = new GNIGenMainWorld(this);
		mGeneratorPassengers = super.getGeneratorPassengers();
		mGeneratorCargo = super.getGeneratorCargo();
		initializeData();
	}

    public void resetData()
    {
    	initialize();
    }
	
	UniverseBean	mUniverse;
    
    private void initializeData()
    {
		mUniverse = loadUniverse();
    }
	
	private UniverseBean loadUniverse()
	{
		UniverseBean universe = new UniverseBean();
		universe.setName("GENII");
		List<SectorBean> sectors = new ArrayList<SectorBean>();
		for (int x = mXLow; x < mXHigh; x++)
			for (int y = mYLow;  y < mYHigh; y++)
			{
				String resourceName = Y_LABELS.substring(y, y+1) + X_LABELS.substring(x, x+1);
				InputStream is = ResourceUtils.loadSystemResourceStream(RESOURCE_PATH+resourceName + ".GNI", GNIGenScheme.class);
				if (is == null)
				{
					//System.out.println("**** Can't load "+RESOURCE_PATH+resourceName);
					continue;
				}
				OrdBean ords = new OrdBean((X_ORIGIN + x)*32, (Y_ORIGIN + y)*40, 0);
				SectorBean sec = loadSector(is, ords, resourceName);
				sectors.add(sec);
			}
		universe.setSectors(sectors.toArray(new SectorBean[0]));
		return universe;
	}
	
	private SectorBean loadSector(InputStream is, OrdBean ords, String resourceName)
	{
		//System.out.print("[");
		SectorBean sec = new SectorBean();
        sec.setOID(getXYZSeed(ords, 0));
		sec.setUpperBound(new OrdBean(ords));
		sec.setLowerBound(new OrdBean(ords.getX() + 32, ords.getY() + 40, 0));
		SubSectorBean[][] subs = new SubSectorBean[4][4];
		List<List<List<MainWorldBean>>> mws = new ArrayList<List<List<MainWorldBean>>>();
		for (int x = 0; x < 4; x++)
		{
		    mws.add(new ArrayList<List<MainWorldBean>>());
			for (int y = 0; y < 4; y++)
			{
				subs[x][y] = new SubSectorBean();
				subs[x][y].setUpperBound(new OrdBean(ords.getX() + 8*x, ords.getY() + 10*y, 0));
				subs[x][y].setLowerBound(new OrdBean(ords.getX() + 8*x + 8, ords.getY() + 10*y + 10, 0));
                subs[x][y].setOID(getXYZSeed(subs[x][y].getUpperBound(), 0));
				mws.get(x).add(new ArrayList<MainWorldBean>());
			}
		}
		try
		{
			String inbuf;
			BufferedReader rdr = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
			sec.setName(readPrefixLine(rdr, "Sector:"));
			//System.out.print(sec.getName());
			readPrefixLine(rdr, "Alt:");
			readPrefixLine(rdr, "DOS:");
			readPrefixLine(rdr, "Coord:");
			rdr.readLine();
			subs[0][0].setName(readPrefixLine(rdr, "A:"));
			subs[1][0].setName(readPrefixLine(rdr, "B:"));
			subs[2][0].setName(readPrefixLine(rdr, "C:"));
			subs[3][0].setName(readPrefixLine(rdr, "D:"));
			subs[0][1].setName(readPrefixLine(rdr, "E:"));
			subs[1][1].setName(readPrefixLine(rdr, "F:"));
			subs[2][1].setName(readPrefixLine(rdr, "G:"));
			subs[3][1].setName(readPrefixLine(rdr, "H:"));
			subs[0][2].setName(readPrefixLine(rdr, "I:"));
			subs[1][2].setName(readPrefixLine(rdr, "J:"));
			subs[2][2].setName(readPrefixLine(rdr, "K:"));
			subs[3][2].setName(readPrefixLine(rdr, "L:"));
			subs[0][3].setName(readPrefixLine(rdr, "M:"));
			subs[1][3].setName(readPrefixLine(rdr, "N:"));
			subs[2][3].setName(readPrefixLine(rdr, "O:"));
			subs[3][3].setName(readPrefixLine(rdr, "P:"));
			rdr.readLine();
			// skip data definitions
			do
			{
				inbuf = rdr.readLine();
			} while ((inbuf != null) && (inbuf.trim().length() > 0));
			// read worlds
			for (;;)
			{
				inbuf = rdr.readLine();
				if (inbuf == null)
					break;
				inbuf = inbuf.trim();
				if (inbuf.length() == 0)
					continue;
				MainWorldBean mw = new MainWorldBean();
				int x = Integer.parseInt(inbuf.substring(19, 21)) - 1;
				int y = Integer.parseInt(inbuf.substring(21, 23)) - 1;
				OrdBean o = new OrdBean(ords.getX() + x, ords.getY() + y, 0);
				mw.setOrds(o);
				MainWorldLogic.importLine(mw, inbuf);
				mws.get(x/8).get(y/10).add(mw);
                mw.setLocalSeed(getXYZSeed(o, 0));
                mw.setOID(mw.getLocalSeed());
                mw.setSubSeed(subs[x/8][y/10].getOID());
                mw.setSecSeed(sec.getOID());
                if (mw.getStars().length == 0)
                {
                    RandBean r = new RandBean();
                    r.setSeed(mw.getLocalSeed());
                    ImpGenMainWorld.generateStars(mw, r, 0);
                }
			}
			rdr.close();
            SubSectorBean[] subList = new SubSectorBean[16];
            for (int x = 0; x < 4; x++)
                for (int y = 0; y < 4; y++)
                {
                    MainWorldBean[] mwList = mws.get(x).get(y).toArray(new MainWorldBean[0]);
                    subs[x][y].setMainWorlds(mwList);
                    subList[x + y*4] = subs[x][y];
                    loadSubSector(subList[x + y*4], x + y*4, resourceName);
                }
            sec.setSubSectors(subList);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//System.out.println("]");
		return sec;
	}
	
	private void loadSubSector(SubSectorBean sub, int off, String resourceName) throws IOException
    {
        char chOff = (char)('A' + off);
        InputStream is = ResourceUtils.loadSystemResourceStream(RESOURCE_PATH+resourceName +"_" + chOff + ".SUB", GNIGenScheme.class);
        if (is == null)
        {
            //System.out.println("**** Can't load "+RESOURCE_PATH+resourceName);
            return;
        }
        BufferedReader rdr = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
        List<LinkBean> links = new ArrayList<LinkBean>();
        for (;;)
        {
            String inbuf = rdr.readLine();
            if (inbuf == null)
                break;
            char type = inbuf.charAt(0);
            StringTokenizer st = new StringTokenizer(inbuf.substring(1), " \r\n");
            if (st.countTokens() < 4)
                continue;
            String hexFrom = st.nextToken();
            String hexTo = st.nextToken();
            int dx = Integer.parseInt(st.nextToken());
            int dy = Integer.parseInt(st.nextToken());
            int fromX = (IntegerUtils.parseInt(hexFrom.substring(0, 2)) - 1)%8;
            int fromY = (IntegerUtils.parseInt(hexFrom.substring(2, 4)) - 1)%10;
            int toX = (IntegerUtils.parseInt(hexTo.substring(0, 2)) - 1)%8;
            int toY = (IntegerUtils.parseInt(hexTo.substring(2, 4)) - 1)%10;
            OrdBean ordFrom = new OrdBean(sub.getUpperBound().getX() + fromX, sub.getUpperBound().getY() + fromY, 0);
            OrdBean ordTo = new OrdBean(sub.getUpperBound().getX() + toX + dx*8, sub.getUpperBound().getY() + toY + dy*10, 0);
            LinkBean link = new LinkBean();
            link.setOrigin(ordFrom);
            link.setDestination(ordTo);
            link.setType(type);
            link.setText("");
            links.add(link);
        }
        rdr.close();
        LinkBean[] l = new LinkBean[links.size()];
        links.toArray(l);
        sub.setLinks(l);
    }

    private String readPrefixLine(BufferedReader rdr, String prefix) throws IOException
	{
		String inbuf = rdr.readLine();
		if (inbuf == null)
			throw new IOException("Badly formatted GENII file, unexpected end of file looking for "+prefix);
		inbuf = inbuf.trim();
		if (!inbuf.startsWith(prefix))
			throw new IOException("Badly formatted GENII file, looking for "+prefix);
		return inbuf.substring(prefix.length()).trim();
	}

    // languages

    public static void main(String[] argv)
    {
		GNIGenScheme gen;
    	long tick0 = System.currentTimeMillis();
        gen = GNIGenScheme.newInstanceSpinward();
        gen.toString();
		long tick1 = System.currentTimeMillis();
		gen = null;
		System.gc();
		long tick2 = System.currentTimeMillis();
		gen = GNIGenScheme.newInstanceKnownWorld();
		long tick3 = System.currentTimeMillis();
		gen = null;
		System.gc();
		long tick4 = System.currentTimeMillis();
		gen = GNIGenScheme.newInstanceEverything();
		long tick5 = System.currentTimeMillis();
		System.out.println("Time for everything: "+(tick5-tick4));
		System.out.println("Time for known world: "+(tick3-tick2));
		System.out.println("Time for spinward: "+(tick1-tick0));
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
                System.out.println("    "+worlds[j].getExportLine());
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