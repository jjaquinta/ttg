/*
 * Created on Feb 5, 2005
 *
 */
package jo.ttg.ship.beans.cbt;

import java.util.EventObject;

/**
 * @author Jo
 *
 */
public class CombatNotifyEvent extends EventObject
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -8171901302061627967L;
    public static final int NOSCAN = 1;
    public static final int LOCATED = 2;
    public static final int LOCKED = 3;
    public static final int DONE = 4;
    public static final int DAMAGE = 10;
    public static final int HIT = 11;
    public static final int MISS = 12;
    public static final int PENETRATE = 13;
    public static final int DEFLECT = 14;
    public static final int JOIN = 15;
    public static final int MOVE = 16;
    
    private int		mId;
    private Object	mSubject;
    private Object	mAdjective;
    
    public CombatNotifyEvent(Object src)
    {
        super(src);
    }
    
    public CombatNotifyEvent(Object src, int id)
    {
        super(src);
        mId = id;
    }
    
    public CombatNotifyEvent(Object src, int id, Object subject)
    {
        super(src);
        mId = id;
        mSubject = subject;
    }
    
    public CombatNotifyEvent(Object src, int id, Object subject, Object adjective)
    {
        super(src);
        mId = id;
        mSubject = subject;
        mAdjective = adjective;
    }
    /**
     * @return Returns the id.
     */
    public int getId()
    {
        return mId;
    }
    /**
     * @param id The id to set.
     */
    public void setId(int id)
    {
        mId = id;
    }
    /**
     * @return Returns the adjective.
     */
    public Object getAdjective()
    {
        return mAdjective;
    }
    /**
     * @param adjective The adjective to set.
     */
    public void setAdjective(Object adjective)
    {
        mAdjective = adjective;
    }
    /**
     * @return Returns the subject.
     */
    public Object getSubject()
    {
        return mSubject;
    }
    /**
     * @param subject The subject to set.
     */
    public void setSubject(Object subject)
    {
        mSubject = subject;
    }
}
