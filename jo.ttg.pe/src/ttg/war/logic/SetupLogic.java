package ttg.war.logic;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.UPPBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.gen.IGenMainWorld;
import jo.ttg.gen.gni.GNIGenSchemeKnownWorld;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.mw.UPPLogic;
import jo.util.utils.DebugUtils;
import ttg.war.beans.Game;
import ttg.war.beans.GameInst;
import ttg.war.beans.Ship;
import ttg.war.beans.ShipInst;
import ttg.war.beans.Side;
import ttg.war.beans.SideInst;
import ttg.war.beans.WorldInst;

public class SetupLogic
{
	public static GameInst newGame() throws IOException
	{
		return newGame(DefaultGame.getDefaultGame());
	}
	
    public static GameInst newGame(Game baseGame) throws IOException
    {
    	GameInst game = new GameInst();
    	game.setGame(baseGame);
    	game.setTurn(0);
    	game.setPhase(0);
		game.setScheme(new GNIGenSchemeKnownWorld());
		SchemeLogic.setDefaultScheme(game.getScheme());
    	setupWorlds(game);
		setupSides(game);
		setupShips(game);
    	GameLogic.status(game, "New game");
    	return game;
    }
    
    private static void setupWorlds(GameInst game)
    {
		Object obj = SchemeLogic.getFromURI(game.getScheme(), game.getGame().getUpperBound());
		if (obj instanceof SectorBean)
			game.setUpperBound(((SectorBean)obj).getUpperBound()); 
		else if (obj instanceof SubSectorBean)
			game.setUpperBound(((SubSectorBean)obj).getUpperBound()); 
		else if (obj instanceof MainWorldBean)
			game.setUpperBound(((MainWorldBean)obj).getOrds()); 
		else if (obj instanceof OrdBean)
			game.setUpperBound((OrdBean)obj); 
		obj = SchemeLogic.getFromURI(game.getScheme(), game.getGame().getLowerBound());
		if (obj instanceof SectorBean)
			game.setLowerBound(((SectorBean)obj).getLowerBound()); 
		else if (obj instanceof SubSectorBean)
			game.setLowerBound(((SubSectorBean)obj).getLowerBound()); 
		else if (obj instanceof MainWorldBean)
			game.setLowerBound(((MainWorldBean)obj).getOrds()); 
		else if (obj instanceof OrdBean)
			game.setLowerBound((OrdBean)obj); 
		OrdBean ub = game.getUpperBound();
		OrdBean lb = game.getLowerBound();

		IGenMainWorld gen = game.getScheme().getGeneratorMainWorld();
		for (long j = ub.getY(); j < lb.getY(); j++)
		{
			for (long i = ub.getX(); i < lb.getX(); i++)
			{
				OrdBean o = new OrdBean(i, j, ub.getZ());
				MainWorldBean world = gen.generateMainWorld(o);
				WorldInst worldInst = new WorldInst();
				worldInst.setWorld(world);
				worldInst.setOrds(o);
				game.getWorlds().put(o.toString(), worldInst);
			}
		}
		
		for (String inbuf : game.getGame().getOverrides())
		{
			StringTokenizer st = new StringTokenizer(inbuf, ",");
			if (st.countTokens() < 2)
				continue;
			String uri = st.nextToken();
			String data = st.nextToken();
			Object o = SchemeLogic.getFromURI(game.getScheme(), uri);
			if (o instanceof MainWorldBean)
			{
				MainWorldBean mw = (MainWorldBean)o;
				String upp;
				if (data.length() > 9)
				{
					mw.setName(data.substring(0, data.length() - 9).trim());
					upp = data.substring(data.length() - 9);
				}
				else
					upp = data;
				UPPBean UPP = mw.getPopulatedStats().getUPP();
				UPP.getPort().setValue(upp.charAt(0));
				UPP.getSize().setValue(FormatUtils.upp2int(upp.charAt(1)));
				UPP.getAtmos().setValue(FormatUtils.upp2int(upp.charAt(2)));
				UPP.getHydro().setValue(FormatUtils.upp2int(upp.charAt(3)));
				UPP.getPop().setValue(FormatUtils.upp2int(upp.charAt(4)));
				UPP.getGov().setValue(FormatUtils.upp2int(upp.charAt(5)));
				UPP.getLaw().setValue(FormatUtils.upp2int(upp.charAt(6)));
				UPP.getTech().setValue(FormatUtils.upp2int(upp.charAt(8)));
				UPPLogic.updateTradeCodes(UPP);
			}
			else
			{
				DebugUtils.debug("Can't find "+uri);
			}
		}
    }
    
    private static Color[] mColors1 =
    {
		new Color(128, 0, 0), new Color(0, 128, 0), new Color(0, 0, 128), Color.BLACK, new Color(0xAA, 0, 0xFF),  
    };
	private static Color[] mColors2 =
	{
		Color.LIGHT_GRAY, Color.YELLOW,  
	};
    
    private static void setupSides(GameInst game)
    {
    	int idx = 0;
    	for (Side side : game.getGame().getSides())
    	{
    		SideInst sideInst = new SideInst();
    		sideInst.setSide(side);
    		sideInst.setVictoryPoints(side.getVictoryPoints());
			sideInst.setResources(side.getStartingResources());
			sideInst.setIndex(idx);
    		sideInst.setColor1(mColors1[idx%mColors1.length]);
			sideInst.setColor2(mColors2[idx%mColors2.length]);
			for (String uri : side.getSetupWorlds())
			{
				Object obj = SchemeLogic.getFromURI(game.getScheme(), uri);
				if (obj instanceof SectorBean)
					addToSide(game, sideInst, (SectorBean)obj);
				else if (obj instanceof SubSectorBean)
					addToSide(game, sideInst, (SubSectorBean)obj);
				else if (obj instanceof MainWorldBean)
					addToSide(game, sideInst, (MainWorldBean)obj);
			}
			game.getSides().add(sideInst);
    	}
    }
    
	private static void addToSide(GameInst game, SideInst sideInst, SectorBean sec)
	{
		for (Iterator<SubSectorBean> i = sec.getSubSectorsIterator(); i.hasNext(); )
		{
			SubSectorBean sub = i.next();
			addToSide(game, sideInst, sub);
		}
	}
    
	private static void addToSide(GameInst game, SideInst sideInst, SubSectorBean sub)
	{
		for (Iterator<MainWorldBean> i = sub.getMainWorldsIterator(); i.hasNext(); )
		{
			MainWorldBean mw = i.next();
			addToSide(game, sideInst, mw);
		}
	}
    
    private static void addToSide(GameInst game, SideInst sideInst, MainWorldBean mw)
    {
		WorldInst world = WorldLogic.getWorld(game, mw);
		sideInst.getWorlds().add(world);
		world.setSide(sideInst);
		SideLogic.victoryPoints(sideInst, WorldLogic.eval(world, game.getGame().getVPHaveWorld()));
    }
    
	private static void setupShips(GameInst game)
	{
		for (Ship ship : game.getGame().getShips())
		{
			ShipInst shipInst = new ShipInst();
			shipInst.setShip(ship);
			SideInst side = (SideInst)game.getSides().get(ship.getSide());
			side.getShips().add(shipInst);
			shipInst.setSideInst(side);
			game.getShips().add(shipInst);
			SideLogic.victoryPoints(side, ShipLogic.eval(shipInst, game.getGame().getVPHaveShip()));
			ShipLogic.setupFuel(shipInst);
		}
		for (SideInst sideInst : game.getSides())
			ShipLogic.autoDock(sideInst.getShips(), null);
	}
}
