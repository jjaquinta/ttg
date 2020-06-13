package jo.ttg.gen.sw.ui.ctrl;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.gen.sw.logic.EditLogic;
import jo.ttg.gen.sw.logic.IconLogic;
import jo.ttg.logic.gen.BodyLogic;
import jo.util.ui.swing.utils.ListenerUtils;

public class BookmarksButton extends JButton
{
    public BookmarksButton()
    {
        super(IconLogic.BOOKMARK);
        ListenerUtils.listen(this, (ev) -> doAction());
    }
    
    private void doAction()
    {
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EditLogic.gotoBookmark(e.getActionCommand());
            }
        };
        List<String> bookmarks = EditLogic.getBookmarks();
        JPopupMenu popup = new JPopupMenu();
        for (String uri : bookmarks)
        {
            BodyBean body = BodyLogic.getFromURI(uri);
            int o = uri.lastIndexOf('/');
            String name = null;
            try
            {
                name = URLDecoder.decode(uri.substring(o + 1), "utf-8");
            }
            catch (UnsupportedEncodingException e1)
            {
            }
            BufferedImage img = IconLogic.getIcon(body);
            JMenuItem menuItem;
            if (img != null)
                menuItem = new JMenuItem(name, new ImageIcon(img));
            else
                menuItem = new JMenuItem(name);
            menuItem.setActionCommand(uri);
            menuItem.addActionListener(al);
            popup.add(menuItem);
        }
        Point l = getLocation();
        popup.show(this, (int)l.getX(), (int)l.getY());
    }
}
