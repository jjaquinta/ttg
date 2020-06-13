package jo.ttg.ship.logic;

public class ShipFormatLogic
{
    public static final String[][] mPrimaryType =
    {
            { "A", "Merchant" },      
            { "B", "Battle" },      
            { "C", "Carrier" },      
            { "c", "Cruiser" },      
            { "D", "Destroyer" },      
            { "E", "Escort" },      
            { "F", "Frigate" },      
            { "f", "Fighter" },      
            { "G", "Gig" },      
            { "H", "Hunter" },      
            { "I", "Intruder" },      
            { "K", "Pinnace" },      
            { "L", "Corvette" },      
            { "l", "Lab" },      
            { "M", "Merchant" },      
            { "N", "Ferry" },      
            { "P", "Planetoid" },      
            { "Q", "Auxiliary" },      
            { "R", "Liner" },      
            { "S", "Scout" },      
            { "s", "Station" },      
            { "T", "Tanker" },      
            { "t", "tender" },      
            { "U", "Tug" },      
            { "V", "Carrier" },      
            { "W", "Barge" },      
            { "X", "Express" },      
            { "Y", "Yacht" },      
            { "Z", "Special" },      
    };
    public static final String[][] mQualifierType =
    {
            { "A", "Armored" },      
            { "B", "Battle" },      
            { "b", "Boat" },      
            { "C", "Cruiser" },      
            { "c", "Close" },      
            { "D", "Destroyer" },      
            { "E", "Escort" },      
            { "F", "Fast" },      
            { "f", "Fleet" },      
            { "G", "Gunned" },      
            { "H", "Heavy" },      
            { "I", "Imperial" },      
            { "K", "Courier" },      
            { "L", "Leader" },      
            { "l", "Light" },      
            { "M", "Missile" },      
            { "N", "Nonstandard" },      
            { "P", "Provincial" },      
            { "Q", "Decoy" },      
            { "R", "Raider" },      
            { "S", "Strike" },      
            { "T", "Troop" },      
            { "t", "Transport" },      
            { "U", "Unpowered" },      
            { "V", "Vehicle" },      
            { "W", "Slow" },      
            { "X", "Alternate" },      
            { "Y", "Shuttle" },      
            { "y", "Cutter" },      
            { "Z", "Experimental" },      
    };
    
    private static int findIndex(String pattern, String[][] table, int searchCol)
    {
        for (int i = 0; i < table.length; i++)
            if (table[i][searchCol].equals(pattern))
                return i;
        return -1;
    }
    
    private static String find(String pattern, String[][] table, int searchCol, int retCol)
    {
        int row = findIndex(pattern, table, searchCol);
        if (row < 0)
            return "";
        else
            return table[row][retCol];
    }
    
    public static String findPrimaryNameFromCode(String code)
    {
        return find(code, mPrimaryType, 0, 1);
    }
    
    public static String findPrimaryCodeFromName(String name)
    {
        return find(name, mPrimaryType, 1, 0);
    }
    
    public static int findPrimaryIndex(String code)
    {
        return findIndex(code.substring(0, 1), mPrimaryType, 0);
    }
    
    public static String findQualifierNameFromCode(String code)
    {
        return find(code, mQualifierType, 0, 1);
    }
    
    public static String findQualifierCodeFromName(String name)
    {
        return find(name, mQualifierType, 1, 0);
    }
    
    public static int findQualifierIndex(String code)
    {
        if (code.length() < 2)
            return -1;
        return findIndex(code.substring(1, 2), mQualifierType, 0);
    }
    
    public static String findNameFromCode(String code)
    {
        String ret = findPrimaryNameFromCode(code.substring(0, 1));
        if (code.length() > 1)
            ret += " " + findQualifierNameFromCode(code.substring(1, 2));
        return ret;
    }
    
    public static String findCodeFromName(String name)
    {
        int o = name.indexOf(" ");
        if (o < 0)
            return findPrimaryCodeFromName(name);
        else
            return findPrimaryCodeFromName(name.substring(0, o))
            	+ findQualifierCodeFromName(name.substring(o+1));
    }
    
    public static String[] getPrimaryNames()
    {
        String[] ret = new String[mPrimaryType.length + 1];
        ret[0] = "";
        for (int i = 0; i < mPrimaryType.length; i++)
            ret[i+1] = mPrimaryType[i][1];
        return ret;
    }
    
    public static String[] getQualifierNames()
    {
        String[] ret = new String[mQualifierType.length + 1];
        ret[0] = "";
        for (int i = 0; i < mQualifierType.length; i++)
            ret[i+1] = mQualifierType[i][1];
        return ret;
    }
}
