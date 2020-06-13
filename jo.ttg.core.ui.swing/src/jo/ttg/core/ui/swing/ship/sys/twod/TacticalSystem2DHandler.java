package jo.ttg.core.ui.swing.ship.sys.twod;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.LocBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.logic.gen.BodyLogic;
import jo.util.geom3d.Point3D;

public class TacticalSystem2DHandler implements ISystem2DHandler
{
    private DateBean    mDate;
    private List<TransformedBody>    mInView;
    
    public TacticalSystem2DHandler()
    {
        mDate = new DateBean();
        mDate.setYear(1100);
        mInView = new ArrayList<TransformedBody>();
    }

    @Override
    public Point3D getLocation(BodyBean body)
    {
        LocBean loc = BodyLogic.getLocation(body, mDate);
        return loc;
    }

    @Override
    public void preDraw(Graphics2D g2, List<TransformedBody> bodies, Dimension fov, Dimension viewport)
    {
        mInView.clear();
        mInView.addAll(bodies);
        // check for possible
        for (int i = 0; i < mInView.size(); i++)
        {
            TransformedBody tbody = mInView.get(i);
            if (!tbody.isInFieldOfView())
            {   // can't see
                //System.out.println("Can't see "+tbody.getBody().getName());
                mInView.remove(i);
                i--;
                continue;
            }
            if (tbody.getDist() < tbody.getBody().getDiameter())
            {   // inside
                //System.out.println("Inside "+tbody.getBody().getName());
                mInView.remove(i);
                i--;
                continue;
            }
        }
        // check for occlusion
        for (int i = 0; i < mInView.size() - 1; i++)
        {
            TransformedBody tbody1 = mInView.get(i);
            for (int j = i + 1; j < mInView.size(); j++)
            {
                TransformedBody tbody2 = mInView.get(j);
                if (!close(tbody1, tbody2))
                    continue;
                if (tbody1.getApparentRadius() > tbody2.getApparentRadius())
                {
                    //System.out.println(tbody2.getBody().getName()+" occluded by "+tbody1.getBody().getName());
                    mInView.remove(j);
                }
                else
                {
                    //System.out.println(tbody1.getBody().getName()+" occluded by "+tbody2.getBody().getName());
                    mInView.remove(i);
                    tbody1 = tbody2;
                    mInView.add(i, tbody1);
                    mInView.remove(j);
                }
                j--;
            }
        }
//        for (TransformedBody tbody : mInView)
//            System.out.println("Remaining: "+tbody.getBody().toString());
        g2.setColor(Color.GREEN);
    }

    private boolean close(TransformedBody b1, TransformedBody b2)
    {
        return (Math.abs(b1.getTheta() - b2.getTheta()) < 1)
                || (Math.abs(b1.getPhi() - b2.getPhi()) < 1);
    }
    
    @Override
    public void draw(Graphics2D g2, TransformedBody tbody)
    {
        if (!mInView.contains(tbody))
            return;
        double x = tbody.getX();
        double y = tbody.getY();
        double r = tbody.getApparentRadius();
        //System.out.println("Body at "+x+","+y+", ("+tbody.getTheta()+","+tbody.getPhi()+") rad="+r);
        if (r < 10)
        {
            g2.draw(new Line2D.Double(x-10, y, x+10, y));
            g2.draw(new Line2D.Double(x, y-10, x, y+10));
        }
        else
        {
            g2.draw(new Ellipse2D.Double(x-r, y-r, r*2, r*2));
        }
        g2.drawString(tbody.getBody().getName(), (float)x, (float)y);
    }

    public DateBean getDate()
    {
        return mDate;
    }

    public void setDate(DateBean date)
    {
        mDate = date;
    }

}
