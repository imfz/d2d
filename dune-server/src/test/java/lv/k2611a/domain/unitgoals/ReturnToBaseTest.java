package lv.k2611a.domain.unitgoals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.BuildingType;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.service.GameServiceImpl;
import lv.k2611a.util.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testContext.xml"})
public class ReturnToBaseTest {

    @Autowired
    private GameServiceImpl gameService;

    @Before
    public void setup() {
        gameService.setTickCount(0);
    }

    @Test
    public void twoHarvestersCannotReturnToSameRefinery() {
        Map map = new Map(64,64);

        // set the numbers big to no let the harvest unload faster
        Harvest.TICKS_FOR_FULL = 100000;
        ReturnToBase.TICKS_COLLECTING_UNLOADED_PER_TICK = 1;

        Building building = new Building();
        building.setType(BuildingType.REFINERY);
        building.setX(1);
        building.setY(1);
        building.setId(1);
        building.setOwnerId(1);
        map.getBuildings().add(building);

        Unit harvester = new Unit();
        harvester.setId(2);
        harvester.setUnitType(UnitType.HARVESTER);
        harvester.setTicksCollectingSpice(Harvest.TICKS_FOR_FULL);
        harvester.setGoal(new ReturnToBase());
        harvester.setOwnerId(1);
        harvester.setX(5);
        harvester.setY(5);
        map.getUnits().add(harvester);

        Unit harvester2 = new Unit();
        harvester2.setId(3);
        harvester2.setUnitType(UnitType.HARVESTER);
        harvester2.setTicksCollectingSpice(Harvest.TICKS_FOR_FULL);
        harvester2.setGoal(new ReturnToBase());
        harvester2.setOwnerId(1);
        harvester2.setX(6);
        harvester2.setY(5);
        map.getUnits().add(harvester2);

        gameService.setMap(map);

        // should be enough ticks to return to base
        for (int i = 0; i < 1000; i++) {
            gameService.tick();
        }

        assertFalse(harvester.getPoint().equals(harvester2.getPoint()));


    }

    @Test
    public void harvesterLooksForAnotherRefineryIfNearestIsBlocked() {
        Map map = new Map(64,64);

        // set the numbers big to no let the harvest unload faster
        Harvest.TICKS_FOR_FULL = 100000;
        ReturnToBase.TICKS_COLLECTING_UNLOADED_PER_TICK = 1;


        // first refinery is near the harvester but its entrance is blocked by the factory
        Building building = new Building();
        building.setType(BuildingType.REFINERY);
        building.setX(1);
        building.setY(3);
        building.setId(1);
        building.setOwnerId(1);
        map.getBuildings().add(building);

        building = new Building();
        building.setType(BuildingType.FACTORY);
        building.setX(1);
        building.setY(5);
        building.setId(2);
        building.setOwnerId(1);
        map.getBuildings().add(building);

        building = new Building();
        building.setType(BuildingType.REFINERY);
        building.setX(1);
        building.setY(10);
        building.setId(3);
        building.setOwnerId(1);
        map.getBuildings().add(building);

        Unit harvester = new Unit();
        harvester.setId(4);
        harvester.setUnitType(UnitType.HARVESTER);
        harvester.setTicksCollectingSpice(Harvest.TICKS_FOR_FULL);
        harvester.setGoal(new ReturnToBase());
        harvester.setOwnerId(1);
        harvester.setX(1);
        harvester.setY(1);
        map.getUnits().add(harvester);

        gameService.setMap(map);

        // should be enough ticks to return to base
        for (int i = 0; i < 1000; i++) {
            gameService.tick();
        }

        Point entranceToSecondRefinery = new Point(2, 11);
        assertEquals(entranceToSecondRefinery, harvester.getPoint());


    }
}
