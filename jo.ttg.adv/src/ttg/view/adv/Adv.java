/*
 * Created on Dec 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.view.adv;

import jo.ttg.core.ui.swing.ctrl.BodyView;
import jo.ttg.core.ui.swing.ctrl.body.BodyPanel;
import jo.ttg.logic.gen.CargoLogic;
import ttg.beans.adv.Game;
import ttg.logic.adv.AdvCargoLogicHandler;
import ttg.logic.adv.GameLogic;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Adv
{
    public static final boolean CHEATS_ENABLED = true;
	private Game	mGame;
	
	public Adv()
	{
	    GameLogic.init();
		mGame = GameLogic.newGame();
		BodyView.addHandler(new AdvViewHandler());
		CargoLogic.addHandler(new AdvCargoLogicHandler(mGame.getScheme()));
		BodyPanel.addHandler(new AdvBodyPanelHander());
	}
	
	public void run()
	{
		AdvFrame adv = new AdvFrame();
		adv.setGame(mGame);
		adv.setVisible(true);
	}
	
	public static void main(String[] argv)
	{
		Adv app = new Adv();
		app.run();
	}
}
