/*
 * Created on Jan 11, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.logic;

import jo.ttg.beans.LocationURI;
import jo.ttg.beans.OrdBean;
import ttg.adv.beans.Game;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ReputationLogic
{
    public static final String BANK = "imp.bank";
    
    public static void incrementReputation(Game game, String rep, double amnt)
    {
        Double v = (Double)game.getReputation().get(rep);
        if (v == null)
            game.getReputation().put(rep, new Double(amnt));
        else
            game.getReputation().put(rep, new Double(amnt + v.doubleValue()));
    }
    
    public static void decrementReputation(Game game, String rep, double amnt)
    {
        incrementReputation(game, rep, -amnt);
    }
    
    public static double getReputation(Game game, String rep)
    {
        Double val = (Double)game.getReputation().get(rep);
        if (val == null)
            return 0;
        else
            return val.doubleValue();
    }
    
    public static void incrementLocation(Game game, String uri, double amnt)
    {
        // location
        incrementReputation(game, uri, amnt*.66);
        amnt *= .33;
        // system
        LocationURI loc = new LocationURI(uri);
        OrdBean ords = loc.getOrds();
        incrementReputation(game, "sys://"+ords.toURIString(), amnt*.66);
        amnt *= .33;
        // subsector
        game.getScheme().nearestSub(ords);
        incrementReputation(game, "sub://"+ords.toURIString(), amnt*.66);
        amnt *= .33;
        // sector
        game.getScheme().nearestSec(ords);
        incrementReputation(game, "sec://"+ords.toURIString(), amnt);
    }
    
    public static void decrementLocation(Game game, String uri, double amnt)
    {
        incrementLocation(game, uri, -amnt);
    }
    
    public static double getLocationReputation(Game game, String uri)
    {
        double rep = getReputation(game, uri);
        // system
        LocationURI loc = new LocationURI(uri);
        OrdBean ords = loc.getOrds();
        rep += getReputation(game, "sys://"+ords.toURIString());
        // subsector
        game.getScheme().nearestSub(ords);
        rep += getReputation(game, "sub://"+ords.toURIString());
        // sector
        game.getScheme().nearestSec(ords);
        rep += getReputation(game, "sec://"+ords.toURIString());
        return rep;
    }
    
    public static double getLocationModifier(Game game, String rep)
    {
        double val = getLocationReputation(game, rep);
        double mod;
        if (val >= 1)
            mod = 1/(1 + Math.log(val)/Math.log(10));
        else if (val < -1)
            mod = 1 + Math.log(-val)/Math.log(10);
        else
            mod = 1.0;
        //System.out.println("ReputationLogic.repMod: "+rep+"->"+val+"->"+mod);
        return mod;
    }
}
