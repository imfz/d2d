package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.network.UnitDTO;
import lv.k2611a.service.game.GameServiceImpl;

public class Guard implements UnitGoal {

    @Override
    public void reserveTiles(Unit unit, Map map) {
        map.setUsed(unit.getX(), unit.getY(), unit.getId());
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (unit.getUnitType() == UnitType.HARVESTER) {
            return;
        }
    }

    @Override
    public void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto) {
    }
}
