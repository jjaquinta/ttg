/*
 * Created on Dec 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.logic;

import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.dist.DistCapabilities;
import jo.ttg.beans.dist.DistConsumption;
import jo.ttg.beans.dist.DistTransition;
import jo.ttg.beans.dist.DistTransitionJump;
import jo.ttg.beans.dist.DistTransitionOrbit;
import jo.ttg.logic.DateLogic;
import jo.ttg.logic.dist.ConsumptionLogic;
import jo.ttg.logic.dist.TraverseException;
import jo.ttg.logic.dist.TraverseLogic;
import ttg.adv.beans.AdvEvent;
import ttg.adv.beans.Game;
import ttg.adv.beans.ShipInst;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TimeLogic
{
    public static void passTime(Game game, int minutes)
    {
        ShipInst ship = game.getShip();
        if (ship.isDocked() || ship.getLocation().equals(ship.getDestination()))
        {        // we aren't moving
            consumeTime(game, minutes);
            AdvEventLogic.fireEvent(game, AdvEvent.TIME_PASSES, new Integer(minutes));
        }
        else 
        {		// we are moving
            DistCapabilities caps = ShipLogic.getCaps(game.getShip().getStats());
            try
            {
                List<DistTransition> trav = TraverseLogic.calcTraverse(ship.getLocation(), ship.getDestination(), caps, game.getScheme());
                DistTransition lastTrans = null;
                for (Iterator<DistTransition> i = trav.iterator(); i.hasNext(); )
                {
                    DistTransition trans = i.next();
                    DistConsumption cons = new DistConsumption();
                    ConsumptionLogic.calcConsumption(trans, caps, cons);
                    boolean enoughFuel = cons.getFuel() <= ship.getFuel();
                    boolean enoughTime = cons.getTime().getMinutes() <= minutes;
                    if ((trans instanceof DistTransitionJump) && !enoughFuel)
                        break;
                    if ((lastTrans instanceof DistTransitionOrbit) && (trans instanceof DistTransitionJump))
                        AdvEventLogic.fireEvent(game, AdvEvent.SHIP_JUMP_ENTER);
                    else if ((lastTrans instanceof DistTransitionJump) && (trans instanceof DistTransitionOrbit))
                        AdvEventLogic.fireEvent(game, AdvEvent.SHIP_JUMP_EXIT);
                    if (enoughFuel && enoughTime)
                    {
                        consumeTime(game, cons.getTime().getMinutes());
                        consumeFuel(game, cons.getFuel());
                        minutes -= cons.getTime().getMinutes();
                        if (!i.hasNext())
                        {
                            ship.setLocation(ship.getDestination());
                            break;
                        }
                    }
                    else if (!enoughFuel && enoughTime)
                    {
                        double pcFuel = ship.getFuel()/cons.getFuel();
                        consumeTime(game, (int)(cons.getTime().getMinutes()*pcFuel));
                        consumeFuel(game, ship.getFuel());
                        ship.setLocation(TraverseLogic.getIncrementalLocation(trans, pcFuel));
                        break;
                    }
                    else if (enoughFuel && !enoughTime)
                    {
                        double pcTime = (double)minutes/(double)cons.getTime().getMinutes();
                        consumeFuel(game, cons.getFuel()*pcTime);
                        consumeTime(game, minutes);
                        ship.setLocation(TraverseLogic.getIncrementalLocation(trans, pcTime));
                        break;
                    }
                    else if (!enoughFuel && !enoughTime)
                    {
                        double pcFuel = ship.getFuel()/cons.getFuel();
                        double pcTime = (double)minutes/(double)cons.getTime().getMinutes();
                        double pc = Math.min(pcFuel, pcTime);
                        consumeTime(game, (int)(cons.getTime().getMinutes()*pc));
                        consumeFuel(game, cons.getFuel()*pc);
                        ship.setLocation(TraverseLogic.getIncrementalLocation(trans, pc));
                        break;
                    }
                    lastTrans = trans;
                }
            }
            catch (TraverseException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public static void consumeTime(Game game, int minutes)
    {
        DateBean oldDate = game.getDate();
        DateBean newDate = new DateBean();
        newDate.setMinutes(oldDate.getMinutes());
        DateLogic.incrementMinutes(newDate, minutes);
        FuelLogic.purify(game.getShip(), minutes);
        MoneyLogic.loanElapsed(game, oldDate, newDate); 
        game.setDate(newDate);
    }
    
    public static void consumeFuel(Game game, double fuel)
    {
        game.getShip().setFuel(game.getShip().getFuel() - fuel);
    }
}
