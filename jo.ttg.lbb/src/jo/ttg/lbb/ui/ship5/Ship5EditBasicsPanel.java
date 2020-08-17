package jo.ttg.lbb.ui.ship5;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.map.BeanMapUtils;
import jo.util.ui.swing.utils.map.MapperComboBox;

public class Ship5EditBasicsPanel extends JPanel
{
    private Ship5Design mShip;
    
    private JTextField  mShipName;
    private JComboBox<String> mShipType1;
    private JComboBox<String> mShipType2;
    private JTextField  mShipClass;
    private JSpinner    mTechLevel;
    private JSpinner    mHullTonnage;
    private JComboBox<String> mHullConfiguration;
    private JSpinner    mArmorFactors;
    private JSpinner    mBridgeAuxiliaryCount;
    private JComboBox<String> mComputerCode;

    public Ship5EditBasicsPanel(Ship5Design ship)
    {
        mShip = ship;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mShipName = new JTextField(24);
        mShipType1 = new JComboBox<String>(Ship5Design.TYPE_PRIMARY);
        mShipType2 = new JComboBox<String>(Ship5Design.TYPE_QUALIFIER);
        mShipClass = new JTextField(24);
        mTechLevel = new JSpinner(new SpinnerNumberModel(15, 7, 15, 1));

        mHullTonnage = new JSpinner(new SpinnerNumberModel(1000, 100, 100000, 100));
        mHullConfiguration = new JComboBox<String>(Ship5Design.CONFIG_NAMES);
        mArmorFactors = new JSpinner(new SpinnerNumberModel(0, 0, 75, 1));
        mBridgeAuxiliaryCount = new JSpinner(new SpinnerNumberModel(0, 0, 75, 1));
        mComputerCode = new JComboBox<String>(Ship5Design.COMPUTER_NAMES);
    }

    private void initLayout()
    {
        setLayout(new TableLayout());
        add("1,+", new JLabel("Ship Name:"));
        add("+,.", mShipName);
        add("+,.", new JLabel("Ship Type:"));
        add("+,.", mShipType1);
        add("+,.", mShipType2);
        add("1,+", new JLabel("Ship Class:"));
        add("+,.", mShipClass);
        add("1,+", new JLabel("Tech Level:"));
        add("+,.", mTechLevel);
        add("+,.", new JLabel("Hull Size:"));
        add("+,.", mHullTonnage);
        add("+,.", new JLabel("Hull Configuration:"));
        add("+,.", mHullConfiguration);
        add("1,+", new JLabel("Armor Factors:"));
        add("+,.", mArmorFactors);
        add("+,.", new JLabel("Computer:"));
        add("+,.", mComputerCode);
        add("+,.", new JLabel("Aux Bridges:"));
        add("+,.", mBridgeAuxiliaryCount);
    }

    private void initLink()
    {
        BeanMapUtils.map(mShipName, mShip, "shipName");
        mShipType1.setName(MapperComboBox.ONECHAR);
        BeanMapUtils.map(mShipType1, mShip, "shipType", null, null, new BiConsumer<Object, Object>() {            
            @Override
            public void accept(Object t, Object u)
            {
                Ship5Design bean = (Ship5Design)t;
                String val = u.toString();
                String oldVal = bean.getShipType();
                String newVal = val.substring(0, 1) + oldVal.substring(1);
                bean.setShipType(newVal);
            }
        }, new Function<Object, Object>() {            
            @Override
            public Object apply(Object t)
            {
                Ship5Design bean = (Ship5Design)t;
                String val = bean.getShipType();
                return val.substring(0, 1);
            }
        });
        mShipType2.setName(MapperComboBox.ONECHAR);
        BeanMapUtils.map(mShipType2, mShip, "shipType", null, null, new BiConsumer<Object, Object>() {            
            @Override
            public void accept(Object t, Object u)
            {
                Ship5Design bean = (Ship5Design)t;
                String val = u.toString();
                String oldVal = bean.getShipType();
                String newVal = oldVal.substring(0, 1) + val.substring(0, 1);
                bean.setShipType(newVal);
            }
        }, new Function<Object, Object>() {            
            @Override
            public Object apply(Object t)
            {
                Ship5Design bean = (Ship5Design)t;
                String val = bean.getShipType();
                return val.substring(1, 2);
            }
        });
        BeanMapUtils.map(mShipClass, mShip, "className");
        BeanMapUtils.map(mTechLevel, mShip, "techLevel");
        BeanMapUtils.map(mHullTonnage, mShip, "hullTonnage");
        mHullConfiguration.setName(MapperComboBox.ONECHAR);
        BeanMapUtils.map(mHullConfiguration, mShip, "hullConfigurationCode");
        BeanMapUtils.map(mArmorFactors, mShip, "armorFactors");
        BeanMapUtils.map(mBridgeAuxiliaryCount, mShip, "bridgeAuxiliaryCount");
        mComputerCode.setName(MapperComboBox.ONECHAR);
        BeanMapUtils.map(mComputerCode, mShip, "computerCode");
    }
}
