package jo.ttg.gen.sw.ui.sub;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.json.simple.JSONObject;
import org.json.simple.ToJSONLogic;

import jo.ttg.beans.OrdBean;
import jo.ttg.gen.sw.data.RuntimeBean;
import jo.ttg.gen.sw.data.SWMainWorldBean;
import jo.ttg.gen.sw.logic.EditLogic;
import jo.ttg.gen.sw.logic.ReportLogic;
import jo.ttg.gen.sw.logic.RuntimeLogic;
import jo.ttg.gen.sw.ui.JSONEditDlg;
import jo.ttg.gen.sw.ui.ctrl.BookmarksButton;
import jo.ttg.gen.sw.ui.ctrl.ReportsButton;
import jo.util.ui.swing.utils.ListenerUtils;

public class SWSubSectorTools extends JPanel
{
    private JToggleButton   mGrid;
    private JToggleButton   mLinks;
    private JSpinner        mX;
    private JSpinner        mY;
    private JSpinner        mZ;
    private JButton         mInsert;
    private JButton         mDelete;
    private JButton         mEdit;
    private JButton         mZoomIn;
    private BookmarksButton mBookmarks;
    private JButton         mMWRep;
    private ReportsButton   mReports;

    public SWSubSectorTools()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mGrid = new JToggleButton("Grid",
                RuntimeLogic.getInstance().isDisplayGrid());
        mLinks = new JToggleButton("Links",
                RuntimeLogic.getInstance().isDisplayLinks());
        OrdBean o = RuntimeLogic.getInstance().getCursorPoint();
        mX = new JSpinner(
                new SpinnerNumberModel((int)o.getX(), -2000, 2000, 1));
        mY = new JSpinner(
                new SpinnerNumberModel((int)o.getY(), -2000, 2000, 1));
        mZ = new JSpinner(
                new SpinnerNumberModel((int)o.getZ(), -2000, 2000, 1));
        mInsert = new JButton("INS");
        mDelete = new JButton("DEL");
        mEdit = new JButton("Edit");
        mZoomIn = new JButton("\u2b0a");
        mBookmarks = new BookmarksButton();
        mMWRep = new JButton("MW");
        mReports = new ReportsButton("region", "cursorMainWorld");
    }

    private void initLayout()
    {
        JPanel left = new JPanel();
        left.setLayout(new FlowLayout(FlowLayout.LEFT));
        left.add(mReports);
        left.add(mGrid);
        left.add(mLinks);
        left.add(mX);
        left.add(mY);
        left.add(mZ);
        left.add(mInsert);
        left.add(mDelete);
        left.add(mEdit);
        left.add(mMWRep);
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
        RuntimeLogic.listen("displayGrid", (ov, nv) -> mGrid
                .setSelected(RuntimeLogic.getInstance().isDisplayGrid()));
        ListenerUtils.listen(mGrid,
                (ev) -> RuntimeLogic.setDisplayGrid(mGrid.isSelected()));
        RuntimeLogic.listen("displayLinks", (ov, nv) -> mLinks
                .setSelected(RuntimeLogic.getInstance().isDisplayLinks()));
        ListenerUtils.listen(mLinks,
                (ev) -> RuntimeLogic.setDisplayLinks(mLinks.isSelected()));
        RuntimeLogic.listen("cursorPoint", (ov, nv) -> updateOrdsData(
                RuntimeLogic.getInstance().getCursorPoint()));
        ListenerUtils.listen(mX, (ev) -> updateOrdsUI());
        ListenerUtils.listen(mY, (ev) -> updateOrdsUI());
        ListenerUtils.listen(mZ, (ev) -> updateOrdsUI());
        ListenerUtils.listen(mInsert, (ev) -> EditLogic.insertMainworld());
        ListenerUtils.listen(mDelete, (ev) -> EditLogic.deleteMainworld());
        ListenerUtils.listen(mEdit, (ev) -> doEditMainWorld());
        ListenerUtils.listen(mZoomIn,
                (ev) -> RuntimeLogic.setZoom(RuntimeBean.ZOOM_SYSTEM));
        ListenerUtils.listen(mMWRep, (ev) -> doReportMainWorld());
    }

    private void updateOrdsUI()
    {
        OrdBean o = new OrdBean((int)mX.getValue(), (int)mY.getValue(),
                (int)mZ.getValue());
        RuntimeLogic.setCursorPoint(o);
    }

    private void updateOrdsData(OrdBean ords)
    {
        if (!mX.getValue().equals((int)ords.getX()))
            mX.setValue((int)ords.getX());
        if (!mY.getValue().equals((int)ords.getY()))
            mY.setValue((int)ords.getY());
        if (!mZ.getValue().equals((int)ords.getZ()))
            mZ.setValue((int)ords.getZ());
    }
    
    private void doReportMainWorld()
    {
        File rep = ReportLogic.mwReport();  
        if (rep != null)
            try
            {
                Desktop.getDesktop().open(rep);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
    }

    private void doEditMainWorld()
    {
        SWMainWorldBean mw = RuntimeLogic.getInstance().getCursorMainWorld();
        if (mw == null)
            return;
        JSONObject json = (JSONObject)ToJSONLogic.toJSON(mw);
        JSONEditDlg dlg = new JSONEditDlg(
                (JFrame)SwingUtilities.getWindowAncestor(this), json);
        dlg.setVisible(true);
        if (dlg.isAccepted())
        {
            json = dlg.getJSON();
            EditLogic.saveMainWorld(json);
        }
    }
}
