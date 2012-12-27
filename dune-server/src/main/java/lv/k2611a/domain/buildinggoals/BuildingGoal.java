package lv.k2611a.domain.buildinggoals;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.Map;

public interface BuildingGoal {
    void process(Building building, Map map, long tickCount);
}
