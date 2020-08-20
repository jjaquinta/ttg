package jo.ttg.lbb.ui.ship5;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.map.BeanMapUtils;

public class Ship5EditBaysPanel extends JPanel
{
    private Ship5Design mShip;
    
    private JSpinner mBays100Meson;
    private JSpinner mBays100Particle;
    private JSpinner mBays100Repulsor;
    private JSpinner mBays100Missile;
    private JSpinner mBays50Meson;
    private JSpinner mBays50Particle;
    private JSpinner mBays50Repulsor;
    private JSpinner mBays50Missile;
    private JSpinner mBays50Plasma;
    private JSpinner mBays50Fusion;

    public Ship5EditBaysPanel(Ship5Design ship)
    {
        mShip = ship;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mBays100Meson = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mBays100Particle = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mBays100Repulsor = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mBays100Missile = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mBays50Meson = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mBays50Particle = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mBays50Repulsor = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mBays50Missile = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mBays50Plasma = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mBays50Fusion = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
    }

    private void initLayout()
    {
        setLayout(new TableLayout("anchor=w,ipadw=4,ipadh=2"));
        add("1,+ anchor=e", new JLabel("100t Meson Bays:"));
        add("+,.", mBays100Meson);
        add("+,. anchor=e", new JLabel("100t Particle Bays:"));
        add("+,.", mBays100Particle);
        add("+,. anchor=e", new JLabel("100t Repulsor Bays:"));
        add("+,.", mBays100Repulsor);
        add("+,. anchor=e", new JLabel("100t Missile Bays:"));
        add("+,.", mBays100Missile);
        add("1,+ anchor=e", new JLabel("50t Meson Bays:"));
        add("+,.", mBays50Meson);
        add("+,. anchor=e", new JLabel("50t Particle Bays:"));
        add("+,.", mBays50Particle);
        add("+,. anchor=e", new JLabel("50t Repulsor Bays:"));
        add("+,.", mBays50Repulsor);
        add("+,. anchor=e", new JLabel("50t Missile Bays:"));
        add("+,.", mBays50Missile);
        add("1,+ anchor=e", new JLabel("50t Plasma Bays:"));
        add("+,.", mBays50Plasma);
        add("+,. anchor=e", new JLabel("50t Fusion Bays:"));
        add("+,.", mBays50Fusion);
    }

    private void initLink()
    {
        BeanMapUtils.map(mBays100Meson, mShip, "bays100Meson");
        BeanMapUtils.map(mBays100Particle, mShip, "bays100Particle");
        BeanMapUtils.map(mBays100Repulsor, mShip, "bays100Repulsor");
        BeanMapUtils.map(mBays100Missile, mShip, "bays100Missile");
        BeanMapUtils.map(mBays50Meson, mShip, "bays50Meson");
        BeanMapUtils.map(mBays50Particle, mShip, "bays50Particle");
        BeanMapUtils.map(mBays50Repulsor, mShip, "bays50Repulsor");
        BeanMapUtils.map(mBays50Missile, mShip, "bays50Missile");
        BeanMapUtils.map(mBays50Plasma, mShip, "bays50Plasma");
        BeanMapUtils.map(mBays50Fusion, mShip, "bays50Fusion");
    }
}
