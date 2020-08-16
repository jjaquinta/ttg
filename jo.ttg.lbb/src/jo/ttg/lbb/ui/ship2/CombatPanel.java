package jo.ttg.lbb.ui.ship2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.logic.ship2.CombatLogic;
import jo.ttg.lbb.logic.ship2.PhaseLogic;
import jo.ttg.lbb.ui.util.TableLayout;

@SuppressWarnings("serial")
public class CombatPanel extends Container
{
	private GamePanel			mGame;
    private Combat              mCombat;

    private Label               mPhase;
    private Label               mSide;
    private Button              mNext;
    private Button              mDone;
    private CombatFieldCanvas   mCanvas;
    private MessagePanel		mMessages;

    public CombatPanel(GamePanel game)
    {
		mGame = game;

		mPhase = new Label();
        mSide = new Label();
        mNext = new Button("Next");
        mDone = new Button("Done");
        mCanvas = new CombatFieldCanvas();
        mMessages = new MessagePanel();
    
        setLayout(new BorderLayout());
        Container buttonBar = new Container();
        buttonBar.setLayout(new TableLayout());
        buttonBar.add("1,1 fill=none", new Label("Phase:"));
        buttonBar.add("+,. fill=h", mPhase);
        buttonBar.add("+,. fill=none", new Label("Side:"));
        buttonBar.add("+,. fill=h", mSide);
        buttonBar.add("+,. fill=none", mNext);
        buttonBar.add("+,. fill=none", mDone);
        add("North", buttonBar);
        add("Center", mCanvas);
        add("South", mMessages);
        
        mNext.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doNext();
            }
        });
        mDone.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doDone();
            }
        });
        addComponentListener(new ComponentListener() {			
			@Override
			public void componentShown(ComponentEvent e)
			{
				mCanvas.resetTransform();
			}
			@Override
			public void componentResized(ComponentEvent e)
			{
				mCanvas.resetTransform();
			}
			@Override
			public void componentMoved(ComponentEvent e)
			{
			}
			@Override
			public void componentHidden(ComponentEvent e)
			{
			}
		});
    }

    protected void doNext()
    {
        PhaseLogic.nextPhase(mCombat);
        updateClient();
    }

    protected void doDone()
    {
    	mGame.setCombat(null);
    }
    
    private void updateClient()
    {
    	if (mCombat == null)
    		return;
        mPhase.setText(CombatLogic.getPhaseName(mCombat));
        mSide.setText(CombatLogic.getSide(mCombat).getName());
        doLayout();
        mCanvas.resetTransform();
    }
    
    public Combat getCombat()
    {
        return mCombat;
    }

    public void setCombat(Combat combat)
    {
        mCombat = combat;
        mCanvas.setCombat(mCombat);
        mMessages.setCombat(mCombat);
        updateClient();
    }
}
