package ttg.beans.war;

import java.util.ArrayList;

public interface PlayerInterface
{
	public void setup();
	public void message(PlayerMessage msg);
	public void move();
	public void flee(WorldInst world);
	public void target(WorldInst world);
    public void repair(WorldInst world, ArrayList ships);
}
