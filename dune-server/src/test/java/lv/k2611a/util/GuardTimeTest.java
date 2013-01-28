package lv.k2611a.util;


import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.unitgoals.Guard;
import lv.k2611a.service.game.GameServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testContext.xml"})
public class GuardTimeTest {
    @Autowired
    private GameServiceImpl gameService;

    @Before
    public void setup() {
        gameService.setTickCount(0);
    }

    @Test
    public void testGuardCalculationTime() {
        final int mapSize = 100;
        int unitCount = 0;
        Map map = new Map(mapSize,mapSize);
        for (int coordX = 0; coordX<mapSize; coordX++) {
            for (int coordY = 0; coordY<mapSize; coordY++) {
                Unit unit = new Unit();
                unit.setUnitType(UnitType.BATTLE_TANK);
                unit.setOwnerId((coordX + coordY) % 2);
                unit.setGoal(new Guard());
                unit.setX(coordX);
                unit.setY(coordY);
                map.addUnit(unit);
                unitCount++;
            }
        }
        gameService.setMap(map);
        long startTime = System.currentTimeMillis();
        gameService.tick();
        long endTime = System.currentTimeMillis();
        long delta = endTime - startTime;
        System.out.println("Tick 1 for " + unitCount + " units on " + mapSize + "x" + mapSize + " map took " + delta + " ms. Average time is " + (double)delta / unitCount);
        for (int tickCount = 2; tickCount <10; tickCount++) {
            startTime = System.currentTimeMillis();
            gameService.tick();
            endTime = System.currentTimeMillis();
            delta = endTime - startTime;
            System.out.println("Tick " + tickCount + " for " + unitCount + " units on " + mapSize + "x" + mapSize + " map took " + delta + " ms. Average time is " + (double)delta / unitCount);
        }
    }
}
