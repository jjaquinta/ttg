package jo.ttg.test.war;

import java.util.List;

import ttg.war.beans.PlayerInterface;
import ttg.war.beans.PlayerMessage;
import ttg.war.beans.ShipInst;
import ttg.war.beans.WorldInst;

public class NullPlayer implements PlayerInterface
{
    @Override
    public void setup()
    {
    }

    @Override
    public void message(PlayerMessage msg)
    {
    }

    @Override
    public void move()
    {
    }

    @Override
    public void flee(WorldInst world)
    {
    }

    @Override
    public void target(WorldInst world)
    {
    }

    @Override
    public void repair(WorldInst world, List<ShipInst> ships)
    {
    }

}
