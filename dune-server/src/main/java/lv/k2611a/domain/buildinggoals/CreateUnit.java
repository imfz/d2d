package lv.k2611a.domain.buildinggoals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Player;
import lv.k2611a.domain.Tile;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.ViewDirection;

public class CreateUnit implements BuildingGoal {

    private static final Logger log = LoggerFactory.getLogger(CreateUnit.class);

    private UnitType unitType;

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    @Override
    public void process(Building building, Map map, long tickCount) {
        Player player = map.getPlayerById(building.getOwnerId());

        if (player.getElectricity() < 0) {
            // throttle 3/4 of ticks, if not enough electricity
            if (tickCount % 4 != 3) {
                return;
            }
        }

        if (player.getMoney() >= unitType.getCostPerTick()) {
            player.setMoney(player.getMoney() - unitType.getCostPerTick());
        } else {
            // no money, no honey
            return;
        }
        if (building.getTicksAccumulated() >= unitType.getTicksToBuild()-1) {
            building.setTicksAccumulated(0);
            building.removeGoal(this);
            placeUnit(map, building);
        } else {
            building.setTicksAccumulated(building.getTicksAccumulated() + 1);
        }
    }

    private void placeUnit(Map map, Building building) {
        int newX = building.getX() + 1;
        int newY = building.getY() +2;

        boolean entranceBlocked = false;

        if (newY >= map.getHeight()) {
            entranceBlocked = true;
        } else {
            if (map.isObstacle(newX, newY)) {
                newX--;
                if (map.isObstacle(newX, newY)) {
                    newX++;
                    newX++;
                    if (map.isObstacle(newX, newY)) {
                        entranceBlocked = true;
                    }
                }
            }
        }

        if (entranceBlocked) {
            Tile freeTile = map.getNearestFreeTile(newX,newY);
            if (freeTile == null) {
                log.warn("Cannot place unit, all tiles occupied");
                return;
            }
            newX = freeTile.getX();
            newY = freeTile.getY();
        }


        Unit unit = new Unit();
        unit.setOwnerId(building.getOwnerId());
        unit.setX(newX);
        unit.setY(newY);
        unit.setUnitType(unitType);
        unit.setViewDirection(ViewDirection.BOTTOM);
        map.addUnit(unit);

    }
}
