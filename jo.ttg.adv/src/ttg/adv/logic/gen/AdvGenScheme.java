/*
 * Created on Dec 24, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.logic.gen;

import jo.ttg.gen.gni.GNIGenSchemeKnownWorld;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AdvGenScheme extends GNIGenSchemeKnownWorld
{
	public AdvGenScheme()
	{
	    super();
	}
	
	protected void initialize()
	{
	    super.initialize();
		mGeneratorSystem = new AdvGenSystem(this);
		mGeneratorCargo = new AdvGenCargo(this);
		mGeneratorPassengers = new AdvGenPassengers(this);
	}

    /**
     * @return
     */
    public static AdvGenScheme newInstance()
    {
   		return new AdvGenScheme();
    }
}
