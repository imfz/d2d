package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.*;
import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.Point;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testContext.xml"})
public class AStarPathfindingTest {
    @Autowired
    private GameServiceImpl gameService;
    private int extraTicksInCaseSomethingGoesWrong = 5;

    @Before
    public void setup() {
        gameService.setTickCount(0);
    }

    // These tests should have required ticks lowered at some point in order to test AStar.
    // At best, one-valid-route-cone tests can be added with asserts checking each tick,
    // to check if we are inside the optimal route diapason.

    @Test
    public void harvesterRouteToRefineryWithoutObstacles() {
        Map map = new Map(32,32);

        // The 3 refineries are located X tiles from 3 harvesters, no obstacles.
        // Objective is for all 3 harvesters to reach the closest refinery in pre-defined number of ticks.

        // Set the numbers big to not let the harvest unload faster
        Harvest.TICKS_FOR_FULL = 100000;
        ReturnToBase.TICKS_COLLECTING_UNLOADED_PER_TICK = 1;

        Building building = new Building();
        building.setType(BuildingType.REFINERY);
        building.setX(1);
        building.setY(3);
        building.setOwnerId(1);
        map.addBuilding(building);

        building = new Building();
        building.setType(BuildingType.REFINERY);
        building.setX(22);
        building.setY(4);
        building.setOwnerId(1);
        map.addBuilding(building);

        building = new Building();
        building.setType(BuildingType.REFINERY);
        building.setX(21);
        building.setY(17);
        building.setOwnerId(1);
        map.addBuilding(building);

        Unit harvester1 = new Unit();
        harvester1.setUnitType(UnitType.HARVESTER);
        harvester1.setTicksCollectingSpice(Harvest.TICKS_FOR_FULL);
        harvester1.setGoal(new ReturnToBase());
        harvester1.setOwnerId(1);
        harvester1.setX(14);
        harvester1.setY(8);
        map.addUnit(harvester1);

        Unit harvester2 = new Unit();
        harvester2.setUnitType(UnitType.HARVESTER);
        harvester2.setTicksCollectingSpice(Harvest.TICKS_FOR_FULL);
        harvester2.setGoal(new ReturnToBase());
        harvester2.setOwnerId(1);
        harvester2.setX(6);
        harvester2.setY(13);
        map.addUnit(harvester2);

        Unit harvester3 = new Unit();
        harvester3.setUnitType(UnitType.HARVESTER);
        harvester3.setTicksCollectingSpice(Harvest.TICKS_FOR_FULL);
        harvester3.setGoal(new ReturnToBase());
        harvester3.setOwnerId(1);
        harvester3.setX(13);
        harvester3.setY(19);
        map.addUnit(harvester3);

        gameService.setMap(map);

        int ticksToFinish = UnitType.HARVESTER.getSpeed() * 10 + extraTicksInCaseSomethingGoesWrong;

        // should be enough ticks to return to base
        for (int i = 0; i < ticksToFinish; i++) {
            gameService.tick();
        }
        Point entranceToRefinery1 = new Point(23, 5);
        Point entranceToRefinery2 = new Point(2, 4);
        Point entranceToRefinery3 = new Point(22, 18);
        assertEquals(entranceToRefinery1, harvester1.getPoint());
        assertEquals(entranceToRefinery2, harvester2.getPoint());
        assertEquals(entranceToRefinery3, harvester3.getPoint());
    }

    @Test
    public void harvesterRouteToRefineryWithObstaclesPaths() {
        Map map = new Map(64,64);

        // The refinery is separated from harvester with some tanks and buildings on the side of the route.
        // The middle of the route has 2 paths of X, X+1 tile route.
        // Objective is to reach the refinery in under X+2 ticks by using either of these routes.
        // Other units are there just for fun.

        // Set the numbers big to not let the harvest unload faster
        Harvest.TICKS_FOR_FULL = 100000;
        ReturnToBase.TICKS_COLLECTING_UNLOADED_PER_TICK = 1;

        Building building = new Building();
        building.setType(BuildingType.REFINERY);
        building.setX(3);
        building.setY(3);
        building.setOwnerId(1);
        map.addBuilding(building);

        building = new Building();
        building.setType(BuildingType.FACTORY);
        building.setX(1);
        building.setY(11);
        building.setOwnerId(1);
        map.addBuilding(building);

        building = new Building();
        building.setType(BuildingType.FACTORY);
        building.setX(2);
        building.setY(8);
        building.setOwnerId(1);
        map.addBuilding(building);

        building = new Building();
        building.setType(BuildingType.FACTORY);
        building.setX(3);
        building.setY(6);
        building.setOwnerId(1);
        map.addBuilding(building);

        building = new Building();
        building.setType(BuildingType.FACTORY);
        building.setX(5);
        building.setY(12);
        building.setOwnerId(1);
        map.addBuilding(building);

        building = new Building();
        building.setType(BuildingType.FACTORY);
        building.setX(7);
        building.setY(5);
        building.setOwnerId(1);
        map.addBuilding(building);

        building = new Building();
        building.setType(BuildingType.FACTORY);
        building.setX(9);
        building.setY(8);
        building.setOwnerId(1);
        map.addBuilding(building);

        Unit harvester = new Unit();
        harvester.setUnitType(UnitType.HARVESTER);
        harvester.setTicksCollectingSpice(Harvest.TICKS_FOR_FULL);
        harvester.setGoal(new ReturnToBase());
        harvester.setOwnerId(1);
        harvester.setX(1);
        harvester.setY(1);
        map.addUnit(harvester);

        Unit harvesterTravel = new Unit();
        harvesterTravel.setUnitType(UnitType.HARVESTER);
        harvesterTravel.setGoal(new Move(3,13));
        harvesterTravel.setOwnerId(1);
        harvesterTravel.setX(3);
        harvesterTravel.setY(21);
        map.addUnit(harvesterTravel);

        harvesterTravel = new Unit();
        harvesterTravel.setUnitType(UnitType.HARVESTER);
        harvesterTravel.setGoal(new Move(8,13));
        harvesterTravel.setOwnerId(1);
        harvesterTravel.setX(9);
        harvesterTravel.setY(21);
        map.addUnit(harvesterTravel);

        harvesterTravel = new Unit();
        harvesterTravel.setUnitType(UnitType.HARVESTER);
        harvesterTravel.setGoal(new Move(4,21));
        harvesterTravel.setOwnerId(1);
        harvesterTravel.setX(4);
        harvesterTravel.setY(16);
        map.addUnit(harvesterTravel);

        harvesterTravel = new Unit();
        harvesterTravel.setUnitType(UnitType.HARVESTER);
        harvesterTravel.setGoal(new Move(8,21));
        harvesterTravel.setOwnerId(1);
        harvesterTravel.setX(8);
        harvesterTravel.setY(16);
        map.addUnit(harvesterTravel);

        gameService.setMap(map);

        int ticksToFinish = UnitType.HARVESTER.getSpeed() * 18 + extraTicksInCaseSomethingGoesWrong;

        // should be enough ticks to return to base
        for (int i = 0; i < ticksToFinish; i++) {
            gameService.tick();
        }

        Point entranceToSecondRefinery = new Point(4, 4);
        assertEquals(entranceToSecondRefinery, harvester.getPoint());
    }
}