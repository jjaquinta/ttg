package jo.ttg.gen.imp;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.beans.trade.GoodBean;
import jo.ttg.beans.uni.UniverseBean;
import jo.ttg.gen.IGenCargo;
import jo.ttg.gen.IGenLanguage;
import jo.ttg.gen.IGenMainWorld;
import jo.ttg.gen.IGenPassengers;
import jo.ttg.gen.IGenScheme;
import jo.ttg.gen.IGenSector;
import jo.ttg.gen.IGenSubSector;
import jo.ttg.gen.IGenSurface;
import jo.ttg.gen.IGenSystem;
import jo.ttg.gen.IGenUniverse;
import jo.ttg.logic.RandLogic;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.ttg.logic.uni.UniverseLogic;
import jo.util.utils.FormatUtils;

public class ImpGenScheme implements IGenScheme
{
    static public final int R_LOCAL     = 0;
    static public final int R_SUBSECTOR = 1;
    static public final int R_SECTOR    = 2;
    static public final int R_UNIVERSE  = 3;
    
    protected OrdBean				mSectorSize;
    protected OrdBean               mSubSectorSize;
    protected IGenUniverse	mGeneratorUniverse;
    protected IGenSector   mGeneratorSector;
    protected IGenSubSector mGeneratorSubSector;
    protected IGenMainWorld mGeneratorMainWorld;
    protected IGenSystem mGeneratorSystem;
    protected IGenSurface mGeneratorSurface;
    protected IGenPassengers mGeneratorPassengers;
    protected IGenCargo mGeneratorCargo;
    protected IGenLanguage mGeneratorLanguage;
    
    // flags
    protected boolean   mExtendedSystemGeneration = false;
    
    public ImpGenScheme()
    {
        mSectorSize = new OrdBean(32, 40, 0);
        mSectorSize = new OrdBean(8, 10, 1);
        mGeneratorUniverse = new ImpGenUniverse(this);
        mGeneratorSector = new ImpGenSector(this);
        mGeneratorSubSector = new ImpGenSubSector(this);
        mGeneratorMainWorld = new ImpGenMainWorld(this);
        mGeneratorSystem = new ImpGenSystemEx(this);
        mGeneratorSurface = new ImpGenSurface(this);
        mGeneratorPassengers = new ImpGenPassengers(this);
        mGeneratorCargo = new ImpGenCargo(this);
        mGeneratorLanguage = new ImpGenLanguage(this);
    }

    public OrdBean getSectorSize()
    {
        return mSectorSize;
    }

    public IGenUniverse getGeneratorUniverse()
    {
        return mGeneratorUniverse;
    }

    public IGenSector getGeneratorSector()
    {
        return mGeneratorSector;
    }

    public IGenSubSector getGeneratorSubSector()
    {
        return mGeneratorSubSector;
    }

    public IGenMainWorld getGeneratorMainWorld()
    {
        return mGeneratorMainWorld;
    }

    public IGenSystem    getGeneratorSystem()
    {
        IGenSystem ret = mGeneratorSystem;
        return ret;
    }

    public IGenSurface    getGeneratorSurface()
    {
        IGenSurface ret = mGeneratorSurface;
        return ret;
    }

    public IGenPassengers getGeneratorPassengers()
    {
        return mGeneratorPassengers;
    }

    public IGenCargo getGeneratorCargo()
    {
        return mGeneratorCargo;
    }

    public IGenLanguage getGeneratorLanguage()
    {
        return mGeneratorLanguage;
    }

    // utils

    public long getXYZSeed(OrdBean ords, int r)
    {
        long ltmp;

		ords = new OrdBean(ords);
        if (r == R_SUBSECTOR)
        {
            nearestSub(ords);
        }
        else if (r == R_SECTOR)
        {
            nearestSec(ords);
        }
        ltmp = make_seed(ords.getX(), ords.getY());
        ltmp += r;
        return ltmp;
    }

    public long getXYZSeed(MainWorldBean mw)
    {
        return getXYZSeed(mw.getOrds(), R_LOCAL);
    }

    private long make_seed(long x, long y)
    {
        // hash bits together
        long ret;
        ret = 0L;
        ret |= ((x & 0x00000001L) << 0);
        ret |= ((y & 0x00000001L) << 1);
        ret |= ((x & 0x00000002L) << 1);
        ret |= ((y & 0x00000002L) << 2);
        ret |= ((x & 0x00000004L) << 2);
        ret |= ((y & 0x00000004L) << 3);
        ret |= ((x & 0x00000008L) << 3);
        ret |= ((y & 0x00000008L) << 4);
        ret |= ((x & 0x00000010L) << 4);
        ret |= ((y & 0x00000010L) << 5);
        ret |= ((x & 0x00000020L) << 5);
        ret |= ((y & 0x00000020L) << 6);
        ret |= ((x & 0x00000040L) << 6);
        ret |= ((y & 0x00000040L) << 7);
        ret |= ((x & 0x00000080L) << 7);
        ret |= ((y & 0x00000080L) << 8);
        ret |= ((x & 0x00000100L) << 8);
        ret |= ((y & 0x00000100L) << 9);
        ret |= ((x & 0x00000200L) << 9);
        ret |= ((y & 0x00000200L) << 10);
        ret |= ((x & 0x00000400L) << 10);
        ret |= ((y & 0x00000400L) << 11);
        ret |= ((x & 0x00000800L) << 11);
        ret |= ((y & 0x00000800L) << 12);
        ret |= ((x & 0x00001000L) << 12);
        ret |= ((y & 0x00001000L) << 13);
        ret |= ((x & 0x00002000L) << 13);
        ret |= ((y & 0x00002000L) << 14);
        ret |= ((x & 0x00004000L) << 14);
        ret |= ((y & 0x00004000L) << 15);
        ret |= ((x & 0x00008000L) << 15);
        ret |= ((y & 0x00008000L) << 16);
        return ret;
    }
    int getMaturity(OrdBean ords)
    {
        int m;
        long dx, dy;

        dx = ords.getX() / 32 / 2;
        if (dx < 0)
            dx = -dx;
        dy = ords.getY() / 40 / 2;
        if (dy < 0)
            dy = -dy;
        if (dy > dx)
            dx = dy;
        if (dx > 4L)
            m = 4;
        else
            m = (int) (4L - dx);
        return m;
    }
    protected int getDensity(OrdBean ords)
    {
        double x1 = (double) ords.getX() / (32.0 * 8.0) * Math.PI;
        double x2 = (double) ords.getX() / (32.0 * 4.0) * Math.PI;
        double x3 = (double) ords.getX() / (32.0 * 2.0) * Math.PI;
        double y1 = (double) ords.getY() / (40.0 * 8.0) * Math.PI;
        double y2 = (double) ords.getY() / (40.0 * 4.0) * Math.PI;
        double y3 = (double) ords.getY() / (40.0 * 2.0) * Math.PI;
        double tot =
            Math.sin(x1)
                + Math.sin(x2)
                + Math.sin(x3)
                + Math.cos(y1)
                + Math.cos(y2)
                + Math.cos(y3);
        tot = (tot / 12.0) / 2 + 0.25;
        return (int) (tot * 100);
    }

    public boolean exists(OrdBean o)
    {
        RandBean r = new RandBean();
        int d;
        if (o.getZ() != 0L)
            return false; // two dimensional
        d = getDensity(o);
        RandLogic.setMagic(r, getXYZSeed(o, R_LOCAL), RandBean.EXIST_MAGIC);
        return (RandLogic.rand(r) % 100) < d;
    }

    private void nearest(OrdBean o, int x, int y, int z)
    {
        long ox = o.getX();
        if (ox >= 0)
            ox = ox/x;
        else
            ox = (ox + 1)/x - 1;
        ox *= x;
        o.setX(ox);
        long oy = o.getY();
        if (oy >= 0)
            oy = oy/y;
        else
            oy = (oy + 1)/y - 1;
        oy *= y;
        o.setY(oy);
        long oz = o.getZ();
        if (oz >= 0)
            oz = oz/z;
        else
            oz = (oz + 1)/z - 1;
        oz *= z;
        o.setZ(oz);
    }

    public void nearestSec(OrdBean o)
    {
        nearest(o, 32, 40, 1);
    }

    public void nearestSub(OrdBean o)
    {
        nearest(o, 8, 10, 1);
    }

    static List<GoodBean> mGoodsNatural;
    static List<GoodBean> mGoodsProcessed;
    static List<GoodBean> mGoodsManufactured;
    static List<GoodBean> mGoodsInformation;
    static List<GoodBean> mGoodsNovelties;
	static List<List<GoodBean>> mGoods;
	static Map<Integer,GoodList>	mGoodsCache = new HashMap<Integer,GoodList>();
	static int		mGoodsCacheTechLevel;
	static int		mGoodsCacheLawLevel;
	
	static private Integer makeGoodsCacheHash(int phylum, int type)
	{
		return new Integer(phylum*256+type);
	}

    static private List<GoodBean> getGoodsType(int type)
    {
		try
		{
			return mGoods.get(type);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return mGoodsNatural;
		}
    }

	static public GoodList getGoods(int phylum, int type, int tl, int law)
	{
		GoodList ret;
		if ((mGoodsCacheTechLevel == tl) && (mGoodsCacheLawLevel == law))
		{
			ret = mGoodsCache.get(makeGoodsCacheHash(phylum, type));
			if (ret != null)
				return ret;
		}
		else
		{
			mGoodsCacheTechLevel = tl;
			mGoodsCacheLawLevel = law;
			mGoodsCache.clear();
		}
		//System.out.println("Making phylum="+CargoBean.CP_DESCRIPTION[phylum]+", type="+CargoBean.GT_DESCRIPTION[type]+", tl="+tl+", law="+law);
		List<GoodBean>  goods = getGoodsType(type);
		ret = new GoodList();
		ret.setPhylum(phylum);
		ret.setType(type);
		ret.setTechLevel(tl);
		ret.setLawLevel(law);
		int total = 0;		
		for (GoodBean g : goods)
		{
			if ((g.getPhylum() == phylum) || (phylum == CargoBean.CP_DONTCARE))
			{ 
				if ((g.getTechLevel() <= tl) && (law < g.getLegality()))
				{
					ret.getGoods().add(g);
					total += g.getChance();
                    //System.out.println("  "+g.getName());
				}
			}
		}
		ret.setTotalChance(total);
		mGoodsCache.put(makeGoodsCacheHash(phylum, type), ret);
		return ret;
	}

    public static GoodBean findGood(GoodList goods, long rnd)
    {
    	if (goods.getTotalChance() == 0)
    		return null;
    	int idx = (int)(rnd%goods.getTotalChance());
    	for (GoodBean g : goods.getGoods())
    	{
    		idx -= g.getChance();
    		if (idx <= 0)
    			return g;
    	}
    	return null;
    }

    private static final String goodsDefaultNatural[] =
	{   //                         CorFlaExpRadPerFra
		"30;Ferrous Metal Ore    ;I;  ;  ;  ;  ;  ;  ;     300;14; 1",
		"20;Normal Ore           ;I;10;  ;12;  ;  ;  ;     100;15; 0",
		"20;Radioactive Ore      ;I;  ;  ;  ; 6;  ;  ;    1000;10; 6",
		"20;Nonferrous Ore       ;I;11;  ;11;11;  ;  ;    2000;14; 1",
		"30;Raw Crystals         ;I;  ;  ;  ;  ;  ;  ;  750000;14; 0",
		"10;Raw Precious Gems    ;I;  ;  ;  ;  ;  ;  ; 1000000;12; 0",
		"20;Nitrogen Compounds   ;I;10;  ; 9;  ;  ;  ;    4000;12; 4",
		"30;Raw Hydrocarbons     ;I;11; 9;11;  ;  ;  ;    5000;13; 5",
		"20;Plants (wood)        ;O;11; 9;  ;  ;  ;11;    1000;15; 0",
		"10;Plants (bales)       ;O;11; 9;  ;  ;10;  ;    1000;15; 0",
		"10;Plants (fibers)      ;O;11; 6;  ;  ; 9;  ;    2000;14; 0",
		"10;Plants (herbs)       ;O;11; 6;  ;12; 9;  ;    3000;13; 0",
		"80;Grain                ;O;  ; 8;  ;  ; 9;  ;     300;15; 0",
		"20;Fruit                ;O;  ; 8;  ;  ; 9;  ;    1000;14; 0",
		"10;Wild Plants (living) ;O;11; 6;12;  ;11;  ;    4000;13; 0",
		"40;Food Plants (living) ;O;11;11;  ;  ; 1;  ;    4000;13; 0",
		"20;Animals (living)     ;O;11;  ;  ;  ; 1;  ;    8000;12; 0",
		"40;Livestock (living)   ;O;11;  ;  ;  ; 1;  ;    8000;12; 0",
		"10;Rare Plants (living) ;O;11;11;  ;  ; 1;  ;   16000;13; 0",
		"10;Rare Animals (living);O;11;  ;  ;  ; 1;  ;   20000;13; 0"
	};
    private static final String goodsDefaultProcessed[] =
	{   //                   CorFlaExpRadPerFra
		"60;Composites     ;I;11; 9;10;  ;  ;  ;    4000;14; 5",
		"40;Special Alloys ;I;12;10;  ;  ;  ;  ;  200000;14; 6",
		"80;Steel          ;I;  ;  ;  ;12;  ;  ;     500;15; 3",
		"60;Copper         ;I;  ;  ;  ;11;  ;  ;    2000;15; 1",
		"60;Aluminum       ;I;  ;  ;  ;  ;  ;  ;    1000;15; 7",
		"40;Tin            ;I;  ;  ;  ;  ;  ;  ;    9000;15; 0",
		"20;Precious Metals;I;11;  ;  ;  ;  ;  ;   70000;12; 0",
		"10;Crystals       ;I;11;  ;  ;  ;  ;  ;   20000;12; 1",
		"30;Radioactives   ;I;  ;  ;  ; 5;  ;  ; 1000000;10; 6",
		"10;Rare Earths    ;I;11;12;12;12;  ;  ;    5000;10; 6",
		"10;Isotopes       ;I;  ;  ;  ; 3;  ;  ;    8000; 9; 6",
		"60;Foodstuffs     ;O;11; 9;12;  ; 9;  ;    1000;15; 0",
		"40;Meat           ;O;  ;  ;  ;  ; 7;  ;    1500;14; 0",
		"40;Petrochemicals ;I;10; 7; 8;  ;  ;  ;   10000;13; 6",
		"10;Textiles       ;O;  ; 9;  ;  ;11;  ;    3000;15; 0",
		"10;Explosives     ; ;12;10; 3;  ;10;  ;    8000;10; 3",
		"30;Polymers       ; ;  ; 9;  ;  ;  ;  ;    7000;14; 5",
		"30;Fertilisers    ;O;10; 9; 9;  ; 9;  ;    6000;12; 2"
	};
    private static final String goodsDefaultManufactured[] =
	{   //                         CorFlaExpRadPerFra
		"30;Pharmaceuticals      ; ;11;  ;  ;  ; 9;10;  100000;12; 5",
		"20;Spice                ;O;  ;  ;  ;  ;10;11;    6000;13; 0",
		"10;Gourmet Food         ;O;  ;  ;  ;  ;10;11;   10000;13; 0",
		"30;Alcoholic Beverage   ;O;11;  ;  ;  ; 9; 8;   10000;12; 0",
		"20;Nonalcoholic Beverage;O;  ;  ;  ;  ; 8;10;     500;15; 0",
		"10;Consumable Teas      ;O;  ;  ;  ;  ;12;11;    6000;14; 0",
		"20;Exotic Fluids        ; ;  ;  ;  ;  ; 9; 8;    8000;13; 0",
		"40;Aromatics            ; ;  ; 9;  ;  ;11;10;    4000;14; 1",
		"20;Clothing             ; ;  ;  ;  ;  ;  ;12;    1000;15; 0",
		"10;Protective Gear      ; ;  ;  ;  ;  ;  ; 9;    4000;14; 5",
		"10;Vacc Suits           ; ;  ;  ;  ;  ;  ; 9;  400000;14; 7",
		"10;Body Armour          ; ;  ;  ;  ;  ;  ; 9;   50000;10;11",
		"30;Weapons              ; ;  ;  ;  ;  ;  ; 9;   30000; 7; 5",
		"40;Ammunition           ; ;  ;  ;  ;  ;  ; 9;   30000; 8; 5",
		"20;Blades               ; ;  ;  ;  ;  ;  ; 9;   10000; 9; 2",
		"20;Farm Machinery       ; ;  ;  ;  ;  ;  ;10;  150000;15; 4",
		"20;Electronic Parts     ; ;  ;  ;  ;  ;  ;10;  100000;13; 7",
		"40;Mechanical Parts     ; ;  ;  ;  ;  ;  ;10;   70000;14; 4",
		"10;Cybernetic Parts     ; ;  ;  ;  ;  ;  ; 8;  250000;11;13",
		"10;Computer Parts       ; ;  ;  ;  ;  ;  ; 8;  150000;12; 7",
		"20;Tools                ; ;  ;  ;  ;  ;  ;11;   10000;15; 4",
		"20;Machine Tools        ; ;  ;  ;  ;  ;  ;11;  750000;14; 6",
		"10;Aircraft             ; ;  ;  ;  ;  ;  ;12; 1000000;13; 5",
		"10;Air/raft             ; ;  ;  ;  ;  ;  ;12; 6000000;12;10",
		"10;All Terrain Vehicles ; ;  ;  ;  ;  ;  ;12; 3000000;12; 6",
		"10;Armoured Vehicles    ; ;  ;  ;  ;  ;  ;12; 7000000;10; 6",
		"10;Vehicles             ; ;  ;  ;  ;  ;  ;12;   50000;14; 3",
		"30;Entertainment Equip  ; ;  ;  ;  ;  ;  ;10;   20000;13; 4",
		"10;Computers            ; ;  ;  ;  ;  ;  ;11;10000000;12; 7",
		"20;Robots               ; ;  ;  ;  ;  ;  ;11; 7500000;10;10"
    };
    private static final String goodsDefaultInformation[] =
	{   //                                CorFlaExpRadPerFra
		"20;Writings (paper)            ; ;  ;  ;  ;  ;  ;  ; 1000;15; 1",
		"20;2-D Still Pictures          ; ;  ;  ;  ;  ;  ;  ; 2000;14; 3",
		"20;Computer Software           ; ;  ;  ;  ;  ;  ;  ;10000;12; 8",
		"20;Robotic Software            ; ;  ;  ;  ;  ;  ;  ;40000;11;10",
		"20;Starship Software           ; ;  ;  ;  ;  ;  ;  ;20000;13; 8",
		"20;3-D Still Pictures          ; ;  ;  ;  ;  ;  ;  ; 3000;13; 8",
		"30;Artistic Images             ; ;  ;  ;  ;  ;  ;  ; 4000;12; 0",
		"10;Audio Recordings            ; ;  ;  ;  ;  ;  ;  ; 2000;15; 5",
		"10;2-D Video Recordings        ; ;  ;  ;  ;  ;  ;  ; 3000;14; 6",
		"10;3-D Video Recordings        ; ;  ;  ;  ;  ;  ;  ; 4000;13; 9",
		"10;Raw Data (paper)            ; ;  ;  ;  ;  ;  ;  ; 2000;15; 1",
		"20;Raw Data (data)             ; ;  ;  ;  ;  ;  ;  ; 4000;14; 7",
		"20;Raw Data (inanimate samples);I; 9;  ;11;11;  ;10; 6000;14; 0",
		"40;Raw Data (biosamples)       ;O;11; 1;  ;  ; 1;  ; 8000;12; 0",
		"30;Records (paper)             ; ;  ;  ;  ;  ;  ;  ; 2000;15; 1",
		"60;Records (data)              ; ;  ;  ;  ;  ;  ;  ; 4000;14; 7"
	};
    private static final String goodsDefaultNovelties[] =
	{   //                                 CorFlaExpRadPerFra
		"10;New Natural Resources        ; ;10 ; ;11;10;12;  ; 4000;15; 0",
		"10;New Processed Resources      ; ;   ; ;11;  ;  ;  ; 4000;14; 1",
		"20;New Manufactured Goods       ; ;10 ; ;11;  ;  ;10; 4000;13; 2",
		"20;New Information              ; ;   ; ;  ;  ;  ; 1; 4000;12; 3",
		"60;Natural Curiosities          ; ;12 ; ;12;12;  ; 9; 4000;14; 0",
		"60;Handmade Artifacts           ; ;   ; ;  ;  ;  ; 6; 6000;15; 0",
		"60;Living Creatures             ;O;12 ; ;  ;  ; 1; 1;10000;12; 0",
		"60;Starving Artist Reproductions; ;   ; ;  ;  ;  ;  ;60000;14; 0",
		"60;Counterfeit Knock-Offs       ; ;   ; ;  ;  ;  ;  ; 4000;12; 0"
	};

    static
    {
        mGoodsNatural      = parseGoodsDefault(goodsDefaultNatural     );
        mGoodsProcessed    = parseGoodsDefault(goodsDefaultProcessed   );
        mGoodsManufactured = parseGoodsDefault(goodsDefaultManufactured);
        mGoodsInformation  = parseGoodsDefault(goodsDefaultInformation );
        mGoodsNovelties    = parseGoodsDefault(goodsDefaultNovelties   );
        mGoods = new ArrayList<List<GoodBean>>();
        mGoods.add(mGoodsNatural);
		mGoods.add(mGoodsProcessed);
		mGoods.add(mGoodsManufactured);
		mGoods.add(mGoodsInformation);
		mGoods.add(mGoodsNovelties);
    }

    private static List<GoodBean> parseGoodsDefault(String[] goods)
    {
		List<GoodBean> ret = new ArrayList<GoodBean>();
        for (int i = 0; i < goods.length; i++)
        {
            GoodBean gb = new GoodBean();
            StringTokenizer st = new StringTokenizer(goods[i], ";", true);
            gb.setChance(FormatUtils.parseInt(st.nextToken()));
            st.nextToken(); // skip delmiter
            gb.setName(st.nextToken().trim());
            st.nextToken(); // skip delmiter
            String phylum = st.nextToken();
            if (phylum.equalsIgnoreCase("O"))
                gb.setPhylum(CargoBean.CP_ORGANIC);
            else if (phylum.equalsIgnoreCase("I"))
                gb.setPhylum(CargoBean.CP_INORGANIC);
            else
                gb.setPhylum(CargoBean.CP_DONTCARE);
            st.nextToken(); // skip delmiter
            gb.setChanceCor(FormatUtils.parseInt(st.nextToken()));
            st.nextToken(); // skip delmiter
            gb.setChanceFla(FormatUtils.parseInt(st.nextToken()));
            st.nextToken(); // skip delmiter
            gb.setChanceExp(FormatUtils.parseInt(st.nextToken()));
            st.nextToken(); // skip delmiter
            gb.setChanceRad(FormatUtils.parseInt(st.nextToken()));
            st.nextToken(); // skip delmiter
            gb.setChancePer(FormatUtils.parseInt(st.nextToken()));
            st.nextToken(); // skip delmiter
            gb.setChanceFra(FormatUtils.parseInt(st.nextToken()));
			st.nextToken(); // skip delmiter
			gb.setBaseValue(FormatUtils.parseLong(st.nextToken()));
			st.nextToken(); // skip delmiter
			gb.setLegality(FormatUtils.parseInt(st.nextToken()));
			st.nextToken(); // skip delmiter
			gb.setTechLevel(FormatUtils.parseInt(st.nextToken()));
			ret.add(gb);
        }
        return ret;
    }
    
    public List<GoodBean> getAllGoods()
    {
        List<GoodBean> goods = new ArrayList<GoodBean>();
        for (List<GoodBean> l : mGoods)
            goods.addAll(l);
        return goods;
    }
    
    public void setAllGoods(Collection<GoodBean> goods)
    {
        for (List<GoodBean> l : mGoods)
            l.clear();
        for (GoodBean g : goods)
            mGoods.get(g.getType()).add(g);
        mGoodsCacheTechLevel = -1;
        mGoodsCacheLawLevel = -1;
        mGoodsCache.clear();
    }

    public static void main(String[] argv)
    {
        ImpGenScheme gen = new ImpGenScheme();
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
                System.out.println("    "+worlds[j].exportLine());
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
        System.out.println("Home:"+home.getExportLine());
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
        System.out.println("Home:"+home.getExportLine());
        System.out.println("Generating system.");
        GenSystem genSys = gen.getGeneratorSystem();
        SystemBean sys = genSys.generateSystem(ords);
        System.out.println("Generated system.");
        System.out.println(BeanUtil.toXML(sys, "system"));
        */
        /* test5 */
        UniverseBean uni = gen.getGeneratorUniverse().generateUniverse();
        List<MainWorldBean> mws = UniverseLogic.findMainWorlds(uni, new OrdBean(0,0,0), new OrdBean(32, 40, 1));
        for (MainWorldBean mw : mws)
        {
            System.out.println("World:"+MainWorldLogic.getExportLine(mw));
            System.out.println("Generating system.");
            IGenSystem genSys = gen.getGeneratorSystem();
            SystemBean sys = genSys.generateSystem(mw.getOrds());
            System.out.println("Generated system."+sys.getName());
        }
        /**/

    }

	/* (non-Javadoc)
	 * @see GenScheme#distance(ttg.beans.OrdBean, ttg.beans.OrdBean)
	 */
	public double distance(OrdBean o1, OrdBean o2)
	{
		int dist;

		OrdBean target = new OrdBean(o2);
		for (dist = 0; (o1.getX() != target.getX()) || (o1.getY() != target.getY()); dist++)
			moveToward(o1, target);
		return dist;
	}
	private void moveToward(OrdBean ords1, OrdBean ords2)
	{
		if (ords1.getX() == ords2.getX())
		{
			if (ords1.getY() > ords2.getY())
				ords2.setY(ords2.getY()+1);
			else if (ords1.getY() < ords2.getY())
				ords2.setY(ords2.getY()-1);
		}
		else if (ords1.getX() < ords2.getX())
		{
			ords2.setX(ords2.getX()-1);
			if ((ords2.getX()%2) != 0)
			{
				if (ords1.getY() < ords2.getY())
					ords2.setY(ords2.getY()-1);
			}
			else
			{
				if (ords1.getY() > ords2.getY())
					ords2.setY(ords2.getY()+1);
			}
		}
		else if (ords1.getX() > ords2.getX())
		{
			ords2.setX(ords2.getX()+1);
			if ((ords2.getX()%2) != 0)
			{
				if (ords1.getY() < ords2.getY())
					ords2.setY(ords2.getY()-1);
			}
			else
			{
				if (ords1.getY() > ords2.getY())
					ords2.setY(ords2.getY()+1);
			}
		}
	}

    public void setGeneratorCargo(IGenCargo cargo)
    {
        mGeneratorCargo = cargo;        
    }

    public void setGeneratorLanguage(IGenLanguage lang)
    {
        mGeneratorLanguage = lang;
    }

    public void setGeneratorMainWorld(IGenMainWorld mw)
    {
        mGeneratorMainWorld = mw;
    }

    public void setGeneratorPassengers(IGenPassengers pass)
    {
        mGeneratorPassengers = pass;
    }

    public void setGeneratorSector(IGenSector sec)
    {
        mGeneratorSector = sec;
    }

    public void setGeneratorSubSector(IGenSubSector sub)
    {
        mGeneratorSubSector = sub;
    }

    public void setGeneratorSurface(IGenSurface surf)
    {
        mGeneratorSurface = surf;
    }

    public void setGeneratorSystem(IGenSystem sys)
    {
        mGeneratorSystem = sys;
    }

    public void setGeneratorUniverse(IGenUniverse uni)
    {
        mGeneratorUniverse = uni;
    }

    public boolean isExtendedSystemGeneration()
    {
        return mExtendedSystemGeneration;
    }

    public void setExtendedSystemGeneration(boolean extendedSystemGeneration)
    {
        mExtendedSystemGeneration = extendedSystemGeneration;
    }

    public OrdBean getSubSectorSize()
    {
        return mSubSectorSize;
    }

    public void setSubSectorSize(OrdBean subSectorSize)
    {
        mSubSectorSize = subSectorSize;
    }
}