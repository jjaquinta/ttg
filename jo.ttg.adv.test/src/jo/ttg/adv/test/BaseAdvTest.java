package jo.ttg.adv.test;

import org.junit.Before;

import ttg.logic.adv.GameLogic;

class BaseAdvTest
{

    @Before
    void setup()
    {
        GameLogic.init();
    }

}
