package lv.k2611a.network.req;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.BuildingType;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.buildinggoals.CreateBuilding;
import lv.k2611a.domain.buildinggoals.CreateUnit;

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
        if (building.getType() == BuildingType.CONSTRUCTIONYARD) {
            CreateBuilding createBuilding = new CreateBuilding();
            createBuilding.setBuildingType(BuildingType.getByJsId(entityToBuildId));
            building.addGoal(createBuilding);
        }

        if (building.getType() == BuildingType.FACTORY || building.getType() == BuildingType.LIGHT_FACTORY || building.getType() == BuildingType.BARRACKS) {
            CreateUnit createUnit = new CreateUnit();
            createUnit.setUnitType(UnitType.getByJsId(entityToBuildId));
            building.addGoal(createUnit);
        }

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
