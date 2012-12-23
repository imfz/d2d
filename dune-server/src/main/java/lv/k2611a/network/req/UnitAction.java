package lv.k2611a.network.req;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lv.k2611a.ClientConnection;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.unitgoals.Move;

public class UnitAction extends AbstractGameStateChanger {

    private int x;
    private int y;
    private long[] ids;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public long[] getIds() {
        return ids;
    }

    public void setIds(long[] ids) {
        this.ids = ids;
    }


    @Override
    public void changeGameState(Map map) {
        Set<Long> unitIds = new HashSet<Long>();
        for (long id : ids) {
            unitIds.add(id);
        }
        for (Unit unit : map.getUnits()) {
            if (unitIds.contains(unit.getId())) {
                if (unit.getOwnerId() == playerId) {
                    unit.setGoal(new Move(x, y));
                }
            }
        }
    }

    @Override
    public String toString() {
        return "UnitAction{" +
                ", x=" + x +
                ", y=" + y +
                ", ids=" + Arrays.toString(ids) +
                '}';
    }
}
