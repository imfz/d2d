package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;
import lv.k2611a.service.GameServiceImpl;

public interface UnitGoal {
    void process(Unit unit, Map map, GameServiceImpl gameService);
}
