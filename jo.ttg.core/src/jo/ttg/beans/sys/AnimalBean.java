/**
 * Created on Sep 20, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package jo.ttg.beans.sys;

import jo.util.beans.Bean;

/**
 * Class for generated Animal
 *
 * This class encapsulates a generated animal or event.
 * It is tabulated within the TAnimals class.
 *
 * @author  Joseph Jaquinta
 * @version     3.0, 01 July, 2001
 * @see ttg.model.AnimalsBean
 */
public class AnimalBean extends Bean
{
    /* animal types */
    /**
      * Animal Type scavenger
      * @see ttg.TAnimal#sType
      */
    public static final int A_SCAVENGER = 0;
    /**
      * Animal Type herbivore
      * @see ttg.TAnimal#sType
      */
    public static final int A_HERBIVORE = 1;
    /**
      * Animal Type omnivore
      * @see ttg.TAnimal#sType
      */
    public static final int A_OMNIVORE = 2;
    /**
      * Animal Type carnivore
      * @see ttg.TAnimal#sType
      */
    public static final int A_CARNIVORE = 3;

    /* animal sub-types */
    /**
      * Animal Subtype filter
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_FILTER = 0;
    /**
      * Animal Subtype intermittent
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_INTERMITTENT = 1;
    /**
      * Animal Subtype grazer
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_GRAZER = 2;

    /**
      * Animal Subtype gatherer
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_GATHERER = 3;
    /**
      * Animal Subtype eater
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_EATER = 4;
    /**
      * Animal Subtype hunter
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_HUNTER = 5;

    /**
      * Animal Subtype siren
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_SIREN = 6;
    /**
      * Animal Subtype pouncer
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_POUNCER = 7;
    /**
      * Animal Subtype killer
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_KILLER = 8;
    /**
      * Animal Subtype trapper
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_TRAPPER = 9;
    /**
      * Animal Subtype chaser
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_CHASER = 10;

    /**
      * Animal Subtype carrion eater
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_CARRION_EATER = 11;
    /**
      * Animal Subtype reducer
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_REDUCER = 12;
    /**
      * Animal Subtype hijacker
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_HIJACKER = 13;
    /**
      * Animal Subtype intimidator
      * @see ttg.TAnimal#sSubType
      */
    public static final int A_INTIMIDATOR = 14;

    /* terrain types */
    /**
      * Terrain type clear
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_CLEAR = 0;
    /**
      * Terrain type prairie
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_PRAIRIE = 1;
    /**
      * Terrain type rough
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_ROUGH = 2;
    /**
      * Terrain type broken
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_BROKEN = 3;
    /**
      * Terrain type mountain
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_MOUNTAIN = 4;
    /**
      * Terrain type forest
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_FOREST = 5;
    /**
      * Terrain type jungle
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_JUNGLE = 6;
    /**
      * Terrain type river
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_RIVER = 7;
    /**
      * Terrain type swamp
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_SWAMP = 8;
    /**
      * Terrain type marsh
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_MARSH = 9;
    /**
      * Terrain type desert
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_DESERT = 10;
    /**
      * Terrain type beach
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_BEACH = 11;
    /**
      * Terrain type sea surface
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_SURFACE = 12;
    /**
      * Terrain type shallow water
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_SHALLOWS = 13;
    /**
      * Terrain type depths of the ocean
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_DEPTHS = 14;
    /**
      * Terrain type sea bottom
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_BOTTOM = 15;
    /**
      * Terrain type sea cave
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_SEA_CAVE = 16;
    /**
      * Terrain type sargasso sea
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_SARGASSO = 17;
    /**
      * Terrain type ruins
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_RUINS = 18;
    /**
      * Terrain type cave
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_CAVE = 19;
    /**
      * Terrain type chasm
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_CHASM = 20;
    /**
      * Terrain type crater
      * @see ttg.TAnimal#sTerrain(int)
      */
    public static final int T_CRATER = 21;

    /* animal clases */
    /**
      * Animal movement type swimer
      * @see ttg.TAnimal#sMovementType
      */
    public static final int T_SWIMER = 1;
    /**
      * Animal movement type flyer
      * @see ttg.TAnimal#sMovementType
      */
    public static final int T_FLYER = 2;
    /**
      * Animal movement type amphibian
      * @see ttg.TAnimal#sMovementType
      */
    public static final int T_AMPHIB = 3;
    /**
      * Animal movement type triphibian
      * @see ttg.TAnimal#sMovementType
      */
    public static final int T_TRIPHIB = 4;

    static private final String a_atype[] =
        { "Scavenger", "Herbivore", "Omnivore", "Carnivore" };
    static private final String a_terrain[] =
        {
            "Prairie",
            "Rough",
            "Broken",
            "Mountain",
            "Forest",
            "Jungle",
            "River",
            "Swamp",
            "Marsh",
            "Desert",
            "Beach",
            "Surface",
            "Shallows",
            "Depths",
            "Bottom",
            "Sea cave",
            "Sargasso",
            "Ruins",
            "Cave",
            "Chasm",
            "Crater" };
    static private final String a_mtype[] =
        { "", "Swimmer", "Flyer", "Amphibian", "Triphibian" };
    /*
    static private final int ter_typ_dm[] =
        {
            3,
            4,
            0,
            -3,
            0,
            -4,
            -3,
            1,
            -2,
            0,
            3,
            3,
            2,
            2,
            -4,
            -2,
            -2,
            -4,
            -3,
            -4,
            -1,
            0 };
    static private final int ter_wt_dm[] =
        {
            0,
            0,
            0,
            -3,
            0,
            -4,
            -2,
            1,
            4,
            -1,
            -3,
            2,
            3,
            2,
            0,
            0,
            0,
            -2,
            0,
            1,
            -3,
            -1 };

    static private final int her_stype[] =
        {
            A_FILTER,
            A_FILTER,
            A_FILTER,
            A_INTERMITTENT,
            A_INTERMITTENT,
            A_INTERMITTENT,
            A_INTERMITTENT,
            A_GRAZER,
            A_GRAZER,
            A_GRAZER,
            A_GRAZER,
            A_GRAZER,
            A_GRAZER };
    static private final int omn_stype[] =
        {
            A_GATHERER,
            A_GATHERER,
            A_EATER,
            A_GATHERER,
            A_EATER,
            A_GATHERER,
            A_HUNTER,
            A_HUNTER,
            A_HUNTER,
            A_GATHERER,
            A_EATER,
            A_HUNTER,
            A_GATHERER,
            A_GATHERER };
    static private final int car_stype[] =
        {
            A_SIREN,
            A_POUNCER,
            A_SIREN,
            A_POUNCER,
            A_KILLER,
            A_TRAPPER,
            A_POUNCER,
            A_CHASER,
            A_CHASER,
            A_CHASER,
            A_KILLER,
            A_CHASER,
            A_SIREN,
            A_CHASER };
    static private final int sca_stype[] =
        {
            A_CARRION_EATER,
            A_CARRION_EATER,
            A_REDUCER,
            A_HIJACKER,
            A_CARRION_EATER,
            A_INTIMIDATOR,
            A_REDUCER,
            A_CARRION_EATER,
            A_REDUCER,
            A_HIJACKER,
            A_INTIMIDATOR,
            A_REDUCER,
            A_HIJACKER,
            A_INTIMIDATOR };

    static private final int ani_num[][] =
        { { 1, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 2, 4, 5 }, {
            0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 1, 1, 0, 0 }, {
            0, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 2, 0, 1 }, {
            1, 2, 1, 1, 2, 1, 0, 1, 0, 0, 0, 1, 0, 1 }
    };
    static private final long ani_wt[] =
        {
            1L,
            3L,
            6L,
            12L,
            25L,
            50L,
            100L,
            200L,
            400L,
            800L,
            1600L,
            3200L,
            0L,
            6000L,
            12000L,
            24000L,
            30000L,
            36000L,
            40000L,
            44000L };
    static private final int ani_hits[] =
        { 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 3, 3, 0, 3, 3, 4, 5, 5, 5, 6 };
    static private final int ani_wnd_d[] =
        { 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 2, 2, 3, 3, 3, 3, 3 };
    static private final int ani_wnd_p[] =
        { 0, 1, 1, 2, 3, 0, 1, 1, 1, 3, 3, 4, 0, 0, 2, 0, 0, 3, 6, 6 };
    static private final int ani_mod_t[] =
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1 };
    static private final int ani_mod_d[] =
        { -1, -1, 0, 0, 0, 0, 0, 0, 1, 1, 2, 3, 0, 1, 1, 1, 2, 2, 2, 3 };
    static private final int ani_mod_p[] =
        { -3, -3, -1, 0, 0, 0, 0, 1, -3, 0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0 };
        */
    static private final String ani_weap[] =
        {
            "horns and hooves",
            "horns",
            "hooves and teeth",
            "hooves",
            "horns and teeth",
            "thrasher",
            "claws and teeth",
            "teeth",
            "claws",
            "claws",
            "thrasher",
            "claws and teeth",
            "claws + 1",
            "stinger",
            "claws + 1 and teeth + 1",
            "teeth + 1",
            "as blade",
            "as pike",
            "as broadsword",
            "as body pistol" };
    static private final String ani_armr[] =
        {
            "",
            "",
            "",
            "jack",
            "",
            "",
            "",
            "",
            "",
            "jack",
            "",
            "",
            "mesh + 1",
            "cloth + 1",
            "mesh",
            "cloth",
            "combat + 4",
            "reflec",
            "ablat",
            "battle" };

    static private final String a_astype[] =
        {
            "Filter",
            "Intermittent",
            "Grazer",
            "Gatherer",
            "Eater",
            "Hunter",
            "Siren",
            "Pouncer",
            "Killer",
            "Trapper",
            "Chaser",
            "Carrion Eater",
            "Reducer",
            "Hijacker",
            "Intimidator" };
    /* 10 = if possible, 11 = if surprise, 12 = if more */
    /*
    static private final int ani_toflee[] =
        { 2, 3, -1, 3, 0, 0, 11, 11, 0, 11, 12, 3, 3, 1, 2 };
    static private final int ani_toatt[] =
        { 10, 3, 2, 2, 3, 2, 3, 11, 3, 2, 3, 2, 2, 2, 1 };
    static private final int ani_speed[] =
        { -5, -4, -2, -3, -4, -3, -4, -4, -3, -5, -2, -3, -4, -4, -4 };
    static private final int ani_mspeed[] =
        { 0, 1, 2, 1, 1, 1, 0, 1, 1, 0, 2, 1, 1, 1, 1 };
    */

    private String Name;
    private int type;
    private int stype;
    private int quan;
    private int mtype;
    private long wt;
    private int hits;
    private int wound_d, wound_p;
    private int mod_t, mod_d, mod_p;
    private int weap;
    private int armr;
    private int flee, attack, speed;

    /**
     * Returns a String description of the armor type of the animal's skin.
     *
     * @return    the armor type.
     */
    public String getArmourDescription()
    {
        if (type < 0)
            return "";
        return ani_armr[armr];
    }
    /**
     * Returns a String description of the chance to attack of the animal.
     *
     * @return    the chance to attack.
     */
    public String getAttackDescription()
    {
        if (type < 0)
            return "";
        if (attack < 10)
            return String.valueOf(attack);
        else if (attack == 10)
            return "if possible";
        else if (attack == 11)
            return "if surprised";
        else if (attack == 12)
            return "if more";
        return "";
    }
    /**
     * Returns a String description of the damange of the animal's attack.
     *
     * @return    the attack damage.
     */
    public String getDamageDescription()
    {
        String ret;

        if (type < 0)
            return "";
        if (wound_d != 0)
            ret = wound_d + "D+" + wound_p;
        else
            ret = wound_p + "   ";
        if ((mod_d != 0) || (mod_p != 0))
        {
            if (mod_t != 0)
                ret += " x";
            else if (mod_d > 0)
                ret += " +";
            if (mod_d != 0)
                if (mod_p >= 0)
                    ret += mod_d + "D+" + mod_p;
                else
                    ret += mod_d + "D" + mod_p;
            else if (mod_p >= 0)
                ret += "+" + mod_p + "  ";
            else
                ret += mod_p + "   ";
        }
        return ret;
    }
    /**
     * Returns a String description of the chance of the animal to flee
     *
     * @return    the chance to flee
     */
    public String getFleeDescription()
    {
        if (type < 0)
            return "";
        if (flee < 10)
            return String.valueOf(flee);
        else if (flee == 10)
            return "if possible";
        else if (flee == 11)
            return "if surprised";
        else if (flee == 12)
            return "if more";
        return "";
    }
    /**
     * Returns a String description of the hit dice of the animal.
     *
     * @return    the hit dice.
     */
    public String getHitsDescription()
    {
        if (type < 0)
            return "";
        if (hits == 0)
            return "1 hit";
        return hits + "D hits";
    }
    /**
     * Returns a String description of the movement type of the animal.
     *
     * @return    the movement type.
     */
    public String getMovementTypeDescription()
    {
        if (type >= 0)
            return a_mtype[type];
        return "";
    }
    /**
     * Returns a String description of the normal quantity appearing of the animal.
     *
     * @return    the quantity.
     */
    public String getQuantityDescription()
    {
        if ((type >= 0) && (quan != 0))
            //return "(" + TObject.sNum(quan) + "D)";
            return "(" + quan + "D)";
        return "";
    }
    /**
     * Returns a String description of the speed of the animal.
     *
     * @return    the speed.
     */
    public String getSpeedDescription()
    {
        if (type < 0)
            return "";
        return String.valueOf(speed);
    }
    /**
     * Returns a String description of the subtype of the animal.
     *
     * @return    the animal subtype.
     */
    public String getSubTypeDescription()
    {
        if (type >= 0)
            return a_astype[stype];
        return "";
    }
    /**
     * Returns a String description of an animal terrain type.
     *
     * @param    ix the terrain index.
     * @return    the movement type.
     */
    public static String getTerrainDescription(int ix)
    {
        return a_terrain[ix];
    }
    /**
     * Returns a String description of the type of the animal.
     *
     * @return    the animal type.
     */
    public String getTypeDescription()
    {
        if (type >= 0)
            return a_atype[type];
        return "";
    }
    /**
     * Returns a String description of the weapon type of the animal.
     *
     * @return    the weapon type.
     */
    public String getWeaponDescription()
    {
        if (type < 0)
            return "";
        return ani_weap[weap];
    }
    /**
     * Returns a String description of the weight of the animal.
     *
     * @return    the weignt.
     */
    public String getWeightDescription()
    {
        if (type < 0)
            return "";
        return wt + "kg  ";
    }
    /**
     * Returns a one line String description of the animal.
     *
     * @return    the description.
     */
    public String toString()
    {
        if (type < 0)
            return "Event";
        return getName()
            + " "
            + getQuantityDescription()
            + " "
            + getTypeDescription()
            + " "
            + getSubTypeDescription()
            + " "
            + getMovementTypeDescription()
            + " "
            + getWeightDescription()
            + " "
            + getHitsDescription()
            + " "
            + getWeaponDescription()
            + " "
            + getFleeDescription()
            + " "
            + getAttackDescription()
            + " "
            + getSpeedDescription();
    }
    /**
     * Returns the armr.
     * @return int
     */
    public int getArmor()
    {
        return armr;
    }

    /**
     * Returns the attack.
     * @return int
     */
    public int getAttack()
    {
        return attack;
    }

    /**
     * Returns the flee.
     * @return int
     */
    public int getFlee()
    {
        return flee;
    }

    /**
     * Returns the hits.
     * @return int
     */
    public int getHits()
    {
        return hits;
    }

    /**
     * Returns the mod_d.
     * @return int
     */
    public int getModD()
    {
        return mod_d;
    }

    /**
     * Returns the mod_p.
     * @return int
     */
    public int getModP()
    {
        return mod_p;
    }

    /**
     * Returns the mod_t.
     * @return int
     */
    public int getModT()
    {
        return mod_t;
    }

    /**
     * Returns the mtype.
     * @return int
     */
    public int getMovementType()
    {
        return mtype;
    }

    /**
     * Returns the name.
     * @return String
     */
    public String getName()
    {
        if (type < 0)
            return "Event";
        return Name;
    }

    /**
     * Returns the quan.
     * @return int
     */
    public int getQuan()
    {
        return quan;
    }

    /**
     * Returns the speed.
     * @return int
     */
    public int getSpeed()
    {
        return speed;
    }

    /**
     * Returns the stype.
     * @return int
     */
    public int getSType()
    {
        return stype;
    }

    /**
     * Returns the type.
     * @return int
     */
    public int getType()
    {
        return type;
    }

    /**
     * Returns the weap.
     * @return int
     */
    public int getWeap()
    {
        return weap;
    }

    /**
     * Returns the wound_d.
     * @return int
     */
    public int getWoundD()
    {
        return wound_d;
    }

    /**
     * Returns the wound_p.
     * @return int
     */
    public int getWoundP()
    {
        return wound_p;
    }

    /**
     * Returns the wt.
     * @return long
     */
    public long getWeight()
    {
        return wt;
    }

    /**
     * Sets the armr.
     * @param armr The armr to set
     */
    public void setArmor(int armr)
    {
        this.armr = armr;
    }

    /**
     * Sets the attack.
     * @param attack The attack to set
     */
    public void setAttack(int attack)
    {
        this.attack = attack;
    }

    /**
     * Sets the flee.
     * @param flee The flee to set
     */
    public void setFlee(int flee)
    {
        this.flee = flee;
    }

    /**
     * Sets the hits.
     * @param hits The hits to set
     */
    public void setHits(int hits)
    {
        this.hits = hits;
    }

    /**
     * Sets the mod_d.
     * @param mod_d The mod_d to set
     */
    public void setModD(int mod_d)
    {
        this.mod_d = mod_d;
    }

    /**
     * Sets the mod_p.
     * @param mod_p The mod_p to set
     */
    public void setModP(int mod_p)
    {
        this.mod_p = mod_p;
    }

    /**
     * Sets the mod_t.
     * @param mod_t The mod_t to set
     */
    public void setModT(int mod_t)
    {
        this.mod_t = mod_t;
    }

    /**
     * Sets the mtype.
     * @param mtype The mtype to set
     */
    public void setMovementType(int mtype)
    {
        this.mtype = mtype;
    }

    /**
     * Sets the name.
     * @param name The name to set
     */
    public void setName(String name)
    {
        Name = name;
    }

    /**
     * Sets the quan.
     * @param quan The quan to set
     */
    public void setQuan(int quan)
    {
        this.quan = quan;
    }

    /**
     * Sets the speed.
     * @param speed The speed to set
     */
    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    /**
     * Sets the stype.
     * @param stype The stype to set
     */
    public void setSType(int stype)
    {
        this.stype = stype;
    }

    /**
     * Sets the type.
     * @param type The type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * Sets the weap.
     * @param weap The weap to set
     */
    public void setWeap(int weap)
    {
        this.weap = weap;
    }

    /**
     * Sets the wound_d.
     * @param wound_d The wound_d to set
     */
    public void setWoundD(int wound_d)
    {
        this.wound_d = wound_d;
    }

    /**
     * Sets the wound_p.
     * @param wound_p The wound_p to set
     */
    public void setWoundP(int wound_p)
    {
        this.wound_p = wound_p;
    }

    /**
     * Sets the wt.
     * @param wt The wt to set
     */
    public void setWeight(long wt)
    {
        this.wt = wt;
    }
}

