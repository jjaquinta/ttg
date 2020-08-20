package jo.ttg.lbb.ui.ship5;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.ttg.lbb.data.ship5.Ship5Design.Ship5DesignLaunchTube;
import jo.ttg.lbb.data.ship5.Ship5Design.Ship5DesignSubCraft;
import jo.util.ui.swing.utils.ListenerUtils;

public class Ship5EditSubCraftPanel extends JPanel
{
    private Ship5Design mShip;
    
    private JTable mSubCraft;
    private JButton mSubCraftAdd;
    private JButton mSubCraftDel;
    private JTable mLaunchTubes;
    private JButton mLaunchTubesAdd;
    private JButton mLaunchTubesDel;

    public Ship5EditSubCraftPanel(Ship5Design ship)
    {
        mShip = ship;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mSubCraft = new JTable(new SubCraftTableModel(mShip));
        mSubCraftAdd = new JButton("+");
        mSubCraftDel = new JButton("-");
        mLaunchTubes = new JTable(new LaunchTubeTableModel(mShip));
        mLaunchTubesAdd = new JButton("+");
        mLaunchTubesDel = new JButton("-");
    }

    private void initLayout()
    {
        JPanel topButtons = new JPanel();
        topButtons.setLayout(new GridLayout(1, 2));
        topButtons.add(mSubCraftAdd);
        topButtons.add(mSubCraftDel);
        JPanel topTitle = new JPanel();
        topTitle.setLayout(new BorderLayout());
        topTitle.add("Center", new JLabel("SubCraft:"));
        topTitle.add("East", topButtons);
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.add("North", topTitle);
        top.add("Center", new JScrollPane(mSubCraft));
        
        JPanel botButtons = new JPanel();
        botButtons.setLayout(new GridLayout(1, 2));
        botButtons.add(mLaunchTubesAdd);
        botButtons.add(mLaunchTubesDel);
        JPanel botTitle = new JPanel();
        botTitle.setLayout(new BorderLayout());
        botTitle.add("Center", new JLabel("Launch Tubes:"));
        botTitle.add("East", botButtons);
        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());
        bot.add("North", botTitle);
        bot.add("Center", new JScrollPane(mLaunchTubes));
        
        setLayout(new GridLayout(2, 1));
        add(top);
        add(bot);
    }

    private void initLink()
    {
        //BeanMapUtils.map(mSubCraft, mShip, "subCraft");
        ListenerUtils.listen(mLaunchTubesAdd, (e) -> doLaunchTubesAdd());
        ListenerUtils.listen(mLaunchTubesDel, (e) -> doLaunchTubesDel());
        ListenerUtils.listen(mSubCraftAdd, (e) -> doSubCraftAdd());
        ListenerUtils.listen(mSubCraftDel, (e) -> doSubCraftDel());
    }
    
    private void doSubCraftAdd()
    {
        SubCraftAddDlg dlg = new SubCraftAddDlg((JFrame)SwingUtilities.getRoot(this), mShip);
        dlg.setVisible(true);
    }
    
    private void doSubCraftDel()
    {
        int viewRowIndex = mSubCraft.getSelectedRow();
        if (viewRowIndex < 0)
            return;
        int row = mSubCraft.convertRowIndexToModel(viewRowIndex);
        Ship5DesignSubCraft craft = mShip.getSubCraft().get(row);
        String txt = craft.getShipName()+" ("+craft.getHullTonnage()+"t)";
        if (craft.getQuantity() > 1)
            txt +=" x"+craft.getQuantity();
        int proceede = JOptionPane.showConfirmDialog(this, "Are you sure you want to get rid of "+txt+"?", 
                "Remove Craft Hanger", JOptionPane.YES_NO_OPTION);
        if (proceede != JOptionPane.YES_OPTION)
            return;
        mShip.getSubCraft().remove(row);
        mShip.fireMonotonicPropertyChange("subCraft", mShip.getSubCraft());
    }
    
    private void doLaunchTubesAdd()
    {
        LaunchTubeAddDlg dlg = new LaunchTubeAddDlg((JFrame)SwingUtilities.getRoot(this), mShip);
        dlg.setVisible(true);
    }
    
    private void doLaunchTubesDel()
    {
        int viewRowIndex = mLaunchTubes.getSelectedRow();
        if (viewRowIndex < 0)
            return;
        int row = mLaunchTubes.convertRowIndexToModel(viewRowIndex);
        Ship5DesignLaunchTube tube = mShip.getLaunchTubes().get(row);
        String txt = tube.getQuantity()+"t";
        if (tube.getQuantity() > 1)
            txt +=" x"+tube.getQuantity();
        int proceede = JOptionPane.showConfirmDialog(this, "Are you sure you want to get rid of "+txt+"?", 
                "Remove Launch Tube", JOptionPane.YES_NO_OPTION);
        if (proceede != JOptionPane.YES_OPTION)
            return;
        mShip.getLaunchTubes().remove(row);
        mShip.fireMonotonicPropertyChange("launchTubes", mShip.getLaunchTubes());
    }
}
