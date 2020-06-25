package jo.ttg.adv.test;

import java.util.List;

import org.junit.Before;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.logic.gen.SchemeLogic;
import ttg.beans.adv.AdvCargoBean;
import ttg.beans.adv.Game;
import ttg.beans.adv.PassengerBean;
import ttg.logic.adv.BuyLogic;
import ttg.logic.adv.ForSaleLogic;
import ttg.logic.adv.GameLogic;
import ttg.logic.adv.PassengerLogic;
import ttg.logic.adv.SellLogic;

class BaseAdvTest
{
    protected static final String NEARBY = "body://20,24,0/Ahti/Udo/Shirene/Shirene+System+Starport";

    @Before
    void setup()
    {
        GameLogic.init();
    }

    protected void emptyHold(Game game)
    {
        CargoBean[] contents = game.getShip().getCargo().toArray(new CargoBean[0]);
        for (CargoBean content : contents)
            SellLogic.sellCargo(game, game.getShip(), content);
    }

    protected void fillUpOnFreight(Game game)
    {
        BodyBean ori = (BodyBean)SchemeLogic.getFromURI(game.getShip().getLocation());
        BodyBean dest = (BodyBean)SchemeLogic.getFromURI(game.getShip().getDestination());
        List<CargoBean> items = ForSaleLogic.genFreightForSale(game, ori, dest, game.getDate());
        for (CargoBean item : items)
            BuyLogic.buyFreight(game, game.getShip(), (AdvCargoBean)item);
    }

    protected void fillUpOnCargo(Game game)
    {
        BodyBean ori = (BodyBean)SchemeLogic.getFromURI(game.getShip().getLocation());
        List<CargoBean> items = ForSaleLogic.genCargosForSale(game, ori, game.getDate());
        for (CargoBean item : items)
            BuyLogic.buyCargo(game, game.getShip(), (AdvCargoBean)item);
    }

    protected void emptyPassengers(Game game)
    {
        PassengerLogic.disembarkPassengers(game, game.getShip().getPassengers());
    }

    protected void fillUpOnPassengers(Game game)
    {
        BodyBean ori = (BodyBean)SchemeLogic.getFromURI(game.getShip().getLocation());
        BodyBean dest = (BodyBean)SchemeLogic.getFromURI(game.getShip().getDestination());
        List<PassengerBean> items = ForSaleLogic.genPassengersForSale(game, ori, dest, game.getDate());
        for (PassengerBean item : items)
            BuyLogic.buyPassenger(game, game.getShip(), item);
    }
}
