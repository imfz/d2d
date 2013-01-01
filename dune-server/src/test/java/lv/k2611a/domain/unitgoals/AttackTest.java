package lv.k2611a.domain.unitgoals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.BuildingType;
import lv.k2611a.domain.Entity;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.service.game.GameServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testContext.xml"})
public class AttackTest {
    @Autowired
    private GameServiceImpl gameService;

    @Before
    public void setup() {
        gameService.setTickCount(0);
    }

    @Test
    public void tankDestroyPowerplant() {
        Map map = new Map(64,64);

        Building building = new Building();
        building.setType(BuildingType.POWERPLANT);
        building.setX(1);
        building.setY(1);
        building.setOwnerId(1);
        int buildingId = map.addBuilding(building);

        Unit tank = new Unit();
        tank.setUnitType(UnitType.BATTLE_TANK);
        tank.setGoal(new Attack(Entity.BUILDING,buildingId));
        tank.setOwnerId(2);
        tank.setX(20);
        tank.setY(20);
        map.addUnit(tank);

        gameService.setMap(map);
        gameService.tick();

        assertFalse(map.getPlayerById(1).hasLost());

        // should be enough ticks to destroy powerplant
        for (int i = 0; i < 5000; i++) {
            gameService.tick();
        }

        assertNull(map.getBuilding(buildingId));
        assertEquals(0,map.getBullets().size());
        assertTrue(map.getPlayerById(1).hasLost());
    }

    @Test
    public void unitsAttackEachOther() {
        Map map = new Map(64,64);


        Unit tank1 = new Unit();
        tank1.setUnitType(UnitType.BATTLE_TANK);
        tank1.setOwnerId(2);
        tank1.setX(20);
        tank1.setY(20);
        int tank1Id = map.addUnit(tank1);

        Unit tank2 = new Unit();
        tank2.setUnitType(UnitType.BATTLE_TANK);
        tank2.setOwnerId(2);
        tank2.setX(20);
        tank2.setY(20);
        int tank2Id = map.addUnit(tank2);

        tank2.setGoal(new Attack(Entity.UNIT, tank1Id));
        tank1.setGoal(new Attack(Entity.UNIT, tank2Id));

        gameService.setMap(map);
        gameService.tick();

        assertEquals(2,map.getUnits().size());

        // should be enough ticks to destroy powerplant
        for (int i = 0; i < 5000; i++) {
            gameService.tick();
        }

        assertEquals(1,map.getUnits().size());
    }
}
