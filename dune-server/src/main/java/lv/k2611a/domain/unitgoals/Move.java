package lv.k2611a.domain.unitgoals;

import java.util.List;
import java.util.Random;

import lv.k2611a.network.UnitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.ViewDirection;
import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.AStar;
import lv.k2611a.util.Node;
import lv.k2611a.util.Point;

public class Move implements UnitGoal {

    private static final Logger log = LoggerFactory.getLogger(Move.class);

    private int goalX;
    private int goalY;
    private List<Node> path;
    private AStar aStarCache = new AStar();
    private int goalUnblockMinimumDistance = 10;
    private int goalUnblockCheckDepth = 7;
    private int goalUnblockBlockCountMinimum = 4;
    private int ticksToWait = 5 + new Random().nextInt(10);

    public Move(int goalX, int goalY) {
        this.goalX = goalX;
        this.goalY = goalY;
    }

    public Move(Point point) {
        this.goalX = point.getX();
        this.goalY = point.getY();
    }

    public void setGoalX(int goalX) {
        this.goalX = goalX;
    }

    public void setGoalY(int goalY) {
        this.goalY = goalY;
    }

    private void resetTicksToWait() {
        this.ticksToWait = 5 + new Random().nextInt(10);
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
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (path == null) {
            calcPath(unit, map);
        }
        if (path == null) {
            unit.removeGoal(this);
            unit.setTicksSpentOnCurrentGoal(0);
            return;
        }
        if (path.isEmpty()) {
            unit.removeGoal(this);
            unit.setTicksSpentOnCurrentGoal(0);
            return;
        }

        // Handle the turning of the unit. If our view direction does not match calculated move direction, we turn
        Node next = path.get(0);
        ViewDirection goalDirection = ViewDirection.getDirection(unit.getPoint(), next.getPoint());
        if (unit.getViewDirection() != goalDirection) {
            unit.insertGoalBeforeCurrent(new Turn(goalDirection));
            return;
        }

        int ticksToNextCell = unit.getUnitType().getSpeed();
        // Attempt to start moving.
        if (unit.getTicksSpentOnCurrentGoal() == 0) {
            if (map.isUnoccupied(next, unit)) {
                // Start moving to the next tile. Reserve the tile. First come first serve.
                unit.setTicksSpentOnCurrentGoal(unit.getTicksSpentOnCurrentGoal() + 1);
                map.setUsedByUnit(next.getX(), next.getY(), unit.getId());
            } else {
                // If we cannot start moving to next tile, wait and decrease waiting timer.
                // Harvesters fail immediately.
                if (unit.getUnitType() == UnitType.HARVESTER) {
                    unit.removeGoal(this);
                    return;
                } else {
                    ticksToWait--;
                }
            }
        } else {
            unit.setTicksSpentOnCurrentGoal(unit.getTicksSpentOnCurrentGoal() + 1);
        }
        // When we have spent enough ticks moving to the target tile, we reach it
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
        // If we could not move to the next tile for the past X ticks, attempt to recalculate the path
        if (ticksToWait <= 0) {
            resetTicksToWait();
            calcPath(unit, map);
            // If we are closer than goalUnblockMinimumDistance to the goal and met an unpassable tile,
            // we check if the goal is reachable.
            // For that, we check tileCount amount of path elements and see if blockCount amount of tiles are blocked.
            // If they are, we change our goal 1 step closer to us, in order to unwind the blocking.
            double distanceToGoal = Map.getDistanceBetween(unit.getPoint(), new Point(goalX,goalY));
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
                    if (path.size() > 1) {
                        Node newGoal = path.get(path.size()-2);
                        setGoalX(newGoal.getX());
                        setGoalY(newGoal.getY());
                    } else {
                        unit.removeGoal(this);
                    }
                }
            }
        }
    }

    private void calcPath(Unit unit, Map map) {
        if (unit.getUnitType() != UnitType.HARVESTER) {
            path = aStarCache.calcPathEvenIfBlocked(unit, map, goalX, goalY, 0);
        } else {
            path = aStarCache.calcPathHarvester(unit, map, goalX, goalY);
        }
    }

    @Override
    public void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto) {
        double travelledPercents = (double) unit.getTicksSpentOnCurrentGoal() / unit.getUnitType().getSpeed() * 100;
        dto.setTravelledPercents((byte) travelledPercents);
    }

    @Override
    public String toString() {
        return "Move{" +
                "goalX=" + goalX +
                ", goalY=" + goalY +
                ", path=" + path +
                ", aStarCache=" + aStarCache +
                '}';
    }
}
