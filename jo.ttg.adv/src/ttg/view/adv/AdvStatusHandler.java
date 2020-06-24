/*
 * Created on Jan 26, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.adv;

import jo.ttg.beans.trade.CargoBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import ttg.beans.adv.AdvEvent;
import ttg.beans.adv.CrewBean;
import ttg.beans.adv.Game;
import ttg.beans.adv.PassengerBean;
import ttg.logic.adv.AdvEventHandler;
import ttg.logic.adv.GameLogic;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AdvStatusHandler implements AdvEventHandler
{
    /* (non-Javadoc)
     * @see ttg.logic.adv.AdvEventHandler#advEvent(ttg.beans.adv.AdvEvent)
     */
    public void advEvent(AdvEvent event)
    {
        Game game = (Game)event.getSource();
        switch (event.getID())
        {
            case AdvEvent.GAME_LOADED:
                GameLogic.status(game, "Game loaded");
                break;
            case AdvEvent.GAME_SAVED:
        		GameLogic.status(game, "Game saved");
                break;
            case AdvEvent.GAME_NEW:
        		GameLogic.status(game, "New game");
                break;
            case AdvEvent.CARGO_BUY_NO_SPACE:
            {
                CargoBean cargo = (CargoBean)event.getNoun();
            	GameLogic.status(game, "Cannot hold "+FormatUtils.sTons(cargo.getQuantity())+" of "+cargo.getName());
                break;
            }
            case AdvEvent.CARGO_BUY_NO_MONEY:
            {
                CargoBean cargo = (CargoBean)event.getNoun();
            	Double amnt = (Double)event.getAdjective();
            	GameLogic.status(game, "Cannot afford "+FormatUtils.sCurrency(amnt.doubleValue())+" for cargo "+cargo.getName());
                break;
            }
            case AdvEvent.FREIGHT_BUY_NO_SPACE:
            {
                CargoBean cargo = (CargoBean)event.getNoun();
            	GameLogic.status(game, "Cannot hold "+FormatUtils.sTons(cargo.getQuantity())+" of "+cargo.getName());
                break;
            }
            case AdvEvent.FREIGHT_BUY_NO_MONEY:
            {
                CargoBean cargo = (CargoBean)event.getNoun();
            	Double amnt = (Double)event.getAdjective();
            	GameLogic.status(game, "Cannot afford "+FormatUtils.sCurrency(amnt.doubleValue())+" for downpayment of "+cargo.getName());
                break;
            }
            case AdvEvent.PASSENGER_CONTRACT_NO_BERTH:
            {
                PassengerBean passenger = (PassengerBean)event.getNoun(); 
                GameLogic.status(game, "Not enough low berths to contract with "+passenger.getName());
                break;
            }
            case AdvEvent.PASSENGER_CONTRACT_NO_CABIN:
            {
                PassengerBean passenger = (PassengerBean)event.getNoun(); 
                GameLogic.status(game, "Not enough cabins to contract with "+passenger.getName());
                break;
            }
            case AdvEvent.PASSENGER_CONTRACT_BUMPED:
            {
                PassengerBean passenger = (PassengerBean)event.getNoun(); 
                GameLogic.status(game, "Standby passenger "+passenger.getName()+" has been bumped.");
                break;
            }
            case AdvEvent.CREW_HIRE_NO_CABIN:
            {
                CrewBean crew = (CrewBean)event.getNoun(); 
                GameLogic.status(game, "Not enough cabins to hire "+crew.getName());
                break;
            }
            case AdvEvent.FUEL_BUY_NO_MONEY:
            {
                double needed = ((Double)event.getNoun()).doubleValue();
                double cost = ((Double)event.getAdjective()).doubleValue();
                GameLogic.status(game, "Can't afford "+FormatUtils.sCurrency(cost)+" for "+FormatUtils.sTons((int)needed)+" of fuel");
                break;
            }
            case AdvEvent.FUEL_SCOOPED:
            {
                GameLogic.status(game, "Fuel scooping operation successful");
                break;
            }
            case AdvEvent.FUEL_SCOOPED_FAILED:
            {
                GameLogic.status(game, "Fuel scooping operation unsuccessful");
                break;
            }
            case AdvEvent.PASSENGER_DISEMBARK:
            {
                String who = (String)event.getNoun();
                GameLogic.status(game, "Passengers disembark: "+who);
                break;
            }
            case AdvEvent.PASSENGER_DISEMBARK_LEAVE:
            {
                String who = (String)event.getNoun();
                GameLogic.status(game, "Passengers abandon you: "+who);
                break;
            }
            case AdvEvent.PASSENGER_DISEMBARK_ABANDON:
            {
                String who = (String)event.getNoun();
                GameLogic.status(game, "Passengers abandoned: "+who);
                break;
            }
            case AdvEvent.TIME_PASSES:
            {
                int minutes = ((Integer)event.getNoun()).intValue();
                GameLogic.status(game, minutes+" minutes pass");
                break;
            }
       }
    }
}
