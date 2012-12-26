package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.BuildingType;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Tile;
import lv.k2611a.domain.TileType;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.ViewDirection;
import lv.k2611a.domain.unitgoals.Harvest;
import lv.k2611a.service.IdGeneratorService;
import lv.k2611a.util.MapUtils;

public class PlaceBuilding extends AbstractGameStateChanger {

    private static final Logger log = LoggerFactory.getLogger(PlaceBuilding.class);

    private int x;
    private int y;
    // since building is produced by constr. yard, ask con yard id here
    private int builderId;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Override
    public void changeGameState(Map map) {
        Building conYard = map.getBuilding(builderId);
        if (conYard.getType() != BuildingType.CONSTRUCTIONYARD) {
            return;
        }
        if (!conYard.isAwaitingClick()) {
            return;
        }
        if (conYard.getOwnerId() != playerId) {
            return;
        }
        BuildingType buildingTypeBuilt = conYard.getBuildingTypeBuilt();

        if (!terrainAllowsConstruction(map, buildingTypeBuilt)) {
            return;
        }

        if (!buildingNearby(map, x, y, buildingTypeBuilt.getWidth(), buildingTypeBuilt.getHeight(), this.playerId)) {
            return;
        }

        Building building = new Building();
        building.setType(buildingTypeBuilt);
        building.setX(x);
        building.setY(y);
        building.setId(idGeneratorService.generateBuildingId());
        building.setOwnerId(conYard.getOwnerId());
        map.getBuildings().add(building);

        conYard.setAwaitingClick(false);
        conYard.setTicksAccumulated(0);
        conYard.setBuildingTypeBuilt(null);

        if (buildingTypeBuilt == BuildingType.REFINERY) {

            int newX = x+1;
            int newY = y+1;

            boolean entranceBlocked = false;

            if (y + 2 >= map.getHeight()) {
                entranceBlocked = true;
            } else {
                if ((map.isObstacle(x, y + 2)) && (map.isObstacle(x + 1, y + 2)) && (map.isObstacle(x + 2, y + 2))) {
                    entranceBlocked = true;
                }
            }

            if (entranceBlocked) {
                Tile freeTile = map.getNearestFreeTile(x + 1, y + 1);
                if (freeTile == null) {
                    log.warn("Cannot place harvester, all tiles occupied");
                    return;
                }
                newX = freeTile.getX();
                newY = freeTile.getY();
            }


            Unit unit = new Unit();
            unit.setId(idGeneratorService.generateUnitId());
            unit.setOwnerId(conYard.getOwnerId());
            unit.setX(newX);
            unit.setY(newY);
            unit.setUnitType(UnitType.HARVESTER);
            unit.setViewDirection(ViewDirection.BOTTOM);
            unit.setGoal(new Harvest());
            map.getUnits().add(unit);
        }

    }

    private boolean buildingNearby(Map map, int x, int y, int width, int height, int playerId) {
        for (Building building : map.getBuildings()) {
            MapUtils.IntersectionType intersectionType =
                    MapUtils.getIntersectionType(
                            x, y, width, height,
                            building.getX(), building.getY(), building.getType().getWidth(), building.getType().getHeight());
            if (intersectionType == MapUtils.IntersectionType.INTERSECT) {
                return false;
            }
            if (building.getOwnerId() == playerId) {
                if (intersectionType == MapUtils.IntersectionType.NEARBY) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean terrainAllowsConstruction(Map map, BuildingType buildingTypeBuilt) {
        for (int x = 0; x < buildingTypeBuilt.getWidth(); x++) {
            for (int y = 0; y < buildingTypeBuilt.getHeight(); y++) {
                if (map.isObstacle(this.x + x, this.y + y)) {
                    return false;
                }
                if (map.getTile(this.x + x, this.y + y).getTileType() != TileType.ROCK) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getBuilderId() {
        return builderId;
    }

    public void setBuilderId(int builderId) {
        this.builderId = builderId;
    }
}
