package lv.k2611a.domain;

import lv.k2611a.util.Point;

public class Target implements java.io.Serializable {

    private int id;
    private Entity entity;
    private Point point;

    public Target(Entity entity, int id, Point point) {
        this.id = id;
        this.entity = entity;
        this.point = point;
    }

    public int getId() {
        return id;
    }

    public Entity getEntity() {
        return entity;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
