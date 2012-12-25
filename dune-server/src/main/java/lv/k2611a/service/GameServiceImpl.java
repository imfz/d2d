package lv.k2611a.service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lv.k2611a.ClientConnection;
import lv.k2611a.domain.Building;
import lv.k2611a.domain.BuildingType;
import lv.k2611a.domain.ConstructionOption;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Player;
import lv.k2611a.domain.RefineryEntrance;
import lv.k2611a.domain.Tile;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.buildinggoals.CreateBuilding;
import lv.k2611a.domain.unitgoals.Move;
import lv.k2611a.jmx.ServerMonitor;
import lv.k2611a.network.BuildingDTO;
import lv.k2611a.network.MapDTO;
import lv.k2611a.network.OptionDTO;
import lv.k2611a.network.TileDTO;
import lv.k2611a.network.TileWithCoordinatesDTO;
import lv.k2611a.network.UnitDTO;
import lv.k2611a.network.req.GameStateChanger;
import lv.k2611a.network.resp.UpdateConstructionOptions;
import lv.k2611a.network.resp.UpdateMap;
import lv.k2611a.network.resp.UpdateMapIncremental;
import lv.k2611a.network.resp.UpdateMoney;
import lv.k2611a.util.MapGenerator;
import lv.k2611a.util.Point;

@Service
@Scope("singleton")
public class GameServiceImpl implements GameService {

    private static final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);
    public static final int TICK_LENGTH = 1000 / 20;
    public static final int BUILDING_USAGE_FLAG = -2;
    public static final int REFINERY_ENTRY_USAGE_FLAG = -3;

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Autowired
    private SessionsService sessionsService;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private UserActionService userActionService;

    @Autowired
    private ServerMonitor serverMonitor;

    private volatile Map map;
    private volatile long tickCount = 0;

    private Set<Point> changedTiles;

    @PostConstruct
    public void init() {
        map = MapGenerator.generateMap(64, 64, 8, idGeneratorService);
        Random r = new Random();
        for (int j = 1; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                Unit unit = new Unit();
                unit.setId(idGeneratorService.generateUnitId());
                unit.setOwnerId(1);
                unit.setX(j);
                unit.setY(i);
                unit.setGoal(new Move(10 + j, i));
                unit.setUnitType(UnitType.HARVESTER);
                map.getUnits().add(unit);
            }
        }
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    @Override
    public synchronized UpdateMap getFullMapUpdate() {
        MapDTO mapDTO = new MapDTO();
        List<TileDTO> tileDTOList = getMapTiles();
        List<UnitDTO> unitDTOList = getMapUnits();
        List<BuildingDTO> buildingDTOList = getBuildings();

        mapDTO.setWidth(map.getWidth());
        mapDTO.setHeight(map.getHeight());
        mapDTO.setTiles(tileDTOList.toArray(new TileDTO[tileDTOList.size()]));
        mapDTO.setUnits(unitDTOList.toArray(new UnitDTO[unitDTOList.size()]));
        mapDTO.setBuildings(buildingDTOList.toArray(new BuildingDTO[buildingDTOList.size()]));
        UpdateMap updateMap = new UpdateMap();
        updateMap.setMap(mapDTO);
        updateMap.setTickCount(tickCount);
        return updateMap;
    }

    @Override
    public synchronized boolean isOwner(int buildingId, int playerId) {
        Building building = map.getBuilding(buildingId);
        if (building == null) {
            return false;
        }
        return building.getOwnerId() == playerId;
    }

    @Override
    public synchronized boolean playerExist(int playerId) {
        if (map.getPlayerById(playerId) == null) {
            return false;
        }
        return true;
    }

    private List<TileDTO> getMapTiles() {
        List<TileDTO> tileDTOList = new ArrayList<TileDTO>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Tile tile = map.getTile(x, y);
                tileDTOList.add(TileDTO.fromTile(tile));
            }
        }
        return tileDTOList;
    }

    private List<UnitDTO> getMapUnits() {
        List<UnitDTO> unitDTOList = new ArrayList<UnitDTO>();
        for (Unit unit : map.getUnits()) {
            unitDTOList.add(UnitDTO.fromUnit(unit));
        }
        return unitDTOList;
    }

    @Scheduled(fixedRate = TICK_LENGTH)
    public synchronized void tick() {
        long startTime = System.currentTimeMillis();

        tickCount++;

        changedTiles = new HashSet<Point>();

        mapFillTileUsage(map);
        processUserClicks(map);
        processBuildingGoals(map);
        processUnitsGoals(map);
        sendIncrementalUpdate();
        sendAvalaibleConstructionOptionsUpdate();
        sendUpdateMoney();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        if (duration > TICK_LENGTH) {
            log.warn("Server is lagging, tick duration exceeded " + TICK_LENGTH + ", was " + duration + " tick count " + tickCount);
        }

        serverMonitor.reportTickTime(duration);

    }

    private void sendUpdateMoney() {
        for (ClientConnection clientConnection : sessionsService.getMembers()) {
            int playerId = clientConnection.getPlayerId();
            Player player = map.getPlayerById(playerId);
            UpdateMoney updateMoney = new UpdateMoney();
            updateMoney.setMoney(player.getMoney());
            clientConnection.sendMessage(updateMoney);
        }
    }

    private void sendAvalaibleConstructionOptionsUpdate() {
        for (ClientConnection clientConnection : sessionsService.getMembers()) {
            Integer selectedBuildingId = clientConnection.getSelectedBuildingId();
            if (selectedBuildingId != null) {
                Building building = map.getBuilding(selectedBuildingId);
                if (building == null) {
                    clientConnection.sendMessage(new UpdateConstructionOptions());
                    continue;
                }
                EnumSet<ConstructionOption> constructionOptions = EnumSet.noneOf(ConstructionOption.class);
                if (!(building.isAwaitingClick() || building.getCurrentGoal() != null)) {
                    constructionOptions = building.getType().getConstructionOptions();
                }

                List<OptionDTO> options = new ArrayList<OptionDTO>();
                for (ConstructionOption constructionOption : constructionOptions) {
                    OptionDTO option = new OptionDTO();
                    option.setType(constructionOption.getIdOnJS());
                    option.setEntityToBuildId(constructionOption.getEntityToBuildIdOnJs());
                    option.setCost(constructionOption.getCost());
                    option.setName(constructionOption.getName());
                    options.add(option);
                }
                UpdateConstructionOptions updateConstructionOptions = new UpdateConstructionOptions();
                updateConstructionOptions.setBuilderId(clientConnection.getSelectedBuildingId());
                if (!(building.isAwaitingClick())) {
                    updateConstructionOptions.setReadyToBuild(true);
                } else {
                    updateConstructionOptions.setCurrentlyBuildingId(building.getBuildingTypeBuilt().getIdOnJS());
                    updateConstructionOptions.setCurrentlyBuildingOptionId(getConstructionOption(building.getType().getConstructionOptions(), building.getBuildingTypeBuilt()));
                    updateConstructionOptions.setPercentsDone(100);
                }
                if (building.getCurrentGoal() != null) {
                    if (building.getCurrentGoal() instanceof CreateBuilding) {
                        CreateBuilding createBuilding = (CreateBuilding) building.getCurrentGoal();
                        BuildingType buildingTypeBuilt = createBuilding.getBuildingType();
                        if (buildingTypeBuilt != null) {
                            updateConstructionOptions.setCurrentlyBuildingId(buildingTypeBuilt.getIdOnJS());
                            updateConstructionOptions.setCurrentlyBuildingOptionId(getConstructionOption(building.getType().getConstructionOptions(), buildingTypeBuilt));
                            double done = (double) building.getTicksAccumulated() / buildingTypeBuilt.getTicksToBuild();
                            int percentsDone = (int) Math.round(done * 100);
                            updateConstructionOptions.setPercentsDone(percentsDone);
                        }
                    }
                }
                updateConstructionOptions.setOptions(options.toArray(new OptionDTO[options.size()]));
                clientConnection.sendMessage(updateConstructionOptions);
            } else {
                clientConnection.sendMessage(new UpdateConstructionOptions());
            }
        }
    }

    private int getConstructionOption(EnumSet<ConstructionOption> constructionOptions, BuildingType buildingTypeBuilt) {
        for (ConstructionOption constructionOption : constructionOptions) {
            if (constructionOption.getEntityToBuildIdOnJs() == buildingTypeBuilt.getIdOnJS()) {
                return constructionOption.getIdOnJS();
            }
        }
        return -1;
    }

    private void processBuildingGoals(Map map) {
        for (Building building : map.getBuildings()) {
            try {
                if (building.getCurrentGoal() != null) {
                    building.getCurrentGoal().process(building, map);
                }
            } catch (RuntimeException e) {
                log.error("Exception while proccessing building goal", e);
            }
        }
    }

    private void processUserClicks(Map map) {
        for (GameStateChanger gameStateChanger : userActionService.drainActions()) {
            try {
                autowireCapableBeanFactory.autowireBean(gameStateChanger);
                gameStateChanger.changeGameState(map);
            } catch (RuntimeException e) {
                log.error("Exception while processing user action", e);
            }
        }
    }

    @Override
    public void registerChangedTile(Point point) {
        changedTiles.add(point);
    }

    private void sendIncrementalUpdate() {
        UpdateMapIncremental update = new UpdateMapIncremental();

        List<UnitDTO> mapUnits = getMapUnits();
        update.setUnits(mapUnits.toArray(new UnitDTO[mapUnits.size()]));

        List<BuildingDTO> buildingDTOList = getBuildings();
        update.setBuildings(buildingDTOList.toArray(new BuildingDTO[buildingDTOList.size()]));

        List<TileWithCoordinatesDTO> tileDTOList = getChangedTiles();
        update.setChangedTiles(tileDTOList.toArray(new TileWithCoordinatesDTO[tileDTOList.size()]));

        update.setTickCount(tickCount);
        sessionsService.sendUpdate(update);

    }

    private List<TileWithCoordinatesDTO> getChangedTiles() {
        List<TileWithCoordinatesDTO> result = new ArrayList<TileWithCoordinatesDTO>();
        for (Point changedTile : this.changedTiles) {
            TileWithCoordinatesDTO tile = TileWithCoordinatesDTO.fromTile(map.getTile(changedTile));
            result.add(tile);
        }
        return result;
    }

    private void processUnitsGoals(Map map) {
        for (Unit unit : map.getUnits()) {
            try {
                if (unit.getCurrentGoal() != null) {
                    unit.getCurrentGoal().process(unit, map, this);
                }
            } catch (Exception e) {
                log.error("Exception while processing unit goal", e);
            }
        }
    }

    private void mapFillTileUsage(Map map) {
        map.clearUsageFlag();
        map.getRefineryEntranceList().clear();
        map.getHarvesters().clear();
        for (Building building : map.getBuildings()) {
            for (int x = 0; x < building.getType().getWidth(); x++) {
                for (int y = 0; y < building.getType().getHeight(); y++) {
                    map.setUsed(x + building.getX(), y + building.getY(), BUILDING_USAGE_FLAG);
                }
            }
        }
        for (Building building : map.getBuildingsByType(BuildingType.REFINERY)) {
            Point point = building.getPoint();
            point = new Point(point.getX() + 1, point.getY() + 1);
            RefineryEntrance refineryEntrance = new RefineryEntrance(building.getOwnerId(), point, building.getId());
            map.getRefineryEntranceList().put(point,refineryEntrance);
        }
        for (Unit unit : map.getUnitsByType(UnitType.HARVESTER)) {
            map.getHarvesters().add(unit.getId());
        }

        for (Unit unit : map.getUnits()) {
            map.setUsed(unit.getX(), unit.getY(), unit.getId());
            if (unit.getTicksMovingToNextCell() > 0) {
                Point unitMovingTo = unit.getViewDirection().apply(new Point(unit.getX(), unit.getY()));
                map.setUsed(unitMovingTo.getX(), unitMovingTo.getY(), unit.getId());
            }
        }
        map.buildPassableSegmentCache();
    }

    public List<BuildingDTO> getBuildings() {
        List<BuildingDTO> buildingDTOList = new ArrayList<BuildingDTO>();
        for (Building building : map.getBuildings()) {
            buildingDTOList.add(BuildingDTO.fromBuilding(building));
        }
        return buildingDTOList;
    }

    @Override
    public long getTickCount() {
        return tickCount;
    }

    @Override
    public void setTickCount(long tickCount) {
        this.tickCount = tickCount;
    }
}
