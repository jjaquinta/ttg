/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl.body;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import jo.ttg.beans.sys.BodyBean;

/**
 * @author jjaquinta
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BodyPanel extends JPanel
{
    private static List<IBodyPanelHandler> mHandlers = new ArrayList<IBodyPanelHandler>();
    static
    {
        mHandlers.add(new DefaultBodyPanelHandler());
    }

    private CardLayout                    mLayout;
    private JPanel                        mBlankPanel;
    private Map<String, JPanel>           mPanelMap;
    private BodyBean                      mBody;

    public BodyPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mPanelMap = new HashMap<String, JPanel>();
        mBlankPanel = new JPanel();
    }

    private void initLayout()
    {
        mLayout = new CardLayout();
        setLayout(mLayout);
        add("blank", mBlankPanel);
        for (Iterator<IBodyPanelHandler> i = mHandlers.iterator(); i.hasNext();)
        {
            IBodyPanelHandler h = (IBodyPanelHandler)i.next();
            h.getBodyPanels(mPanelMap);
        }
        for (Iterator<String> i = mPanelMap.keySet().iterator(); i.hasNext();)
        {
            String key = (String)i.next();
            add(key, (JPanel)mPanelMap.get(key));
        }
    }

    private void initLink()
    {
    }

    /**
     * @return
     */
    public BodyBean getBody()
    {
        return mBody;
    }

    /**
     * @param bean
     */
    public void setBody(BodyBean bean)
    {
        mBody = bean;
        for (Iterator<IBodyPanelHandler> i = mHandlers.iterator(); i.hasNext();)
        {
            IBodyPanelHandler h = (IBodyPanelHandler)i.next();
            String name = h.handleBody(mPanelMap, mBody);
            if (name != null)
            {
                mLayout.show(this, name);
                return;
            }
        }
        mLayout.show(this, "blank");
    }

    public static void addHandler(IBodyPanelHandler h)
    {
        mHandlers.add(0, h);
    }

    public static void setHandler(IBodyPanelHandler h)
    {
        mHandlers.clear();
        addHandler(h);
    }
}