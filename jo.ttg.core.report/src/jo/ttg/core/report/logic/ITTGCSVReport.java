package jo.ttg.core.report.logic;

import java.util.Collection;

import jo.ttg.beans.URIBean;
import jo.ttg.gen.IGenScheme;

public interface ITTGCSVReport extends ITTGReport
{
    public Collection<Collection<?>> report(IGenScheme scheme, URIBean bean);
}
