/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.URIBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.gen.IGenScheme;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class LocationSelectDlg extends JDialog implements TTGActionListener
{
	public static final int	ANY = 0;
	public static final int	SEC = 1;
	public static final int	SUB = 2;
	public static final int	HEX = 3;
	public static final int	SYS = 4;
	
	private IGenScheme	mScheme;
	private int 		mLocationType;
	private String		mLocationURI;
	private URIBean		mLocationObj;
	private boolean		mAccepted;
	
	private JLabel	mStatus;
	private JButton	mOK;
	private JButton	mCancel;
	private JButton	mZoomIn;
	private JButton	mZoomOut;
	private int		mZoomLevel;
	
	private CardLayout		mLayout;
	private JPanel			mClient;
	private SubsectorField	mSecSelect;
	private HexField		mSubSelect;
	private HexField		mHexSelect;
	private SystemPanel		mSysSelect;
	
	private static final String[] mZoomNames =
		{ "sec", "sub", "hex", "sys" };
	
	public LocationSelectDlg(JFrame frame, IGenScheme scheme)
	{
		super(frame, "Select Location", true);
		mScheme = scheme;
		mAccepted = false;
		initInstantiate();
		initLink();
		initLayout();
	}

	/**
	 * 
	 */
	private void initInstantiate()
	{
		mStatus = new JLabel();
		mOK = new JButton("OK");
		adjustOK();
		mCancel = new JButton("Cancel");
		mZoomIn = new JButton("Zoom In");
		mZoomOut = new JButton("Zoom Out");
		
		mSysSelect = new SystemPanel(mScheme);
		mSysSelect.setForeground(Color.YELLOW);
		mSysSelect.setBackground(Color.BLACK);

		mHexSelect = new HexField(mScheme);
		mHexSelect.setHexesHigh(10);
		mHexSelect.setHexesWide(8);
		mHexSelect.setForeColor(Color.YELLOW);
		mHexSelect.setBackground(Color.BLACK);
		mHexSelect.setFocusedColor(Color.WHITE);
		mHexSelect.setDisabledColor(Color.LIGHT_GRAY);

		mSubSelect = new HexField(mScheme);
		mSubSelect.setHexesHigh(40);
		mSubSelect.setHexesWide(32);
		mSubSelect.setHexSide(8);
		mSubSelect.setForeColor(Color.YELLOW);
		mSubSelect.setBackground(Color.BLACK);
		mSubSelect.setFocusedColor(Color.WHITE);
		mSubSelect.setDisabledColor(Color.LIGHT_GRAY);
		
		mSecSelect = new SubsectorField(mScheme);
		
		mLayout = new CardLayout();
		mClient = new JPanel();
		mClient.setBackground(Color.BLACK);
	}


	/**
	 * 
	 */
	private void initLink()
	{
		mSecSelect.addTTGActionListener(this);
		mSubSelect.addTTGActionListener(this);
		mHexSelect.addTTGActionListener(this);
		mSysSelect.addTTGActionListener(this);
		mOK.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				doOK();
			}
		});
		mCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				doCancel();
			}
		});
		mZoomIn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				doZoomIn();
			}
		});
		mZoomOut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				doZoomOut();
			}
		});
	}

	/**
	 * 
	 */
	private void initLayout()
	{
		JPanel buttonBar1 = new JPanel();
		buttonBar1.add(mOK);
		buttonBar1.add(mCancel);
		JPanel buttonBar2 = new JPanel();
		buttonBar2.add(mZoomIn);
		buttonBar2.add(mZoomOut);
		JPanel buttonBar3 = new JPanel();
		buttonBar3.setLayout(new BorderLayout());
		buttonBar3.add("Center", mStatus);
		buttonBar3.add("East", buttonBar2);
		
		mClient.setLayout(mLayout);
		mClient.add(mZoomNames[0], mSecSelect);
		mClient.add(mZoomNames[1], mSubSelect);
		mClient.add(mZoomNames[2], mHexSelect);
		mClient.add(mZoomNames[3], mSysSelect);
		mLayout.show(mClient, String.valueOf(mZoomLevel));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", mClient);
		getContentPane().add("South", buttonBar1);
		getContentPane().add("North", buttonBar3);
		setSize(512, 720);
	}
	
	private void doOK()
	{
		mAccepted = mLocationObj != null;
		dispose();
	}
	
	private void doCancel()
	{
		mLocationURI = null;
		mLocationObj = null;
		dispose();
	}
	
	private void doZoomIn()
	{
		setZoomLevel(getZoomLevel() + 1);
	}
	
	private void doZoomOut()
	{
		setZoomLevel(getZoomLevel() - 1);
	}
	
	private void adjustOK()
	{
		boolean todo = false;
		switch (mLocationType)
		{
			case ANY:
				todo = mLocationObj != null;
				break;
			case SUB:
				todo = mLocationObj instanceof SubSectorBean;
				break;
			case HEX:
				todo = mLocationObj instanceof MainWorldBean;
				break;
			case SYS:
				todo = mLocationObj instanceof BodyBean;
				break;
		}
		mOK.setEnabled(todo);
	}
	
	/**
	 * @return
	 */
	public String getLocationURI()
	{
		return mLocationURI;
	}

	/**
	 * @return
	 */
	public int getLocationType()
	{
		return mLocationType;
	}

	/**
	 * @param string
	 */
	public void setLocationURI(String string)
	{
		mLocationURI = string;
	}

	/**
	 * @param i
	 */
	public void setLocationType(int i)
	{
		mLocationType = i;
		adjustOK();
	}

	/**
	 * @return
	 */
	public URIBean getLocationObj()
	{
		return mLocationObj;
	}

	/**
	 * @param bean
	 */
	public void setLocationObj(URIBean bean)
	{
		SectorBean sec = null;
		SubSectorBean sub = null;
		MainWorldBean mw = null;
		BodyBean body = null;
		mLocationObj = bean;
		setLocationURI(mLocationObj.getURI());
		if (mLocationObj instanceof SubSectorBean)
		{
			sub = (SubSectorBean)mLocationObj;
			OrdBean o = sub.getUpperBound();
			mHexSelect.setFocus(o);
			mSubSelect.setFocus(o);
			OrdBean seco = new OrdBean(o);
			mScheme.nearestSec(seco);
			sec = mScheme.getGeneratorSector().generateSector(seco);
		}
		else if (mLocationObj instanceof MainWorldBean)
		{
			mw = (MainWorldBean)mLocationObj;
			OrdBean o = mw.getOrds();
			OrdBean subo = new OrdBean(o);
			mScheme.nearestSub(subo);
			mSecSelect.setSelected(subo);
			sub = mScheme.getGeneratorSubSector().generateSubSector(subo);
			mSubSelect.setFocus(o);
			mHexSelect.setFocus(o);
			OrdBean seco = new OrdBean(o);
			mScheme.nearestSec(seco);
			sec = mScheme.getGeneratorSector().generateSector(seco);
			mSysSelect.setOrigin(o);
		}
		else if (mLocationObj instanceof BodyBean)
		{
			body = (BodyBean)mLocationObj;
			OrdBean o = body.getSystem().getOrds();
			mw = mScheme.getGeneratorMainWorld().generateMainWorld(o);
			OrdBean subo = new OrdBean(o);
			mScheme.nearestSub(subo);
			mSecSelect.setSelected(subo);
			sub = mScheme.getGeneratorSubSector().generateSubSector(subo);
			mSubSelect.setFocus(o);
			mHexSelect.setFocus(o);
			OrdBean seco = new OrdBean(o);
			mScheme.nearestSec(seco);
			sec = mScheme.getGeneratorSector().generateSector(seco);
			mSysSelect.setOrigin(o);
		}
		StringBuffer sb = new StringBuffer();
		if (sec != null)
		{
			sb.append(sec.getName());
			if (sub != null)
			{
				sb.append(" / ");
				sb.append(sub.getName());
				if (mw != null)
				{
					sb.append(" / ");
					sb.append(mw.getName());
					if (body != null)
					{
						sb.append(" / ");
						sb.append(body.getName());
					}
				}
			}
		}
		mStatus.setText(sb.toString());
		adjustOK();
	}

	/* (non-Javadoc)
	 * @see ttg.view.ctrl.TTGActionListener#actionPerformed(ttg.view.ctrl.TTGActionEvent)
	 */
	public void actionPerformed(TTGActionEvent e)
	{
		if (e.getID() == TTGActionEvent.SELECTED)
			setLocationObj((URIBean)e.getObject());
		else if (e.getID() == TTGActionEvent.ACTIVATED)
			doZoomIn();
		else if (e.getID() == TTGActionEvent.DEACTIVATED)
			doZoomOut();
		//System.out.println(e.toString());		
	}

	/**
	 * @return
	 */
	public boolean isAccepted()
	{
		return mAccepted;
	}

	/**
	 * @param b
	 */
	public void setAccepted(boolean b)
	{
		mAccepted = b;
	}

	/**
	 * @return
	 */
	public int getZoomLevel()
	{
		return mZoomLevel;
	}

	/**
	 * @param i
	 */
	public void setZoomLevel(int i)
	{
		if ((i < 0) || (i > mZoomNames.length - 1))
			return;
		mZoomLevel = i;
		mLayout.show(mClient, mZoomNames[mZoomLevel]);
	}

}
