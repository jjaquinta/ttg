/*
 * Created on Dec 18, 2004
 *
 */
package ttg.logic.adv;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.LocationURI;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.dist.DistCapabilities;
import jo.ttg.beans.dist.DistConsumption;
import jo.ttg.beans.dist.DistTransition;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodyPopulated;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.dist.ConsumptionLogic;
import jo.ttg.logic.dist.TraverseException;
import jo.ttg.logic.dist.TraverseLogic;
import jo.ttg.logic.gen.SchemeLogic;
import ttg.beans.adv.AdvEvent;
import ttg.beans.adv.BodySpecialAdvBean;
import ttg.beans.adv.Game;
import ttg.beans.adv.ShipInst;

/**
 * @author Jo
 *
 */
public class LocationLogic
{
    public static String toString(LocationURI uri, IGenScheme scheme)
    {
        OrdBean ords = uri.getOrds();
        if (uri.getType() == LocationURI.MAINWORLD)
        {
            MainWorldBean mw = scheme.getGeneratorMainWorld().generateMainWorld(ords);
            if (mw == null)
                return "Hex "+OrdLogic.getShortNum(ords);
            else
                return mw.getName()+" ("+OrdLogic.getShortNum(ords)+")";
        }
        else if ((uri.getType() == LocationURI.SYSTEM) || (uri.getType() == LocationURI.BODY))
        {
            MainWorldBean mw = scheme.getGeneratorMainWorld().generateMainWorld(ords);
            String primary = uri.getPath();
            int o = primary.lastIndexOf("/");
            if (o >= 0)
                primary = primary.substring(o+1);
            String orbit = uri.getParam("orbit");
            if (orbit == null)
                return "At "+primary+" in the "+mw.getName()+" system.";
            else if (orbit.equals("0"))
                return "On "+primary+" in the "+mw.getName()+" system.";
            else
            {
                BodyBean b = (BodyBean)SchemeLogic.getFromURI(scheme, uri.getURI());
                String desc;
                if (b instanceof BodyStarBean)
                    desc = "orbit #"+FormatUtils.sNum(BodyBean.convRadiusToOrbit(Double.parseDouble(orbit)*b.getDiameter()), 0, 1)+" around ";
                else if ((b instanceof BodyWorldBean) || (b instanceof BodyGiantBean))
                    desc = FormatUtils.sNum(Double.parseDouble(orbit), 0, 1)+" diameters around ";
                else
                    desc = "";
                return "At "+desc+primary+" in the "+mw.getName()+" system.";
            }
        }
        return uri.toString();
    }

    public static String toString(String uri, IGenScheme scheme)
    {
        return toString(new LocationURI(uri), scheme);
    }
    
    public static boolean dock(Game game)
    {
        ShipInst ship = game.getShip();
        if (!ship.getLocation().equals(ship.getDestination()))
            return false;
        Object o = SchemeLogic.getFromURI(game.getScheme(), ship.getLocation());
        if (!(o instanceof BodySpecialAdvBean))
            return false;
        game.getShip().setDocked(true);
        AdvEventLogic.fireEvent(game, AdvEvent.SHIP_DOCK);
        PassengerLogic.disembarkPassengers(game);
        return true;
    }
    
    public static void undock(Game game)
    {
        game.getShip().setDocked(false);
        AdvEventLogic.fireEvent(game, AdvEvent.SHIP_UNDOCK);
    }

    public static PopulatedStatsBean findNearestPopulatedStats(BodyBean body)
    {
        if (body == null)
            return null;
        if (body instanceof BodyPopulated)
            return ((BodyPopulated)body).getPopulatedStats();
        if ((body.getPrimary() != null) && (body.getPrimary() instanceof BodyPopulated))
            return ((BodyPopulated)body.getPrimary()).getPopulatedStats();
        for (Iterator i = body.getSystem().getSystemRoot().getAllSatelitesIterator(); i.hasNext(); )
        {
            BodyBean b = (BodyBean)i.next();
            if (b.isMainworld())
                return ((BodyPopulated)b).getPopulatedStats();
        }
        return null;
    }
    
    public static int calcTimeBetween(String originURI, String destURI, IGenScheme scheme)
    {
        DistCapabilities caps = new DistCapabilities();
        caps.setAcceleration(1.0);
        caps.setFuelPerMinute(.01);
        caps.setJumpRange(1);
        caps.setVolume(1000.0);
        List<DistTransition> traverse;
        try
        {
            traverse = TraverseLogic.calcTraverse(originURI, destURI, caps, scheme);
        }
        catch (TraverseException e)
        {
            return 0;
        }
        DistConsumption con = ConsumptionLogic.calcConsumption(traverse, caps);
        return con.getTime().getMinutes();
    }

    /**
     * @param origin
     * @param destination
     * @return
     */
    public static boolean isSameSystem(String origin, String destination)
    {
        return SchemeLogic.isSameOrds(origin, destination);
    }
}
