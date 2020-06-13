package jo.ttg.test.surf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.core.ui.swing.surf.SpherePainterLogic;
import jo.ttg.gen.IGenScheme;
import jo.ttg.gen.gni.GNIGenSchemeSpinward;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.gen.SurfaceLogic;
import jo.ttg.logic.sec.SectorLogic;
import jo.ttg.logic.sys.SystemLogic;
import jo.util.utils.obj.StringUtils;

public class MWToSphere
{
    private static int WIDTH = 256;
    private static int HEIGHT = 256;
    private static void test(IGenScheme scheme)
    {
        SchemeLogic.setDefaultScheme(scheme);
        File dir = new File("d:\\temp\\data\\ttg\\map");
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
                SurfaceBean bean = SurfaceLogic.getFromBody(body);
                long end = System.currentTimeMillis();
                surfaceTime += (end - start);
                System.out.println(String.valueOf(end - start));
                if (bean == null)
                    continue;
                for (int rot = 0; rot < 360; rot += 360/24)
                {
                    BufferedImage img = SpherePainterLogic.paintSphere(bean, WIDTH, HEIGHT, rot);
                    String fname = StringUtils.zeroPrefix(mw.getOrds().getX()+1, 2)+StringUtils.zeroPrefix(mw.getOrds().getY()+1, 2)
                        +"_"+mw.getName()+"_g_"+StringUtils.zeroPrefix(rot, 3)+".png";
                    try
                    {
                        ImageIO.write(img, "PNG", new File(dir, fname));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            }
            break;
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
