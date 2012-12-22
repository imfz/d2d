package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;

public interface UnitGoal {
    void process(Unit unit, Map map);
}
