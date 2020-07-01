package jo.ttg.deckplans.ui.deck;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jo.ttg.deckplans.logic.RuntimeLogic;

public class DeckViewer extends JPanel
{
    private int             mDeck;

    private JScrollPane     mScroller;
    private JLabel          mClient;
    
    public DeckViewer()
    {
        initInstantiate();
        initLink();
        initLayout();
    }
    
    private void initInstantiate()
    {
        mClient = new JLabel();
        mScroller = new JScrollPane(mClient);
    }

    private void initLayout()
    {
        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new FlowLayout());
        
        setLayout(new BorderLayout());
        add("Center", mScroller);
        add("North", buttonBar);
    }
    
    private void initLink()
    {
        RuntimeLogic.listen("shipDeck", (ov,nv) -> doRefreshImage());
    }       
    
    public void doUp()
    {
        if (mDeck > 0)
            mDeck--;
        doRefreshLevel();
    }
    
    public void doDown()
    {        
        if (mDeck < RuntimeLogic.getInstance().getShipDeck().getDecks().size() - 1)
            mDeck++;
        doRefreshLevel();
    }
    
    private void doRefreshImage()
    {
        if (mDeck >= RuntimeLogic.getInstance().getShipDeck().getDecks().size())
            mDeck = 0;
        doRefreshLevel();
    }
    
    private void doRefreshLevel()
    {
        BufferedImage deck = RuntimeLogic.getInstance().getShipDeck().getDecks().get(mDeck);
        mClient.setIcon(new ImageIcon(deck));
        mScroller.invalidate();
    }

    public int getDeck()
    {
        return mDeck;
    }

    public void setDeck(int deck)
    {
        mDeck = deck;
        doRefreshLevel();
    }
}
