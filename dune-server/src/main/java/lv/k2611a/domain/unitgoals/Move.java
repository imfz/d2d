package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.Node;
import lv.k2611a.util.Point;

public class Move extends MoveGoal implements UnitGoal {

    private static final Logger log = LoggerFactory.getLogger(Move.class);

    public Move(int goalX, int goalY) {
        this.target = new Target(Entity.UNIT, 0, new Point(goalX, goalY));
        enableUnblockingIfGoalUnreachable();
    }

    public Move(Point point) {
        this.target = new Target(Entity.UNIT, 0, point);
        enableUnblockingIfGoalUnreachable();
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (!checkIfGoalReachable(unit, map)) {
            unit.removeGoal(this);
            return;
        }
        if (checkIfWeNeedToTurn(unit, map, gameService)) {
            return;
        }
        if (!unitCanStartMoving(unit, map)) {
            unit.removeGoal(this);
            return;
        }
        checkIfWeReachedNextTile(unit);
        if (!attemptToUnblockIfBlocked(unit, map)) {
            unit.removeGoal(this);
            return;
        }
    }

    // Attempt to start moving.
    private boolean unitCanStartMoving(Unit unit, Map map) {
        if (unit.getTicksSpentOnCurrentGoal() == 0) {
            Node next = path.get(0);
            if (map.isUnoccupied(next, unit)) {
                // Start moving to the next tile. Reserve the tile. First come first serve.
                unit.setTicksSpentOnCurrentGoal(unit.getTicksSpentOnCurrentGoal() + 1);
                map.setUsedByUnit(next.getX(), next.getY(), unit.getId());
            } else {
                // If we cannot start moving to next tile, wait and decrease waiting timer.
                // Harvesters fail immediately.
                if (unit.getUnitType() == UnitType.HARVESTER) {
                    return false;
                } else {
                    ticksBeforeAttemptToUnblock--;
                }
            }
        } else {
            unit.setTicksSpentOnCurrentGoal(unit.getTicksSpentOnCurrentGoal() + 1);
        }
        return true;
    }

    @Override
    protected void calcPath(Unit unit, Map map) {
        if (unit.getUnitType() != UnitType.HARVESTER) {
            path = aStarCache.calcPathEvenIfBlocked(unit, map, target.getPoint().getX(), target.getPoint().getY(), 0);
        } else {
            path = aStarCache.calcPathHarvester(unit, map, target.getPoint().getX(), target.getPoint().getY());
        }
    }

    @Override
    public String toString() {
        return "Move{" +
                "goalX=" + target.getPoint().getX() +
                ", goalY=" + target.getPoint().getY() +
                ", path=" + path +
                ", aStarCache=" + aStarCache +
                '}';
    }
}
