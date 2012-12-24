package lv.k2611a.network.req;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.BuildingType;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.TileType;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.service.GameServiceImpl;
import lv.k2611a.service.UserActionService;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testContext.xml"})
public class StartConstructionTest {

    public static final int CONSTRUCTION_YARD_ID = 1;
    public static final int ANOTHER_CONSTRUCTION_YARD_ID = 2;

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private UserActionService userActionService;

    @Before
    public void setup() {
        gameService.setTickCount(0);
        // all building should cost 0
        for (BuildingType buildingType : BuildingType.values()) {
            buildingType.setCostPerTick(0);
        }
    }

    @Test
    public void testOnlyConstructionYardCanBuild() {
        Map map = new Map(64,64, TileType.ROCK);
        gameService.setMap(map);

        Building constructionYard = new Building();
        constructionYard.setId(CONSTRUCTION_YARD_ID);
        constructionYard.setOwnerId(1);
        constructionYard.setType(BuildingType.POWERPLANT);
        constructionYard.setX(1);
        constructionYard.setY(1);
        constructionYard.setHp(100);
        map.getBuildings().add(constructionYard);

        StartConstruction startConstruction = new StartConstruction();
        startConstruction.setBuilderId(CONSTRUCTION_YARD_ID);
        startConstruction.setEntityToBuildId(BuildingType.POWERPLANT.getIdOnJS());
        startConstruction.setPlayerId(1);
        userActionService.registerAction(startConstruction);

        for (int i = 0; i < BuildingType.POWERPLANT.getTicksToBuild(); i++) {
            gameService.tick();
        }

        assertEquals("building should not be built", 1, map.getBuildings().size());
        assertEquals("building should not be built", false, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());

    }


    @Test
    public void testNonExistingConstructionYardDoesntCauseError() {
        Map map = new Map(64,64, TileType.ROCK);
        gameService.setMap(map);

        StartConstruction startConstruction = new StartConstruction();
        startConstruction.setBuilderId(CONSTRUCTION_YARD_ID);
        startConstruction.setEntityToBuildId(BuildingType.POWERPLANT.getIdOnJS());
        userActionService.registerAction(startConstruction);

        gameService.tick();
        gameService.tick();
    }

    @Test
    public void testConstructionWithMoneyScenario() {
        Map map = new Map(64,64, TileType.ROCK);
        gameService.setMap(map);

        Building constructionYard = new Building();
        constructionYard.setId(CONSTRUCTION_YARD_ID);
        constructionYard.setOwnerId(1);
        constructionYard.setType(BuildingType.CONSTRUCTIONYARD);
        constructionYard.setX(1);
        constructionYard.setY(1);
        constructionYard.setHp(100);
        map.getBuildings().add(constructionYard);

        BuildingType.POWERPLANT.setCostPerTick(10);

        map.getPlayerById(1).setMoney(BuildingType.POWERPLANT.getTicksToBuild() * BuildingType.POWERPLANT.getCostPerTick() - 1);

        StartConstruction startConstruction = new StartConstruction();
        startConstruction.setBuilderId(CONSTRUCTION_YARD_ID);
        startConstruction.setEntityToBuildId(BuildingType.POWERPLANT.getIdOnJS());
        startConstruction.setPlayerId(1);
        userActionService.registerAction(startConstruction);


        for (int i = 0; i < BuildingType.POWERPLANT.getTicksToBuild(); i++) {
            gameService.tick();
            assertEquals("building should not be build without money", false, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());
        }

        // one last final tick
        map.getPlayerById(1).setMoney(map.getPlayerById(1).getMoney() + 1);
        gameService.tick();

        assertEquals("building should be built", true, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());
        assertEquals("tick count should reset", 0, map.getBuilding(CONSTRUCTION_YARD_ID).getTicksAccumulated());
        assertEquals("building goal should be empty", null, map.getBuilding(CONSTRUCTION_YARD_ID).getCurrentGoal());
        assertEquals(0,map.getPlayerById(1).getMoney());
    }

    @Test
    public void testConstructionCancelledOnLastTickMoneyReturnedScenario() {
        Map map = new Map(64,64, TileType.ROCK);
        gameService.setMap(map);

        Building constructionYard = new Building();
        constructionYard.setId(CONSTRUCTION_YARD_ID);
        constructionYard.setOwnerId(1);
        constructionYard.setType(BuildingType.CONSTRUCTIONYARD);
        constructionYard.setX(1);
        constructionYard.setY(1);
        constructionYard.setHp(100);
        map.getBuildings().add(constructionYard);

        BuildingType.POWERPLANT.setCostPerTick(10);

        int moneyRequiredToBuyPowerPlant = BuildingType.POWERPLANT.getTicksToBuild() * BuildingType.POWERPLANT.getCostPerTick();
        map.getPlayerById(1).setMoney(moneyRequiredToBuyPowerPlant);

        StartConstruction startConstruction = new StartConstruction();
        startConstruction.setBuilderId(CONSTRUCTION_YARD_ID);
        startConstruction.setEntityToBuildId(BuildingType.POWERPLANT.getIdOnJS());
        startConstruction.setPlayerId(1);
        userActionService.registerAction(startConstruction);


        for (int i = 0; i < BuildingType.POWERPLANT.getTicksToBuild()-1; i++) {
            gameService.tick();
            assertEquals("building should not be build", false, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());
        }

        // cancel construction
        CancelConstruction cancelConstruction = new CancelConstruction();
        cancelConstruction.setBuilderId(CONSTRUCTION_YARD_ID);
        cancelConstruction.setPlayerId(1);
        userActionService.registerAction(cancelConstruction);
        // one last final tick
        gameService.tick();

        assertEquals("building should not be build", false, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());
        assertEquals("ticks should reset", 0, map.getBuilding(CONSTRUCTION_YARD_ID).getTicksAccumulated());
        assertEquals(moneyRequiredToBuyPowerPlant, map.getPlayerById(1).getMoney());

        // event after more ticks building should not be built and money should not be subtracted
        for (int i = 0; i < 10; i++) {
            gameService.tick();
            assertEquals("building should not be build", false, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());
            assertEquals(moneyRequiredToBuyPowerPlant, map.getPlayerById(1).getMoney());
        }
    }

    @Test
    public void testConstructionCancelledWhenBuildingReadyTickMoneyReturnedScenario() {
        Map map = new Map(64,64, TileType.ROCK);
        gameService.setMap(map);

        Building constructionYard = new Building();
        constructionYard.setId(CONSTRUCTION_YARD_ID);
        constructionYard.setOwnerId(1);
        constructionYard.setType(BuildingType.CONSTRUCTIONYARD);
        constructionYard.setX(1);
        constructionYard.setY(1);
        constructionYard.setHp(100);
        map.getBuildings().add(constructionYard);

        BuildingType.POWERPLANT.setCostPerTick(10);

        int moneyRequiredToBuyPowerPlant = BuildingType.POWERPLANT.getTicksToBuild() * BuildingType.POWERPLANT.getCostPerTick();
        map.getPlayerById(1).setMoney(moneyRequiredToBuyPowerPlant);

        StartConstruction startConstruction = new StartConstruction();
        startConstruction.setBuilderId(CONSTRUCTION_YARD_ID);
        startConstruction.setEntityToBuildId(BuildingType.POWERPLANT.getIdOnJS());
        startConstruction.setPlayerId(1);
        userActionService.registerAction(startConstruction);


        for (int i = 0; i < BuildingType.POWERPLANT.getTicksToBuild(); i++) {
            gameService.tick();
        }

        assertEquals("building should be built", true, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());

        // cancel construction
        CancelConstruction cancelConstruction = new CancelConstruction();
        cancelConstruction.setBuilderId(CONSTRUCTION_YARD_ID);
        cancelConstruction.setPlayerId(1);
        userActionService.registerAction(cancelConstruction);
        // one last final tick
        gameService.tick();

        assertEquals("building should not be build", false, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());
        assertEquals("ticks should reset", 0, map.getBuilding(CONSTRUCTION_YARD_ID).getTicksAccumulated());
        assertEquals(moneyRequiredToBuyPowerPlant, map.getPlayerById(1).getMoney());

        // event after more ticks building should not be built and money should not be subtracted
        for (int i = 0; i < 10; i++) {
            gameService.tick();
            assertEquals("building should not be build", false, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());
            assertEquals(moneyRequiredToBuyPowerPlant, map.getPlayerById(1).getMoney());
        }
    }

    @Test
    public void testConstructionScenario() {
        Map map = new Map(64,64, TileType.ROCK);
        gameService.setMap(map);

        Building constructionYard = new Building();
        constructionYard.setId(CONSTRUCTION_YARD_ID);
        constructionYard.setOwnerId(1);
        constructionYard.setType(BuildingType.CONSTRUCTIONYARD);
        constructionYard.setX(1);
        constructionYard.setY(1);
        constructionYard.setHp(100);
        map.getBuildings().add(constructionYard);

        Building anotherConstructionYard = new Building();
        anotherConstructionYard.setId(ANOTHER_CONSTRUCTION_YARD_ID);
        anotherConstructionYard.setOwnerId(1);
        anotherConstructionYard.setType(BuildingType.CONSTRUCTIONYARD);
        anotherConstructionYard.setX(3);
        anotherConstructionYard.setY(3);
        anotherConstructionYard.setHp(100);
        map.getBuildings().add(anotherConstructionYard);

        StartConstruction startConstruction = new StartConstruction();
        startConstruction.setBuilderId(CONSTRUCTION_YARD_ID);
        startConstruction.setEntityToBuildId(BuildingType.POWERPLANT.getIdOnJS());
        startConstruction.setPlayerId(1);
        userActionService.registerAction(startConstruction);

        for (int i = 0; i < BuildingType.POWERPLANT.getTicksToBuild()-1; i++) {
            gameService.tick();
            assertEquals("building should not be build faster", 2, map.getBuildings().size());
            assertEquals("building should not be build faster", false, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());
        }

        // one last final tick
        gameService.tick();

        assertEquals("building should be built", true, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());
        assertEquals("tick count should reset", 0, map.getBuilding(CONSTRUCTION_YARD_ID).getTicksAccumulated());
        assertEquals("building goal should be empty", null, map.getBuilding(CONSTRUCTION_YARD_ID).getCurrentGoal());

        PlaceBuilding placeBuilding = new PlaceBuilding();
        placeBuilding.setBuilderId(ANOTHER_CONSTRUCTION_YARD_ID);
        placeBuilding.setX(5);
        placeBuilding.setY(5);
        placeBuilding.setPlayerId(1);
        userActionService.registerAction(placeBuilding);
        gameService.tick();

        assertEquals("building should not be built from another construction yard", 2, map.getBuildings().size());
        assertEquals("con yard should not be empty", true, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());

        placeBuilding = new PlaceBuilding();
        placeBuilding.setBuilderId(CONSTRUCTION_YARD_ID);
        placeBuilding.setX(2);
        placeBuilding.setY(2);
        placeBuilding.setPlayerId(1);
        userActionService.registerAction(placeBuilding);
        gameService.tick();

        assertEquals("building should not be built on nonempty location", 2, map.getBuildings().size());
        assertEquals("con yard should not be empty", true, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());

        Unit unit = new Unit();
        unit.setId(5);
        unit.setX(16);
        unit.setY(16);
        unit.setUnitType(UnitType.BATTLE_TANK);
        map.getUnits().add(unit);
        placeBuilding = new PlaceBuilding();
        placeBuilding.setBuilderId(CONSTRUCTION_YARD_ID);
        placeBuilding.setX(15);
        placeBuilding.setY(15);
        placeBuilding.setPlayerId(1);
        userActionService.registerAction(placeBuilding);
        gameService.tick();

        assertEquals("building should not be built on a unit", 2, map.getBuildings().size());
        assertEquals("con yard should not be empty", true, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());

        placeBuilding = new PlaceBuilding();
        placeBuilding.setBuilderId(CONSTRUCTION_YARD_ID);
        placeBuilding.setX(63);
        placeBuilding.setY(63);
        placeBuilding.setPlayerId(1);
        userActionService.registerAction(placeBuilding);
        gameService.tick();

        assertEquals("building should not be built on the edge of the map", 2, map.getBuildings().size());
        assertEquals("con yard should not be empty", true, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());

        placeBuilding = new PlaceBuilding();
        placeBuilding.setBuilderId(CONSTRUCTION_YARD_ID);
        placeBuilding.setX(58);
        placeBuilding.setY(58);
        placeBuilding.setPlayerId(1);
        map.getTile(59,59).setTileType(TileType.SAND);
        userActionService.registerAction(placeBuilding);
        gameService.tick();

        assertEquals("building should not be built on the sand", 2, map.getBuildings().size());
        assertEquals("con yard should not be empty", true, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());


        placeBuilding = new PlaceBuilding();
        placeBuilding.setBuilderId(CONSTRUCTION_YARD_ID);
        placeBuilding.setX(5);
        placeBuilding.setY(5);
        placeBuilding.setPlayerId(1);
        userActionService.registerAction(placeBuilding);
        gameService.tick();

        assertEquals("building should be built", 3, map.getBuildings().size());
        assertEquals("power plant is built", 1, map.getBuildingByType(BuildingType.POWERPLANT).size());
        assertEquals("power plant hp is ok", BuildingType.POWERPLANT.getHp(), map.getBuildingByType(BuildingType.POWERPLANT).get(0).getHp());
        assertEquals("con yard should be empty", false, map.getBuilding(CONSTRUCTION_YARD_ID).isAwaitingClick());

    }


}
