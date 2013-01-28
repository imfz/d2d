package lv.k2611a.domain.unitgoals;

import java.util.ArrayList;
import java.util.List;

import lv.k2611a.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.Node;
import lv.k2611a.util.Point;

public class Chase extends MoveGoal implements UnitGoal {

    private static final Logger log = LoggerFactory.getLogger(Move.class);
    private boolean targetDestroyed = false;
    private boolean targetMoved = false;

    public Chase(Target target) {
        this.target = target;
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        checkIfTargetMovedOrDestroyed(unit, map);
        if (unit.getTicksSpentOnCurrentGoal() == 0) {
            if (targetDestroyed) {
                unit.removeGoal(this);
                return;
            }
            if (goalInAttackRange(unit)) {
                unit.removeGoal(this);
                return;
            }
            if (targetMoved) {
                targetMoved = false;
                calcPath(unit, map);
            }
        } else {
            unit.setTicksSpentOnCurrentGoal(unit.getTicksSpentOnCurrentGoal() + 1);
        }
        if (!checkIfGoalReachable(unit, map)) {
            unit.removeGoal(this);
            return;
        }
        if (checkIfWeNeedToTurn(unit, map, gameService)) {
            return;
        }
        unitCanStartMoving(unit, map);
        checkIfWeReachedNextTile(unit);
        if (!attemptToUnblockIfBlocked(unit, map)) {
            unit.removeGoal(this);
            return;
        }
    }

    protected void calcPath(Unit unit, Map map) {
        path = aStarCache.calcPathEvenIfBlocked(unit, map, target.getPoint().getX(), target.getPoint().getY(), unit.getUnitType().getAttackRange());
    }

    // Attempt to start moving.
    private void unitCanStartMoving(Unit unit, Map map) {
        if (unit.getTicksSpentOnCurrentGoal() == 0) {
            Node next = path.get(0);
            if (map.isUnoccupied(next, unit)) {
                // Start moving to the next tile. Reserve the tile. First come first serve.
                unit.setTicksSpentOnCurrentGoal(unit.getTicksSpentOnCurrentGoal() + 1);
                map.setUsedByUnit(next.getX(), next.getY(), unit.getId());
            } else {
                // If we cannot start moving to next tile, wait and decrease waiting timer.
                ticksBeforeAttemptToUnblock--;
            }
        } else {
            unit.setTicksSpentOnCurrentGoal(unit.getTicksSpentOnCurrentGoal() + 1);
        }
    }

    // Attempt to get the current coordinates of the target.
    // If target moved, raise targetMoved flag and recalculate path as soon as we finish current move.
    // If the target is destroyed, raise targetDestroyed flag and end this goal as soon as we finish current move.
    private void checkIfTargetMovedOrDestroyed(Unit unit, Map map) {
        Point targetCurrentPoint = target.getPoint();
        if (target.getEntity() == Entity.BUILDING) {
            if (map.getBuilding(target.getId()) == null) {
                targetDestroyed = true;
            } else {
                targetCurrentPoint = map.getClosestPoint(map.getBuilding(target.getId()), unit);
            }
        } else if (target.getEntity() == Entity.UNIT) {
            if (map.getUnit(target.getId()) == null) {
                targetDestroyed = true;
            } else {
                targetCurrentPoint = map.getUnit(target.getId()).getPoint();
            }
        }
        if (targetCurrentPoint != target.getPoint()) {
            targetMoved = true;
            target.setPoint(targetCurrentPoint);
        }
    }


    @Override
    public String toString() {
        return "Chase{" +
                "goalX=" + target.getPoint().getX() +
                ", goalY=" + target.getPoint().getY() +
                ", path=" + path +
                ", aStarCache=" + aStarCache +
                '}';
    }
}
