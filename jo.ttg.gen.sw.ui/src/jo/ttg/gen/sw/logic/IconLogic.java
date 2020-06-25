package jo.ttg.gen.sw.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import jo.ttg.beans.URIBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.core.ui.swing.surf.SpherePainterLogic;
import jo.ttg.logic.gen.SurfaceLogic;
import jo.util.utils.io.ResourceUtils;
import jo.util.utils.obj.StringUtils;

public class IconLogic
{
    public static final Icon BOOKMARK = getLocalIcon("bm");
    public static final Icon MAKE_BOOKMARK = getLocalIcon("makebm");
    public static final Icon REPORTS = getLocalIcon("reports");
    
    private static Map<String, SurfaceBean> mIconQueue = new HashMap<>();
    private static Thread mIconRunner = null;
    private static Map<String, BodyWorldBean> mBodyQueue = new HashMap<>();
    private static Thread mSurfaceRunner = null;
    
    public static boolean isIcon(BodyBean body)
    {
        File dir = new File(RuntimeLogic.getDataDir(), "icons");
        dir.mkdirs();
        String name = toBaseName(body);
        File iconFile = new File(dir, name+"_icon.png");
        return iconFile.exists();
    }
    
    public static BufferedImage getIcon(BodyBean body)
    {
        File dir = new File(RuntimeLogic.getDataDir(), "icons");
        dir.mkdirs();
        String name = toBaseName(body);
        File iconFile = new File(dir, name+"_icon.png");
        if (!iconFile.exists())
            return null;
        try
        {
            return ImageIO.read(iconFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static URL getIconURI(BodyBean body)
    {
        File dir = new File(RuntimeLogic.getDataDir(), "icons");
        dir.mkdirs();
        String name = toBaseName(body);
        File iconFile = new File(dir, name+"_icon.png");
        if (!iconFile.exists())
            return null;
        try
        {
            return iconFile.toURI().toURL();
        }
        catch (MalformedURLException e)
        {
            return null;
        }
    }
    
    public static List<BufferedImage> getIcons(Object body)
    {
        File dir = new File(RuntimeLogic.getDataDir(), "icons");
        dir.mkdirs();
        String name = toBaseName(body);
        List<BufferedImage> icons = new ArrayList<BufferedImage>();
        for (int rot = 0; rot < 360; rot += 360/24)
        {
            File imgFile = new File(dir, name+"_spin_"+StringUtils.zeroPrefix(rot, 3)+".png");
            if (!imgFile.exists())
                return null;
            try
            {
                BufferedImage img = ImageIO.read(imgFile);
                if (img != null)
                    icons.add(img);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
        return icons;
    }
    
    public static void ensureIcon(SurfaceBean surface)
    {
        File dir = new File(RuntimeLogic.getDataDir(), "icons");
        dir.mkdirs();
        String name = toBaseName(surface);
        File iconFile = new File(dir, name+"_icon.png");
        if (iconFile.exists())
            return;
        synchronized (mIconQueue)
        {
            if (mIconQueue.containsKey(surface.getURI()))
                return;
            mIconQueue.put(surface.getURI(), surface);
            if ((mIconRunner != null) && mIconRunner.isAlive())
                return;
            mIconRunner = new Thread("Icon Generator") { public void run() { doIconGeneration(); } };
            mIconRunner.start();
        }
    }
    
    private static void doIconGeneration()
    {
        for (;;)
        {
            SurfaceBean todo = null;
            synchronized (mIconQueue)
            {
                if (mIconQueue.size() == 0)
                {   // all done
                    mIconRunner = null;
                    break;
                }
                String uri = mIconQueue.keySet().iterator().next();
                todo = mIconQueue.get(uri);
            }
            if (todo == null)
                break;
            generateIcons(todo);
            synchronized (mIconQueue)
            {
                mIconQueue.remove(todo.getURI());
            }
        }
    }
    
    private static void generateIcons(SurfaceBean surface)
    {
        File dir = new File(RuntimeLogic.getDataDir(), "icons");
        String name = toBaseName(surface);
        for (int rot = 360 - 360/24; rot >= 0; rot -= 360/24)
        {
            BufferedImage img = SpherePainterLogic.paintSphere(surface, 256, 256, rot);
            File imgFile = new File(dir, name+"_spin_"+StringUtils.zeroPrefix(rot, 3)+".png");
            try
            {
                ImageIO.write(img, "PNG", imgFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (rot == 0)
            {
                BufferedImage icon = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
                icon.getGraphics().drawImage(img.getScaledInstance(24, 24, BufferedImage.SCALE_SMOOTH), 0, 0, null);
                File iconFile = new File(dir, name+"_icon.png");
                try
                {
                    ImageIO.write(icon, "PNG", iconFile);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static String toBaseName(Object world)
    {
        String uri;
        if (world instanceof URIBean)
            uri = ((URIBean)world).getURI();
        else
            uri = world.toString();
        String name = uri.replace('/', '_');
        int o = name.indexOf(':');
        if (o > 0)
            name = name.substring(o+1); // strip body:
        return name;
    }
    
    public static void ensureIcons(SystemBean system)
    {
        synchronized (mBodyQueue)
        {
            for (Iterator<BodyBean> i = system.getSystemRoot().getAllSatelitesIterator(); i.hasNext(); )
            {
                BodyBean b = i.next();
                if (!(b instanceof BodyWorldBean))
                    continue;
                if (isIcon(b))
                    continue;
                if (mBodyQueue.containsKey(b.getURI()))
                    return;
                mBodyQueue.put(b.getURI(), (BodyWorldBean)b);
            }
            if (mBodyQueue.size() > 0)
            {
                if ((mSurfaceRunner != null) && mSurfaceRunner.isAlive())
                    return;
                mSurfaceRunner = new Thread("Surface Generator") { public void run() { doSurfaceGeneration(); } };
                mSurfaceRunner.start();
            }
        }
    }
    
    private static void doSurfaceGeneration()
    {
        for (;;)
        {
            BodyWorldBean todo = null;
            synchronized (mBodyQueue)
            {
                if (mBodyQueue.size() == 0)
                {   // all done
                    mSurfaceRunner = null;
                    break;
                }
                String uri = mBodyQueue.keySet().iterator().next();
                todo = mBodyQueue.get(uri);
            }
            if (todo == null)
                break;
            generateSurface(todo);
            synchronized (mBodyQueue)
            {
                mBodyQueue.remove(todo.getURI());
            }
        }
    }
    
    private static void generateSurface(BodyWorldBean w)
    {
        SurfaceBean surface = SurfaceLogic.getFromBody(w);
        ensureIcon(surface);
    }
    
    private static ImageIcon getLocalIcon(String name)
    {
        try
        {
            BufferedImage img = ImageIO.read(ResourceUtils.loadSystemResourceStream("img/"+name+".png", RuntimeLogic.class));
            return new ImageIcon(img);
        }
        catch (IOException e)
        {
            return null;
        }
    }
}
