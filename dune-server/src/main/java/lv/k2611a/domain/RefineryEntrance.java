package lv.k2611a.domain;

import lv.k2611a.util.Point;

public class RefineryEntrance {
    private int ownerId;
    private Point point;

    public RefineryEntrance(int ownerId, Point point) {
        this.ownerId = ownerId;
        this.point = point;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
