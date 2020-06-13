/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.mw.UPPBean;
import jo.util.ui.swing.TableLayout;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PopulatedStatsPanel extends JPanel
{
	private	PopulatedStatsBean	mStats;
	
	private JTextArea	mPort;
	private JTextArea	mSize;
	private JTextArea	mAtmos;
	private JTextArea	mHydro;
	private JTextArea	mPop;
	private JTextArea	mGov;
	private JTextArea	mLaw;
	private JTextArea	mTech;
	private	JTextArea	mBases;
	private	JTextArea	mZone;
	private	JTextArea	mAll;
	
	public PopulatedStatsPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mPort = newJTextArea();
		mSize = newJTextArea();
		mAtmos = newJTextArea();
		mHydro = newJTextArea();
		mPop = newJTextArea();
		mGov = newJTextArea();
		mLaw = newJTextArea();
		mTech = newJTextArea();
		mBases = newJTextArea();
		mZone = newJTextArea();
		mAll = newJTextArea();
	}
	
	public static JTextArea newJTextArea()
	{
		JTextArea ret = new JTextArea();
		ret.setEditable(false);
		ret.setLineWrap(true);
		ret.setWrapStyleWord(true);
		return ret;
	}
	
	private void initLayout()
	{
		setLayout(new TableLayout());
		add("1,+ anchor=nw", new JLabel("Port:"));
		add("+,. fill=hv", mPort);
		add("1,+ anchor=nw", new JLabel("Size:"));
		add("+,. fill=hv", mSize);
		add("1,+ anchor=nw", new JLabel("Atmosphere:"));
		add("+,. fill=hv", mAtmos);
		add("1,+ anchor=nw", new JLabel("Hydrosphere:"));
		add("+,. fill=hv", mHydro);
		add("1,+ anchor=nw", new JLabel("Population:"));
		add("+,. fill=hv", mPop);
		add("1,+ anchor=nw", new JLabel("Government:"));
		add("+,. fill=hv", mGov);
		add("1,+ anchor=nw", new JLabel("Law Level:"));
		add("+,. fill=hv", mLaw);
		add("1,+ anchor=nw", new JLabel("Tech Level:"));
		add("+,. fill=hv", mTech);
		add("1,+ anchor=nw", new JLabel("Bases:"));
		add("+,. fill=hv", mBases);
		add("1,+ anchor=nw", new JLabel("Travel Zone:"));
		add("+,. fill=hv", mZone);
		add("1,+ anchor=nw", new JLabel("Alliegence:"));
		add("+,. fill=hv", mAll);
	}
	private void initLink()
	{
	}
	
	public void setStats(PopulatedStatsBean stats)
	{
		mStats = stats;
		UPPBean upp = mStats.getUPP();
		mPort.setText(upp.getPort().getValueDescription());
		mSize.setText(upp.getSize().getValueDescription());
		mAtmos.setText(upp.getAtmos().getValueDescription());
		mHydro.setText(upp.getHydro().getValueDescription());
		mPop.setText(upp.getPop().getValueDescription());
		mGov.setText(upp.getGov().getValueDescription());
		mLaw.setText(upp.getLaw().getValueDescription());
		mTech.setText(upp.getTech().getValueDescription());
		mBases.setText(mStats.getBasesDesc());
		mZone.setText(mStats.getTravelZoneDesc());
		mAll.setText(mStats.getAllegiance());
	}
}
