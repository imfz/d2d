package lv.k2611a.domain.buildinggoals;

import lv.k2611a.domain.unitgoals.Guard;
import lv.k2611a.domain.unitgoals.Harvest;
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
    private boolean blocked = false;

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    @Override
    public void process(Building building, Map map, long tickCount) {
        blocked = false;
        Player player = map.getPlayerById(building.getOwnerId());

        if (building.getTicksAccumulated() >= unitType.getTicksToBuild()) {
            if (placeUnit(map, building)) {
                building.setTicksAccumulated(0);
                building.removeGoal(this);
            } else {
                blocked = true;
            }
        } else {
            if (player.getElectricity() < 0) {
                // throttle 3/4 of ticks, if not enough electricity
                if (tickCount % 4 != 3) {
                    return;
                }
            }

            if (player.getMoney() >= unitType.getCostPerTick()) {
                player.setMoney(player.getMoney() - unitType.getCostPerTick());
                building.setTicksAccumulated(building.getTicksAccumulated() + 1);
                if (building.getTicksAccumulated() >= unitType.getTicksToBuild()) {
                    if (placeUnit(map, building)) {
                        building.setTicksAccumulated(0);
                        building.removeGoal(this);
                    }
                }
            }
        }
    }

    private boolean placeUnit(Map map, Building building) {
        int minX = building.getX() - 1;
        int maxX = building.getX() + building.getType().getWidth();
        int minY = building.getY() - 1;
        int maxY = building.getY() + building.getType().getHeight();

        Tile freeTile = map.getNearestFreeTileForUnitPlacement(minX, maxX, minY, maxY);
        if (freeTile == null) {
            return false;
        }
        Unit unit = new Unit();
        unit.setOwnerId(building.getOwnerId());

        unit.setX(freeTile.getX());
        unit.setY(freeTile.getY());
        unit.setUnitType(unitType);
        ViewDirection unitDirection = ViewDirection.getDirection(unit.getPoint(), map.getClosestPoint(building, unit));
        unit.setViewDirection(ViewDirection.getDirectionByAngle(unitDirection.getAngle()+180));

        if (unit.getUnitType() == UnitType.HARVESTER) {
            unit.setGoal(new Harvest());
        } else {
            unit.addDefaultGoal();
        }
        map.addUnit(unit);

        return true;
    }

    public boolean isBlocked() {
        return blocked;
    }
}
