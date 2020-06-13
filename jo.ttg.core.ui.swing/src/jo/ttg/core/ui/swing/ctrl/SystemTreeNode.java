/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyToidsBean;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SystemTreeNode implements TreeNode
{
    private SystemTreeNode         mParent;
    private BodyBean               mBody;
    private Vector<SystemTreeNode> mChildren;

    public SystemTreeNode(SystemTreeNode parent, BodyBean body)
    {
        mParent = parent;
        mBody = body;
        mChildren = new Vector<SystemTreeNode>();
        for (Iterator<BodyBean> i = mBody.getSatelitesIterator(); i.hasNext();)
        {
            BodyBean b = i.next();
            SystemTreeNode n = new SystemTreeNode(this, b);
            mChildren.add(n);
        }
    }

    public BodyBean getBody()
    {
        return mBody;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#getChildCount()
     */
    public int getChildCount()
    {
        return mChildren.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#getAllowsChildren()
     */
    public boolean getAllowsChildren()
    {
        return !(mBody instanceof BodyToidsBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#isLeaf()
     */
    public boolean isLeaf()
    {
        return mChildren.size() == 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#children()
     */
    public Enumeration<SystemTreeNode> children()
    {
        return mChildren.elements();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#getParent()
     */
    public TreeNode getParent()
    {
        return mParent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#getChildAt(int)
     */
    public TreeNode getChildAt(int arg0)
    {
        return (TreeNode)mChildren.get(arg0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
     */
    public int getIndex(TreeNode arg0)
    {
        return mChildren.indexOf(arg0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return mBody.getName();
    }

}
