package lv.k2611a.domain.unitgoals;

import java.util.List;

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
    private int neededRange = 0;

    public Move(int goalX, int goalY) {
        this.goalX = goalX;
        this.goalY = goalY;
    }

    public Move(int goalX, int goalY, int neededRange) {
        this.goalX = goalX;
        this.goalY = goalY;
        this.neededRange = neededRange;
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
            lookAtNextNode(unit);
        }
        if (path == null) {
            log.warn("Recalculated path, but still null");
            return;
        }
        if (path.isEmpty()) {
            unit.removeGoal(this);
            unit.setTicksMovingToNextCell(0);
            return;
        }
        if (path.size() < neededRange) {
            unit.removeGoal(this);
            unit.setTicksMovingToNextCell(0);
            return;
        }
        int ticksToNextCell = unit.getUnitType().getSpeed();
        if (unit.getTicksMovingToNextCell() >= ticksToNextCell - 1) {
            // moved to new cell
            moveUnit(unit);
            Node next;
            if (!(path.isEmpty())) {
                next = path.get(0);
                // recalc path if we hit an obstacle
                if (map.isObstacle(next, unit.getId(), unit.getOwnerId(), unit.getUnitType() == UnitType.HARVESTER)) {
                    calcPath(unit, map);
                }
                lookAtNextNode(unit);
            }
        } else {
            if (!path.isEmpty()) {
                Node next = path.get(0);
                if (map.isObstacle(next, unit.getId(), unit.getOwnerId(), unit.getUnitType() == UnitType.HARVESTER)) {
                    calcPath(unit, map);
                    lookAtNextNode(unit);
                } else {
                    unit.setTicksMovingToNextCell(unit.getTicksMovingToNextCell() + 1);
                }
            }
        }
        if (path.isEmpty()) {
            unit.removeGoal(null);
            unit.setTicksMovingToNextCell(0);
        }

    }

    private void moveUnit(Unit unit) {
        unit.setTicksMovingToNextCell(0);
        Node next = path.get(0);
        unit.setX(next.getX());
        unit.setY(next.getY());
        path.remove(next);
    }

    private void calcPath(Unit unit, Map map) {
        if (neededRange > 0) {
            path = aStarCache.calcPathEvenIfBlocked(unit.getX(), unit.getY(), goalX, goalY, map, unit.getId(), unit.getUnitType() == UnitType.HARVESTER, unit.getOwnerId());
        } else {
            path = aStarCache.calcShortestPath(unit.getX(), unit.getY(), goalX, goalY, map, unit.getId(), unit.getUnitType() == UnitType.HARVESTER, unit.getOwnerId());
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
}
