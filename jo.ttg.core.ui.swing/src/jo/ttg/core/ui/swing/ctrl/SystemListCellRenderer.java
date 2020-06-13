/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import jo.ttg.beans.sys.BodyBean;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
//public class SystemTreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer
public class SystemListCellRenderer extends BodyView implements ListCellRenderer<BodyBean>
{
    public SystemListCellRenderer()
    {
        setDrawBorder(false);
    }
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
    @Override
    public Component getListCellRendererComponent(
            JList<? extends BodyBean> list, BodyBean value, int index,
            boolean isSelected, boolean cellHasFocus)
	{
        setBody(value);
	    Color fg, bg;
        if (isSelected)
        {
            fg = UIManager.getColor("Tree.selectionForeground");
            bg = UIManager.getColor("Tree.selectionBackground");
        }
        else
        {
            fg = UIManager.getColor("Tree.textForeground");
            bg = UIManager.getColor("Tree.textBackground");
        }
        setForeground(fg);
        setBackground(bg);
		return this;
	}
}
