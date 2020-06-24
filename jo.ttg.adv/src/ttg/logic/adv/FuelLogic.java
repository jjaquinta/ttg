/*
 * Created on Jan 7, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.logic.adv;

import java.util.Iterator;

import jo.ttg.beans.mw.UPPPorBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.logic.chr.CharLogic;
import jo.ttg.logic.chr.TaskLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.comp.FuelPurifier;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.ttg.ship.logic.ShipLogic;
import jo.util.utils.obj.StringUtils;
import ttg.beans.adv.AdvEvent;
import ttg.beans.adv.BodySpecialAdvBean;
import ttg.beans.adv.Game;
import ttg.beans.adv.ShipInst;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FuelLogic
{

    public static boolean canRefuel(Object o)
    {
        if (!(o instanceof BodySpecialAdvBean))
            return false;
        BodySpecialAdvBean body = (BodySpecialAdvBean)o;
        if (body.getSubType() == BodySpecialBean.ST_REFINERY)
            return true;
        if (body.getSubType() == BodySpecialBean.ST_STARPORT)
            return ((UPPPorBean)body.getSpecialInfo()).getHasFuel();
        if (body.getSubType() == BodySpecialBean.ST_SPACEPORT)
            return ((UPPPorBean)body.getSpecialInfo()).getHasFuel();
        if (body.getSubType() == BodySpecialBean.ST_NAVYBASE)
            return true;
        if (body.getSubType() == BodySpecialBean.ST_SCOUTBASE)
            return true;
        if (body.getSubType() == BodySpecialBean.ST_LOCALBASE)
            return true;
        if (body.getSubType() == BodySpecialBean.ST_LABBASE)
            return true;
        return false;
    }

    public static boolean canScoop(String uri, Object o)
    {
        double orbit;
        int off = uri.indexOf("orbit=");
        if (off < 0)
            orbit = 0;
        else
            orbit = FormatUtils.atod(uri.substring(off + 6));
        if (o instanceof BodyGiantBean)
            return orbit < 2.0;
        if (o instanceof BodyWorldBean)
            return (orbit == 0) && ((BodyWorldBean)o).getPopulatedStats().getUPP().getHydro().getValue() >= 2;
        return false;
    }
    
    public static double spaceInTanks(ShipInst ship)
    {
        double left = ship.getStats().getFuel();
        left -= ship.getFuel();
        left -= ship.getUnrefinedFuel();
        return left;
    }
    
    public static double getFuelPrice(Game game)
    {
        double price;
        BodySpecialAdvBean body = (BodySpecialAdvBean)SchemeLogic.getFromURI(game.getScheme(), game.getShip().getLocation());
        switch (body.getSubType())
        {
            case BodySpecialAdvBean.ST_REFINERY:
                price = 100.0;
            	break;
            case BodySpecialAdvBean.ST_LABBASE:
                price = 2000.0;
            	break;
            case BodySpecialAdvBean.ST_NAVYBASE:
            case BodySpecialAdvBean.ST_LOCALBASE:
            case BodySpecialAdvBean.ST_SCOUTBASE:
                price = 1000.0;
            	break;
            default:
                price = 500;
            	break;
        }
        price *= ReputationLogic.getLocationModifier(game, game.getShip().getLocation());
        return price;
    }

    public static void buyFuel(Game game)
    {
        double needed = spaceInTanks(game.getShip());
        if (needed <= 0)
            return;
        double cost = getFuelPrice(game)*needed;
        if (!MoneyLogic.debitFromCash(game, cost, "Purchase of "+FormatUtils.sTons((int)needed)+" of fuel"))
        {
            AdvEventLogic.fireEvent(game, AdvEvent.FUEL_BUY_NO_MONEY, new Double(needed), new Double(cost));
            return;
        }
        addRefined(game.getShip(), needed);
        AdvEventLogic.fireEvent(game, AdvEvent.FUEL_BUY, new Double(needed), new Double(cost));
    }
    
    public static void scoopFuel(Game game)
    {
        double needed = spaceInTanks(game.getShip());
        if (needed <= 0)
            return;
        BodyBean location = (BodyBean)SchemeLogic.getFromURI(game.getScheme(), game.getShip().getLocation());
        String skill1 = "Pilot";
        String skill2;
        int time;
        if (location instanceof BodyGiantBean)
        {	// skim from gas giant
            skill2 = "Navigation";
            time = 60;
        }
        else
        {
            skill2 = "Sensor Ops";
            time = 4*60;
        }
        if (TaskLogic.attemptFatefulTask(TaskLogic.DIFF_ROUTINE, game.getRnd(), 
                CharLogic.findBestSkill(game.getShip().getCrew(), skill1), CharLogic.findBestSkill(game.getShip().getCrew(), skill2)))
        {
            addUnrefined(game.getShip(), needed);
            AdvEventLogic.fireEvent(game, AdvEvent.FUEL_SCOOPED, new Double(needed));
        }
        else
        {
            AdvEventLogic.fireEvent(game, AdvEvent.FUEL_SCOOPED_FAILED, new Double(needed));
            time /= 2;
        }
        TimeLogic.consumeTime(game, time);
    }
    
    public static double getPurificationRate(ShipBean ship)
    {
        double fuelPurificationRate = 0;
		for (Iterator i = ShipLogic.getComponentList(ship).iterator(); i.hasNext(); )
		{
			ShipComponent comp = (ShipComponent)i.next();
			if (comp instanceof FuelPurifier)
				fuelPurificationRate += ((FuelPurifier)comp).getRate();
		}
        return fuelPurificationRate;
    }
    
    public static void addRefined(ShipInst ship, double amnt)
    {
        ship.setFuel(ship.getFuel() + amnt);
    }
    
    public static void addUnrefined(ShipInst ship, double amnt)
    {
        ship.setUnrefinedFuel(ship.getUnrefinedFuel() + amnt);
    }

    /**
     * @param ship
     * @param minutes
     */
    public static void purify(ShipInst ship, int minutes)
    {
        if (ship.getUnrefinedFuel() <= 0.0)
            return;
            double rate = FuelLogic.getPurificationRate(ship.getDesign());
        if (rate <= 0)
            return;
        double amnt = rate*minutes/60.0;
        amnt = Math.max(amnt, FuelLogic.spaceInTanks(ship));
        amnt = Math.min(amnt, ship.getUnrefinedFuel());
        FuelLogic.addRefined(ship, amnt);
        FuelLogic.addUnrefined(ship, -amnt);
    }
}
