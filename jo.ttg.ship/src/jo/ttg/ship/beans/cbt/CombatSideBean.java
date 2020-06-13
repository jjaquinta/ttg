/*
 * Created on Jan 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.beans.cbt;

import java.util.ArrayList;
import java.util.List;

import jo.util.beans.PCSBean;

/**
 * @author jgrant
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CombatSideBean extends PCSBean
{
    private CombatBean           mCombat;
    private String               mName;
    private List<CombatShipBean> mShips;
    private List<CombatShipBean> mShipsLeftToMove;
    private List<CombatShipBean> mShipsDestroyed;
    private int                  mFleetTactics;
    private int                  mFleetTacticsUsed;

    public CombatSideBean()
    {
        mShips = new ArrayList<CombatShipBean>();
        mShipsLeftToMove = new ArrayList<CombatShipBean>();
        mShipsDestroyed = new ArrayList<CombatShipBean>();
    }

    public String toString()
    {
        return getName();
    }

    public List<CombatShipBean> getShips()
    {
        return mShips;
    }

    public void setShips(List<CombatShipBean> ships)
    {
        queuePropertyChange("ships", mShips, ships);
        mShips = ships;
        firePropertyChange();
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        queuePropertyChange("name", mName, name);
        mName = name;
        firePropertyChange();
    }

    public int getFleetTactics()
    {
        return mFleetTactics;
    }

    public void setFleetTactics(int tacticalPoints)
    {
        mFleetTactics = tacticalPoints;
    }

    public int getFleetTacticsUsed()
    {
        return mFleetTacticsUsed;
    }

    public void setFleetTacticsUsed(int tacticalPointsUsed)
    {
        mFleetTacticsUsed = tacticalPointsUsed;
    }

    public List<CombatShipBean> getShipsLeftToMove()
    {
        return mShipsLeftToMove;
    }

    public void setShipsLeftToMove(List<CombatShipBean> shipsLeftToMove)
    {
        queuePropertyChange("shipsLeftToMove", mShipsLeftToMove,
                shipsLeftToMove);
        mShipsLeftToMove = shipsLeftToMove;
        firePropertyChange();
    }

    /**
     * @return Returns the combat.
     */
    public CombatBean getCombat()
    {
        return mCombat;
    }

    /**
     * @param combat
     *            The combat to set.
     */
    public void setCombat(CombatBean combat)
    {
        mCombat = combat;
    }

    /**
     * @return Returns the shipsDestroyed.
     */
    public List<CombatShipBean> getShipsDestroyed()
    {
        return mShipsDestroyed;
    }

    /**
     * @param shipsDestroyed
     *            The shipsDestroyed to set.
     */
    public void setShipsDestroyed(List<CombatShipBean> shipsDestroyed)
    {
        queuePropertyChange("shipsDestroyed", mShipsDestroyed, shipsDestroyed);
        mShipsDestroyed = shipsDestroyed;
        firePropertyChange();
    }
}
