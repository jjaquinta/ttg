package jo.ttg.gen.imp.ui.swing.ui.sys;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.util.ui.swing.utils.ComponentUtils;
import jo.util.utils.obj.IntegerUtils;

public class IMPSystemPanel extends JPanel
{
    private IMPSystemTools   mTools;
    private JPanel          mClient;
    private JSplitPane      mPane;
    private IMPSystemViewer  mClient1;
    private IMPSystemList    mClient2;
    private IMPWorldDetails mDetails;

    public IMPSystemPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
        updateDisplay();
    }

    private void initInstantiate()
    {
        mTools = new IMPSystemTools();
        mClient = new JPanel();
        mClient1 = new IMPSystemViewer();
        mClient2 = new IMPSystemList();
        mDetails = new IMPWorldDetails();
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
