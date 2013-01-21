package lv.k2611a.util;

import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import org.junit.Test;

import lv.k2611a.domain.Map;

import static junit.framework.Assert.assertEquals;

public class AStarTest {
    @Test
    public void testAStar() {
        Map map = new Map(50,50);

        Unit unit = new Unit();
        unit.setUnitType(UnitType.HARVESTER);
        unit.setOwnerId(1);
        unit.setX(49);
        unit.setY(49);
        map.addUnit(unit);

        assertEquals(49, getPathLengthTo(unit, map, 0,0));
        assertEquals(49, getPathLengthTo(unit, map, 0, 49));
        assertEquals(49, getPathLengthTo(unit, map, 49, 0));

        assertEquals(1, getPathLengthTo(unit, map, 48, 49));
        assertEquals(1, getPathLengthTo(unit, map, 49, 48));
        assertEquals(1, getPathLengthTo(unit, map, 48, 48));

        assertEquals(0, getPathLengthTo(unit, map, 49, 49));
    }

    private int getPathLengthTo(Unit unit, Map map, int toX, int toY) {
        return new AStar().calcPathHarvester(unit, map, toX, toY).size();
    }

    @Test
    public void testAStarPerformanceUnits() {
        Map map = new Map(256,256);
        long startTime = System.currentTimeMillis();
        int iterationCount = 100;
        int totalMoveCount = 0;
        Unit unit = new Unit();
        unit.setUnitType(UnitType.BATTLE_TANK);
        unit.setOwnerId(1);
        unit.setX(0);
        unit.setY(0);
        map.addUnit(unit);
        for (int i = 0; i < iterationCount; i++) {
            int result = new AStar().calcPathEvenIfBlocked(unit, map, 255, 255, 0).size();
            totalMoveCount += result;
        }
        long endTime = System.currentTimeMillis();
        long delta = endTime - startTime;
        double timeForIteration = (double)delta / iterationCount;
        System.out.println("calcPathEvenIfBlocked time "
                + timeForIteration + " ms. average move count " + totalMoveCount / iterationCount);
    }

    @Test
    public void testAStarPerformanceHarvesters() {
        Map map = new Map(256,256);
        long startTime = System.currentTimeMillis();
        int travelCoordinate = 133;
        int iterationCount = 100;
        int totalMoveCount = 0;
        Unit unit = new Unit();
        unit.setUnitType(UnitType.HARVESTER);
        unit.setOwnerId(1);
        unit.setX(0);
        unit.setY(0);
        map.addUnit(unit);
        for (int i = 0; i < iterationCount; i++) {
            int result = new AStar().calcPathHarvester(unit, map, travelCoordinate, travelCoordinate).size();
            totalMoveCount += result;
        }
        long endTime = System.currentTimeMillis();
        long delta = endTime - startTime;
        double timeForIteration = (double)delta / iterationCount;
        System.out.println("calcPathHarvester time for " + travelCoordinate + " tiles "
                + timeForIteration + " ms. average move count " + totalMoveCount / iterationCount);
    }
}
