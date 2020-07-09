/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl.body.ext;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.core.ui.swing.ctrl.BodyView;
import jo.util.ui.swing.ctrl.HTMLPane;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HTMLBodySpecialPanel extends JPanel
{
    protected static final int T_NAME    = 0x01;
    protected static final int T_NOTES   = 0x02;
    protected static final int T_DOCKING = 0x04;
    protected static final int T_FUEL    = 0x08;
    protected static final int T_REPAIR  = 0x10;

    private BodySpecialBean    mBody;
    private IBodySpecialHandler mHandler;

    private HTMLPane   mClient;
    
    public HTMLBodySpecialPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }
    
    private static final String TEMPLATE_PREAMBLE = "<html>"
            + "<body style='font-family: Sans-Serif;'>"
            + "<table width='100%' align='top'>";
    private static final String TEMPLATE_NAME = "<tr><th>Name:</th><td colspan='2'><font size='+2'>[[NAME]]</font></td></tr>";
    private static final String TEMPLATE_DOCKING = "<tr><th>Docking:</th><td colspan='2'>[[DOCKING]]</td></tr>";
    private static final String TEMPLATE_FUEL = "<tr><th>Fuel:</th><td colspan='2'>[[FUEL]]</td></tr>";
    private static final String TEMPLATE_REPAIR = "<tr><th>Repair:</th><td colspan='2'>[[REPAIR]]</td></tr>";
    private static final String TEMPLATE_INTERLUDE = "</table>";
    private static final String TEMPLATE_NOTES = "<h3>Notes:</h3>"
            + "<p>[[notes]]</p>";
    private static final String TEMPLATE_POSTAMBLE = "</body>"
            + "</html>";

    private void initInstantiate()
    {
        mClient = new HTMLPane();
    }
    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("Center", mClient);
    }
    private void initLink()
    {
    }
    
    private void updateTemplate()
    {
        String template = "";
        int type = (mHandler == null) ? 0 : mHandler.getType();
        template += TEMPLATE_PREAMBLE;
        if ((type & T_NAME) != 0)
            template += TEMPLATE_NAME;
        if ((type & T_DOCKING) != 0)
            template += TEMPLATE_DOCKING;
        if ((type & T_FUEL) != 0)
            template += TEMPLATE_FUEL;
        if ((type & T_REPAIR) != 0)
            template += TEMPLATE_REPAIR;
        template += TEMPLATE_INTERLUDE;
        if ((type & T_NOTES) != 0)
            template += TEMPLATE_NOTES;
        template += TEMPLATE_POSTAMBLE;
        mClient.setTemplate(template);
    }
    /**
     * @param bean
     */
    public void setBody(BodySpecialBean bean)
    {
        mBody = bean;
        mClient.replaceBean(mBody);
        mHandler = null;
        for (IBodySpecialHandler h : BodySpecialPanel.mHandlers)
            if (h.isHandlerFor(mBody))
            {
                mHandler = h;
                break;
            }
        updateTemplate();
        int type = (mHandler == null) ? 0 : mHandler.getType();
        if ((type & T_NAME) != 0)
        {
            mClient.setSubstitution("ICON", BodyView.getIconURI(mBody));
            mClient.setSubstitution("NAME", mBody.getName());
        }
        if ((type & T_NOTES) != 0)
            mClient.setSubstitution("NOTES", mHandler.getNotes(mBody));
        if ((type & T_DOCKING) != 0)
            mClient.setSubstitution("DOCKING", mHandler.getDocking(mBody));
        if ((type & T_FUEL) != 0)
            mClient.setSubstitution("FUEL", mHandler.getFuel(mBody));
        if ((type & T_REPAIR) != 0)
            mClient.setSubstitution("FUEL", mHandler.getRepair(mBody));
        mClient.updateText();
        doLayout();
    }
}
