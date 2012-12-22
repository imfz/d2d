package lv.k2611a.domain.goals;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;

public interface Goal {
    void onSet(Unit unit);
    void process(Unit unit, Map map);
}
