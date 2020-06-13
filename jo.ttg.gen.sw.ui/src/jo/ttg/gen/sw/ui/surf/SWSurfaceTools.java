package jo.ttg.gen.sw.ui.surf;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jo.ttg.gen.sw.data.RuntimeBean;
import jo.ttg.gen.sw.logic.RuntimeLogic;
import jo.ttg.gen.sw.ui.ctrl.BookmarkButton;
import jo.ttg.gen.sw.ui.ctrl.BookmarksButton;
import jo.util.ui.swing.utils.ListenerUtils;

public class SWSurfaceTools extends JPanel
{
    private JTextField      mName;
    private JButton         mZoomOut;
    private BookmarksButton mBookmarks;
    private BookmarkButton  mBookmark;

    public SWSurfaceTools()
    {
        initInstantiate();
        initLink();
        initLayout();
        doUpdateName();
    }

    private void initInstantiate()
    {
        mName = new JTextField();
        mName.setEditable(false);
        mZoomOut = new JButton("\u2b09");
        mBookmarks = new BookmarksButton();
        mBookmark = new BookmarkButton();
    }

    private void initLayout()
    {
        JPanel left = new JPanel();
        left.setLayout(new FlowLayout(FlowLayout.LEFT));
        left.add(mZoomOut);
        left.add(mBookmark);
        left.add(mName);
        JPanel right = new JPanel();
        right.setLayout(new FlowLayout(FlowLayout.RIGHT));
        right.add(mBookmarks);

        setLayout(new BorderLayout());
        add("East", right);
        add("Center", left);
    }

    private void initLink()
    {
        ListenerUtils.listen(mZoomOut,
                (ev) -> RuntimeLogic.setZoom(RuntimeBean.ZOOM_SYSTEM));
        RuntimeLogic.listen("surface", (ov, nv) -> doUpdateName());
    }

    private void doUpdateName()
    {
        if (RuntimeLogic.getInstance().getSurface() != null)
            mName.setText(RuntimeLogic.getInstance().getSurface().getBody()
                    .getName());
    }
}
