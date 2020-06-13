package jo.ttg.logic.gen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.logic.sec.SectorLogic;

public class ImageLogic
{
	private static Map<OrdBean,BufferedImage> mSubImageCache = new HashMap<OrdBean,BufferedImage>();
	
	public static BufferedImage makeImage(SubSectorBean sub)
	{
		OrdBean base = sub.getUpperBound();
		BufferedImage image = mSubImageCache.get(base);
		if (image == null)
		{
			image = new BufferedImage(24, 30, BufferedImage.TYPE_BYTE_GRAY);
			Graphics g = image.createGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 24, 30);
			g.setColor(Color.white);
			for (MainWorldBean mw :  sub.getMainWorlds())
			{
				OrdBean o = mw.getOrds();
				int x = (int)(o.getX() - base.getX())*3;
				int y = (int)(o.getY() - base.getY())*3;
				if (x%2 != 0)
					y++;
				g.fillRect(x, y, 2, 2);
			}
			mSubImageCache.put(base, image);
		}
		return image;
	}

	private static Map<OrdBean,BufferedImage> mSecImageCache = new HashMap<OrdBean,BufferedImage>();
	
	public static BufferedImage makeImage(SectorBean sec)
	{
		OrdBean base = sec.getUpperBound();
		BufferedImage image = mSecImageCache.get(base);
		if (image == null)
		{
			image = new BufferedImage(96, 120, BufferedImage.TYPE_BYTE_GRAY);
			Graphics g = image.createGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 96, 120);
			g.setColor(Color.white);
			for (MainWorldBean mw : SectorLogic.findMainWorlds(sec, sec.getUpperBound(), sec.getLowerBound()))
			{
				OrdBean o = mw.getOrds();
				int x = (int)(o.getX() - base.getX())*3;
				int y = (int)(o.getY() - base.getY())*3;
				if (x%2 != 0)
					y++;
				g.fillRect(x, y, 2, 2);
			}
			mSecImageCache.put(base, image);
		}
		return image;
	}
}
