/*
 * Created on Dec 3, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.gen.gni;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.gen.IGenMainWorld;
import jo.ttg.logic.uni.UniverseLogic;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GNIGenMainWorld implements IGenMainWorld
{
	GNIGenScheme   mScheme;

	public GNIGenMainWorld(GNIGenScheme scheme)
	{
		mScheme = scheme;
	}

	/* (non-Javadoc)
	 * @see ttg.gen.GenMainWorld#generateMainWorld(ttg.beans.OrdBean)
	 */
	public MainWorldBean generateMainWorld(OrdBean ords)
	{
		return UniverseLogic.findMainWorld(mScheme.mUniverse, ords);
	}
}
