package lv.k2611a.network.req;

import java.util.HashSet;
import java.util.Set;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;

public class UnitStop extends AbstractGameStateChanger {
    private int[] ids;

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    @Override
    public void changeGameState(Map map) {
        if (ids == null) {
            ids = new int[0];
        }
        Set<Integer> unitIds = new HashSet<Integer>();
        for (int id : ids) {
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
