package jo.ttg.gen.imp;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.ttg.beans.RandBean;
import jo.ttg.beans.surf.MapHexBean;
import jo.ttg.beans.surf.SurfaceAnnotationBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.beans.sys.CityBean;
import jo.ttg.beans.sys.TemperatureBean;
import jo.ttg.gen.IGenSurface;
import jo.ttg.logic.LanguageLogic;
import jo.ttg.logic.RandLogic;
import jo.ttg.logic.healpix.ContiguousStrategy;
import jo.ttg.logic.healpix.SelectLogic;
import jo.util.heal.HEALLogic;
import jo.util.heal.IHEALCoord;
import jo.util.heal.IHEALGlobe;
import jo.util.utils.ArrayUtils;

public class ImpGenSurface implements IGenSurface
{
    ImpGenScheme   scheme;
    private Map<Thread, SurfaceBean> mSurfaceCache = new HashMap<>();
    private Map<Thread, SurfaceParameters> mParamsCache = new HashMap<>();

    public ImpGenSurface(ImpGenScheme _scheme)
    {
        scheme = _scheme;
    }

    public SurfaceBean generateSurface(BodyWorldBean world)
    {
        long localSeed = world.getSeed();
        RandBean r = new RandBean();
        RandLogic.setMagic(r, localSeed, RandBean.SURFACE_MAGIC);
        SurfaceParameters params = new SurfaceParameters();  
		params.setResolution(4);
		params.setTectonicPlates(world.getStatsHyd().getNumPlates());
		params.setWaterCoverage(world.getStatsHyd().getWaterPercent());
		params.setLife(world.getStatsAtm().isLife());
		params.setVolcanos(world.getStatsHyd().getNumVolcanos());
		TemperatureBean temps = new TemperatureBean(world);
		params.setTropical(temps.getTempLine(40.0));
		params.setTemperate(temps.getTempLine(20.0));
		params.setFrostLine(temps.getFrostLine());
		params.setPermafrostLine(temps.getPermafrostLine());
		params.setMajor(world.getStatsHyd().getMajor());
		params.setMinor(world.getStatsHyd().getMinor());
		params.setIslands(world.getStatsHyd().getIslands());
		params.setArchepeligos(world.getStatsHyd().getArchepeligos());
		params.setLanguage(LanguageLogic.getLoadedLanguage(world.getPopulatedStats().getAllegiance()));
		if (params.getLanguage() == null)
		    params.setLanguage(LanguageLogic.getLoadedLanguage("Im"));
        SurfaceBean surface = new SurfaceBean();
        surface.setURI("surface://"+world.getURI().substring(7));
        surface.setBody(world);
        mSurfaceCache.put(Thread.currentThread(), surface);
        mParamsCache.put(Thread.currentThread(), params);
        IHEALGlobe<MapHexBean> globe = generateSurface(r, params);
        surface.setGlobe(globe);
        mSurfaceCache.remove(Thread.currentThread());
        mParamsCache.remove(Thread.currentThread());
        return surface;
    }
    
    private SurfaceBean getSurface()
    {
        return mSurfaceCache.get(Thread.currentThread());
    }
    
    private SurfaceParameters getParams()
    {
        return mParamsCache.get(Thread.currentThread());
    }
    
    private void addAnnotation(IHEALCoord ord, int type, RandBean r)
    {
        if (ord == null)
            return;
        String name = LanguageLogic.getName(getParams().getLanguage(), r);
        addAnnotation(ord, type, r, name);
    }
    
    private void addAnnotation(IHEALCoord ord, int type, RandBean r, String name)
    {
        SurfaceAnnotationBean note = new SurfaceAnnotationBean(ord, type, name);
        getSurface().getAnnotations().add(note);
    }
    
	private IHEALGlobe<MapHexBean> generateSurface(RandBean rnd, SurfaceParameters params)
	{
	    IHEALGlobe<MapHexBean> globe;
	    if ("jo.util".equals(System.getProperty("jo.ttg.gen.imp.surface.globe")))
	        globe = new jo.util.heal.impl.HEALGlobe<MapHexBean>(params.getResolution());
	    else
	        globe = new jo.ttg.beans.surf.HEALGlobe<MapHexBean>(params.getResolution());
		for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
		{
			IHEALCoord c = i.next();
			MapHexBean hex = new MapHexBean();
			hex.setLocation(c);
			hex.setAltitude(RandLogic.nextInt(rnd, 1024)/1024.0);
			globe.set(c, hex);
		}
		generatePlates(globe, rnd, params);
		generatePlateMovement(globe, rnd, params);
		generateSeasAndLand(globe, rnd, params);
		generateCover(globe, rnd, params);
        generateAltitude(globe, rnd, params);
        generateCities(globe, rnd, params);
		return globe;
	}
	
	private  void generatePlates(IHEALGlobe<MapHexBean> globe, RandBean rnd, SurfaceParameters params)
	{
        if (params.getTectonicPlates() <= 1)
        {   // all one plate
            HEALLogic.opOnAll(globe, (hex) -> { hex.setPlate(1); hex.setPlateMove(MapHexBean.M_CONVERGING); });
            return;
        }
		ContiguousStrategy strat = new ContiguousStrategy();
		strat.setStrategy(ContiguousStrategy.STRAT_NORM);
		strat.setMatcher((hex) -> (hex.getPlate() == 0));
		strat.setLoosness(2.0/6.0);
		double[] plateSizes = new double[params.getTectonicPlates()];
		double tot = 0;
		for (int i = 0; i < plateSizes.length; i++)
		{
            double pc = (RandLogic.D(rnd, 2)*RandLogic.D(rnd, 1)*5.0)/500.0;
            tot += pc;
            plateSizes[i] = pc;
		}
		if (tot > 1)
		{
		    double mult = 1/tot;
	        for (int i = 0; i < plateSizes.length; i++)
	            plateSizes[i] *= mult;
		}
		for (int plate = 1; plate < params.getTectonicPlates(); plate++)
		{
			double pc = plateSizes[plate-1];
			//System.out.println("Rolling for plate "+plate+", seed="+rnd.getSeed());
			int size = (int)(globe.size()*pc);
			strat.setNumber(size);
			Collection<MapHexBean> plateHexes = SelectLogic.findContiguous(globe, rnd, strat);
            //System.out.println("  Found contiguous, seed="+rnd.getSeed());
			if (plateHexes == null)
			{
				System.err.println("Can't create plate #"+plate+" size "+size);
				continue;
			}
			final int thePlate = plate;
			ArrayUtils.opCollection(plateHexes, (hex) -> hex.setPlate(thePlate));
		}
	}
	
	private  void generatePlateMovement(IHEALGlobe<MapHexBean> globe, RandBean rnd, SurfaceParameters params)
	{
        if (params.getTectonicPlates() <= 1)
            return;
		int[][] dir = new int[params.getTectonicPlates()][params.getTectonicPlates()];
		for (int i = 0; i < dir.length; i++)
		    for (int j = 0; j < dir.length; j++)
		        if (i == j)
		            dir[i][j] = MapHexBean.M_NONE;
		        else if (i > j)
		            dir[i][j] = dir[j][i];
		        else
				{
					int roll = RandLogic.D(rnd, 2);
					if (roll <= 5)
						dir[i][j] = MapHexBean.M_CONVERGING;
					else if (roll >= 9)
						dir[i][j] = MapHexBean.M_DIVERGING;
					else
						dir[i][j] = MapHexBean.M_TRANSVERSING;
				}
		for (Iterator<MapHexBean> i = globe.iterator(); i.hasNext(); )
		{
			MapHexBean hex = i.next();
			int thisPlate = hex.getPlate();
			int thatPlate = thisPlate;
			for (Iterator<MapHexBean> j = SelectLogic.getNeighborIterator(globe, hex); j.hasNext(); )
			{
			    MapHexBean nh = j.next();
				int p = nh.getPlate();
				if (p != thisPlate)
				{
				    thatPlate = p;
				    break;
				}
			}
			hex.setPlateMove(dir[thisPlate][thatPlate]);
		}
	}

	private  void generateSeasAndLand(IHEALGlobe<MapHexBean> globe, RandBean r, SurfaceParameters params)
	{
		int pc = params.getWaterCoverage();
		int vMaj, vMin, vIsl, vArch, vOther;
		if (pc < 50)
		{
			vMaj = MapHexBean.G_WATER;
			vMin = MapHexBean.G_WATER;
			vIsl = MapHexBean.G_WATER;
			vArch = MapHexBean.G_ISLANDS;
			vOther = MapHexBean.G_LAND;
		}
		else
		{
		    pc = 100 - pc;
			vMaj = MapHexBean.G_LAND;
			vMin = MapHexBean.G_LAND;
			vIsl = MapHexBean.G_LAND;
			vArch = MapHexBean.G_LAKES;
			vOther = MapHexBean.G_WATER;
		}
		generateSeasAndLand(globe, pc, r,
			params, vMaj, vMin, vIsl, vArch, vOther);
	}

	private  void    generateSeasAndLand(IHEALGlobe<MapHexBean> globe, int landpc, RandBean r,
				SurfaceParameters params,
				int vMaj, int vMin, int vIsl, int vArch,
				int vOther)
	{
		ArrayUtils.opCollection(globe.iterator(), (hex) -> hex.setElevation(0));
//System.out.println("Maj:"+String.valueOf(nMaj)+" Min:"+String.valueOf(nMin)+" Isl:"+String.valueOf(nIsl)+" Arch:"+String.valueOf(nArch));
		if ((params.getMajor()>0) || (params.getMinor()>0))
		{
			int tot = params.getMajor() + params.getMinor();
			int size[] = new int[tot];
			int land = (globe.size()*landpc)/100 - (params.getArchepeligos() + 1)/2 - params.getIslands();
			int totland = 0;
			for (int i = 0; i < tot; i++)
			{
				if (i < params.getMajor())
					size[i] = land*2/(params.getMinor() + 2*params.getMajor());
				else
					size[i] = land/(params.getMinor() + 2*params.getMajor());
				totland += size[i];
			}
			while (totland++ < land)
				size[RandLogic.rand(r)%tot]++;
			for (int i = 0; i < tot; i++)
			{
			    IHEALCoord center;
				if (i < params.getMajor())
				    center = SelectLogic.place(globe, r, size[i], (hex) -> (hex.getElevation() == 0), (hex) -> hex.setElevation(vMaj));
				else
				    center = SelectLogic.place(globe, r, size[i], (hex) -> (hex.getElevation() == 0), (hex) -> hex.setElevation(vMin));
				addAnnotation(center, (vMaj == MapHexBean.G_LAND) ? SurfaceAnnotationBean.CONTINENT : SurfaceAnnotationBean.OCEAN, 
				        r);
			}
		}
		for (int i = 0; i < params.getIslands(); i++)
		{
		    IHEALCoord center = SelectLogic.place(globe, r, 1, (hex) -> (hex.getElevation() == 0), (hex) -> hex.setElevation(vIsl));
            addAnnotation(center, (vMaj == MapHexBean.G_LAND) ? SurfaceAnnotationBean.ISLAND : SurfaceAnnotationBean.LAKE, 
                    r);
		}
		for (int i = 0; i < params.getArchepeligos(); i++)
		{
		    IHEALCoord center = SelectLogic.place(globe, r, 1, (hex) -> (hex.getElevation() == 0), (hex) -> hex.setElevation(vArch));
            addAnnotation(center, (vMaj == MapHexBean.G_LAND) ? SurfaceAnnotationBean.ISLANDS : SurfaceAnnotationBean.LAKES, 
                    r);
		}
		SelectLogic.setAll(globe, (hex) -> (hex.getElevation() == 0), (hex) -> hex.setElevation(vOther));
	}
	
//	private  void printElevation(HEALGlobe<MapHexBean> globe)
//	{
//		int j = 0;
//		for (Iterator i = globe.iterator(); i.hasNext(); j++)
//		{
//			MapHexBean hex = (MapHexBean)i.next();
//			System.out.print(hex.getElevation()+" ");
//			if (j%40 == 39)
//			    System.out.println();
//		}
//		System.out.println();
//	}

	private  void    generateCover(IHEALGlobe<MapHexBean> globe, RandBean r, SurfaceParameters params)
	{
		ArrayUtils.opCollection(globe.iterator(), (hex) -> hex.setCover(MapHexBean.C_UNSET));
		generateCoverWater(globe, r, params);
		generateCoverMoutains(globe, r, params);
		generateCoverTundra(globe, r, params);
		if (!params.isLife())
		{
			/* no life so set all to desert */
            SelectLogic.setAll(globe, (hex) -> (hex.getCover() ==  MapHexBean.C_UNSET), (hex) -> hex.setCover(MapHexBean.C_DESERT));
			return;
		}
		generateCoverDesert(globe, r, params);
		generateCoverWoods(globe, r, params);
	}

	private  void generateCoverWater(IHEALGlobe<MapHexBean> globe, RandBean r, SurfaceParameters params)
	{
		double sumfreeze, winfreeze;

		sumfreeze = params.getPermafrostLine();
		winfreeze = params.getFrostLine();
		List<MapHexBean> deep = new ArrayList<MapHexBean>();
		for (Iterator<MapHexBean> i = globe.iterator(); i.hasNext(); )
		{
			MapHexBean hex = i.next();
			if (hex.getElevation() == MapHexBean.G_ISLANDS)
			{
				hex.setCover(MapHexBean.C_ISLAND);
				continue;
			}
			if (hex.getElevation() != MapHexBean.G_WATER)
				continue;
			double latt =  Math.abs(HEALLogic.getLongLat(hex.getLocation())[1]);
			if (latt < winfreeze)
			{
				if (SelectLogic.anyNeighborOf(globe, hex, (h) -> (h.getElevation() == MapHexBean.G_LAND)))
					hex.setCover(MapHexBean.C_WATER);
				else if (hex.getPlateMove() == MapHexBean.M_CONVERGING)
					hex.setCover(MapHexBean.C_WATER);
				else
				{
					hex.setCover(MapHexBean.C_DEEP);
					deep.add(hex);
				}
			}
			else if (latt < sumfreeze)
				hex.setCover(MapHexBean.C_WICE);
			else
				hex.setCover(MapHexBean.C_SICE);
		}
		// extend shallow
		int quota = deep.size()/2;
		while ((quota > 0) && (deep.size() > 0))
		{
			MapHexBean hex = (MapHexBean)deep.get(RandLogic.rand(r)%deep.size());
            deep.remove(hex);
			if (hex.getCover() != MapHexBean.C_DEEP)
			    continue;
			if (RandLogic.D(r, 1) < SelectLogic.countNeighborsOf(globe, hex, (h) -> (h.getCover() == MapHexBean.C_WATER)))
			{
			    hex.setCover(MapHexBean.C_WATER);
			    quota--;
			}
		}
	}

	private  void generateCoverMoutains(IHEALGlobe<MapHexBean> globe, RandBean r, SurfaceParameters params)
	{
		/* sort out mountains */
		Collection<MapHexBean> landList = SelectLogic.findAll(globe, (hex) -> hex.getElevation() == MapHexBean.G_LAND);
		MapHexBean[] land = new MapHexBean[landList.size()];
		landList.toArray(land);
		RandLogic.randomize(land, r);
		int nland = land.length;
		int mntnpc = (RandLogic.D(r, 2) - 2)*RandLogic.D(r, 1);
		int len = (int)(((long)nland*(long)mntnpc)/100L);
		len += params.getVolcanos();
		//System.out.println(String.valueOf(mntnpc)+"% of "+String.valueOf(nland)+" landhexes = "+String.valueOf(len)+" mountains, "+params.getVolcanos()+" volcanos.");
		if (len <= 0)
			return;
		if (len > land.length)
		    len = land.length;
		if (params.getTectonicPlates() > 1)
	        generateMountainsTectonic(globe, r, params, land, len);
		else
            generateMountainsCraters(globe, r, params, land, len);
	}

    private void generateMountainsTectonic(IHEALGlobe<MapHexBean> globe, RandBean r,
            SurfaceParameters params, MapHexBean[] land, int len)
    {
        int[] viability = new int[land.length];
        for (int i = 0; i < land.length; i++)
        {
        	for (Iterator<MapHexBean> j = SelectLogic.getNeighborIterator(globe, land[i]); j.hasNext(); )
        	{
        		MapHexBean nHex = j.next();
        		switch (nHex.getPlateMove())
        		{
        			case MapHexBean.M_CONVERGING:
        				viability[i] += 2;
        				break;
        			case MapHexBean.M_TRANSVERSING:
        				viability[i] += 1;
        				break;
        			case MapHexBean.M_DIVERGING:
        				viability[i] -= 1;
        				break;
        		}
        	}
        }
        // sort the top #len
        for (int i = 0; i < len; i++)
        {
        	for (int j = i + 1; j < land.length; j++)
        	{
        		if (viability[i] < viability[j])
        		{
        			MapHexBean hTmp = land[i];
        			land[i] = land[j];
        			land[j] = hTmp;
        			int iTmp = viability[i];
        			viability[i] = viability[j];
        			viability[j] = iTmp;
        		}
        	}
        }
        for (int i = 0; i < len; i++)
        {
            if ((i < params.getVolcanos()) && !SelectLogic.anyNeighborOf(globe, land[i], (hex) -> (hex.getCover() == MapHexBean.C_VOLC)))
                land[i].setCover(MapHexBean.C_VOLC);
            else if ((RandLogic.rand(r)%3) == 0)
                land[i].setCover(MapHexBean.C_MTNS);
            else
                land[i].setCover(MapHexBean.C_ROUGH);
        }
    }

    private void generateMountainsCraters(IHEALGlobe<MapHexBean> globe, RandBean r,
            SurfaceParameters params, MapHexBean[] land, int len)
    {
        int volcs = params.getVolcanos();
        List<IHEALCoord> centers = new ArrayList<>();
        List<Integer> radii = new ArrayList<Integer>();
        int count = 0;
        Iterator<IHEALCoord> rnd = SelectLogic.getRandomCoordIterator(globe, r);
        while (count < len)
        {
            IHEALCoord center = rnd.next();
            int radius = RandLogic.rand(r)%globe.getResolution() + globe.getResolution()/2;
            int circ = (int)(2*Math.PI*radius);
            centers.add(center);
            radii.add(radius);
            count += circ;
            addAnnotation(center, SurfaceAnnotationBean.CRATER, r);
            //System.out.println("Crater @"+center+", r="+radius+" c="+circ);
        }
        // find on edge
        List<IHEALCoord> edges = new ArrayList<>();
        double mult = globe.getResolution()*globe.getResolution()*4*Math.sqrt(2)/Math.PI/2.0;
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
        {
            IHEALCoord c = i.next();
            for (int j = 0; j < centers.size(); j++)
            {
                int d = (int)(HEALLogic.getDistance(c, centers.get(j))*mult);
                if ((d == 0) || (d == radii.get(j)))
                {
                    edges.add(c);
                    break;
                }
            }
        }
        //System.out.println("Edges="+edges.size());
        while ((edges.size() > 0) && (len > 0))
        {
            int idx = RandLogic.nextInt(r, edges.size());
            IHEALCoord edge = edges.get(idx);
            edges.remove(idx);
            MapHexBean hex = (MapHexBean)globe.get(edge);
            if (hex.getElevation() != MapHexBean.G_LAND)
                continue;
            if (hex.getCover() != MapHexBean.C_UNSET)
                continue;
            int i = RandLogic.rand(r)%(len + volcs);
            if (i < volcs)
                hex.setCover(MapHexBean.C_VOLC);
            else if (i%3 == 0)
                hex.setCover(MapHexBean.C_MTNS);
            else
                hex.setCover(MapHexBean.C_ROUGH);
            len--;
        }
    }

	private  void generateCoverTundra(IHEALGlobe<MapHexBean> globe, RandBean r, SurfaceParameters params)
	{
		double sumfreeze = params.getPermafrostLine();
		// sort out tundra
		for (Iterator<MapHexBean> i = globe.iterator(); i.hasNext(); )
		{
			MapHexBean hex = i.next();
			if (hex.getElevation() == MapHexBean.G_LAKES)
			{
				hex.setCover(MapHexBean.C_LAKE);
				continue;
			}
			if ((hex.getElevation() != MapHexBean.G_LAND) || (hex.getCover() != MapHexBean.C_UNSET))
				continue;
			double latt = Math.abs(HEALLogic.getLongLat(hex.getLocation())[1]);
			if (latt >= sumfreeze)
				hex.setCover(MapHexBean.C_TUNDRA);
		}
	}

	private  void generateCoverDesert(IHEALGlobe<MapHexBean> globe, RandBean r, SurfaceParameters params)
	{
		if (RandLogic.D(r, 2)*10 >= params.getWaterCoverage())
		{
		    IHEALCoord center = SelectLogic.place(globe, r, RandLogic.D(r, 2) - 1, (hex) -> (hex.getElevation() == MapHexBean.G_LAND), (hex) -> hex.setCover(MapHexBean.C_DESERT));
            addAnnotation(center, SurfaceAnnotationBean.DESERT, r);
		}
	}

	/**
	 * returns how many tropical hexes exist
	 */
	private  void generateCoverWoods(IHEALGlobe<MapHexBean> globe, RandBean r, SurfaceParameters params)
	{
		/* set non-tropical to open */
		for (MapHexBean hex : SelectLogic.findAll(globe, (hex) -> (hex.getCover() == MapHexBean.C_UNSET)))
		{
			double latt = Math.abs(HEALLogic.getLongLat(hex.getLocation())[1]);
			if (latt < params.getTropical())
			{
				if (RandLogic.D(r, 1) <= 2)
					hex.setCover(MapHexBean.C_JUNGLE);
				else
					hex.setCover(MapHexBean.C_OPEN);
			}
			else if (latt < params.getTemperate())
			{
				if (RandLogic.D(r, 1) <= 2)
					hex.setCover(MapHexBean.C_FOREST);
				else
					hex.setCover(MapHexBean.C_OPEN);
			}
			else
				hex.setCover(MapHexBean.C_OPEN);
		}
	}
    
    private  void generateAltitude(IHEALGlobe<MapHexBean> globe, RandBean rnd, SurfaceParameters params)
    {
        generateAltitudeFromTerrain(globe);
        generateAltitudeFromPlates(globe);
    }

    private void generateAltitudeFromPlates(IHEALGlobe<MapHexBean> globe)
    {
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
        {
            IHEALCoord ords = i.next();
            MapHexBean hex = globe.get(ords);
            double delta = 0;
            if (hex.getPlateMove() == MapHexBean.M_CONVERGING)
                delta = 1.2;
            else if (hex.getPlateMove() == MapHexBean.M_DIVERGING)
                delta = 0.8;
            if (delta == 0)
                continue;
            int plate = hex.getPlate();
            hex.setAltitude(hex.getAltitude()*delta);
            for (IHEALCoord o : HEALLogic.getNeighbors(ords))
            {
                MapHexBean h = globe.get(o);
                if (h.getPlate() != plate)
                    continue;
                h.setAltitude(h.getAltitude()*delta);
            }
        }
    }

    private void generateAltitudeFromTerrain(IHEALGlobe<MapHexBean> globe)
    {
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
        {
            IHEALCoord ords = i.next();
            MapHexBean hex = globe.get(ords);
            switch (hex.getCover())
            {
                case MapHexBean.C_DESERT:
                case MapHexBean.C_FOREST:
                case MapHexBean.C_ISLAND:
                case MapHexBean.C_JUNGLE:
                case MapHexBean.C_OPEN:
                case MapHexBean.C_TUNDRA:
                    hex.setAltitude(hex.getAltitude()+1);
                    incrementNeighboringAltitude(globe, ords, 1, 1);
                    break;
                case MapHexBean.C_ROUGH:
                    hex.setAltitude(hex.getAltitude()+2);
                    incrementNeighboringAltitude(globe, ords, 2, 2);
                    break;
                case MapHexBean.C_VOLC:
                case MapHexBean.C_MTNS:
                    hex.setAltitude(hex.getAltitude()+3);
                    incrementNeighboringAltitude(globe, ords, 3, 3);
                    break;
                case MapHexBean.C_LAKE:
                case MapHexBean.C_WATER:
                case MapHexBean.C_SICE:
                case MapHexBean.C_WICE:
                    hex.setAltitude(hex.getAltitude()-1);
                    incrementNeighboringAltitude(globe, ords, 1, -1);
                    break;
                case MapHexBean.C_DEEP:
                    hex.setAltitude(hex.getAltitude()-2);
                    incrementNeighboringAltitude(globe, ords, 2, -2);
                    break;
            }
        }
    }

    private void incrementNeighboringAltitude(IHEALGlobe<MapHexBean> globe, IHEALCoord ords, int r, int delta)
    {
        for (IHEALCoord o : HEALLogic.getRadius(ords, r))
        {
            MapHexBean hex = globe.get(o);
            hex.setAltitude(hex.getAltitude()+delta);
        }
    }
    
    private void generateCities(IHEALGlobe<MapHexBean> globe, RandBean rnd, SurfaceParameters params)
    {
        BodyWorldBean world = (BodyWorldBean)getSurface().getBody();
        double totalPop = world.getStatsPop().getTotalPop();
        Iterator<IHEALCoord> ords = SelectLogic.getRandomCoordIterator(globe, rnd);
        for (CityBean city : world.getStatsPop().getCities())
        {
            int type;
            if (city.getPop()/totalPop > .1)
                type = SurfaceAnnotationBean.MEGACITY;
            else if (city.getPop()/totalPop > .01)
                type = SurfaceAnnotationBean.CITY;
            else if (city.getPop()/totalPop > .001)
                type = SurfaceAnnotationBean.TOWN;
            else
                type = SurfaceAnnotationBean.VILLAGE;
            int bestOf = 3;
            IHEALCoord best = null;
            int bestv = -1;
            while ((bestOf > 0) && ords.hasNext())
            {
                IHEALCoord o = ords.next();
                int suitability = getSuitability(globe.get(o), world, type);
                if (suitability < 0)
                    continue;
                bestOf--;
                if (suitability > bestv)
                {
                    bestv = suitability;
                    best = o;
                }
            }
            if (best != null)
                addAnnotation(best, type, rnd, city.getName());
        }
    }
    
    private int getSuitability(MapHexBean hex, BodyWorldBean world, int type)
    {
        switch (hex.getCover())
        {
            case MapHexBean.C_WATER:
            case MapHexBean.C_LAKE:
                if (world.getPopulatedStats().getUPP().getTech().getValue() < 11)
                    return -1;
                return 4;
            case MapHexBean.C_TUNDRA:
                if (world.getPopulatedStats().getUPP().getTech().getValue() < 10)
                    return -1;
                return 11;
            case MapHexBean.C_MTNS:
                if (world.getPopulatedStats().getUPP().getTech().getValue() < 8)
                    return -1;
                return 10;
            case MapHexBean.C_VOLC:
                if (world.getPopulatedStats().getUPP().getTech().getValue() < 4)
                    return -1;
                return 12;
            case MapHexBean.C_ROUGH:
                return 13;
            case MapHexBean.C_OPEN:
                return 20;
            case MapHexBean.C_DESERT:
                return 12;
            case MapHexBean.C_FOREST:
                return 15;
            case MapHexBean.C_JUNGLE:
                return 14;
            case MapHexBean.C_DEEP:
                if (world.getPopulatedStats().getUPP().getTech().getValue() < 14)
                    return -1;
                return 1;
            case MapHexBean.C_SICE:
                if (world.getPopulatedStats().getUPP().getTech().getValue() < 13)
                    return -1;
                return 2;
            case MapHexBean.C_WICE:
                if (world.getPopulatedStats().getUPP().getTech().getValue() < 12)
                    return -1;
                return 3;
            case MapHexBean.C_ISLAND:
                return 18;
            default:
                throw new IllegalArgumentException("Unknown surface type "+hex.getCover());
        }
    }
}