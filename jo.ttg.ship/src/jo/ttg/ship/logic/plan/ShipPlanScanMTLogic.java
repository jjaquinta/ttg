package jo.ttg.ship.logic.plan;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jo.ttg.ship.beans.comp.Hull;
import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipScanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.util.utils.io.FileUtils;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.IntegerUtils;

public class ShipPlanScanMTLogic
{
    
    public static final String SAMPLE = " Liirushu class System Defense Boat (Civilian Spaceship)\r\n" + 
            "\r\n" + 
            "The Liirushu (\"Defender\" in Old High Vilani) is a standard system defense boat design of substantial antiquity. Although seldom built by worlds beyond the Vilani cultural region at the present, the Liirishus are a common sight in that region. The appearance in not remarkable, being a standard boxy Vilani product.\r\n" + 
            "\r\n" + 
            "CraftID:    Liirushu class SDB, TL10, MCr319.5 (Mass Cost MCr256)\r\n" + 
            "Hull:   360/900, Disp=400t, Config=4SL, Armor=52E, Loaded=10083t, Unloaded=9733t\r\n" + 
            "Power:  62/124, Fusion=5580MW, Duration=39.6 days at full power\r\n" + 
            "Loco:   62/124, Maneuver=6 (StdGrav=44.2kt), MaxSpeed=1000kph, Cruise=750kph, TrueAcc=4.38G (2.19G deepspace), Agility=2 (deepspace)\r\n" + 
            "Comm:   Radio=System, Laser=System, Maser=System\r\n" + 
            "Sensors:    EMS Active(FarOrbit), EMS Passive(Interplanetary), ActObjScan=Rout, ActObjPin=Rout, PassEnScan=Rout\r\n" + 
            "Off:    \r\n" + 
            "\r\n" + 
            "Hardpoints=4\r\n" + 
            "               Missile=x02   BeamLaser=xx3\r\n" + 
            "          Batteries      2               1\r\n" + 
            "          Bearing        2               1\r\n" + 
            "\r\n" + 
            "Def:    \r\n" + 
            "\r\n" + 
            "DefDM+7\r\n" + 
            "              SandCaster=x04\r\n" + 
            "          Batteries        1\r\n" + 
            "          Bearing          1\r\n" + 
            "\r\n" + 
            "Control:    Computer Mod4*3, 6*HeadsUpDisplay, 690*DynLink\r\n" + 
            "Accom:  Crew=8 (2 bridge, 2 engineer, 2 gunners, 1 command, 1 medical), Staterooms=8, Env=basic env, basic ls, extended ls, inertial comp\r\n" + 
            "Other:  Fuel=2650kl, Cargo=135kl, Missile Magazine=60kl (100b-r), Fuel Scoops, ObjSize=Large, EmLevel=Moderate\r\n" + 
            "\r\n" + 
            "Author: R.S.Dean ";

    public static ShipScanBean scanMT(File f) throws IOException
    {
        String txt = FileUtils.readFileAsString(f.toString());
        ShipScanBean scan = scanMT(txt);
        if (scan != null)
            scan.getMetadata().put("file", f.toString());
        return scan;
    }

    public static ShipScanBean scanMT(String rawText)
    {
        ShipScanBean scan = new ShipScanBean();
        scan.getMetadata().put("raw", rawText);
        Map<String,String> text = chunkText(rawText);
        scan.setVolume(getVolume(text));
        if (scan.getVolume() < 13)
            return null;
        scan.setConfiguration(getConfiguration(text));
        
        scanManeuver(scan.getItems(), scan.getVolume(), text);
        scanJump(scan.getItems(), scan.getVolume(), text);
        scanPower(scan.getItems(), scan.getVolume(), text);
        scanCargo(scan.getItems(), scan.getVolume(), text);
        scanFuel(scan.getItems(), scan.getVolume(), text);
        scanSubcraft(scan.getItems(), scan.getVolume(), text);
        scanTurrets(scan.getItems(), scan.getVolume(), text);
        scanStaterooms(scan.getItems(), scan.getVolume(), text);
//      items.add(new PlanItem(ShipSquareBean.JUMP, 10000*6.75));
//      items.add(new PlanItem(ShipSquareBean.HANGER, 30*13.5*1.1, 300, "Fighter Bay"));
//      items.add(new PlanItem(ShipSquareBean.HANGER, 100*13.5*1.1, 10, "Tender Bay"));
        
        return scan;
    }
    
    private static String getLineAt(String text, int o)
    {
        int start = o;
        while ((start > -1) && (text.charAt(start) != '\n'))
            start--;
        int end = o;
        while ((end < text.length()) && (text.charAt(end) != '\n'))
            end++;
        String line = text.substring(start+1, end);
        return line;
    }
    
    private static final String BEAM_LASER="BeamLaser";
    private static final String PULSE_LASER="PulseLaser";
    private static final String MISSILE="Missile";
    private static final String SAND_CASTER="SandCaster";
    private static final String PLASMA_GUN="PlasmaGun";
    private static final String FUSION_GUN="FusionGun";
    private static final String PARTICLE_ACCELERATOR="PartAcc";
    private static final String MESON_GUN="MesonGun";
    private static final String REPULSOR="Repulsor";      
    private static final String[] WEAPONS = {
            BEAM_LASER,
            MISSILE,
            SAND_CASTER,
            PLASMA_GUN,
            FUSION_GUN,
            PARTICLE_ACCELERATOR,
            MESON_GUN,
            REPULSOR,      
    };
    
    private static void scanTurrets(List<PlanItem> items, int volume,
            Map<String,String> text)
    {
        String v = getValue(CRAFTID, "TL", text);
        int tl = IntegerUtils.parseInt(v);
        for (String weapon : WEAPONS)
        {
            String chunk = text.get(OFF);
            v = getValue(weapon, chunk);
            if (v == null)
            {
                chunk = text.get(DEF);
                v = getValue(weapon, chunk);
                if (v == null)
                    continue;
            }
            if (v.length() < 3)
                continue;
            char spineFactor = v.charAt(0);
            if ((spineFactor != 'x') && (spineFactor != '0'))
            {
                int spineSize = calcSpineSize(spineFactor, tl, weapon);
                items.add(new PlanItem(ShipSquareBean.SPINE, spineSize, 1, weapon));
            }
            char bayFactor = v.charAt(1);
            if ((bayFactor != 'x') && (bayFactor != '0'))
            {
                int o = chunk.indexOf(weapon+"=");
                if (o >= 0)
                {
                    String firstLine = getLineAt(chunk, o);
                    o = chunk.indexOf('\n', o);
                    String secondLine = getLineAt(chunk, o + 1);
                    o = firstLine.indexOf(weapon+"=");
                    o += weapon.length() + 1;
                    secondLine = secondLine.substring(o).trim();
                    if (secondLine.length() > 3)
                        secondLine = secondLine.substring(0, 3).trim();
                    int batteries = IntegerUtils.parseInt(secondLine);
                    int baySize = calcBaySize(bayFactor, tl, weapon);
                    items.add(new PlanItem(ShipSquareBean.BAY, baySize*13.5, batteries, weapon));
                }
            }
            char turretFactor = v.charAt(2);
            if ((turretFactor != 'x') && (turretFactor != '0'))
            {
                int numItems = calcNumTurrets(turretFactor, tl, weapon);
                int numTurrets;
                if (weapon.equals(PARTICLE_ACCELERATOR) || weapon.equals(FUSION_GUN) || weapon.equals(PLASMA_GUN))
                    numTurrets = numItems/2;
                else
                    numTurrets = numItems/3;
                items.add(new PlanItem(ShipSquareBean.TURRET, 13.5, numTurrets, weapon));
            }
        }
    }
    
    private static final int[] SPINE_VOLUME_MESON = {
            65000, // A
            110000,
            25000,
            65000,
            15000, // E
            25000,
            15000,
            25000, // H
            0,     // I
            15000, // J
            110000,
            65000,
            55000,
            25000,  // N
            0,      // O
            110000, // P
            95000,
            65000,
            110000,
            95000,
            110000, // U
            95000,
            65000,
            110000,
            95000,
            65000, // Z
    };
    
    private static final int[] SPINE_VOLUME_PA = {            
            75000, // A
            65000,
            60000,
            55000,
            45000, // E
            40000,
            35000,
            35000, // H
            0,     // I
            60000, // J
            60000, 
            55000,
            45000,
            40000, // N
            0,     // O
            35000, // P
            60000,
            55000,
            45000,
            40000,
            35000, // U
            25000,
            20000,
            15000,
            0,     // X
            15000,
            65000, // Z
    };
    
    private static int calcSpineSize(char factor, int tl, String weapon)
    {
        switch (weapon)
        {
            case MESON_GUN:
                return SPINE_VOLUME_MESON[factor - 'A'];                
            case PARTICLE_ACCELERATOR:
                return SPINE_VOLUME_PA[factor - 'A'];                
        }
        throw new IllegalArgumentException("Unhandled spinal mount "+weapon+" factor "+factor);
    }
    
    private static int[] PA_BAY_FAC_TL = {  // starting at 3
            50, 50, 50, 16, 18, 20, 100, 100, 100, 100
    };
    private static int[] REP_BAY_FAC_TL = { // starting at 2
            100, 50, 100, 50, 100, 16, 100, 17, 18, 19, 20, 21
    };
    
    private static int calcBaySize(char factor, int tl, String weapon)
    {
        switch (weapon)
        {
            case MISSILE:
                switch (factor)
                {
                    case '7':
                        if (tl < 10)
                            return 100;
                        else
                            return 50;
                    case '8':
                        if (tl < 12)
                            return 100;
                        else
                            return 50;
                    case '9':
                        if (tl < 14)
                            return 100;
                        else
                            return 50;
                    case 'A':
                        if (tl < 16)
                            return 100;
                        else
                            return 50;
                    case 'B':
                        if (tl < 18)
                            return 100;
                        else
                            return 50;
                    case 'C':
                        if (tl < 20)
                            return 100;
                        else
                            return 50;
                    case 'D':
                        return 100;
                }
                break;
            case MESON_GUN:
                switch (factor)
                {
                    case '3':
                        if (tl < 15)
                            return 100;
                        else
                            return 50;
                    case '5':
                        if (tl < 16)
                            return 100;
                        else
                            return 50;
                    case '9':
                        if (tl < 17)
                            return 100;
                        else
                            return 50;
                    case 'B':
                        if (tl < 18)
                            return 100;
                        else
                            return 50;
                    case 'D':
                        if (tl < 19)
                            return 100;
                        else
                            return 50;
                    case 'F':
                        if (tl < 20)
                            return 100;
                        else
                            return 50;
                    case 'H':
                        if (tl < 21)
                            return 100;
                        else
                            return 50;
                }
                break;
            case PARTICLE_ACCELERATOR:
                return calcBayRangeSize(tl, PA_BAY_FAC_TL, factor, '3');
            case REPULSOR:
                return calcBayRangeSize(tl, REP_BAY_FAC_TL, factor, '2');
            case FUSION_GUN:
                return 100;
        }
        throw new IllegalArgumentException("Can't handle a "+weapon+" bay of factor "+factor);
    }
    
    private static int calcBayRangeSize(int tl, int[] range, char factor, char base)
    {
        int idx = factorToOff(factor) - factorToOff(base);
        int tlTest = range[idx];
        if ((tlTest == 50) || (tlTest == 100))
            return tlTest;
        else if (tl < tlTest)
            return 100;
        else
            return 50;
    }
    
    private static int factorToOff(char fac)
    {
        if (fac >= 'A')
            return fac - 'A' + 10;
        else
            return fac - '0';
    }

    private static final int[] MISSILE_NUM = { 1, 3, 6, 12, 18, 30 };
    private static final int[] BEAM_LASER_NUM = { 1, 2, 3, 6, 10, 15, 21, 30 };
    private static final int[] PULSE_LASER_NUM = { 1, 3, 6, 10, 21, 30 };
    private static final int[] PARTICLE_ACCELERATOR_NUM = { 1, 2, 4, 6, 8, 10 };
    private static final int[] PLASMA_GUN_NUM = { 1, 4, 10, 16, 20 };
    private static final int[] FUSION_GUN_NUM = { 1, 4, 10, 16, 20 };
    private static final int[] SANDCASTER_NUM = { 1, 3, 6, 8, 10, 20, 30 };
    
    private static int calcNumTurrets(char turretFactor, int TL, String weapon)
    {
        int o = factorToOff(turretFactor);
        switch (weapon)
        {
            case MISSILE:
                if (TL < 13)
                    return MISSILE_NUM[o - 1];
                else if (TL < 21)
                    return MISSILE_NUM[o - 2];
                else 
                    return MISSILE_NUM[o - 3];
            case BEAM_LASER:
                if (TL < 13)
                    return BEAM_LASER_NUM[o - 1];
                else if (TL < 16)
                    return BEAM_LASER_NUM[o - 2];
                else 
                    return BEAM_LASER_NUM[o - 3];
            case PULSE_LASER:
                if (TL < 13)
                    return PULSE_LASER_NUM[o - 1];
                else if (TL < 16)
                    return PULSE_LASER_NUM[o - 2];
                else 
                    return PULSE_LASER_NUM[o - 3];
            case PARTICLE_ACCELERATOR:
                if (TL < 15)
                    return PARTICLE_ACCELERATOR_NUM[o - 1];
                else if (TL < 16)
                    return PARTICLE_ACCELERATOR_NUM[o - 2];
                else if (TL < 18)
                    return PARTICLE_ACCELERATOR_NUM[o - 3];
                else 
                    return PARTICLE_ACCELERATOR_NUM[o - 4];
            case PLASMA_GUN:
                if (TL < 11)
                    return PLASMA_GUN_NUM[o - 1];
                else if (TL < 12)
                    return PLASMA_GUN_NUM[o - 2];
                else if (TL < 16)
                    return PLASMA_GUN_NUM[o - 3];
                else 
                    return PLASMA_GUN_NUM[o - 4];
            case FUSION_GUN:
                if (TL < 14)
                    return FUSION_GUN_NUM[o - 4];
                else if (TL < 17)
                    return FUSION_GUN_NUM[o - 5];
                else 
                    return FUSION_GUN_NUM[o - 6];
            case SAND_CASTER:
                if (TL < 8)
                    return SANDCASTER_NUM[o - 1];
                else if (TL < 10)
                    return SANDCASTER_NUM[o - 2];
                else if (TL < 16)
                    return SANDCASTER_NUM[o - 3];
                else 
                    return SANDCASTER_NUM[o - 4];
        }
        throw new IllegalArgumentException("Unsupported turret: "+weapon);
    }
    
    private static void scanCargo(List<PlanItem> items, int volume,
            Map<String,String> text)
    {
        String v = getValue(OTHER, "Cargo", text);
        if (v == null)
            return;
        double mult = 1.0;
        if (v.endsWith("kl"))
            v = v.substring(0, v.length() - 2);
        else if (v.endsWith("t"))
        {
            v = v.substring(0, v.length() - 1);
            mult = 13.5;
        }
        double vol = DoubleUtils.parseDouble(v)*mult;
        if (vol <= 0)
            return;
        items.add(new PlanItem(ShipSquareBean.CARGO, vol));
    }
    
    private static void scanFuel(List<PlanItem> items, int volume,
            Map<String,String> text)
    {
        String v = getValue(OTHER, "Fuel", text);
        if (v == null)
            return;
        double mult = 1.0;
        if (v.endsWith("kl"))
            v = v.substring(0, v.length() - 2);
        else if (v.endsWith("t"))
        {
            v = v.substring(0, v.length() - 1);
            mult = 13.5;
        }
        v = v.replace(",", "");
        double vol = DoubleUtils.parseDouble(v)*mult;
        if (vol <= 0)
            return;
        items.add(new PlanItem(ShipSquareBean.FUEL, vol));
    }
    
    private static void scanSubcraft(List<PlanItem> items, int volume,
            Map<String,String> text)
    {
        String subcraft = text.get(OTHER);
        int o = subcraft.indexOf("Subcraft=");
        if (o < 0)
            return;
        subcraft = subcraft.substring(o + 9).trim();
        o = subcraft.indexOf('=');
        if (o > 0)
        {
            subcraft = subcraft.substring(0, o).trim();
            o = subcraft.lastIndexOf(' ');
            if (o >= 0)
                subcraft = subcraft.substring(0, o).trim();
        }
        for (StringTokenizer st = new StringTokenizer(subcraft, ","); st.hasMoreElements(); )
            scanSubcraft(items, volume, st.nextToken());
    }
    
    private static void scanSubcraft(List<PlanItem> items, int volume, String v)
    {
        if (v == null)
            return;
        int o = v.indexOf('*');
        if (o < 0)
            o = v.indexOf(' ');
        int num = 1;
        if (o > 0)
        {
            num = IntegerUtils.parseInt(v.substring(0, o));
            if (num <= 0)
                throw new IllegalArgumentException("Unknown subcraft '"+v+"'");
            v = v.substring(o + 1).trim();
        }
        String notes = v;
        double mult = 1.0;
        if (v.endsWith("kl"))
            v = v.substring(0, v.length() - 2);
        else if (v.endsWith("t") || v.endsWith("ton"))
        {
            v = v.substring(0, v.length() - 1);
            mult = 13.5;
        }
        v = v.replace(",", "");
        v = v.trim();
        double vol = DoubleUtils.parseDouble(v)*mult;
        if (vol <= 0)
        {
            if (v.startsWith("Type K Gunboat"))
                vol = 400*13.5;
            else if (v.startsWith("Pinnace"))
                vol = 40*13.5;
            else if (v.startsWith("Horde Ship's") || "Ship's Boat".equals(v))
                vol = 20*13.5;
            else if ("Armed Launch".equals(v) || v.startsWith("Launch") || v.startsWith("Titania class") 
                    || v.startsWith("Kontos class fighter") || v.startsWith("Javelin class"))
                vol = 20*13.5;
            else if (v.startsWith("Stinger class Fighter"))
                vol = 6*13.5;
            else if (v.startsWith("Dependable AirRaf"))
                vol = 4*13.5;
            else
                throw new IllegalArgumentException("Unknown subcraft '"+v+"'");
        }
        items.add(new PlanItem(ShipSquareBean.HANGER, vol, num, notes));
    }
    
    private static void scanStaterooms(List<PlanItem> items, int volume,
            Map<String,String> text)
    {
        String v = getValue(ACCOM, "Staterooms", text);
        if (v == null)
            return;
        int num = IntegerUtils.parseInt(v);
        if (num <= 0)
            return;
        items.add(new PlanItem(ShipSquareBean.STATEROOM, 54.0, num, null));
    }
    
    private static Pattern mPowerScan = Pattern.compile("Power:\\D*(\\d*)/");
    
    private static void scanPower(List<PlanItem> items, int volume,
            Map<String,String> text)
    {
        Matcher m = mPowerScan.matcher(text.get(POWER));
        if (!m.find())
            return;
        String v = m.group(1);
        if (v == null)
            return;
        double damage = DoubleUtils.parseDouble(v);
        if (damage <= 0)
            return;
        double vol = damage*15;
        items.add(new PlanItem(ShipSquareBean.POWER, vol));
    }
    
    private static void scanManeuver(List<PlanItem> items, int volume,
            Map<String,String> text)
    {
        String v = getValue(LOCO, "Maneuver", text);
        if (v == null)
            return;
        double man = DoubleUtils.parseDouble(v);
        if (man <= 0)
            return;
        double pc = man*3 - 1;
        double manVolume = pc*volume/100.0;
        items.add(new PlanItem(ShipSquareBean.MANEUVER, manVolume));
    }
    
    private static void scanJump(List<PlanItem> items, int volume,
            Map<String,String> text)
    {
        String v = getValue(LOCO, "Jump", text);
        if (v == null)
            return;
        double jump = DoubleUtils.parseDouble(v);
        if (jump <= 0)
            return;
        double pc = jump + 1;
        double manVolume = pc*volume/100.0;
        items.add(new PlanItem(ShipSquareBean.JUMP, manVolume));
    }

    private static int getConfiguration(Map<String,String> text)
    {
        String v = getValue(HULL, "Config", text);
        if (v == null)
            return Hull.HULL_CYLINDER; // shrug
        switch (v.charAt(0))
        {
            case '0': return Hull.HULL_OPEN_FRAME;
            case '1': return Hull.HULL_NEEDLE;
            case '2': return Hull.HULL_CONE;
            case '3': return Hull.HULL_CYLINDER;
            case '4': return Hull.HULL_BOX;
            case '5': return Hull.HULL_SPHERE;
            case '6': return Hull.HULL_DOME;
            case '7': return Hull.HULL_IRREGULAR;
            case '8': return Hull.HULL_PLANETOID;
            case '9': return Hull.HULL_BUFFERED_PLANETOID;
        }
        return Hull.HULL_CYLINDER; // shrug
    }
    
    private static int getVolume(Map<String,String> text)
    {
        String v = getValue(HULL, "Disp", text);
        if (v == null)
            return 1350; // shrug
        double mult = 1.0;
        if (v.endsWith("t"))
        {
            v = v.substring(0, v.length() - 1);
            mult = 13.5;
        }
        else if (v.endsWith("kl"))
        {
            v = v.substring(0, v.length() - 2);
            mult = 1;
        }
        else
        {   // assume tons
            mult = 13.5;
        }
        double volume = DoubleUtils.parseDouble(v);
        volume *= mult;
        return (int)volume;
    }
    
    private static String getValue(String section, String ident, Map<String,String> chunks)
    {
        String text = chunks.get(section);
        return getValue(ident, text);
    }
    
    private static String getValue(String ident, String text)
    {
        for (;;)
        {
            int o = text.toLowerCase().indexOf(ident.toLowerCase());
            if (o < 0)
                return null;
            text = text.substring(o + ident.length());
            text = text.trim(); // skip blanks
            if (text.startsWith(":") || text.startsWith("="))
            {
                text = text.substring(1);
                text = text.trim();
            }
            else if (Character.isDigit(text.charAt(0)))
                ; // no delimiter
            else
                continue;
            o = findNearest(text, ", ", "\r", "\n", "(", "<", "/");
            if (o < 0)
                return text;
            text = text.substring(0, o).trim();
            if (text.endsWith(","))
                text = text.substring(0, text.length() - 1);
            return text;
        }
    }
    
    private static int findNearest(String text, String... patterns)
    {
        int nearest = -1;
        for (String pattern : patterns)
        {
            int o = text.indexOf(pattern);
            if (o < 0)
                continue;
            if ((nearest < 0) || (o < nearest))
                nearest = o;
        }
        return nearest;
    }
    
    private static final String CRAFTID = "CraftID";
    private static final String HULL = "Hull";
    private static final String POWER = "Power";
    private static final String LOCO = "Loco";
    private static final String COMM = "Comm";
    private static final String SENSORS = "Sensors";
    private static final String OFF = "Off";
    private static final String DEF = "Def";
    private static final String CONTROL = "Control";
    private static final String ACCOM = "Accom";
    private static final String OTHER = "Other";
    private static final String[] SECTIONS = {
            CRAFTID,
            HULL,
            POWER,
            LOCO,
            COMM,
            SENSORS,
            OFF,
            DEF,
            CONTROL,
            ACCOM,
            OTHER,
    };

    private static Map<String,String> chunkText(String text)
    {
        Map<String,String> chunks = new HashMap<String, String>();
        chunks.put("ALL", text);
        int[] offsets = new int[SECTIONS.length];
        for (int i = 0; i < SECTIONS.length; i++)
        {
            if (i == 0)
                offsets[i] = text.indexOf(SECTIONS[i]);
            else
                offsets[i] = text.indexOf(SECTIONS[i], offsets[i-1]);
            if (offsets[i] < 0)
                throw new IllegalArgumentException("No '"+SECTIONS[i]+"' in description");
        }
        for (int i = 0; i < SECTIONS.length; i++)
        {
            String section;
            if (i + 1 < SECTIONS.length)
                section = text.substring(offsets[i], offsets[i+1]);
            else
                section = text.substring(offsets[i]);
            chunks.put(SECTIONS[i], section);
        }
        return chunks;
    }
}
