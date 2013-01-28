package lv.k2611a.network.req;

import lv.k2611a.domain.*;
import lv.k2611a.util.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.k2611a.domain.unitgoals.Harvest;
import lv.k2611a.util.MapUtils;

public class PlaceBuilding extends AbstractGameStateChanger {

    private static final Logger log = LoggerFactory.getLogger(PlaceBuilding.class);

    private int x;
    private int y;
    // since building is produced by constr. yard, ask con yard id here
    private int builderId;

    @Override
    public void changeGameState(Map map) {
        Building conYard = map.getBuilding(builderId);
        if (conYard == null) {
            return;
        }
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
        building.setOwnerId(conYard.getOwnerId());
        map.addBuilding(building);
        for (int x = 0; x < building.getType().getWidth(); x++) {
            for (int y = 0; y < building.getType().getHeight(); y++) {
                map.setUsedByBuilding(x + building.getX(), y + building.getY(), building.getId());
            }
        }

        conYard.setAwaitingClick(false);
        conYard.setTicksAccumulated(0);
        conYard.setBuildingTypeBuilt(null);

        if (buildingTypeBuilt == BuildingType.REFINERY) {
            Point point = new Point(building.getX()+1, building.getY()+1);
            RefineryEntrance refineryEntrance = new RefineryEntrance(building.getOwnerId(), point, building.getId());
            map.getRefineryEntranceList().put(point, refineryEntrance);

            Unit unit = new Unit();
            unit.setOwnerId(conYard.getOwnerId());
            unit.setX(building.getX()+1);
            unit.setY(building.getY()+1);
            unit.setUnitType(UnitType.HARVESTER);
            unit.setViewDirection(ViewDirection.TOP);
            unit.setGoal(new Harvest());
            map.addUnit(unit);
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
                if (!map.isUnoccupied(this.x + x, this.y + y)) {
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

    public long getBuilderId() {
        return builderId;
    }

    public void setBuilderId(int builderId) {
        this.builderId = builderId;
    }
}
