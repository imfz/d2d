package lv.k2611a.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static lv.k2611a.util.MapUtils.IntersectionType.INTERSECT;
import static lv.k2611a.util.MapUtils.IntersectionType.NEARBY;
import static lv.k2611a.util.MapUtils.IntersectionType.NONE;

public class MapUtilsTest {
    @Test
    public void testNoIntersectionType() {
        testIntersect(NONE, 1, 1, 1 ,1,     3, 1, 1, 1);
        testIntersect(NONE, 1, 1, 1 ,1,     1, 3, 1, 1);
        testIntersect(NONE, 2, 1, 1 ,1,     4, 1, 1, 1);
        testIntersect(NONE, 1, 2, 1 ,1,     1, 4, 1, 1);

        testIntersect(NONE, 1, 1, 2 ,1,     4, 1, 1, 1);
        testIntersect(NONE, 1, 1, 1 ,2,     1, 4, 1, 1);
        testIntersect(NONE, 2, 1, 2 ,1,     5, 1, 1, 1);
        testIntersect(NONE, 1, 2, 1 ,2,     1, 5, 1, 1);

    }

    @Test
    public void testNearbyIntersectionType() {
        testIntersect(NEARBY, 1, 1, 1 ,1,     2, 1, 1, 1);
        testIntersect(NEARBY, 1, 1, 1 ,1,     1, 2, 1, 1);
        testIntersect(NEARBY, 2, 1, 1 ,1,     3, 1, 1, 1);
        testIntersect(NEARBY, 1, 2, 1 ,1,     1, 3, 1, 1);

        testIntersect(NEARBY, 1, 1, 2 ,1,     3, 1, 1, 1);
        testIntersect(NEARBY, 1, 1, 1 ,2,     1, 3, 1, 1);
        testIntersect(NEARBY, 2, 1, 2 ,1,     4, 1, 1, 1);
        testIntersect(NEARBY, 1, 2, 1 ,2,     1, 4, 1, 1);

    }

    @Test
    public void testIntersectIntersectionType() {
        testIntersect(INTERSECT, 1, 1, 1 ,1,     1, 1, 1, 1);
        testIntersect(INTERSECT, 2, 1, 1 ,1,     2, 1, 1, 1);
        testIntersect(INTERSECT, 1, 2, 1 ,1,     1, 2, 1, 1);

        testIntersect(INTERSECT, 1, 1, 2 ,1,     2, 1, 1, 1);
        testIntersect(INTERSECT, 1, 1, 1 ,2,     1, 2, 1, 1);
        testIntersect(INTERSECT, 2, 1, 2 ,1,     3, 1, 1, 1);
        testIntersect(INTERSECT, 1, 2, 1 ,2,     1, 3, 1, 1);

    }

    private void testIntersect(MapUtils.IntersectionType intersectionType, int x, int y, int width, int height, int x2, int y2, int width2, int height2) {
        assertEquals(
                intersectionType,
                MapUtils.getIntersectionType(
                x,y,width,height,
                x2,y2,width2,height2

        ));
        assertEquals(
                intersectionType,
                MapUtils.getIntersectionType(
                        x2,y2,width2,height2,
                        x,y,width,height

                ));
    }


}
