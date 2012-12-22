package lv.k2611a.domain;

import org.junit.Test;

import lv.k2611a.util.Node;

import static junit.framework.Assert.assertEquals;

public class MapTest {
    @Test
    public void testNeigbourHoods() {
        Map map = new Map(10,10);
        assertEquals(4, map.getTileDirectNeighbours(2,2).size());
        assertEquals(2, map.getTileDirectNeighbours(0,0).size());
        assertEquals(2, map.getTileDirectNeighbours(9,9).size());
        assertEquals(2, map.getTileDirectNeighbours(9,0).size());
        assertEquals(2, map.getTileDirectNeighbours(0,9).size());
        assertEquals(4, map.getTileDirectNeighbours(8,8).size());

        assertEquals(8, map.getTileNeighbours(2, 2).size());
        assertEquals(3, map.getTileNeighbours(0, 0).size());
        assertEquals(3, map.getTileNeighbours(9, 9).size());
        assertEquals(3, map.getTileNeighbours(9, 0).size());
        assertEquals(3, map.getTileNeighbours(0, 9).size());
        assertEquals(8, map.getTileNeighbours(8, 8).size());

        assertEquals(2, map.getTileHorizontalNeighbours(2, 2).size());
        assertEquals(1, map.getTileHorizontalNeighbours(0, 0).size());
        assertEquals(1, map.getTileHorizontalNeighbours(9, 9).size());
        assertEquals(1, map.getTileHorizontalNeighbours(9, 0).size());
        assertEquals(1, map.getTileHorizontalNeighbours(0, 9).size());
        assertEquals(2, map.getTileHorizontalNeighbours(8, 8).size());

        assertEquals(2, map.getTileVerticalNeighbours(2, 2).size());
        assertEquals(1, map.getTileVerticalNeighbours(0, 0).size());
        assertEquals(1, map.getTileVerticalNeighbours(9, 9).size());
        assertEquals(1, map.getTileVerticalNeighbours(9, 0).size());
        assertEquals(1, map.getTileVerticalNeighbours(0, 9).size());
        assertEquals(2, map.getTileVerticalNeighbours(8, 8).size());

    }

    @Test
    public void testDistance() {
        assertEquals(Math.sqrt(1), Map.getDistanceBetween(new Node(0,0), new Node(1,0)), 0.001);
        assertEquals(Math.sqrt(1), Map.getDistanceBetween(new Node(0,0), new Node(0,1)), 0.001);
        assertEquals(Math.sqrt(2), Map.getDistanceBetween(new Node(0,0), new Node(1,1)), 0.001);
        assertEquals(Math.sqrt(200), Map.getDistanceBetween(new Node(0,0), new Node(10,10)), 0.001);
    }
}
