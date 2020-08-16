package jo.ttg.lbb.ui.ship2;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import jo.ttg.lbb.logic.ship2.ScenarioLogic;

@SuppressWarnings("serial")
public class CombatFrame extends Frame
{
    private GamePanel mClient;
    
    public CombatFrame()
    {
        ScenarioLogic.loadBuiltIn();
        mClient = new GamePanel();
        add("Center", mClient);
        addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e)
        	{
        		System.exit(0);
        	}
		});
    }
    
    public static void main(String[] argv)
    {
        CombatFrame app = new CombatFrame();
        app.setSize(640, 480);
        app.setVisible(true);
    }
}
