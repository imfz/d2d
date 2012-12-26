package lv.k2611a.domain.buildinggoals;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.BuildingType;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Player;
import lv.k2611a.service.IdGeneratorService;

public class CreateBuilding implements BuildingGoal {

    private BuildingType buildingType;

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    @Override
    public void process(Building building, Map map, IdGeneratorService idGeneratorService, long tickCount) {
        Player player = map.getPlayerById(building.getOwnerId());
        if (player.getMoney() >= buildingType.getCostPerTick()) {
            player.setMoney(player.getMoney() - buildingType.getCostPerTick());
        } else {
            // no money, no honey
            return;
        }
        if (building.getTicksAccumulated() >= buildingType.getTicksToBuild()-1) {
            building.setAwaitingClick(true);
            building.setBuildingTypeBuilt(buildingType);
            building.setTicksAccumulated(0);
            building.removeGoal(this);
        } else {
            building.setTicksAccumulated(building.getTicksAccumulated() + 1);
        }

    }
}
