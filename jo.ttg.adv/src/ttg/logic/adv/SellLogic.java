/*
 * Created on Jan 5, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.logic.adv;

import jo.ttg.beans.chr.CharBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.logic.gen.CargoLogic;
import ttg.beans.adv.Game;
import ttg.beans.adv.ShipInst;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SellLogic
{
	/**
	 * @param mGame
	 * @param mShip
	 */
	public static void sellCargo(Game game, ShipInst ship, CargoBean cargo)
	{
		ship.getCargo().remove(cargo);
		double amnt = CargoLogic.salePrice(cargo, ship.getLocation(), game.getDate(), game.getScheme());
		MoneyLogic.creditToCash(game, amnt, "Sale of cargo "+cargo.getName());
		ReputationLogic.incrementReputation(game, ship.getLocation()+".trade", cargo.getQuantity());
		ship.fireCargoChange();
	}

	public static void ditchCargo(Game game, ShipInst ship, CargoBean cargo)
	{
		ship.getCargo().remove(cargo);
		if (cargo.getClassification() == CargoBean.CC_FREIGHT)
		    ReputationLogic.decrementLocation(game, cargo.getDestination(), cargo.getQuantity()*10);
		ship.fireCargoChange();
	}

	/**
	 * @param mGame
	 * @param mShip
	 */
	public static void fireCrew(Game game, ShipInst ship, CharBean crew)
	{
		ship.getCrew().remove(crew);
		// TODO: factor in cost
		ship.fireCrewChange();
	}
}
