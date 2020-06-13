/*
 * Created on Feb 5, 2005
 *
 */
package jo.ttg.ship.logic.cbt;

import jo.ttg.ship.beans.cbt.CombatNotifyEvent;

/**
 * @author Jo
 *
 */
public interface CombatNotifyListener
{
    public void combatNotification(CombatNotifyEvent ev);
}
