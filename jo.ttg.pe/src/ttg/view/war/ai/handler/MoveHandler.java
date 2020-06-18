/*
 * Created on Apr 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.war.ai.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jo.ttg.logic.OrdLogic;
import jo.util.utils.DebugUtils;
import ttg.beans.war.ShipInst;
import ttg.beans.war.WorldInst;
import ttg.logic.war.PathLogic;
import ttg.logic.war.ShipLogic;
import ttg.logic.war.WorldLogic;
import ttg.view.war.ai.ComputerPlayer;

/**
 * @author jgrant
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MoveHandler extends BaseHandler
{
    private WorldInst[]                    mHomeland;
    private int[]                          mHomelandValue;
    private WorldInst[]                    mBorder;
    private int[]                          mBorderValue;
    private List<ShipInst>                 mScoutShips;
    private List<ShipInst>                 mMovingShips;
    private List<ShipInst>                 mStationaryShips;
    private Map<WorldInst, Integer>        mLastKnownDefenseFactors;
    private Map<WorldInst, List<ShipInst>> mCanDeploy;
    private Set<WorldInst>                 mScoutedWorlds;
    private Map<WorldInst, Integer>        mUnderdefended;
    private Map<WorldInst, Integer>        mUnderattacked;
    private List<LongRangePlanner>         mLongRangePlanner;

    public MoveHandler(ComputerPlayer player)
    {
        super(player);
        mScoutShips = new ArrayList<>();
        mMovingShips = new ArrayList<>();
        mStationaryShips = new ArrayList<>();
        mLastKnownDefenseFactors = new HashMap<>();
        mCanDeploy = new HashMap<>();
        mScoutedWorlds = new HashSet<>();
        mUnderdefended = new HashMap<>();
        mUnderattacked = new HashMap<>();
        mLongRangePlanner = new ArrayList<>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ttg.beans.war.PlayerInterface#move()
     */
    public void move()
    {
        DebugUtils.beginGroup("move side='" + name(getSide()) + "'");
        moveSetup();
        dockFighters();
        findArena();
        allocateScouts();
        allocateDefense();
        allocateOffense();
        allocateLongRangePlanner();
        int todo = mMovingShips.size() + mUnderattacked.size()
                + mUnderdefended.size();
        while (mMovingShips.size() > 0)
        {
            allocateUnderdefended();
            allocateUnderattacked();
            int newTodo = mMovingShips.size() + mUnderattacked.size()
                    + mUnderdefended.size();
            if (todo == newTodo)
            {
                debug(mMovingShips.size() + " unallocated ships");
                break;
            }
            todo = newTodo;
        }
        DebugUtils.endGroup("move");
    }

    private void moveSetup()
    {
        mStationaryShips.clear();
        mMovingShips.clear();
        mScoutShips.clear();
        mCanDeploy.clear();
        mScoutedWorlds.clear();
        mUnderdefended.clear();
        mUnderattacked.clear();
    }

    private void findArena()
    {
        Set<WorldInst> homeland = new HashSet<>();
        Set<WorldInst> border = new HashSet<>();
        homeland.addAll(getSide().getWorlds());
        for (ShipInst ship : getSide().getShips())
        {
            List<WorldInst> worlds = ShipLogic
                    .validExtendedDestinations(getGame(), ship);
            if (!getGame().getGame().isAllowConvertNeutral())
                for (Iterator<WorldInst> j = worlds.iterator(); j.hasNext();)
                {
                    WorldInst world = j.next();
                    if (world.getSide() == null)
                        j.remove();
                }
            border.addAll(worlds);
            addDeployability(ship, worlds);
            if (ship.getContainedBy() == null)
            {
                if ((ship.getShip().getJump() > 0) && (ship.getFuel() > 0))
                {
                    if (isScout(ship))
                        mScoutShips.add(ship);
                    else
                        mMovingShips.add(ship);
                }
                else
                    mStationaryShips.add(ship);
            }
        }
        // thin out empty hexes
        for (Iterator<WorldInst> i = border.iterator(); i.hasNext();)
        {
            WorldInst world = i.next();
            if ((world.getWorld() == null) || (world.getSide() == getSide()))
                i.remove();
        }
        // mark ships needing repair as stationary
        for (WorldInst world : homeland)
        {
            int canRepair = WorldLogic.canRepair(world);
            reserveDamaged(world, canRepair);
        }

        mHomeland = new WorldInst[homeland.size()];
        homeland.toArray(mHomeland);
        mHomelandValue = new int[mHomeland.length];
        for (int i = 0; i < mHomeland.length; i++)
        {
            mHomelandValue[i] = worldValue(mHomeland[i]);// WorldLogic.getHaveWorldValue(getGame(),
                                                         // mHomeland[i]);
            // debug(name(mHomeland[i])+" value "+mHomelandValue[i]);
        }
        sortDescending(mHomeland, mHomelandValue);

        mBorder = new WorldInst[border.size()];
        border.toArray(mBorder);
        mBorderValue = new int[mBorder.length];
        for (int i = 0; i < mBorder.length; i++)
        {
            mBorderValue[i] = WorldLogic.getHaveWorldValue(getGame(),
                    mBorder[i]);
            mBorderValue[i] -= getKnownDefense(mBorder[i]);
            if (getGame().getGame().isAllowIntrinsicDefense())
                mBorderValue[i] -= WorldLogic.getIntrinsicDefense(mBorder[i]);
        }
        sortDescending(mBorder, mBorderValue);
    }

    private void reserveDamaged(WorldInst world, int canRepair)
    {
        if (canRepair <= 0)
            return;
        if (world.getShips().size() == 0)
            return;
        List<ShipInst> wounded = new ArrayList<>();
        for (ShipInst ship : world.getShips())
        {
            if (ship.getSideInst() != getSide())
                continue;
            if (!ship.isDamaged())
                continue;
            int shipCost = ShipLogic.cost(ship.getShip());
            for (int j = 0; j < wounded.size(); j++)
            {
                ShipInst other = (ShipInst)wounded.get(j);
                if (ShipLogic.cost(other.getShip()) < shipCost)
                {
                    wounded.add(j, ship);
                    ship = null;
                    break;
                }
            }
            if (ship != null)
                wounded.add(ship);
        }
        canRepair = Math.min(canRepair, wounded.size());
        // mark stationary
        for (int i = 0; i < canRepair; i++)
        {
            ShipInst ship = (ShipInst)wounded.get(i);
            mMovingShips.remove(ship);
            mStationaryShips.add(ship);
        }
    }

    // we want scouts to go to worlds we don't have recent data on
    private void allocateScouts()
    {
        DebugUtils.beginGroup("scouts");
        for (int i = 0; i < mBorder.length; i++)
        {
            if (mScoutShips.size() == 0)
                break; // none left to scout
            if (WorldLogic.isWitness(mBorder[i], getSide()))
                continue;
            List<ShipInst> potentials = mCanDeploy.get(mBorder[i]);
            for (ShipInst potential : potentials)
                if (mScoutShips.contains(potential))
                {
                    deployTowards(potential, mBorder[i]);
                    mScoutShips.remove(potential);
                    mScoutedWorlds.add(mBorder[i]);
                    break;
                }
        }
        DebugUtils.endGroup("scouts");
    }

    private void allocateOffense()
    {
        DebugUtils.beginGroup("offense");
        for (int i = 0; i < mBorder.length; i++)
        {
            List<ShipInst> potentials = mCanDeploy.get(mBorder[i]);
            if ((potentials == null) || (potentials.size() == 0))
            {
                mUnderattacked.put(mBorder[i],
                        new Integer(estimatedDefense(mBorder[i])));
                continue;
            }
            int bringToBear = 0;
            List<ShipInst> locals = new ArrayList<>();
            List<ShipInst> foreigners = new ArrayList<>();
            for (Iterator<ShipInst> j = potentials.iterator(); j.hasNext();)
            {
                ShipInst potential = j.next();
                if (!mMovingShips.contains(potential))
                    j.remove();
                else
                {
                    bringToBear += ShipLogic.getAttackRecursive(potential);
                    if (potential.getLocation() == mBorder[i])
                        locals.add(potential);
                    else
                        foreigners.add(potential);
                }
            }
            debug("target=" + name(mBorder[i]));
            debug("bringToBear=" + bringToBear);
            if (bringToBear == 0)
            {
                mUnderattacked.put(mBorder[i],
                        new Integer(estimatedDefense(mBorder[i])));
                continue;
            }
            // is combat worth it?
            int needed = whatShouldWeAttackWith(mBorder[i], bringToBear);
            if (needed == 0)
            {
                debug("cannot bring enough to bear");
                mUnderattacked.put(mBorder[i],
                        new Integer(estimatedDefense(mBorder[i])));
                continue;
            }
            ShipInst[] ships = sortDescendingAttack(locals);
            for (int j = 0; j < ships.length; j++)
            {
                debug("attack with=" + name(ships[j]));
                deployTowards(ships[j], mBorder[i]);
                needed -= ShipLogic.getAttackRecursive(ships[j]);
                mMovingShips.remove(ships[j]);
                deployTowards(ships[j], mBorder[i]);
                debug("   deploying " + name(ships[j]) + " here");
                if (needed <= 0)
                {
                    StringBuffer sb = new StringBuffer(" ");
                    while (--j >= 0)
                        sb.append(name(ships[j]) + " ");
                    debug("    remaining:" + sb.toString());
                    break;
                }
            }
            if (needed <= 0)
                continue;
            ships = sortDescendingAttack(foreigners);
            for (int j = 0; j < ships.length; j++)
            {
                debug("attack with=" + name(ships[j]));
                deployTowards(ships[j], mBorder[i]);
                needed -= ShipLogic.getAttackRecursive(ships[j]);
                mMovingShips.remove(ships[j]);
                debug("   deploying " + name(ships[j]) + " to here");
                if (needed <= 0)
                    break;
            }
            if (needed <= 0)
                continue;
            mUnderattacked.put(mBorder[i], new Integer(needed)); // should never
                                                                 // happen
        }
        DebugUtils.endGroup("offsense");
    }

    private void allocateDefense()
    {
        if (mHomeland.length == 0)
            return;
        DebugUtils.beginGroup("defense");
        // see what moves can be made that help achieve this
        for (int i = 0; i < mHomeland.length; i++)
        {
            // work out ideal
            int idealDefense = idealDefenseFactors(mHomeland[i]);
            idealDefense = (int)Math
                    .ceil(idealDefense * getParanoia(mHomeland[i]));
            int actualDefense = 0;
            debug(name(mHomeland[i]) + " needs " + idealDefense);

            // adjust for intrinsic defense
            if (getGame().getGame().isAllowIntrinsicDefense())
                actualDefense += WorldLogic.getIntrinsicDefense(mHomeland[i]);
            debug("  after intrinsic=" + actualDefense);
            if (actualDefense >= idealDefense)
                continue; // we got our quota

            // adjust for stationary ships
            for (ShipInst ship : mHomeland[i].getShips())
                if (ship.getShip().getJump() == 0)
                    actualDefense += ShipLogic.getDefense(ship);
            debug("  after stationary=" + actualDefense);
            if (actualDefense >= idealDefense)
                continue; // we got our quota

            // look amongst ships presently here
            ShipInst[] ships = sortDescendingDefense(mHomeland[i].getShips());
            for (int j = ships.length - 1; j >= 0; j--)
            {
                if (!mMovingShips.contains(ships[j]))
                    continue;
                if (ships[j].getShip().getJump() == 0)
                    continue;
                actualDefense += ShipLogic.getDefense(ships[j]);
                mMovingShips.remove(ships[j]);
                deployTowards(ships[j], mHomeland[i]);
                debug("   deploying " + name(ships[j]) + " here");
                if (actualDefense >= idealDefense)
                {
                    StringBuffer sb = new StringBuffer(" ");
                    while (--j >= 0)
                        sb.append(name(ships[j]) + " ");
                    debug("    remaining:" + sb.toString());
                    break;
                }
            }
            debug("  after present=" + actualDefense);
            if (actualDefense >= idealDefense)
                continue; // we got our quota

            // look amongst ships who can jump here
            List<ShipInst> potentials = mCanDeploy.get(mHomeland[i]);
            if ((potentials == null) || (potentials.size() == 0))
                continue;
            ships = sortDescendingDefense(potentials);
            for (int j = ships.length - 1; j >= 0; j--)
            {
                if (!mMovingShips.contains(ships[j]))
                    continue;
                deployTowards(ships[j], mHomeland[i]);
                actualDefense += ShipLogic.getDefense(ships[j]);
                debug("   deploying " + name(ships[j]) + " to here");
                mMovingShips.remove(ships[j]);
                if (actualDefense >= idealDefense)
                    break;
            }
            debug("  after cavalry=" + actualDefense);
            if (actualDefense >= idealDefense)
                continue; // we got our quota

            mUnderdefended.put(mHomeland[i],
                    new Integer(idealDefense - actualDefense));
        }
        DebugUtils.endGroup("defense");
    }

    private void allocateLongRangePlanner()
    {
        for (Iterator<LongRangePlanner> i = mLongRangePlanner.iterator(); i
                .hasNext();)
        {
            LongRangePlanner lrp = (LongRangePlanner)i.next();
            if (!mMovingShips.contains(lrp.ship))
            {
                debug(name(lrp.ship) + " redployed from long range plan to "
                        + name(lrp.world) + ".");
                i.remove();
                continue;
            }
            if (mUnderattacked.containsKey(lrp.world))
            {
                debug("Continuing " + lrp.toString());
                ShipLogic.setDestination(getGame(), lrp.ship,
                        (WorldInst)lrp.path.get(0));
                lrp.path.remove(0);
                if (lrp.path.size() == 0)
                    i.remove();
                Integer amnt = (Integer)mUnderattacked.get(lrp.world);
                int att = ShipLogic.getAttack(lrp.ship);
                if (amnt.intValue() <= att)
                    mUnderattacked.remove(lrp.world);
                else
                    mUnderattacked.put(lrp.world,
                            new Integer(amnt.intValue() - att));
                mMovingShips.remove(lrp.ship);
                continue;
            }
            if (mUnderdefended.containsKey(lrp.world))
            {
                debug("Continuing " + lrp.toString());
                ShipLogic.setDestination(getGame(), lrp.ship,
                        (WorldInst)lrp.path.get(0));
                lrp.path.remove(0);
                if (lrp.path.size() == 0)
                    i.remove();
                Integer amnt = (Integer)mUnderdefended.get(lrp.world);
                int att = ShipLogic.getAttack(lrp.ship);
                if (amnt.intValue() <= att)
                    mUnderdefended.remove(lrp.world);
                else
                    mUnderdefended.put(lrp.world,
                            new Integer(amnt.intValue() - att));
                mMovingShips.remove(lrp.ship);
                continue;
            }
            debug("Abandoning " + lrp.toString());
            i.remove();
        }
    }

    private void allocateUnderattacked()
    {
        for (WorldInst world : mUnderattacked.keySet())
        {
            int amnt = ((Integer)mUnderattacked.get(world)).intValue();
            while ((mMovingShips.size() > 0) && (amnt > 0))
            {
                LongRangePlanner lrp = findClosest(mMovingShips, world);
                if (lrp.ship == null)
                    break;
                ShipLogic.setDestination(getGame(), lrp.ship,
                        (WorldInst)lrp.path.get(0));
                lrp.path.remove(0);
                lrp.mission = LongRangePlanner.ATTACK;
                if (lrp.path.size() > 0)
                {
                    mLongRangePlanner.add(lrp);
                    debug("Undertaking " + lrp.toString());
                }
                amnt -= ShipLogic.getAttack(lrp.ship);
                mMovingShips.remove(lrp.ship);
            }
            if (mMovingShips.size() == 0)
                break;
        }
    }

    private void allocateUnderdefended()
    {
        for (WorldInst world : mUnderdefended.keySet())
        {
            int amnt = ((Integer)mUnderdefended.get(world)).intValue();
            while ((mMovingShips.size() > 0) && (amnt > 0))
            {
                LongRangePlanner lrp = findClosest(mMovingShips, world);
                if (lrp.ship == null)
                    break;
                ShipLogic.setDestination(getGame(), lrp.ship, lrp.path.get(0));
                lrp.path.remove(0);
                lrp.mission = LongRangePlanner.DEFEND;
                if (lrp.path.size() > 0)
                {
                    mLongRangePlanner.add(lrp);
                    debug("Undertaking " + lrp.toString());
                }
                amnt -= ShipLogic.getDefense(lrp.ship);
                mMovingShips.remove(lrp.ship);
            }
            if (mMovingShips.size() == 0)
                break;
        }
    }

    private LongRangePlanner findClosest(List<ShipInst> ships, WorldInst world)
    {
        LongRangePlanner lrp = new LongRangePlanner();
        lrp.ship = null;
        lrp.world = world;
        for (ShipInst ship : ships)
        {
            List<WorldInst> path = PathLogic.findPath(getGame(),
                    ship.getLocation(), world, ship.getShip().getJump(),
                    ship.getFuel() / ShipLogic.fuelForJump1(ship),
                    ShipLogic.fuelTankage(ship) / ShipLogic.fuelForJump1(ship));
            if (path.size() <= 1)
            {
                debug("Can't path from "
                        + OrdLogic.getShortNum(ship.getLocation().getOrds())
                        + " to " + OrdLogic.getShortNum(world.getOrds()));
                continue;
            }
            if ((lrp.ship == null) || (path.size() < lrp.path.size()))
            {
                lrp.ship = ship;
                lrp.path = path;
            }
        }
        if (lrp.ship != null)
            lrp.path.remove(0);
        return lrp;
    }

    private int estimatedDefense(WorldInst world)
    {
        int def = getKnownDefense(world);
        if (def < 0)
            def = 0;
        if (getGame().getGame().isAllowIntrinsicDefense())
            def += WorldLogic.getIntrinsicDefense(world);
        return def;
    }

    private int whatShouldWeAttackWith(WorldInst world, int bringToBear)
    {
        int def = getKnownDefense(world);
        if (def < 0)
        {
            // are we scouting?
            if (mScoutedWorlds.contains(world))
                return 0; // lets check it out
            else
                return bringToBear; // lets check it out with force!
        }
        if (getGame().getGame().isAllowIntrinsicDefense())
        {
            int intDef = WorldLogic.getIntrinsicDefense(world);
            if (bringToBear < intDef)
                return 0; // fool's errand
            def += intDef;
        }
        determineThresholds(world);
        int minForce = (int)Math
                .ceil(def * mPlayer.getMinimalAttackThreshold());
        int maxForce = (int)Math
                .ceil(def * mPlayer.getDesiredAttackThreshold());
        if (bringToBear < minForce)
            return 0;
        if (bringToBear <= maxForce)
            return bringToBear;
        return maxForce;
    }

    private boolean deployTowards(ShipInst ship, WorldInst world)
    {
        if (ship.getLocation() == world)
            return true;
        List<WorldInst> valid = ShipLogic.validDestinations(getGame(), ship);
        if (valid.contains(world))
        {
            ShipLogic.setDestination(getGame(), ship, world);
            return true;
        }
        List<WorldInst> path = PathLogic.findPath(getGame(), ship.getLocation(),
                world, ship.getShip().getJump(),
                ship.getFuel() / ShipLogic.fuelForJump1(ship),
                ShipLogic.fuelTankage(ship) / ShipLogic.fuelForJump1(ship));
        if (path.size() > 1)
        {
            WorldInst step = (WorldInst)path.get(1);
            debug("     deploying " + name(ship) + " via " + name(step)
                    + " towards " + world);
            StringBuffer sb = new StringBuffer("{");
            for (WorldInst w : path)
                sb.append(" " + name(w));
            debug("     " + sb.toString() + " }");
            ShipLogic.setDestination(getGame(), ship, step);
            return true;
        }
        else
        {
            debug("**** Can't get from " + name(ship.getLocation()) + " to "
                    + name(world));
            return false;
        }
    }

    private int getKnownDefense(WorldInst world)
    {
        if (WorldLogic.isWitness(world, getSide()))
        {
            int ret = WorldLogic.getDefenseFactors(getGame(), world, getSide());
            mLastKnownDefenseFactors.put(world, new Integer(ret));
            return ret;
        }
        else
        {
            Integer def = (Integer)mLastKnownDefenseFactors.get(world);
            if (def == null)
                return -1;
            else
                return def.intValue();
        }
    }

    private void addDeployability(ShipInst ship, List<WorldInst> worlds)
    {
        for (WorldInst world : worlds)
        {
            List<ShipInst> ships = mCanDeploy.get(world);
            if (ships == null)
            {
                ships = new ArrayList<>();
                mCanDeploy.put(world, ships);
            }
            ships.add(ship);
        }
    }

    private void dockFighters()
    {
        for (ShipInst ship : getSide().getShips())
        {
            if (!ShipLogic.isCarrier(ship.getShip()))
                continue;
            int cap = ShipLogic.additionalCapacity(ship);
            Object[] ships = ship.getLocation().getShips().toArray();
            for (int j = 0; j < ships.length; j++)
            {
                ShipInst fighter = (ShipInst)ships[j];
                if (!ShipLogic.isFighter(fighter.getShip()))
                    continue;
                if (fighter.getContainedBy() != null)
                    continue;
                if (ShipLogic.size(fighter) < cap)
                    ShipLogic.dock(ship, fighter);
            }
        }
    }
}

class LongRangePlanner
{
    public static final int ATTACK = 1;
    public static final int DEFEND = 1;

    ShipInst                ship;
    List<WorldInst>         path;
    WorldInst               world;
    int                     mission;

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if (mission == ATTACK)
            sb.append("Long range attack by ");
        else
            sb.append("Long range defense by ");
        sb.append(BaseHandler.name(ship));
        sb.append(" [");
        sb.append(ShipLogic.getJumpDescription(ship));
        sb.append("] ");
        sb.append(BaseHandler.name(ship.getLocation()));
        for (WorldInst world : path)
        {
            sb.append("->");
            sb.append(BaseHandler.name(world));
        }
        return sb.toString();
    }
}