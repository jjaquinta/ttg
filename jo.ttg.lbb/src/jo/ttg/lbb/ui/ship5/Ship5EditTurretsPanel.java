package jo.ttg.lbb.ui.ship5;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.map.BeanMapUtils;

public class Ship5EditTurretsPanel extends JPanel
{
    private Ship5Design mShip;
    
    private JSpinner mTurretMissile;
    private JSpinner mTurretBeamLaser;
    private JSpinner mTurretPulseLaser;
    private JSpinner mTurretPlasmaGun;
    private JSpinner mTurretFusionGun;
    private JSpinner mTurretSandcaster;
    private JSpinner mTurretParticle;
    private JSpinner mBarbetteParticle;

    public Ship5EditTurretsPanel(Ship5Design ship)
    {
        mShip = ship;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mTurretMissile = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mTurretBeamLaser = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mTurretPulseLaser = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mTurretPlasmaGun = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mTurretFusionGun = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mTurretSandcaster = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mTurretParticle = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mBarbetteParticle = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
    }

    private void initLayout()
    {
        setLayout(new TableLayout("anchor=w,ipadw=4,ipadh=2"));
        add("1,+ anchor=e", new JLabel("Missile Turrets:"));
        add("+,.", mTurretMissile);
        add("+,. anchor=e", new JLabel("Beam Laser Turrets:"));
        add("+,.", mTurretBeamLaser);
        add("+,. anchor=e", new JLabel("Pulse Laser Turrets:"));
        add("+,.", mTurretPulseLaser);
        add("+,. anchor=e", new JLabel("Plasma Gun Turrets:"));
        add("+,.", mTurretPlasmaGun);
        add("1,+ anchor=e", new JLabel("Fusion Gun Turrets:"));
        add("+,.", mTurretFusionGun);
        add("+,. anchor=e", new JLabel("Sandcaster Turrets:"));
        add("+,.", mTurretSandcaster);
        add("+,. anchor=e", new JLabel("Particle Turrets:"));
        add("+,.", mTurretParticle);
        add("+,. anchor=e", new JLabel("Particle Barbettes:"));
        add("+,.", mBarbetteParticle);
    }

    private void initLink()
    {
        BeanMapUtils.map(mTurretMissile, mShip, "turretMissile");
        BeanMapUtils.map(mTurretBeamLaser, mShip, "turretBeamLaser");
        BeanMapUtils.map(mTurretPulseLaser, mShip, "turretPulseLaser");
        BeanMapUtils.map(mTurretPlasmaGun, mShip, "turretPlasmaGun");
        BeanMapUtils.map(mTurretFusionGun, mShip, "turretFusionGun");
        BeanMapUtils.map(mTurretSandcaster, mShip, "turretSandcaster");
        BeanMapUtils.map(mTurretParticle, mShip, "turretParticle");
        BeanMapUtils.map(mBarbetteParticle, mShip, "barbetteParticle");
    }
}
