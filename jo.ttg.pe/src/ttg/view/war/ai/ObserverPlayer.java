package ttg.view.war.ai;

import java.util.ArrayList;

import ttg.beans.war.GameInst;
import ttg.beans.war.PlayerInterface;
import ttg.beans.war.PlayerMessage;
import ttg.beans.war.SideInst;
import ttg.beans.war.WorldInst;
import ttg.view.war.WarPanel;

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

    public void repair(WorldInst world, ArrayList ships)
    {
		mCompPlayer.repair(world, ships);
    }

}
