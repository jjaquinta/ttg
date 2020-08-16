package jo.ttg.lbb.logic.ship2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jo.ttg.lbb.data.ship2.Scenario;
import jo.util.utils.io.ResourceUtils;
import jo.util.utils.xml.XMLUtils;

public class ScenarioLogic
{
	private static Map<String, Scenario> SCENARIOS = new HashMap<String, Scenario>();
	private static List<Scenario> SORTED_SCENARIOS = null;
	
	public static void loadBuiltIn()
	{
		loadFromStream(ResourceUtils.loadSystemResourceStream("jo/ttg/lbb/logic/ship2/scenarios.xml", ScenarioLogic.class));
	}
	
	public static void loadFromStream(InputStream is)
	{
		Document doc = XMLUtils.readStream(is);
		for (Node s : XMLUtils.findNodes(doc, "scenarios/scenario"))
			loadFromXML(s); 
	}
	
	public static Scenario loadFromXML(Node s)
	{
		Scenario scenario = XMLIOLogic.readScenario(s);
		addScenario(scenario);
		return scenario;
	}
	
	public static void addScenario(Scenario scenario)
	{
		SCENARIOS.put(scenario.getID(), scenario);
		SORTED_SCENARIOS = null;
	}
	
	public static Scenario getScenario(String id)
	{
		return SCENARIOS.get(id);
	}
	
	public static synchronized List<Scenario> getScenarios()
	{
		if (SORTED_SCENARIOS == null)
		{
			SORTED_SCENARIOS = new ArrayList<Scenario>();
			SORTED_SCENARIOS.addAll(SCENARIOS.values());
			Collections.sort(SORTED_SCENARIOS, new Comparator<Scenario>() {
				@Override
				public int compare(Scenario o1, Scenario o2)
				{
					return o1.getName().compareTo(o2.getName());
				}
			});
		}
		return SORTED_SCENARIOS;
	}
}
