package jo.ttg.core.report.body;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jo.ttg.beans.URIBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.report.logic.ITTGHTMLReport;
import jo.ttg.gen.IGenScheme;

public class TTGBodyDetailReport implements ITTGHTMLReport
{
    @Override
    public String getID()
    {
        return getClass().getName();
    }

    @Override
    public String getName()
    {
        return "World Detail Sheet";
    }

    @Override
    public String getName(URIBean bean)
    {
        BodyWorldBean body = (BodyWorldBean)bean;
        return body.getName()+" Detail Sheet";    }

    @Override
    public boolean isReportable(URIBean bean)
    {
        return (bean instanceof BodyWorldBean);
    }

    @Override
    public Node report(Document doc, IGenScheme scheme, URIBean bean)
    {
        return BodyDetailReport.report(doc, scheme, (BodyWorldBean)bean);
    }

}
