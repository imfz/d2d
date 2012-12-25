package lv.k2611a.util;

import java.util.HashSet;
import java.util.Set;

public class MapUtils {

    public enum IntersectionType {
        NONE,INTERSECT,NEARBY
    }

    public static IntersectionType getIntersectionType(int x, int y, int width, int height, int x2, int y2, int width2, int height2) {

        int p1X = x;
        int p1Y = y;
        int p2X = x + width;
        int p2Y = y + height;

        int p3X = x2;
        int p3Y = y2;
        int p4X = x2 + width2;
        int p4Y = y2 + height2;


        if (!( p2Y < p3Y || p1Y > p4Y || p2X < p3X || p1X > p4X )) {
            Set<Point> pointSet = new HashSet<Point>();
            for (int i = x; i < x + width; i++) {
                for (int j = y; j < y + height; j++) {
                    Point point = new Point(i,j);
                    pointSet.add(point);
                }
            }
            for (int i = x2; i < x2 + width2; i++) {
                for (int j = y2; j < y2 + height2; j++) {
                    Point point = new Point(i,j);
                    if (!pointSet.add(point)) {
                        return IntersectionType.INTERSECT;
                    }
                }
            }
            return IntersectionType.NEARBY;
        }

        return IntersectionType.NONE;
    }
}
