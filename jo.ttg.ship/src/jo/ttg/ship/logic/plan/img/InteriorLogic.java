package jo.ttg.ship.logic.plan.img;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.ShipPlanPerimeterBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.ttg.ship.logic.plan.StateroomLogic;
import jo.util.utils.ImageUtils;
import jo.util.utils.MathUtils;
import jo.util.utils.io.ResourceUtils;
import jo.util.utils.obj.StringUtils;

public class InteriorLogic
{
    private static final int FLIP_NONE = 0;
    private static final int FLIP_X = 1;
    private static final int FLIP_Y = 2;
    private static final int FLIP_XY = 3;

    public static void drawInterior(ShipPlanPerimeterBean comp, Shape perimeter)
    {
        switch (comp.getType())
        {
            case ShipSquareBean.STATEROOM:
                drawAccomodation(comp, perimeter);
                break;
            case ShipSquareBean.TURRET:
                drawTurret(comp, perimeter);
                break;
            case ShipSquareBean.BAY:
                drawBay(comp, perimeter);
                break;
            case ShipSquareBean.SPINE:
                drawSpine(comp, perimeter);
                break;
            case ShipSquareBean.POWER:
                drawRandomUnits(comp, perimeter, "engine", 1.0);
                break;
            case ShipSquareBean.MANEUVER:
                drawRandomUnits(comp, perimeter, "man", 1.0);
                break;
            case ShipSquareBean.JUMP:
                drawRandomUnits(comp, perimeter, "jump", 2.0);
                break;
            case ShipSquareBean.CARGO:
                drawRandomUnits(comp, perimeter, "cargo", 0.25);
                break;
            case ShipSquareBean.HANGER:
                drawRandomUnits(comp, perimeter, "hanger", 0.2);
                break;
        }
    }

    private static void drawAccomodation(ShipPlanPerimeterBean comp, Shape perimeter)
    {
        if (comp.getNotes() == null) // stateroom
            drawStateroom(comp, perimeter);
        else if (comp.getNotes().startsWith(StateroomLogic.GALLEY))
            drawRandomUnit(comp, perimeter, "galley");
        else if (comp.getNotes().startsWith(StateroomLogic.MAINTENENCE))
            drawRandomUnit(comp, perimeter, "maint");
        else if (comp.getNotes().startsWith(StateroomLogic.CREW_LOUNGE))
            drawRandomUnit(comp, perimeter, "crew");
        else if (comp.getNotes().startsWith(StateroomLogic.OFFICER_LOUNGE))
            drawRandomUnit(comp, perimeter, "officer");
        else if (comp.getNotes().startsWith(StateroomLogic.BRIEFING_ROOM))
            drawRandomUnit(comp, perimeter, "brief");
        else if (comp.getNotes().startsWith(StateroomLogic.MED_BAY))
            drawRandomUnit(comp, perimeter, "medical");
    }
    
    private static BufferedImage mStateroom = null;

    private static void drawStateroom(ShipPlanPerimeterBean comp, Shape perimeter)
    {
        if (mStateroom == null)
            mStateroom = loadImage("stateroom.png");
        Rectangle r = perimeter.getBounds();
        // find door
        ShipSquareBean[][] room = getRoomAsRectangle(comp);
        if (room[1][1].isPlusYAccess() || room[1][0].isPlusXAccess())
            drawImage(mStateroom, r.x, r.y, 0, 0, 2, 2, FLIP_NONE);
        else if (room[0][0].isMinusYAccess() || room[0][0].isMinusXAccess())
            drawImage(mStateroom, r.x, r.y, 0, 0, 2, 2, FLIP_XY);
        else if (room[1][0].isMinusYAccess() || room[1][0].isPlusXAccess())
            drawImage(mStateroom, r.x, r.y, 0, 0, 2, 2, FLIP_Y);
        else if (room[0][1].isPlusYAccess() || room[0][1].isMinusXAccess())
            drawImage(mStateroom, r.x, r.y, 0, 0, 2, 2, FLIP_X);
    }
    
    private static BufferedImage mTurretX = null;
    private static BufferedImage mTurretY = null;

    private static void drawTurret(ShipPlanPerimeterBean comp, Shape perimeter)
    {
        if (mTurretX == null)
        {
            mTurretX = loadImage("turret.png");
            mTurretY = ImageUtils.rot90(mTurretX);
        }
        Rectangle r = perimeter.getBounds();
        // find door
        ShipSquareBean[][] room = getRoomAsRectangle(comp);
        if (r.width > r.height)
        {
            if (room[0][0].isPlusYAccess() || room[0][0].isMinusYAccess() || room[0][0].isMinusXAccess())
                drawImage(mTurretX, r.x, r.y, 0, 0, 2, 1, FLIP_NONE);
            else
                drawImage(mTurretX, r.x, r.y, 0, 0, 2, 1, FLIP_X);
        }
        else
        {
            if (room[0][0].isPlusYAccess() || room[0][0].isMinusXAccess() || room[0][0].isPlusXAccess())
                drawImage(mTurretY, r.x, r.y, 0, 0, 1, 2, FLIP_NONE);
            else
                drawImage(mTurretY, r.x, r.y, 0, 0, 1, 2, FLIP_Y);
        }
    }

    private static void drawBay(ShipPlanPerimeterBean comp, Shape perimeter)
    {
        Rectangle r = perimeter.getBounds();
        BufferedImage bayImage = getBayImage(comp.getNotes(), r);
        Point3i p = comp.getSquares().iterator().next();
        if (r.width > r.height)
        {
            if (p.y < 0)
                drawImage(bayImage, r.x, r.y, 0, 0, r.width, r.height, FLIP_NONE);
            else
                drawImage(bayImage, r.x, r.y, 0, 0, r.width, r.height, FLIP_Y);
        }
        else
        {
            if (p.x > 0)
                drawImage(bayImage, r.x, r.y, 0, 0, r.width, r.height, FLIP_NONE);
            else
                drawImage(bayImage, r.x, r.y, 0, 0, r.width, r.height, FLIP_X);
        }
    }

    private static Map<String,BufferedImage> mBays = new HashMap<String, BufferedImage>();

    private static BufferedImage getBayImage(String name, Rectangle shape)
    {
        String imgName = "bay_miss1.png";
        if (name.toLowerCase().indexOf("missile") >= 0)
            imgName = "bay_miss1.png";
        else if (name.toLowerCase().indexOf("fusion") >= 0)
            imgName = "bay_fg.png";
        else if (name.toLowerCase().indexOf("meson") >= 0)
            imgName = "bay_mg.png";
        else if (name.toLowerCase().indexOf("part") >= 0)
            imgName = "bay_pa.png";
        else if (name.toLowerCase().indexOf("sand") >= 0)
            imgName = "bay_miss2.png";
        else if (name.toLowerCase().indexOf("laser") >= 0)
            imgName = "bay_fg.png";
        if (shape.width > shape.height)
        {
            if (mBays.containsKey(imgName))
                return mBays.get(imgName);
            BufferedImage img = loadImage(imgName);
            mBays.put(imgName, img);
            return img;
        }
        else
        {
            if (mBays.containsKey(imgName+"R"))
                return mBays.get(imgName+"R");
            BufferedImage img = loadImage(imgName);
            mBays.put(imgName, img);
            img = ImageUtils.rot90(img);
            mBays.put(imgName+"R", img);
            return img;
        }
    }
    
    private static void drawSpine(ShipPlanPerimeterBean comp, Shape perimeter)
    {
        Rectangle r = perimeter.getBounds();
        int w = r.width;
        int h = r.height;
        double gap = Math.min(w,  h)/6.0;
        Path2D p = new Path2D.Double();
        p.moveTo(r.x + gap, r.y + h/2);
        p.lineTo(r.x + w/2, r.y + gap);
        p.lineTo(r.x + w - gap, r.y + h/2);
        p.lineTo(r.x + w/2, r.y + h - gap);
        ShipPlanImageLogic.mData.g.setColor(Color.BLACK);
        ShipPlanImageLogic.mData.g.fill(p);
        if (w > h)
        {
            for (double x = gap*4; x < w - gap*4; x += gap*2)
            {
                double y;
                if (x < w/2)
                    y = MathUtils.interpolateSin(x, 0, w/2, 0, h/2 - gap/2);
                else
                    y = MathUtils.interpolateSin(x, w, w/2, 0, h/2 - gap/2);
                ShipPlanImageLogic.mData.g.fillRect((int)(r.x + x), (int)(r.y + h/2 - y), (int)gap, (int)(y*2));
            }
        }
        else
        {
            for (double y = gap*4; y < h - gap*4; y += gap*2)
            {
                double x;
                if (y < h/2)
                    x = MathUtils.interpolateSin(y, 0, h/2, 0, w/2 - gap/2);
                else
                    x = MathUtils.interpolateSin(y, h, h/2, 0, w/2 - gap/2);
                ShipPlanImageLogic.mData.g.fillRect((int)(r.x + w/2 - x), (int)(r.y + y), (int)(x*2), (int)gap);
            }
        }
        // find door
        ShipSquareBean[][] room = getRoomAsRectangle(comp);
        if (w > h)
        {
            if (room[0][0].isPlusYAccess() || room[0][0].isMinusYAccess() || room[0][0].isMinusXAccess())
                drawImage(mTurretX, r.x, r.y, 0, 0, 2, 1, FLIP_NONE);
            else
                drawImage(mTurretX, r.x, r.y, 0, 0, 2, 1, FLIP_X);
        }
        else
        {
            if (room[0][0].isPlusYAccess() || room[0][0].isMinusXAccess() || room[0][0].isPlusXAccess())
                drawImage(mTurretY, r.x, r.y, 0, 0, 1, 2, FLIP_NONE);
            else
                drawImage(mTurretY, r.x, r.y, 0, 0, 1, 2, FLIP_Y);
        }
    }

    private static void drawRandomUnit(ShipPlanPerimeterBean comp, Shape perimeter, String key)
    {
        Rectangle r = perimeter.getBounds();
        int w = (int)(r.getWidth()/ShipPlanImageLogic.mData.ss());
        int h = (int)(r.getHeight()/ShipPlanImageLogic.mData.ss());
        BufferedImage img = getRandomImage(key, w, h);
        // TODO: find door
        drawImage(img, r.x, r.y, 0, 0, w, h, 0);
    }

    private static void drawRandomUnits(ShipPlanPerimeterBean comp,
            Shape perimeter, String key, double density)
    {
        Rectangle r = perimeter.getBounds();
        ShipSquareBean[][] room = getRoomAsRectangle(comp);
        int w = room.length;
        int h = room[0].length;
        int units = (int)((w*h)/(2*2)*density);
        while (units-- > 0)
        {
            BufferedImage img = getRandomImage(key);
            int imgW = img.getWidth()/24; // scale size
            int imgH = img.getHeight()/24; // scale size
            if ((w < imgW) || (h < imgH))
                continue;
            int x = (w > imgW) ? ShipPlanImageLogic.mData.rnd.nextInt(w - imgW) : 0;
            int y = (h > imgH) ? ShipPlanImageLogic.mData.rnd.nextInt(h - imgH) : 0;
            if (!isEmpty(room, x, y, imgW, imgH))
                continue;
            drawImage(img, 
                    r.x, r.y, x, y, imgW, imgH,
                    ShipPlanImageLogic.mData.rnd.nextInt(3));
            markEmpty(room, x, y, imgW, imgH);
        }
    }
    
    private static boolean isEmpty(ShipSquareBean[][] room, int x, int y, int w, int h)
    {
        for (int dx = 0; dx < w; dx++)
            for (int dy = 0; dy < h; dy++)
                if (room[x+dx][y + dy] == null)
                    return false;
        return true;
    }
    
    private static void markEmpty(ShipSquareBean[][] room, int x, int y, int w, int h)
    {
        for (int dx = 0; dx < w; dx++)
            for (int dy = 0; dy < h; dy++)
                room[x+dx][y + dy] = null;
    }
    
    private static ShipSquareBean[][] getRoomAsRectangle(ShipPlanPerimeterBean comp)
    {
        Iterator<Point3i> i = comp.getSquares().iterator();
        Point3i p = i.next();
        int minx = p.x;
        int maxx = minx;
        int miny = p.y;
        int maxy = miny;
        while (i.hasNext())
        {
            p = i.next();
            minx = Math.min(minx, p.x);
            maxx = Math.max(maxx, p.x);
            miny = Math.min(miny, p.y);
            maxy = Math.max(maxy, p.y);
        }
        ShipSquareBean[][] room = new ShipSquareBean[maxx - minx + 1][maxy - miny + 1];
        for (i = comp.getSquares().iterator(); i.hasNext(); )
        {
            p = i.next();
            room[p.x - minx][p.y - miny] = ShipPlanImageLogic.mData.ship.getSquare(p);
        }
        return room;
    }
    
    private static void drawImage(BufferedImage img, int ox, int oy, int x, int y, int w, int h, int ori)
    {
        int ss = ShipPlanImageLogic.mData.ss();
        if (w >= ss)
            w /= ss;
        if (h >= ss)
            h /= ss;
        if (ori < 0)
            ori = ShipPlanImageLogic.mData.rnd.nextInt(4);
        if (ori == 0)
            ShipPlanImageLogic.mData.g.drawImage(img, 
                    ox + x*ss, oy + y*ss, ox + (x + w)*ss, oy + (y + h)*ss, 
                    0, 0, img.getWidth(), img.getHeight(), 
                    null);
        else if (ori == 3)
            ShipPlanImageLogic.mData.g.drawImage(img,
                    ox + (x + w)*ss, oy + (y + h)*ss, ox + x*ss, oy + y*ss, 
                    0, 0, img.getWidth(), img.getHeight(), 
                    null);
        else if (ori == 2)
            ShipPlanImageLogic.mData.g.drawImage(img,
                    ox + x*ss, oy + (y + h)*ss, ox + (x + w)*ss, oy + y*ss, 
                    0, 0, img.getWidth(), img.getHeight(), 
                    null);
        else if (ori == 1)
            ShipPlanImageLogic.mData.g.drawImage(img,
                    ox + (x + w)*ss, oy + y*ss, ox + x*ss, oy + (y + h)*ss, 
                    0, 0, img.getWidth(), img.getHeight(), 
                    null);
    }
    
    private static Map<String, List<BufferedImage>> mImages = new HashMap<String, List<BufferedImage>>();
    
    private static BufferedImage getRandomImage(String key)
    {
        List<BufferedImage> images = getImages(key);
        return images.get(ShipPlanImageLogic.mData.rnd.nextInt(images.size()));
    }
    
    private static BufferedImage getRandomImage(String key, int w, int h)
    {
        double aspect = (double)w/(double)h;
        List<BufferedImage> images = getImages(key);
        List<BufferedImage> qualifying = new ArrayList<>();
        for (BufferedImage img : images)
        {
            double a = (double)img.getWidth()/(double)img.getHeight();
            if (Math.abs(a - aspect) < .05)
                qualifying.add(img);
        }
        if (qualifying.size() == 0)
        {
            System.err.println("No images of type "+key+" of aspect ratio "+w+":"+h+" ("+aspect+")");
            for (BufferedImage img : images)
            {
                double a = (double)img.getWidth()/(double)img.getHeight();
                System.err.println("    "+img.getWidth()+":"+img.getHeight()+" ("+a+")");
            }
            return images.get(ShipPlanImageLogic.mData.rnd.nextInt(images.size()));
        }
        return qualifying.get(ShipPlanImageLogic.mData.rnd.nextInt(qualifying.size()));
    }
    
    private static List<BufferedImage> getImages(String key)
    {
        List<BufferedImage> images = mImages.get(key);
        if (images == null)
        {
            images = new ArrayList<>();
            loadImages(images, key);
            mImages.put(key, images);
        }
        return images;
    }
    
    private static void loadImages(List<BufferedImage> images, String pattern)
    {
        for (int i = 1; i < 99; i++)
        {
            String name = pattern+StringUtils.zeroPrefix(i, 2)+".png";
            BufferedImage img = loadImage(name);
            if (img == null)
                break;
            images.add(img);
            images.add(ImageUtils.rot90(img));
        }
        System.out.println("Loaded "+images.size()+" images of type "+pattern);
    }
    
    private static BufferedImage loadImage(String name)
    {
        InputStream is = ResourceUtils.loadSystemResourceStream(name, InteriorLogic.class);
        if (is == null)
            return null;
        try
        {
            return ImageIO.read(is);
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Cannot load image "+name);
        }
    }
}
