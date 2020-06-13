package jo.ttg.gen.sw.ui.surf;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SWSurfacePanel extends JPanel
{
    private SWSurfaceViewer  mPanel;
    private SWSurfaceTools   mTools;
    
    public SWSurfacePanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        setMinimumSize(new Dimension(256, 256));
        setMaximumSize(new Dimension(2560, 2560));
        mPanel = new SWSurfaceViewer();
        mTools = new SWSurfaceTools();
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("North", mTools);
        add("Center", new JScrollPane(mPanel));
    }

    private void initLink()
    {
    }
}
