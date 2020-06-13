/*
 * Created on Nov 25, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.sys.SystemLogic;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SystemTree extends JTree
{
	private IGenScheme	mScheme;
	private OrdBean		mOrigin;
	private SystemBean  mSystem;
	
	public SystemTree()
	{
		setCellRenderer(new SystemTreeCellRenderer());
	}
    
    public SystemTree(IGenScheme scheme)
    {
        this();
        mScheme = scheme;
    }
	/**
	 * @return
	 */
	public OrdBean getOrigin()
	{
		return mOrigin;
	}

	/**
	 * @param bean
	 */
	public void setOrigin(OrdBean bean)
	{
		mOrigin = bean;
		SystemBean sys = SystemLogic.getFromOrds(mScheme, mOrigin);
		setSystem(sys);
	}
	
	/*
	private TreePath findMainworld(SystemTreeNode[] path, int depth)
	{
	    for (Enumeration<SystemTreeNode> e = path[depth].children(); e.hasMoreElements(); )
	    {
	        path[depth+1] = e.nextElement();
	        if (path[depth+1].getBody().isMainworld())
	        {
	            Object[] p = new Object[depth+2];
	            System.arraycopy(path, 0, p, 0, depth+2);
	            return new TreePath(p);
	        }
	        if (path[depth+1].getAllowsChildren())
	        {
	            TreePath ret = findMainworld(path, depth+1);
	            if (ret != null)
	                return ret;
	        }
	    }
	    return null;
	}
	*/
	
	public void setSelectedBody(BodyBean body)
	{
	    if (body == null)
	    {
	        setSelectionPath(null);
	        return;
	    }
	    if (body == getSelectedBody())
	        return;
	    List<BodyBean> decendency = new ArrayList<BodyBean>();
	    for (BodyBean b = body; b != null; b = b.getPrimary())
	        decendency.add(0, b);
	    SystemTreeNode[] path = new SystemTreeNode[decendency.size()];
	    DefaultTreeModel m = (DefaultTreeModel)getModel();
	    path[0] = (SystemTreeNode)m.getRoot();
	    for (int i = 1; i < path.length; i++)
	    {
	        String decendencyURI = decendency.get(i).getURI();
	        for (Enumeration<SystemTreeNode> e = path[i-1].children(); e.hasMoreElements(); )
	        {
	        	path[i] = e.nextElement();
	        	String uri = path[i].getBody().getURI();
	        	if (uri.equals(decendencyURI))
	        		break;
	        }
	    }
	    setSelectionPath(new TreePath(path));
	}
	
	public BodyBean getSelectedBody()
	{
	    TreePath path = getSelectionPath();
	    if (path == null)
	        return null;
	    SystemTreeNode node = (SystemTreeNode)path.getLastPathComponent();
	    return node.getBody();
	}

    public SystemBean getSystem()
    {
        return mSystem;
    }

    public void setSystem(SystemBean system)
    {
        mSystem = system;
        if (mSystem == null)
            return;
        SystemTreeNode root = new SystemTreeNode(null, mSystem.getSystemRoot());
        setModel(new DefaultTreeModel(root));
        /*
        SystemTreeNode[] path = new SystemTreeNode[8];
        path[0] = root;
        TreePath sel = findMainworld(path, 0);
        if (sel != null)
            setSelectionPath(sel);
            */
    }
}
