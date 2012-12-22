package lv.k2611a.domain;

public enum BuildingType {

    SILO(1,2,2,50),
    REFINERY(2,3,2,50),
    POWERPLANT(3,2,2,50);

    private final int idOnJS;
    private final int width;
    private final int height;
    private final int hp;

    private BuildingType(int idOnJS, int width, int height, int hp) {
        this.idOnJS = idOnJS;
        this.width = width;
        this.height = height;
        this.hp = hp;
    }

    public int getIdOnJS() {
        return idOnJS;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getHp() {
        return hp;
    }
}
