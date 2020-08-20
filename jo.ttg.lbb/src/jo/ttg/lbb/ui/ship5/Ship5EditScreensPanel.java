package jo.ttg.lbb.ui.ship5;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.map.BeanMapUtils;
import jo.util.ui.swing.utils.map.MapperComboBox;

public class Ship5EditScreensPanel extends JPanel
{
    private Ship5Design mShip;
    
    private JComboBox<String> mNuclearCode;
    private JComboBox<String> mMesonCode;
    private JComboBox<String> mForceCode;

    public Ship5EditScreensPanel(Ship5Design ship)
    {
        mShip = ship;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mNuclearCode = new JComboBox<String>(Ship5Design.MAJOR_CODES);
        mMesonCode = new JComboBox<String>(Ship5Design.MAJOR_CODES);
        mForceCode = new JComboBox<String>(Ship5Design.MAJOR_CODES);
    }

    private void initLayout()
    {
        setLayout(new TableLayout("anchor=w,ipadw=4,ipadh=2"));
        add("1,+ anchor=e", new JLabel("Nuclear Damper:"));
        add("+,.", mNuclearCode);
        add("+,. anchor=e", new JLabel("Meson Screen:"));
        add("+,.", mMesonCode);
        add("+,. anchor=e", new JLabel("Black Globe:"));
        add("+,.", mForceCode);
    }

    private void initLink()
    {
        mNuclearCode.setName(MapperComboBox.ONECHAR);
        BeanMapUtils.map(mNuclearCode, mShip, "screenNuclearCode");
        mMesonCode.setName(MapperComboBox.ONECHAR);
        BeanMapUtils.map(mMesonCode, mShip, "screenMesonCode");
        mForceCode.setName(MapperComboBox.ONECHAR);
        BeanMapUtils.map(mForceCode, mShip, "screenForceCode");
    }
}
