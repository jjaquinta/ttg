package ttg.war.view;

import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import ttg.war.logic.IconLogic;

public class WarButton extends JButton
{

    /**
     *
     */

    public WarButton()
    {
        super();
		setup();
    }

    /**
     *
     */

    public WarButton(Icon icon)
    {
        super(icon);
		setup();
    }

    /**
     *
     */

    public WarButton(String text)
    {
        super(text);
		setup();
    }

    /**
     *
     */

    public WarButton(String text, Icon icon)
    {
        super(text, icon);
        setup();
    }

	private void setup()
	{
		if (getIcon() == IconLogic.mButtonDone)
			setDefaultCapable(true);
		setHorizontalAlignment(SwingConstants.LEADING);
		setMargin(new Insets(0, 0, 0, 0));
	}
}
