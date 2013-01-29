package lv.k2611a.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import lv.k2611a.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AStar {
    private static final Logger log = LoggerFactory.getLogger(AStar.class);
    private HashSet<Node> closedSet;
    private SortedSet<Node> openSet;

    public static double UNIT_HEURISTICS = 3.0;
    public static double OCCUPIED_TILE_COST = 3.5;
    public static int PATH_LENGTH = 20;
    public static double HARVESTER_HEURISTICS = 2.0;

    public List<Node> calcPathEvenIfBlocked(Unit unit, Map map, int toX, int toY, double requiredRangeToGoal) {

        if ((unit.getX() == toX) && (unit.getY() == toY)) {
            return new ArrayList<Node>();
        }

        Node start = new Node(unit.getX(), unit.getY());
        Node goal = new Node(toX, toY);

        int currentDirection;
        closedSet = new HashSet<Node>();
        openSet = createSortedNodeSet();

        start.setDistanceFromStart(0);
        start.setHeuristicDistanceFromGoal(UNIT_HEURISTICS * Map.getDistanceBetween(start, goal));

        double expectedTravelDistance = Map.getDistanceBetween(start, goal) - PATH_LENGTH;
        double requiredDistanceToGoal = Math.max(requiredRangeToGoal, expectedTravelDistance);

        openSet.add(start);
        while (!openSet.isEmpty()) {
            Node current = openSet.first();

            if (Map.getDistanceBetween(current, goal) <= requiredDistanceToGoal) {
                return reconstructPath(current);
            }

            openSet.remove(current);
            closedSet.add(current);

            // 150 iterations should be enough, as we will return the found path for the units even if we ran out of nodes.
            if (closedSet.size() >= 150) {
                log.warn("Maximum AStar iteration count reached for a unit");
                Node bestNode = getBestNode(closedSet);
                if (bestNode.getHeuristicDistanceFromGoal() > start.getHeuristicDistanceFromGoal()) {
                    return null;
                }
                return reconstructPath(getBestNode(closedSet));
            }
            if(current.getPreviousNode() != null) {
                currentDirection = ViewDirection.getDirection(current.getPreviousNode().getPoint(), current.getPoint()).getAngle();
            } else {
                currentDirection = unit.getViewDirection().getAngle();
            }

            for (Node neighbor : map.getTileNeighbourNodes(current.getX(), current.getY())) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                boolean pathPassable = map.isPassable(neighbor);
                if (pathPassable) {
                    int neighborDirection = ViewDirection.getDirection(current.getPoint(), neighbor.getPoint()).getAngle();
                    double neighborDistanceFromStart = current.getDistanceFromStart()
                            + getPathCost(unit.getId(), map, current, neighbor) * unit.getUnitType().getSpeed()
                            + getRequiredTicksToTurn(currentDirection, neighborDirection) * unit.getUnitType().getTurnSpeed();
                    if (!openSet.contains(neighbor)) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(neighborDistanceFromStart
                                + UNIT_HEURISTICS * Map.getDistanceBetween(neighbor, goal) * unit.getUnitType().getSpeed() + getHeuristicSalt(neighbor));
                        openSet.add(neighbor);
                    } else if (neighborDistanceFromStart < current.getDistanceFromStart()) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(neighborDistanceFromStart
                                + UNIT_HEURISTICS * Map.getDistanceBetween(neighbor, goal) * unit.getUnitType().getSpeed() + getHeuristicSalt(neighbor));
                    }
                } else {
                    neighbor.setHeuristicDistanceFromGoal(Double.MAX_VALUE / 2 + getHeuristicSalt(neighbor));
                    closedSet.add(neighbor);
                }
            }
        }
        return reconstructPath(getBestNode(closedSet));
    }

    public List<Node> calcPathHarvester(Unit unit, Map map, int toX, int toY) {

        if ((unit.getX() == toX) && (unit.getY() == toY)) {
            return new ArrayList<Node>();
        }

        Node start = new Node(unit.getX(), unit.getY());
        Node goal = new Node(toX, toY);

        RefineryEntrance refineryEntrance = map.getRefineryEntranceList().get(start.getPoint());

        if (!map.isUnoccupied(goal, unit)) {
            return new ArrayList<Node>();
        }

        int currentDirection;
        closedSet = new HashSet<Node>();
        openSet = createSortedNodeSet();

        start.setDistanceFromStart(0);
        start.setHeuristicDistanceFromGoal(HARVESTER_HEURISTICS * Map.getDistanceBetween(start, goal) * unit.getUnitType().getSpeed());

        openSet.add(start);
        while (!openSet.isEmpty()) {
            Node current = openSet.first();

            if (current.equals(goal)) {
                return reconstructPath(current);
            }

            openSet.remove(current);
            closedSet.add(current);

            // 400 iterations should be enough for harvesters, as we need to find full route to destination.
            // This is about 133 sectors in a line, which should be enough for a map of size 64x64 worst-case scenario
            // For now...
            if (closedSet.size() >= 400) {
                log.warn("AStar maximum iteration count reached. Harvester route to base not found. Return null");
                return new ArrayList<Node>();
            }

            if(current.getPreviousNode() != null) {
                currentDirection = ViewDirection.getDirection(current.getPreviousNode().getPoint(), current.getPoint()).getAngle();
            } else {
                currentDirection = unit.getViewDirection().getAngle();
            }

            for (Node neighbor : map.getTileNeighbourNodes(current.getX(), current.getY())) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                boolean isUnoccupied = map.isUnoccupiedAStar(refineryEntrance, neighbor, unit);
                if (isUnoccupied) {
                    int neighborDirection = ViewDirection.getDirection(current.getPoint(), neighbor.getPoint()).getAngle();
                    double neighborDistanceFromStart = current.getDistanceFromStart()
                            + Map.getDistanceBetween(current, neighbor) * unit.getUnitType().getSpeed()
                            + getRequiredTicksToTurn(currentDirection, neighborDirection) * unit.getUnitType().getTurnSpeed();
                    if (!openSet.contains(neighbor)) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(neighborDistanceFromStart
                                + HARVESTER_HEURISTICS * Map.getDistanceBetween(neighbor, goal) * unit.getUnitType().getSpeed() + getHeuristicSalt(neighbor));
                        openSet.add(neighbor);
                    } else if (neighborDistanceFromStart < current.getDistanceFromStart()) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(neighborDistanceFromStart
                                + HARVESTER_HEURISTICS * Map.getDistanceBetween(neighbor, goal) * unit.getUnitType().getSpeed() + getHeuristicSalt(neighbor));
                    }
                } else {
                    neighbor.setHeuristicDistanceFromGoal(Double.MAX_VALUE / 2  + getHeuristicSalt(neighbor));
                    closedSet.add(neighbor);
                }
            }
        }
        log.warn("Harvester route not found, openSet empty. Return null");
        return new ArrayList<Node>();
    }

    private Node getBestNode(HashSet<Node> closedSet) {
        Node bestNode = null;
        for (Node node : closedSet) {
            if (node.getPreviousNode() == null) {
                continue;
            }
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

    private double getHeuristicSalt(Node node) {
        return getHeuristicSalt(node.getX(), node.getY());
    }

    private double getHeuristicSalt(double x, double y) {
        return x * 0.001 + y * 0.000001;
    }

    private TreeSet<Node> createSortedNodeSet() {
        return new TreeSet<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                int compareResult = Double.compare(o1.getHeuristicDistanceFromGoal(), o2.getHeuristicDistanceFromGoal());
                if (compareResult != 0) {
                    return compareResult;
                } else {
                    compareResult = o1.getX() - o2.getX();
                    if (compareResult != 0) {
                        return compareResult;
                    } else {
                        compareResult = o1.getY() - o2.getY();
                        return compareResult;
                    }
                }
            }
        });
    }

    private List<Node> reconstructPath(Node current) {
        List<Node> result = new ArrayList<Node>();
        if (current == null) {
            return result;
        }
        while (!(current.getPreviousNode() == null)) {
            result.add(current);
            current = current.getPreviousNode();
        }
        Collections.reverse(result);
        return result;
    }

    public static boolean pathExists(Unit unit, Map map, Point targetPoint) {
        AStar aStar = new AStar();
        List<Node> path = aStar.calcPathHarvester(unit, map, targetPoint.getX(), targetPoint.getY());
        return !path.isEmpty();
    }

    // Calculates the cost of passing the tile. If the tile contains a unit, the cost of passing this tile is higher.
    private static double getPathCost(int unitId, Map map, Node node1, Node node2) {
        Tile targetTile = map.getTile(node2.getX(), node2.getY());
        if (targetTile.isUnoccupied(unitId)) {
            return Map.getDistanceBetween(node1, node2);
        }
        return Map.getDistanceBetween(node1, node2) + OCCUPIED_TILE_COST;
    }

    private int getRequiredTicksToTurn(int currentAngle, int goalAngle) {
        if (currentAngle == goalAngle) {
            return 0;
        }
        int turnLeft;
        int turnRight;
        if (currentAngle > goalAngle) {
            turnLeft = currentAngle - goalAngle;
            turnRight = ViewDirection.VIEW_DIRECTION_DEGREES + goalAngle - currentAngle;
        } else {
            turnLeft = ViewDirection.VIEW_DIRECTION_DEGREES + currentAngle - goalAngle;
            turnRight = goalAngle - currentAngle;
        }
        if (turnLeft > turnRight) {
            return ViewDirection.ticksToTurn(turnRight);
        } else {
            return ViewDirection.ticksToTurn(turnLeft);
        }
    }
}
