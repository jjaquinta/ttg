package jo.ttg.gen.imp.ui.swing.ui.sys;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.json.simple.JSONObject;
import org.json.simple.ToJSONLogic;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.gen.imp.ui.swing.data.RuntimeBean;
import jo.ttg.gen.imp.ui.swing.logic.EditLogic;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.ttg.gen.imp.ui.swing.ui.JSONEditDlg;
import jo.ttg.gen.imp.ui.swing.ui.ctrl.BookmarkButton;
import jo.ttg.gen.imp.ui.swing.ui.ctrl.BookmarksButton;
import jo.ttg.gen.imp.ui.swing.ui.ctrl.ReportsButton;
import jo.util.ui.swing.utils.ListenerUtils;

public class IMPSystemTools extends JPanel
{
    private JButton         mZoomOut;
    private JButton         mZoomIn;
    private JToggleButton   mList;
    private JButton         mEdit;
    private JButton         mYesterday;
    private JLabel          mDate;
    private JButton         mTomorrow;
    private BookmarksButton mBookmarks;
    private BookmarkButton  mBookmark;
    private ReportsButton   mReports;

    public IMPSystemTools()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mZoomIn = new JButton("\u2b0a");
        mZoomOut = new JButton("\u2b09");
        mList = new JToggleButton("List",
                RuntimeLogic.getInstance().isDisplayList());
        mEdit = new JButton("Edit");
        mYesterday = new JButton("\u23ea");
        mDate = new JLabel(RuntimeLogic.getInstance().getDate().toString());
        mTomorrow = new JButton("\u23e9");
        mBookmarks = new BookmarksButton();
        mBookmark = new BookmarkButton();
        mReports = new ReportsButton("cursorWorld");
    }

    private void initLayout()
    {
        JPanel left = new JPanel();
        left.setLayout(new FlowLayout(FlowLayout.LEFT));
        left.add(mZoomOut);
        left.add(mBookmark);
        left.add(mReports);
        left.add(mList);
        left.add(mEdit);
        left.add(mYesterday);
        left.add(mDate);
        left.add(mTomorrow);
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
        RuntimeLogic.listen("displayList", (ov, nv) -> mList
                .setSelected(RuntimeLogic.getInstance().isDisplayList()));
        RuntimeLogic.listen("date", (ov, nv) -> mDate
                .setText((RuntimeLogic.getInstance().getDate().toString())));
        ListenerUtils.listen(mList,
                (ev) -> RuntimeLogic.setDisplayList(mList.isSelected()));
        ListenerUtils.listen(mZoomIn,
                (ev) -> RuntimeLogic.setZoom(RuntimeBean.ZOOM_SURFACE));
        ListenerUtils.listen(mZoomOut,
                (ev) -> RuntimeLogic.setZoom(RuntimeBean.ZOOM_SUBSECTOR));
        ListenerUtils.listen(mEdit, (ev) -> doEditWorld());
        ListenerUtils.listen(mYesterday,
                (ev) -> RuntimeLogic.setDateDelta(-DateBean.ONE_DAY));
        ListenerUtils.listen(mTomorrow,
                (ev) -> RuntimeLogic.setDateDelta(DateBean.ONE_DAY));
    }

    private void doEditWorld()
    {
        BodyBean body = RuntimeLogic.getInstance().getCursorWorld();
        if (body == null)
            return;
        JSONObject json = (JSONObject)ToJSONLogic.toJSON(body);
        JSONEditDlg dlg = new JSONEditDlg(
                (JFrame)SwingUtilities.getWindowAncestor(this), json);
        dlg.setVisible(true);
        if (dlg.isAccepted())
        {
            json = dlg.getJSON();
            EditLogic.saveWorld(json);
        }
    }
}
