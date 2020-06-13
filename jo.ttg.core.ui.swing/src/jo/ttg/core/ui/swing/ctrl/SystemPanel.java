/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.gen.IGenScheme;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SystemPanel extends JPanel
{
    private IGenScheme              mScheme;
    private OrdBean                 mOrigin;
    private BodyBean                mSelected;
    private List<TTGActionListener> mTTGActionListeners;

    private SystemTree              mTree;
    private BodyPanel               mBody;

    public SystemPanel(IGenScheme scheme)
    {
        mScheme = scheme;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mTree = new SystemTree(mScheme);
        mBody = new BodyPanel();
        mTTGActionListeners = new ArrayList<>();
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("West", new JScrollPane(mTree));
        add("Center", mBody);
    }

    private void initLink()
    {
        mTree.addTreeSelectionListener(new SystemTreeSelectionListener());
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
        mTree.setOrigin(bean);
    }

    class SystemTreeSelectionListener implements TreeSelectionListener
    {

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.
         * event.TreeSelectionEvent)
         */
        public void valueChanged(TreeSelectionEvent ev)
        {
            TreePath path = ev.getPath();
            if (path == null)
            {
                setSelected(null);
                return;
            }
            SystemTreeNode n = (SystemTreeNode)path.getLastPathComponent();
            setSelected(n.getBody());
        }
    }

    /**
     * @return
     */
    public BodyBean getSelected()
    {
        return mSelected;
    }

    /**
     * @param bean
     */
    public void setSelected(BodyBean bean)
    {
        mSelected = bean;
        mBody.setBody(mSelected);
        fireTTGActionEvent(new TTGActionEvent(this, TTGActionEvent.SELECTED,
                bean.getURI(), bean));
    }

    public void addTTGActionListener(TTGActionListener l)
    {
        synchronized (mTTGActionListeners)
        {
            mTTGActionListeners.add(l);
        }
    }

    public void removeTTGActionListener(TTGActionListener l)
    {
        synchronized (mTTGActionListeners)
        {
            mTTGActionListeners.remove(l);
        }
    }

    protected void fireTTGActionEvent(TTGActionEvent ev)
    {
        for (TTGActionListener l : mTTGActionListeners)
            l.actionPerformed(ev);
    }
}
