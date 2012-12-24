package lv.k2611a.network.req;

import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.BuildingType;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.TileType;
import lv.k2611a.service.IdGeneratorService;

public class PlaceBuilding extends AbstractGameStateChanger {
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

        for (int x = 0; x < buildingTypeBuilt.getWidth(); x++) {
            for (int y = 0; y < buildingTypeBuilt.getHeight(); y++) {
                if (map.isObstacle(this.x + x,this.y + y)) {
                    return;
                }
                if (map.getTile(this.x + x,this.y + y).getTileType() != TileType.ROCK) {
                    return;
                }
            }
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
