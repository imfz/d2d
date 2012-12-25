package lv.k2611a.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Tile;

public class AStar {
    private List<Node> closedSet = new ArrayList<Node>();
    private List<Node> openSet = new ArrayList<Node>();

    public AStar copy() {
        AStar copy = new AStar();
        copy.closedSet = new ArrayList<Node>(closedSet);
        copy.openSet = new ArrayList<Node>(openSet);
        return copy;
    }

    public List<Node> calcShortestPath(int fromX, int fromY, int toX, int toY, Map map, long unitId, boolean isHarvester, int ownerId) {
        Node start = new Node(fromX, fromY);
        Node goal = new Node(toX, toY);

        if (map.isObstacle(goal, unitId, ownerId, isHarvester)) {
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

            // 400 iterations should be enough.
            if (closedSet.size() >= 500) {
                return new ArrayList<Node>();
            }

            for (Node neighbor : tilesToNodes(map.getTileNeighbours(current.getX(), current.getY()))) {
                boolean neighborIsBetter;
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                if (!map.isObstacle(neighbor, unitId, ownerId, isHarvester)) {
                    double neighborDistanceFromStart = current.getDistanceFromStart() + Map.getDistanceBetween(current.getPoint(), neighbor.getPoint());

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
                        neighbor.setHeuristicDistanceFromGoal(Map.getDistanceBetween(neighbor.getPoint(), goal.getPoint()));
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
