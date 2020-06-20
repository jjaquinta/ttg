/*
 * Created on Mar 30, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.war.view;

import java.awt.Insets;

import jo.ttg.logic.OrdLogic;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.war.beans.Ship;
import ttg.war.beans.ShipInst;
import ttg.war.beans.SideInst;
import ttg.war.beans.WorldInst;
import ttg.war.logic.IconLogic;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ObjectButton extends WarButton
{
	private WarPanel	mPanel;
	private Object		mObject;
	
	public ObjectButton(WarPanel panel)
	{
		mPanel = panel;
		setMargin(new Insets(1, 1, 1, 1));
		ListenerUtils.listen(this, (ev) -> doIt());
	}

	private void doIt()
	{
		if (mObject != null)
		{
			if (!(mObject instanceof Ship)) 
				mPanel.getInfoPanel().setObject(mObject);
		}
	}
	
	/**
	 * @return
	 */
	public Object getObject()
	{
		return mObject;
	}

	/**
	 * @param inst
	 */
	public void setObject(Object inst)
	{
		mObject = inst;
		if (mObject == null)
		{
			setText(null);
			setIcon(null);
			setVisible(false);
		}
		else
		{
			if (mObject instanceof ShipInst)
			{
				setIcon(IconLogic.getShipIcon((ShipInst)mObject, IconLogic.SEL_DONT));
				setText(((ShipInst)mObject).getShip().getName());
			}
			else if (mObject instanceof Ship)
			{
				setIcon(IconLogic.getShipIcon((Ship)mObject, IconLogic.SEL_DONT));
				setText(((Ship)mObject).getName());
			}
			else if (mObject instanceof WorldInst)
			{
				setIcon(IconLogic.getWorldIcon(mPanel.getGame(), (WorldInst)mObject, mPanel.getSide()));
				String str;
				if (((WorldInst)mObject).getWorld() != null)
					str = ((WorldInst)mObject).getWorld().getName();
				else
					str = OrdLogic.getShortNum(((WorldInst)mObject).getOrds());
				setText(str);
			} 
			else if (mObject instanceof SideInst)
			{
				setIcon(IconLogic.getSideIcon((SideInst)mObject)); 
				setText(((SideInst)mObject).getSide().getName());
			} 
			setVisible(true);
		}
	}

}
