package lv.k2611a.domain;

import lv.k2611a.util.Point;

public class RefineryEntrance {
    private int ownerId;
    private Point point;
    private long refineryId;

    public RefineryEntrance(int ownerId, Point point, long refineryId) {
        this.ownerId = ownerId;
        this.point = point;
        this.refineryId = refineryId;
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

    public long getRefineryId() {
        return refineryId;
    }
}
