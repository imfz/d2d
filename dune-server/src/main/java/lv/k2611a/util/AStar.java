package lv.k2611a.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import lv.k2611a.domain.Map;

public class AStar {
    private Set<Node> closedSet;
    private SortedSet<Node> openSet;

    public List<Node> calcShortestPath(int fromX, int fromY, int toX, int toY, Map map, long unitId, boolean isHarvester, int ownerId) {

        if ((fromX == toX) && (fromY == toY)) {
            return new ArrayList<Node>();
        }

        Node start = new Node(fromX, fromY);
        Node goal = new Node(toX, toY);

        if (map.isObstacle(goal, unitId, ownerId, isHarvester)) {
            return new ArrayList<Node>();
        }

        closedSet = new HashSet<Node>();
        openSet = new TreeSet<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Double.compare(o1.getHeuristicDistanceFromGoal(), o2.getHeuristicDistanceFromGoal());
            }
        });

        start.setDistanceFromStart(0);


        openSet.add(start);
        while (!openSet.isEmpty()) {
            Node current = openSet.first();

            if (current.equals(goal)) {
                return reconstructPath(current);
            }

            openSet.remove(current);
            closedSet.add(current);

            // 400 iterations should be enough.
            if (closedSet.size() >= 500) {
                return new ArrayList<Node>();
            }

            for (Node neighbor : map.getTileNeighbourNodes(current.getX(), current.getY())) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                if (!map.isObstacle(neighbor, unitId, ownerId, isHarvester)) {
                    double neighborDistanceFromStart = current.getDistanceFromStart() + Map.getDistanceBetween(current, neighbor);

                    if (!openSet.contains(neighbor)) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(Map.getDistanceBetween(neighbor, goal));
                        openSet.add(neighbor);
                    } else if (neighborDistanceFromStart < current.getDistanceFromStart()) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(Map.getDistanceBetween(neighbor, goal));
                    }
                }

            }
        }
        return new ArrayList<Node>();

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

}
