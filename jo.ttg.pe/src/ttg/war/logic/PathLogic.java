/*
 * Created on Apr 18, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.war.logic;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.logic.OrdLogic;
import ttg.war.beans.GameInst;
import ttg.war.beans.WorldInst;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PathLogic
{
	public static List<WorldInst> findPath(GameInst game, WorldInst from, WorldInst to, int jump, int fuel, int fueltank)
	{
//		DebugLogic.beginGroup("path from="+WorldLogic.getName(from)+" to="+WorldLogic.getName(to)
//			+" jump="+jump+" fuel="+fuel);
		List<HexPoint> open = new ArrayList<>();
		List<HexPoint> closed = new ArrayList<>();
		HexPoint start = new HexPoint();
		start.ords = from.getOrds();
		start.world = from.getWorld() != null;
		start.g = 0;
		start.fuel = fuel;
		calc(game, start, to.getOrds());
		start.p = null;
		calc(game, start, to.getOrds());        
		open.add(start); 
		HexPoint best = plotPath(game, open, closed, to.getOrds(), jump, fueltank);
		// reverse
		List<WorldInst> ret = new ArrayList<>();
		for (HexPoint p = best; p != null; p = p.p)
		{
			ret.add(0, WorldLogic.getWorld(game, p.ords));
		}
//		if (DebugLogic.debug)
//		{
//			StringBuffer msg = new StringBuffer("path= ");
//			for (Iterator i = ret.iterator(); i.hasNext(); )
//			{
//				WorldInst world = (WorldInst)i.next();
//				msg.append(" ");
//				msg.append(WorldLogic.getName(world));
//			}
//			DebugLogic.debug(msg.toString());
//		}
//		DebugLogic.endGroup("path");
		return ret;
	}
    
	private static HexPoint plotPath(GameInst game, List<HexPoint> open, List<HexPoint> closed, OrdBean dest, int jump, int fuel)
	{                
		for (;;)
		{                
			HexPoint n = findLowestF(open);
			if (n == null)
				return null;
			if (n.h == 0)
				return n;
			open.remove(n);
			closed.add(n);
			// neighbors
			List<WorldInst> within = WorldLogic.hexesWithin(game, n.ords, jump);
			boolean present;
			for (WorldInst w : within)
			{
				HexPoint nn = new HexPoint();
				nn.ords = w.getOrds();
				nn.world = (w.getWorld() != null);
				nn.p = n;
				double dist = game.getScheme().distance(nn.ords, n.ords);
				if (nn.world)
					nn.fuel = fuel;
				else
				{
					nn.fuel = n.fuel - (int)Math.ceil(dist); 
					if (nn.fuel == 0)
						continue;
				}
				nn.g = n.g + dist;
				calc(game, nn, dest);
				// closed
				present = false;
				for (HexPoint p : closed)
				{
					if (p.equals(nn))
					{      
						present = true;
						if (nn.g < p.g)
						{
							closed.remove(p);
							open.add(nn);
						}
						break;
					}
				}
				if (present)
					continue;
				// open
				present = false;
				for (HexPoint p : open)
				{
					if (p.equals(nn))
					{      
						present = true;
						if (nn.g < p.g)
						{
							open.remove(p);
							open.add(nn);
						}
						break;
					}
				}
				if (present)
					continue;
				open.add(nn);                                                                            
				for (HexPoint p = nn; p != null; p = p.p)
				{                                 
					if (p.p == p)
					{
						System.out.println("LOOP!");
						break;
					}        
				}
			}                      
		}        
	}            
	    
	/**
	 * @param n
	 * @param jump
	 * @param fuel
	 * @return
	 */
//	private static int calcFuelNeeded(GameInst game, HexPoint n)
//	{
//		int needed = 0;
//		for (HexPoint p = n; p.p != null; p = p.p)
//		{
//			needed += (int)game.getScheme().distance(p.ords, p.p.ords);
//			if (p.p.world)
//				break;
//		}
//		return needed;
//	}

	private static void calc(GameInst game, HexPoint p, OrdBean dest)
	{
		p.h = game.getScheme().distance(p.ords, dest)*10;
		p.g++;
		p.f = p.g + p.h;
	}

    
	private static HexPoint findLowestF(List<HexPoint> l)
	{                                         
		HexPoint best = null;
        
		for (HexPoint p : l)
		{
			if (best == null)
				 best = p;
			else if (p.f < best.f)
				best = p;            
		}                
		return best;
	} 
}

class HexPoint
{
	public OrdBean	ords;
	public  boolean	world;
	public int fuel;
	public double  f;
	public double  g;
	public double  h;
	public HexPoint p;
        
	public boolean equals(Object o)
	{
		if (o instanceof HexPoint)
		{                
			HexPoint pp = (HexPoint)o;
			return pp.ords.equals(ords);
		}
		else
			return false;
	} 
    
	public String toString()
	{
		if (p == null)
			return OrdLogic.getShortNum(ords);
		else
			return p.toString()+"."+OrdLogic.getShortNum(ords);
	}
}
