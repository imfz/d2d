package lv.k2611a.domain.goals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Tile;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.ViewDirection;
import lv.k2611a.util.Node;
import lv.k2611a.util.Point;

public class Move implements Goal {

    private int goalX;
    private int goalY;
    private List<Node> path;

    private List<Node> closedSet = new ArrayList<Node>();
    private List<Node> openSet = new ArrayList<Node>();

    @Override
    public Goal copy() {
        Move copy = new Move(goalX, goalY);
        if (path != null) {
            copy.path = new ArrayList<Node>(path);
        }
        copy.closedSet = new ArrayList<Node>(closedSet);
        copy.openSet = new ArrayList<Node>(openSet);
        return copy;
    }

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
    public void onSet(Unit unit) {
        unit.setTicksMovingToNextCell(0);
        path = null;
    }

    @Override
    public void process(Unit unit, Map map) {
        if (path == null) {
            path = calcShortestPath(unit.getX(), unit.getY(), map, unit.getId());
            lookAtNextNode(unit);
        }
        if (path.isEmpty()) {
            unit.removeGoal(this);
            return;
        }
        int ticksToNextCell = unit.getUnitType().getSpeed();
        if (unit.getTicksMovingToNextCell() >= ticksToNextCell - 1) {
            // moved to new cell
            unit.setTicksMovingToNextCell(0);
            Node next = path.get(0);
            unit.setX(next.getX());
            unit.setY(next.getY());
            path.remove(next);
            if (!(path.isEmpty())) {
                next = path.get(0);
                // recalc path if we hit an obstacle
                if (map.isObstacle(next, unit.getId())) {
                    path = calcShortestPath(unit.getX(), unit.getY(), map, unit.getId());
                }
                lookAtNextNode(unit);
            }
        } else {
            if (!path.isEmpty()) {
                Node next = path.get(0);
                if (map.isObstacle(next, unit.getId())) {
                    path = calcShortestPath(unit.getX(), unit.getY(), map, unit.getId());
                    lookAtNextNode(unit);
                } else {
                    unit.setTicksMovingToNextCell(unit.getTicksMovingToNextCell() + 1);
                }
            }
        }
        if (path.isEmpty()) {
            unit.removeGoal(null);
        }

    }

    private void lookAtNextNode(Unit unit) {
        if (path.isEmpty()) {
            return;
        }
        Node next = path.get(0);
        unit.setViewDirection(ViewDirection.getDirection(unit.getX(), unit.getY(), next.getX(), next.getY()));
    }

    List<Node> calcShortestPath(int fromX, int fromY, Map map, long unitId) {
        Node start = new Node(fromX, fromY);
        Node goal = new Node(goalX, goalY);

        if (map.isObstacle(goal, unitId)) {
            return new ArrayList<Node>();
        }

        closedSet = new ArrayList<Node>();
        openSet = new ArrayList<Node>();

        start.setDistanceFromStart(0);


        openSet.add(start);
        while (openSet.size() != 0) {
            Node current = getBestNode(openSet);

            if (current.equals(goal)) {
                return reconstructPath(current);
            }

            openSet.remove(current);
            closedSet.add(current);

            for (Node neighbor : tilesToNodes(map.getTileNeighbours(current.getX(), current.getY()))) {
                boolean neighborIsBetter;
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                if (!map.isObstacle(neighbor, unitId)) {
                    double neighborDistanceFromStart = current.getDistanceFromStart() + Map.getDistanceBetween(current, neighbor);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                        neighborIsBetter = true;
                    } else if (neighborDistanceFromStart < current.getDistanceFromStart()) {
                        neighborIsBetter = true;
                    } else {
                        neighborIsBetter = false;
                    }
                    if (neighborIsBetter) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(Map.getDistanceBetween(neighbor, goal));
                    }
                }

            }
        }
        return new ArrayList<Node>();

    }

    private Node getBestNode(List<Node> set) {
        Collections.shuffle(set);
        Node bestNode = null;
        for (Node node : set) {
            if (bestNode == null) {
                bestNode = node;
            } else {
                if (bestNode.getHeuristicDistanceFromGoal() > node.getHeuristicDistanceFromGoal()) {
                    bestNode = node;
                }
            }
        }
        return bestNode;
    }

    private List<Node> reconstructPath(Node current) {
        List<Node> result = new ArrayList<Node>();
        while (!(current.getPreviousNode() == null)) {
            result.add(current);
            current = current.getPreviousNode();
        }
        Collections.reverse(result);
        return result;
    }

    private List<Node> tilesToNodes(List<Tile> tiles) {
        List<Node> result = new ArrayList<Node>();
        for (Tile tile : tiles) {
            result.add(Node.fromTile(tile));
        }
        return result;
    }
}
