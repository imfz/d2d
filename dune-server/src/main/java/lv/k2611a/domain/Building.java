package lv.k2611a.domain;

public class Building {
    private long id;
    private BuildingType type;
    private int x;
    private int y;
    private int hp;

    public Building copy() {
        Building copy = new Building();
        copy.type = type;
        copy.x = x;
        copy.y = y;
        copy.id = id;
        copy.hp = hp;
        return copy;
    }

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
        this.hp = type.getHp();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public long getId() {
        return id;
    }

    public int getHp() {
        return hp;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
