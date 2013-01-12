package lv.k2611a.domain;

import lv.k2611a.util.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ViewDirection {
    TOPLEFT(1, 315) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX()-distance, point.getY()-distance);
        }
    },
    TOP(2, 0) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX(), point.getY()-distance);
        }
    },
    TOPRIGHT(3, 45) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX()+distance, point.getY()-distance);
        }
    },
    RIGHT(4, 90) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX()+distance, point.getY());
        }
    },
    BOTTOMRIGHT(5, 135) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX()+distance, point.getY()+distance);
        }
    },
    BOTTOM(6, 180) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX(), point.getY()+distance);
        }
    },
    BOTTOMLEFT(7, 225) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX()-distance, point.getY()+distance);
        }
    },
    LEFT(8, 270) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX()-distance, point.getY());
        }
    };

    private int idOnJS;
    private int angle;
    private static final Logger log = LoggerFactory.getLogger(ViewDirection.class);
    public static final int VIEW_DIRECTION_STEP = 45;
    public static final int VIEW_DIRECTION_DEGREES = 360;

    private ViewDirection(int idOnJS, int angle) {
        this.idOnJS = idOnJS;
        this.angle = angle;
    }

    public int getIdOnJS() {
        return idOnJS;
    }

    public int getAngle() {
        return angle;
    }

    public ViewDirection turnInDirection(ViewDirection goalDirection) {
        int goalAngle = goalDirection.getAngle();
        int currentAngle = this.getAngle();
        int turnLeft;
        int turnRight;
        if (currentAngle > goalAngle) {
            turnLeft = currentAngle - goalAngle;
            turnRight = VIEW_DIRECTION_DEGREES + goalAngle - currentAngle;
        } else {
            turnLeft = VIEW_DIRECTION_DEGREES + currentAngle - goalAngle;
            turnRight = goalAngle - currentAngle;
        }
        if (turnLeft > turnRight) {
            return getDirectionByAngle(currentAngle + VIEW_DIRECTION_STEP);
        } else {
            return getDirectionByAngle(currentAngle - VIEW_DIRECTION_STEP);
        }
    }

    public static ViewDirection getDirectionByAngle(int newAngle) {
        newAngle = newAngle % VIEW_DIRECTION_DEGREES;
        if (newAngle < 0) {
            newAngle = newAngle + VIEW_DIRECTION_DEGREES;
        }
        for (ViewDirection viewDirection : ViewDirection.values()) {
            if (viewDirection.getAngle() == newAngle) {
                return viewDirection;
            }
        }
        log.warn("Incorrect unit direction - wrong angle:" + newAngle);
        return ViewDirection.TOP;
    }

    public Point apply(Point point) {
        return apply(point,1);
    }

    public abstract Point apply(Point point, int distance);

    public static ViewDirection getDirection(Point point1, Point point2) {
        return getDirection(point1.getX(),point1.getY(),point2.getX(),point2.getY());
    }

    public static ViewDirection getDirection(int x, int y, int toX, int toY) {
        int deltaX = x - toX;
        int deltaY = y - toY;
        if ((deltaX > 0) && (deltaY > 0)) {
            return ViewDirection.TOPLEFT;
        }
        if ((deltaX < 0) && (deltaY > 0)) {
            return ViewDirection.TOPRIGHT;
        }
        if ((deltaX > 0) && (deltaY == 0)) {
            return ViewDirection.LEFT;
        }
        if ((deltaX < 0) && (deltaY == 0)) {
            return ViewDirection.RIGHT;
        }
        if ((deltaX == 0) && (deltaY > 0)) {
            return ViewDirection.TOP;
        }
        if ((deltaX == 0) && (deltaY < 0)) {
            return ViewDirection.BOTTOM;
        }
        if ((deltaX > 0) && (deltaY < 0)) {
            return ViewDirection.BOTTOMLEFT;
        }
        if ((deltaX < 0) && (deltaY < 0)) {
            return ViewDirection.BOTTOMRIGHT;
        }
        return ViewDirection.TOP;
    }

    public static int ticksToTurn(int angle) {
        return angle / VIEW_DIRECTION_STEP;
    }

}
