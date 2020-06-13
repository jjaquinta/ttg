/**
 * Created on Sep 20, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package jo.ttg.logic;

import java.util.Iterator;

import jo.ttg.beans.RandBean;

/**
 * @author jgrant
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class RandomIterator implements Iterator<Object>
{
    Object[] mObjs;
    int      mIdx;

    public RandomIterator(Object[] objs, RandBean r)
    {
        mObjs = new Object[objs.length];
        System.arraycopy(mObjs, 0, objs, 0, objs.length);
        // shuffle
        for (int i = 0; i < mObjs.length; i++)
        {
            int j = RandLogic.rand(r)%mObjs.length;
            if (j != i)
            {
                Object tmp = mObjs[i];
                mObjs[i] = mObjs[j];
                mObjs[j] = tmp;
            }
        }
        mIdx = 0;
    }

    public boolean hasNext()
    {
        return mIdx < mObjs.length;
    }

    public Object next()
    {
        return mObjs[mIdx++];
    }

    public void remove()
    {
    }
}
