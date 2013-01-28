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
public class AttackMoveTest {
    @Autowired
    private GameServiceImpl gameService;

    @Before
    public void setup() {
        gameService.setTickCount(0);
    }

    @Test
    public void thereCanBeOnlyOneTestNumberOne() {
        Map map = new Map(64,64);

        Unit killer = new Unit();
        killer.setUnitType(UnitType.TESTING_DUMMY_MINIGUN);
        killer.setGoal(new AttackMove(32,63));
        killer.setOwnerId(1);
        killer.setX(32);
        killer.setY(0);
        killer.setViewDirection(ViewDirection.BOTTOM);
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
    public void thereCanBeOnlyOneTestNumberTwo() {
        Map map = new Map(64,64);

        Unit killer = new Unit();
        killer.setUnitType(UnitType.TESTING_DUMMY_MINIGUN);
        killer.setGoal(new AttackMove(32,63));
        killer.setOwnerId(1);
        killer.setX(30);
        killer.setY(0);
        killer.setViewDirection(ViewDirection.BOTTOM);
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
    public void destroyingBuildingsWhilePassingBy() {
        Map map = new Map(64,64);

        Unit killer = new Unit();
        killer.setUnitType(UnitType.TESTING_DUMMY_MINIGUN);
        killer.setGoal(new AttackMove(32,63));
        killer.setOwnerId(1);
        killer.setX(32);
        killer.setY(0);
        killer.setViewDirection(ViewDirection.BOTTOM);
        map.addUnit(killer);

        // Place 6 factories near the edge of unit attack range.
        for (int coordY = 30; coordY <= 34; coordY+=2) {
            Building building = new Building();
            building.setType(BuildingType.FACTORY);
            building.setX(killer.getX() - killer.getUnitType().getAttackRange());
            building.setY(coordY);
            building.setOwnerId(2);
            map.addBuilding(building);
            building = new Building();
            building.setType(BuildingType.FACTORY);
            building.setX(killer.getX() - killer.getUnitType().getAttackRange() + 3);
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
    public void onlyEnemiesAreKilled() {
        Map map = new Map(64,64);

        Unit killer = new Unit();
        killer.setUnitType(UnitType.TESTING_DUMMY_MINIGUN);
        killer.setGoal(new AttackMove(32,63));
        killer.setOwnerId(1);
        killer.setX(32);
        killer.setY(0);
        killer.setViewDirection(ViewDirection.BOTTOM);
        map.addUnit(killer);

        // Place 3 allied and 3 enemy factories near the edge of unit attack range.
        for (int coordY = 30; coordY <= 34; coordY+=2) {
            Building building = new Building();
            building.setType(BuildingType.FACTORY);
            building.setX(killer.getX() - killer.getUnitType().getAttackRange());
            building.setY(coordY);
            building.setOwnerId(1);
            map.addBuilding(building);

            building = new Building();
            building.setType(BuildingType.FACTORY);
            building.setX(killer.getX() - killer.getUnitType().getAttackRange() + 3);
            building.setY(coordY);
            building.setOwnerId(2);
            map.addBuilding(building);
        }

        for (int coordX = 33; coordX<38; coordX++) {
            // Place 5x5 of allied units
            for (int coordY = 25; coordY<30; coordY++) {
                Unit ally = new Unit();
                ally.setUnitType(UnitType.TESTING_DUMMY_SLOW);
                ally.setGoal(new Guard());
                ally.setOwnerId(1);
                ally.setX(coordX);
                ally.setY(coordY);
                map.addUnit(ally);
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

    @Test
    public void totalAnnihilationOfUnitsOnPath() {
        int mapSize = 32;
        int maxCoord = mapSize-1;
        Map map = new Map(mapSize,mapSize);


        Unit killer = new Unit();
        killer.setUnitType(UnitType.TESTING_DUMMY_MINIGUN);
        killer.setGoal(new AttackMove(0,maxCoord));
        killer.setOwnerId(1);
        killer.setX(0);
        killer.setY(0);
        killer.setViewDirection(ViewDirection.BOTTOM);
        map.addUnit(killer);
        int counter = 0;

        // Fill map with enemy units. Only these that are outside of maximum attack range should survive
        for (int coordX = 0; coordX < mapSize; coordX++) {
            for (int coordY = 0; coordY < mapSize; coordY++) {
                if (coordX == killer.getX() && (coordY == 0 || coordY == maxCoord))
                    continue;
                Unit victim = new Unit();
                victim.setUnitType(UnitType.TESTING_DUMMY_SLOW);
                victim.setGoal(new Guard());
                victim.setOwnerId(2);
                victim.setX(coordX);
                victim.setY(coordY);
                map.addUnit(victim);
                counter++;
            }
        }

        gameService.setMap(map);
        gameService.tick();

        assertEquals(mapSize * mapSize - 1,map.getUnits().size());

        // should be enough ticks to destroy the other tanks
        for (int i = 0; i < 15000; i++) {
            gameService.tick();
        }
        int remainingUnitCount = (maxCoord - killer.getUnitType().getAttackRange()) * mapSize + 1;
        assertEquals(remainingUnitCount,map.getUnits().size());
    }
}
