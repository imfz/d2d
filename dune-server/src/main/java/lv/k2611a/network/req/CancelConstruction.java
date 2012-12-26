package lv.k2611a.network.req;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Player;
import lv.k2611a.domain.buildinggoals.CreateBuilding;
import lv.k2611a.domain.buildinggoals.CreateUnit;

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
        Building constructorBuilding = map.getBuilding(builderId);
        if (constructorBuilding == null) {
            return;
        }
        if (constructorBuilding.getOwnerId() != this.playerId) {
            return;
        }
        if (constructorBuilding.getCurrentGoal() instanceof CreateBuilding) {
            cancelCreateBuilding(map, constructorBuilding);
        } else if (constructorBuilding.getCurrentGoal() instanceof CreateUnit) {
            cancelCreateUnit(map, constructorBuilding);
        } else {
            if (constructorBuilding.getBuildingTypeBuilt() != null) {
                cancelReadyToPlaceBuilding(map, constructorBuilding);
            }
        }
    }

    private void cancelReadyToPlaceBuilding(Map map, Building constructorBuilding) {
        int moneyBack = constructorBuilding.getBuildingTypeBuilt().getCost();
        constructorBuilding.setTicksAccumulated(0);
        constructorBuilding.setAwaitingClick(false);
        Player player = map.getPlayerById(playerId);
        player.setMoney(player.getMoney() + moneyBack);
    }

    private void cancelCreateUnit(Map map, Building constructorBuilding) {
        CreateUnit goal = (CreateUnit) constructorBuilding.getCurrentGoal();
        int moneyBack = constructorBuilding.getTicksAccumulated() * goal.getUnitType().getCostPerTick();
        constructorBuilding.setTicksAccumulated(0);
        Player player = map.getPlayerById(playerId);
        player.setMoney(player.getMoney() + moneyBack);
        constructorBuilding.removeGoal(constructorBuilding.getCurrentGoal());
    }

    private void cancelCreateBuilding(Map map, Building constructorBuilding) {
        CreateBuilding goal = (CreateBuilding) constructorBuilding.getCurrentGoal();
        int moneyBack = constructorBuilding.getTicksAccumulated() * goal.getBuildingType().getCostPerTick();
        constructorBuilding.setTicksAccumulated(0);
        Player player = map.getPlayerById(playerId);
        player.setMoney(player.getMoney() + moneyBack);
        constructorBuilding.removeGoal(constructorBuilding.getCurrentGoal());
    }
}
