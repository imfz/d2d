package lv.k2611a.domain.buildinggoals;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.Map;
import lv.k2611a.service.IdGeneratorService;

public interface BuildingGoal {
    void process(Building building, Map map, IdGeneratorService idGeneratorService, long tickCount);
}
