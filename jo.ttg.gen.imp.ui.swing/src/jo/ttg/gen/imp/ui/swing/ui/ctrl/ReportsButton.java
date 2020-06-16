package jo.ttg.gen.imp.ui.swing.ui.ctrl;

import java.awt.Desktop;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jo.ttg.beans.URIBean;
import jo.ttg.core.report.logic.ITTGReport;
import jo.ttg.core.report.logic.TTGReportLogic;
import jo.ttg.gen.imp.ui.swing.logic.IconLogic;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.utils.BeanUtils;
import jo.util.utils.obj.IntegerUtils;

public class ReportsButton extends JButton
{
    private URIBean[] mSubjects;
    
    public ReportsButton(String... prop)
    {
        super(IconLogic.REPORTS);
        mSubjects = new URIBean[prop.length];
        for (int i = 0; i < mSubjects.length; i++)
        {
            final int j = i;
            RuntimeLogic.listen(prop[i], (ov,nv) -> mSubjects[j] = (URIBean)nv);
            mSubjects[j] = (URIBean)BeanUtils.get(RuntimeLogic.getInstance(), prop[i]);
        }
        ListenerUtils.listen(this, (ev) -> doAction());
    }
    
    private void doAction()
    {
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doReport(e.getActionCommand());
            }
        };
        JPopupMenu popup = new JPopupMenu();
        for (int i = 0; i < mSubjects.length; i++)
        {
            URIBean subject = mSubjects[i];
            if (subject != null)
            {
                List<ITTGReport> reports = TTGReportLogic.getReporters(subject);
                for (ITTGReport report : reports)
                {
                    String name = report.getName(subject);
                    JMenuItem menuItem = new JMenuItem(name);
                    menuItem.setActionCommand(report.getID()+":"+i);
                    menuItem.addActionListener(al);
                    popup.add(menuItem);
                }
            }
        }
        Point l = getLocation();
        popup.show(this, (int)l.getX(), (int)l.getY());
    }
    
    private void doReport(String id)
    {
        int o = id.lastIndexOf(':');
        int idx = IntegerUtils.parseInt(id.substring(o + 1));
        String repid = id.substring(0, o);
        ITTGReport reporter = TTGReportLogic.getReporter(mSubjects[idx], repid);
        if (reporter == null)
            return;
        try
        {
            File html = TTGReportLogic.reportToFile(reporter, mSubjects[idx]);
            Desktop.getDesktop().open(html);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
