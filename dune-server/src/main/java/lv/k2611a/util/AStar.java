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

    public static final double OCCUPIED_TILE_COST = 200.0;
    public static final double DEPTH_OF_PATH = 320.0;

    public List<Node> calcPathEvenIfBlocked(Unit unit, Map map, int toX, int toY, int goalRadius) {

        if ((unit.getX() == toX) && (unit.getY() == toY)) {
            return new ArrayList<Node>();
        }

        Node start = new Node(unit.getX(), unit.getY());
        Node goal = new Node(toX, toY);

        closedSet = new HashSet<Node>();
        openSet = createSortedNodeSet();

        start.setDistanceFromStart(0);
        start.setHeuristicDistanceFromGoal(3 * Map.getDistanceBetween(start, goal) * TileType.SAND.getMovementCost());
        double maximumDepthOfPath = start.getHeuristicDistanceFromGoal() - DEPTH_OF_PATH * TileType.SAND.getMovementCost();

        openSet.add(start);
        while (!openSet.isEmpty()) {
            Node current = openSet.first();

            if (Map.getDistanceBetween(current, goal) <= goalRadius || current.getHeuristicDistanceFromGoal() <= maximumDepthOfPath) {
                return reconstructPath(current);
            }

            openSet.remove(current);
            closedSet.add(current);

            // 200 iterations should be enough, as we will return the found path for the units even if we ran out of nodes.
            if (closedSet.size() >= 200) {
                log.warn("AStar maximum iteration count reached");
                return reconstructPath(getBestNode(closedSet));
            }

            for (Node neighbor : map.getTileNeighbourNodes(current.getX(), current.getY())) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                boolean pathPassable = map.isPassable(neighbor);
                if (pathPassable) {
                    double neighborDistanceFromStart = current.getDistanceFromStart()
                            + getPathCost(unit.getId(), map, current, neighbor);
                    if (!openSet.contains(neighbor)) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(neighborDistanceFromStart
                                + 3 * Map.getDistanceBetween(neighbor, goal) * TileType.SAND.getMovementCost());
                        openSet.add(neighbor);
                    } else if (neighborDistanceFromStart < current.getDistanceFromStart()) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(neighborDistanceFromStart
                                + 3 * Map.getDistanceBetween(neighbor, goal) * TileType.SAND.getMovementCost());
                    }
                } else {
                    neighbor.setHeuristicDistanceFromGoal(Double.MAX_VALUE);
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

        if (!map.isUnoccupied(goal, unit.getId(), unit.getOwnerId(), unit.getUnitType() == UnitType.HARVESTER)) {
            return new ArrayList<Node>();
        }

        closedSet = new HashSet<Node>();
        openSet = createSortedNodeSet();

        start.setDistanceFromStart(0);
        start.setHeuristicDistanceFromGoal(Map.getDistanceBetween(start, goal) * TileType.SAND.getMovementCost());

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

            for (Node neighbor : map.getTileNeighbourNodes(current.getX(), current.getY())) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                boolean isUnoccupied = map.isUnoccupiedHarvesterAStar(start, neighbor, unit.getId(), unit.getOwnerId(), unit.getUnitType() == UnitType.HARVESTER);
                // boolean isUnoccupied = map.isUnoccupied(neighbor, unit.getId(), unit.getOwnerId(), unit.getUnitType() == UnitType.HARVESTER);
                if (isUnoccupied) {
                    double neighborDistanceFromStart = current.getDistanceFromStart()
                            + Map.getDistanceBetween(current, neighbor) * TileType.SAND.getMovementCost();
                    if (!openSet.contains(neighbor)) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(neighborDistanceFromStart
                                + 2 * Map.getDistanceBetween(neighbor, goal) * TileType.SAND.getMovementCost());
                        openSet.add(neighbor);
                    } else if (neighborDistanceFromStart < current.getDistanceFromStart()) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(neighborDistanceFromStart
                                + 2 * Map.getDistanceBetween(neighbor, goal) * TileType.SAND.getMovementCost());
                    }
                } else {
                    neighbor.setHeuristicDistanceFromGoal(Double.MAX_VALUE);
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

    private TreeSet<Node> createSortedNodeSet() {
        return new TreeSet<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Double.compare(o1.getHeuristicDistanceFromGoal(), o2.getHeuristicDistanceFromGoal());
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
    public static double getPathCost(int unitId, Map map, Node node1, Node node2) {
        Tile targetTile = map.getTile(node2.getX(), node2.getY());
        if (targetTile.isUnoccupied(unitId)) {
            return map.getDistanceBetween(node1, node2) * targetTile.getTileType().getMovementCost();
        }
        return map.getDistanceBetween(node1, node2) * targetTile.getTileType().getMovementCost() + AStar.OCCUPIED_TILE_COST;
    }
}
