package jo.ttg.gen.sw.ui.sub;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.gen.sw.data.SWMainWorldBean;
import jo.ttg.gen.sw.data.SWSystemBean;
import jo.ttg.gen.sw.logic.IconLogic;
import jo.ttg.gen.sw.logic.RuntimeLogic;
import jo.ttg.logic.sys.SystemLogic;
import jo.util.ui.swing.ctrl.AnimatedIcon;
import jo.util.ui.swing.utils.ComponentUtils;
import jo.util.utils.obj.IntegerUtils;

public class SWSubSectorPanel extends JPanel
{
    private SWSubSectorTools   mTools;
    private JSplitPane         mPane;
    private SWSubSectorViewer  mClient;
    private SWMainWorldDetails mDetails;
    private JLabel             mSurface;
    private AnimatedIcon       mSurfaceIcon;

    public SWSubSectorPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
        doUpdateSurface();
    }

    private void initInstantiate()
    {
        mTools = new SWSubSectorTools();
        mClient = new SWSubSectorViewer();
        mDetails = new SWMainWorldDetails();
        mSurface = new JLabel();
        mSurfaceIcon = new AnimatedIcon(mSurface);
        mSurface.setIcon(mSurfaceIcon);
        JPanel info = new JPanel();
        info.setLayout(new GridLayout(2,1));
        info.add(mDetails);
        info.add(mSurface);
        mPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mClient,
                info);
        if (RuntimeLogic.isSetting("subsectorDividerLocation"))
            mPane.setDividerLocation(IntegerUtils.parseInt(RuntimeLogic.getSetting("subsectorDividerLocation")));
        else
            mPane.setDividerLocation(.75);
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("North", mTools);
        add("Center", mPane);
    }

    private void initLink()
    {
        ComponentUtils.componentResized(mClient, (ev) -> RuntimeLogic.getInstance().getSettings().put("subsectorDividerLocation", mPane.getDividerLocation()));
        RuntimeLogic.listen("cursorMainWorld", (ov,nv) -> doUpdateSurface());
    }
    
    private static final Map<String,String> mURIMap = new HashMap<>();
    
    private void doUpdateSurface()
    {
        mSurfaceIcon.stop();
        mSurfaceIcon.replaceIcon();
        SWMainWorldBean mw = RuntimeLogic.getInstance().getCursorMainWorld();
        if (mw == null)
            return;
        if (!mURIMap.containsKey(mw.getURI()))
        {
            SWSystemBean sys = (SWSystemBean)SystemLogic.getFromOrds(mw.getOrds());
            BodyBean b = SystemLogic.findMainworld(sys);
            mURIMap.put(mw.getURI(), b.getURI());
        }
        List<BufferedImage> icons = IconLogic.getIcons(mURIMap.get(mw.getURI()));
        if (icons == null)
            return;
        for (BufferedImage img : icons)
            mSurfaceIcon.addIcon(new ImageIcon(img));
        mSurfaceIcon.start();
    }
}
