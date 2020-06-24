package jo.ttg.adv.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import jo.ttg.ship.beans.comp.ShipComponent;
import ttg.beans.adv.Game;
import ttg.logic.adv.GameLogic;

class IOTest extends BaseAdvTest
{

    @Test
    void test() throws IOException
    {
        Game gameIn = GameLogic.newGame();
        File file = File.createTempFile("adv", ".json");
        System.out.println(file.toString());
        gameIn.setSaveFile(file.toString());
        GameLogic.save(gameIn);
        Game gameOut = GameLogic.open(file);
        List<ShipComponent> comps1 = gameIn.getShip().getDesign().getComponents();
        List<ShipComponent> comps2 = gameOut.getShip().getDesign().getComponents();
        Assert.assertEquals("Component mismatch", comps1.size(), comps2.size());
        for (int i = 0; i < comps1.size(); i++)
        {
            ShipComponent comp1 = comps1.get(i);
            ShipComponent comp2 = comps2.get(i);
            Assert.assertEquals("Component mismatch", comp1, comp2);
        }
    }

}
