package jo.ttg.core.report.sys;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jo.ttg.beans.URIBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.core.report.logic.ITTGHTMLReport;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.sys.SystemLogic;

public class TTGSystemReport implements ITTGHTMLReport
{
    @Override
    public String getID()
    {
        return getClass().getName();
    }

    @Override
    public String getName()
    {
        return "System Orbits Report";
    }

    @Override
    public String getName(URIBean bean)
    {
        if (bean instanceof SystemBean)
            return ((SystemBean)bean).getName()+" Orbits Report";
        else if (bean instanceof MainWorldBean)
            return ((MainWorldBean)bean).getName()+" Orbits Report";
        else
            throw new IllegalArgumentException("Unhandled bean '"+bean.getClass().getCanonicalName()+"'");
    }

    @Override
    public boolean isReportable(URIBean bean)
    {
        return (bean instanceof SystemBean) || (bean instanceof MainWorldBean);
    }

    @Override
    public Node report(Document doc, IGenScheme scheme, URIBean bean)
    {
        SystemBean sys;
        if (bean instanceof SystemBean)
            sys = (SystemBean)bean;
        else if (bean instanceof MainWorldBean)
            sys = SystemLogic.getFromOrds(scheme, ((MainWorldBean)bean).getOrds());
        else
            throw new IllegalArgumentException("Unhandled bean '"+bean.getClass().getCanonicalName()+"'");
        return SystemReport.report(doc, scheme, sys);
    }

}
