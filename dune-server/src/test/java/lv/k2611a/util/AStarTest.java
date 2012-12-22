package lv.k2611a.util;

import org.junit.Test;

import lv.k2611a.domain.Map;

import static junit.framework.Assert.assertEquals;

public class AStarTest {
    @Test
    public void testAStar() {
        Map map = new Map(50,50);
        assertEquals(49, getPathLengthTo(map, 49, 49, 0,0));
        assertEquals(49, getPathLengthTo(map, 49, 49, 0, 49));
        assertEquals(49, getPathLengthTo(map, 49, 49, 49, 0));

        assertEquals(1, getPathLengthTo(map, 49, 49, 48, 49));
        assertEquals(1, getPathLengthTo(map, 49, 49, 49, 48));
        assertEquals(1, getPathLengthTo(map, 49, 49, 48, 48));

        assertEquals(0, getPathLengthTo(map, 49, 49, 49, 49));
    }

    private int getPathLengthTo(Map map, int fromX, int fromY, int toX, int toY) {
        map.clearUsageFlag();
        return new AStar().calcShortestPath(fromX, fromY, toX, toY, map,-1).size();
    }

    @Test
    public void testAStarPerformance() {
        Map map = new Map(256,256);
        long startTime = System.currentTimeMillis();
        int iterationCount = 100;
        int totalMoveCount = 0;
        for (int i = 0; i < iterationCount; i++) {
            int result = new AStar().calcShortestPath(0, 0, 255, 255, map,-1).size();
            totalMoveCount += result;
        }
        long endTime = System.currentTimeMillis();
        long delta = endTime - startTime;
        double timeForIteration = (double)delta / iterationCount;
        System.out.println("A star time for iteration " + timeForIteration + " ms " + " total move count " + totalMoveCount);
    }
}
