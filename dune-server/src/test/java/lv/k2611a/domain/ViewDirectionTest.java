package lv.k2611a.domain;

import org.junit.Test;

import lv.k2611a.util.Point;

import static junit.framework.Assert.assertEquals;

public class ViewDirectionTest {
    @Test
    public void testCorrectDirection() {
        assertEquals(ViewDirection.TOP, ViewDirection.getDirection(0,0,0,-1));
        assertEquals(ViewDirection.LEFT, ViewDirection.getDirection(0,0,-1,0));
        assertEquals(ViewDirection.TOPLEFT, ViewDirection.getDirection(0,0,-1,-1));
        assertEquals(ViewDirection.BOTTOMRIGHT, ViewDirection.getDirection(0,0,1,1));
        assertEquals(ViewDirection.RIGHT, ViewDirection.getDirection(0,0,1,0));
        assertEquals(ViewDirection.BOTTOM, ViewDirection.getDirection(0,0,0,1));
        assertEquals(ViewDirection.BOTTOMLEFT, ViewDirection.getDirection(0,0,-1,1));
        assertEquals(ViewDirection.TOPRIGHT, ViewDirection.getDirection(0,0,1,-1));
        assertEquals(ViewDirection.TOP, ViewDirection.getDirection(0,0,0,0));
    }

    @Test
    public void testCorrectTurning() {
        int testRangeConstant = 20;
        ViewDirection viewDirections[] = new ViewDirection[testRangeConstant*2];
        // First part of test is left turn(-45 degrees) by using an angle -90 degrees from current
        for (int i = -testRangeConstant; i < testRangeConstant; i++) {
            viewDirections[i+testRangeConstant] = ViewDirection.getDirectionByAngle(ViewDirection.VIEW_DIRECTION_STEP*i);
            viewDirections[i+testRangeConstant] =
                         viewDirections[i+testRangeConstant].turnInDirection(
                            ViewDirection.getDirectionByAngle(ViewDirection.VIEW_DIRECTION_STEP*(i-2)));
            assertEquals(viewDirections[i+testRangeConstant].getAngle(),
                         ViewDirection.getDirectionByAngle(ViewDirection.VIEW_DIRECTION_STEP*(i-1)).getAngle());
        }
        // Second part of test is right turn(+45 degrees) by using an angle +90 degrees from current
        for (int i = -testRangeConstant; i < testRangeConstant; i++) {
            viewDirections[i+testRangeConstant] = ViewDirection.getDirectionByAngle(ViewDirection.VIEW_DIRECTION_STEP*i);
            viewDirections[i+testRangeConstant] =
                         viewDirections[i+testRangeConstant].turnInDirection(
                            ViewDirection.getDirectionByAngle(ViewDirection.VIEW_DIRECTION_STEP*(i+2)));
            assertEquals(viewDirections[i+testRangeConstant].getAngle(),
                         ViewDirection.getDirectionByAngle(ViewDirection.VIEW_DIRECTION_STEP*(i+1)).getAngle());
        }
    }


    @Test
    public void testApplyIsCorrect() {
        Point point = new Point(0,0);
        for (ViewDirection viewDirection : ViewDirection.values()) {
            point = viewDirection.apply(point);
        }
        assertEquals(0,point.getX());
        assertEquals(0,point.getY());
    }
}
