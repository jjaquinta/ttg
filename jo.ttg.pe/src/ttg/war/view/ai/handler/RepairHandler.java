/*
 * Created on Apr 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.war.view.ai.handler;

import java.util.List;

import ttg.war.beans.ShipInst;
import ttg.war.beans.WorldInst;
import ttg.war.view.ai.ComputerPlayer;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RepairHandler extends BaseHandler
{
	public RepairHandler(ComputerPlayer player)
	{
		super(player);
	}

	public void repair(WorldInst world, List<ShipInst> ships)
	{
		int port = world.getWorld().getPopulatedStats().getUPP().getPort().getValue();
		((ShipInst)ships.get(0)).setToDo(false);
		debug("Request repair of "+((ShipInst)ships.get(0)).getShip().getName());
		if ((port == 'A') && (ships.size() > 1))
			((ShipInst)ships.get(1)).setToDo(false);
	}
}
