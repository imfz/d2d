package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.*;
import lv.k2611a.service.game.GameServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testContext.xml"})
public class GuardTest {
    @Autowired
    private GameServiceImpl gameService;

    @Before
    public void setup() {
        gameService.setTickCount(0);
    }

    @Test
    public void unitDestroysUnitsStatic() {
        Map map = new Map(64,64);

        Unit killer = new Unit();
        killer.setUnitType(UnitType.TESTING_DUMMY_MINIGUN);
        killer.setGoal(new Guard());
        killer.setOwnerId(1);
        killer.setX(32);
        killer.setY(32);
        map.addUnit(killer);

        // Place 5x5 of units to be killed
        for (int coordX = 25; coordX<30; coordX++) {
            for (int coordY = 25; coordY<30; coordY++) {
                Unit victim = new Unit();
                victim.setUnitType(UnitType.TESTING_DUMMY_SLOW);
                victim.setGoal(new Guard());
                victim.setOwnerId(2);
                victim.setX(coordX);
                victim.setY(coordY);
                map.addUnit(victim);
            }
        }

        gameService.setMap(map);
        gameService.tick();

        assertEquals(26,map.getUnits().size());

        // should be enough ticks to destroy the other tanks
        for (int i = 0; i < 5000; i++) {
            gameService.tick();
        }
        assertEquals(1,map.getUnits().size());
    }

    @Test
    public void unitDestroysUnitsMoving() {
        Map map = new Map(64,64);

        Unit killer = new Unit();
        killer.setUnitType(UnitType.TESTING_DUMMY_MINIGUN);
        killer.setGoal(new Guard());
        killer.setOwnerId(1);
        killer.setX(30);
        killer.setY(30);
        map.addUnit(killer);

        // Place 6 units to be killed
        for (int coordX = 27; coordX<30; coordX++) {
                Unit victim = new Unit();
                victim.setUnitType(UnitType.TESTING_DUMMY_SLOW);
                victim.setGoal(new Move(coordX,63));
                victim.setOwnerId(2);
                victim.setX(coordX);
                victim.setY(0);
                map.addUnit(victim);
        }
        for (int coordX = 31; coordX<34; coordX++) {
            Unit victim = new Unit();
            victim.setUnitType(UnitType.TESTING_DUMMY_SLOW);
            victim.setGoal(new Move(coordX,63));
            victim.setOwnerId(2);
            victim.setX(coordX);
            victim.setY(0);
            map.addUnit(victim);
        }

        gameService.setMap(map);
        gameService.tick();

        assertEquals(7,map.getUnits().size());

        // should be enough ticks to destroy the other tanks
        for (int i = 0; i < 5000; i++) {
            gameService.tick();
        }
        assertEquals(1,map.getUnits().size());
    }

    @Test
    public void unitDestroysBuildingsStatic() {
        Map map = new Map(64,64);

        Unit killer = new Unit();
        killer.setUnitType(UnitType.TESTING_DUMMY_MINIGUN);
        killer.setGoal(new Guard());
        killer.setOwnerId(1);
        killer.setX(32);
        killer.setY(32);
        map.addUnit(killer);

        // Place 6 factories near the edge of unit attack range.
        for (int coordY = killer.getY()-2; coordY <=killer.getY()+2; coordY+=2) {
            Building building = new Building();
            building.setType(BuildingType.FACTORY);
            building.setX(killer.getX() - (int)killer.getUnitType().getAttackRange());
            building.setY(coordY);
            building.setOwnerId(2);
            map.addBuilding(building);
            building = new Building();
            building.setType(BuildingType.FACTORY);
            building.setX(killer.getX() - (int)killer.getUnitType().getAttackRange() + 3);
            building.setY(coordY);
            building.setOwnerId(2);
            map.addBuilding(building);
        }

        gameService.setMap(map);
        gameService.tick();

        assertEquals(1,map.getUnits().size());
        assertEquals(6,map.getBuildings().size());

        // should be enough ticks to destroy the other tanks and buildings
        for (int i = 0; i < 5000; i++) {
            gameService.tick();
        }
        assertEquals(1,map.getUnits().size());
        assertEquals(0,map.getBuildings().size());
    }

    @Test
    public void unitMissesFastMovingUnits() {
        Map map = new Map(64,64);

        Unit killer = new Unit();
        killer.setUnitType(UnitType.TESTING_DUMMY_MINIGUN);
        killer.setGoal(new Guard());
        killer.setOwnerId(1);
        killer.setX(32);
        killer.setY(32);
        map.addUnit(killer);

        // Place 30 units that should never get hit
        for (int coordX = 2; coordX<32; coordX++) {
                Unit victim = new Unit();
                victim.setUnitType(UnitType.TESTING_DUMMY_FAST);
                victim.setGoal(new Move(coordX, 63));
                victim.setOwnerId(2);
                victim.setX(coordX);
                victim.setY(0);
                map.addUnit(victim);
        }
        // Place 30 units that should never get hit
        for (int coordX = 33; coordX<63; coordX++) {
            Unit victim = new Unit();
            victim.setUnitType(UnitType.TESTING_DUMMY_FAST);
            victim.setGoal(new Move(coordX, 63));
            victim.setOwnerId(2);
            victim.setX(coordX);
            victim.setY(15);
            map.addUnit(victim);
        }

        gameService.setMap(map);
        gameService.tick();

        assertEquals(61,map.getUnits().size());

        // should be enough ticks to destroy the other tanks
        for (int i = 0; i < 5000; i++) {
            gameService.tick();
        }
        assertEquals(61,map.getUnits().size());
    }

    @Test
    public void unitAttacksOnlyEnemies() {
        Map map = new Map(64,64);

        Unit killer = new Unit();
        killer.setUnitType(UnitType.TESTING_DUMMY_MINIGUN);
        killer.setGoal(new Guard());
        killer.setOwnerId(1);
        killer.setX(32);
        killer.setY(32);
        map.addUnit(killer);

        // Place 3 allied and 3 enemy factories near the edge of unit attack range.
        for (int coordY = killer.getY()-2; coordY <=killer.getY()+2; coordY+=2) {
            Building building = new Building();
            building.setType(BuildingType.FACTORY);
            building.setX(killer.getX() - (int)killer.getUnitType().getAttackRange());
            building.setY(coordY);
            building.setOwnerId(1);
            map.addBuilding(building);
            building = new Building();
            building.setType(BuildingType.FACTORY);
            building.setX(killer.getX() - (int)killer.getUnitType().getAttackRange() + 3);
            building.setY(coordY);
            building.setOwnerId(2);
            map.addBuilding(building);
        }

        for (int coordX = 33; coordX<38; coordX++) {
            // Place 5x5 of allied units
            for (int coordY = 25; coordY<30; coordY++) {
                Unit victim = new Unit();
                victim.setUnitType(UnitType.TESTING_DUMMY_SLOW);
                victim.setGoal(new Guard());
                victim.setOwnerId(1);
                victim.setX(coordX);
                victim.setY(coordY);
                map.addUnit(victim);
            }
            // Place 5x5 of units to be killed
            for (int coordY = 33; coordY<38; coordY++) {
                Unit victim = new Unit();
                victim.setUnitType(UnitType.TESTING_DUMMY_SLOW);
                victim.setGoal(new Guard());
                victim.setOwnerId(2);
                victim.setX(coordX);
                victim.setY(coordY);
                map.addUnit(victim);
            }
        }

        gameService.setMap(map);
        gameService.tick();

        assertEquals(51,map.getUnits().size());
        assertEquals(6,map.getBuildings().size());

        // should be enough ticks to destroy the other tanks
        for (int i = 0; i < 5000; i++) {
            gameService.tick();
        }
        assertEquals(26,map.getUnits().size());
        assertEquals(3,map.getBuildings().size());
    }
}
