package jo.ttg.gen.imp.ui.swing.ui.sub;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;

import jo.ttg.beans.OrdBean;
import jo.ttg.gen.imp.ui.swing.data.RuntimeBean;
import jo.ttg.gen.imp.ui.swing.logic.EditLogic;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.ttg.gen.imp.ui.swing.ui.ctrl.BookmarksButton;
import jo.ttg.gen.imp.ui.swing.ui.ctrl.ReportsButton;
import jo.util.ui.swing.utils.ListenerUtils;

public class IMPSubSectorTools extends JPanel
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
    private JButton         mZoomOut;
    private BookmarksButton mBookmarks;
    private ReportsButton   mReports;

    public IMPSubSectorTools()
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
        mZoomOut = new JButton("\u2b09");
        mBookmarks = new BookmarksButton();
        mReports = new ReportsButton("region", "cursorMainWorld");
    }

    private void initLayout()
    {
        JPanel left = new JPanel();
        left.setLayout(new FlowLayout(FlowLayout.LEFT));
        left.add(mZoomOut);
        left.add(mReports);
        left.add(mGrid);
        left.add(mLinks);
        left.add(mX);
        left.add(mY);
        left.add(mZ);
        left.add(mInsert);
        left.add(mDelete);
        left.add(mEdit);
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
        ListenerUtils.listen(mZoomOut,
                (ev) -> RuntimeLogic.setZoom(RuntimeBean.ZOOM_SECTOR));
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

    private void doEditMainWorld()
    {
        /*
        MainWorldBean mw = RuntimeLogic.getInstance().getCursorMainWorld();
        if (mw == null)
            return;
        JSONObject json = mw.toJSON();
        JSONEditDlg dlg = new JSONEditDlg(
                (JFrame)SwingUtilities.getWindowAncestor(this), json);
        dlg.setVisible(true);
        if (dlg.isAccepted())
        {
            json = dlg.getJSON();
            EditLogic.saveMainWorld(json);
        }
        */
    }
}
