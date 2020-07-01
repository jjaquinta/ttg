package jo.ttg.deckplans.ui.deck;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class DeckPanel extends JPanel
{
    private DeckToolbar mToolbar;
    private DeckViewer  mClient;

    public DeckPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mClient = new DeckViewer();
        mToolbar = new DeckToolbar(mClient);
    }

    private void initLink()
    {
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("North", mToolbar);
        add("Center", mClient);
    }
}
