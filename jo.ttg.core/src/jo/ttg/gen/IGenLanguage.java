package jo.ttg.gen;

import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;

public interface IGenLanguage
{
    public String generatePlaceName(OrdBean ul, OrdBean lr, String alliegence, RandBean r);
    public String generatePersonalName(OrdBean ul, OrdBean lr, String alliegence, RandBean r);
    public List<String> generateWords(OrdBean ul, OrdBean lr, String alliegence, RandBean r, int num);
}