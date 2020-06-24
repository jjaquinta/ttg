/*
 * Created on Jan 5, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.logic.adv;

import jo.ttg.logic.RandLogic;
import jo.ttg.logic.gen.CargoLogic;
import ttg.beans.adv.AdvCargoBean;
import ttg.beans.adv.AdvEvent;
import ttg.beans.adv.CrewBean;
import ttg.beans.adv.Game;
import ttg.beans.adv.PassengerBean;
import ttg.beans.adv.ShipInst;
import ttg.beans.adv.UNIDInst;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BuyLogic
{
	/**
	 * @param mGame
	 * @param mShip
	 */
	public static boolean buyCargo(Game game, ShipInst ship, AdvCargoBean cargo)
	{
	    int contains = CargoLogic.totalTons(ship.getCargo());
	    if (contains + cargo.getQuantity() > ship.getStats().getCargo()/13.5)
	    {
	        AdvEventLogic.fireEvent(game, AdvEvent.CARGO_BUY_NO_SPACE, cargo);		
		    return false;
	    }
	    cargo.getDelivered().setMinutes(game.getDate().getMinutes() + RandLogic.roll(game.getRnd(), 4, 24*60));
		ship.getCargo().add(cargo);
		double amnt = CargoLogic.purchasePrice(cargo, game.getScheme());
		if (!MoneyLogic.debitFromCash(game, amnt, "Purchase of cargo "+cargo.getName()))
		{
	        AdvEventLogic.fireEvent(game, AdvEvent.CARGO_BUY_NO_MONEY, cargo, new Double(amnt));
		    return false;
		}
		ReputationLogic.incrementLocation(game, ship.getLocation(), cargo.getQuantity());
		UNIDLogic.use(game, cargo, UNIDInst.CARGO, 7);
		ship.fireCargoChange();
        AdvEventLogic.fireEvent(game, AdvEvent.CARGO_BUY, cargo);		
		return true;
	}

	/**
	 * @param mGame
	 * @param mShip
	 */
	public static boolean buyFreight(Game game, ShipInst ship, AdvCargoBean freight)
	{
	    int contains = CargoLogic.totalTons(ship.getCargo());
	    if (contains + freight.getQuantity() > ship.getStats().getCargo()/13.5)
	    {
	        AdvEventLogic.fireEvent(game, AdvEvent.FREIGHT_BUY_NO_SPACE, freight);		
		    return false;
	    }
	    if (LocationLogic.isSameSystem(freight.getOrigin(), freight.getDestination()))
	        freight.getDelivered().setMinutes(game.getDate().getMinutes() +  RandLogic.roll(game.getRnd(), 6, 60));
		else
		    freight.getDelivered().setMinutes(game.getDate().getMinutes() +  RandLogic.roll(game.getRnd(), 4, 24*60));
		ship.getCargo().add(freight);
		double amnt = CargoLogic.purchasePrice(freight, game.getScheme());
		if (!MoneyLogic.debitFromCash(game, amnt, "Down payment on freight "+freight.getName()))
		{
	        AdvEventLogic.fireEvent(game, AdvEvent.FREIGHT_BUY_NO_MONEY, freight, new Double(amnt));
		    return false;
		}
		ReputationLogic.incrementLocation(game, ship.getLocation(), freight.getQuantity());
		UNIDLogic.use(game, freight, UNIDInst.CARGO, 7);
		ship.fireCargoChange();
		return true;
	}

	/**
	 * @param mGame
	 * @param mShip
	 */
	public static boolean buyPassenger(Game game, ShipInst ship, PassengerBean passenger)
	{
	    if (passenger.getPassage() == PassengerBean.PASSAGE_LOW)
	    {
		    int contains = PassengerLogic.totalBerths(ship.getPassengers());
		    if (contains + 1 > ship.getStats().getLowBerths())
		    {
		        AdvEventLogic.fireEvent(game, AdvEvent.PASSENGER_CONTRACT_NO_BERTH, passenger);
		        return false;
		    }
	    }
	    else
	    {
		    int contains = PassengerLogic.totalCabins(ship.getPassengers()) + ship.getCrew().size();
		    if (contains + 1 > ship.getStats().getStaterooms())
		    {
		        PassengerBean m = PassengerLogic.getYoungestMiddle(ship.getPassengers());
		        if (m == null)
		        {
		            AdvEventLogic.fireEvent(game, AdvEvent.PASSENGER_CONTRACT_NO_CABIN, passenger);
		            return false;
		        }
		        else
		        {
		            ship.getPassengers().remove(m);
		            AdvEventLogic.fireEvent(game, AdvEvent.PASSENGER_CONTRACT_BUMPED, m);
		    		UNIDLogic.unuse(game, m, UNIDInst.STAFF);
		        }
		    }
	    }
		ship.getPassengers().add(passenger);
		passenger.getBoarded().set(game.getDate());
		UNIDLogic.use(game, passenger, UNIDInst.STAFF, 7);
		ship.firePassengersChange();
        AdvEventLogic.fireEvent(game, AdvEvent.PASSENGER_CONTRACT, passenger);
		return true;
	}

	/**
	 * @param mGame
	 * @param mShip
	 */
	public static boolean hireCrew(Game game, ShipInst ship, CrewBean crew)
	{
	    int contains = PassengerLogic.totalCabins(ship.getPassengers()) + ship.getCrew().size();
	    if (contains + 1 > ship.getStats().getStaterooms())
	    {
            AdvEventLogic.fireEvent(game, AdvEvent.CREW_HIRE_NO_CABIN, crew);
            return false;
	    }
		ship.getCrew().add(crew);
		crew.getDateOfHire().set(game.getDate());
		UNIDLogic.use(game, crew, UNIDInst.STAFF, 7);
		ship.fireCrewChange();
        AdvEventLogic.fireEvent(game, AdvEvent.CREW_HIRE, crew);
		return true;
	}
}
