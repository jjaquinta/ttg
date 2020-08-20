package jo.ttg.lbb.ui.ship5;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jo.ttg.lbb.data.ship5.Ship5Stats;
import jo.util.ui.swing.TableLayout;

public class Ship5StatsPanel extends JPanel implements PropertyChangeListener
{
    private Ship5Stats               mShip;

    private JList<String> mErrors;
    private JTable mSpace;
    private JTable mCost;
    private JTable mEnergy;
    private JTable mFuel;
    private JTable mCrew;

    public Ship5StatsPanel(Ship5Stats ship)
    {
        mShip = ship;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mErrors = new JList<>(new DefaultListModel<>());
        mSpace = new JTable(new NumStringTableModel(new String[] { "Tons", "Item" }, mShip.getSpaceUsage()));
        mCost = new JTable(new NumStringTableModel(new String[] { "Cost", "Item" }, mShip.getCostUsage()));
        mEnergy = new JTable(new NumStringTableModel(new String[] { "Energy", "Item" }, mShip.getEnergyUsage()));
        mFuel = new JTable(new NumStringTableModel(new String[] { "Tons", "Item" }, mShip.getFuelUsage()));
        mCrew = new JTable(new NumStringTableModel(new String[] { "Tons", "Item" }, mShip.getCrewUsage()));
    }

    private void initLayout()
    {
        setLayout(new TableLayout());
        add("1,+", new JLabel("Errors"));
        add("1,+ fill=hv", new JScrollPane(mErrors));
        add("1,+", new JLabel("Space Used"));
        add("1,+ fill=hv", new JScrollPane(mSpace));
        add("1,+", new JLabel("Costs"));
        add("1,+ fill=hv", new JScrollPane(mCost));
        add("1,+", new JLabel("Energy Used"));
        add("1,+ fill=hv", new JScrollPane(mEnergy));
        add("1,+", new JLabel("Fuel Allocations"));
        add("1,+ fill=hv", new JScrollPane(mFuel));
        add("1,+", new JLabel("Crew Required"));
        add("1,+ fill=hv", new JScrollPane(mCrew));
    }

    private void initLink()
    {
        mShip.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        String prop = evt.getPropertyName();
        if (prop.equals("errors"))
        {
            DefaultListModel<String> lm = (DefaultListModel<String>)(mErrors.getModel());
            lm.removeAllElements();
            for (String err : mShip.getErrors())
                lm.addElement(err);
        }
        else if (prop.equals("spaceUsage"))
            ((NumStringTableModel)(mSpace.getModel())).setData(mShip.getSpaceUsage());
        else if (prop.equals("costUsage"))
            ((NumStringTableModel)(mCost.getModel())).setData(mShip.getCostUsage());
        else if (prop.equals("energyUsage"))
            ((NumStringTableModel)(mEnergy.getModel())).setData(mShip.getEnergyUsage());
        else if (prop.equals("fuelUsage"))
            ((NumStringTableModel)(mFuel.getModel())).setData(mShip.getFuelUsage());
        else if (prop.equals("crew"))
            ((NumStringTableModel)(mCrew.getModel())).setData(mShip.getCrewUsage());        
    }
}
