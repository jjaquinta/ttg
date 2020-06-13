package jo.ttg.ship.logic.plan;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipScanBean;
import jo.ttg.ship.logic.plan.img.ShipPlanImageLogic;
import jo.util.utils.io.FileUtils;
import jo.util.utils.obj.StringUtils;

public class ShipPlanConvert
{
    public static void main(String[] argv)
    {
        final File dir = new File("d:\\temp\\data\\ship");
        File dean = new File(dir, "deanfiles");
        //scanShips(dir, dean);
        //ShipPlanLogic.debug = true; dumpShip(new File(dean, "ship0315.htm"), true);
        ShipPlanLogic.debug = true; dumpShip(new File(dean, "ship0339.htm"), true); // 3:16
    }
    
    private static int MAX_THREADS = 1;

    private static void scanShips(final File dir, File dean)
    {
        File[] files = dean.listFiles();
        List<Thread> todo = makeThreads(files);        
        runThreads(todo);
    }

    private static void runThreads(List<Thread> todo)
    {
        List<Thread> running = new ArrayList<>();
        while ((todo.size() > 0) || (running.size() > 0))
        {
            System.out.println(todo.size()+" left to do, "+running.size()+" running, "+Runtime.getRuntime().freeMemory()+" memory free");
            for (Thread t : running)
            {
                ShipPlanBean p = ShipPlanBean.getPlan(t);
                if (p != null)
                    System.out.println("  "+t.getName()+" - "+p.getLastLog());
                else
                    System.out.println("  "+t.getName()+" - ?");
            }
            // remove finished
            for (Iterator<Thread> i = running.iterator(); i.hasNext(); )
            {
                Thread t = i.next();
                if (!t.isAlive())
                {
                    System.out.println("Finished: "+t.getName());
                    i.remove();
                }
            }
            // start new
            while ((running.size() < MAX_THREADS) && (todo.size() > 0))
            {
                Thread t = todo.get(0);
                todo.remove(0);
                System.out.println("Starting: "+t.getName());
                t.start();
                running.add(t);
            }
            try
            {
                Thread.sleep(2000L);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static List<Thread> makeThreads(File[] files)
    {
        List<ShipScanBean> scans = new ArrayList<>();
        // make threads
        for (int i = 0; i < files.length; i++)
        {
            try
            {
                ShipScanBean scan = scanShip(files[i], false);
                if (scan == null)
                    continue;
                scan.getMetadata().put("fileName", files[i].getName());
                scans.add(scan);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
        Collections.sort(scans, new Comparator<ShipScanBean>() {
            @Override
            public int compare(ShipScanBean o1, ShipScanBean o2)
            {
                return o1.getVolume() - o2.getVolume();
            }
        });
        List<Thread> todo = new ArrayList<Thread>();
        // make threads
        for (ShipScanBean s : scans)
        {
            try
            {
                final ShipScanBean scan = s;
                if (scan == null)
                    continue;
                Thread t = new Thread((String)scan.getMetadata().get("fileName")) { public void run() {
                    try
                    {
                        planShip(scan);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } };
                todo.add(t);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
        return todo;
    }

    private static ShipScanBean scanShip(File htmlFile, boolean overwrite)
    {
        File dir = htmlFile.getParentFile(); 
        String baseName = htmlFile.getName();
        if (!baseName.startsWith("ship"))
            return null;
        if (!baseName.endsWith("htm"))
            return null;
        if (baseName.startsWith("ship0000"))
            return null;
        baseName = baseName.substring(0, baseName.length() - 4);
        File pngFile = new File(dir, baseName+"_00.png");
        if (!overwrite && pngFile.exists())
            return null;
        try
        {
            ShipScanBean scan = ShipPlanScanMTLogic.scanMT(htmlFile);
            if (scan == null)
                return null;
            if (scan.getVolume() < 1350)
                return null; // too small
            scan.getMetadata().put("dir", dir);
            scan.getMetadata().put("htmlFile", htmlFile);
            scan.getMetadata().put("baseName", baseName);
            return scan;
        }
        catch (Exception e)
        {
            System.out.println(baseName+": "+e);
            e.printStackTrace();
            return null;
        }
    }

    private static void planShip(ShipScanBean scan)
    {
        String baseName = (String)scan.getMetadata().get("baseName");
        File dir = (File)scan.getMetadata().get("dir");
        File data = new File(dir, "data");
        File scanFile = new File(data, baseName+"_scan.json");
        try
        {
            FileUtils.writeFile(scan.toJSON().toJSONString(), scanFile);
        }
        catch (IOException e2)
        {
            e2.printStackTrace();
        }
        ShipPlanBean ship = ShipPlanLogic.generateFrame(scan);
        if (ship.getSquares().size() == 0)
            return;
        File shipFile = new File(data, baseName+"_ship.json");
               try
        {
            FileUtils.writeFile(ship.toJSON().toJSONString(), shipFile);
        }
        catch (IOException e2)
        {
            e2.printStackTrace();
        }
        ship.println("Printing Image (waiting)");
        // synchronized to avoid memory overloading
        synchronized (ShipPlanConvert.class)
        {
            ship.println("Printing Image");
            List<BufferedImage> imgs = ShipPlanImageLogic.printShipImage(ship, null);
            for (int i = 0; i < imgs.size(); i++)
            {
                String fname = baseName+"_"+StringUtils.zeroPrefix(i, 2)+".png";
                File imgFile = new File((i == 0) ? dir : data, fname);
                try
                {
                    ImageIO.write(imgs.get(i), "PNG", imgFile);
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        File logFile = new File(data, baseName+".log");
        try
        {
            FileUtils.writeFile(ship.getLog(), logFile);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        ship.println("Modifying HTML file");
        File htmlFile = (File)scan.getMetadata().get("htmlFile");
        try
        {
            String html = FileUtils.readFileAsString(htmlFile.toString());
            String tag = "<IMG src=\""+baseName+"_00.png\"/>";
            int o = html.indexOf(tag);
            if (o < 0)
            {
                o = html.indexOf("Author:");
                if (o >= 0)
                {
                    html = html.substring(0, o) + tag + "\n<P>\n" + html.substring(o);
                    FileUtils.writeFile(html, htmlFile);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(baseName+": "+e);
            e.printStackTrace();
        }
        ship.println("Done");
    }
    
    private static void dumpShip(File htmlFile, boolean overwrite)
    {
        File dir = htmlFile.getParentFile(); 
        String baseName = htmlFile.getName();
        if (!baseName.startsWith("ship"))
            return;
        if (!baseName.endsWith("htm"))
            return;
        if (baseName.startsWith("ship0000"))
            return;
        baseName = baseName.substring(0, baseName.length() - 4);
        File pngFile = new File(dir, baseName+"_00.png");
        if (!overwrite && pngFile.exists())
            return;
        ShipPlanBean ship = new ShipPlanBean();
        if (ShipPlanLogic.debug)
            ship.addPropertyChangeListener("log", new PropertyChangeListener() {
                private int soFar = 0;
                @Override
                public void propertyChange(PropertyChangeEvent evt)
                {
                    String log = (String)evt.getNewValue();
                    String newLog = log.substring(soFar);
                    System.out.print(newLog);
                    soFar= log.length();
                }
            });
        try
        {
            ShipScanBean scan = ShipPlanScanMTLogic.scanMT(htmlFile);
            if (scan == null)
                return;
            if (scan.getVolume() < 1350)
                return; // too small
            System.out.println(baseName);
            ShipPlanLogic.generateFrame(scan, ship);
        }
        catch (Exception e)
        {
            System.out.println(baseName+": "+e);
            e.printStackTrace();
            return;
        }
        if (ship.getSquares().size() == 0)
            return;
        List<BufferedImage> imgs = ShipPlanImageLogic.printShipImage(ship, null);
        for (int i = 0; i < imgs.size(); i++)
        {
            String fname = baseName+"_"+StringUtils.zeroPrefix(i, 2)+".png";
            File imgFile = new File(dir, fname);
            try
            {
                ImageIO.write(imgs.get(i), "PNG", imgFile);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            String html = FileUtils.readFileAsString(htmlFile.toString());
            String tag = "<IMG src=\""+baseName+"_00.png\"/>";
            int o = html.indexOf(tag);
            if (o < 0)
            {
                o = html.indexOf("Author:");
                if (o >= 0)
                {
                    html = html.substring(0, o) + tag + "\n<P>\n" + html.substring(o);
                    FileUtils.writeFile(html, htmlFile);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(baseName+": "+e);
            e.printStackTrace();
            return;
        }
    }
}
