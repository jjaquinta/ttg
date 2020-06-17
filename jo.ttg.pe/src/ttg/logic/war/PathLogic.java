/*
 * Created on Apr 18, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.logic.war;

import java.util.ArrayList;
import java.util.Iterator;

import jo.ttg.beans.OrdBean;
import jo.ttg.logic.OrdLogic;
import ttg.beans.war.GameInst;
import ttg.beans.war.WorldInst;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PathLogic
{
	public static ArrayList findPath(GameInst game, WorldInst from, WorldInst to, int jump, int fuel, int fueltank)
	{
//		DebugLogic.beginGroup("path from="+WorldLogic.getName(from)+" to="+WorldLogic.getName(to)
//			+" jump="+jump+" fuel="+fuel);
		ArrayList open = new ArrayList();
		ArrayList closed = new ArrayList();
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
		ArrayList ret = new ArrayList();
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
    
	private static HexPoint plotPath(GameInst game, ArrayList open, ArrayList closed, OrdBean dest, int jump, int fuel)
	{                
		for (int tick = 0;; tick++)
		{                
			HexPoint n = findLowestF(open);
			if (n == null)
				return null;
			if (n.h == 0)
				return n;
			open.remove(n);
			closed.add(n);
			// neighbors
			ArrayList within = WorldLogic.hexesWithin(game, n.ords, jump);
			boolean present;
			for (Iterator i = within.iterator(); i.hasNext(); )
			{
				WorldInst w = (WorldInst)i.next();
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
				for (Iterator j = closed.iterator(); j.hasNext(); )
				{
					HexPoint p = (HexPoint)j.next();
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
				for (Iterator j = open.iterator(); j.hasNext(); )
				{
					HexPoint p = (HexPoint)j.next();
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

    
	private static HexPoint findLowestF(ArrayList l)
	{                                         
		HexPoint best = null;
        
		for (Iterator i = l.iterator(); i.hasNext(); )
		{
			HexPoint p = (HexPoint)i.next();
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
