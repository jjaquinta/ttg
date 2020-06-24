/*
 * Created on Jan 9, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.logic.adv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.beans.trade.PassengersBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.gen.IGenPassengersEx;
import jo.ttg.gen.IGenScheme;
import ttg.beans.adv.BodySpecialAdvBean;
import ttg.logic.adv.gen.AdvGenCargo;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ForSaleReportLogic
{
    public static String genIntraSystemFreightReport(IGenScheme scheme, SystemBean sys, DateBean date)
    {
        BodySpecialAdvBean[] locations = findLocations(sys);
        String[][] freightTable = new String[locations.length][locations.length];
        for (int i = 0; i < locations.length; i++)
            for (int j = 0; j < locations.length; j++)
                if (i == j)
                    freightTable[i][j] = "-";
                else
                {
                    int amnt = calc28DayFreightAverage(scheme, locations[i], locations[j], date);
                    if (amnt == 0)
                        freightTable[i][j] = "-";
                    else
                        freightTable[i][j] = FormatUtils.sTons(amnt);
                }
        return HTMLUtils.createTable(locations, locations, freightTable);
    }
    
    public static String genIntraSystemPassengerReport(IGenScheme scheme, SystemBean sys, DateBean date)
    {
        BodySpecialAdvBean[] locations = findLocations(sys);
        String[][] freightTable = new String[locations.length][locations.length];
        for (int i = 0; i < locations.length; i++)
            for (int j = 0; j < locations.length; j++)
                if (i == j)
                    freightTable[i][j] = "-";
                else
                {
                    int[] amnt = calc28DayPassengerAverage(scheme, locations[i], locations[j], date);
                    if (amnt[0] + amnt[1] + amnt[2] == 0)
                        freightTable[i][j] = "-";
                    else
                        freightTable[i][j] = amnt[0]+"/"+amnt[1]+"/"+amnt[2];
                }
        return HTMLUtils.createTable(locations, locations, freightTable);
    }
    
    public static String genIntraSystemCargoReport(IGenScheme scheme, SystemBean sys, DateBean date)
    {
        String[] colLabels = { "Cargo", "Demand", "Production" };
        BodySpecialAdvBean[] locations = findLocations(sys);
        String[][] freightTable = new String[locations.length][3];
        for (int i = 0; i < locations.length; i++)
        {
            int amnt = calc28DayCargoAverage(scheme, locations[i], date);
            freightTable[i][0] = FormatUtils.sTons(amnt);
            freightTable[i][1] = String.valueOf(locations[i].getDemand());
            freightTable[i][2] = String.valueOf(locations[i].getProduction());
        }
        return HTMLUtils.createTable(colLabels, locations, freightTable);
    }
    
    private static int calc28DayFreightAverage(IGenScheme scheme, BodySpecialAdvBean from, BodySpecialAdvBean to, DateBean date)
    {
        int count = 0;
        int total = 0;
        int[] mmi = new int[3];
        DateBean day = new DateBean();
        for (int i = 0; i < 28; i++)
        {
            day.setMinutes(date.getMinutes() - i*24*60);
            ((AdvGenCargo)scheme.getGeneratorCargo()).generateFreightQuanDay(mmi, from, to, day);
            count++;
        }
        total = ((mmi[0] + mmi[1] + mmi[2])*7)/3 + mmi[0]*10 + mmi[1]*5;
        return total*7/count;
    }
    
    private static int calc28DayCargoAverage(IGenScheme scheme, BodySpecialAdvBean from, DateBean date)
    {
        int count = 0;
        int total = 0;
        int[] mmi = new int[3];
        DateBean day = new DateBean();
        for (int i = 0; i < 28; i++)
        {
            day.setMinutes(date.getMinutes() - i*24*60);
            ((AdvGenCargo)scheme.getGeneratorCargo()).generateCargoQuanDay(mmi, from, day);
            count++;
        }
        total = ((mmi[0] + mmi[1] + mmi[2])*7)/3 + mmi[0]*10 + mmi[1]*5;
        return total*7/count;
    }
    
    private static int[] calc28DayPassengerAverage(IGenScheme scheme, BodySpecialAdvBean from, BodySpecialAdvBean to, DateBean date)
    {
        int count = 0;
        int[] total = new int[3];
        DateBean day = new DateBean();
        for (int i = 0; i < 28; i++)
        {
            day.setMinutes(date.getMinutes() - i*24*60);
    		List<PassengersBean> passengerList = ((IGenPassengersEx)scheme.getGeneratorPassengers()).generatePassengers(from, to, date);
    		for (Iterator j = passengerList.iterator(); j.hasNext(); )
    		{
    		    PassengersBean passengers = (PassengersBean)j.next();
    		    total[0] += passengers.getHigh();
    		    total[1] += passengers.getMiddle();
    		    total[2] += passengers.getLow();
    		}
            count++;
        }
        total[0] /= count;
        total[1] /= count;
        total[2] /= count;
        return total;
    }
    
    private static BodySpecialAdvBean[] findLocations(SystemBean sys)
    {
        ArrayList locs = new ArrayList();
        for (Iterator i = sys.getSystemRoot().getAllSatelitesIterator(); i.hasNext(); )
        {
            Object o = i.next();
            if (o instanceof BodySpecialAdvBean)
                locs.add(o);
        }
        BodySpecialAdvBean[] ret = new BodySpecialAdvBean[locs.size()];
        locs.toArray(ret);
        return ret;
    }
}
