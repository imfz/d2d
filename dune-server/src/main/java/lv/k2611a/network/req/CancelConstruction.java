package lv.k2611a.network.req;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.BuildingType;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Player;
import lv.k2611a.domain.buildinggoals.CreateBuilding;

public class CancelConstruction extends AbstractGameStateChanger {
    private int builderId;

    public int getBuilderId() {
        return builderId;
    }

    public void setBuilderId(int builderId) {
        this.builderId = builderId;
    }

    @Override
    public void changeGameState(Map map) {
        Building conYard = map.getBuilding(builderId);
        if (conYard == null) {
            return;
        }
        if (conYard.getOwnerId() != this.playerId) {
            return;
        }
        if (!(conYard.getType() == BuildingType.CONSTRUCTIONYARD)) {
            return;
        }
        if (conYard.getCurrentGoal() instanceof CreateBuilding) {
            CreateBuilding goal = (CreateBuilding) conYard.getCurrentGoal();
            int moneyBack = conYard.getTicksAccumulated() * goal.getBuildingType().getCostPerTick();
            conYard.setTicksAccumulated(0);
            Player player = map.getPlayerById(playerId);
            player.setMoney(player.getMoney() + moneyBack);
            conYard.removeGoal(conYard.getCurrentGoal());
        } else {
            if (conYard.getBuildingTypeBuilt() != null) {
                int moneyBack = conYard.getBuildingTypeBuilt().getCost();
                conYard.setTicksAccumulated(0);
                conYard.setAwaitingClick(false);
                Player player = map.getPlayerById(playerId);
                player.setMoney(player.getMoney() + moneyBack);
            }
        }
    }
}
