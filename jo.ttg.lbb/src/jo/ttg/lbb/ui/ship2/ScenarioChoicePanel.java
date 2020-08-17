package jo.ttg.lbb.ui.ship2;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Container;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.data.ship2.Scenario;
import jo.ttg.lbb.logic.ship2.CombatLogic;
import jo.ttg.lbb.logic.ship2.ScenarioLogic;
import jo.util.ui.swing.TableLayout;
import jo.util.utils.obj.StringUtils;
import jo.util.utils.xml.XMLUtils;

@SuppressWarnings("serial")
public class ScenarioChoicePanel extends Container
{
	private GamePanel		mGame;
	private List<Scenario> 	mScenarios;
	
	private Choice		mCannedScenario;
	private TextArea	mCustomScenario;
	private Label		mError;
	private Button		mSelect;
	
	public ScenarioChoicePanel(GamePanel game)
	{
		mGame = game;
		mScenarios = ScenarioLogic.getScenarios();
		
		mCannedScenario = new Choice();
		for (Scenario s : mScenarios)
			mCannedScenario.addItem(s.getName());
		mCustomScenario = new TextArea();
		mError = new Label();
		mSelect = new Button("Select");
		
		setLayout(new TableLayout());
		add("1,1 anchor=w", new Label("Pick builtin scenario:"));
		add("1,+ fill=h", mCannedScenario);
		add("1,+ anchor=w", new Label("  or paste in your own custom one:"));
		add("1,+ fill=hv", mCustomScenario);
		add("1,+ fill=h", mError);
		add("1,+", mSelect);
		
		mSelect.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				doSelect();
			}
		});
	}
	
	private void doSelect()
	{
		mError.setText("");
		String custom = mCustomScenario.getText();
		Scenario scenario = null;
		if (!StringUtils.isTrivial(custom))
		{
			Document doc = XMLUtils.readString(custom);
			if (doc == null)
			{
				mError.setText("Invalid XML");
				return;
			}
			Node s = XMLUtils.findFirstNode(doc, "scenarios/scenario");
			if (s == null)
			{
				mError.setText("Can't find scenario node");
				return;
			}
			scenario = ScenarioLogic.loadFromXML(s);
			if (scenario == null)
			{
				mError.setText("Badly formatted scenario");
				return;
			}
		}
		else
		{
			int idx = mCannedScenario.getSelectedIndex();
			if (idx < 0)
			{
				mError.setText("Select or paste in scenario, please.");
				return;
			}
			scenario = mScenarios.get(idx);
		}
		Combat combat = CombatLogic.newCombat(scenario.getID(), scenario.getName());
		mGame.setCombat(combat);
	}
}
