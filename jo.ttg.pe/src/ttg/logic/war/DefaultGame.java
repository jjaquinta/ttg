package ttg.logic.war;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.util.utils.io.ResourceUtils;
import ttg.beans.war.Game;
import ttg.beans.war.Ship;
import ttg.beans.war.Side;

public class DefaultGame
{
	private static String[] mInternalGames = 
	{
		"sancor1",
		"sancor2",
		"sancor3",
		"core",
		"islands",
	};
	
	public static Game getDefaultGame() throws IOException
	{
		return getInternalGame(mInternalGames[0]);
	}
	
	public static List<Game> getInternalGames() throws IOException
	{
		List<Game> ret = new ArrayList<>();
		for (int i = 0; i < mInternalGames.length; i++)
			ret.add(getInternalGame(mInternalGames[i]));
		return ret;
	}
	
	public static Game getInternalGame(String gameName) throws IOException
	{
		InputStream is = ResourceUtils.loadSystemResourceStream("games/"+gameName+".txt", DefaultGame.class);
		BufferedReader rdr = new BufferedReader(new InputStreamReader(is));
		Game game = parseGame(rdr);
		rdr.close();
		return game;
	}
	
	public static Game parseGame(BufferedReader rdr) throws IOException
	{
		Game game = new Game();
		for (;;)
		{
			String inbuf = rdr.readLine();
			if (inbuf == null)
				break;
			inbuf = inbuf.trim();
			game.setRawText(game.getRawText() + inbuf + "\n");
			if ((inbuf.length() == 0) || inbuf.startsWith("#"))
				continue;
			int o = inbuf.indexOf("=");
			if (o < 0)
				continue;
			parseLine(game, inbuf.substring(0, o).trim().toLowerCase(), inbuf.substring(o+1).trim());
		}
		addGlobals(game);
		return game;
	}
	
	private static void parseLine(Game game, String key, String value) throws IOException
	{
		if (key.startsWith("side"))
		{
			int num = Integer.parseInt(key.substring(4, 5));
			parseSide(game, num, key.substring(5), value);
		}
		else if (key.equals("upperbounduri"))
			game.setUpperBound(value);
		else if (key.equals("lowerbounduri"))
			game.setLowerBound(value);
		else if (key.equals("vphaveworld"))
			game.setVPHaveWorld(value);
		else if (key.equals("vpgainworld"))
			game.setVPGainWorld(value);
		else if (key.equals("vploseworld"))
			game.setVPLoseWorld(value);
		else if (key.equals("vphaveship"))
			game.setVPHaveShip(value);
		else if (key.equals("vploseship"))
			game.setVPLoseShip(value);
		else if (key.equals("vpgainship"))
			game.setVPGainShip(value);
		else if (key.equals("resourcegenerationformula"))
			game.setResourceGenerationFormula(value);
		else if (key.equals("constructionperturnformula"))
			game.setConstructionPerTurnFormula(value);
		else if (key.equals("allowconvertneutral"))
			game.setAllowConvertNeutral(value.equalsIgnoreCase("true"));
		else if (key.equals("allowomniscentsensors"))
			game.setAllowOmniscentSensors(value.equalsIgnoreCase("true"));
		else if (key.equals("allowfleetreconfiguration"))
			game.setAllowFleetReconfiguration(value.equalsIgnoreCase("true"));
		else if (key.equals("allowintrinsicdefense"))
			game.setAllowIntrinsicDefense(value.equalsIgnoreCase("true"));
		else if (key.equals("allowconstruction"))
			game.setAllowConstruction(value.equalsIgnoreCase("true"));
		else if (key.equals("gamelength"))
			game.setGameLength(Integer.parseInt(value));
		else if (key.equals("name"))
			game.setName(value);
		else if (key.equals("author"))
			game.setAuthor(value);
		else if (key.equals("version"))
			game.setVersion(value);
		else if (key.equals("description"))
			game.setDescription(game.getDescription() + " " + value);
		else if (key.equals("override"))
			game.getOverrides().add(value);
	}
	
	private static void parseSide(Game game, int num, String key, String value) throws IOException
	{
		while (game.getSides().size() < num)
			game.getSides().add(new Side()); 
		Side side = (Side)game.getSides().get(num - 1);
		if (key.equals("name"))
			side.setName(value);
		else if (key.equals("worlduri"))
			side.getSetupWorlds().add(value);
		else if (key.equals("ship"))
		{
			Ship ship = parseShip(num - 1, value);
			game.getShips().add(ship);
			int off = value.indexOf(",x");
			if (off > 0)
			{
				int mult = FormatUtils.atoi(value.substring(off+2));
				while (--mult > 0)
				{
					Ship additional = new Ship();
					ShipLogic.copy(ship, additional);
					game.getShips().add(additional);
				}
			}
		}
		else if (key.equals("vp"))
			side.setVictoryPoints(Integer.parseInt(value));
		else if (key.equals("resources"))
			side.setStartingResources(Integer.parseInt(value));
	}
	
	// Name,Att,Def,Cap,Jump
	private static Ship parseShip(int side, String inbuf) throws IOException
	{
		Ship ret = new Ship();
		parseShip(ret, inbuf);
		ret.setSide(side);
		return ret;
	}
	
	// Name,Att,Def,Cap,Jump
	public static void parseShip(Ship ship, String inbuf) throws IOException
	{
		StringTokenizer st = new StringTokenizer(inbuf, ",");
		if (st.countTokens() < 5)
			throw new IOException("Incorrect ship initialization string '"+inbuf+"'");
		ship.setName(st.nextToken());
		ship.setAttack(Integer.parseInt(st.nextToken()));
		ship.setDefense(Integer.parseInt(st.nextToken()));
		String cap = st.nextToken();
		ship.setJump(Integer.parseInt(st.nextToken()));
		int extraJumps;
		int rawCap = 0;
		int o = cap.indexOf(".");
		if (o > 0)
		{
			rawCap = Integer.parseInt(cap.substring(0, o))*100;
			extraJumps = Integer.parseInt(cap.substring(o+1));
		}
		else
		{
			rawCap = Integer.parseInt(cap)*100;
			extraJumps = 0;
		}
		if (ship.getJump() > 0)
		{
			for (;;)
			{
				int targetCap = rawCap + ShipLogic.fuelForJump1(ship)*extraJumps;
				if (ship.getCapacity() == targetCap)
					break;
				ship.setCapacity(targetCap);
			}
		}
		else
		{
			ship.setCapacity(rawCap + extraJumps);
		}
	}
	
	private static String[] mShipLibrary =
	{
		"Dreadnought,6,12,0,2",
		"Battleship,6,6,0,2",
		"Far Battleship,5,6,0.2,2",
		"Carrier Battleship,6,6,3.0,2",
		"Cruiser,4,4,0,2",
		"Far Cruiser,4,4,0.2,2",
		"Carrier Cruiser,4,4,2.0,2",
		"Destroyer,2,2,0,2",
		"Far Destroyer,2,2,0.2,2",
		"Carrier Destroyer,2,2,2.0,2",
		"Fighter,1,0,0,0",
		"Heavy Fighter,1,1,0,0",
		"Light SDB,1,2,0,0",
		"SDB,2,4,0,0",
		"Heavy SDB,3,6,0,0",
		"Scout,0,1,0,2",
		"Battleship Carrier,0,1,6.0,2",
		"Cruiser Carrier,0,1,4.0,2",
		"Destroyer Carrier,0,1,2.0,2",
	};
	
	private static void addGlobals(Game game)
	{
		for (int i = 0; i < mShipLibrary.length; i++)
		{
			Ship ship = new Ship();
			try
			{
				parseShip(ship, mShipLibrary[i]);
				ShipLogic.addUnique(game.getShipLibrary(), ship);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
