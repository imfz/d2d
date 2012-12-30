package lv.k2611a.domain;

import lv.k2611a.util.Point;

public enum ViewDirection {
    TOPLEFT(1) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX()-distance, point.getY()-distance);
        }
    },
    TOP(2) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX(), point.getY()-distance);
        }
    },
    TOPRIGHT(3) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX()+distance, point.getY()-distance);
        }
    },
    RIGHT(4) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX()+distance, point.getY());
        }
    },
    BOTTOMRIGHT(5) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX()+distance, point.getY()+distance);
        }
    },
    BOTTOM(6) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX(), point.getY()+distance);
        }
    },
    BOTTOMLEFT(7) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX()-distance, point.getY()+distance);
        }
    },
    LEFT(8) {
        @Override
        public Point apply(Point point, int distance) {
            return new Point(point.getX()-distance, point.getY());
        }
    };

    private int idOnJS;

    private ViewDirection(int idOnJS) {
        this.idOnJS = idOnJS;
    }

    public int getIdOnJS() {
        return idOnJS;
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

        return TOP;
    }

}
