/*
 * Created on Jan 6, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.logic.adv;

import java.util.ArrayList;
import java.util.List;

import ttg.beans.adv.CrewBean;
import ttg.beans.adv.ShipInst;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CrewLogic
{
    private static final String[] mJobNames = new String[16];
    static
    {
            mJobNames[CrewBean.JOB_BRIDGE] = "Bridge";
            mJobNames[CrewBean.JOB_ENGINEERING] = "Engineering";
            mJobNames[CrewBean.JOB_MAINTENENCE] = "Maintenence";
            mJobNames[CrewBean.JOB_GUNNER] = "Gunner";
            mJobNames[CrewBean.JOB_COMMAND] = "Command";
            mJobNames[CrewBean.JOB_STEWARD] = "Steward";
            mJobNames[CrewBean.JOB_MEDICAL] = "Medical";
    }
    private static final String[][] mJobSkills = 
    {
            null,
            { "Pilot", "Navigation", "Ship's Boat"},
            { "Engineering" },
            { "Mechanical", "Electronic" },
            { "Gunnery" },
            { "Leader", "Admin", "Tactics", "Liason" },
            { "Steward" },
            { "Medical" },
    };
    private static final double[] mJobPay = 
    {
            0,
            500,
            500,
            500,
            500,
            1000,
            500,
            500,
    };

    public static int[] totalCrew(ShipInst ship)
    {
        int[] crewHave = new int[16];
        for (CrewBean crew : ship.getCrew())
            try
            {
                crewHave[crew.getJob()]++;
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
            }
        return crewHave;
    }
    
    public static int[] neededCrew(ShipInst ship)
    {
        int[] crewNeed = new int[16];
        crewNeed[CrewBean.JOB_BRIDGE] = ship.getStats().getCrewBridge();
        crewNeed[CrewBean.JOB_COMMAND] = ship.getStats().getCrewCommand();
        crewNeed[CrewBean.JOB_ENGINEERING] = ship.getStats().getCrewEngineering();
        crewNeed[CrewBean.JOB_GUNNER] = ship.getStats().getCrewGunners();
        crewNeed[CrewBean.JOB_MAINTENENCE] = ship.getStats().getCrewMaintenence();
        crewNeed[CrewBean.JOB_MEDICAL] = ship.getStats().getCrewMedical();
        crewNeed[CrewBean.JOB_STEWARD] = ship.getStats().getCrewSteward();
        return crewNeed;
    }
    
    public static String[] jobNames()
    {
        return mJobNames;
    }
    
    public static String[] jobSkills(int job)
    {
        return mJobSkills[job];
    }
    
    public static boolean qualifiedFor(CrewBean crew, int job)
    {
        for (int j = 0; j < mJobSkills[job].length; j++)
            if (crew.getSkill(mJobSkills[job][j]) > 0)
                return true;
        return false;
    }
    
    public static List<String> qualifiedJobs(CrewBean crew)
    {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < mJobNames.length; i++)
        {
            if (mJobNames[i] == null)
                continue;
            for (int j = 0; j < mJobSkills[i].length; j++)
                if (crew.getSkill(mJobSkills[i][j]) > 0)
                {
                    ret.add(mJobNames[i]);
                    break;
                }
        }
        return ret;
    }
    
    public static void assignJob(ShipInst ship, CrewBean crew, int job)
    {
        for (int j = 0; j < mJobSkills[job].length; j++)
            if (crew.getSkill(mJobSkills[job][j]) > 0)
            {
                crew.setJob(job);
                crew.setSalary(calcSalary(crew));
                ship.fireCrewChange();
                break;
            }
    }
    
    public static int getBestSkill(CrewBean crew, String[] skills)
    {
        int best = 0;
        for (int i = 0; i < skills.length; i++)
        {
            int skill = crew.getSkill(skills[i]);
            if (skill > best)
                best = skill;
        }
        return best;
    }
    
    public static double calcSalary(CrewBean crew)
    {
        int brn = Math.max(crew.getRank(), 1);
        int bonus = getBestSkill(crew, jobSkills(crew.getJob()));
        double salary = mJobPay[crew.getJob()]*brn*(1.0 + .1*bonus);
        return salary;
    }
}
