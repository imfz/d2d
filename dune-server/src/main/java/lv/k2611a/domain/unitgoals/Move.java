package lv.k2611a.domain.unitgoals;

import java.util.ArrayList;
import java.util.List;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Tile;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.ViewDirection;
import lv.k2611a.service.GameServiceImpl;
import lv.k2611a.util.AStar;
import lv.k2611a.util.Node;
import lv.k2611a.util.Point;

public class Move implements UnitGoal {

    private int goalX;
    private int goalY;
    private List<Node> path;
    private AStar aStarCache = new AStar();

    public Move(int goalX, int goalY) {
        this.goalX = goalX;
        this.goalY = goalY;
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
        if (path.isEmpty()) {
            unit.removeGoal(this);
            unit.setTicksMovingToNextCell(0);
            return;
        }
        int ticksToNextCell = unit.getUnitType().getSpeed();
        if (unit.getTicksMovingToNextCell() >= ticksToNextCell-1) {
            // moved to new cell
            unit.setTicksMovingToNextCell(0);
            Node next = path.get(0);
            unit.setX(next.getX());
            unit.setY(next.getY());
            path.remove(next);
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

    private void calcPath(Unit unit, Map map) {
        boolean roadExists = false;
        int targetsSegment = map.getTile(goalX, goalY).getPassableSegmentNumber();
        for (Tile tile : map.getTileNeighbours(unit.getX(), unit.getY())) {
            if (tile.getPassableSegmentNumber() == targetsSegment) {
                roadExists = true;
                break;
            }
        }
        if (!roadExists) {
            // no path can be found, segments are mutually separated
            path = new ArrayList<Node>();
            return;
        }
        path = aStarCache.calcShortestPath(unit.getX(), unit.getY(), goalX, goalY, map, unit.getId(), unit.getUnitType() == UnitType.HARVESTER, unit.getOwnerId());
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
