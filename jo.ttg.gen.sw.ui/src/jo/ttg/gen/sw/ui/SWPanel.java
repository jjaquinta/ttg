package jo.ttg.gen.sw.ui;

import java.awt.CardLayout;

import javax.swing.JPanel;

import jo.ttg.gen.sw.data.RuntimeBean;
import jo.ttg.gen.sw.logic.RuntimeLogic;
import jo.ttg.gen.sw.ui.sub.SWSubSectorPanel;
import jo.ttg.gen.sw.ui.surf.SWSurfacePanel;
import jo.ttg.gen.sw.ui.sys.SWSystemPanel;

public class SWPanel extends JPanel
{
    private SWSubSectorPanel mSubsector;
    private SWSystemPanel    mSystem;
    private SWSurfacePanel  mSurface;

    public SWPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
        updateDisplay();
    }

    private void initInstantiate()
    {
        mSubsector = new SWSubSectorPanel();
        mSystem = new SWSystemPanel();
        mSurface = new SWSurfacePanel();
    }

    private void initLayout()
    {
        setLayout(new CardLayout());
        add("subsector", mSubsector);
        add("system", mSystem);
        add("surface", mSurface);
    }

    private void initLink()
    {
        RuntimeLogic.listen("zoom", (ov,nv) -> updateDisplay());
    }
    
    private void updateDisplay()
    {
        switch (RuntimeLogic.getInstance().getZoom())
        {
            case RuntimeBean.ZOOM_SUBSECTOR:
                ((CardLayout)getLayout()).show(this, "subsector");
                break;
            case RuntimeBean.ZOOM_SYSTEM:
                ((CardLayout)getLayout()).show(this, "system");
                break;
            case RuntimeBean.ZOOM_SURFACE:
                ((CardLayout)getLayout()).show(this, "surface");
                break;
        }
    }
}
