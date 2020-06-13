/*
 * Created on Feb 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.chr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import jo.ttg.beans.RandBean;
import jo.ttg.beans.chr.ServiceStatsBean;
import jo.ttg.logic.RandLogic;
import jo.util.utils.obj.IntegerUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ServiceLogic
{
    public static final String ADMIN = "Admin";
    public static final String AIR_CRAFT = "Air Craft";
    public static final String AIR_RAFT = "Air/Raft";
    public static final String BATTLE_DRESS = "Battle Dress";
    public static final String BLADE_CBT = "Blade Cbt";
    public static final String BOW_CBT = "Bow Cbt";
    public static final String BRAWLING = "Brawling";
    public static final String BRIBERY = "Bribery";
    public static final String CAROUSING = "Carousing";
    public static final String COMMO = "Commo";
    public static final String COMPUTER = "Computer";
    public static final String DEMOLITION = "Demolition";
    public static final String ELECTRONIC = "Electronic";
    public static final String ENGINEERING = "Engineering";
    public static final String FORGERY = "Forgery";
    public static final String FWD_OBSVR = "Fwd Obsvr";
    public static final String GAMBLING = "Gambling";
    public static final String GRAVITICS = "Gravitics";
    public static final String GUN_CBT = "Gun Cbt";
    public static final String GUNNERY = "Gunnery";
    public static final String INSTRUCTION = "Instruction";
    public static final String INTERROGATION = "Interrogation";
    public static final String JACK_O_T = "Jack-o-T";
    public static final String LEADER = "Leader";
    public static final String LIASON = "Liason";
    public static final String MECHANICAL = "Mechanical";
    public static final String MEDICAL = "Medical";
    public static final String NAVIGATION = "Navigation";
    public static final String PILOT = "Pilot";
    public static final String PROSPECTING = "Prospecting";
    public static final String RECON = "Recon";
    public static final String RECRUITING = "Recruiting";
    public static final String SHIP_TACTICS = "Ship Tactics";
    public static final String SHIP_S_BOAT = "Ship's Boat";
    public static final String STEWARD = "Steward";
    public static final String STREETWISE = "Streetwise";
    public static final String SURVIVAL = "Survival";
    public static final String TACTICS = "Tactics";
    public static final String VACC_SUIT = "Vacc Suit";
    public static final String VEHICLE = "Vehicle";
    public static final String WATER_CRAFT = "Water Craft";
    public static final String ZERO_G_CBT = "Zero-G Cbt";

    public static final String SERVICE_NAVY = "Navy";
    public static final String SERVICE_MARINES = "Marines";
    public static final String SERVICE_ARMY = "Army";
    public static final String SERVICE_SCOUTS = "Scouts";
    public static final String SERVICE_MERCHANTS = "Merchants";
    public static final String SERVICE_OTHER = "Other";
    public static final String SERVICE_PIRATES = "Pirates";
    public static final String SERVICE_BELTERS = "Belters";
    public static final String SERVICE_SAILORS = "Sailors";
    public static final String SERVICE_DIPLOMATS = "Diplomats";
    public static final String SERVICE_DOCTORS = "Doctors";
    public static final String SERVICE_FLYERS = "Flyers";
    public static final String SERVICE_BARBARIAN = "Barbarian";
    public static final String SERVICE_BUREAUCRAT = "Bureaucrat";
    
    private static List<ServiceStatsBean>	mServices = new ArrayList<ServiceStatsBean>();
	
	static final String mCharClasses[][][] =
	{
		{
			{ "Navy", "8", "+8 Int", "+9 Edu", "5", "+7 Int", "10", "+9 Soc", "8", "+8 Edu", "6" },
			{ "Ensign", "Lieutenant", "Lt Cmdr", "Commander", "Captain", "Admiral" },
			{ "+1000 Cr", "+1 Int", "+2 Edu", BLADE_CBT, "TAS", "+10000 Cr", "+2 Soc" },
			{ "+1000 Cr", "+5000 Cr", "+5000 Cr", "+10000 Cr", "+20000 Cr", "+50000 Cr", "+50000 Cr" },
			{ "+1 Str", "+1 Dex", "+1 End", "+1 Int", "+1 Edu", "+1 Soc" },
			{ SHIP_S_BOAT, VACC_SUIT, FWD_OBSVR, GUNNERY, BLADE_CBT, GUN_CBT },
			{ VACC_SUIT, MECHANICAL, ELECTRONIC, ENGINEERING, GUNNERY, JACK_O_T },
			{ MEDICAL, NAVIGATION, ENGINEERING, COMPUTER, PILOT, ADMIN },
			{ null, null, null, null, null, "+1 Soc", "+1 Soc" }
		},
		{
			{ "Marines", "9", "+8 Int", "+8 Str", "6", "+8 End", "9", "+7 Edu", "9", "+8 Soc", "6" },
			{ "Lieutenant", "Captain", "Force Cmdr", "Lt Colonel", "Colonel", "Brigadier" },
			{ "+1000 Cr", "+2 Int", "+1 Edu", BLADE_CBT, "TAS", "+10000 Cr", "+2 Soc" },
			{ "+2000 Cr", "+5000 Cr", "+5000 Cr", "+10000 Cr", "+20000 Cr", "+30000 Cr", "+40000 Cr" },
			{ "+1 Str", "+1 Dex", "+1 End", GAMBLING, BRAWLING, BLADE_CBT },
			{ VEHICLE, VACC_SUIT, BLADE_CBT, GUN_CBT, BLADE_CBT, GUN_CBT },
			{ VEHICLE, MECHANICAL, ELECTRONIC, TACTICS, BLADE_CBT, GUN_CBT },
			{ MEDICAL, TACTICS, TACTICS, COMPUTER, LEADER, ADMIN },
			{ BLADE_CBT, GUN_CBT, null, null, null, null, null }
		},
		{
			{ "Army", "5", "+6 Dex", "+5 End", "5", "+6 End", "5", "+7 End", "6", "+7 Edu", "7" },
			{ "Lieutenant", "Captain", "Major", "Lt Colonel", "Colonel", "General" },
			{ "+1000 Cr", "+1 Int", "+2 Edu", GUN_CBT, "+10000 Cr", "+10000 Cr", "+1 Soc" },
			{ "+2000 Cr", "+5000 Cr", "+10000 Cr", "+10000 Cr", "+10000 Cr", "+20000 Cr", "+30000 Cr" },
			{ "+1 Str", "+1 Dex", "+1 End", GAMBLING, "+1 Edu", BRAWLING },
			{ VEHICLE, AIR_RAFT, GUN_CBT, FWD_OBSVR, BLADE_CBT, GUN_CBT },
			{ VEHICLE, MECHANICAL, ELECTRONIC, TACTICS, BLADE_CBT, GUN_CBT },
			{ MEDICAL, TACTICS, TACTICS, COMPUTER, LEADER, ADMIN },
			{ GUN_CBT, GUN_CBT, null, null, null, null, null }
		},
		{
			{ "Scouts", "7", "+6 Int", "+8 Str", "7", "+9 End", "", "", "", "", "3" },
			{ },
			{ "+1000 Cr", "+2 Int", "+2 Edu", BLADE_CBT, GUN_CBT, "Ship", "" },
			{ "+20000 Cr", "+20000 Cr", "+30000 Cr", "+30000 Cr", "+50000 Cr", "+50000 Cr", "+50000 Cr" },
			{ "+1 Str", "+1 Dex", "+1 End", "+1 Int", "+1 Edu", GUN_CBT },
			{ VEHICLE, VACC_SUIT, MECHANICAL, NAVIGATION, ELECTRONIC, JACK_O_T },
			{ VEHICLE, MECHANICAL, ELECTRONIC, JACK_O_T, GUNNERY, MEDICAL },
			{ MEDICAL, NAVIGATION, ENGINEERING, COMPUTER, PILOT, JACK_O_T },
			{ PILOT, null, null, null, null, null, null }
		},
		{
			{ "Merchants", "7", "+7 Str", "+6 Int", "5", "+7 Int", "4", "+6 Int", "10", "+9 Int", "4" },
			{ "4th Officer", "3rd Officer", "2nd Officer", "1st Officer", "Captain" },
			{ "+1000 Cr", "+1 Int", "+1 Edu", BLADE_CBT, GUN_CBT, "+1000 Cr", "Ship" },
			{ "+1000 Cr", "+5000 Cr", "+10000 Cr", "+20000 Cr", "+20000 Cr", "+40000 Cr", "+40000 Cr" },
			{ "+1 Str", "+1 Dex", "+1 End", "+1 Str", BLADE_CBT, BRIBERY },
			{ VEHICLE, VACC_SUIT, JACK_O_T, STEWARD, ELECTRONIC, GUN_CBT },
			{ STREETWISE, MECHANICAL, ELECTRONIC, NAVIGATION, GUNNERY, MEDICAL },
			{ MEDICAL, NAVIGATION, ENGINEERING, COMPUTER, PILOT, ADMIN },
			{ null, null, null, null, PILOT, null, null }
		},
		{
			{ "Other", "3", "", "", "5", "+9 Int", "", "", "", "", "5" },
			{ },
			{ "+1000 Cr", "+1 Int", "+1 Edu", GUN_CBT, "+10000 Cr", "", "" },
			{ "+1000 Cr", "+5000 Cr", "+10000 Cr", "+10000 Cr", "+10000 Cr", "+50000 Cr", "+100000 Cr" },
			{ "+1 Str", "+1 Dex", "+1 End", BLADE_CBT, BRAWLING, "-1 Soc" },
			{ VEHICLE, GAMBLING, BRAWLING, BRIBERY, BLADE_CBT, GUN_CBT },
			{ STREETWISE, MECHANICAL, ELECTRONIC, GAMBLING, BRAWLING, FORGERY },
			{ MEDICAL, FORGERY, ELECTRONIC, COMPUTER, STREETWISE, JACK_O_T },
			{ null, null, null, null, null, null, null }
		},
		{
			{ "Pirates", "7", "-7 Soc", "+9 End", "6", "+8 Int", "9", "+10 Str", "8", "+9 Int", "7" },
			{ "Henchman", "Corporal", "Sergeant", "Lieutenant", LEADER },
			{ "+1000 Cr", "+1 Int", GUN_CBT, "", "-1 Soc", "+8000 Cr", "Ship" },
			{ "+0 Cr", "+0 Cr", "+1000 Cr", "+10000 Cr", "+50000 Cr", "+50000 Cr", "+50000 Cr" },
			{ "+1 Str", "+1 Dex", "+1 End", GAMBLING, BRAWLING, BLADE_CBT },
			{ BLADE_CBT, VACC_SUIT, GUN_CBT, GUNNERY, ZERO_G_CBT, GUN_CBT },
			{ STREETWISE, GUNNERY, ENGINEERING, SHIP_TACTICS, TACTICS, MECHANICAL },
			{ NAVIGATION, PILOT, FORGERY, COMPUTER, LEADER, ELECTRONIC },
			{ BRAWLING, null, null, null, PILOT, null, null }
		},
		{
			{ "Belters", "8", "+9 Dex", "+6 Int", "9", "Terms", "", "", "", "", "7" },
			{ },
			{ "+1000 Cr", "+1 Int", GUN_CBT, "+10000 Cr", "TAS", "Ship", "" },
			{ "+0 Cr", "+0 Cr", "+1000 Cr", "+10000 Cr", "+100000 Cr", "+100000 Cr", "+100000 Cr" },
			{ "+1 Str", "+1 Dex", "+1 End", GAMBLING, BRAWLING, VACC_SUIT },
			{ VACC_SUIT, VACC_SUIT, PROSPECTING, FWD_OBSVR, PROSPECTING, SHIP_S_BOAT },
			{ SHIP_S_BOAT, ELECTRONIC, PROSPECTING, MECHANICAL, PROSPECTING, INSTRUCTION },
			{ NAVIGATION, MEDICAL, PILOT, COMPUTER, ENGINEERING, JACK_O_T },
			{ VACC_SUIT, null, null, null, null, null, null }
		},
		{
			{ "Sailors", "6", "+10 End", "+8 Str", "5", "+8 End", "5", "+9 Int", "6", "+8 Edu", "6" },
			{ "Ensign", "Lieutenant", "Lt Cmdr", "Commander", "Captain", "Admiral" },
			{ "+1000 Cr", "+1 Edu", GUN_CBT, GUN_CBT, "+10000 Cr", "+10000 Cr", "+1 Soc" },
			{ "+2000 Cr", "+5000 Cr", "+10000 Cr", "+10000 Cr", "+10000 Cr", "+20000 Cr", "+30000 Cr" },
			{ "+1 Str", "+1 Dex", "+1 End", GAMBLING, BRAWLING, CAROUSING },
			{ GUN_CBT, COMMO, FWD_OBSVR, VEHICLE, VEHICLE, BATTLE_DRESS },
			{ WATER_CRAFT, ELECTRONIC, MECHANICAL, GRAVITICS, NAVIGATION, DEMOLITION },
			{ MEDICAL, VEHICLE, STREETWISE, COMPUTER, ADMIN, JACK_O_T },
			{ null, null, null, null, null, null, null }
		},
		{
			{ "Diplomats", "8", "+8 Edu", "+9 Soc", "3", "+8 Edu", "5", "+8 Int", "10", "+10 Edu", "5" },
			{ "3rd Secretary", "2nd Secretary", "1st Secretary", "Counselor", "Minister", "Ambassador" },
			{ "+1000 Cr", "+1 Int", "+2 Edu", GUN_CBT, "+1 Soc", "+10000 Cr", "TAS" },
			{ "+10000 Cr", "+10000 Cr", "+10000 Cr", "+20000 Cr", "+50000 Cr", "+60000 Cr", "+70000 Cr" },
			{ "+1 Str", "+1 Edu", "+1 Int", BLADE_CBT, GUN_CBT, CAROUSING },
			{ "+1 Int", VACC_SUIT, VEHICLE, VEHICLE, GAMBLING, COMPUTER },
			{ FORGERY, STREETWISE, INTERROGATION, RECRUITING, INSTRUCTION, ADMIN },
			{ LIASON, LIASON, ADMIN, COMPUTER, "+1 Soc", JACK_O_T },
			{ LIASON, null, null, null, null, null, null }
		},
		{
			{ "Doctors", "9", "+8 Int", "+9 Dex", "3", "+8 Int", "", "", "", "", "4" },
			{ },
			{ "+1000 Cr", "+1 Edu", "+1 Edu", GUN_CBT, "Instruments", "+8000 Cr", "" },
			{ "+20000 Cr", "+20000 Cr", "+20000 Cr", "+30000 Cr", "+40000 Cr", "+60000 Cr", "+100000 Cr" },
			{ "+1 Str", "+1 Dex", "+1 End", "+1 Int", "+1 Edu", "+1 Soc" },
			{ "+1 Dex", ELECTRONIC, MEDICAL, STREETWISE, MEDICAL, BLADE_CBT },
			{ MEDICAL, MEDICAL, MECHANICAL, ELECTRONIC, COMPUTER, ADMIN },
			{ MEDICAL, MEDICAL, ADMIN, COMPUTER, "+1 Int", "+1 Edu" },
			{ MEDICAL, null, null, null, null, null, null }
		},
		{
			{ "Flyers", "6", "+7 Str", "+9 Dex", "5", "+8 Dex", "5", "+6 Edu", "8", "+8 Edu", "6" },
			{ PILOT, "Flight Leader", "Squadron Leader", "Staff Major", "Group Leader", "Air Marshal" },
			{ "+1000 Cr", "+1 Edu", GUN_CBT, GUN_CBT, "+10000 Cr", "+8000 Cr", "+1 Soc" },
			{ "+2000 Cr", "+5000 Cr", "+10000 Cr", "+10000 Cr", "+10000 Cr", "+20000 Cr", "+30000 Cr" },
			{ "+1 Str", "+1 Dex", "+1 End", GAMBLING, BRAWLING, CAROUSING },
			{ BRAWLING, VACC_SUIT, GUN_CBT, VEHICLE, VEHICLE, VEHICLE },
			{ AIR_CRAFT, MECHANICAL, ELECTRONIC, GRAVITICS, GUN_CBT, SURVIVAL },
			{ MEDICAL, LEADER, PILOT, COMPUTER, ADMIN, JACK_O_T },
			{ AIR_CRAFT, null, null, null, null, null, null }
		},
		{
			{ "Barbarian", "5", "+9 End", "+10 Str", "6", "+8 Str", "6", "+10 Str", "9", "+6 Int", "6" },
			{ "", "Warrior", "", "", "Chief" },
			{ "+1000 Cr", BLADE_CBT, BLADE_CBT, BLADE_CBT, "", "+10000 Cr", "+10000 Cr" },
			{ "+0 Cr", "+0 Cr", "+1000 Cr", "+2000 Cr", "+3000 Cr", "+4000 Cr", "+5000 Cr" },
			{ "+1 Str", "+2 Str", "+1 Str", CAROUSING, "+1 Dex", "+1 End" },
			{ BRAWLING, BLADE_CBT, BLADE_CBT, BOW_CBT, BOW_CBT, GUN_CBT },
			{ BLADE_CBT, MECHANICAL, SURVIVAL, RECON, STREETWISE, BOW_CBT },
			{ MEDICAL, INTERROGATION, TACTICS, LEADER, INSTRUCTION, JACK_O_T },
			{ BLADE_CBT, null, BLADE_CBT, null, null, LEADER, null }
		},
		{
			{ "Bureaucrat", "5", "+8 Edu", "-8 Str", "4", "+10 Edu", "6", "+9 Soc", "7", "+9 Int", "3" },
			{ "Clerk", "Supervisor", "Asst Manager", "Manager", "Executive", "Director" },
			{ "+1000 Cr", "+8000 Cr", "", "Watch", "", "+10000 Cr", "+1 Soc" },
			{ "+0 Cr", "+0 Cr", "+10000 Cr", "+10000 Cr", "+40000 Cr", "+40000 Cr", "+80000 Cr" },
			{ "+1 End", "+1 Edu", "+1 Int", BRAWLING, CAROUSING, "+1 Dex" },
			{ GUN_CBT, VEHICLE, BLADE_CBT, INSTRUCTION, VEHICLE, "+1 Edu" },
			{ RECRUITING, VEHICLE, LIASON, INTERROGATION, ADMIN, ADMIN },
			{ ADMIN, ADMIN, COMPUTER, ADMIN, JACK_O_T, LEADER },
			{ BLADE_CBT, null, BLADE_CBT, null, null, LEADER, null }
		}
	};
	static
	{
	    for (int i = 0; i < mCharClasses.length; i++)
	        addService(mCharClasses[i]);
	}
	
	public static void addService(String[][] serv)
	{
		ServiceStatsBean service = new ServiceStatsBean();
		service.setEnlist(IntegerUtils.parseInt(serv[0][1]));
		service.setEnlistMod1(serv[0][2]);
		service.setEnlistMod2(serv[0][3]);
		service.setService(serv[0][0]);
		service.setSurvival(IntegerUtils.parseInt(serv[0][4]));
		service.setSurvivalMod(serv[0][5]);
		service.setCommission(IntegerUtils.parseInt(serv[0][6]));
		service.setCommissionMod(serv[0][7]);
		service.setPromotion(IntegerUtils.parseInt(serv[0][8]));
		service.setPromotionMod(serv[0][9]);
		service.setReenlist(IntegerUtils.parseInt(serv[0][10]));
		service.setTitles(serv[1]);
		service.setMusterBenefits(serv[2]);
		service.setMusterMoney(serv[3]);
		service.setSkills(new String[4][0]);
		service.getSkills()[0] = serv[4];
		service.getSkills()[1] = serv[5];
		service.getSkills()[2] = serv[6];
		service.getSkills()[3] = serv[7];
		service.setRankSkills(serv[8]);
	    addService(service);
	}
	
	public static void addService(ServiceStatsBean service)
	{
	    mServices.add(service);
	}

	public static String[] getServiceNames()
	{
	    String[] ret = new String[mServices.size()];
	    for (int i = 0; i < mServices.size(); i++)
	        ret[i] = (mServices.get(i)).getService();
	    return ret;
	}

	public static ServiceStatsBean getService(String serviceName)
	{
	    for (Iterator<ServiceStatsBean> i = mServices.iterator(); i.hasNext(); )
	    {
	        ServiceStatsBean service = i.next();
	        if (service.getService().equalsIgnoreCase(serviceName))
	            return service;
	    }
	    return null;
	}

	public static ServiceStatsBean getAnyService(RandBean rnd)
	{
	    return mServices.get(RandLogic.rand(rnd)%mServices.size());
	}
	
	public static String getBestServiceFor(String skill)
	{
	    String best = null;
	    int bestV = 0;
        for (Iterator<ServiceStatsBean> i = mServices.iterator(); i.hasNext(); )
        {
            ServiceStatsBean service = i.next();
            int v = 0;
            for (StringTokenizer st = new StringTokenizer(skill, "|"); st.hasMoreTokens(); )
                v = Math.max(v, countSkills(service, st.nextToken()));
            if (v > bestV)
            {
                bestV = v;
                best = service.getService();
            }
        }
        return best;
	}

    private static int countSkills(ServiceStatsBean service, String skill)
    {
        int v = 0;
        for (int j = 0; j < service.getSkills().length; j++)
            for (int k = 0; k < service.getSkills()[j].length; k++)
                if (skill.equalsIgnoreCase(service.getSkills()[j][k]))
                    v++;
        for (int j =0; j < service.getRankSkills().length; j++)
            if (skill.equalsIgnoreCase(service.getRankSkills()[j]))
                v++;
        return v;
    }
    
    public static void main(String[] argv)
    {
        System.out.println("<services>");
        for (ServiceStatsBean service : mServices)
        {
            System.out.println("<service");
            System.out.println(" id=\""+service.getService()+"\"");
            System.out.println(" title=\""+service.getService()+"\"");
            System.out.println(" enlist=\""+service.getEnlist()+"\"");
            System.out.println(" survival=\""+service.getSurvival()+"\"");
            System.out.println(" commission=\""+service.getCommission()+"\"");
            System.out.println(" promotion=\""+service.getPromotion()+"\"");
            System.out.println(" reenlist=\""+service.getReenlist()+"\"");
            System.out.println(" enlistMod1=\""+service.getEnlistMod1()+"\"");
            System.out.println(" enlistMod2=\""+service.getEnlistMod2()+"\"");
            System.out.println(" survivalMod=\""+service.getSurvivalMod()+"\"");
            System.out.println(" commissionMod=\""+service.getCommissionMod()+"\"");
            System.out.println(" promotionMod=\""+service.getPromotionMod()+"\"");
            System.out.println(">");
            for (String title : service.getTitles())
                System.out.println("<title>"+title+"</title>");
            for (String benefit : service.getMusterBenefits())
                System.out.println("<musterBenefit>"+benefit+"</musterBenefit>");
            for (String money : service.getMusterMoney())
                System.out.println("<musterMoney>"+money+"</musterMoney>");
            for (String[] skillTable : service.getSkills())
            {
                System.out.println("<skillTable>");
                for (String skill : skillTable)
                    System.out.println("<skillRef id=\""+skill+"\"/>");
                System.out.println("</skillTable>");
            }
            for (String rankSkill : service.getRankSkills())
                System.out.println("<rankSkills>"+rankSkill+"</rankSkills>");
            System.out.println("</service>");
        }
        System.out.println("</services>");
    }
}
