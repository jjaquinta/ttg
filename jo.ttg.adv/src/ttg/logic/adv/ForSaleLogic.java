/*
 * Created on Jan 9, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.logic.adv;

import java.beans.XMLDecoder;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.chr.CharBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.UPPPorBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyPopulated;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.beans.trade.PassengersBean;
import jo.ttg.gen.IGenCargoEx;
import jo.ttg.gen.IGenLanguage;
import jo.ttg.gen.IGenPassengersEx;
import jo.ttg.logic.RandLogic;
import jo.ttg.logic.chr.CharLogic;
import jo.ttg.ship.beans.ShipBean;
import jo.util.utils.DebugUtils;
import ttg.beans.adv.BodySpecialAdvBean;
import ttg.beans.adv.CrewBean;
import ttg.beans.adv.Game;
import ttg.beans.adv.PassengerBean;
import ttg.beans.adv.UNIDInst;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ForSaleLogic
{
	private static final String SHIP_PACKAGE = "ships/";
	private static final String[] shipTemplates = 
	{
		"TL12FarFatMerchant.ship.xml",
		"TL12FarMerchant.ship.xml",
		"TL12FarMerchantHopper.ship.xml",
		"TL12FarScout.ship.xml",
		"TL12FarStretchMerchant.ship.xml",
		"TL12FatMerchant.ship.xml",
		"TL12Merchant.ship.xml",
		"TL12MerchantHopper.ship.xml",
		"TL12Scout.ship.xml",
		"TL12ShortSupportCorvette.ship.xml",
		"TL12ShortSupportDestroyer.ship.xml",
		"TL12ShortSupportEscort.ship.xml",
		"TL12StretchMerchant.ship.xml",
		"TL13FarFatMerchant.ship.xml",
		"TL13FarMerchant.ship.xml",
		"TL13FarMerchantHopper.ship.xml",
		"TL13FarScout.ship.xml",
		"TL13FarStretchMerchant.ship.xml",
		"TL13FarSupportCorvette.ship.xml",
		"TL13FarSupportDestroyer.ship.xml",
		"TL13FarSupportEscort.ship.xml",
		"TL13FastScout.ship.xml",
		"TL13FatMerchant.ship.xml",
		"TL13Merchant.ship.xml",
		"TL13MerchantHopper.ship.xml",
		"TL13Scout.ship.xml",
		"TL13ShortFightingCorvette.ship.xml",
		"TL13ShortFightingDestroyer.ship.xml",
		"TL13ShortFightingEscort.ship.xml",
		"TL13ShortSupportCorvette.ship.xml",
		"TL13ShortSupportDestroyer.ship.xml",
		"TL13ShortSupportEscort.ship.xml",
		"TL13StretchMerchant.ship.xml",
		"TL14Courier.ship.xml",
		"TL14FarFatMerchant.ship.xml",
		"TL14FarMerchant.ship.xml",
		"TL14FarMerchantHopper.ship.xml",
		"TL14FarScout.ship.xml",
		"TL14FarStretchMerchant.ship.xml",
		"TL14FarSupportCorvette.ship.xml",
		"TL14FarSupportDestroyer.ship.xml",
		"TL14FarSupportEscort.ship.xml",
		"TL14FastScout.ship.xml",
		"TL14FatMerchant.ship.xml",
		"TL14Merchant.ship.xml",
		"TL14MerchantHopper.ship.xml",
		"TL14Scout.ship.xml",
		"TL14ShortFightingCorvette.ship.xml",
		"TL14ShortFightingDestroyer.ship.xml",
		"TL14ShortFightingEscort.ship.xml",
		"TL14ShortSupportCorvette.ship.xml",
		"TL14ShortSupportDestroyer.ship.xml",
		"TL14ShortSupportEscort.ship.xml",
		"TL14StretchMerchant.ship.xml",
		"TL15Courier.ship.xml",
		"TL15FarCourier.ship.xml",
		"TL15FarFatMerchant.ship.xml",
		"TL15FarFightingCorvette.ship.xml",
		"TL15FarFightingDestroyer.ship.xml",
		"TL15FarFightingEscort.ship.xml",
		"TL15FarMerchant.ship.xml",
		"TL15FarMerchantHopper.ship.xml",
		"TL15FarRapidEngagementCorvette.ship.xml",
		"TL15FarRapidEngagementDestroyer.ship.xml",
		"TL15FarRapidEngagementEscort.ship.xml",
		"TL15FarScout.ship.xml",
		"TL15FarStretchMerchant.ship.xml",
		"TL15FarSupportCorvette.ship.xml",
		"TL15FarSupportDestroyer.ship.xml",
		"TL15FarSupportEscort.ship.xml",
		"TL15FastScout.ship.xml",
		"TL15FatMerchant.ship.xml",
		"TL15Merchant.ship.xml",
		"TL15MerchantHopper.ship.xml",
		"TL15RapidDeploymentFightingCorvette.ship.xml",
		"TL15RapidDeploymentFightingDestroyer.ship.xml",
		"TL15RapidDeploymentFightingEscort.ship.xml",
		"TL15RapidDeploymentSupportCorvette.ship.xml",
		"TL15RapidDeploymentSupportDestroyer.ship.xml",
		"TL15RapidDeploymentSupportEscort.ship.xml",
		"TL15Scout.ship.xml",
		"TL15ShortFightingCorvette.ship.xml",
		"TL15ShortFightingDestroyer.ship.xml",
		"TL15ShortFightingEscort.ship.xml",
		"TL15ShortRapidEngagementCorvette.ship.xml",
		"TL15ShortRapidEngagementDestroyer.ship.xml",
		"TL15ShortRapidEngagementEscort.ship.xml",
		"TL15ShortSupportCorvette.ship.xml",
		"TL15ShortSupportDestroyer.ship.xml",
		"TL15ShortSupportEscort.ship.xml",
		"TL15StretchMerchant.ship.xml",
	};
	private static Map<String,ShipBean> shipCache = new HashMap<>();
	
	private static ShipBean loadShip(String which)
	{
		ShipBean ret = shipCache.get(which);
		if (ret == null)
		{
			ret = (ShipBean)loadSystemResource(SHIP_PACKAGE+which);
			shipCache.put(which, ret);
		}
		return ret;
	}

    public static Object loadSystemResource(String path)
    {
        Object ret = null;
        try
        {
            ClassLoader loader = ForSaleLogic.class.getClassLoader();
            InputStream is = loader.getResourceAsStream(path);
            ret = loadStream(is);
            is.close();
        }
        catch (Exception e)
        {
            DebugUtils.error("Error loading system resource "+path);
        }
        return ret;
    }

    public static Object loadStream(InputStream is)
    {
        XMLDecoder dec = new XMLDecoder(is);
        Object ret = dec.readObject();
        dec.close();
        return ret;
    }

	private static void findForSale(ArrayList forSale, MainWorldBean where, long date, int expiryLimit)
	{
		char port = (char)where.getPopulatedStats().getUPP().getPort().getValue();
		if ((port != 'A') && (port != 'B'))
			return;
		int pop = where.getPopulatedStats().getUPP().getPop().getValue();
		int num = pop/3;
		RandBean rnd = new RandBean();
		RandLogic.setMagic(rnd, where.getOrds().hashCode()^date, RandBean.SHIP_MAGIC);
		int amnt = RandLogic.D(rnd, num);
		if (port == 'B')
			amnt /= 2;
		for (int i = 0; i < amnt; i++)
		{
			int expiry = RandLogic.D(rnd, 3);
			long unid = rnd.getSeed();
			if (expiry <= expiryLimit)
				continue;
			ShipBean ship = loadShip(shipTemplates[RandLogic.rand(rnd)%shipTemplates.length]);
			ship.setOID(unid);
			forSale.add(ship);
		}
	}
	
	public static ArrayList genShipsForSale(MainWorldBean where, DateBean date)
	{
		ArrayList ret = new ArrayList();
		for (int exp = 0; exp < 18; exp++)
			findForSale(ret, where, date.getDays()-exp, exp);
		return ret;
	}
	
	public static List<CargoBean> genCargosForSale(Game game, BodyBean ori, DateBean date)
	{
		List<CargoBean> ret = ((IGenCargoEx)game.getScheme().getGeneratorCargo()).generateCargo(ori, date);
		UNIDLogic.purgeUsed(game, ret, UNIDInst.CARGO);
		return ret;
	}
	
	public static List<CargoBean> genFreightForSale(Game game, BodyBean ori, BodyBean dest, DateBean date)
	{
	    List<CargoBean> ret = ((IGenCargoEx)game.getScheme().getGeneratorCargo()).generateFreight(ori, dest, date);
		UNIDLogic.purgeUsed(game, ret, UNIDInst.CARGO);
		return ret;
	}
	
	public static List<PassengerBean> genPassengersForSale(Game game, BodyBean ori, BodyBean dest, DateBean date)
	{
		ArrayList ret = new ArrayList();
		if (ori.getURI().equals(dest.getURI()))
		    return ret;
		List<PassengersBean> passengerList = ((IGenPassengersEx)game.getScheme().getGeneratorPassengers()).generatePassengers(ori, dest, date);
		if (passengerList == null)
		    return ret;
		IGenLanguage lang = game.getScheme().getGeneratorLanguage();
		for (Iterator i = passengerList.iterator(); i.hasNext(); )
		{
		    PassengersBean passengers = (PassengersBean)i.next();
		    RandBean r = new RandBean();
		    r.setSeed(passengers.getSeed());
		    int hml = passengers.getHigh() + passengers.getMiddle() + passengers.getLow();
		    for (int j = 0; j < hml; j++)
		    {
		        PassengerBean p = (PassengerBean)CharLogic.generate(null, null, r, new PassengerBean());
		        p.setOrigin(passengers.getOrigin());
		        p.setDestination(passengers.getDestination());
		        if (j < passengers.getHigh())
		            p.setPassage(PassengerBean.PASSAGE_HIGH);
		        else if (j < passengers.getHigh() + passengers.getMiddle())
		            p.setPassage(PassengerBean.PASSAGE_MIDDLE);
	            else
	                p.setPassage(PassengerBean.PASSAGE_LOW);
				p.setName(lang.generatePersonalName(ori.getSystem().getOrds(), ori.getSystem().getOrds(), 
				        LocationLogic.findNearestPopulatedStats(ori).getAllegiance(), r));
		        ret.add(p);
		    }
		}
		UNIDLogic.purgeUsed(game, ret, UNIDInst.STAFF);
		return ret;
	}
	
	public static ArrayList genStaffForHire(Game game, BodyBean ori, DateBean date)
	{
		ArrayList ret = new ArrayList();
		if (!(ori instanceof BodySpecialAdvBean))
		    return ret;
		String preferredService = null;
		int dice = 0;
		switch (((BodySpecialAdvBean)ori).getSubType())
		{
		    case BodySpecialBean.ST_LABBASE:
		        dice = 1;
		    	preferredService = "Doctors"; 
		        break;
		    case BodySpecialBean.ST_LOCALBASE:
		        dice = 1;
    			preferredService = "Army"; 
		        break;
		    case BodySpecialBean.ST_NAVYBASE:
		        dice = 1;
	    		preferredService = "Navy"; 
		        break;
		    case BodySpecialBean.ST_REFINERY:
		        dice = 1;
    			preferredService = "Other"; 
		        break;
		    case BodySpecialBean.ST_SCOUTBASE:
		        dice = 1;
		    	preferredService = "Scouts"; 
		        break;
		    case BodySpecialBean.ST_SPACEPORT:
				dice += ((BodyPopulated)ori.getPrimary()).getPopulatedStats().getUPP().getPop().getValue()/5;
		    	if (((UPPPorBean)((BodySpecialAdvBean)ori).getSpecialInfo()).getValue() == 'F')
		        	dice += 4; 
		    	else if (((UPPPorBean)((BodySpecialAdvBean)ori).getSpecialInfo()).getValue() == 'G')
		    	    dice += 2;
		        break;
		    case BodySpecialBean.ST_STARPORT:
				dice += ((BodyPopulated)ori.getPrimary()).getPopulatedStats().getUPP().getPop().getValue()/5;
				if (((UPPPorBean)((BodySpecialAdvBean)ori).getSpecialInfo()).getValue() == 'A')
				    dice *= 4; 
				else if (((UPPPorBean)((BodySpecialAdvBean)ori).getSpecialInfo()).getValue() == 'B')
				    dice *= 2;
		        break;
		}
		RandBean r = new RandBean();
		r.setSeed(ori.getOID()^date.getDays());
		int num = RandLogic.D(r, dice);
		IGenLanguage lang = game.getScheme().getGeneratorLanguage();
		while (num-- > 0)
		{
			CharBean ch = CharLogic.generate(preferredService, "Terms-"+RandLogic.D(r, 1), r, new CrewBean());
			ch.setName(lang.generatePersonalName(ori.getSystem().getOrds(), ori.getSystem().getOrds(), 
			        LocationLogic.findNearestPopulatedStats(ori).getAllegiance(), r));
			ret.add(ch); 
		}
		UNIDLogic.purgeUsed(game, ret, UNIDInst.STAFF);
		return ret;
	}
}
