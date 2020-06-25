package jo.ttg.adv.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import ttg.beans.adv.Game;
import ttg.beans.adv.PassengerBean;
import ttg.logic.adv.GameLogic;
import ttg.logic.adv.LocationLogic;
import ttg.logic.adv.ShipLogic;
import ttg.logic.adv.TimeLogic;

class CargoTest extends BaseAdvTest
{
    @Test
    void testFreight() throws IOException
    {
        Game game = GameLogic.newGame();
        ShipLogic.selectDestination(game, NEARBY);
        
        Assert.assertTrue("Items!", game.getShip().getCargo().size() == 0);
        fillUpOnFreight(game);
        Assert.assertTrue("No items!", game.getShip().getCargo().size() > 0);
        
        LocationLogic.undock(game);
        TimeLogic.passTime(game, 14*24*60);
        Assert.assertEquals("Did not get to destination", game.getShip().getDestination(), game.getShip().getLocation());
        boolean docked = LocationLogic.dock(game);
        Assert.assertTrue("Did not dock!", docked);
        
        emptyHold(game);
        Assert.assertTrue("Items!", game.getShip().getCargo().size() == 0);
    }

    @Test
    void testCargo() throws IOException
    {
        Game game = GameLogic.newGame();
        ShipLogic.selectDestination(game, NEARBY);
        
        Assert.assertTrue("Items!", game.getShip().getCargo().size() == 0);
        fillUpOnCargo(game);
        Assert.assertTrue("No items!", game.getShip().getCargo().size() > 0);
        
        LocationLogic.undock(game);
        TimeLogic.passTime(game, 14*24*60);
        Assert.assertEquals("Did not get to destination", game.getShip().getDestination(), game.getShip().getLocation());
        boolean docked = LocationLogic.dock(game);
        Assert.assertTrue("Did not dock!", docked);
        
        emptyHold(game);
        Assert.assertTrue("Items!", game.getShip().getCargo().size() == 0);
    }

    @Test
    void testPassengers() throws IOException
    {
        Game game = GameLogic.newGame();
        ShipLogic.selectDestination(game, NEARBY);
        
        Assert.assertTrue("Items!", game.getShip().getPassengers().size() == 0);
        fillUpOnPassengers(game);
        Assert.assertTrue("No items!", game.getShip().getPassengers().size() > 0);
        
        LocationLogic.undock(game);
        TimeLogic.passTime(game, 14*24*60);
        Assert.assertEquals("Did not get to destination", game.getShip().getDestination(), game.getShip().getLocation());
        boolean docked = LocationLogic.dock(game);
        Assert.assertTrue("Did not dock!", docked);

        for (PassengerBean p : game.getShip().getPassengers())
            Assert.assertEquals("Non-low berth passenger", PassengerBean.PASSAGE_LOW, p.getPassage());
        emptyPassengers(game);
        Assert.assertTrue("Items!", game.getShip().getPassengers().size() == 0);
    }
}
