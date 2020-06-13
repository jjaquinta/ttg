package jo.ttg.core.ui.swing.ship;
/*
 * Created on Dec 9, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jo.ttg.ship.beans.ShipStats;
import jo.ttg.ship.beans.ShipStatsError;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.logic.FontUtils;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ShipStatsPanel extends JPanel
{
    private ShipStats   mStats;
    
    private JTextArea    mCraftID;
    private JTextArea    mHull;
    private JTextArea    mPower;
    private JTextArea    mLoco;
    private JTextArea    mCommo;
    private JTextArea    mSensors;
    private JTextArea    mOff;
    private JTextArea    mDef;
    private JTextArea    mControl;
    private JTextArea    mAccomm;
    private JTextArea    mOther;
    private JLabel   mErrorLabel;
    private JList    mErrors;
    private JTextArea    mErrorDesc;
    
    public ShipStatsPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }


    /**
     * 
     */
    protected void initInstantiate()
    {
        Font def = getFont();
        Font fixed = FontUtils.getFont("Courier", def.getSize() - 2, def.getStyle());
        mCraftID = newJTextArea();
        mHull = newJTextArea();
        mPower = newJTextArea();
        mLoco = newJTextArea();
        mCommo = newJTextArea();
        mSensors = newJTextArea();
        mOff = newJTextArea();
        mOff.setFont(fixed);
        mDef = newJTextArea();
        mDef.setFont(fixed);
        mControl = newJTextArea();
        mAccomm = newJTextArea();
        mOther = newJTextArea();
        mErrorLabel = new JLabel("Errors:");
        mErrors = new JList(new DefaultListModel());
        mErrorDesc = newJTextArea();
    }

    /**
     * 
     */
    protected void initLayout()
    {
        setLayout(new TableLayout());
        add("1,. anchor=nw", new JLabel("CraftID:"));
        add("+,. fill=hv", mCraftID);
        add("1,+ anchor=nw", new JLabel("Hull:"));
        add("+,. fill=hv", mHull);
        add("1,+ anchor=nw", new JLabel("Power:"));
        add("+,. fill=hv", mPower);
        add("1,+ anchor=nw", new JLabel("Loco:"));
        add("+,. fill=hv", mLoco);
        add("1,+ anchor=nw", new JLabel("Commo:"));
        add("+,. fill=hv", mCommo);
        add("1,+ anchor=nw", new JLabel("Sensors:"));
        add("+,. fill=hv", mSensors);
        add("1,+ anchor=nw", new JLabel("Off:"));
        add("+,. fill=hv", mOff);
        add("1,+ anchor=nw", new JLabel("Def:"));
        add("+,. fill=hv", mDef);
        add("1,+ anchor=nw", new JLabel("Control:"));
        add("+,. fill=hv", mControl);
        add("1,+ anchor=nw", new JLabel("Accomm:"));
        add("+,. fill=hv", mAccomm);
        add("1,+ anchor=nw", new JLabel("Other:"));
        add("+,. fill=hv", mOther);
        add("1,+ anchor=nw", mErrorLabel);
        add("+,. fill=hv", mErrors);
        add("2,+", mErrorDesc);
    }

    /**
     * 
     */
    protected void initLink()
    {
        mErrors.addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                doErrorChange();
            }
        });
    }
    
    private JTextArea newJTextArea()
    {
        JTextArea ctrl = new JTextArea();
        ctrl.setEditable(false);
        ctrl.setLineWrap(true);
        return ctrl;
    }

    /**
     * @return
     */
    public ShipStats getStats()
    {
        return mStats;
    }

    /**
     * @param stats
     */
    public void setStats(ShipStats stats)
    {
        mStats = stats;
        if (mStats == null)
        {
            setVisible(false);
            mCraftID.setText("");
            mHull.setText("");
            mPower.setText("");
            mLoco.setText("");
            mCommo.setText("");
            mSensors.setText("");
            mOff.setText("");
            mDef.setText("");
            mControl.setText("");
            mAccomm.setText("");
            mOther.setText("");
            mErrors.removeAll();
        }
        else
        {
            mCraftID.setText(mStats.sCraftID());
            mHull.setText(mStats.sHull());
            mPower.setText(mStats.sPower());
            mLoco.setText(mStats.sLoco());
            mCommo.setText(mStats.sCommo());
            mSensors.setText(mStats.sSensors());
            //String off = mStats.sOff();
            mOff.setText(mStats.sOff());
            mDef.setText(mStats.sDef());
            mControl.setText(mStats.sControl());
            mAccomm.setText(mStats.sAccomm());
            mOther.setText(mStats.sOther());
            mErrors.removeAll();
            for (ShipStatsError err : mStats.getErrors())
                ((DefaultListModel)mErrors.getModel()).addElement(err.getErrorMessage());
            setVisible(true);
        }
    }

    private void doErrorChange()
    {        
        ShipStatsError err = getError();
        if (err == null)
            mErrorDesc.setText("");
        else
            mErrorDesc.setText(err.getDescription());
    }
    
    public ShipStatsError getError()
    {
        return (ShipStatsError)mStats.getErrors().get(mErrors.getSelectedIndex());
    }
    
    public void setDisplayErrors(boolean visible)
    {
        mErrorLabel.setVisible(visible);
        mErrors.setVisible(visible);
        mErrorDesc.setVisible(visible);
    }
}
