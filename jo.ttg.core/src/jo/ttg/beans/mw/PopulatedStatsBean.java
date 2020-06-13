package jo.ttg.beans.mw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.beans.sys.AnimalsBean;
import jo.ttg.logic.mw.UPPLogic;
import jo.util.beans.Bean;

/**
 * @author Joseph Jaquinta
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PopulatedStatsBean extends Bean
{
    public final static int NAVAL_BASE = 1<<0;
    public final static int SCOUT_BASE = 1<<1;
    public final static int LOCAL_BASE = 1<<2;
    public final static int SCOUT_WAY = 1<<3;
    public final static int FARM_BASE = 1<<4;
    public final static int MINE_BASE = 1<<5;
    public final static int COLONY_BASE = 1<<6;
    public final static int E_BASE = 1<<7;
    public final static int I_BASE = 1<<8;
    public final static int RELAY_STATION = 1<<9;
    public final static int NAVAL_OUTPOST = 1<<10;
    public final static int COLON_BASE = 1<<11;
    public final static int TWO_BASE = 1<<12;
    public final static int SEVEN_BASE = 1<<13;
    public final static int THREE_BASE = 1<<14;
    public final static int EIGHT_BASE = 1<<15;
    public final static int MILITARY_GARRISON = 1<<16;
    public final static int LAB_BASE = 1<<17;
    public final static int FOUR_BASE = 1<<18;
    public final static int ZERO_BASE = 1<<19;
    public final static int ONE_BASE = 1<<20;
    public final static int SIX_BASE = 1<<21;
    public final static int FIVE_BASE = 1<<22;
    public final static int NAVAL_DEPOT = 1<<23;
    public final static int CLAN_BASE = 1<<24;
    public final static int TLAUKHU_BASE = 1<<25;
    public final static int J_BASE = 1<<26;
    public final static int U_BASE = 1<<27;
    public final static int CORSAIR_BASE = 1<<28;
    
    public final static int[] BASES = {
        NAVAL_BASE,
        SCOUT_BASE,
        LOCAL_BASE,
        SCOUT_WAY,
        FARM_BASE,
        MINE_BASE,
        COLONY_BASE,
        E_BASE,
        I_BASE,
        RELAY_STATION,
        NAVAL_OUTPOST,
        COLON_BASE,
        TWO_BASE,
        SEVEN_BASE,
        THREE_BASE,
        EIGHT_BASE,
        MILITARY_GARRISON,
        LAB_BASE,
        FOUR_BASE,
        ZERO_BASE,
        ONE_BASE,
        SIX_BASE,
        FIVE_BASE,
        NAVAL_DEPOT,
        CLAN_BASE,
        TLAUKHU_BASE,
        J_BASE,
        U_BASE,
        CORSAIR_BASE,
    };
    public static final char[] BASES_NAME = {
            'N',
            'S',
            'M',
            'W',
            'F',
            '#',
            'C',
            'L',
            'E',
            'I',
            'X',
            'O',
            ':',
            '2',
            '7',
            '3',
            'Q',
            '8',
            '4',
            '0',
            '1',
            '6',
            '5',
            'D',
            'R',
            'T',
            'J',
            'U',     
            'C',
    };
    public static final String[] BASES_DESC = {
            "Naval",
            "Scout",
            "Local",
            "Way Station",
            "Farm",
            "Mine",
            "Colony",
            "Lab",
            "E",
            "I",
            "Relay Station",
            "Naval Outpost",
            ":",
            "2",
            "7",
            "3",
            "Military Garrison",
            "8",
            "4",
            "0",
            "1",
            "6",
            "5",
            "Naval Depot",
            "Clan",
            "Tlaukhu",
            "J",
            "U",        
            "Corsair",        
    };
    
    public static final int[] TRAVEL_ZONES = {
        'G',
        'A',
        'R',
    };

    // Bases
    private long mBases;
    public long getBases()
    {
        return mBases;
    }
    public void setBases(long v)
    {
        mBases = v;
    }

    // UPP
    private UPPBean mUPP;
    public UPPBean getUPP()
    {
        return mUPP;
    }
    public void setUPP(UPPBean v)
    {
        mUPP = v;
    }

    // TravelZone
    private int mTravelZone;
    public int getTravelZone()
    {
        return mTravelZone;
    }
    public void setTravelZone(int v)
    {
        mTravelZone = v;
    }

    // Animals
    private AnimalsBean mAnimals;
    public AnimalsBean getAnimals()
    {
        return mAnimals;
    }
    public void setAnimals(AnimalsBean v)
    {
        mAnimals = v;
    }

    // Economics
    private EconomicsBean mEconomics;
    public EconomicsBean getEconomics()
    {
        return mEconomics;
    }
    public void setEconomics(EconomicsBean v)
    {
        mEconomics = v;
    }

    // Allegiance
    private java.lang.String mAllegiance;
    public java.lang.String getAllegiance()
    {
        return mAllegiance;
    }
    public void setAllegiance(java.lang.String v)
    {
        mAllegiance = v;
    }
    public java.lang.String getAllegianceDesc()
    {
        String a = CODE2DESC.get(mAllegiance);
        if (a == null)
            return mAllegiance;
        else
            return a;
    }
    
    public static String getAllegianceDesc(String all)
    {
        String a = CODE2DESC.get(all);
        if (a == null)
            return all;
        else
            return a;
    }
    
    public static String[] getAllegiances()
    {
        String[] all = CODE2DESC.keySet().toArray(new String[0]);
        Arrays.sort(all);
        return all;
    }
    
    private static final Map<String,String> CODE2DESC = new HashMap<String,String>();
    static
    {
        CODE2DESC.put("A0", "Tlaukhu bloc");
        CODE2DESC.put("A1", "Tlaukhu bloc");
        CODE2DESC.put("A2", "Tlaukhu Bloc");
        CODE2DESC.put("A3", "Tlaukhu Bloc");
        CODE2DESC.put("A4", "Tlaukhu Bloc");
        CODE2DESC.put("A5", "Tlaukhu Bloc");
        CODE2DESC.put("A6", "Tlaukhu Bloc");
        CODE2DESC.put("A7", "Tlaukhu Bloc");
        CODE2DESC.put("A8", "Tlaukhu Bloc");
        CODE2DESC.put("A9", "Tlaukhu Bloc");
        CODE2DESC.put("Ac", "Acara/Solomani");
        CODE2DESC.put("Ac", "Altarean Confederation");
        CODE2DESC.put("Ac", "Anubian Trade Coalition");
        CODE2DESC.put("Ak", "Akeena Union");
        CODE2DESC.put("Ak", "Auftei Ktaih");
        CODE2DESC.put("Al", "Alto/Solomani");
        CODE2DESC.put("Am", "Amon/Imperial");
        CODE2DESC.put("Am", "Amondiage");
        CODE2DESC.put("Ar", "Aoiftu Roakh");
        CODE2DESC.put("As", "Aslan");
        CODE2DESC.put("Au", "Austa/Imperial");
        CODE2DESC.put("Ba", "Confederation of Bammesuka");
        CODE2DESC.put("Bl", "Blackjack/Solomani");
        CODE2DESC.put("Bo", "Bobbo/Solomani");
        CODE2DESC.put("Bs", "Belgardian Sojourn");
        CODE2DESC.put("Bs", "Bright Star Cooperate");
        CODE2DESC.put("Ca", "Corsair Alliance");
        CODE2DESC.put("Ca", "Principality of Caledon");
        CODE2DESC.put("Cb", "Carillian Assembly");
        CODE2DESC.put("Ch", "Chirper");
        CODE2DESC.put("Cn", "Creation of Nonpareil");
        CODE2DESC.put("Co", "Corellian");
        CODE2DESC.put("Cs", "Client State");
        CODE2DESC.put("Ct", "Carter Technocracy");
        CODE2DESC.put("Cu", "Cytrialin Unity");
        CODE2DESC.put("Cv", "Unknown (Cv)");
        CODE2DESC.put("Da", "Darrian");
        CODE2DESC.put("Da", "Daftnew/Solomani");
        CODE2DESC.put("Dc", "Delsun Comagistrant");
        CODE2DESC.put("Dd", "Domain of Deneb");
        CODE2DESC.put("De", "Deriabar/Solomani");
        CODE2DESC.put("Dg", "Dienbach Grüpen");
        CODE2DESC.put("Dj", "Daprolix Juncture of Suns");
        CODE2DESC.put("Dn", "Demos of Nobles");
        CODE2DESC.put("Dr", "Droyne");
        CODE2DESC.put("Ek", "Ekar/Solomani");
        CODE2DESC.put("En", "Unknown");
        CODE2DESC.put("Es", "Eslyat");
        CODE2DESC.put("Es", "Esperanza");
        CODE2DESC.put("Et", "Ethlum/Solomani");
        CODE2DESC.put("Fa", "Federation of Arden");
        CODE2DESC.put("Fd", "Federation of Daibei");
        CODE2DESC.put("Fi", "Federation of Ilelish");
        CODE2DESC.put("Fl", "Florian League");
        CODE2DESC.put("Fo", "Fteiheiel Oih");
        CODE2DESC.put("Ga", "Third Empire of Gashikan");
        CODE2DESC.put("Ga", "Galian Federation");
        CODE2DESC.put("Ga", "Gralyn Assemblage");
        CODE2DESC.put("Gi", "Gniivi");
        CODE2DESC.put("Gk", "Khan World League");
        CODE2DESC.put("Gl", "Ginlenchy Concordance");
        CODE2DESC.put("Go", "Gyj-nuah 'Oew");
        CODE2DESC.put("Gt", "Great Terbah");
        CODE2DESC.put("Gu", "Gursky/Solomani");
        CODE2DESC.put("H*", "Star Patterns Trading");
        CODE2DESC.put("Ha", "Hachiman/Solomani");
        CODE2DESC.put("Hc", "Hefrin Colony");
        CODE2DESC.put("Hc", "Council of Heads");
        CODE2DESC.put("He", "Hessoun/Solomani");
        CODE2DESC.put("Hf", "Fed. Develop. Agency");
        CODE2DESC.put("Hi", "Six Eyes Nest");
        CODE2DESC.put("Hl", "Julian Prot. (Lorean Hegem.)");
        CODE2DESC.put("Ho", "Hochiken People's Assembly");
        CODE2DESC.put("Hr", "Harpring/Solomani");
        CODE2DESC.put("Hv", "Hive Federation");
        CODE2DESC.put("Ic", "Icenogle/Solomani");
        CODE2DESC.put("Id", "Islaiat Dominate");
        CODE2DESC.put("If", "Iyeaao'fte");
        CODE2DESC.put("Im", "Imperial");
        CODE2DESC.put("J-", "Julian Prot. (Unincorporated)");
        CODE2DESC.put("Ja", "Julian Prot. (Asimikigir Confed.)");
        CODE2DESC.put("Jc", "Julian Prot. (Const. of Koekhon)");
        CODE2DESC.put("Jd", "Joie De Vivre");
        CODE2DESC.put("Jf", "Jonson-Bowes Federation");
        CODE2DESC.put("Jh", "Julian Prot. (Hhkar Sphere)");
        CODE2DESC.put("Jl", "Julian Prot. (Lumda Dower)");
        CODE2DESC.put("Jm", "Julian Prot. (Cmnwlth of Mendan)");
        CODE2DESC.put("Jn", "Jurisiction of Nadon");
        CODE2DESC.put("Jo", "Joyeuse");
        CODE2DESC.put("Jo", "Julian Prot. (Alliance of Ozuvon)");
        CODE2DESC.put("Jp", "Julian Prot. (Pirbarish Starlane)");
        CODE2DESC.put("JP", "Julian Protectorate");
        CODE2DESC.put("Jr", "Julian Prot. (Rukadukaz Rep.)");
        CODE2DESC.put("Jr", "Julian Prot. (Rukadukaz Repub.)");
        CODE2DESC.put("Ju", "Julian Prot. (Ukhanzi Coord.)");
        CODE2DESC.put("Jv", "Julian Prot. (Vugurar Dominion)");
        CODE2DESC.put("KC", "K'kree Client State");
        CODE2DESC.put("Kl", "Kline/Solomani");
        CODE2DESC.put("Kx", "Krax Confederation");
        CODE2DESC.put("La", "Lachlan/Solomani");
        CODE2DESC.put("La", "League of Antares");
        CODE2DESC.put("Lg", "Landgrebe/Solomani");
        CODE2DESC.put("Li", "Lucan's Imperium");
        CODE2DESC.put("Lp", "Council of Leh Perash");
        CODE2DESC.put("Lv", "Lords of Vision");
        CODE2DESC.put("Ma", "Marlan Primate");
        CODE2DESC.put("Ma", "Margaret's Imperium");
        CODE2DESC.put("Me", "Megusard Corporate");
        CODE2DESC.put("Mh", "Grand Duchy of Marlheim");
        CODE2DESC.put("Mi", "Minsk/Solomani");
        CODE2DESC.put("Mn", "Mandanin Co-Dominion");
        CODE2DESC.put("Mo", "Morsen/Solomani");
        CODE2DESC.put("Mu", "Malorn Union");
        CODE2DESC.put("Na", "Non-aligned");
        CODE2DESC.put("Nb", "Neubayem");
        CODE2DESC.put("Nc", "New Colchis");
        CODE2DESC.put("Nh", "New Home");
        CODE2DESC.put("Nu", "Nubtar/Solomani");
        CODE2DESC.put("Oc", "Ock/Solomani");
        CODE2DESC.put("Of", "Oleaiy'fte");
        CODE2DESC.put("Ow", "Outcasts of the Whispering Sky");
        CODE2DESC.put("Pl", "Plavian League");
        CODE2DESC.put("Pb", "Parity of Brothers");
        CODE2DESC.put("Ra", "Ral Ranta");
        CODE2DESC.put("Ra", "Randzo/Solomani");
        CODE2DESC.put("Re", "Reynold/Imperial");
        CODE2DESC.put("Re", "Renkard Union");
        CODE2DESC.put("Rn", "Rm Nai");
        CODE2DESC.put("Rv", "Restored Vilani Empire");
        CODE2DESC.put("Sa", "Sansterre");
        CODE2DESC.put("Sa", "Sapphire/Solomani");
        CODE2DESC.put("Sb", "Serendip Belt");
        CODE2DESC.put("Sc", "Sarkan Constellation");
        CODE2DESC.put("Sh", "Sharn/Solomani");
        CODE2DESC.put("Sk", "Stanko/Solomani");
        CODE2DESC.put("So", "Solomani");
        CODE2DESC.put("Sr", "Stansifer/Solomani");
        CODE2DESC.put("St", "Stancombe/Solomani");
        CODE2DESC.put("St", "Strephon's Worlds");
        CODE2DESC.put("Sw", "Sword Worlds");
        CODE2DESC.put("Sw", "Swanfei Independency");
        CODE2DESC.put("Sy", "Sylean Federation");
        CODE2DESC.put("Sy", "Sydymic Empire");
        CODE2DESC.put("Ta", "Tealou Arlaoh");
        CODE2DESC.put("Tb", "Trita Brotherhood");
        CODE2DESC.put("Tc", "Tellerian Cluster");
        CODE2DESC.put("Td", "Trelyn Domain");
        CODE2DESC.put("Tl", "Tolson/Solomani");
        CODE2DESC.put("To", "Tooma/Solomani");
        CODE2DESC.put("Tr", "Trindel Confederacy");
        CODE2DESC.put("Tr", "Toh Republic");
        CODE2DESC.put("Tw", "Theocracy of Weltschmerz");
        CODE2DESC.put("Uh", "Union of Harmony");
        CODE2DESC.put("Un", "Unpopulated");
        CODE2DESC.put("V6", "Assemblage of 1116");
        CODE2DESC.put("V7", "Vargr (17th Disjuncture)");
        CODE2DESC.put("Va", "Vargr (independent)");
        CODE2DESC.put("Ve", "Vegan");
        CODE2DESC.put("Vf", "Dzarrgh Federate");
        CODE2DESC.put("Vg", "Glory of Taarskoerzn");
        CODE2DESC.put("Vh", "Irrgh Manifest");
        CODE2DESC.put("Vi", "Viyard Concourse");
        CODE2DESC.put("Vn", "Vargr (Ngath Confederation)");
        CODE2DESC.put("Vu", "United Followers of Augurgh");
        CODE2DESC.put("Vw", "Julian Prot. (Rar Errall/Wolves W.)");
        CODE2DESC.put("Vx", "Vargr (Antares Pact)");
        CODE2DESC.put("Wa", "Wair/Solomani");
        CODE2DESC.put("Wc", "Counsel of the Wise");
        CODE2DESC.put("Wd", "Winston Democracy");
        CODE2DESC.put("Wi", "Wilkerson/Solomani");
        CODE2DESC.put("Wu", "Wu/Solomani");
        CODE2DESC.put("Ww", "Woal Warliylr");
        CODE2DESC.put("Xf", "FDA world outside of HF");
        CODE2DESC.put("Xi", "Xianjin/Solomani");
        CODE2DESC.put("Za", "Zarian Realm");
        CODE2DESC.put("Zh", "Zhodani");
        CODE2DESC.put("Zo", "Zongwu/Solomani");
        CODE2DESC.put("Zu", "Zuugabish Tripartite");
    }
    
    private static final Map<Character,String> BASE2DESC = new HashMap<Character,String>();
    static
    {
        //                                   Ind  Imp  Zho  Sol  Var  Asl  Hiv  Kkr  Dro
        //Naval Depot                              D    Y
        //Naval Base                          J    N    Z    N    G         L    K    P
        //Naval Outpost                                                          O
        //Scout Way Station                        W
        //Scout Base                               S         S
        //Military Base                       M    M    M    M    M    M    M    M    M
        //Military Garrison                                                           Q
        //Naval Base and Scout Base                A         A
        //Naval Base and Scout Way Station         B
        //Naval Base and Military Base        F                             F
        //Relay Station                                 X
        //Corsair Base                                            C
        //Naval and Corsair Base                                  H
        //Clan Naval Base                                              R
        //Tlaukhu Naval Base                                           T
        //Clan and Tlaukhu Naval Base                                  U
        //Embassy Center                                                    E
        BASE2DESC.put('A', "Naval/Scout Base");
        BASE2DESC.put('B', "Naval Base/Scout Way Station");
        BASE2DESC.put('C', "Corsair Base");
        BASE2DESC.put('D', "Naval Depot");
        BASE2DESC.put('E', "Embassy Center");
        BASE2DESC.put('F', "Naval/Military Base");
        BASE2DESC.put('G', "Naval Base (Va)");
        BASE2DESC.put('H', "Naval/Corsair Base");
        BASE2DESC.put('J', "Naval Base (Ind)");
        BASE2DESC.put('K', "Naval Base (Kk)");
        BASE2DESC.put('L', "Naval Base (Hi)");
        BASE2DESC.put('M', "Military Base");
        BASE2DESC.put('N', "Naval Base");
        BASE2DESC.put('O', "Naval Outpost");
        BASE2DESC.put('P', "Naval Base (Dr)");
        BASE2DESC.put('Q', "Military Garrison");
        BASE2DESC.put('R', "Clan Base");
        BASE2DESC.put('S', "Scout Base");
        BASE2DESC.put('T', "Tlaukhu Naval Base");
        BASE2DESC.put('U', "Clan/Tlaukhu Naval Base");
        BASE2DESC.put('W', "Scout Way Station");
        BASE2DESC.put('X', "Relay Station");
        BASE2DESC.put('Y', "Naval Depot (Zh)");
        BASE2DESC.put('Z', "Naval Base (Zh)");
    }

    // constructor
    public PopulatedStatsBean()
    {
        mBases = 0;
        mUPP = new UPPBean();
        mTravelZone = 0;
        mAnimals = new AnimalsBean();
        mEconomics = new EconomicsBean();
        mAllegiance = new java.lang.String();
    }

    public boolean isBase(int mask)
    {
        return ((mBases & mask) != 0);
    }

    public boolean isNavalBase()
    {
        return isBase(NAVAL_BASE);
    }
    public boolean isScoutBase()
    {
        return isBase(SCOUT_BASE);
    }
    public boolean isLocalBase()
    {
        return isBase(LOCAL_BASE);
    }
    public boolean isScoutWay()
    {
        return isBase(SCOUT_WAY);
    }
    public boolean isFarmBase()
    {
        return isBase(FARM_BASE);
    }
    public boolean isMineBase()
    {
        return isBase(MINE_BASE);
    }
    public boolean isColonyBase()
    {
        return isBase(COLONY_BASE);
    }
    public boolean isLabBase()
    {
        return isBase(LAB_BASE);
    }

    public String getBasesDesc()
    {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < BASES.length; i++)
            if (isBase(BASES[i]))
                ret.append(BASES_NAME[i]);
        return ret.toString();
    }
    public String getBasesDescLong()
    {
        List<String> bases = new ArrayList<String>();
        for (int i = 0; i < BASES.length; i++)
            if (isBase(BASES[i]))
                bases.add(BASES_DESC[i]);
        StringBuffer ret = new StringBuffer();
        for (String base : bases)
        {
            if (ret.length() > 0)
                ret.append(", ");
            ret.append(base);
        }
        return ret.toString();
    }
    public String getTravelZoneDesc()
    {
    	switch (mTravelZone)
    	{
    		case 'R':
    			return "Red";
    		case 'A':
    			return "Amber"; 
    	}
    	return "Green";
    }

    public boolean isRing()
    {
        return getUPP().getSize().getValue() == UPPSizBean.S_RING;
    }

    public boolean isTiny()
    {
        return getUPP().getSize().getValue() == UPPSizBean.S_TINY;
    }
    
    public String toString()
    {
        return UPPLogic.getUPPDesc(mUPP)+" "+UPPLogic.getTradeCodesDesc(mUPP);
    }
}
