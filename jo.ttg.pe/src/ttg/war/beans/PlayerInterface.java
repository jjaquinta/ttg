package ttg.war.beans;

import java.util.List;

public interface PlayerInterface
{
    public void setup();

    public void message(PlayerMessage msg);

    public void move();

    public void flee(WorldInst world);

    public void target(WorldInst world);

    public void repair(WorldInst world, List<ShipInst> ships);
}
