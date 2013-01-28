package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lv.k2611a.service.game.GameServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testContext.xml"})
public class UnitAttackTest {
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
        tank.setGoal(new Attack(new Target(Entity.BUILDING, building.getId(), building.getPoint())));
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

        tank2.setGoal(new Attack(new Target(Entity.UNIT, map.getUnit(tank1Id).getId(), map.getUnit(tank1Id).getPoint())));
        tank1.setGoal(new Attack(new Target(Entity.UNIT, map.getUnit(tank2Id).getId(), map.getUnit(tank2Id).getPoint())));

        gameService.setMap(map);
        gameService.tick();

        assertEquals(2,map.getUnits().size());

        // should be enough ticks to destroy the other tank
        for (int i = 0; i < 5000; i++) {
            gameService.tick();
        }

        assertEquals(1,map.getUnits().size());
    }

    // A tank starts in attack range of enemy tanks that attempt to kill it.
    // Objective is for the following tanks to pursuit and destroy the opponent.
    @Test
    public void unitPursuitCloseDistance() {
        Map map = new Map(64,64);

        Unit tank1 = new Unit();
        tank1.setUnitType(UnitType.BATTLE_TANK);
        tank1.setOwnerId(1);
        tank1.setGoal(new Move(60, 4));
        tank1.setX(4);
        tank1.setY(4);
        int tankVictim = map.addUnit(tank1);

        Unit tank2 = new Unit();
        tank2.setUnitType(UnitType.BATTLE_TANK);
        tank2.setOwnerId(2);
        tank2.setGoal(new Attack(new Target(Entity.UNIT, tankVictim, map.getUnit(tankVictim).getPoint())));
        tank2.setX(0);
        tank2.setY(3);
        map.addUnit(tank2);

        Unit tank3 = new Unit();
        tank3.setUnitType(UnitType.BATTLE_TANK);
        tank3.setOwnerId(2);
        tank3.setGoal(new Attack(new Target(Entity.UNIT, tankVictim, map.getUnit(tankVictim).getPoint())));
        tank3.setX(0);
        tank3.setY(4);
        map.addUnit(tank3);

        Unit tank4 = new Unit();
        tank4.setUnitType(UnitType.BATTLE_TANK);
        tank4.setOwnerId(2);
        tank4.setGoal(new Attack(new Target(Entity.UNIT, tankVictim, map.getUnit(tankVictim).getPoint())));
        tank4.setX(0);
        tank4.setY(5);
        map.addUnit(tank4);

        gameService.setMap(map);
        gameService.tick();

        assertEquals(4,map.getUnits().size());

        // should be enough ticks to destroy the other tank
        for (int i = 0; i < 5000; i++) {
            gameService.tick();
        }

        assertEquals(3,map.getUnits().size());
    }

    // Tank starts outside of attack range of enemy tanks that attempt to kill it.
    // Objective is for the following tanks to pursuit and destroy the opponent.
    @Test
    public void unitPursuitLongDistance() {
        Map map = new Map(64,64);

        Unit tank1 = new Unit();
        tank1.setUnitType(UnitType.BATTLE_TANK);
        tank1.setOwnerId(1);
        tank1.setGoal(new Move(60, 4));
        tank1.setX(6);
        tank1.setY(4);
        int tankVictim = map.addUnit(tank1);

        Unit tank2 = new Unit();
        tank2.setUnitType(UnitType.BATTLE_TANK);
        tank2.setOwnerId(2);
        tank2.setGoal(new Attack(new Target(Entity.UNIT, map.getUnit(tankVictim).getId(), map.getUnit(tankVictim).getPoint())));
        tank2.setX(0);
        tank2.setY(3);
        map.addUnit(tank2);

        tank2 = new Unit();
        tank2.setUnitType(UnitType.BATTLE_TANK);
        tank2.setOwnerId(2);
        tank2.setGoal(new Attack(new Target(Entity.UNIT, map.getUnit(tankVictim).getId(), map.getUnit(tankVictim).getPoint())));
        tank2.setX(0);
        tank2.setY(4);
        map.addUnit(tank2);

        tank2 = new Unit();
        tank2.setUnitType(UnitType.BATTLE_TANK);
        tank2.setOwnerId(2);
        tank2.setGoal(new Attack(new Target(Entity.UNIT, map.getUnit(tankVictim).getId(), map.getUnit(tankVictim).getPoint())));
        tank2.setX(0);
        tank2.setY(5);
        map.addUnit(tank2);

        gameService.setMap(map);
        gameService.tick();

        assertEquals(4,map.getUnits().size());

        // should be enough ticks to destroy the other tank
        for (int i = 0; i < 5000; i++) {
            gameService.tick();
        }

        assertEquals(3,map.getUnits().size());
    }
}