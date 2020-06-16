package jo.ttg.gen.imp.ui.swing.ui.surf;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class IMPSurfacePanel extends JPanel
{
    private IMPSurfaceViewer  mPanel;
    private IMPSurfaceTools   mTools;
    
    public IMPSurfacePanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        setMinimumSize(new Dimension(256, 256));
        setMaximumSize(new Dimension(2560, 2560));
        mPanel = new IMPSurfaceViewer();
        mTools = new IMPSurfaceTools();
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
