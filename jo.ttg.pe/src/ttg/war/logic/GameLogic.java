/*
 * Created on Dec 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.war.logic;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.json.simple.FromJSONLogic;
import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;
import org.json.simple.ToJSONLogic;

import jo.ttg.gen.gni.GNIGenSchemeKnownWorld;
import jo.ttg.logic.gen.SchemeLogic;
import ttg.war.beans.GameInst;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GameLogic
{
	public static void status(GameInst game, String newStatus)
	{
		List<String> history = game.getStatusHistory();
		while (history.size() > 5)
			history.remove(0);
		history.add(newStatus);
		game.setStatus(newStatus);
	}
	
	public static GameInst load(File file)
	{
	    if (!file.exists())	        
	        return null;
	    try
        {
            JSONObject json = JSONUtils.readJSON(file);
            GameInst game = new GameInst();
            game.setScheme(new GNIGenSchemeKnownWorld());
            FromJSONLogic.fromJSONInto(json, game);
            SchemeLogic.setDefaultScheme(game.getScheme());
            return game;
        }
        catch (IOException e)
        {
            return null;
        }
	}
    
    public static void save(GameInst game, File file)
    {
        JSONObject json = (JSONObject)ToJSONLogic.toJSON(game);
        try
        {
            JSONUtils.writeJSON(file, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
