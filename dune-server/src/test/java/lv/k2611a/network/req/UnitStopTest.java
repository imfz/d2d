package lv.k2611a.network.req;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.TileType;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.unitgoals.Move;
import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.service.game.UserActionService;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testContext.xml"})
public class UnitStopTest {

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private UserActionService userActionService;

    @Before
    public void setup() {
        gameService.setTickCount(0);
    }

    @Test
    public void unitIsNotMovingAfterStop() {
        Map map = new Map(64,64, TileType.ROCK);
        gameService.setMap(map);

        Unit unit = new Unit();
        unit.setX(1);
        unit.setY(1);
        unit.setUnitType(UnitType.BATTLE_TANK);
        unit.setGoal(new Move(10,10));
        int id = map.addUnit(unit);

        UnitStop unitStop = new UnitStop();
        unitStop.setIds(new int[]{id});

        userActionService.registerAction(unitStop);

        for (int i = 0; i < 1000; i++) {
            gameService.tick();
        }

        assertEquals(1,unit.getX());
        assertEquals(1,unit.getY());

    }
}
