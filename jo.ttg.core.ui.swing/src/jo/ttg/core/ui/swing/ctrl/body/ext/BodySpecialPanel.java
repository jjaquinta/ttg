/*
 * Created on Feb 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl.body.ext;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.core.ui.swing.ctrl.BodyView;
import jo.ttg.core.ui.swing.ctrl.PopulatedStatsPanel;
import jo.util.ui.swing.TableLayout;

/**
 * @author jgrant
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BodySpecialPanel extends JPanel
{
    static List<IBodySpecialHandler> mHandlers = new ArrayList<>();
    static
    {
        addHandler(new BodyLabHandler());
        addHandler(new BodyLocalHandler());
        addHandler(new BodyNavyHandler());
        addHandler(new BodyRefineryHandler());
        addHandler(new BodyScoutHandler());
        addHandler(new BodySpaceportHandler());
        addHandler(new BodyStarportHandler());
    }
    
    public static void addHandler(IBodySpecialHandler h)
    {
        mHandlers.add(0, h);
    }
    
    private BodySpecialBean    mBody;
    private IBodySpecialHandler mHandler;

    private JLabel             mNameLabel;
    private JTextArea          mName;
    private JTextArea          mNotes;
    private JTextArea          mDocking;
    private JTextArea          mFuel;
    private JTextArea          mRepair;

    public BodySpecialPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
            mNameLabel = new JLabel("Name:");
            mName = PopulatedStatsPanel.newJTextArea();
            mNotes = PopulatedStatsPanel.newJTextArea();
            mDocking = PopulatedStatsPanel.newJTextArea();
            mFuel = PopulatedStatsPanel.newJTextArea();
            mRepair = PopulatedStatsPanel.newJTextArea();
    }

    private void initLayout()
    {
        setLayout(new TableLayout());
    }

    private void redoLayout()
    {
        while (getComponentCount() > 0)
            remove(getComponent(0));
        if (mHandler == null)
            return;
        int type = mHandler.getType();
        if ((type & IBodySpecialHandler.T_NAME) != 0)
        {
            add("1,+ anchor=nw", mNameLabel);
            add("+,. fill=hv", mName);
        }
        if ((type & IBodySpecialHandler.T_NOTES) != 0)
        {
            add("1,+ anchor=nw", new JLabel("Notes:"));
            add("+,. fill=hv", mNotes);
        }
        if ((type & IBodySpecialHandler.T_DOCKING) != 0)
        {
            add("1,+ anchor=nw", new JLabel("Docking:"));
            add("+,. fill=hv", mDocking);
        }
        if ((type & IBodySpecialHandler.T_FUEL) != 0)
        {
            add("1,+ anchor=nw", new JLabel("Fuel:"));
            add("+,. fill=hv", mFuel);
        }
        if ((type & IBodySpecialHandler.T_REPAIR) != 0)
        {
            add("1,+ anchor=nw", new JLabel("Repair:"));
            add("+,. fill=hv", mRepair);
        }
    }

    private void initLink()
    {
    }

    /**
     * @param bean
     */
    public void setBody(BodySpecialBean bean)
    {
        mBody = bean;
        mHandler = null;
        for (IBodySpecialHandler h : mHandlers)
            if (h.isHandlerFor(mBody))
            {
                mHandler = h;
                break;
            }
        redoLayout();
        if (mHandler == null)
        {
            
        }
        else
        {
            int type = mHandler.getType();
            if ((type & IBodySpecialHandler.T_NAME) != 0)
            {
                mNameLabel.setIcon(BodyView.getIcon(mBody));
                mName.setText(mBody.getName());
            }
            if ((type & IBodySpecialHandler.T_NOTES) != 0)
            {
                mNotes.setText(mHandler.getNotes(mBody));
            }
            if ((type & IBodySpecialHandler.T_DOCKING) != 0)
            {
                mDocking.setText(mHandler.getDocking(mBody));
            }
            if ((type & IBodySpecialHandler.T_FUEL) != 0)
            {
                mFuel.setText(mHandler.getFuel(mBody));
            }
            if ((type & IBodySpecialHandler.T_REPAIR) != 0)
            {
                mRepair.setText(mHandler.getRepair(mBody));
            }
        }
    }
    public BodySpecialBean getBody()
    {
        return mBody;
    }
}