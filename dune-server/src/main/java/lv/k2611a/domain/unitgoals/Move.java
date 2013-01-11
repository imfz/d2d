package lv.k2611a.domain.unitgoals;

import java.util.List;

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
    private int goalRadius = 0;
    private MoveExpired moveExpired;

    public Move(int goalX, int goalY) {
        this.goalX = goalX;
        this.goalY = goalY;
    }

    public Move(int goalX, int goalY, int goalRadius, MoveExpired moveExpired) {
        this.goalX = goalX;
        this.goalY = goalY;
        this.goalRadius = goalRadius;
        this.moveExpired = moveExpired;
    }

    public Move(Point point) {
        this.goalX = point.getX();
        this.goalY = point.getY();
    }

    public int getGoalX() {
        return goalX;
    }

    public int getGoalY() {
        return goalY;
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (path == null) {
            calcPath(unit, map);
            // lookAtNextNode(unit);
        }
        if (path == null) {
            log.warn("Recalculated path, but still null");
            return;
        }
        if (path.isEmpty()) {
            unit.removeGoal(this);
            unit.setTicksSpentOnCurrentGoal(0);
            return;
        }

        //HANDLE TURNS
        Node next = path.get(0);
        ViewDirection goalDirection = ViewDirection.getDirection(unit.getPoint(), next.getPoint());
        if (unit.getViewDirection() != goalDirection) {
            unit.insertGoalBeforeCurrent(new Turn(goalDirection));
            return;
        }

        int ticksToNextCell = unit.getUnitType().getSpeed();
        if (unit.getTicksSpentOnCurrentGoal() >= ticksToNextCell - 1) {
            // moved to new cell
            if (!moveUnit(unit, map)) {
                if (path.size() < goalRadius) {
                    unit.removeGoal(this);
                    return;
                }
                if (moveExpired != null) {
                    if (moveExpired.isExpired(this, map)) {
                        unit.removeGoal(this);
                        return;
                    }
                }
                if (!(path.isEmpty())) {
                    next = path.get(0);
                    // recalc path if we hit an obstacle
                    if (!map.isUnoccupied(next, unit.getId(), unit.getOwnerId(), unit.getUnitType() == UnitType.HARVESTER)) {
                        calcPath(unit, map);
                    }
                    // lookAtNextNode(unit);
                }
            }
        } else {
            if (!path.isEmpty()) {
                next = path.get(0);
                if (!map.isUnoccupied(next, unit.getId(), unit.getOwnerId(), unit.getUnitType() == UnitType.HARVESTER)) {
                    calcPath(unit, map);
                } else {
                    unit.setTicksSpentOnCurrentGoal(unit.getTicksSpentOnCurrentGoal() + 1);
                }
                // lookAtNextNode(unit);
            }
        }
        if (path.isEmpty()) {
            unit.removeGoal(null);
            unit.setTicksSpentOnCurrentGoal(0);
        }

    }

    private boolean moveUnit(Unit unit, Map map) {
         unit.setTicksSpentOnCurrentGoal(0);
         Node next = path.get(0);
         if (map.isUnoccupied(next, unit.getId(), unit.getOwnerId(), unit.getUnitType() == UnitType.HARVESTER)) {
            unit.setX(next.getX());
            unit.setY(next.getY());
            path.remove(next);
            return true;
         }
         return false;
    }

    private void calcPath(Unit unit, Map map) {
        //TODO: goalRadius parameter should tell the required radius range from target AStar should plan route to
        //For units MOVING, this should be 0. For units that attempt to get to their max attack range > 0.
        if (unit.getUnitType() != UnitType.HARVESTER) {
            // log.warn("Unit path calc");
            path = aStarCache.calcPathEvenIfBlocked(unit, map, goalX, goalY, goalRadius);
        } else {
            // log.warn("Harvester path calc");
            path = aStarCache.calcPathHarvester(unit, map, goalX, goalY);
        }
    }

    private void lookAtNextNode(Unit unit) {
        if (path.isEmpty()) {
            return;
        }
        Node next = path.get(0);
        unit.setViewDirection(ViewDirection.getDirection(unit.getX(), unit.getY(), next.getX(), next.getY()));
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

    @Override
    public void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto) {
        double travelledPercents = (double) unit.getTicksSpentOnCurrentGoal() / unit.getUnitType().getSpeed() * 100;
        dto.setTravelledPercents((byte) travelledPercents);
    }
}
