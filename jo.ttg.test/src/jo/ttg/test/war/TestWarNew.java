package jo.ttg.test.war;

import java.io.IOException;
import java.util.List;

import jo.util.utils.DebugUtils;
import ttg.war.beans.Game;
import ttg.war.beans.GameInst;
import ttg.war.beans.SideInst;
import ttg.war.logic.DefaultGame;
import ttg.war.logic.PhaseLogic;
import ttg.war.logic.SetupLogic;
import ttg.war.view.ai.ComputerPlayer;

public class TestWarNew
{
    private Game        mGameBase;
    private GameInst    mGame;
    
    public void run()
    {
        DebugUtils.debug = false;
        try
        {
            List<Game> games = DefaultGame.getInternalGames();
            for (Game game : games)
                testGame(game);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void testGame(Game game) throws IOException
    {
        mGameBase = game;
        for (int i = 0; i < mGameBase.getSides().size(); i++)
        {
            mGame = SetupLogic.newGame(mGameBase);
            List<SideInst> mSides = mGame.getSides();
            for (int j = 0; i < mSides.size(); i++)
            {
                SideInst side = mSides.get(i);
                if (j == i)
                {
                    side.setPlayer(new NullPlayer());
                }
                else
                    side.setPlayer(new ComputerPlayer(mGame, side, j));
            }
            PhaseLogic.start(mGame, 0);
        }
    }
    
    public static void main(String[] argv)
    {
        TestWarNew app = new TestWarNew();
        app.run();
    }
}
