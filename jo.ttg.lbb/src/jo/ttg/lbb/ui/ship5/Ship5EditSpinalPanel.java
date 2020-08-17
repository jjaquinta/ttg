package jo.ttg.lbb.ui.ship5;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.map.BeanMapUtils;
import jo.util.ui.swing.utils.map.MapperComboBox;

public class Ship5EditSpinalPanel extends JPanel
{
    private Ship5Design mShip;
    
    private JComboBox<String> mMajorWeapon;
    private JComboBox<String> mMajorCode;

    public Ship5EditSpinalPanel(Ship5Design ship)
    {
        mShip = ship;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mMajorWeapon = new JComboBox<String>(Ship5Design.MAJOR_NAMES);
        mMajorCode = new JComboBox<String>(Ship5Design.MAJOR_CODES);
    }

    private void initLayout()
    {
        setLayout(new TableLayout());
        add("1,+", new JLabel("Major Weapon:"));
        add("+,.", mMajorWeapon);
        add("+,.", new JLabel("Major Code"));
        add("+,.", mMajorCode);
    }

    private void initLink()
    {
        mMajorWeapon.setName(MapperComboBox.ONECHAR);
        BeanMapUtils.map(mMajorWeapon, mShip, "majorWeapon");
        mMajorCode.setName(MapperComboBox.ONECHAR);
        BeanMapUtils.map(mMajorCode, mShip, "majorCode");
    }
}
