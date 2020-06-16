package jo.ttg.gen.imp.ui.swing.ui.sec;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import jo.ttg.beans.sec.SectorBean;
import jo.ttg.gen.imp.ui.swing.data.RuntimeBean;
import jo.ttg.gen.imp.ui.swing.logic.IconLogic;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.ttg.gen.imp.ui.swing.ui.SettingsDlg;
import jo.ttg.gen.imp.ui.swing.ui.ctrl.BookmarksButton;
import jo.ttg.gen.imp.ui.swing.ui.ctrl.ReportsButton;
import jo.util.ui.swing.utils.ListenerUtils;

public class IMPSectorTools extends JPanel
{
    private JButton         mSettings;
    private JButton         mEdit;
    private JButton         mZoomIn;
    private BookmarksButton mBookmarks;
    private ReportsButton   mReports;
    private JTextField      mName;

    public IMPSectorTools()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mEdit = new JButton(IconLogic.EDIT);
        mEdit.setToolTipText("Edit");
        mSettings = new JButton(IconLogic.SETTINGS);
        mSettings.setToolTipText("Settings");
        mZoomIn = new JButton("\u2b0a");
        mZoomIn.setToolTipText("Zoom In");
        mBookmarks = new BookmarksButton();
        mReports = new ReportsButton("region", "cursorMainWorld");
        mName = new JTextField();
        mName.setEditable(false);
    }

    private void initLayout()
    {
        JPanel left = new JPanel();
        left.setLayout(new FlowLayout(FlowLayout.LEFT));
        left.add(mSettings);
        left.add(mReports);
        left.add(mEdit);
        left.add(mName);
        JPanel right = new JPanel();
        right.setLayout(new FlowLayout(FlowLayout.RIGHT));
        right.add(mBookmarks);
        right.add(mZoomIn);

        setLayout(new BorderLayout());
        add("Center", left);
        add("East", right);
    }

    private void initLink()
    {
        ListenerUtils.listen(mSettings, (ev) -> doSettings());
        ListenerUtils.listen(mEdit, (ev) -> doEditMainWorld());
        ListenerUtils.listen(mZoomIn,
                (ev) -> RuntimeLogic.setZoom(RuntimeBean.ZOOM_SUBSECTOR));
        RuntimeLogic.listen("focusSector", (ov,nv) -> doUpdateName());
    }

    private void doUpdateName()
    {
        SectorBean sec = RuntimeLogic.getInstance().getFocusSector();
        if (sec != null)
            mName.setText(sec.getName());
        else
            mName.setText("Empty "+RuntimeLogic.getInstance().getFocusPoint());
    }
    
    private void doSettings()
    {
        SettingsDlg dlg = new SettingsDlg((JFrame)SwingUtilities.getWindowAncestor(this));
        dlg.setVisible(true);
    }

    private void doEditMainWorld()
    {
//        MainWorldBean mw = RuntimeLogic.getInstance().getCursorMainWorld();
//        if (mw == null)
//            return;
//        JSONObject json = mw.toJSON();
//        JSONEditDlg dlg = new JSONEditDlg(
//                (JFrame)SwingUtilities.getWindowAncestor(this), json);
//        dlg.setVisible(true);
//        if (dlg.isAccepted())
//        {
//            json = dlg.getJSON();
//            EditLogic.saveMainWorld(json);
//        }
    }
}
