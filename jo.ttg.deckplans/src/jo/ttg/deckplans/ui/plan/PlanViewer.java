package jo.ttg.deckplans.ui.plan;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jo.ttg.deckplans.logic.RuntimeLogic;

public class PlanViewer extends JPanel
{
    private ShipSquarePanel mClient;
    
    public PlanViewer()
    {
        initInstantiate();
        initLink();
        initLayout();
        doUpdatePlan();
    }
    
    private void initInstantiate()
    {
        mClient = new ShipSquarePanel();
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("Center", new JScrollPane(mClient));
    }
    
    private void initLink()
    {
        RuntimeLogic.listen("shipPlan", (ov,nv) -> doUpdatePlan());
    }       
    
    private void doUpdatePlan()
    {
        mClient.setSquares(RuntimeLogic.getInstance().getShipPlan().getSquares());
        mClient.setSettings(RuntimeLogic.getInstance().getShipDeck().getImageSettings());
        mClient.setDeck(1);
    }
    
    public int getDeck()
    {
        return mClient.getDeck();
    }
    
    public void setDeck(int deck)
    {
        mClient.setDeck(deck);
    }
    
    public int getDecks()
    {
        return mClient.getDecks();
    }
}
