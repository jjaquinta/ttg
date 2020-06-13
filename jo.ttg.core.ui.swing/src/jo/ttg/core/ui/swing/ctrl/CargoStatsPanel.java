/*
 * Created on Jan 29, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;


import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.gen.CargoLogic;
import jo.ttg.utils.URIUtils;
import jo.util.ui.swing.TableLayout;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CargoStatsPanel extends JPanel
{
    private IGenScheme	mScheme;
	private CargoBean	mCargo;
	private DateBean	mNow;
	private String		mAt;
	
	private JLabel	mOrigin;
	private JLabel	mDestination;
    private JLabel	mType;
    private JLabel	mClassification;
    private JLabel	mName;
    private JLabel	mLegality;
    private JLabel	mToBuy;
    private JLabel	mToSell;
    private JLabel	mQuantity;
    private JLabel	mBestBy;
	private JLabel	mTechLevel;
    
	private JLabel mWarnings;

	public CargoStatsPanel()
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
		mOrigin = new JLabel();
		mDestination = new JLabel();
	    mType = new JLabel();
	    mClassification = new JLabel();
	    mName = new JLabel();
	    mLegality = new JLabel();
	    mToBuy = new JLabel();
	    mToSell = new JLabel();
	    mQuantity = new JLabel();
	    mBestBy = new JLabel();
		mTechLevel = new JLabel();
		mWarnings = new JLabel();
	}


	/**
	 * 
	 */
	protected void initLink()
	{
	}

	/**
	 * 
	 */
	protected void initLayout()
	{
		setLayout(new TableLayout());
		add("1,+", new JLabel("Class:"));
		add("+,. fill=h", mClassification);
		add("1,+", new JLabel("Name:"));
		add("+,. fill=h", mName);
		add("1,+", new JLabel("Origin:"));
		add("+,. fill=h", mOrigin);
		add("1,+", new JLabel("Destination:"));
		add("+,. fill=h", mDestination);
		add("1,+", new JLabel("Purchase Price:"));
		add("+,. fill=h", mToBuy);
		add("1,+", new JLabel("Sale Price:"));
		add("+,. fill=h", mToSell);
		add("1,+", new JLabel("Size:"));
		add("+,. fill=h", mQuantity);
		add("1,+", new JLabel("Type:"));
		add("+,. fill=h", mType);
		add("1,+", new JLabel("Legality:"));
		add("+,. fill=h", mLegality);
		add("1,+", new JLabel("Tech Level:"));
		add("+,. fill=h", mTechLevel);
		add("1,+", new JLabel("Warnings:"));
		add("+,. fill=h", mWarnings);
		add("1,+", new JLabel("Best By:"));
		add("+,. fill=h", mBestBy);
	}

	/**
	 * @return
	 */
	public CargoBean getCargo()
	{
		return mCargo;
	}

	/**
	 * @param stats
	 */
	public void setCargo(CargoBean stats)
	{
		mCargo = stats;
		if (mCargo == null)
		{
			mOrigin.setText("");
			mDestination.setText("");
		    mType.setText("");
		    mClassification.setText("");
		    mName.setText("");
		    mLegality.setText("");
		    mToBuy.setText("");
		    mToSell.setText("");
		    mQuantity.setText("");
		    mBestBy.setText("");
			mTechLevel.setText("");
		}
		else
		{
			mOrigin.setText(URIUtils.extractName(mCargo.getOrigin()));
		    if (mCargo.getDestination() != null)
		        mDestination.setText(URIUtils.extractName(mCargo.getDestination()));
			else
			    mDestination.setText("-");
		    mType.setText(CargoLogic.getTypeText(mCargo));
		    mClassification.setText(CargoLogic.getClassificationText(mCargo));
		    mName.setText(mCargo.getName());
		    mLegality.setText(String.valueOf(mCargo.getLegality()));
		    mToBuy.setText(FormatUtils.sCurrency(CargoLogic.purchasePrice(mCargo, mScheme)));
		    mToSell.setText(FormatUtils.sCurrency(CargoLogic.salePrice(mCargo, mAt, mNow, mScheme)));
		    mQuantity.setText(String.valueOf(mCargo.getQuantity()));
		    mBestBy.setText(CargoLogic.getClassificationText(mCargo));
			mTechLevel.setText(String.valueOf(mCargo.getTechLevel()));
		}
	}
    public String getAt()
    {
        return mAt;
    }
    public void setAt(String at)
    {
        mAt = at;
    }
    public DateBean getNow()
    {
        return mNow;
    }
    public void setNow(DateBean now)
    {
        mNow = now;
    }
    public IGenScheme getScheme()
    {
        return mScheme;
    }
    public void setScheme(IGenScheme scheme)
    {
        mScheme = scheme;
    }
}
