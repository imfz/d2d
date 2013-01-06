package lv.k2611a.domain;

import org.junit.Test;

import lv.k2611a.util.Point;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;

public class MapTest {
    @Test
    public void testNeighbourHoods() {
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
        assertEquals(Math.sqrt(1), Map.getDistanceBetween(new Point(0,0), new Point(1,0)), 0.001);
        assertEquals(Math.sqrt(1), Map.getDistanceBetween(new Point(0,0), new Point(0,1)), 0.001);
        assertEquals(Math.sqrt(2), Map.getDistanceBetween(new Point(0,0), new Point(1,1)), 0.001);
        assertEquals(Math.sqrt(200), Map.getDistanceBetween(new Point(0,0), new Point(10,10)), 0.001);
    }

    @Test
    public void testBuildingsId() {
        Map map = new Map(64,64);


        Building building = new Building();
        building.setType(BuildingType.FACTORY);
        building.setX(1);
        building.setY(2);
        map.addBuilding(building);

        Building building2 = new Building();
        building.setType(BuildingType.FACTORY);
        building.setX(1);
        building.setY(2);
        map.addBuilding(building2);

        assertSame(building, map.getBuilding(building.getId()));
        assertSame(building2, map.getBuilding(building2.getId()));
        assertNull(map.getBuilding(building2.getId() + 1));
        map.removeBuilding(building);
        assertSame(building2, map.getBuilding(building2.getId()));

    }

    @Test
    public void testUnitsById() {
        Map map = new Map(64,64);

        Unit unit = new Unit();
        unit.setUnitType(UnitType.HARVESTER);
        unit.setX(1);
        unit.setY(2);
        map.addUnit(unit);

        Unit unit2 = new Unit();
        unit2.setUnitType(UnitType.HARVESTER);
        unit2.setX(1);
        unit2.setY(2);
        map.addUnit(unit2);

        assertSame(unit, map.getUnit(unit.getId()));
        assertSame(unit2, map.getUnit(unit2.getId()));
        assertNull(map.getUnit(unit2.getId() + 1));
        map.removeUnit(unit);
        assertSame(unit2, map.getUnit(unit2.getId()));

    }

}
