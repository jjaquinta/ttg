/*
 * Created on Sep 26, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenSchemeLogic
{
    private static final List<SchemeChoice> mSchemeChoices = new ArrayList<>();
    private static final Map<String,IGenScheme> mSchemes = new HashMap<>();
    
    public static List<SchemeChoice> getSchemeChoices()
    {
        return mSchemeChoices;
    }
    
    public static void addScheme(String id, String name, String desc, IGenScheme scheme)
    {
        SchemeChoice choice = new SchemeChoice();
        choice.setName(name);
        choice.setDescription(desc);
        choice.setId(id);
        mSchemeChoices.add(choice);
        mSchemes.put(id, scheme);
    }
    
    public static IGenScheme getScheme(String id)
    {   
        return mSchemes.get(id);
    }
}
