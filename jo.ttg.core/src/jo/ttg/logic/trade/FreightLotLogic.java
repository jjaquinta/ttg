package jo.ttg.logic.trade;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.trade.FreightLotBean;
import jo.ttg.gen.IGenCargoEx;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.DateLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.util.html.URIBuilder;
import jo.util.utils.obj.StringUtils;

public class FreightLotLogic
{
    public static FreightLotBean getFromURI(IGenScheme scheme, String uri)
    {
        if (!uri.startsWith("freight://"))
            return null;
        URIBuilder u = new URIBuilder(uri);
        String originURI = u.getQuery().getProperty("originURI");
        if (StringUtils.isTrivial(originURI))
            return null;
        Object origin = SchemeLogic.getFromURI(scheme, originURI);
        if (origin == null)
            return null;
        String destinationURI = u.getQuery().getProperty("destinationURI");
        if (StringUtils.isTrivial(destinationURI))
            return null;
        Object destination = SchemeLogic.getFromURI(scheme, destinationURI);
        if (destination == null)
            return null;
        DateBean date = DateLogic.fromString(u.getQuery().getProperty("date"));
        FreightLotBean lot = new FreightLotBean();
        lot.setURI(uri);
        lot.setOriginURI(originURI);
        lot.setOriginURI(destinationURI);
        lot.setDate(date);
        if ((origin instanceof MainWorldBean) && (destination instanceof MainWorldBean))
            lot.setFreights(scheme.getGeneratorCargo().generateFreight((MainWorldBean)origin, (MainWorldBean)destination, date));
        else if ((origin instanceof BodyBean) && (destination instanceof BodyBean) && (scheme.getGeneratorCargo() instanceof IGenCargoEx))
            lot.setFreights(((IGenCargoEx)scheme.getGeneratorCargo()).generateFreight((BodyBean)origin, (BodyBean)destination, date));
        else
            return null;
        return lot;
    }
}
