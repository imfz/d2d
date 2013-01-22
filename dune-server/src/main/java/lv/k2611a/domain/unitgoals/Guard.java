package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.network.UnitDTO;
import lv.k2611a.service.game.GameServiceImpl;

public class Guard implements UnitGoal {

    @Override
    public void reserveTiles(Unit unit, Map map) {
        map.setUsedByUnit(unit.getX(), unit.getY(), unit.getId());
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (unit.getUnitType() == UnitType.HARVESTER) {
            return;
        }
        int maximumTargetRange = unit.getUnitType().getAttackRange();
        int minX = unit.getX() - maximumTargetRange;
        int maxX = unit.getX() + maximumTargetRange;
        int minY = unit.getY() - maximumTargetRange;
        int maxY = unit.getY() + maximumTargetRange;
    }

    @Override
    public void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto) {
    }
}
