package jo.ttg.adv.test;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import jo.ttg.beans.URIBean;
import jo.ttg.logic.gen.SchemeLogic;
import jo.util.utils.io.FileUtils;
import ttg.beans.adv.Game;
import ttg.logic.adv.GameLogic;
import ttg.logic.adv.LocationLogic;
import ttg.logic.adv.ShipLogic;
import ttg.logic.adv.TimeLogic;

class URITest extends BaseAdvTest
{

    @Test
    void test() throws IOException
    {
        Game game = GameLogic.newGame();
        URIBean l = SchemeLogic.getFromURI(game.getShip().getLocation());
        Assert.assertNotNull("Can't resolve location '"+game.getShip().getLocation()+"'", l);
    }

    private void testState(Game game) throws IOException
    {
        URIBean ori = SchemeLogic.getFromURI(game.getShip().getLocation());
        Assert.assertNotNull("Can't resolve location '"+game.getShip().getLocation()+"'", ori);
        URIBean dest = SchemeLogic.getFromURI(game.getShip().getDestination());
        Assert.assertNotNull("Can't resolve location '"+game.getShip().getDestination()+"'", dest);
        File file = File.createTempFile("adv", ".json");
        game.setSaveFile(file.toString());
        GameLogic.save(game);
        String json = FileUtils.readFileAsString(file.toString());
        int idx = json.indexOf("/[");
        if (idx > 0)
        {
            int low = Math.max(0, idx - 24);
            int high = Math.min(json.length(), idx + 64);
            String desc = json.substring(low, high);
            Assert.fail("Bad URL in save file: '"+desc+"'");
        }
    }
    
    @Test
    void testMove() throws IOException
    {
        Game game = GameLogic.newGame();
        ShipLogic.selectDestination(game, NEARBY);
        testState(game);

        LocationLogic.undock(game);
        for (;;)
        {
            TimeLogic.passTime(game, 24*60);
            testState(game);
            if (game.getShip().getDestination().equals(game.getShip().getLocation()))
                break;
        }
        boolean docked = LocationLogic.dock(game);
        Assert.assertTrue("Did not dock!", docked);
        testState(game);
    }
    
    @Test
    void testShipments() throws IOException
    {
        Game game = GameLogic.newGame();
        ShipLogic.selectDestination(game, NEARBY);
        testState(game);

        fillUpOnFreight(game);
        fillUpOnPassengers(game);
        LocationLogic.undock(game);
        for (;;)
        {
            TimeLogic.passTime(game, 24*60);
            testState(game);
            if (game.getShip().getDestination().equals(game.getShip().getLocation()))
                break;
        }
        boolean docked = LocationLogic.dock(game);
        Assert.assertTrue("Did not dock!", docked);
        testState(game);
    }

}
