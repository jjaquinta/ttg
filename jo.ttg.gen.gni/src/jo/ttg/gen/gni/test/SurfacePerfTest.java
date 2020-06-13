package jo.ttg.gen.gni.test;

import java.util.Iterator;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.gen.gni.GNIGenSchemeSpinward;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.gen.SurfaceLogic;
import jo.ttg.logic.sec.SectorLogic;
import jo.ttg.logic.sys.SystemLogic;

public class SurfacePerfTest
{
    private static void test(IGenScheme scheme)
    {
        SchemeLogic.setDefaultScheme(scheme);
        long surfaceTime = 0;
        int systems = 0;
        SectorBean sec = SectorLogic.getFromOrds(new OrdBean());
        for (Iterator<SubSectorBean> i = sec.getSubSectorsIterator(); i.hasNext(); )
        {
            SubSectorBean sub = i.next();
            for (Iterator<MainWorldBean> j = sub.getMainWorldsIterator(); j.hasNext(); )
            {
                MainWorldBean mw = j.next();
                systems++;
                SystemBean sys = SystemLogic.getFromOrds(mw.getOrds());
                BodyBean body = SystemLogic.findMainworld(sys);
                System.out.print(systems+" "+mw.getName()+": ");
                long start = System.currentTimeMillis();
                SurfaceLogic.getFromBody(body);
                long end = System.currentTimeMillis();
                surfaceTime += (end - start);
                System.out.println(String.valueOf(end - start));
            }
        }
        System.out.println("Average Time: "+(surfaceTime/systems));
        System.out.println("Total Time: "+surfaceTime);
    }
    
    public static void main(String[] argv)
    {
        IGenScheme scheme = new GNIGenSchemeSpinward();
        test(scheme);
    }
}
