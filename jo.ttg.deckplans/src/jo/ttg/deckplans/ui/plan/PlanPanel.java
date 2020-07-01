package jo.ttg.deckplans.ui.plan;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class PlanPanel extends JPanel
{
    private PlanToolbar mToolbar;
    private PlanViewer  mClient;

    public PlanPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mClient = new PlanViewer();
        mToolbar = new PlanToolbar(mClient);
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
