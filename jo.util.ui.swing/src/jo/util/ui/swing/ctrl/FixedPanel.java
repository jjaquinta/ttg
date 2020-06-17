package jo.util.ui.swing.ctrl;
/*
 * Created on Mar 8, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FixedPanel extends JPanel
{

    /* (non-Javadoc)
     * @see java.awt.Component#setSize(int, int)
     */
    public void setSize(int arg0, int arg1)
    {
        super.setSize(arg0, arg1);
        setPreferredSize(new Dimension(arg0, arg1));
    }

}
