package jo.ttg.gen.imp.ui.swing.ui.sec;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.util.ui.swing.utils.ComponentUtils;
import jo.util.utils.obj.IntegerUtils;

public class IMPSectorPanel extends JPanel
{
    private IMPSectorTools   mTools;
    private JSplitPane         mPane;
    private IMPSectorViewer  mClient;
    private IMPSectorDetails mSecDetails;
    private IMPSubSectorDetails mSubDetails;

    public IMPSectorPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mTools = new IMPSectorTools();
        mClient = new IMPSectorViewer();
        mSecDetails = new IMPSectorDetails();
        mSubDetails = new IMPSubSectorDetails();
        JPanel details = new JPanel(new GridLayout(2,1));
        details.add(mSecDetails);
        details.add(mSubDetails);
        mPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mClient,
                details);
        if (RuntimeLogic.isSetting("sectorDividerLocation"))
            mPane.setDividerLocation(IntegerUtils.parseInt(RuntimeLogic.getSetting("sectorDividerLocation")));
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
        ComponentUtils.componentResized(mClient, (ev) -> RuntimeLogic.getInstance().getSettings().put("sectorDividerLocation", mPane.getDividerLocation()));
    }
}
