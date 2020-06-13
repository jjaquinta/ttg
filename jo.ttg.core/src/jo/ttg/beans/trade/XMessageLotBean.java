package jo.ttg.beans.trade;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.URIBean;


public class XMessageLotBean implements URIBean
{
    public static final String SCHEME = "xmessagelot://";
    
    private String          mURI;
    private List<XMessageBean> mMessages;
    private String          mOriginURI;
    private String          mDestinationURI;
    private DateBean        mDate;
    
    public XMessageLotBean()
    {
        setMessages(new ArrayList<XMessageBean>());
        mDate = new DateBean();
    }
    
    public String getURI()
    {
        return mURI;
    }
    public void setURI(String uRI)
    {
        mURI = uRI;
    }
    public String getOriginURI()
    {
        return mOriginURI;
    }
    public void setOriginURI(String originURI)
    {
        mOriginURI = originURI;
    }

    public DateBean getDate()
    {
        return mDate;
    }

    public void setDate(DateBean date)
    {
        mDate = date;
    }

    public String getDestinationURI()
    {
        return mDestinationURI;
    }

    public void setDestinationURI(String destinationURI)
    {
        mDestinationURI = destinationURI;
    }

    public List<XMessageBean> getMessages()
    {
        return mMessages;
    }

    public void setMessages(List<XMessageBean> messages)
    {
        mMessages = messages;
    }
}
