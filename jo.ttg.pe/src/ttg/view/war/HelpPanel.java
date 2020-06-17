package ttg.view.war;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.util.ui.swing.utils.ListenerUtils;
import ttg.logic.war.IconLogic;

public class HelpPanel extends JPanel
{
	protected WarPanel	mPanel;
	
	private JButton		mHelp;
	private String		mTitle;
	private String		mHelpPage;

	protected JPanel makeTitle(String title, String helpPage)
	{
		mTitle = title;
		mHelpPage = helpPage;
		mHelp = new WarButton(IconLogic.mButtonHelp);

		ListenerUtils.listen(mHelp, (ev) -> doHelp());

		JLabel lTitle = new JLabel(title);
		lTitle.setFont(WarPanel.TITLE_FONT);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add("Center", lTitle);
		p.add("East", mHelp);
		return p;
	}
	
	private void doHelp()
	{
		mPanel.doHelp(mHelpPage);
	}
}
