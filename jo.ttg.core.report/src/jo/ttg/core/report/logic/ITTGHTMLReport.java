package jo.ttg.core.report.logic;

import jo.ttg.beans.URIBean;
import jo.ttg.gen.IGenScheme;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface ITTGHTMLReport extends ITTGReport
{
    public Node report(Document doc, IGenScheme scheme, URIBean bean);
}
