package jo.ttg.core.report.logic;

import jo.ttg.beans.URIBean;

public interface ITTGReport
{
    public String getID();
    public String getName();
    public String getName(URIBean bean);
    public boolean isReportable(URIBean bean);
}
