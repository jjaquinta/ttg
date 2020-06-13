/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
//public class SystemTreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer
public class SystemTreeCellRenderer implements TreeCellRenderer
{
    private Map<Object,BodyView>	mCache;
    
    public SystemTreeCellRenderer()
    {
        mCache = new HashMap<>();
    }
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(
		JTree tree,
		Object obj,
		boolean sel,
		boolean expanded,
		boolean leaf,
		int row,
		boolean hasFocus)
	{
	    BodyView ret = mCache.get(obj);
	    if (ret == null)
	    {
		    ret = new BodyView();
		    mCache.put(obj, ret);
		    ret.setDrawBorder(false);
		    if (obj instanceof SystemTreeNode)
		        ret.setBody(((SystemTreeNode)obj).getBody());
		    else
		    {
		        ret.setBody(null);
		    }
	    }
        if (sel)
        {
            ret.setForeground(UIManager.getColor("Tree.selectionForeground"));
            ret.setBackground(UIManager.getColor("Tree.selectionBackground"));
        }
        else
        {
            ret.setForeground(UIManager.getColor("Tree.textForeground"));
            ret.setBackground(UIManager.getColor("Tree.textBackground"));
        }
		return ret;
	}
}
