/*
 * Created on Dec 7, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.dist;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.LocationURI;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.dist.DistCapabilities;
import jo.ttg.beans.dist.DistLocation;
import jo.ttg.beans.dist.DistTransition;
import jo.ttg.beans.dist.DistTransitionJump;
import jo.ttg.beans.dist.DistTransitionOrbit;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.LocationURILogic;
import jo.ttg.logic.gen.BodyLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.StringUtils;

/**
 * @author jgrant
 * 
 * Supported URIs: On surface of a world:
 * body://[x,y,z]/star/giant/world?orbit=0 In orbit around a world:
 * body://[x,y,z]/star/giant/world?orbit=x Interstellar space:
 * sys://[x,y,z]/?destSys=[x,y,z]&timeLeft=h where h is in hours
 *  
 */
public class TraverseLogic
{
    public static List<DistTransition> calcTraverse(String originURI,
            String destinationURI, DistCapabilities caps, IGenScheme scheme)
            throws TraverseException
    {
        List<DistTransition> route = new ArrayList<DistTransition>();
        if (originURI.equals(destinationURI) || StringUtils.isTrivial(originURI) || StringUtils.isTrivial(destinationURI))
            return route;
        DistLocation origin = new DistLocation();
        DistLocationLogic.setURI(origin, originURI);
        DistLocation destination = new DistLocation();
        DistLocationLogic.setURI(destination, destinationURI);
        if (origin.equals(destination))
            return route;
        calcTraverse(origin, destination, caps, route, scheme);
        return route;
    }

    private static void calcTraverse(DistLocation origin,
            DistLocation destination, DistCapabilities caps, List<DistTransition> route,
            IGenScheme scheme) throws TraverseException
    {
        if (origin.getOrds().equals(destination.getOrds()))
            appendWithinSystem(origin, destination, caps, route, scheme);
        else
        {
            if (!DistLocationLogic.isInterstellar(origin))
                appendToJump(origin, caps, route, scheme);
            appendInterstellar(origin, destination, caps, route, scheme);
            appendFromJump(destination, caps, route, scheme);
        }
    }

    private static void appendWithinSystem(DistLocation origin,
            DistLocation destination, DistCapabilities caps, List<DistTransition> route,
            IGenScheme scheme) throws TraverseException
    {
        BodyBean outboundBody = findBody(origin, scheme);
        BodyBean inboundBody = findBody(destination, scheme);
        if (outboundBody == null)
            throw new TraverseException("Cannot find point of origin coming from "+origin.getURI());
        if (inboundBody == null)
            throw new TraverseException("Cannot find point of origin going to "+destination.getURI());
        BodyBean commonParent = BodyLogic.findCommonParent(outboundBody, inboundBody);
        double outboundOrbit = origin.getOrbit();
        for (BodyBean b = outboundBody; !b.getName().equals(commonParent.getName()); b = b.getPrimary())
        {
            if (b.getDiameter() > 0)
                route.add(new DistTransitionOrbit(b.getURI(), outboundOrbit, 100, b.getDiameter()));
            outboundOrbit = BodyLogic.getDiametersFromPrimary(b);
        }
        int end = route.size();
        double inboundOrbit = destination.getOrbit();
        for (BodyBean b = inboundBody; !b.getName().equals(commonParent.getName()); b = b.getPrimary())
        {
            if (b.getDiameter() > 0)
                route.add(end, new DistTransitionOrbit(b.getURI(), 100, inboundOrbit, b.getDiameter()));
            inboundOrbit = BodyLogic.getDiametersFromPrimary(b);
        }
        if (!DoubleUtils.equals(outboundOrbit, inboundOrbit) || (commonParent.getType() == BodyBean.BT_TOIDS))
        {
            route.add(end, new DistTransitionOrbit(commonParent.getURI(), outboundOrbit, inboundOrbit, commonParent.getDiameter()));
        }
    }

    private static void appendToJump(DistLocation origin,
            DistCapabilities caps, List<DistTransition> route, IGenScheme scheme)
            throws TraverseException
    {
        BodyBean body = findBody(origin, scheme);
        // first get me to 100 diameters
        if ((body.getDiameter() > 0) && (origin.getOrbit() < 100))
        {
            route.add(new DistTransitionOrbit(body.getURI(), origin
                    .getOrbit(), 100, body.getDiameter()));
        }
        while (!BodyLogic.is100DiametersFromAllPrimaries(body))
        {
            route.add(new DistTransitionOrbit(body.getPrimary().getURI(),
                    BodyLogic.getDiametersFromPrimary(body), 100, body.getPrimary().getDiameter()));
            body = body.getPrimary();
        }
    }

    private static void appendFromJump(DistLocation destination,
            DistCapabilities caps, List<DistTransition> route, IGenScheme scheme)
            throws TraverseException
    {
        int end = route.size();
        BodyBean body = findBody(destination, scheme);
        // first get me in from 100 diameters
        if ((body.getDiameter() > 0) && (destination.getOrbit() < 100))
        {
            route.add(end, new DistTransitionOrbit(body.getURI(), 100,
                    destination.getOrbit(), body.getDiameter()));
        }
        while (!BodyLogic.is100DiametersFromAllPrimaries(body))
        {
            route.add(end, new DistTransitionOrbit(body.getPrimary().getURI(),
                    100, BodyLogic.getDiametersFromPrimary(body), body.getPrimary().getDiameter()));
            body = body.getPrimary();
        }
    }

    private static void appendInterstellar(DistLocation origin,
            DistLocation destination, DistCapabilities caps, List<DistTransition> route,
            IGenScheme scheme) throws TraverseException
    {
        OrdBean originOrds = origin.getOrds();
        OrdBean destinationOrds = destination.getOrds();
        if (DistLocationLogic.isInterstellar(origin))
        {
            double dist = scheme.distance(originOrds, origin.getDestOrds());
            double time = origin.getDestTimeLeft().getMinutes();
            route.add(new DistTransitionJump("sys://"+origin.getOrds().toURIString(), "sys://"+origin.getDestOrds().toURIString(), dist, time));
            if (origin.getDestOrds().equals(destinationOrds))
                return;
            originOrds = origin.getDestOrds();
        }
        for (;;)
        {
            double dist = scheme.distance(originOrds, destinationOrds);
            if (dist <= caps.getJumpRange())
            { // we are one jump away
                route.add(new DistTransitionJump("sys://" + originOrds.toURIString(),
                        "sys://" + destinationOrds.toURIString(), dist, 7*24*60));
                break;
            }
            else
            { // we are more than one jump away
                OrdBean interimOrds = findClosestTo(originOrds,
                        destinationOrds, caps.getJumpRange(), scheme);
                //System.out.println("TraverseLogic.appendInterestellar, dist="+dist+", originOrds="+originOrds+", interimOrds="+interimOrds+", destinationOrds="+destinationOrds);
                dist = scheme.distance(originOrds, interimOrds);
                if (interimOrds == null)
                    throw new TraverseException("Route dead-ends at "
                            + origin.getOrds());
                route.add(new DistTransitionJump("sys://" + originOrds,
                        "sys://" + interimOrds, dist, 7*24*60));
                originOrds = interimOrds;
            }
        }
    }

    private static OrdBean findClosestTo(OrdBean origin, OrdBean dest,
            double jumpRange, IGenScheme scheme) throws TraverseException
    {
        List<MainWorldBean> range = SchemeLogic.getWorldsWithin(scheme, origin,
                (int)jumpRange);
        //System.out.println("TraverseLogic.findClosestTo "+range.size()+" worlds within "+jumpRange+" of "+origin);
        if (range.size() == 0)
            throw new TraverseException("No worlds within " + jumpRange
                    + " of " + origin.toURIString());
        OrdBean best = null;
        double bestDist = 0;
        for (MainWorldBean mw : range)
        {
            if (mw.getOrds().equals(origin))
                continue;
            double dist = scheme.distance(dest, mw.getOrds());
            if (dist == 0)
                continue;
            if ((best == null) || (dist < bestDist))
            {
                best = mw.getOrds();
                bestDist = dist;
            }
        }
        return best;
    }

    private static BodyBean findBody(DistLocation loc, IGenScheme scheme)
            throws TraverseException
    {
        DistLocationLogic.resetURI(loc);
        String bodyURI = "body:" + loc.getURI().substring(4); // strip sys:
        return (BodyBean)SchemeLogic.getFromURI(scheme, bodyURI);
    }

    /**
     * @param trans
     * @param pcFuel
     * @return
     */
    public static String getIncrementalLocation(DistTransition trans, double pc)
    {
        String ret = null;
        if (trans instanceof DistTransitionJump)
        {
            DistTransitionJump transJump = (DistTransitionJump)trans;
            LocationURI loc = LocationURILogic.fromURI(transJump.getOriginURI());
            LocationURI d = LocationURILogic.fromURI(transJump.getDestinationURI());
            loc.setParam("destSys", d.getOrds().toURIString());
            double timeLeft = transJump.getTimeLeft()*(1.0 - pc);
            loc.setParam("timeLeft", String.valueOf(timeLeft));
            ret = LocationURILogic.getURI(loc);
        }
        else if (trans instanceof DistTransitionOrbit)
        {
            DistTransitionOrbit transOrbit = (DistTransitionOrbit)trans;
            LocationURI loc = LocationURILogic.fromURI(transOrbit.getBodyURI());
            double orbit = (transOrbit.getDestinationOrbit() - transOrbit.getOriginOrbit())*pc + transOrbit.getOriginOrbit();
            loc.setParam("orbit", String.valueOf(orbit));
            ret = LocationURILogic.getURI(loc);
        }
        return ret;
    }
}