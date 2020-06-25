/*
 * Created on Dec 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.adv.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.RandBean;
import jo.ttg.gen.IGenScheme;
import jo.util.beans.PCSBean;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Game extends PCSBean
{
    private String              mSaveFile;
    private DateBean            mDate;
    private boolean             mAnyChange;
    private String              mStatus;
    private List<String>        mStatusHistory;
    private long                mStatusChange;
    private ShipInst            mShip;
    private IGenScheme          mScheme;
    private List<UNIDInst>      mUsedUNIDs;
    private RandBean            mRnd;
    private AccountsBean        mAccounts;
    private Map<String, Double> mReputation;

    public Game()
    {
        mStatus = "";
        mStatusHistory = new ArrayList<>();
        mShip = new ShipInst();
        mDate = new DateBean();
        mDate.setYear(1110);
        mUsedUNIDs = new ArrayList<>();
        mRnd = new RandBean();
        mRnd.setSeed(System.currentTimeMillis());
        mAccounts = new AccountsBean();
        mReputation = new HashMap<>();
        // test();
    }

    // private void test()
    // {
    // UniverseBean uni = mScheme.getGeneratorUniverse().generateUniverse();
    // List mws = uni.findMainWorlds(new OrdBean(0,0,0), new OrdBean(32, 40,
    // 1));
    // System.out.println(mws.size()+" worlds.");
    // MainWorldBean home;
    // for (Iterator i = mws.iterator(); i.hasNext(); )
    // {
    // MainWorldBean mw = (MainWorldBean)i.next();
    // System.out.println("World:"+mw.getExportLine());
    // //System.out.println("Generating system.");
    // //ttg.gen.GenSystem genSys = mScheme.getGeneratorSystem();
    // //SystemBean sys = genSys.generateSystem(mw.getOrds());
    // //System.out.println("Generated system.");
    // }
    // }

    /**
     * @return
     */
    public DateBean getDate()
    {
        return mDate;
    }

    /**
     * @return
     */
    public String getSaveFile()
    {
        return mSaveFile;
    }

    /**
     * @param l
     */
    public void setDate(DateBean l)
    {
        queuePropertyChange("date", mDate, l);
        mDate = l;
        firePropertyChange();
    }

    /**
     * @param file
     */
    public void setSaveFile(String file)
    {
        mSaveFile = file;
    }

    /**
     * @return
     */
    public String getStatus()
    {
        return mStatus;
    }

    /**
     * @return
     */
    public List<String> getStatusHistory()
    {
        return mStatusHistory;
    }

    /**
     * @param string
     */
    public void setStatus(String string)
    {
        queuePropertyChange("status", mStatus, string);
        mStatus = string;
        firePropertyChange();
    }

    /**
     * @return
     */
    public boolean isAnyChange()
    {
        return mAnyChange;
    }

    /**
     * @param b
     */
    public void setAnyChange(boolean b)
    {
        mAnyChange = b;
    }

    /**
     * @return
     */
    public ShipInst getShip()
    {
        return mShip;
    }

    /**
     * @param bean
     */
    public void setShip(ShipInst bean)
    {
        queuePropertyChange("ship", mShip, bean);
        mShip = bean;
        firePropertyChange();
    }

    /**
     * @return
     */
    public IGenScheme getScheme()
    {
        return mScheme;
    }

    /**
     * @param scheme
     */
    public void setScheme(IGenScheme scheme)
    {
        mScheme = scheme;
    }

    /**
     * @return
     */
    public List<UNIDInst> getUsedUNIDs()
    {
        return mUsedUNIDs;
    }

    public RandBean getRnd()
    {
        return mRnd;
    }

    public void setRnd(RandBean rnd)
    {
        mRnd = rnd;
    }

    public AccountsBean getAccounts()
    {
        return mAccounts;
    }

    public void setAccounts(AccountsBean accounts)
    {
        mAccounts = accounts;
    }

    public Map<String, Double> getReputation()
    {
        return mReputation;
    }

    public void setReputation(Map<String, Double> reputation)
    {
        mReputation = reputation;
    }

    public long getStatusChange()
    {
        return mStatusChange;
    }

    public void setStatusChange(long statusChange)
    {
        mStatusChange = statusChange;
    }

    public void setStatusHistory(List<String> statusHistory)
    {
        mStatusHistory = statusHistory;
    }

    public void setUsedUNIDs(List<UNIDInst> usedUNIDs)
    {
        mUsedUNIDs = usedUNIDs;
    }
}
