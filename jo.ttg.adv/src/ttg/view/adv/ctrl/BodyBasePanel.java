/*
 * Created on Feb 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.adv.ctrl;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import jo.ttg.core.ui.swing.ctrl.BodyView;
import jo.ttg.core.ui.swing.ctrl.PopulatedStatsPanel;
import jo.util.ui.swing.TableLayout;
import ttg.beans.adv.BodySpecialAdvBean;

/**
 * @author jgrant
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class BodyBasePanel extends JPanel
{
    protected static final int T_NAME    = 0x01;
    protected static final int T_NOTES   = 0x02;
    protected static final int T_DOCKING = 0x04;
    protected static final int T_FUEL    = 0x08;
    protected static final int T_REPAIR  = 0x10;

    private BodySpecialAdvBean mBody;

    private JLabel             mNameLabel;
    private JTextArea          mName;
    private JTextArea          mNotes;
    private JTextArea          mDocking;
    private JTextArea          mFuel;
    private JTextArea          mRepair;

    public BodyBasePanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        int type = getType();
        if ((type & T_NAME) != 0)
        {
            mNameLabel = new JLabel("Name:");
            mName = PopulatedStatsPanel.newJTextArea();
        }
        if ((type & T_NOTES) != 0)
        {
            mNotes = PopulatedStatsPanel.newJTextArea();
        }
        if ((type & T_DOCKING) != 0)
        {
            mDocking = PopulatedStatsPanel.newJTextArea();
        }
        if ((type & T_FUEL) != 0)
        {
            mFuel = PopulatedStatsPanel.newJTextArea();
        }
        if ((type & T_REPAIR) != 0)
        {
            mRepair = PopulatedStatsPanel.newJTextArea();
        }
    }

    private void initLayout()
    {
        setLayout(new TableLayout());
        int type = getType();
        if ((type & T_NAME) != 0)
        {
            add("1,+ anchor=nw", mNameLabel);
            add("+,. fill=hv", mName);
        }
        if ((type & T_NOTES) != 0)
        {
            add("1,+ anchor=nw", new JLabel("Notes:"));
            add("+,. fill=hv", mNotes);
        }
        if ((type & T_DOCKING) != 0)
        {
            add("1,+ anchor=nw", new JLabel("Docking:"));
            add("+,. fill=hv", mDocking);
        }
        if ((type & T_FUEL) != 0)
        {
            add("1,+ anchor=nw", new JLabel("Fuel:"));
            add("+,. fill=hv", mFuel);
        }
        if ((type & T_REPAIR) != 0)
        {
            add("1,+ anchor=nw", new JLabel("Repair:"));
            add("+,. fill=hv", mRepair);
        }
    }

    private void initLink()
    {
    }

    protected abstract int getType();

    protected abstract String getNotes();

    protected abstract String getDocking();

    protected abstract String getFuel();

    protected abstract String getRepair();

    /**
     * @param bean
     */
    public void setBody(BodySpecialAdvBean bean)
    {
        mBody = bean;
        int type = getType();
        if ((type & T_NAME) != 0)
        {
            mNameLabel.setIcon(BodyView.getIcon(mBody));
            mName.setText(mBody.getName());
        }
        if ((type & T_NOTES) != 0)
        {
            mNotes.setText(getNotes());
        }
        if ((type & T_DOCKING) != 0)
        {
            mDocking.setText(getDocking());
        }
        if ((type & T_FUEL) != 0)
        {
            mFuel.setText(getFuel());
        }
        if ((type & T_REPAIR) != 0)
        {
            mRepair.setText(getRepair());
        }
    }
    public BodySpecialAdvBean getBody()
    {
        return mBody;
    }
    
    protected String getDemandProduction()
    {
        StringBuffer sb = new StringBuffer();
        if (mBody.getDemandGood() != null)
            sb.append(" Importer of "+mBody.getDemandGood().getName()+".");
        if (mBody.getProductionGood() != null)
            sb.append(" Exporter of "+mBody.getProductionGood().getName()+".");
        return sb.toString().trim();
    }
}