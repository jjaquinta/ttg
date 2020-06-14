package jo.ttg.core.report.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jo.ttg.beans.URIBean;
import jo.ttg.core.report.body.TTGBodyDetailReport;
import jo.ttg.core.report.sub.TTGPopulationReport;
import jo.ttg.core.report.surf.TTGSurfaceReport;
import jo.ttg.core.report.surf.TTGTacticalReport;
import jo.ttg.core.report.sys.TTGDistanceReport;
import jo.ttg.core.report.sys.TTGSystemReport;
import jo.ttg.core.report.sys.TTGTravelTimeReport;
import jo.ttg.logic.gen.SchemeLogic;
import jo.util.logic.CSVLogic;
import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLUtils;

public class TTGReportLogic
{
    private static List<ITTGReport> mReporters = new ArrayList<>();
    static
    {
        addReporter(new TTGSystemReport());
        addReporter(new TTGSurfaceReport());
        addReporter(new TTGTacticalReport());
        addReporter(new TTGBodyDetailReport());
        addReporter(new TTGTravelTimeReport());
        addReporter(new TTGDistanceReport());
        addReporter(new TTGPopulationReport());
    }
    
    public static void addReporter(ITTGReport report)
    {
        mReporters.add(report);
    }

    public static List<ITTGReport> getReporters(URIBean bean)
    {
        List<ITTGReport> reporters = new ArrayList<ITTGReport>();
        for (ITTGReport r : mReporters)
            if (r.isReportable(bean))
                reporters.add(r);
        return reporters;
    }

    public static ITTGReport getReporter(URIBean bean, String id)
    {
        for (ITTGReport reporter : TTGReportLogic.getReporters(bean))
            if (reporter.getID().equals(id))
                return reporter;
        return null;
    }
    
    public static Document reportHTML(ITTGHTMLReport reporter, URIBean subject)
    {
        Document doc = XMLUtils.newDocument();
        Node report = reporter.report(doc, SchemeLogic.getDefaultScheme(), subject);
        Node html = XMLEditUtils.addElement(doc, "html");
        Node head = XMLEditUtils.addElement(html, "head");
        XMLEditUtils.addTextTag(head, "title", reporter.getName(subject));
        Node body = XMLEditUtils.addElement(html, "body");
        XMLEditUtils.addTextTag(body, "h1", reporter.getName(subject));
        body.appendChild(report);
        return doc;
    }

    public static File reportToFile(ITTGReport reporter, URIBean subject)
    {
        try
        {
            File outfile = null;
            if (reporter instanceof ITTGHTMLReport)
            {
                Document doc = TTGReportLogic.reportHTML((ITTGHTMLReport)reporter, subject);
                outfile = File.createTempFile(reporter.getID(), ".html");
                XMLUtils.writeFile(doc, outfile);
            }
            else if (reporter instanceof ITTGCSVReport)
            {
                Collection<Collection<?>> grid = ((ITTGCSVReport)reporter).report(SchemeLogic.getDefaultScheme(), subject);
                outfile = File.createTempFile(reporter.getID(), ".csv");
                CSVLogic.toCSV(outfile, grid);
            }
            return outfile;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
