package lv.k2611a.network.req;

import java.util.HashSet;
import java.util.Set;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;

public class UnitStop extends AbstractGameStateChanger {
    private long[] ids;

    public long[] getIds() {
        return ids;
    }

    public void setIds(long[] ids) {
        this.ids = ids;
    }

    @Override
    public void changeGameState(Map map) {
        if (ids == null) {
            ids = new long[0];
        }
        Set<Long> unitIds = new HashSet<Long>();
        for (long id : ids) {
            unitIds.add(id);
        }
        for (Unit unit : map.getUnits()) {
            if (unitIds.contains(unit.getId())) {
                if (unit.getOwnerId() == playerId) {
                    unit.clearGoals();
                }
            }
        }
    }

}
