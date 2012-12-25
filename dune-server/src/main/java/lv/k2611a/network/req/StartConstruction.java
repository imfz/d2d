package lv.k2611a.network.req;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.BuildingType;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.buildinggoals.CreateBuilding;

public class StartConstruction extends AbstractGameStateChanger {
    private int builderId;
    private int entityToBuildId;

    @Override
    public void changeGameState(Map map) {
        Building building = map.getBuilding(builderId);
        if (building == null) {
            return;
        }
        if (building.getOwnerId() != this.playerId) {
            return;
        }
        if (!(building.getType() == BuildingType.CONSTRUCTIONYARD)) {
            return;
        }
        CreateBuilding createBuilding = new CreateBuilding();
        createBuilding.setBuildingType(BuildingType.getByJsId(entityToBuildId));
        building.addGoal(createBuilding);
    }

    public int getBuilderId() {
        return builderId;
    }

    public void setBuilderId(int builderId) {
        this.builderId = builderId;
    }

    public int getEntityToBuildId() {
        return entityToBuildId;
    }

    public void setEntityToBuildId(int entityToBuildId) {
        this.entityToBuildId = entityToBuildId;
    }
}
