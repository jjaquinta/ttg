/*
 * Created on Jan 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.beans.cbt;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.RandBean;
import jo.ttg.ship.logic.cbt.CombatNotifyListener;
import jo.util.beans.PCSBean;

/**
 * @author jgrant
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CombatBean extends PCSBean
{
    private int                        mRound;
    private RandBean                   mRnd;
    private List<CombatSideBean>       mSides;
    private List<CombatSideBean>       mSidesLeftToMove;
    private List<CombatNotifyListener> mCombatNotifyListeners;

    public CombatBean()
    {
        mSides = new ArrayList<CombatSideBean>();
        mSidesLeftToMove = new ArrayList<CombatSideBean>();
        mCombatNotifyListeners = new ArrayList<CombatNotifyListener>();
    }

    public RandBean getRnd()
    {
        return mRnd;
    }

    public void setRnd(RandBean rnd)
    {
        queuePropertyChange("rnd", mRnd, rnd);
        mRnd = rnd;
        firePropertyChange();
    }

    public List<CombatSideBean> getSides()
    {
        return mSides;
    }

    public void setSides(List<CombatSideBean> sides)
    {
        queuePropertyChange("sides", mSides, sides);
        mSides = sides;
        firePropertyChange();
    }

    public List<CombatSideBean> getSidesLeftToMove()
    {
        return mSidesLeftToMove;
    }

    public void setSidesLeftToMove(List<CombatSideBean> sidesLeftToMove)
    {
        queuePropertyChange("sidesLeftToMove", mSidesLeftToMove,
                sidesLeftToMove);
        mSidesLeftToMove = sidesLeftToMove;
        firePropertyChange();
    }

    /**
     * @return Returns the combatNotifyListeners.
     */
    public List<CombatNotifyListener> getCombatNotifyListeners()
    {
        return mCombatNotifyListeners;
    }

    /**
     * @param combatNotifyListeners
     *            The combatNotifyListeners to set.
     */
    public void setCombatNotifyListeners(
            List<CombatNotifyListener> combatNotifyListeners)
    {
        mCombatNotifyListeners = combatNotifyListeners;
    }

    public void addCombatNotifyListener(CombatNotifyListener l)
    {
        mCombatNotifyListeners.add(l);
    }

    public void removeCombatNotifyListener(CombatNotifyListener l)
    {
        mCombatNotifyListeners.remove(l);
    }

    public int getRound()
    {
        return mRound;
    }

    public void setRound(int round)
    {
        queuePropertyChange("round", mRound, round);
        mRound = round;
        firePropertyChange();
    }
}
