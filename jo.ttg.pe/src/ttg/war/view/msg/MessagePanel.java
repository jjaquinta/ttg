package ttg.war.view.msg;

import java.awt.CardLayout;

import javax.swing.JPanel;

import ttg.war.beans.PlayerMessage;
import ttg.war.view.WarPanel;

public class MessagePanel extends JPanel
{
	private WarPanel	mPanel;
	
	private CardLayout	mLayout;
	
	private JPanel				mBlank;
	private OrderPanel			mOrderMessage;
	private NewOwnerPanel		mNewOwnerMessage;
	private MoveErrorPanel		mMoveErrorMessage;
	private ShipMessagePanel	mShipMessage;
	private WorldMessagePanel	mWorldMessage;
	private SideMessagePanel	mSideMessage;
	private GameOverPanel		mGameOverMessage;
	private NewShipPanel		mNewShipMessage;
	private TextPanel			mTextMessage;
	
	/**
	 *
	 */

	public MessagePanel(WarPanel panel)
	{
		mPanel = panel;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mBlank = new JPanel();
		mOrderMessage = new OrderPanel(mPanel);
		mNewOwnerMessage = new NewOwnerPanel(mPanel);
		mMoveErrorMessage = new MoveErrorPanel(mPanel);
		mShipMessage = new ShipMessagePanel(mPanel);
		mWorldMessage = new WorldMessagePanel(mPanel);
		mSideMessage = new SideMessagePanel(mPanel);
		mGameOverMessage = new GameOverPanel(mPanel);
		mNewShipMessage = new NewShipPanel(mPanel);
		mTextMessage = new TextPanel(mPanel);

		mLayout = new CardLayout();
	}

	private void initLink()
	{
	}

	private void initLayout()
	{
		setLayout(mLayout);

		add("blank", mBlank);
		add("order", mOrderMessage);
		add("newOwner", mNewOwnerMessage);
		add("moveError", mMoveErrorMessage);
		add("ship", mShipMessage);
		add("world", mWorldMessage);
		add("side", mSideMessage);
		add("gameOver", mGameOverMessage);
		add("newShip", mNewShipMessage);
		add("text", mTextMessage);
	}
	
	public void setMessage(PlayerMessage msg)
	{
		switch (msg.getID())
		{
			case PlayerMessage.PLAYERORDER:
				mOrderMessage.setMessage(msg);
				mLayout.show(this, "order");
				break;
			case PlayerMessage.NEWOWNER:
				mNewOwnerMessage.setMessage(msg);
				mLayout.show(this, "newOwner");
				break;
			case PlayerMessage.CANTMOVE:
				mMoveErrorMessage.setMessage(msg);
				mLayout.show(this, "moveError");
				break;
			case PlayerMessage.SHIPDESTROYED:
			case PlayerMessage.SHIPMISSED:
			case PlayerMessage.SHIPDAMAGED:
			case PlayerMessage.CANTREPAIR:
			case PlayerMessage.DIDREPAIR:
				mShipMessage.setMessage(msg);
				mLayout.show(this, "ship");
				break;
			case PlayerMessage.COMBATSTART:
			case PlayerMessage.COMBATEND:
				mWorldMessage.setMessage(msg);
				mLayout.show(this, "world");
				break;
			case PlayerMessage.YOULOSE:
				mSideMessage.setMessage(msg);
				mLayout.show(this, "side");
				break;
			case PlayerMessage.GAMEOVER:
				mGameOverMessage.setMessage(msg);
				mLayout.show(this, "gameOver");
				break;
			case PlayerMessage.NEWSHIP:
				mNewShipMessage.setMessage(msg);
				mLayout.show(this, "newShip");
				break;
			case PlayerMessage.ENDOFTURN:
				mTextMessage.setMessage(msg);
				mLayout.show(this, "text");
				break;
			default:
				mLayout.show(this, "blank");
				break;
		}
	}
}
