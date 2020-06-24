package jo.ttg.adv.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import jo.ttg.beans.URIBean;
import jo.ttg.logic.gen.SchemeLogic;
import ttg.beans.adv.Game;
import ttg.logic.adv.GameLogic;

class URITest extends BaseAdvTest
{

    @Test
    void test() throws IOException
    {
        Game game = GameLogic.newGame();
        URIBean l = SchemeLogic.getFromURI(game.getShip().getLocation());
        Assert.assertNotNull("Can't resolve location '"+game.getShip().getLocation()+"'", l);
    }

}
