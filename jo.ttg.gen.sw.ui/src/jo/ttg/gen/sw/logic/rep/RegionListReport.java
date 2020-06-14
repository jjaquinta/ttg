package jo.ttg.gen.sw.logic.rep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jo.ttg.beans.URIBean;
import jo.ttg.core.report.logic.ITTGReport;
import jo.ttg.gen.IGenScheme;
import jo.ttg.gen.sw.data.SWMainWorldBean;
import jo.ttg.gen.sw.data.SelectedRegionBean;
import jo.util.utils.ArrayUtils;
import jo.util.utils.xml.XMLEditUtils;

public class RegionListReport implements ITTGReport
{
    public static final int HTML = 0;
    public static final int CSV = 1;
    private int mType;
    
    public RegionListReport(int type)
    {
        mType = type;
    }
    
    @Override
    public String getID()
    {
        return getClass().getSimpleName()+"_"+mType;
    }

    @Override
    public String getName()
    {
        if (mType == HTML)
            return "Region List (HTML)";
        else if (mType == CSV)
            return "Region List (CSV)";
        else
            throw new IllegalArgumentException();
    }

    @Override
    public String getName(URIBean bean)
    {
        return getName();
    }

    @Override
    public boolean isReportable(URIBean bean)
    {
        return bean instanceof SelectedRegionBean;
    }

    public Node report(Document doc, IGenScheme scheme, URIBean bean)
    {
        Node table = doc.createElement("table");
        XMLEditUtils.addAttribute(table, "id", bean.getURI() + "$report");
        List<SWMainWorldBean> innerWorldList = ((SelectedRegionBean)bean).getInnerWorldList();
        List<SWMainWorldBean[]> shortLinks = ((SelectedRegionBean)bean).getShortLinks();
        List<SWMainWorldBean[]> longLinks = ((SelectedRegionBean)bean).getLongLinks();
        addHeaderRow(table, getHeaderData());
        for (SWMainWorldBean mw : innerWorldList)
        {
            List<?> line = getRowData(mw, shortLinks, longLinks);
            addTableRow(table, line);
        }
        return table;
    }

    private List<?> getRowData(SWMainWorldBean mw,
            List<SWMainWorldBean[]> shortLinks,
            List<SWMainWorldBean[]> longLinks)
    {
        int shorts = count(mw, shortLinks);
        int longs = count(mw, longLinks);
        List<String> line = new ArrayList<String>();
        line.add(String.valueOf(mw.getOrds().getX()));
        line.add(String.valueOf(mw.getOrds().getY()));
        line.add(String.valueOf(mw.getOrds().getZ()));
        line.add(mw.getName());
        line.add(String.valueOf(mw.getPopulatedStats().getUPP().getPort().getValueDigit()));
        line.add(mw.getPopulatedStats().getUPP().getPort().getValueName());
        line.add(String.valueOf(mw.getPopulatedStats().getUPP().getSize().getValue()));
        line.add(mw.getPopulatedStats().getUPP().getSize().getValueName());
        line.add(String.valueOf(mw.getPopulatedStats().getUPP().getAtmos().getValue()));
        line.add(mw.getPopulatedStats().getUPP().getAtmos().getValueName());
        line.add(String.valueOf(mw.getPopulatedStats().getUPP().getHydro().getValue()));
        line.add(mw.getPopulatedStats().getUPP().getHydro().getValueName());
        line.add(String.valueOf(mw.getPopulatedStats().getUPP().getPop().getValue()));
        line.add(mw.getPopulatedStats().getUPP().getPop().getValueName());
        line.add(String.valueOf(mw.getPopulatedStats().getUPP().getGov().getValue()));
        line.add(mw.getPopulatedStats().getUPP().getGov().getValueName());
        line.add(String.valueOf(mw.getPopulatedStats().getUPP().getLaw().getValue()));
        line.add(mw.getPopulatedStats().getUPP().getLaw().getValueName());
        line.add(String.valueOf(mw.getPopulatedStats().getUPP().getTech().getValue()));
        line.add(mw.getPopulatedStats().getUPP().getTech().getValueName());
        line.add(mw.getPopulatedStats().getAllegiance());
        line.add(mw.getPopulatedStats().getBasesDescLong());
        line.add(String.valueOf(shorts));
        line.add(String.valueOf(longs));
        return line;
    }

    private List<?> getHeaderData()
    {
        return ArrayUtils.toList(new String[] {
                "X", "Y", "Z", "Name",
                "Port", "Port", "Size", "Size", "Atmos", "Atmos", "Hydro", "Hydro", "Pop", "Pop", "Gov", "Gov", "Law", "Law", "Tech","Tech",
                "All", "Bases", "Short Links", "Long Links"
        });
    }

    public Collection<Collection<?>> report(IGenScheme scheme, URIBean bean)
    {
        Collection<Collection<?>> grid = new ArrayList<>();
        List<SWMainWorldBean> innerWorldList = ((SelectedRegionBean)bean).getInnerWorldList();
        List<SWMainWorldBean[]> shortLinks = ((SelectedRegionBean)bean).getShortLinks();
        List<SWMainWorldBean[]> longLinks = ((SelectedRegionBean)bean).getLongLinks();
        grid.add(getHeaderData());
        for (SWMainWorldBean mw : innerWorldList)
        {
            List<?> line = getRowData(mw, shortLinks, longLinks);
            grid.add(line);
        }
        return grid;
    }
    
    private void addHeaderRow(Node table, List<?> cells)
    {
        addRow(table, "th", cells);
    }
    
    private void addTableRow(Node table, List<?> cells)
    {
        addRow(table, "td", cells);
    }
    
    private void addRow(Node table, String cellTag, List<?> cells)
    {
        Node tr = XMLEditUtils.addElement(table, "tr");
        for (Object cell : cells)
            XMLEditUtils.addTextTag(tr, cellTag, cell.toString());
    }

    private static int count(SWMainWorldBean mw, List<SWMainWorldBean[]> links)
    {
        int count = 0;
        for (SWMainWorldBean[] link : links)
            if ((link[0] == mw) || (link[1] == mw))
                count++;
        return count;
    }

}
