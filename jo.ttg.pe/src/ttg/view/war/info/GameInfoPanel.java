package ttg.view.war.info;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jo.util.ui.swing.TableLayout;
import ttg.beans.war.Game;
import ttg.logic.war.IconLogic;
import ttg.view.war.HelpPanel;
import ttg.view.war.WarPanel;

public class GameInfoPanel extends HelpPanel
{
	private InfoPanel	mInfo;
	private Game		mGame;

	private JLabel		mName;
	private JLabel		mAuthor;
	private JLabel		mVersion;
	private JTextArea	mDescription;
	private JLabel		mAllowConvertNeutral;
	private JLabel		mAllowOmniscentSensors;
	private JLabel		mAllowFleetReconfiguration;
	private JLabel		mAllowIntrinsicDefense;
	private JLabel		mAllowConstruction;
	
	/**
	 *
	 */

	public GameInfoPanel(WarPanel panel, InfoPanel info)
	{
		mPanel = panel;
		mInfo = info;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mName = new JLabel();
		mAuthor = new JLabel();
		mVersion = new JLabel();
		mDescription = new JTextArea();
		mDescription.setEditable(false);
		mDescription.setLineWrap(true);
		mDescription.setWrapStyleWord(true);
		mAllowConvertNeutral = new JLabel("Allow Convert Neutral");
		mAllowOmniscentSensors = new JLabel("Allow Omniscent Sensors");
		mAllowFleetReconfiguration = new JLabel("Allow Fleet Reconfiguration");
		mAllowIntrinsicDefense = new JLabel("Allow Intrinsic Defense");
		mAllowConstruction = new JLabel("Allow Construction");
	}

	private void initLink()
	{
	}

	private void initLayout()
	{
		setLayout(new TableLayout("anchor=w"));
		add("1,+ 2x1 fill=h", makeTitle("Scenario Info", "InfoGame.htm"));
		add("1,+", new JLabel("Name:"));
		add("1,+ fill=h", mName);
		add("1,+", new JLabel("Author:"));
		add("1,+ fill=h", mAuthor);
		add("1,+", new JLabel("Version:"));
		add("1,+ fill=h", mVersion);
		add("1,+ 2x1 weighty=20 fill=hv", new JScrollPane(mDescription));
		add("1,+ 2x1 fill=h", mAllowConvertNeutral);
		add("1,+ 2x1 fill=h", mAllowOmniscentSensors);
		add("1,+ 2x1 fill=h", mAllowFleetReconfiguration);
		add("1,+ 2x1 fill=h", mAllowIntrinsicDefense);
		add("1,+ 2x1 fill=h", mAllowConstruction);
	}
	
	public void setObject(Game obj)
	{
		mGame = obj;
		if (mGame == null)
		{
			mName.setText(null);
			mAuthor.setText(null);
			mVersion.setText(null);
			mDescription.setText(null);
			mAllowConvertNeutral.setIcon(null);
			mAllowOmniscentSensors.setIcon(null);
			mAllowFleetReconfiguration.setIcon(null);
			mAllowIntrinsicDefense.setIcon(null);
			mAllowConstruction.setIcon(null);
		}
		else
		{
			mName.setText(mGame.getName());
			mAuthor.setText(mGame.getAuthor());
			mVersion.setText(mGame.getVersion());
			mDescription.setText(mGame.getDescription());
			mAllowConvertNeutral.setIcon(mGame.isAllowConvertNeutral() ? IconLogic.mButtonDone : IconLogic.mButtonCancel);
			mAllowOmniscentSensors.setIcon(mGame.isAllowOmniscentSensors() ? IconLogic.mButtonDone : IconLogic.mButtonCancel);
			mAllowFleetReconfiguration.setIcon(mGame.isAllowFleetReconfiguration() ? IconLogic.mButtonDone : IconLogic.mButtonCancel);
			mAllowIntrinsicDefense.setIcon(mGame.isAllowIntrinsicDefense() ? IconLogic.mButtonDone : IconLogic.mButtonCancel);
			mAllowConstruction.setIcon(mGame.isAllowConstruction() ? IconLogic.mButtonDone : IconLogic.mButtonCancel);
		}
	}
}
