package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.*;
import lv.k2611a.network.UnitDTO;
import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.AStar;
import lv.k2611a.util.Node;
import lv.k2611a.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class MoveGoal implements UnitGoal {

    protected List<Node> path;
    protected Target target;
    protected AStar aStarCache = new AStar();
    protected int ticksBeforeAttemptToUnblock = 5 + new Random().nextInt(10);

    protected boolean requireUnblocking = false;
    protected int goalUnblockMinimumDistance = 10;
    protected int goalUnblockCheckDepth = 7;
    protected int goalUnblockBlockCountMinimum = 4;

    protected void enableUnblockingIfGoalUnreachable() {
        requireUnblocking = true;
    }

    protected void resetTicksToWait() {
        this.ticksBeforeAttemptToUnblock = 5 + new Random().nextInt(10);
    }

    protected boolean checkIfGoalReachable(Unit unit, Map map) {
        if (path == null) {
            calcPath(unit, map);
        }
        if (path == null) {
            unit.setTicksSpentOnCurrentGoal(0);
            return false;
        }
        if (path.isEmpty()) {
            unit.setTicksSpentOnCurrentGoal(0);
            return false;
        }
        return true;
    }

    // When we have spent enough ticks moving to the target tile, we reach it
    protected void checkIfWeReachedNextTile(Unit unit) {
        Node next = path.get(0);
        int ticksToNextCell = unit.getUnitType().getSpeed();
        if (unit.getTicksSpentOnCurrentGoal() >= ticksToNextCell) {
            unit.setTicksSpentOnCurrentGoal(0);
            resetTicksToWait();
            unit.setX(next.getX());
            unit.setY(next.getY());
            path.remove(next);
            // If we have successfully moved the last planned move, attempt to recalculate the path next tick.
            if (path.isEmpty()) {
                path = null;
            }
        }
    }

    // Handle the turning of the unit. If our view direction does not match calculated move direction, we turn
    protected boolean checkIfWeNeedToTurn(Unit unit, Map map, GameServiceImpl gameService) {
        Node next = path.get(0);
        ViewDirection goalDirection = ViewDirection.getDirection(unit.getPoint(), next.getPoint());
        if (unit.getViewDirection() != goalDirection) {
            unit.insertGoalBeforeCurrent(new Turn(goalDirection));
            unit.getCurrentGoal().process(unit, map, gameService);
            return true;
        }
        return false;
    }

    // If we could not move to the next tile for the past X ticks, attempt to recalculate the path
    protected boolean attemptToUnblockIfBlocked(Unit unit, Map map) {
        if (ticksBeforeAttemptToUnblock <= 0) {
            resetTicksToWait();
            calcPath(unit, map);
            if (requireUnblocking) {
                if (impossibleToUnblockPath(unit, map)) {
                    return false;
                }
            }
        }
        return true;
    }

    // If we are closer than goalUnblockMinimumDistance to the goal and met an impassable tile,
    // we check if the goal is reachable.
    // For that, we check tileCount amount of path elements and see if blockCount amount of tiles are blocked.
    // If they are, we change our goal 1 step closer to us, in order to unwind the blocking.
    protected boolean impossibleToUnblockPath(Unit unit, Map map) {
        double distanceToGoal = Map.getDistanceBetween(unit.getPoint(), target.getPoint());
        if (distanceToGoal <= goalUnblockMinimumDistance) {
            int tileCount = Math.min(goalUnblockCheckDepth, path.size());
            int blockCount = Math.min(goalUnblockBlockCountMinimum, path.size());
            int pathBlockedTileCount = 0;
            for (int pathElementID = path.size()-1; pathElementID >= path.size() - tileCount; pathElementID--) {
                if (!map.isUnoccupied(path.get(pathElementID), unit)) {
                    pathBlockedTileCount++;
                }
            }
            if (pathBlockedTileCount >=blockCount) {
                if (path.size() < 2) {
                    return true;
                }
                target.setPoint((path.get(path.size()-2)).getPoint());
            }
        }
        return false;
    }

    protected void calcPath(Unit unit, Map map) {
        path = aStarCache.calcPathEvenIfBlocked(unit, map, target.getPoint().getX(), target.getPoint().getY(), 0);
    }

    protected boolean goalInAttackRange(Unit unit) {
        double distanceToGoal = Map.getDistanceBetween(unit.getPoint(), target.getPoint());
        if (distanceToGoal <= unit.getUnitType().getAttackRange()) {
            return true;
        }
        return false;
    }

    @Override
    public void reserveTiles(Unit unit, Map map) {
        int ticksToFreeCurrentCell = unit.getUnitType().getSpeed()/2;
        if (unit.getTicksSpentOnCurrentGoal() <= ticksToFreeCurrentCell) {
            map.setUsedByUnit(unit.getX(), unit.getY(), unit.getId());
        }
        if (unit.getTicksSpentOnCurrentGoal() > 0) {
            if (path != null) {
                if (!path.isEmpty()) {
                    Node next = path.get(0);
                    map.setUsedByUnit(next.getX(), next.getY(), unit.getId());
                }
            }
        }
    }

    @Override
    public void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto) {
        double travelledPercents = (double) unit.getTicksSpentOnCurrentGoal() / unit.getUnitType().getSpeed() * 100;
        dto.setTravelledPercents((byte) travelledPercents);
    }

}
