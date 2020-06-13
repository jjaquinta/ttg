package jo.ttg.test.surf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.core.ui.swing.surf.MapPainterLogic;
import jo.ttg.gen.IGenScheme;
import jo.ttg.gen.gni.GNIGenSchemeSpinward;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.gen.SurfaceLogic;
import jo.ttg.logic.sec.SectorLogic;
import jo.ttg.logic.sys.SystemLogic;
import jo.util.utils.obj.StringUtils;

public class MWToSVG
{
    private static int WIDTH = 5*256;
    private static int HEIGHT = 2*256+32;

    private static void printMap(File dir, MainWorldBean mw, SurfaceBean bean)
    {
        // Get a DOMImplementation
        DOMImplementation domImpl =
            GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document
        Document document = domImpl.createDocument(null, "svg", null);

        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // Ask the test to render into the SVG Graphics2D implementation
        MapPainterLogic.paintMap(svgGenerator, bean, WIDTH, HEIGHT, true, null);

        // Finally, stream out SVG to the standard output using UTF-8
        // character to byte encoding
        boolean useCSS = true; // we want to use CSS style attribute
        String fname = StringUtils.zeroPrefix(mw.getOrds().getX()+1, 2)+StringUtils.zeroPrefix(mw.getOrds().getY()+1, 2)
            +"_"+mw.getName()+".svg";
        Writer out;
        try
        {
            out = new FileWriter(new File(dir, fname));
            svgGenerator.stream(out, useCSS);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
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
                printMap(dir, mw, bean);
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
