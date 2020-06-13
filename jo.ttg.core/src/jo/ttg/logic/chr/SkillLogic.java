/*
 * Created on Jan 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.chr;

import java.util.Collection;
import java.util.Iterator;

import jo.ttg.beans.chr.CharBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SkillLogic
{
    public static int getBestSkill(String what, Collection<CharBean> chars)
    {
        int best = -1;
        for (Iterator<CharBean> i = chars.iterator(); i.hasNext(); )
        {
            int skill = i.next().getSkill(what);
            if (skill > best)
                best = skill;
        }
        return best;
    }
}
