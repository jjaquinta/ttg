package ttg.war.view.ai;

import java.util.List;

import ttg.war.beans.GameInst;
import ttg.war.beans.PlayerInterface;
import ttg.war.beans.PlayerMessage;
import ttg.war.beans.ShipInst;
import ttg.war.beans.SideInst;
import ttg.war.beans.WorldInst;
import ttg.war.view.WarPanel;

public class ObserverPlayer implements PlayerInterface
{
	private GUIPlayer		mGUIPlayer;	
	private ComputerPlayer	mCompPlayer;	

	public ObserverPlayer(WarPanel panel, GameInst game, SideInst side)
	{
		mGUIPlayer = new GUIPlayer(panel);
		mCompPlayer = new ComputerPlayer(game, side);
	}

    public void setup()
    {
        mCompPlayer.setup();
    }

    public void message(PlayerMessage msg)
    {
		mCompPlayer.message(msg);
		if ((msg.getID() == PlayerMessage.ENDOFTURN) || (msg.getID() == PlayerMessage.GAMEOVER))
			mGUIPlayer.message(msg);
    }

    public void move()
    {
		mCompPlayer.move();
    }

    public void flee(WorldInst world)
    {
		mCompPlayer.flee(world);
    }

    public void target(WorldInst world)
    {
		mCompPlayer.target(world);
    }

    public void repair(WorldInst world, List<ShipInst> ships)
    {
		mCompPlayer.repair(world, ships);
    }

}
