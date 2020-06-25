/*
 * Created on Dec 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.adv.logic;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.json.simple.FromJSONLogic;
import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;
import org.json.simple.ToJSONLogic;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.LocationURI;
import jo.ttg.gen.IGenLanguage;
import jo.ttg.logic.DateLogic;
import jo.ttg.logic.chr.CharLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.ship.beans.ShipStats;
import jo.ttg.ship.logic.ShipReport;
import ttg.adv.beans.AccountsBean;
import ttg.adv.beans.AdvEvent;
import ttg.adv.beans.CrewBean;
import ttg.adv.beans.Game;
import ttg.adv.beans.ShipInst;
import ttg.adv.logic.gen.AdvGenScheme;
import ttg.adv.logic.h.GameHandler;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GameLogic
{
    private static boolean mInitialized = false;
    
    public static void init()
    {
        if (!mInitialized)
        {
            SchemeLogic.setDefaultScheme(new AdvGenScheme());
            ToJSONLogic.addHandler(new GameHandler());
            mInitialized = true;
        }
    }
    
	public static Game newGame()
	{
	    init();
		Game game = new Game();
		game.setScheme(SchemeLogic.getDefaultScheme());
		DateBean date = new DateBean();
		date.setYear(1110);
		game.setDate(date);
		setupShip(game);
		setupCrew(game);
		setupAccounts(game);
		AdvEventLogic.fireEvent(game, AdvEvent.GAME_NEW);
		return game;
	}
	
	private static void setupShip(Game game)
	{
		ShipInst ship = game.getShip();
		ship.setDesign(ShipLogic.makeDefaultShip());
		ShipStats stats = ShipReport.report(ship.getDesign());
		ship.setStats(stats);
		ship.setName(game.getShip().getDesign().getName());
		ship.setLocation("body://20,23,0/Piran/Lunion/Lunion+System+Starport");
		ship.setDocked(true);
		ship.setDestination("body://20,23,0/Piran/Lunion/Lunion+System+Starport");
		ship.setFuel(stats.getFuel());
	}
	
	private static void setupCrew(Game game)
	{
	    LocationURI loc = new LocationURI(game.getShip().getLocation());
		IGenLanguage lang = game.getScheme().getGeneratorLanguage();
	    ShipInst ship = game.getShip();
	    int[] crewNeeded = CrewLogic.neededCrew(ship);
	    for (int i = 0; i < crewNeeded.length; i++)
	    {
	        if (crewNeeded[i] <= 0)
	            continue;
	        String[] skills = CrewLogic.jobSkills(i);
	        for (int j = 0; crewNeeded[i] > 0; j++)
	        {
	            CrewBean crewman = new CrewBean();
	            CharLogic.generate(null, skills[j%skills.length]+"-1", game.getRnd(), crewman);
	            if (CrewLogic.qualifiedFor(crewman, i))
	            {
        			crewman.setName(lang.generatePersonalName(loc.getOrds(), loc.getOrds(), "Im", game.getRnd()));
	                BuyLogic.hireCrew(game, ship, crewman);
	                CrewLogic.assignJob(ship, crewman, i);
	                crewNeeded[i]--;
	            }
	        }
	    }
	}
	
	private static void setupAccounts(Game game)
	{
		AccountsBean ac = game.getAccounts();
		double shipValue = game.getShip().getStats().getRawCost();
		ac.setLoanPaymentsLeft(240);
		ac.setLoanPaymentAmount(shipValue/ac.getLoanPaymentsLeft());
		ac.getLoanPaymentInterval().setMinutes(4*7*24*60); // every two weeks
		ac.getLoanPaymentDue().set(game.getDate());
		DateLogic.incrementMinutes(ac.getLoanPaymentDue(), ac.getLoanPaymentInterval().getMinutes());
		ac.setCash(ac.getLoanPaymentAmount()*2);
	}
	
	public static void save(Game game)
	{
		UNIDLogic.purgeExpired(game);
		JSONObject json = (JSONObject)ToJSONLogic.toJSON(game);
		try
        {
            JSONUtils.writeJSON(new File(game.getSaveFile()), json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
		game.setAnyChange(false);
		AdvEventLogic.fireEvent(game, AdvEvent.GAME_SAVED);
	}
	
	public static Game open(File f)
	{
        init();
	    JSONObject json;
        try
        {
            json = JSONUtils.readJSON(f);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }	    
		Game ret = (Game)FromJSONLogic.fromJSON(json, Game.class);
		if (ret != null)
		{
	        ret.setScheme(SchemeLogic.getDefaultScheme());
			ret.setSaveFile(f.toString()); 
			ret.setAnyChange(false);
			AdvEventLogic.fireEvent(ret, AdvEvent.GAME_LOADED);
		}
		return ret;
	}
	
	public static void status(Game game, String newStatus)
	{
	    List<String> history = game.getStatusHistory();
	    long now = System.currentTimeMillis();
	    if (game.getStatusChange() < now - 30*1000)
	    {
			while (history.size() > 5)
				history.remove(0);
	    }
		history.add(newStatus);
		game.setStatusChange(now);
		game.setStatus(newStatus);
	}
}
