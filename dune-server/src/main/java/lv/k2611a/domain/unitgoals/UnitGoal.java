package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;
import lv.k2611a.network.UnitDTO;
import lv.k2611a.service.game.GameServiceImpl;

public interface UnitGoal {
    void reserveTiles(Unit unit, Map map);
    void process(Unit unit, Map map, GameServiceImpl gameService);
    void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto);
}
