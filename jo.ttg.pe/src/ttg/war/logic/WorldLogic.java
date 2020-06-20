package ttg.war.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.mw.UPPBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.logic.OrdLogic;
import ttg.war.beans.GameInst;
import ttg.war.beans.ShipInst;
import ttg.war.beans.SideInst;
import ttg.war.beans.WorldInst;
import ttg.war.logic.eval.EvalLogic;

public class WorldLogic
{
	public static WorldInst getWorld(GameInst mGame, Object obj)
	{
		if (obj == null)
			return null;
		if (obj instanceof MainWorldBean)
			return (WorldInst)mGame.getWorlds().get(((MainWorldBean)obj).getOrds().toString());
		else if (obj instanceof OrdBean)
			return (WorldInst)mGame.getWorlds().get(((OrdBean)obj).toString());
		else
			return null;
	}

    public static WorldInst getWorld(GameInst mGame, MainWorldBean mw)
    {
    	if (mw == null)
    		return null;
        return getWorld(mGame, mw.getOrds());
    }

	public static WorldInst getWorld(GameInst mGame, OrdBean ords)
	{
		if (ords == null)
			return null;
		WorldInst ret = (WorldInst)mGame.getWorlds().get(ords.toString());
//		if (ret == null)
//		{
//			System.out.println("*** Can't find world for ords: "+ords);
//		}
		return ret;
	}

	public static int canRepair(WorldInst world)
	{
		MainWorldBean mw = world.getWorld();
		if (mw == null)
			return 0;
		int port = mw.getPopulatedStats().getUPP().getPort().getValue();
		if (port == 'A')
			return 2 - world.getRepairsThisTurn();
		if (port == 'B')
			return 1 - world.getRepairsThisTurn();
		if (port == 'C')
			return ((world.getRepairsThisTurn() < 1) && (world.getRepairsThisGame() < 2))
				? 1 : 0;
		if (port == 'D')
			return ((world.getRepairsThisTurn() < 1) && (world.getRepairsThisGame() < 1))
				? 1 : 0;
		return 0;
	}
	
	public static boolean isWitness(WorldInst world, SideInst side)
	{
		if (world.getSide() == side)
			return true;
		for (ShipInst ship : world.getShips())
		{
			if (ship.getSideInst() == side)
				return true;
		}
		return false;
	}
	
	public static int eval(WorldInst world, String expr)
	{
		if ((expr == null) || (expr.length() == 0))
			return 0;
		Map<String,Double> vars = new HashMap<>();
		MainWorldBean mw = world.getWorld();
		if (mw == null)
			return 0;
		vars.put("NumGiants", new Double(mw.getNumGiants()));
		vars.put("NumBelts", new Double(mw.getNumBelts()));
		PopulatedStatsBean ps = mw.getPopulatedStats();
		vars.put("RedZone", new Double((ps.getTravelZone() == 'R') ? 1 : 0));
		vars.put("AmberZone", new Double((ps.getTravelZone() == 'A') ? 1 : 0));
		vars.put("GreenZone", new Double((ps.getTravelZone() == 'G') ? 1 : 0));
		vars.put("NavalBase", new Double(ps.isNavalBase() ? 1 : 0));
		vars.put("ScoutBase", new Double(ps.isScoutBase() ? 1 : 0));
		UPPBean upp = ps.getUPP();
		vars.put("Port", new Double(Math.max('F' - upp.getPort().getValue(), 0)));
		vars.put("Size", new Double(upp.getSize().getValue()));
		vars.put("Atmos", new Double(upp.getAtmos().getValue()));
		vars.put("Hydro", new Double(upp.getHydro().getValue()));
		vars.put("Pop", new Double(upp.getPop().getValue()));
		vars.put("Gov", new Double(upp.getGov().getValue()));
		vars.put("Law", new Double(upp.getLaw().getValue()));
		vars.put("Tech", new Double(upp.getTech().getValue()));
		int ret = EvalLogic.evaluateInteger(expr, vars);
		return ret;
	}
	
	public static List<ShipInst> getVisibleShips(GameInst game, WorldInst world, SideInst pov)
	{ 
		List<ShipInst> ships = new ArrayList<>();
		if (game.getGame().isAllowOmniscentSensors()
			|| isWitness(world, pov))
		{
			for (ShipInst ship : world.getShips())
			{
				if (ship.getSideInst() != pov)
					ships.add(ship); 
				else if (ship.getDestination() == null)
					ships.add(ship);
			}
		}
		for (ShipInst ship : world.getShipsEnRoute())
		{
			if (ship.getSideInst() == pov)
				ships.add(ship);
		}
		return ships;
	}

    public static int getIntrinsicDefense(WorldInst world)
    {
    	MainWorldBean mw = world.getWorld();
        if (mw == null)
        	return 0;
        return mw.getPopulatedStats().getUPP().getPop().getValue();
    }
    
    public static int getHaveWorldValue(GameInst game, WorldInst world)
    {
    	if (!game.getGame().isAllowConvertNeutral() && (world.getSide() == null))
    		return 0;
    	return eval(world, game.getGame().getVPHaveWorld());
    }
    
	public static int getResourceGeneration(GameInst game, WorldInst world)
	{
		return eval(world, game.getGame().getResourceGenerationFormula());
	}
    
    public static void addResourceProduction(GameInst game, WorldInst world)
    {
        SideInst side = world.getSide();
        if (side == null)
        	return;
        int prod = eval(world, game.getGame().getResourceGenerationFormula());
        SideLogic.addResources(side, prod);
    }

    public static String getName(WorldInst inst)
    {
    	if (inst == null)
    		return "nowhere";
    	if (inst.getWorld() == null)
    		return OrdLogic.getShortNum(inst.getOrds());
        return inst.getWorld().getName();
    }

	/**
	 * @param mGame
	 * @param world
	 * @return
	 */
	public static int getConstructionPerTurn(GameInst game, WorldInst world)
	{
		return eval(world, game.getGame().getConstructionPerTurnFormula());
	}
	
	public static int getMaxJumpConstruction(WorldInst world)
	{
		int port = world.getWorld().getPopulatedStats().getUPP().getPort().getValue();
		int maxJump;
		if (port != 'A')
			maxJump = 0;
		else
		{
			maxJump = SideLogic.getMaxTech(world.getSide()) - 9;
			if (maxJump > 6)
				maxJump = 6;
			else if (maxJump < 0)
				maxJump = 0;
		}
		return maxJump;
	}

	/**
	 * @param mWorld
	 * @return
	 */
	public static String getPopulationDesc(WorldInst world)
	{
		if ((world == null) || (world.getWorld() == null))
			return "0";
		return world.getWorld().getPopulatedStats().getUPP().getPop().getValue()
					+" ("
					+FormatUtils.sPopulation(world.getWorld().getPopulatedStats().getUPP().getPop().getPopulation())
					+")";
	}

	/**
	 * @param mWorld
	 * @return
	 */
	public static String getFuelDesc(WorldInst world)
	{
		if ((world == null) || (world.getWorld() == null))
			return "none";
		StringBuffer ret = new StringBuffer();
		int gas = world.getWorld().getNumGiants();
		if (world.getWorld().getPopulatedStats().getUPP().getHydro().getValue() > 0)
		{
			ret.append("1x Water World");
		}
		if (gas > 0)
		{
			if (ret.length() > 0)
				ret.append(", ");
			ret.append(gas);
			ret.append("x Gas Giant");
			if (gas > 1)
				ret.append("s");
		}
		return ret.toString();
	}

	/**
	 * @param mWorld
	 * @return
	 */
	public static String getRepairsDesc(WorldInst world)
	{
		if ((world == null) || (world.getWorld() == null))
			return "-";
		MainWorldBean mw = world.getWorld();
		int port = mw.getPopulatedStats().getUPP().getPort().getValue();
		if (port == 'A')
			return "2 per turn";
		else if (port == 'B')
			return "1 per turn";
		else if (port == 'C')
			return "2 per game ("+(2 - world.getRepairsThisGame())+" left)";
		else if (port == 'D')
			return "2 per game ("+(1 - world.getRepairsThisGame())+" left)";
		else
			return "-";
	}

	/**
	 * @return
	 */
	public static int getFuelPoints(WorldInst world)
	{
		if (world == null)
			return 0;
		MainWorldBean mw = world.getWorld();
		if (mw == null)
			return 0;
		int ret = mw.getNumGiants();
		if (mw.getPopulatedStats().getUPP().getHydro().getValue() > 0)
			ret++;
		return ret;
	}

	/**
	 * @param world
	 * @return
	 */
	public static int getDefenseFactors(WorldInst world)
	{
		int ret = 0;
		for (ShipInst ship : world.getShips())
		{
			if (ship.getSideInst() == world.getSide())
				ret += ShipLogic.getDefense(ship);
		}
		return ret;
	}

	/**
	 * @param world
	 * @return
	 */
	public static int getDefenseFactors(GameInst game, WorldInst world, SideInst pov)
	{
		int ret = 0;
		for (ShipInst ship : getVisibleShips(game, world, pov))
		{
			if (ship.getSideInst() == world.getSide())
				ret += ShipLogic.getDefense(ship);
		}
		return ret;
	}
	
	public static boolean isFuelDefended(WorldInst world)
	{
		return WorldLogic.getDefenseFactors(world) >= WorldLogic.getFuelPoints(world)*4;
	}
	
	public static boolean isFuelDefended(GameInst game, WorldInst world, SideInst pov)
	{
		return WorldLogic.getDefenseFactors(game, world, pov) >= WorldLogic.getFuelPoints(world)*4;
	}
	
	public static List<WorldInst> hexesWithin(GameInst game, OrdBean location, int radius)
	{
		List<WorldInst> ret = new ArrayList<>();
		if (radius == 0)
			return ret;
		for (long x = -radius; x <= radius; x++)
			for (long y = -radius; y <= radius; y++)
			{
				OrdBean ords = new OrdBean(location.getX() + x, location.getY() + y, 0);
				if (game.getScheme().distance(location, ords) > radius)
					continue;
				WorldInst world = WorldLogic.getWorld(game, ords);
				if (world != null)
					ret.add(world);
			}
		return ret;
	}
}
