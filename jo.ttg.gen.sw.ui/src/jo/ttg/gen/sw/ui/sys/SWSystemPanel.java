package jo.ttg.gen.sw.ui.sys;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import jo.ttg.gen.sw.logic.RuntimeLogic;
import jo.util.ui.swing.utils.ComponentUtils;
import jo.util.utils.obj.IntegerUtils;

public class SWSystemPanel extends JPanel
{
    private SWSystemTools   mTools;
    private JPanel          mClient;
    private JSplitPane      mPane;
    private SWSystemViewer  mClient1;
    private SWSystemList    mClient2;
    private SWWorldDetails mDetails;

    public SWSystemPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
        updateDisplay();
    }

    private void initInstantiate()
    {
        mTools = new SWSystemTools();
        mClient = new JPanel();
        mClient1 = new SWSystemViewer();
        mClient2 = new SWSystemList();
        mDetails = new SWWorldDetails();
    }

    private void initLayout()
    {
        mClient.setLayout(new CardLayout());
        mClient.add("viewer", mClient1);
        mClient.add("list", mClient2);

        mPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mClient,
                mDetails);
        if (RuntimeLogic.isSetting("systemDividerLocation"))
            mPane.setDividerLocation(IntegerUtils.parseInt(RuntimeLogic.getSetting("systemDividerLocation")));
        else
            mPane.setDividerLocation(.75);
        
        setLayout(new BorderLayout());
        add("North", mTools);
        add("Center", mPane);
    }

    private void initLink()
    {
        RuntimeLogic.listen("displayList", (ov,nv) -> updateDisplay());
        ComponentUtils.componentResized(mClient, (ev) -> RuntimeLogic.getInstance().getSettings().put("systemDividerLocation", mPane.getDividerLocation()));
    }
    
    public void updateDisplay()
    {
        if (RuntimeLogic.getInstance().isDisplayList())
            ((CardLayout)mClient.getLayout()).show(mClient, "list");
        else
            ((CardLayout)mClient.getLayout()).show(mClient, "viewer");
    }
}
