/*
 * Created on Dec 6, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.logic.chr;

import java.util.Collection;
import java.util.Iterator;

import jo.ttg.beans.RandBean;
import jo.ttg.beans.chr.CharBean;
import jo.ttg.beans.chr.ServiceStatsBean;
import jo.ttg.logic.RandLogic;
import jo.util.utils.obj.IntegerUtils;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CharLogic
{
	private static void incrementSkill(CharBean ch, String what, Integer amnt) 
	{ 
		incrementSkill(ch, what, amnt.intValue()); 
	}

	private static void incrementSkill(CharBean ch, String what, int amnt)
	{
		if (what == null)
			return;
		int current = ch.getBaseSkill(what); 
		if (current >= 0)
			current += amnt;
		else
			current = amnt; 
		ch.setSkill(what, current);
	}

	private static void incrementSkill(CharBean ch, String adj)
	{
		if (adj == null)
			return;
		Object ret[] = splitSkill(adj);
		if (ret == null)
			return;
		incrementSkill(ch, (String)ret[1], (Integer)ret[0]);
	}

	private static boolean checkSkillLimit(CharBean ch, String test)
	{
	    int o = test.indexOf("-");
	    if (o < 0)
	    {
			System.out.println("DON'T UNDERSTAND SKILL LIMIT "+test);
			return true;
	    }
	    String limit = test.substring(o + 1);
	    String skill = test.substring(0, o);
	    return checkSkill(ch, "+"+limit+" "+skill);
	}

	private static boolean checkSkill(CharBean ch, String test)
	{
		Object ret[] = splitSkill(test);
		if (ret == null)
			return true;
		int amnt = ch.getSkill((String)ret[1]);
		int tst = ((Integer)ret[0]).intValue();
		if (tst < 0)
			return amnt <= -tst;
		return amnt >= tst;
	}

	private static Object[] splitSkill(String adj)
	{
		if (adj.length() == 0)
			return null;
		Object ret[] = new Object[2];
		if ((adj.charAt(0) == '+') || (adj.charAt(0) == '-'))
		{
			int o = adj.indexOf(" ");
			if (o < 0)
			{
				System.out.println("DON'T UNDERSTAND SKILL "+adj);
			}
			else
			{
                int v = IntegerUtils.parseInt(adj.substring(1, o).trim());
                if (adj.charAt(0) == '-')
                    v = -v;
				ret[0] = new Integer(v);
				ret[1] = adj.substring(o+1).trim();
			}
		}
		else
		{
			ret[0] = new Integer(1);
			ret[1] = adj;
		}
		return ret;
	}
	
	// generate

	// TERM FUNCTIONS
	public static CharBean generate(String preferredService, String till, RandBean r)
	{
	    return generate(preferredService, till, r, new CharBean());
	}

	// TERM FUNCTIONS
	public static CharBean generate(String preferredService, String till, RandBean r, CharBean ch)
	{
		ch.setOID(r.getSeed());
		boolean madeIt = false; // did we make criteria
		boolean mustMakeIt = (till != null) && (till.startsWith(CharBean.S_TERMS) || till.startsWith(CharBean.S_AGE));
		generateBasics(r, ch);
		ServiceStatsBean serv = findService(ch, preferredService, r);
		ch.setService(serv.getService());
		generateSkill(ch, r, serv); // one for free
		incrementSkill(ch, serv.getRankSkills()[0]);
		// march through terms
		do
		{
			// check survival
			if (!mustMakeIt && !doWe(ch, r, serv.getSurvival(), serv.getSurvivalMod()))
				break; // we died!
			// get one skill immediately
			generateSkill(ch, r, serv);
			if (serv.getCommission() > 0)
			{   // ranked profession
				generateCommission(r, ch, serv);
			}
			else
			{   // unranked profession
				// just get another benefit
				generateSkill(ch, r, serv);
			}
			generateBenefit(ch, r, serv);
			incrementSkill(ch, CharBean.S_AGE, 4);
			incrementSkill(ch, CharBean.S_TERMS);
			if ((till != null) && checkSkillLimit(ch, till))
				madeIt = true;
			if (!mustMakeIt)
			{
				if (!generateAging(ch, r))
					break;
				if (!doWe(ch, r, serv.getReenlist()))
					break;
			}
		} while (!madeIt);
		// set out titles
		generateTitle(ch, serv);
		filter(ch);
		String[] best = getBestSkills(ch, 2);
		ch.setBestSkill(best[0]);
		ch.setSecondBestSkill(best[1]);
		return ch;
	}

	private static void    filter(CharBean ch)
	{   // remove "skills" into variables
		ch.getUpp()[0] = ch.getBaseSkill(CharBean.S_STR);
		ch.getSkills().remove(CharBean.S_STR);
		ch.getUpp()[1] = ch.getBaseSkill(CharBean.S_DEX);
		ch.getSkills().remove(CharBean.S_DEX);
		ch.getUpp()[2] = ch.getBaseSkill(CharBean.S_END);
		ch.getSkills().remove(CharBean.S_END);
		ch.getUpp()[3] = ch.getBaseSkill(CharBean.S_INT);
		ch.getSkills().remove(CharBean.S_INT);
		ch.getUpp()[4] = ch.getBaseSkill(CharBean.S_EDU);
		ch.getSkills().remove(CharBean.S_EDU);
		ch.getUpp()[5] = ch.getBaseSkill(CharBean.S_SOC);
		ch.getSkills().remove(CharBean.S_SOC);
		ch.setMoney(ch.getBaseSkill(CharBean.S_MONEY));
		ch.getSkills().remove(CharBean.S_MONEY);
		ch.setTerms(ch.getBaseSkill(CharBean.S_TERMS));
		ch.getSkills().remove(CharBean.S_TERMS);
		ch.setAge(ch.getBaseSkill(CharBean.S_AGE));
		ch.getSkills().remove(CharBean.S_AGE);
		ch.setRank(ch.getBaseSkill(CharBean.S_RANK));
		ch.getSkills().remove(CharBean.S_RANK);
	}

	private static void generateTitle(CharBean ch, ServiceStatsBean serv)
	{
		int rnk = ch.getBaseSkill(CharBean.S_RANK);
		if (rnk <= 0)
			ch.setTitle("");
		else
		{
			if (rnk <= serv.getTitles().length)
				ch.setTitle(serv.getTitles()[rnk - 1]);
			else
				ch.setTitle(serv.getTitles()[serv.getTitles().length - 1]);
		}
	}

	private static boolean generateAging(CharBean ch, RandBean r)
	{
		int age = ch.getBaseSkill(CharBean.S_AGE);
		if (age < 34)
			return true;
		if (age <= 46)
		{
			if (RandLogic.D(r, 2) < 8)
				incrementSkill(ch, CharBean.S_STR, -1);
			if (RandLogic.D(r, 2) < 7)
				incrementSkill(ch, CharBean.S_DEX, -1);
			if (RandLogic.D(r, 2) < 8)
				incrementSkill(ch, CharBean.S_END, -1);
		}
		else if (age <= 62)
		{
			if (RandLogic.D(r, 2) < 9)
				incrementSkill(ch, CharBean.S_STR, -1);
			if (RandLogic.D(r, 2) < 8)
				incrementSkill(ch, CharBean.S_DEX, -1);
			if (RandLogic.D(r, 2) < 9)
				incrementSkill(ch, CharBean.S_END, -1);
		}
		else
		{
			if (RandLogic.D(r, 2) < 9)
				incrementSkill(ch, CharBean.S_STR, -2);
			if (RandLogic.D(r, 2) < 9)
				incrementSkill(ch, CharBean.S_DEX, -2);
			if (RandLogic.D(r, 2) < 9)
				incrementSkill(ch, CharBean.S_END, -2);
			if (RandLogic.D(r, 2) < 9)
				incrementSkill(ch, CharBean.S_INT, -1);
		}
		if (ch.getBaseSkill(CharBean.S_STR) < 2)
		{
			ch.setSkill(CharBean.S_STR, 2);
			return false;
		}
		if (ch.getBaseSkill(CharBean.S_DEX) < 2)
		{
			ch.setSkill(CharBean.S_DEX, 2);
			return false;
		}
		if (ch.getBaseSkill(CharBean.S_END) < 2)
		{
			ch.setSkill(CharBean.S_END, 2);
			return false;
		}
		if (ch.getBaseSkill(CharBean.S_INT) < 2)
		{
			ch.setSkill(CharBean.S_INT, 2);
			return false;
		}
		return true;
	}

	private static void generateCommission(
		RandBean r,
		CharBean ch,
		ServiceStatsBean serv)
	{
		if (ch.getBaseSkill(CharBean.S_RANK) <= 0)
		{   // try for commission
			if (doWe(ch, r, serv.getCommission(), serv.getCommissionMod()))
			{
				ch.setSkill(CharBean.S_RANK, 1);
				generateSkill(ch, r, serv);
				incrementSkill(ch, serv.getRankSkills()[1]);
			}
		}
		if (ch.getBaseSkill(CharBean.S_RANK) > 0)
		{   // try for promotion
			if (doWe(ch, r, serv.getPromotion(), serv.getPromotionMod()))
			{
				incrementSkill(ch, CharBean.S_RANK);
				generateSkill(ch, r, serv);
				int rnk = ch.getBaseSkill(CharBean.S_RANK);
				if (rnk < serv.getRankSkills().length)
					incrementSkill(ch, serv.getRankSkills()[rnk]);
			}
		}
	}

	private static void generateBenefit(CharBean ch, RandBean r, ServiceStatsBean serv)
	{
		String  table[];
		int roll = RandLogic.D(r, 1);
		if (RandLogic.D(r, 1) <= 2)
		{
			table = serv.getMusterMoney();
			if (checkSkill(ch, "+5 Rank"))
				roll++;
		}
		else
		{
			table = serv.getMusterBenefits();
			if (checkSkill(ch, "+1 Gambling"))
				roll++;
		}
		if (roll <= table.length)
			incrementSkill(ch, table[roll-1]);
	}

	private static ServiceStatsBean findService(CharBean ch, String preferredService, RandBean r)
	{
		ServiceStatsBean serv = null;
		do
		{
			if (preferredService != null)
			    serv = ServiceLogic.getService(preferredService);
			if (serv == null)
				serv = ServiceLogic.getAnyService(r);
		} while (!doWe(ch, r, serv.getEnlist(), serv.getEnlistMod1(), serv.getEnlistMod2()));
		return serv;
	}

	private static boolean doWe(CharBean ch, RandBean r, int target) { return doWe(ch, r, target, null, null); }
	private static boolean doWe(CharBean ch, RandBean r, int target, String mod1) { return doWe(ch, r, target, mod1, null); }
	private static boolean doWe(CharBean ch, RandBean r, int target, String mod1, String mod2)
	{
		int roll = RandLogic.D(r, 2);
		if (mod1 != null)
		{
			Object o[] = splitSkill(mod1);
			if ((o != null) &&  (((Integer)(o[0])).intValue() == 1))
				roll += ch.getSkill((String)(o[1]));
			else if (checkSkill(ch, mod1))
				roll++;
		}
		if ((mod2 != null) && checkSkill(ch, mod2))
			roll += 2;
		return roll >= target;
	}

	private static void generateBasics(RandBean r, CharBean ch)
	{
		// roll stats
		ch.setSkill(CharBean.S_STR, RandLogic.D(r, 2));
		ch.setSkill(CharBean.S_DEX, RandLogic.D(r, 2));
		ch.setSkill(CharBean.S_END, RandLogic.D(r, 2));
		ch.setSkill(CharBean.S_INT, RandLogic.D(r, 2));
		ch.setSkill(CharBean.S_EDU, RandLogic.D(r, 2));
		ch.setSkill(CharBean.S_SOC, RandLogic.D(r, 2));
		// basics
		ch.setSkill(CharBean.S_AGE, 18);
		ch.setMale(RandLogic.D(r, 1) <= 3);
	}	

	private static void generateSkill(CharBean ch, RandBean r, ServiceStatsBean serv)
	{
		String  table[];
		if (ch.getBaseSkill(CharBean.S_EDU) >= 8)
			table = serv.getSkills()[RandLogic.rand(r)%4];
		else
			table = serv.getSkills()[RandLogic.rand(r)%3];
		incrementSkill(ch, table[RandLogic.D(r, 1) - 1]);
	}
	
	private static String[] getBestSkills(CharBean ch, int num)
	{
		String[] ret = new String[num];
		int[] best = new int[ret.length];
		for (Iterator<String> i = ch.getSkills().keySet().iterator(); i.hasNext(); )
		{
			String skill = i.next();
			int value = ch.getSkill(skill);
			for (int j = 0; j < ret.length; j++)
				if (ret[j] == null)
				{
					ret[j] = skill;
					best[j] = value;
					break;
				}
				else if (best[j] < value)
				{
					for (int k = ret.length - 1; k > j; k--)
					{
						ret[k] = ret[k-1];
						best[k] = best[k-1];
					}
					ret[j] = skill;
					best[j] = value;
					break;
				}
		}
		for (int j = 0; j < ret.length; j++)
			if (ret[j] == null)
			    ret[j] = "";
			else
			    ret[j] += "-"+best[j];
		return ret;
	}
	
	public static int findBestSkill(Collection<CharBean> chars, String skill)
	{
	    int best = -1;
	    for (Iterator<CharBean> i = chars.iterator(); i.hasNext(); )
	    {
	        CharBean ch = i.next();
	        int value = ch.getSkill(skill);
	        if (value > best)
	            best = value;
	    }
	    return best;
	}
}
