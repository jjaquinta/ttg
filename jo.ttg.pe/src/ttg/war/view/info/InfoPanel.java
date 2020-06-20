package ttg.war.view.info;

import java.awt.CardLayout;
import java.beans.PropertyChangeSupport;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import jo.util.ui.swing.ctrl.FixedPanel;
import jo.util.utils.DebugUtils;
import ttg.war.beans.Game;
import ttg.war.beans.GameInst;
import ttg.war.beans.Ship;
import ttg.war.beans.ShipInst;
import ttg.war.beans.SideInst;
import ttg.war.beans.WorldInst;
import ttg.war.view.WarPanel;

public class InfoPanel extends FixedPanel
{
    private WarPanel              mPanel;
    private PropertyChangeSupport mPCS;

    private CardLayout            mLayout;

    private JPanel                mBlank;
    private ShipInfoPanel         mShipInfo;
    private ShipInstInfoPanel     mShipInstInfo;
    private WorldInfoPanel        mWorldInfo;
    private PortInfoPanel         mPortInfo;
    private SideInfoPanel         mSideInfo;
    private GameInfoPanel         mGameInfo;
    private GameInstInfoPanel     mGameInstInfo;

    /**
     *
     */

    public InfoPanel(WarPanel panel)
    {
        mPanel = panel;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mPCS = new PropertyChangeSupport(this);
        mBlank = new JPanel();
        mSideInfo = new SideInfoPanel(mPanel, this);
        mShipInfo = new ShipInfoPanel(mPanel, this);
        mShipInstInfo = new ShipInstInfoPanel(mPanel, this);
        mWorldInfo = new WorldInfoPanel(mPanel, this);
        mPortInfo = new PortInfoPanel(mPanel, this);
        mGameInfo = new GameInfoPanel(mPanel, this);
        mGameInstInfo = new GameInstInfoPanel(mPanel, this);

        mLayout = new CardLayout();
    }

    private void initLink()
    {
    }

    private void initLayout()
    {
        Border b;
        b = BorderFactory.createMatteBorder(3, 3, 3, 3, getBackground());
        b = BorderFactory.createCompoundBorder(b,
                BorderFactory.createRaisedBevelBorder());
        b = BorderFactory.createCompoundBorder(b,
                BorderFactory.createMatteBorder(3, 3, 3, 3, getBackground()));
        setBorder(b);
        setLayout(mLayout);

        add("blank", mBlank);
        add("side", mSideInfo);
        add("ship", mShipInfo);
        add("shipInst", mShipInstInfo);
        add("world", mWorldInfo);
        add("port", mPortInfo);
        add("game", mGameInfo);
        add("gameInst", mGameInstInfo);
    }

    public void setObject(Object obj)
    {
        if (obj == null)
        {
            DebugUtils.debug("Setting a null.");
            DebugUtils.dumpCallStack();
        }
        // if (obj == mObject)
        // return;
        mShipInfo.setObject(null);
        mShipInstInfo.setObject(null);
        mSideInfo.setObject(null);
        mWorldInfo.setObject(null);
        mPortInfo.setObject(null);
        mGameInfo.setObject(null);
        mGameInstInfo.setObject(null);
        if (obj instanceof SideInst)
        {
            mSideInfo.setObject((SideInst)obj);
            mLayout.show(this, "side");
        }
        else if (obj instanceof Ship)
        {
            mShipInfo.setObject((Ship)obj);
            mLayout.show(this, "ship");
        }
        else if (obj instanceof ShipInst)
        {
            mShipInstInfo.setObject((ShipInst)obj);
            mLayout.show(this, "shipInst");
        }
        else if (obj instanceof WorldInst)
        {
            mWorldInfo.setObject((WorldInst)obj);
            mPortInfo.setObject((WorldInst)obj);
            mLayout.show(this, "world");
        }
        else if (obj instanceof Game)
        {
            mGameInfo.setObject((Game)obj);
            mLayout.show(this, "game");
        }
        else if (obj instanceof GameInst)
        {
            mGameInstInfo.setObject((GameInst)obj);
            mLayout.show(this, "gameInst");
        }
        else
        {
            mLayout.show(this, "blank");
        }
        mPCS.firePropertyChange("object", null, obj);
    }

    public void showInfo(String panel)
    {
        mLayout.show(this, panel);
    }

    /**
     * @return
     */
    public PropertyChangeSupport getPCS()
    {
        return mPCS;
    }

}
