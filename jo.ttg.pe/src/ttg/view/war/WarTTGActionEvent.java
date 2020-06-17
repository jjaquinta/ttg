/*
 * Created on Apr 1, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.war;

import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WarTTGActionEvent extends TTGActionEvent
{
	public static final int	DROPPED = 4;

	private Object mActor;	
	
	public WarTTGActionEvent(Object source, int id, String uri, Object object, Object actor)
	{
		super(source, id, uri, object);
		mActor = actor;
	}

	/**
	 * @return
	 */
	public Object getActor()
	{
		return mActor;
	}

	/**
	 * @param object
	 */
	public void setActor(Object object)
	{
		mActor = object;
	}

}
