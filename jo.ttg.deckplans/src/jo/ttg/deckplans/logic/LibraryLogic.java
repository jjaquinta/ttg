package jo.ttg.deckplans.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.deckplans.beans.LibEntryBean;
import jo.ttg.ship.logic.plan.ShipPlanScanMTLogic;
import jo.util.utils.io.ResourceUtils;
import jo.util.utils.io.StreamUtils;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;

public class LibraryLogic
{
    private static List<LibEntryBean> mLibrary = new ArrayList<LibEntryBean>();
    
    static void init()
    {
        for (int i = 1; i < 400; i++)
        {
            String resourceName = "lib/ship"+StringUtils.zeroPrefix(i, 4)+"_scan.json";
            String txt = null;
            try
            {
                InputStream is = ResourceUtils.loadSystemResourceStream(resourceName, LibraryLogic.class);
                if (is == null)
                    continue;
                txt = StreamUtils.readStreamAsString(is);
                is.close();
            }
            catch (IOException e)
            {
                continue;
            }
            JSONObject json = JSONUtils.readJSONString(txt);
            if (json == null)
                continue;
            ingest(json);
        }
    }

    private static void ingest(JSONObject json)
    {
        LibEntryBean lib = new LibEntryBean();
        lib.setJSON(json);
        lib.setHTML(JSONUtils.getString(json, "metadata.raw"));
        if (StringUtils.isTrivial(lib.getHTML()))
            return;
        Map<String,String> chunks = ShipPlanScanMTLogic.chunkText(lib.getHTML());
        lib.setTitle(StringUtils.extract(lib.getHTML(), "<TITLE>", "</TITLE>"));
        String v = ShipPlanScanMTLogic.getValue(ShipPlanScanMTLogic.CRAFTID, "TL", chunks);
        int tl = IntegerUtils.parseInt(v);
        lib.setTechLevel(tl);
        int disp = (int)ShipPlanScanMTLogic.getVolume(chunks);
        lib.setDisplacement(disp);
        v = ShipPlanScanMTLogic.getValue(ShipPlanScanMTLogic.LOCO, "Jump", chunks);
        int jump = IntegerUtils.parseInt(v);
        lib.setJump(jump);
        v = ShipPlanScanMTLogic.getValue(ShipPlanScanMTLogic.LOCO, "Maneuver", chunks);
        int man = IntegerUtils.parseInt(v);
        lib.setManeuver(man);
        mLibrary.add(lib);
    }
    
    public static List<LibEntryBean> getLibraryEntries()
    {
        return mLibrary;
    }
}
