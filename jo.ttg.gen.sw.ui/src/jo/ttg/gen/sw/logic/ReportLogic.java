package jo.ttg.gen.sw.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jo.ttg.gen.sw.data.SWMainWorldBean;
import jo.ttg.gen.sw.data.SelectedRegionBean;
import jo.util.logic.CSVLogic;

public class ReportLogic
{
    public static File mwReport()
    {
        SelectedRegionBean region = RuntimeLogic.getInstance().getRegion();
        List<SWMainWorldBean> innerWorldList = region.getInnerWorldList();
        List<SWMainWorldBean[]> shortLinks = region.getShortLinks();
        List<SWMainWorldBean[]> longLinks = region.getLongLinks();
        try
        {
            File tmp = File.createTempFile("mwrep", ".csv");
            BufferedWriter wtr = new BufferedWriter(new FileWriter(tmp));
            wtr.write(CSVLogic.toCSVHeader(new String[] {
                    "X", "Y", "Z", "Name",
                    "Port", "Port", "Size", "Size", "Atmos", "Atmos", "Hydro", "Hydro", "Pop", "Pop", "Gov", "Gov", "Law", "Law", "Tech","Tech",
                    "All", "Bases", "Short Links", "Long Links"
            }));
            wtr.newLine();
            List<String> line = new ArrayList<String>();
            for (SWMainWorldBean mw : innerWorldList)
            {
                int shorts = count(mw, shortLinks);
                int longs = count(mw, longLinks);
                line.clear();
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
                wtr.write(CSVLogic.toCSVLine(line));
                wtr.newLine();
            }
            wtr.close();
            return tmp;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
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
