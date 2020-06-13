package jo.ttg.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jo.ttg.beans.URIBookmarkBean;
import jo.util.utils.io.FileUtils;
import jo.util.utils.xml.XMLIOUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Document;

public class BookmarkLogic
{
    private static List<URIBookmarkBean>    mBookmarks = new ArrayList<URIBookmarkBean>();
    
    @SuppressWarnings("unchecked")
    public static void load(File bookmarkFile)
    {
        if (!bookmarkFile.exists())
            return;
        Document doc = XMLUtils.readFile(bookmarkFile);
        if (doc == null)
            return;
        mBookmarks = (List<URIBookmarkBean>)XMLIOUtils.fromXML(doc.getFirstChild(), BookmarkLogic.class.getClassLoader());
    }
    
    public static void save(File bookmarkFile) throws IOException
    {
        Document doc = XMLUtils.newDocument();
        XMLIOUtils.toXML(doc, mBookmarks, new HashMap<Object,Object>());
        String xml = XMLUtils.writeString(doc.getFirstChild());
        FileUtils.writeFile(xml, bookmarkFile);
    }
    
    public static List<URIBookmarkBean> getBookmarks()
    {
        return mBookmarks;
    }
    
    public static void addBookmark(URIBookmarkBean mark)
    {
        mBookmarks.add(mark);
    }
    
    public static void removeBookmark(URIBookmarkBean mark)
    {
        mBookmarks.remove(mark);
    }
}
