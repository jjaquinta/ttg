/*
 * Created on Jan 22, 2005
 *
 */
package ttg.view.adv.ctrl;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.utils.io.ResourceUtils;
import jo.util.utils.xml.XMLTransUtils;

/**
 * @author Jo
 *
 */
public class TXMLPanel extends JPanel implements HyperlinkListener
{
    private JTextPane           mClient;
    private JButton             mHome;
    private JButton             mNext;
    private JButton             mPrev;
    private JPanel              mStatusBar;

    private String              mSystemResourceRoot;
    private Map<String, Object> mValueMap;
    private String              mHomePage;
    private List<String>        mPages;
    private int                 mPageOffset;

    /**
     *
     */

    public TXMLPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mSystemResourceRoot = "";
        mHomePage = "index.html";
        mPages = new ArrayList<>();
        mPageOffset = 0;

        mClient = new JTextPane();
        mClient.setContentType("text/html");
        mClient.setEditable(false);
        mClient.setEditorKit(new XHTMLEditorKit());
        mHome = new JButton("Home");
        mNext = new JButton("Next");
        mPrev = new JButton("Prev");
    }

    private void initLink()
    {
        mClient.addHyperlinkListener(this);
        ListenerUtils.listen(mHome, (ev) -> goHome());
        ListenerUtils.listen(mNext, (ev) -> goNext());
        ListenerUtils.listen(mPrev, (ev) -> goPrev());
    }

    private void initLayout()
    {
        mStatusBar = new JPanel();
        mStatusBar.add(mHome);
        mStatusBar.add(mNext);
        mStatusBar.add(mPrev);

        setLayout(new BorderLayout());
        add("Center", mClient);
        add("North", mStatusBar);
    }

    public void go(String basePage)
    {
        String page = basePage;
        if (getSystemResourceRoot().length() > 0)
            page = getSystemResourceRoot() + basePage;
        String txml;
        try
        {
            txml = ResourceUtils.loadSystemResourceString(page,
                    TXMLPanel.class);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        Map<String,Object> valueMap = getValueMap();
        valueMap.put("xml.root.ref", TXMLPanel.class);
        String html = XMLTransUtils.transform(txml, valueMap);
        //System.out.println(html);
        mClient.setText(html);
        updateHistory(basePage);
    }

    private void updateHistory(String page)
    {
        // test for forward
        if ((mPages.size() > mPageOffset)
                && page.equals(mPages.get(mPageOffset)))
            mPageOffset++;
        // test for backward
        else if ((mPages.size() > 0)
                && page.equals(mPages.get(mPageOffset - 1)))
            mPageOffset--;
        // else new page
        else
        {
            while (mPages.size() > mPageOffset)
                mPages.remove(mPageOffset);
            mPages.add(mPageOffset, page);
            mPageOffset++;
        }
        mPrev.setEnabled(mPageOffset > 1);
        mNext.setEnabled(mPageOffset < mPages.size());
    }

    public void goPrev()
    {
        if (mPageOffset > 0)
            go((String)mPages.get(mPageOffset - 1));
    }

    public void goNext()
    {
        if (mPageOffset < mPages.size())
            go((String)mPages.get(mPageOffset));
    }

    public void goHome()
    {
        go(getHomePage());
    }

    public void flushHistory()
    {
        mPages.clear();
        mPageOffset = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.
     * HyperlinkEvent)
     */
    public void hyperlinkUpdate(HyperlinkEvent ev)
    {
        if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
            go(ev.getDescription());
    }

    /**
     * @return Returns the systemResourceRoot.
     */
    public String getSystemResourceRoot()
    {
        return mSystemResourceRoot;
    }

    /**
     * @param systemResourceRoot
     *            The systemResourceRoot to set.
     */
    public void setSystemResourceRoot(String systemResourceRoot)
    {
        mSystemResourceRoot = systemResourceRoot;
        if (!mSystemResourceRoot.endsWith("/"))
            mSystemResourceRoot += "/";
        mValueMap.put("xml.root", mSystemResourceRoot);
    }

    /**
     * @return Returns the valueMap.
     */
    public Map<String,Object> getValueMap()
    {
        return mValueMap;
    }

    /**
     * @param valueMap
     *            The valueMap to set.
     */
    public void setValueMap(Map<String,Object> valueMap)
    {
        mValueMap = valueMap;
    }

    /**
     * @return Returns the homePage.
     */
    public String getHomePage()
    {
        return mHomePage;
    }

    /**
     * @param homePage
     *            The homePage to set.
     */
    public void setHomePage(String homePage)
    {
        mHomePage = homePage;
        URL url = ResourceUtils.loadSystemResourceURL(
                mSystemResourceRoot + mHomePage, TXMLPanel.class);
        ((HTMLDocument)mClient.getDocument()).setBase(url);
    }

    public void setShowStatusBar(boolean show)
    {
        mStatusBar.setVisible(show);
    }

    /**
     * @author Peter De Bruycker
     */
    public class XHTMLEditorKit extends HTMLEditorKit
    {

        protected Parser getParser()
        {
            return new Parser() {
                public void parse(Reader reader, ParserCallback callback,
                        boolean ignoreCharSet) throws IOException
                {
                    try
                    {
                        SAXParser parser = SAXParserFactory.newInstance()
                                .newSAXParser();
                        SaxHandler handler = new SaxHandler(callback);
                        parser.parse(new InputSource(reader), handler);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        IOException ioe = new IOException();
                        ioe.initCause(e);
                        throw ioe;
                    }
                }
            };
        }

        private class SaxHandler extends DefaultHandler
        {

            private final ParserCallback callback;

            public SaxHandler(ParserCallback callback)
            {
                this.callback = callback;
            }

            public void endElement(String uri, String name, String qName)
                    throws SAXException
            {
                callback.handleEndTag(HTML.getTag(qName), -1);
            }

            public void startElement(String uri, String name, String qName,
                    Attributes atts) throws SAXException
            {
                SimpleAttributeSet attributeSet = convertAttributes(atts);
                callback.handleStartTag(HTML.getTag(qName), attributeSet, -1);
            }

            private SimpleAttributeSet convertAttributes(Attributes atts)
            {
                SimpleAttributeSet attributeSet = new SimpleAttributeSet();
                for (int i = 0; i < atts.getLength(); i++)
                {
                    HTML.Attribute attribute = HTML
                            .getAttributeKey(atts.getQName(i));
                    attributeSet.addAttribute(attribute, atts.getValue(i));
                }
                return attributeSet;
            }

            public void characters(char[] ch, int start, int length)
                    throws SAXException
            {
                String str = new String(ch, start, length);
                str = str.trim();
                if (str.length() == 0)
                    return;
                callback.handleText(str.toCharArray(), -1);
            }

            public void error(SAXParseException e) throws SAXException
            {
                callback.handleError(e.getMessage(), -1);
            }

            public void fatalError(SAXParseException e) throws SAXException
            {
                callback.handleError(e.getMessage(), -1);
            }
        }
    }

}
