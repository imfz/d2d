package lv.k2611a.network.req;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.RefineryEntrance;
import lv.k2611a.domain.Tile;
import lv.k2611a.domain.TileType;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.unitgoals.Harvest;
import lv.k2611a.domain.unitgoals.RepetetiveMove;
import lv.k2611a.domain.unitgoals.ReturnToBase;
import lv.k2611a.util.Point;

public class UnitAction extends AbstractGameStateChanger {

    private int x;
    private int y;
    private int[] ids;

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
        Tile tile = map.getTile(x, y);
        for (Unit unit : map.getUnitsByIds(unitIds)) {
            if (unit.getOwnerId() == playerId) {
                processUnit(map, tile, unit);
            }
        }
    }

    private void processUnit(Map map, Tile tile, Unit unit) {
        if (unit.getUnitType() == UnitType.HARVESTER) {
            processHarvester(map, tile, unit);
        } else {
            unit.setGoal(new RepetetiveMove(x, y));
        }
    }

    private void processHarvester(Map map, Tile tile, Unit unit) {
        Point target = new Point(x, y);
        if (tile.getTileType() == TileType.SPICE) {
            unit.setGoal(new Harvest(target));
        } else {
            RefineryEntrance refineryEntrance = map.getRefineryEntranceList().get(target);
            if (refineryEntrance != null) {
                if (refineryEntrance.getOwnerId() == unit.getOwnerId()) {
                    unit.setGoal(new ReturnToBase(refineryEntrance));
                } else {
                    unit.setGoal(new RepetetiveMove(x, y));
                }
            } else {
                unit.setGoal(new RepetetiveMove(x, y));
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
