package ttg.war.view.ai;

import java.util.List;

import ttg.war.beans.PlayerInterface;
import ttg.war.beans.PlayerMessage;
import ttg.war.beans.ShipInst;
import ttg.war.beans.WorldInst;
import ttg.war.logic.ShipLogic;
import ttg.war.view.WarPanel;

public class GUIPlayer implements PlayerInterface
{
	private WarPanel	mPanel;

	public GUIPlayer(WarPanel panel)
	{
		mPanel = panel;
	}
	
	private void waitForDone()
	{
		while (mPanel.getMode() != WarPanel.DONE)
		{
			try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException e)
            {
            }
		}
	}

	public void setup()
	{
		if ((mPanel.getSide().getWorlds().size() == 1)
			&& !mPanel.getGame().getGame().isAllowFleetReconfiguration())
		{
			WorldInst world = (WorldInst)mPanel.getSide().getWorlds().get(0);
			for (ShipInst ship : mPanel.getSide().getShips())
				ShipLogic.setDestination(mPanel.getGame(), ship, world);
		}
		else
		{
			mPanel.setMode(WarPanel.SETUP);
			waitForDone();
		}
	}

	/* (non-Javadoc)
	 * @see ttg.beans.war.PlayerInterface#message(ttg.beans.war.PlayerMessage)
	 */
	public void message(PlayerMessage msg)
	{
		mPanel.getMessagePanel().setMessage(msg);
		mPanel.setMode(WarPanel.MESSAGE);
		waitForDone();
	}

	/* (non-Javadoc)
	 * @see ttg.beans.war.PlayerInterface#move()
	 */
	public void move()
	{
		mPanel.setMode(WarPanel.MOVE);
		waitForDone();
	}

	/* (non-Javadoc)
	 * @see ttg.beans.war.PlayerInterface#flee(ttg.beans.war.WorldInst)
	 */
	public void flee(WorldInst world)
	{
		boolean any = false;
		for (ShipInst ship : world.getShips())
		{
			if ((ship.getSideInst() == mPanel.getSide()) && !ship.isFleeing())
			{
				any = true;
				break;
			}
		}
		if (!any)
			return;
		mPanel.setArg1(world);
		mPanel.setMode(WarPanel.FLEE);
		waitForDone();
	}

	/* (non-Javadoc)
	 * @see ttg.beans.war.PlayerInterface#target(ttg.beans.war.WorldInst)
	 */
	public void target(WorldInst world)
	{
		mPanel.setArg1(world);
		mPanel.setMode(WarPanel.TARGET);
		waitForDone();
	}

    public void repair(WorldInst world, List<ShipInst> ships)
    {
		mPanel.setArg1(ships);
		mPanel.setMode(WarPanel.REPAIR);
		waitForDone();
    }
}
