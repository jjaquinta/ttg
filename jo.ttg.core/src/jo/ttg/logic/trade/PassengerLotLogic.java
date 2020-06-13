package jo.ttg.logic.trade;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.trade.PassengerLotBean;
import jo.ttg.gen.IGenPassengersEx;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.DateLogic;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.util.html.URIBuilder;
import jo.util.utils.obj.StringUtils;

public class PassengerLotLogic
{
    public static PassengerLotBean getFromURI(IGenScheme scheme, String uri)
    {
        if (!uri.startsWith("passengers://"))
            return null;
        URIBuilder u = new URIBuilder(uri);
        String originURI = u.getQuery().getProperty("originURI");
        if (StringUtils.isTrivial(originURI))
            return null;
        Object origin = MainWorldLogic.getFromURI(scheme, originURI);
        if (origin == null)
            return null;
        String destinationURI = u.getQuery().getProperty("destinationURI");
        if (StringUtils.isTrivial(destinationURI))
            return null;
        Object destination = MainWorldLogic.getFromURI(scheme, destinationURI);
        if (destination == null)
            return null;
        DateBean date = DateLogic.fromString(u.getQuery().getProperty("date"));
        PassengerLotBean lot = new PassengerLotBean();
        lot.setURI(uri);
        lot.setOriginURI(originURI);
        lot.setOriginURI(destinationURI);
        lot.setDate(date);
        if ((origin instanceof MainWorldBean) && (destination instanceof MainWorldBean))
            lot.setPassengers(scheme.getGeneratorPassengers().generatePassengers((MainWorldBean)origin, (MainWorldBean)destination, date));
        else if ((origin instanceof BodyBean) && (destination instanceof BodyBean) && (scheme.getGeneratorCargo() instanceof IGenPassengersEx))
            lot.setPassengers(((IGenPassengersEx)scheme.getGeneratorPassengers()).generatePassengers((BodyBean)origin, (BodyBean)destination, date));
        else
            return null;
        return lot;
    }
}
