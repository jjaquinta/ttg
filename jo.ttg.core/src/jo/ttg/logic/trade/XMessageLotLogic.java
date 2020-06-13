package jo.ttg.logic.trade;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.URIBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.trade.XMessageBean;
import jo.ttg.beans.trade.XMessageLotBean;
import jo.ttg.gen.IGenCargoEx;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.DateLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.util.html.URIBuilder;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;

public class XMessageLotLogic
{
    public static URIBean getFromURI(IGenScheme scheme, String uri)
    {
        if (uri.startsWith(XMessageLotBean.SCHEME))
            return getMessageLot(scheme, uri);
        else if (uri.startsWith(XMessageBean.SCHEME))
            return getMessage(scheme, uri);
        else
            return null;
    }
    public static XMessageLotBean getMessageLot(IGenScheme scheme, String uri)
    {
        if (!uri.startsWith(XMessageLotBean.SCHEME))
            return null;
        URIBuilder u = new URIBuilder(uri);
        String originURI = u.getQuery().getProperty("originURI");
        if (StringUtils.isTrivial(originURI))
            return null;
        Object origin = SchemeLogic.getFromURI(scheme, originURI);
        if (origin == null)
            return null;
        String dateText = u.getQuery().getProperty("date");
        DateBean date;
        if (StringUtils.isTrivial(dateText))
            date = new DateBean();
        else
            date = DateLogic.fromString(dateText);
        XMessageLotBean lot = new XMessageLotBean();
        lot.setURI(uri);
        lot.setOriginURI(originURI);
        lot.setDate(date);
        if (origin instanceof MainWorldBean)
            lot.setMessages(scheme.getGeneratorCargo().generateMessages((MainWorldBean)origin, date));
        else if ((origin instanceof BodyBean) && (scheme.getGeneratorCargo() instanceof IGenCargoEx))
            lot.setMessages(((IGenCargoEx)scheme.getGeneratorCargo()).generateMessages((BodyBean)origin, date));
        else
            return null;
        URIBuilder messageURI = new URIBuilder(uri);
        messageURI.setScheme(XMessageBean.SCHEME);
        for (int i = 0; i < lot.getMessages().size(); i++)
        {
            XMessageBean message = lot.getMessages().get(i);
            messageURI.setQuery("index", String.valueOf(i));
            message.setURI(messageURI.toString());
        }
        return lot;
    }
    public static XMessageBean getMessage(IGenScheme scheme, String uri)
    {
        if (!uri.startsWith(XMessageBean.SCHEME))
            return null;
        URIBuilder messageURI = new URIBuilder(uri);
        int index = IntegerUtils.parseInt(messageURI.getQuery("index"));
        String originURI = messageURI.getQuery("originURI");
        String dateText = messageURI.getQuery().getProperty("date");
        URIBuilder messageLotURI = new URIBuilder();
        messageLotURI.setScheme(XMessageLotBean.SCHEME);
        messageLotURI.setQuery("originURI", originURI);
        messageLotURI.setQuery("date", dateText);
        XMessageLotBean lot = getMessageLot(scheme, messageLotURI.toString());
        if (index < lot.getMessages().size())
            return lot.getMessages().get(index);
        else
            return null;
    }
}
