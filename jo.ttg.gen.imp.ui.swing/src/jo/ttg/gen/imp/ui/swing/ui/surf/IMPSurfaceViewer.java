package jo.ttg.gen.imp.ui.swing.ui.surf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import jo.ttg.beans.surf.MapHexBean;
import jo.ttg.beans.surf.SurfaceAnnotationBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.core.ui.swing.surf.MapPainterLogic;
import jo.ttg.gen.imp.ui.swing.data.RuntimeBean;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.util.heal.HEALLogic;
import jo.util.ui.swing.utils.MouseUtils;

public class IMPSurfaceViewer extends JPanel
{
    private SurfaceBean                         mSurface;
    private Dimension                           mImageSize = null;
    private Map<MapHexBean, Shape>              mPositionCache = new HashMap<>();

    public IMPSurfaceViewer()
    {
        initInstantiate();
        initLink();
        initLayout();
        doUpdateSurface();
    }

    private void initInstantiate()
    {
        setBackground(Color.black);
    }

    private void initLayout()
    {
    }

    private void initLink()
    {
        RuntimeLogic.listen("surface", (ov, nv) -> doUpdateSurface());
        MouseUtils.mouseWheelMoved(this, (ev) -> doMouseWheelMoved(ev));
        MouseUtils.mouseClicked(this, (ev) -> doMouseClicked(ev));
        MouseUtils.mouseMoved(this, (ev) -> doMouseMoved(ev));
    }

    private void doUpdateSurface()
    {
        setSurface(RuntimeLogic.getInstance().getSurface());
    }

    private void doMouseClicked(MouseEvent ev)
    {
        if (ev.getClickCount() == 2)
            if (ev.getButton() == MouseEvent.BUTTON3)
            {
                RuntimeLogic.setZoom(RuntimeBean.ZOOM_SYSTEM);
            }
    }

    private void doMouseMoved(MouseEvent ev)
    {
        MapHexBean hex = findHex(ev.getX(), ev.getY());
        if (hex == null)
            setToolTipText("");
        else
            setToolTipText(getOneLine(hex));
    }

    private void doMouseWheelMoved(MouseWheelEvent ev)
    {
        if (ev.getWheelRotation() < 0)
        { // zoom in
            if (mImageSize == null)
                mImageSize = getSize();
            mImageSize.width += mImageSize.width / 20;
            mImageSize.height += mImageSize.height / 20;
        }
        else
        { // zoom out
            if (mImageSize == null)
                mImageSize = getSize();
            mImageSize.width -= mImageSize.width / 20;
            mImageSize.height -= mImageSize.height / 20;
        }
        setPreferredSize(mImageSize);
        repaint();
        getParent().doLayout();
    }

    @Override
    public void paint(Graphics g1)
    {
        Graphics2D g = (Graphics2D)g1;
        Dimension size = getSize();
        g.setColor(getBackground());
        g.fillRect(0, 0, size.width, size.height);
        if (mSurface == null)
            return;
        Dimension designSize = (mImageSize == null) ? size : mImageSize;
        BufferedImage img = MapPainterLogic.paintMap(mSurface, designSize.width,
                designSize.height, mPositionCache);
        g.drawImage(img, 0, 0, null);
    }
    
    // utility
    
    private MapHexBean findHex(int x, int y)
    {
        for (MapHexBean hex : mPositionCache.keySet())
        {
            Shape s = mPositionCache.get(hex);
            if (s.contains(x, y))
                return hex;
        }
        return null;
    }
    
    private String getOneLine(MapHexBean hex)
    {
        double[] longlat = HEALLogic.getLongLat(hex.getLocation());
        StringBuffer ret = new StringBuffer();
        ret.append(FormatUtils.formatLongLat(longlat));
        boolean doneNote = false;
        for (SurfaceAnnotationBean note : mSurface.getAnnotations())
            if (note.getLocation().equals(hex.getLocation()))
            {
                ret.append(" ");
                ret.append(note.getDescription());
                doneNote = true;
                break;
            }
        if (!doneNote)
        {
            ret.append(" ");
            switch (hex.getCover())
            {
                case MapHexBean.C_SICE:
                    ret.append("Year Round Ice");
                    break;
                case MapHexBean.C_DEEP:
                    ret.append("Ocean Depths");
                    break;
                case MapHexBean.C_WICE:
                    ret.append("Seasonal Ice");
                    break;
                case MapHexBean.C_MTNS:
                    ret.append("Mountains");
                    break;
                case MapHexBean.C_VOLC:
                    ret.append("Volcano");
                    break;
                case MapHexBean.C_ROUGH:
                    ret.append("Hills");
                    break;
                case MapHexBean.C_FOREST:
                    ret.append("Forest");
                    break;
                case MapHexBean.C_JUNGLE:
                    ret.append("Jungle");
                    break;
                case MapHexBean.C_DESERT:
                    ret.append("Desert");
                    break;
                case MapHexBean.C_WATER:
                    ret.append("Ocean");
                    break;
                case MapHexBean.C_TUNDRA:
                    ret.append("Tundra");
                    break;
                case MapHexBean.C_OPEN:
                    ret.append("Open");
                    break;
                case MapHexBean.C_ISLAND:
                    ret.append("Island");
                    break;
                case MapHexBean.C_LAKE:
                    ret.append("Lake");
                    break;
            }
        }
        return ret.toString();
    }
    
    // getters and setters

    public SurfaceBean getSurface()
    {
        return mSurface;
    }

    public void setSurface(SurfaceBean surface)
    {
        mSurface = surface;
        mImageSize = null;
        repaint();
    }
}
