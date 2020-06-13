package jo.ttg.logic.scans;

import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.dist.DistCapabilities;
import jo.ttg.beans.dist.DistConsumption;
import jo.ttg.beans.dist.DistTransition;
import jo.ttg.beans.scans.BodyScanBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyPopulated;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.DateLogic;
import jo.ttg.logic.dist.ConsumptionLogic;
import jo.ttg.logic.dist.TraverseException;
import jo.ttg.logic.dist.TraverseLogic;
import jo.ttg.logic.gen.BodyLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.util.html.URIBuilder;
import jo.util.utils.obj.StringUtils;

public class BodyScanLogic
{
    public static long OBSCURE_VALUE = 100;
    public static long REMOTE_VALUE = 100;
    public static long AGE_VALUE = -50;
    
    public static BodyScanBean getFromURI(IGenScheme scheme, String uri)
    {
        if ((uri == null) || !uri.startsWith(BodyScanBean.SCHEME))
            return null;
        BodyScanBean bodyScan = new BodyScanBean();
        bodyScan.setURI(uri);
        URIBuilder scanURI = new URIBuilder(uri);
        String sOrd = scanURI.getAuthority();
        String sPath = scanURI.getPath();
        URIBuilder bodyURI = new URIBuilder();
        bodyURI.setScheme(BodyBean.SCHEME);
        bodyURI.setAuthority(sOrd);
        bodyURI.setPath(sPath);
        bodyScan.setBody(BodyLogic.getFromURI(scheme, bodyURI.toString()));
        String atURI = scanURI.getQuery("at");
        if (!StringUtils.isTrivial(atURI))
            bodyScan.setAt(BodyLogic.getFromURI(scheme, atURI));
        String sDate = scanURI.getQuery("date");
        if (!StringUtils.isTrivial(sDate))
            bodyScan.setDate(DateLogic.fromString(sDate));
        String sNow = scanURI.getQuery("now");
        if (!StringUtils.isTrivial(sNow))
            bodyScan.setNow(DateLogic.fromString(sNow));
        if ((bodyScan.getBody() != null) && (bodyScan.getAt() != null) && (bodyScan.getDate() != null) && (bodyScan.getNow() != null))
            calculateValue(scheme, bodyScan);
        return bodyScan;
    }

    private static void calculateValue(IGenScheme scheme, BodyScanBean bodyScan)
    {
        if (!(bodyScan.getAt() instanceof BodyPopulated) && !(bodyScan.getAt() instanceof BodySpecialBean))
            return;
        BodyBean nearest = SchemeLogic.findNearestPopulatedBody(bodyScan.getBody());
        long daysToNearest = daysApart(bodyScan.getBody(), nearest, scheme);
        long daysToCurrent = daysApart(bodyScan.getBody(), bodyScan.getAt(), scheme);
        long daysOld = bodyScan.getNow().getDays() - bodyScan.getDate().getDays();
        long value = OBSCURE_VALUE*daysToNearest + REMOTE_VALUE*daysToCurrent + AGE_VALUE*daysOld;
        System.out.println("daysToNearest="+daysToNearest+", daysToCurrent="+daysToCurrent+", daysOld="+daysOld);
        if (bodyScan.getAt() instanceof BodySpecialBean)
            if (((BodySpecialBean)bodyScan.getAt()).getSubType() == BodySpecialBean.ST_NAVYBASE)
                value *= 2;
            else if (((BodySpecialBean)bodyScan.getAt()).getSubType() == BodySpecialBean.ST_LABBASE)
                value *= 3;
            else if (((BodySpecialBean)bodyScan.getAt()).getSubType() == BodySpecialBean.ST_SCOUTBASE)
                value *= 5;
            else if (((BodySpecialBean)bodyScan.getAt()).getSubType() == BodySpecialBean.ST_REFINERY)
                value /= 2;
        if (bodyScan.getAt() instanceof BodyPopulated)
            if (((BodyPopulated)bodyScan.getAt()).getPopulatedStats().isNavalBase())
                value *= 2;
            else if (((BodyPopulated)bodyScan.getAt()).getPopulatedStats().isScoutBase())
                value *= 5;
            else if (((BodyPopulated)bodyScan.getAt()).getPopulatedStats().isLabBase())
                value *= 3;                
        bodyScan.setValue((int)value);
    }

    private static long daysApart(BodyBean from, BodyBean to, IGenScheme scheme)
    {
        DistCapabilities caps = new DistCapabilities();
        caps.setAcceleration(2.0);
        caps.setDate(new DateBean());
        caps.setJumpCheckPassed(true);
        caps.setJumpRange(2);
        DistConsumption cons;
        try
        {
            List<DistTransition> traverse = TraverseLogic.calcTraverse(from.getURI(), to.getURI(), caps, scheme);
            cons = ConsumptionLogic.calcConsumption(traverse, caps);
        }
        catch (TraverseException e)
        {
            throw new IllegalArgumentException(e);
        }
        return cons.getTime().getDays();        
    }
}
