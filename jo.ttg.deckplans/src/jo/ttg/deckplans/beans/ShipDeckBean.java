package jo.ttg.deckplans.beans;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jo.ttg.ship.beans.plan.ShipImageSettingsBean;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.util.beans.PCSBean;

public class ShipDeckBean extends PCSBean
{
    private ShipPlanBean          mPlan;
    private List<BufferedImage>   mDecks         = new ArrayList<BufferedImage>();
    private ShipImageSettingsBean mImageSettings = new ShipImageSettingsBean();

    public ShipPlanBean getPlan()
    {
        return mPlan;
    }

    public void setPlan(ShipPlanBean plan)
    {
        queuePropertyChange("plan", plan, mPlan);
        mPlan = plan;
        firePropertyChange();
    }

    public List<BufferedImage> getDecks()
    {
        return mDecks;
    }

    public void setDecks(List<BufferedImage> decks)
    {
        queuePropertyChange("decks", mDecks, decks);
        mDecks = decks;
        firePropertyChange();
    }

    public ShipImageSettingsBean getImageSettings()
    {
        return mImageSettings;
    }

    public void setImageSettings(ShipImageSettingsBean imageSettings)
    {
        queuePropertyChange("imageSettings", mImageSettings, imageSettings);
        mImageSettings = imageSettings;
        firePropertyChange();
    }
}
