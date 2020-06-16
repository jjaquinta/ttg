package jo.ttg.gen.imp.ui.swing.ui;

import java.awt.CardLayout;

import javax.swing.JPanel;

import jo.ttg.gen.imp.ui.swing.data.RuntimeBean;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.ttg.gen.imp.ui.swing.ui.sec.IMPSectorPanel;
import jo.ttg.gen.imp.ui.swing.ui.sub.IMPSubSectorPanel;
import jo.ttg.gen.imp.ui.swing.ui.surf.IMPSurfacePanel;
import jo.ttg.gen.imp.ui.swing.ui.sys.IMPSystemPanel;

public class IMPPanel extends JPanel
{
    private IMPSectorPanel    mSector;
    private IMPSubSectorPanel mSubsector;
    private IMPSystemPanel    mSystem;
    private IMPSurfacePanel   mSurface;

    public IMPPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
        updateDisplay();
    }

    private void initInstantiate()
    {
        mSector = new IMPSectorPanel();
        mSubsector = new IMPSubSectorPanel();
        mSystem = new IMPSystemPanel();
        mSurface = new IMPSurfacePanel();
    }

    private void initLayout()
    {
        setLayout(new CardLayout());
        add("sector", mSector);
        add("subsector", mSubsector);
        add("system", mSystem);
        add("surface", mSurface);
    }

    private void initLink()
    {
        RuntimeLogic.listen("zoom", (ov, nv) -> updateDisplay());
    }

    private void updateDisplay()
    {
        switch (RuntimeLogic.getInstance().getZoom())
        {
            case RuntimeBean.ZOOM_SECTOR:
                ((CardLayout)getLayout()).show(this, "sector");
                break;
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
