/*
 * Created on Jan 27, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.healpix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import jo.ttg.beans.RandBean;
import jo.ttg.beans.surf.MapHexBean;
import jo.ttg.logic.RandLogic;
import jo.util.heal.HEALLogic;
import jo.util.heal.IHEALCoord;
import jo.util.heal.IHEALGlobe;
import jo.util.utils.ArrayUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SelectLogic
{
    public static IHEALCoord getRandomCoord(IHEALGlobe<MapHexBean> globe, RandBean r)
    {
        return getRandomCoordIterator(globe, r).next();
    }
	public static Iterator<IHEALCoord> getRandomCoordIterator(IHEALGlobe<MapHexBean> globe, RandBean r)
	{
		List<IHEALCoord> list = new ArrayList<IHEALCoord>();
		for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
		{
		    IHEALCoord o = i.next();
			list.add(o);
		}
		Collections.shuffle(list, new Random(r.getSeed()));
		return list.iterator();
	}

	public static Iterator<MapHexBean> getRandomContentsIterator(IHEALGlobe<MapHexBean> globe, RandBean r)
	{
		List<MapHexBean> list = new ArrayList<MapHexBean>();
		for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
		    list.add(globe.get(i.next()));
		randomize(list, r);
		return list.iterator();
	}
	
    public static <T> void randomize(List<T> l, RandBean r)
	{
		for (int i = 0; i < l.size(); i++)
		{
		    int j = RandLogic.rand(r)%l.size();
			T io = l.get(i);
            T jo = l.get(j);
            l.remove(i);
            l.add(i, jo);
            l.remove(j);
            l.add(j, io);
		}
	}

	public static Iterator<IHEALCoord> getRandomNeighborIterator(IHEALCoord c, RandBean r)
	{
		List<IHEALCoord> list = new ArrayList<>();
		int size = 0;
		for (IHEALCoord o : HEALLogic.getNeighbors(c))
		{
		    if (size == 0)
				list.add(o);
			else
				list.add(RandLogic.rand(r)%size, o);
		}
		return list.iterator();
	}

	public static Iterator<MapHexBean> getNeighborIterator(IHEALGlobe<MapHexBean> globe, IHEALCoord c)
	{
		List<MapHexBean> list = new ArrayList<>();
		for (IHEALCoord o : HEALLogic.getNeighbors(c))
			list.add(globe.get(o));
		return list.iterator();
	}

	public static Iterator<MapHexBean> getNeighborIterator(IHEALGlobe<MapHexBean> globe, MapHexBean hex)
	{
		return getNeighborIterator(globe, hex.getLocation());
	}
	
	public static Collection<MapHexBean> findContiguous(IHEALGlobe<MapHexBean> globe, RandBean r, ContiguousStrategy strat)
	{
        List<MapHexBean> candidates = new ArrayList<MapHexBean>();
		for (Iterator<MapHexBean> i = getRandomContentsIterator(globe, r); i.hasNext(); )
		{
			MapHexBean hex = i.next();
			if (strat.candidateHex(globe, hex))
				candidates.add(hex);
		}
		randomize(candidates, r);
		//System.out.println("  after iterator seed="+r.getSeed());
		while (candidates.size() > 0)
		{
			MapHexBean hex = candidates.iterator().next();
			List<MapHexBean> set = new ArrayList<MapHexBean>();
			List<MapHexBean> nearby = new ArrayList<MapHexBean>();
			for (;;)
			{
				set.add(hex);
				if (set.size() >= strat.getNumber())
					return set;
				candidates.remove(hex);
				nearby.remove(hex);
				addNearby(globe, hex, nearby, candidates);
				if (nearby.size() == 0)
					break;
				hex = findBest(globe, set, nearby, r, strat);
		        //System.out.println("  after best seed="+r.getSeed());
				if (hex == null)
					break;
			}
		}
		return null;
	}
	
	private static void addNearby(IHEALGlobe<MapHexBean> globe, MapHexBean hex, List<MapHexBean> nearby, List<MapHexBean> candidates)
	{
		for (Iterator<?> i = getNeighborIterator(globe, hex); i.hasNext(); )
		{
			MapHexBean nHex = (MapHexBean)i.next();
			if (candidates.contains(nHex) && !nearby.contains(nHex))
				nearby.add(nHex);
		}
	}
	
	private static MapHexBean findBest(IHEALGlobe<MapHexBean> globe, List<MapHexBean> set, List<MapHexBean> nearby, RandBean rnd, ContiguousStrategy strat)
	{
		List<List<MapHexBean>> best = new ArrayList<List<MapHexBean>>();
		for (int i = 0; i < 8; i++)
			best.add(new ArrayList<MapHexBean>());
		for (MapHexBean hex : nearby)
		{
			int val = 0;
			for (Iterator<MapHexBean> j = getNeighborIterator(globe, hex); j.hasNext(); )
			{
				MapHexBean nHex = j.next();
				if (set.contains(nHex))
					val++;
			}
			best.get(best.size()-1-val).add(hex);
		}
		int loose = (int)(6*strat.getLoosness());
		for (int i = 1; (i <= loose) || (best.get(0).size() == 0); i++)
			best.get(0).addAll(best.get(i));
		return best.get(0).get(RandLogic.rand(rnd)%best.get(0).size());
	}

	public static IHEALCoord place(IHEALGlobe<MapHexBean> globe, RandBean r, int num, Function<MapHexBean,Boolean> matcher, Consumer<MapHexBean> op)
	{
		ContiguousStrategy strat = new ContiguousStrategy();
		strat.setNumber(num);
		strat.setMatcher(matcher);
		strat.setAltType(MapHexBean.TYPE_PLATE);
		strat.setStrategy(ContiguousStrategy.STRAT_UNDERLAYFIRST);
		Collection<MapHexBean> c = findContiguous(globe, r, strat);
		if (c == null)
		{
			strat.setStrategy(ContiguousStrategy.STRAT_NORM);
			c = findContiguous(globe, r, strat);
			if (c == null)
			{
				System.err.println("Can't place!");
				return null;
			}
		}
		ArrayUtils.opCollection(c, op);
		return findCenter(c);
	}

	public static Collection<MapHexBean> findAll(IHEALGlobe<MapHexBean> globe, Function<MapHexBean,Boolean> op)
	{
	    return HEALLogic.findAllContents(globe, op);
	}

    public static void setAll(IHEALGlobe<MapHexBean> globe, Function<MapHexBean,Boolean> match, Consumer<MapHexBean> op)
    {
        ArrayUtils.opCollection(findAll(globe, match), op);
    }
	
	public static boolean anyNeighborOf(IHEALGlobe<MapHexBean> globe, MapHexBean hex, Function<MapHexBean,Boolean> match)
	{
		for (Iterator<MapHexBean> j = SelectLogic.getNeighborIterator(globe, hex); j.hasNext(); )
		{
			MapHexBean next = j.next();
			if (match.apply(next))
			    return true;
		}
		return false;
	}
	
	public static int countNeighborsOf(IHEALGlobe<MapHexBean> globe, MapHexBean hex, Function<MapHexBean,Boolean> match)
	{
	    int ret = 0;
		for (Iterator<MapHexBean> j = SelectLogic.getNeighborIterator(globe, hex); j.hasNext(); )
		{
			MapHexBean next = j.next();
			if (match.apply(next))
			    ret++;
		}
		return ret;
	}
	
	public static IHEALCoord findCenter(Collection<MapHexBean> hexes)
	{
	    List<IHEALCoord> ords = new ArrayList<>();
	    for (MapHexBean hex : hexes)
	        ords.add(hex.getLocation());
	    return HEALLogic.findCenter(ords);
	}
}
