/*
 * Created on Jan 13, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.logic.adv;

import java.util.List;

import jo.util.beans.Bean;
import ttg.beans.adv.Game;
import ttg.beans.adv.UNIDInst;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UNIDLogic
{
	public static void use(Game game, Bean unidObj, int type, long expiresInDays)
	{
		UNIDInst ui = new UNIDInst();
		ui.setOID(unidObj.getOID());
		ui.setType(type);
		ui.setExpiryDate(game.getDate().getDays() + expiresInDays*24*60*60);
		game.getUsedUNIDs().add(ui);
		//System.out.println("Using UNID "+type+":"+ui.getUNID());
	}
	
	public static void unuse(Game game, Bean unidObj, int type)
	{
		//System.out.println("Unusing UNID "+type+":"+unidObj.getUNID());
	    for (UNIDInst ui : game.getUsedUNIDs())
	        if ((ui.getOID() == unidObj.getOID()) && (ui.getType() == type))
	        {
	            game.getUsedUNIDs().remove(ui);
	            break;
	        }
	}
	
	public static boolean isUsed(Game game, Bean unidObj, int type)
	{
		//System.out.print("Used? "+type+":"+unidObj.getUNID());
        for (UNIDInst ui : game.getUsedUNIDs())
			if ((ui.getOID() == unidObj.getOID()) && (ui.getType() == type))
			{
				//System.out.println("  YES");
				return true;
			}
		//System.out.println("  NO");
		return false;
	}
	
	public static void purgeExpired(Game game)
	{
		long now = game.getDate().getDays();
		Object[] uis = game.getUsedUNIDs().toArray();
		for (int i = 0; i < uis.length; i++)
		{
			UNIDInst ui = (UNIDInst)uis[i];
			if (ui.getExpiryDate() > now)
				game.getUsedUNIDs().remove(uis[i]);
		}
	}

	/**
	 * @param mGame
	 * @param ships
	 */
	public static void purgeUsed(Game game, List<?> objs, int type)
	{
		Object[] beans = objs.toArray();
		for (int i = 0; i < beans.length; i++)
		{
			Bean b = (Bean)beans[i];
			if (isUsed(game, b, type))
				objs.remove(beans[i]);
		}
	}
}
