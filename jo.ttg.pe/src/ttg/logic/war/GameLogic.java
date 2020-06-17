/*
 * Created on Dec 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.logic.war;

import java.util.ArrayList;

import ttg.beans.war.GameInst;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GameLogic
{
	public static void status(GameInst game, String newStatus)
	{
		ArrayList history = game.getStatusHistory();
		while (history.size() > 5)
			history.remove(0);
		history.add(newStatus);
		game.setStatus(newStatus);
	}
}
