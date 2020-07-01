package jo.ttg.deckplans.ui.scan;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class ScanPanel extends JPanel
{
    private ScanToolbar mToolbar;
    private ScanViewer  mClient;

	public ScanPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    mToolbar = new ScanToolbar();
	    mClient = new ScanViewer();
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
