package ttg.logic.war;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.ttg.beans.OrdBean;
import jo.util.utils.DebugUtils;
import ttg.beans.war.GameInst;
import ttg.beans.war.Ship;
import ttg.beans.war.ShipInst;
import ttg.beans.war.SideInst;
import ttg.beans.war.WorldInst;
import ttg.logic.war.eval.EvalLogic;

public class ShipLogic
{
	public static final int FUEL_ALL = 0;
	public static final int FUEL_ONE = 1;
	public static final int FUEL_TENTH = 2;
	
    public static void place(GameInst game, ShipInst ship)
    {
    	WorldInst to = ship.getDestination();
		if (to == null)
			return;

		WorldInst from = ship.getLocation();
		if (from == to)
			return;
        if (from != null)
        	from.getShips().remove(ship);

		to.getShips().add(ship);
		to.getShipsEnRoute().remove(ship);
		ship.setLocation(to);
		ship.setDestination(null);
		ship.setHasMoved(true);
		for (ShipInst s : ship.getContains())
			place(game, s);
    }

	public static void setDestination(GameInst game, ShipInst ship, WorldInst world)
	{
		if ((ship.getContainedBy() != null) && (ship.getContainedBy().getDestination() != world))
			return;
		WorldInst current = ship.getDestination();
		if (current != null)
			current.getShipsEnRoute().remove(ship);
		ship.setDestination(world);
		if (world != null)
			world.getShipsEnRoute().add(ship);
		for (ShipInst s : ship.getContains())
			setDestination(game, s, world);
	}
	
	public static String validateMove(GameInst game, ShipInst ship, WorldInst destination)
	{
		if (ship == null)
			return "No ship selected";
		if (destination == null)
			return null; // OK
		// check distance
		WorldInst location = ship.getLocation();
		OrdBean oLocation = location.getOrds();
		OrdBean oDestination = destination.getOrds();
		double dist = game.getScheme().distance(oLocation, oDestination);
		if (dist > getJump(ship))
			return "Ship lacks range";
		return null; 
	}
	
	public static void useFuel(GameInst game, ShipInst ship)
	{
		if (ship == null)
			return;
		WorldInst destination = ship.getDestination();
		if (destination == null)
			return; // OK
		// calc distance
		WorldInst location = ship.getLocation();
		OrdBean oLocation = location.getOrds();
		OrdBean oDestination = destination.getOrds();
		int dist = (int)game.getScheme().distance(oLocation, oDestination);
		ship.setFuel(ship.getFuel() - dist*fuelForJump1(ship));
	}
	
	public static String validateMove(GameInst game, ShipInst ship)
	{
		return validateMove(game, ship, ship.getDestination()); 
	}
	
	public static List<WorldInst> validDestinations(GameInst game, ShipInst ship)
	{
		return validDestinations(game, ship, getJump(ship));
	}
	
	public static List<WorldInst> validExtendedDestinations(GameInst game, ShipInst ship)
	{
		//DebugLogic.debug("ship="+ship.getShip().getName()+", range="+getRange(ship));
		return validDestinations(game, ship, getRange(ship));
	}
	
	private static List<WorldInst> validDestinations(GameInst game, ShipInst ship, long radius)
	{
		WorldInst loc = ship.getLocation();
		OrdBean location = loc.getOrds();
		List<WorldInst> ret = new ArrayList<>();
		if ((radius == 0) || (ship.getContainedBy() != null))
			return ret;
		for (long x = -radius; x <= radius; x++)
			for (long y = -radius; y <= radius; y++)
			{
				OrdBean ords = new OrdBean(location.getX() + x, location.getY() + y, 0);
				if (game.getScheme().distance(location, ords) > radius)
					continue;
				WorldInst world = WorldLogic.getWorld(game, ords);
				if (world == null)
					continue;
				ret.add(world);
			}
		return ret;
	}

	/**
	 * @param ship
	 * @return
	 */
	public static int getAttack(ShipInst ship)
	{
		if (ship.isDamaged())
			if (ship.getShip().getAttack() == 1)
				return 1;
			else
				return ship.getShip().getAttack()/2;
		else
			return ship.getShip().getAttack();
	}

	/**
	 * @param ship
	 * @return
	 */
	public static int getDefense(ShipInst ship)
	{
		if (ship.isDamaged())
			return ship.getShip().getDefense()/2;
		else
			return ship.getShip().getDefense();
	}

	/**
	 * @param ship
	 * @return
	 */
	public static int getJump(ShipInst ship)
	{
		int jump = ship.getShip().getJump();
		if (jump == 0)
			return 0;
		int fueled = ship.getFuel()/fuelForJump1(ship);
		return Math.min(jump, fueled);
	}

	/**
	 * @param ship
	 * @return
	 */
	public static int getRange(ShipInst ship)
	{
		int jump = ship.getShip().getJump();
		if (jump == 0)
			return 0;
		int jump1 = fuelForJump1(ship);
		if (jump1 == 0)
		{
			DebugUtils.debug("WTF?!? fuelForJump1("+ship.getShip().getName()+") = 0");
			return 0;
		}
		int fueled = ship.getFuel()/jump1;
		return fueled;
	}

	/**
	 * @param ship
	 */
	public static void damage(ShipInst ship)
	{
		ship.setDamaged(true);		
	}

	/**
	 * @param ship
	 */
	public static void destroy(GameInst game, ShipInst ship)
	{
		undock(ship);
		ship.getLocation().getShips().remove(ship);
		ship.getSideInst().getShips().remove(ship);
		game.getShips().remove(ship);
	}

    public static void repair(WorldInst world, ShipInst ship)
    {
		int port = world.getWorld().getPopulatedStats().getUPP().getPort().getValue();
		if ((port == 'A') || (port == 'B'))
	    	world.setRepairsThisTurn(world.getRepairsThisTurn() + 1);
	    else
			world.setRepairsThisGame(world.getRepairsThisGame() + 1);
		ship.setDamaged(false);
    }
    
	public static int fuelForJump1(ShipInst ship)
	{
		return fuelForJump1(ship.getShip());
	}
    
	public static int fuelForJump1(Ship ship)
	{
		return (int)Math.ceil(size(ship)*.1);
	}
    
	public static int size(ShipInst ship)
	{
		return size(ship.getShip());
	}
    
    public static int size(Ship ship)
    {
    	int esize = (ship.getAttack() + ship.getDefense())*100  + ship.getCapacity();
    	if (esize < 100)
    		esize = 100;
    	if (ship.getJump() == 0)
    		return esize;
    	double pc = .01 + .11*ship.getJump();
    	double jsize = (esize*pc)/(1-pc); 
    	return esize + (int)Math.ceil(jsize);
    }
    
	public static int capacity(Ship ship)
	{
		return ship.getCapacity();
	}
    
	public static int carrying(ShipInst ship)
	{
		int ret = 0;
		for (ShipInst s : ship.getContains())
			ret += size(s);
		return ret;
	}

    public static int additionalCapacity(ShipInst ship)
    {
    	return capacity(ship.getShip()) - carrying(ship);
    }
    
    public static int cost(Ship ship)
    {
    	if (ship == null)
    		return 0;
    	return size(ship) - capacity(ship);
    }
    
    public static void dock(ShipInst parent, ShipInst child)
    {
    	undock(child);
    	parent.getContains().add(child);
    	child.setContainedBy(parent);
		setupFuel(parent);
		setupFuel(child);
    }
    
	public static void undock(ShipInst child)
	{
		ShipInst parent = child.getContainedBy();
		if (parent != null)
		{
			parent.getContains().remove(child);
			setupFuel(parent);
		}
		child.setContainedBy(null);
		setupFuel(child);
	}

	/**
	 * @param mShips
	 */
	public static void autoDock(List<ShipInst> ships, SideInst pov)
	{
		for (;;)
		{
			// find ship with biggest capacity
			ShipInst biggestCapacity = null;
			int biggestCapacityValue = 0;
			for (Iterator<ShipInst> i = ships.iterator(); i.hasNext(); )
			{
				ShipInst ship = i.next();
				if ((pov != null) && (ship.getSideInst() != pov))
				{
					i.remove();
					continue;
				}
				int cap = additionalCapacity(ship);
				if (cap == 0)
					continue;
				if ((biggestCapacity == null) || (cap > biggestCapacityValue))
				{
					biggestCapacity = ship;
					biggestCapacityValue = cap;
				}
			}
			if (biggestCapacity == null)
				break;
			//DebugLogic.debug("Biggest capacity is "+biggestCapacity+" at "+biggestCapacityValue);
			// find biggest ship that will fit
			ShipInst biggestSize = null;
			int biggestSizeValue = 0;
			for (ShipInst ship : ships)
			{
				if (ship.getContainedBy() != null)
					continue;
				if (ship.getShip().getJump() > 0)
					continue;
				int size = size(ship);
				if (size > biggestCapacityValue)
					continue;
				if ((biggestSize == null) || (size > biggestSizeValue))
				{
					biggestSize = ship;
					biggestSizeValue = size;
				}
			}
			if (biggestSize == null)
				break;
			//DebugLogic.debug("Biggest size is "+biggestSize+" at "+biggestSizeValue);
			dock(biggestCapacity, biggestSize);
		}
	}

	/**
	 * @param inst
	 * @param b
	 */
	public static void setToDoAll(ShipInst ship, boolean v)
	{
		ship.setToDo(v);
		for (ShipInst s : ship.getContains())
			setToDoAll(s, v);
	}
	
	public static int eval(ShipInst ship, String expr)
	{
		if ((expr == null) || (expr.length() == 0))
			return 0;
		Map<String,Double> vars = new HashMap<>();
		vars.put("Damaged", new Double(ship.isDamaged() ? 1 : 0));
		vars.put("Attack", new Double(ship.getShip().getAttack()));
		vars.put("Defense", new Double(ship.getShip().getDefense()));
		vars.put("Capacity", new Double(capacity(ship.getShip())));
		vars.put("Carrying", new Double(carrying(ship)));
		vars.put("AdditionalCapacity", new Double(additionalCapacity(ship)));
		vars.put("Size", new Double(size(ship)));
		vars.put("Jump", new Double(ship.getShip().getJump()));
		vars.put("Cost", new Double(cost(ship.getShip())));
		return EvalLogic.evaluateInteger(expr, vars);
	}

	/**
	 * @param mGame
	 * @param string
	 * @return
	 */
	public static ShipInst getShip(GameInst mGame, String id)
	{
		for (ShipInst ship : mGame.getShips())
		{
			if (ship.toString().equals(id))
				return ship;
		}
		return null;
	}
	
	public static int getAttackRecursive(ShipInst ship)
	{
		int ret = getAttack(ship);
		for (ShipInst s : ship.getContains())
			ret += getAttackRecursive(s);
		return ret;
	}
	
	public static int getDefenseRecursive(ShipInst ship)
	{
		int ret = getDefense(ship);
		for (ShipInst s : ship.getContains())
			ret += getDefenseRecursive(s);
		return ret;
	}
	
	public static int fuelTankage(ShipInst ship)
	{
		return ShipLogic.fuelForJump1(ship)*ship.getShip().getJump()
			+ ShipLogic.additionalCapacity(ship);
	}
	
	public static int fuelTankage(Ship ship)
	{
		return ShipLogic.fuelForJump1(ship)*ship.getJump()
			+ ship.getCapacity();
	}

    public static void setupFuel(ShipInst ship)
    {
		WorldInst world = ship.getLocation();
		if ((world != null) && (world.getSide() != ship.getSideInst()))
		{
			if (WorldLogic.isFuelDefended(world))
				return; // fuel points adequately defended
		}
		if ((world == null) || (world.getWorld() != null))
		{
			ship.setFuel(ShipLogic.fuelForJump1(ship)*ship.getShip().getJump()
				+ ShipLogic.additionalCapacity(ship));
		}
    }
    
	public static String getJumpDescription(ShipInst ship)
	{
		return getJumpDescription(ship.getShip(), ship.getFuel());
	}
    
    public static String getJumpDescription(Ship ship, int fuel)
    {
    	StringBuffer sb = new StringBuffer();
    	int rawJump = ship.getJump(); 
    	sb.append(rawJump);
    	if (rawJump > 0)
    	{
    		sb.append(" (");
    		int fuel1 = fuelForJump1(ship);
    		int effectiveJumps;
			if (fuel1 > 0)
				effectiveJumps = fuel/fuel1;
			else
				effectiveJumps = 0;
			while (effectiveJumps > rawJump)
			{
				sb.append(rawJump);
				sb.append("+");
				effectiveJumps -= rawJump;
			}
			sb.append(effectiveJumps);
    		sb.append(")");
    	}
    	return sb.toString();
    }
    
    public static void transferFuel(ShipInst from, ShipInst to, int amnt)
    {
    	if (from.getLocation() != to.getLocation())
    		return;
    	int fromFuel = from.getFuel();
    	int toJump1 = fuelForJump1(to);
		int toFuel = to.getFuel();
		int toTankage = fuelTankage(to);
		int desired = 0;
		if (fromFuel == 0)
			return;
		if (toTankage == 0)
			return;
		switch (amnt)
		{
			case FUEL_ALL:
				desired = toTankage - toFuel;
				break;
			case FUEL_ONE:
				desired = 1;
				break;
			case FUEL_TENTH:
				desired = toJump1 - (toFuel%toJump1);
				break;
		}
		if (desired > toTankage - toFuel)
			desired = toTankage - toFuel;
		if (desired > fromFuel)
			desired = fromFuel;
		from.setFuel(from.getFuel() - desired);
		to.setFuel(to.getFuel() + desired);
    }

	/**
	 * @param ship
	 */
	public static void scrap(GameInst game, ShipInst ship)
	{
		SideInst side = ship.getSideInst();
		undock(ship);
		SideLogic.addResources(side, cost(ship.getShip()));
		if (ship.getLocation() != null)
			ship.getLocation().getShips().remove(ship);
		if (ship.getDestination() != null)
			ship.getDestination().getShipsEnRoute().remove(ship);
		side.getShips().remove(ship);
		game.getShips().remove(ship);
		SideLogic.victoryPoints(side, -ShipLogic.eval(ship, game.getGame().getVPHaveShip()));
	}

	/**
	 * @param ship
	 */
	public static ShipInst buy(GameInst game, SideInst side, Ship design)
	{
		Ship ship = new Ship();
		copy(design, ship);
		ShipInst shipInst = new ShipInst();
		shipInst.setShip(ship);
		side.getShips().add(shipInst);
		shipInst.setSideInst(side);
		game.getShips().add(shipInst);
		SideLogic.victoryPoints(side, ShipLogic.eval(shipInst, game.getGame().getVPHaveShip()));
		ShipLogic.setupFuel(shipInst);
		return shipInst;
	}

    public static void copy(Ship from, Ship to)
    {
		to.setName(from.getName());
        to.setAttack(from.getAttack());
		to.setDefense(from.getDefense());
		to.setCapacity(from.getCapacity());
		to.setJump(from.getJump());
		to.setSide(from.getSide());
    }

	public static boolean compare(Ship from, Ship to)
	{
		if (to.getAttack() != from.getAttack())
			return false;
		if (to.getDefense() != from.getDefense())
			return false;
		if (to.getCapacity() != from.getCapacity())
			return false;
		if (to.getJump() != from.getJump())
			return false;
		return true;
	}
	
	public static void addUnique(List<Ship> uniqueShips, Ship design)
	{
		for (Ship unique : uniqueShips)
		{
			if (ShipLogic.compare(design, unique))
			{
				design = null;
				break;
			}
		}
		if (design != null)
		{
			Ship newDesign = new Ship();
			ShipLogic.copy(design, newDesign);
			uniqueShips.add(newDesign);
		}
	}

    public static boolean isSDB(Ship ship)
    {
    	if (ship.getJump() > 0)
        	return false;
        return ship.getDefense() > ship.getAttack();
    }

	public static boolean isFighter(Ship ship)
	{
		if (ship.getJump() > 0)
			return false;
		return ship.getDefense() <= ship.getAttack();
	}

	public static boolean isCarrier(Ship ship)
	{
		if (ship.getJump() == 0)
			return false;
		if (ship.getCapacity() == 0)
			return false;
		return (ship.getCapacity()%100) == 0;
	}

	public static boolean isStarship(Ship ship)
	{
		if (ship.getJump() == 0)
			return false;
		return (ship.getCapacity()%fuelForJump1(ship)) == 0;
	}

    public static Ship techDown(WorldInst world, Ship design)
    {
    	Ship ship = new Ship();
    	copy(design, ship);
    	ship.setJump(Math.min(design.getJump(), WorldLogic.getMaxJumpConstruction(world)));
        return ship;
    }
    
    public static boolean canBeRepaired(ShipInst ship, WorldInst world)
    {
		if (ship.isHasFired())
			return false;
		if (ship.isHasMoved())
			return false;
		if (!ship.isDamaged())
			return false;
		if (ship.getSideInst() != world.getSide())
			return false;
		return true;
    }
}
