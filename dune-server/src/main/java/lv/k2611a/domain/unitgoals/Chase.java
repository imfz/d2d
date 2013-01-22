package lv.k2611a.domain.unitgoals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lv.k2611a.domain.*;
import lv.k2611a.network.UnitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.AStar;
import lv.k2611a.util.Node;
import lv.k2611a.util.Point;

public class Chase implements UnitGoal {

    private static final Logger log = LoggerFactory.getLogger(Move.class);
    private int targetId;
    private Entity targetEntity;
    private Point goalPoint;
    private List<Node> path;
    private AStar aStarCache = new AStar();
    private int ticksToWait = 5 + new Random().nextInt(10);
    private boolean targetDestroyed = false;
    private boolean targetMoved = false;

    public Chase(Entity targetEntity, int targetId, Point goalPoint) {
        this.targetId = targetId;
        this.targetEntity = targetEntity;
        this.goalPoint = goalPoint;
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
        // Attempt to get the current coordinates of the target.
        // If target moved, raise targetMoved flag and recalculate path as soon as we finish current move.
        // If the target is destroyed, raise targetDestroyed flag and end this goal as soon as we finish current move.
        Point targetPoint = goalPoint;
        if (targetEntity == Entity.BUILDING) {
            if (map.getBuilding(targetId) == null) {
                targetDestroyed = true;
            } else {
                targetPoint = getClosestPoint(map.getBuilding(targetId), unit);
            }
        } else if (targetEntity == Entity.UNIT) {
            if (map.getUnit(targetId) == null) {
                targetDestroyed = true;
            } else {
                targetPoint = map.getUnit(targetId).getPoint();
            }
        }
        if (targetPoint != goalPoint) {
            targetMoved = true;
            goalPoint = targetPoint;
        }
        // If we are in attack range, finish current movement and attempt to shoot.
        // If the target is dead, also remove the current goal as soon as we finish the previous movement.
        if (unit.getTicksSpentOnCurrentGoal() == 0) {
            if (targetDestroyed) {
                unit.removeGoal(this);
            }
            double distanceToGoal = Map.getDistanceBetween(unit.getPoint(), goalPoint);
            if (distanceToGoal <= unit.getUnitType().getAttackRange()) {
                unit.removeGoal(this);
                return;
            }
            if (targetMoved) {
                targetMoved = false;
                calcPath(unit, map);
            }
        }

        if (path == null) {
            calcPath(unit, map);
        }

        if (path == null) {
            unit.removeGoal(this);
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
            unit.getCurrentGoal().process(unit, map, gameService);
            return;
        }

        // Attempt to start moving.
        if (unit.getTicksSpentOnCurrentGoal() == 0) {
            if (map.isUnoccupied(next, unit)) {
                // Start moving to the next tile. Reserve the tile. First come first serve.
                unit.setTicksSpentOnCurrentGoal(unit.getTicksSpentOnCurrentGoal() + 1);
                map.setUsedByUnit(next.getX(), next.getY(), unit.getId());
            } else {
                // If we cannot start moving to next tile, wait and decrease waiting timer.
                ticksToWait--;
            }
        } else {
            unit.setTicksSpentOnCurrentGoal(unit.getTicksSpentOnCurrentGoal() + 1);
        }
        int ticksToNextCell = unit.getUnitType().getSpeed();
        // When we have spent enough ticks moving to the target tile, we reach it
        if (unit.getTicksSpentOnCurrentGoal() >= ticksToNextCell) {
            unit.setTicksSpentOnCurrentGoal(0);
            resetTicksToWait();
            unit.setX(next.getX());
            unit.setY(next.getY());
            path.remove(next);
        }
        // If we could not move to the next tile for the past X ticks, attempt to recalculate the path
        if (ticksToWait <= 0) {
            resetTicksToWait();
            calcPath(unit, map);
        }
    }

    private void calcPath(Unit unit, Map map) {
        path = aStarCache.calcPathEvenIfBlocked(unit, map, goalPoint.getX(), goalPoint.getY(), unit.getUnitType().getAttackRange());
    }

    @Override
    public void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto) {
        double travelledPercents = (double) unit.getTicksSpentOnCurrentGoal() / unit.getUnitType().getSpeed() * 100;
        dto.setTravelledPercents((byte) travelledPercents);
    }

    private Point getClosestPoint(Building building, Unit unit) {
        List<Point> points = new ArrayList<Point>();
        points.add(building.getPoint());
        points.add(building.getPoint2());
        points.add(building.getPoint3());
        points.add(building.getPoint4());

        return Map.getClosestNode(unit.getPoint(), points);
    }

    @Override
    public String toString() {
        return "Chase{" +
                "goalX=" + goalPoint.getX() +
                ", goalY=" + goalPoint.getY() +
                ", path=" + path +
                ", aStarCache=" + aStarCache +
                '}';
    }
}
