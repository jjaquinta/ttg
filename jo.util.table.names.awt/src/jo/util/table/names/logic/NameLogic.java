package jo.util.table.names.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jo.util.table.beans.TableGroup;
import jo.util.table.logic.RollLogic;
import jo.util.table.logic.TableGroupLogic;
import jo.util.utils.xml.EntityUtils;

public class NameLogic
{
    public static final String CATEGORY_ANY = "Any";
    public static final String CATEGORY_ICELAND = "Icelandic";
    public static final String[] CATEGORIES = {
        CATEGORY_ANY,
        "Albanian", "Arabic", "Armenian", "Austrian", "Belgan", "Chinese", "Czech", "Danish", "Dutch",
        "English", "Finnish", "French", "German", "Greek", "Hindi", "Hungarian", CATEGORY_ICELAND, "Iranian", "Irish",
        "Italian", "Japanese", "Jewish", "Nigerian", "Norwegian", "Pakistani", "Polish", "Portuguese", "Russian",
        "Spanish", "Swedish", "Swiss",
    };
    public static final String FILE_ICELAND = "iceland";
    public static final String[] CATEGORY_FILES = {
        null, "alban", "arabic", "armenia", "austria", "belgium", "chinese", "czech", "danish", "dutch",
        "en", "finnish", "fr", "ge", "gr", "hindi", "hungary", FILE_ICELAND, "iran", "irish",
        "italian", "japanese", "jewish", "niger", "norway", "pakistan", "pole", "portug", "russian",
        "spanish", "swedish", "swiss",
    };
    public static final String[] LANGUAGE_CODES = {
        null, "sq", "ar", "hy", "de", "fr", "zh", "cs", "da", "nl",
        "en_GB", "fi", "fr", "ge", "gr", "hi", "hu", "ic", "ar", "ga",
        "it", "ja", "he", "en", "no", "ar", "pl", "pt", "ru",
        "es", "wv", "de",
    };
    public static final String MODE_FEMALE_FIRST = "Female, first";
    public static final String MODE_MALE_FIRST = "Male, first";
    public static final String MODE_MIXED_FIRST = "Mixed, first";
    public static final String MODE_FEMALE_FULL = "Female, full";
    public static final String MODE_MALE_FULL = "Male, full";
    public static final String MODE_MIXED_FULL = "Mixed, full";
    public static final String MODE_LAST_ONLY = "Last name only";
    public static final String[] MODES = {
        MODE_FEMALE_FIRST,
        MODE_MALE_FIRST,
        MODE_MIXED_FIRST,
        MODE_FEMALE_FULL,
        MODE_MALE_FULL,
        MODE_MIXED_FULL,
        MODE_LAST_ONLY,
    };
    
    private static TableGroup mNameTables = null;
    private static Random mRND = new Random();

    private static void initialize()
    {
        if (mNameTables != null)
            return;
        try
        {
            mNameTables = TableGroupLogic.create();
            //TableGroupLogic.addTablepath(mNameTables, "resource://jo.util.table.names.resources");
            for (int i = 0; i < CATEGORY_FILES.length; i++)
                if (CATEGORY_FILES[i] != null)
                {
                    TableGroupLogic.addTablepath(mNameTables, "resource://jo/util/table/names/resources/"+CATEGORY_FILES[i]+"-f.t");
                    TableGroupLogic.addTablepath(mNameTables, "resource://jo/util/table/names/resources/"+CATEGORY_FILES[i]+"-m.t");
                    TableGroupLogic.addTablepath(mNameTables, "resource://jo/util/table/names/resources/"+CATEGORY_FILES[i]+"-s.t");
                }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private static String findNationalityFile(String nationality)
    {
        if (nationality == null)
            return null;
        for (int i = 0; i < CATEGORIES.length; i++)
            if (CATEGORIES[i].equalsIgnoreCase(nationality))
                return CATEGORY_FILES[i];
        for (int i = 0; i < LANGUAGE_CODES.length; i++)
            if (LANGUAGE_CODES[i].equalsIgnoreCase(nationality))
                return CATEGORY_FILES[i];
        return null;
    }
    
    public static String findLanguageCode(String nationality)
    {
        if (nationality == null)
            return null;
        for (int i = 0; i < CATEGORIES.length; i++)
            if (CATEGORIES[i].equalsIgnoreCase(nationality))
                return LANGUAGE_CODES[i];
        return nationality;
    }
    
    public static String[] generatePersonalNames(String nationality, String mode, int number)
    {
        initialize();
        String prefix = null;
        String table1 = null;
        String table1alt = null;
        String table2 = null;
        prefix = findNationalityFile(nationality);
        if (MODE_FEMALE_FIRST.equals(mode))
        {
            table1 = "-f.t";
        }
        else if (MODE_MALE_FIRST.equals(mode))
        {
            table1 = "-m.t";
        }
        else if (MODE_MIXED_FIRST.equals(mode))
        {
            table1 = "-m.t";
            table1alt = "-f.t";
        }
        else if (MODE_FEMALE_FULL.equals(mode))
        {
            table1 = "-f.t";
            table2 = "-s.t";
        }
        else if (MODE_MALE_FULL.equals(mode))
        {
            table1 = "-m.t";
            table2 = "-s.t";
        }
        else if (MODE_MIXED_FULL.equals(mode))
        {
            table1 = "-m.t";
            table1alt = "-f.t";
            table2 = "-s.t";
        }
        else if (MODE_LAST_ONLY.equals(mode))
        {
            table1 = "-s.t";
        }
        else
            throw new IllegalArgumentException("Unrecognized mode="+mode);
        List<String> names = new ArrayList<String>();
        for (int i = 0; i < number; i++)
        {
            String thisPrefix = prefix;
            String thisTable1 = table1;
            if (thisPrefix == null)
                thisPrefix = CATEGORY_FILES[mRND.nextInt(CATEGORY_FILES.length - 1) + 1];
            if (table1alt != null)
                if (mRND.nextBoolean())
                    thisTable1 = table1alt;
            StringBuffer name = new StringBuffer();
            if (thisTable1 != null)
                name.append(RollLogic.roll(mNameTables, thisPrefix + thisTable1, mRND));
            if (table2 != null)
            {
                if (name.length() > 0)
                    name.append(" ");
                if (FILE_ICELAND.equals(thisPrefix))
                {
                    name.append(RollLogic.roll(mNameTables, FILE_ICELAND+"-m.t", mRND));
                    if (thisTable1 != null)
                        if (thisTable1.endsWith("-f.t"))
                            name.append("dottir");
                        else
                            name.append("son");
                }
                else
                    name.append(RollLogic.roll(mNameTables, thisPrefix + table2, mRND));
            }
            names.add(EntityUtils.removeEntities(name.toString(), true));
        }
        return names.toArray(new String[0]);
    }
    
    public static void main(String[] argv)
    {
        // UNIT TEST
        for (int i = 0; i < CATEGORIES.length; i++)
        {
            String cat = CATEGORIES[i];
            for (int j = 0; j < MODES.length; j++)
            {
                String mode = MODES[j];
                System.out.println(cat + ", " + mode + ":");
                String[] names = generatePersonalNames(cat, mode, 3);
                for (String name : names)
                    System.out.println("  "+name);
            }
        }
    }
}
