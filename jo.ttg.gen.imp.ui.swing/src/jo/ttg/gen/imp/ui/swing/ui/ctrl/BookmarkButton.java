package jo.ttg.gen.imp.ui.swing.ui.ctrl;

import javax.swing.JButton;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.gen.imp.ui.swing.logic.EditLogic;
import jo.ttg.gen.imp.ui.swing.logic.IconLogic;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.util.ui.swing.utils.ListenerUtils;

public class BookmarkButton extends JButton
{
    public BookmarkButton()
    {
        super(IconLogic.MAKE_BOOKMARK);
        ListenerUtils.listen(this, (ev) -> doAction());
    }
    
    private void doAction()
    {
        BodyBean body = RuntimeLogic.getInstance().getCursorWorld();
        if (body != null)
            EditLogic.addBookmark(body);
    }
}
