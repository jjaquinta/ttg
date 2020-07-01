package jo.ttg.deckplans.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.deckplans.beans.RuntimeBean;
import jo.ttg.deckplans.beans.ShipDeckBean;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.logic.plan.img.ShipPlanImageLogic;
import jo.util.utils.ThreadHelper;
import jo.util.utils.io.FileUtils;

public class PlanLogic
{

    public static void write(File planFile)
    {
        try
        {
            JSONObject json = RuntimeLogic.getInstance().getShipPlan().toJSON();
            FileUtils.writeFile(json.toJSONString(), planFile);
            RuntimeLogic.getInstance().setLastPlan(planFile);
            RuntimeLogic.setStatus("Saved as "+planFile);
            RuntimeLogic.getInstance().setDirty(false);
        }
        catch (IOException e)
        {
            RuntimeLogic.setStatus(e.toString());
            return;
        }
    }

    public static void read(File planFile)
    {
        try
        {
            JSONObject json = JSONUtils.readJSON(planFile);
            ShipPlanBean plan = RuntimeLogic.getInstance().getShipPlan();
            plan.fromJSON(json);
            RuntimeLogic.getInstance().fireMonotonicPropertyChange("shipPlan", plan);
            RuntimeLogic.getInstance().setLastPlan(planFile);
            RuntimeLogic.getInstance().setDirty(false);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
    }

    public static ThreadHelper process()
    {
        ThreadHelper t = new ThreadHelper("Creating Deck Images") { public void run() { doProcess(); } };
        t.start();
        return t;
    }
    
    private static void doProcess()
    {
        ShipPlanBean plan = RuntimeLogic.getInstance().getShipPlan();
        ShipDeckBean deck = RuntimeLogic.getInstance().getShipDeck();
        List<BufferedImage> imgs = ShipPlanImageLogic.printShipImage(plan, deck.getImageSettings());
;       if (ThreadHelper.isCanceled())
            return;
        deck.getDecks().clear();
        deck.getDecks().addAll(imgs);
        RuntimeLogic.getInstance().fireMonotonicPropertyChange("shipDeck");
        deck.fireMonotonicPropertyChange("decks");
        RuntimeLogic.getInstance().setDirty(true);
        RuntimeLogic.setMode(RuntimeBean.DECK);
    }

}
