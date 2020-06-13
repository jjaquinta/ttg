/*
 * Created on Jan 10, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.chr;

import jo.ttg.beans.RandBean;
import jo.ttg.beans.chr.CharBean;
import jo.ttg.logic.RandLogic;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TaskLogic
{
    public static final int DIFF_SIMPLE = 3;
    public static final int DIFF_ROUTINE = 7;
    public static final int DIFF_DIFFICULT = 11;
    public static final int DIFF_FORMIDIBLE = 15;
    public static final int DIFF_IMPOSSIBLE = 19;
    
    public static final int RES_SUCCESS = 2;
    public static final int RES_MARGINAL_SUCCESS = 1;
    public static final int RES_MARGINAL_FAILURE = -1;
    public static final int RES_FAILURE = -2;
    
    public static final int RES_TOTAL_TRUTH = 2;
    public static final int RES_SOME_TRUTH = 1;
    public static final int RES_NO_TRUTH = 0;
    public static final int RES_TRUTH_MASK = 3;
    public static final int RES_KNOW_SUCCESS = 4;
    public static final int RES_KNOW_FAILURE = 0;
    public static final int RES_KNOW_MASK = 4;
    
    public static int attemptUncertainTask(int diff, RandBean rnd, CharBean ch, String skill1, String skill2)
    {
        return attemptUncertainTask(diff, rnd, ch.getSkill(skill1), ch.getSkill(skill2));
    }
    
    public static boolean attemptFatefulTask(int diff, RandBean rnd, CharBean ch, String skill1, String skill2)
    {
        return attemptFatefulTask(diff, rnd, ch.getSkill(skill1), ch.getSkill(skill2)); 
    }
    
    public static int attemptTask(int diff, RandBean rnd, CharBean ch, String skill1, String skill2)
    {
        return attemptTask(diff, rnd, ch.getSkill(skill1), ch.getSkill(skill2));
    }
    
    public static int attemptUncertainTask(int diff, RandBean rnd, int skill1, int skill2)
    {
        int d1 = rollTask(diff, rnd, skill1, skill2);
        int d2 = rollTask(diff, rnd, skill1, skill2);
        int ret = 0;
        if (d1 >= 0)
        {
            ret += RES_KNOW_SUCCESS;
            if (d2 >= 0)
                ret += RES_TOTAL_TRUTH;
            else
                ret += RES_SOME_TRUTH;
        }
        else
        {
            ret += RES_KNOW_FAILURE;
            if (d2 >= 0)
                ret += RES_SOME_TRUTH;
            else
                ret += RES_NO_TRUTH;
        }
        return ret;
    }
    
    public static boolean attemptFatefulTask(int diff, RandBean rnd, int skill1, int skill2)
    {
        return rollTask(diff, rnd, skill1, skill2) >= 0; 
    }
    
    public static int attemptTask(int diff, RandBean rnd, int skill1, int skill2)
    {
        int res = rollTask(diff, rnd, skill1, skill2);
        if (res >= 4)
            return RES_SUCCESS;
        if (res <= -4)
            return RES_FAILURE;
        if (res < 0)
            return RES_MARGINAL_FAILURE;
        return RES_MARGINAL_SUCCESS;
    }
    
    public static int rollTask(int diff, RandBean rnd, int skill1, int skill2)
    {
        int d = RandLogic.D(rnd, 2);
        d += skill1;
        d += skill2;
        d -= diff;
        return d;
    }
    
    public static int convStringToDifficulty(String diff)
    {
        if (diff.equalsIgnoreCase("Simple"))
            return DIFF_SIMPLE;
        if (diff.equalsIgnoreCase("Routine"))
            return DIFF_ROUTINE;
        if (diff.equalsIgnoreCase("Difficult"))
            return DIFF_DIFFICULT;
        if (diff.equalsIgnoreCase("Formidible"))
            return DIFF_FORMIDIBLE;
        if (diff.equalsIgnoreCase("Impossible"))
            return DIFF_IMPOSSIBLE;
        return DIFF_IMPOSSIBLE;
    }
    
    public static String convDifficultyToString(int diff)
    {
        switch (diff)
        {
            case DIFF_SIMPLE:
                return "Simple";
            case DIFF_ROUTINE:
                return "Routine";
            case DIFF_DIFFICULT:
                return "Difficult";
            case DIFF_FORMIDIBLE:
                return "Formidible";
            case DIFF_IMPOSSIBLE:
                return "Impossible";
        }
        return "-";
    }
}
