package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum BuildingType {

    SILO(1,2,2,50,10),
    REFINERY(2,3,2,50,10),
    POWERPLANT(3,2,2,50,10),
    CONSTRUCTIONYARD(4,2,2,50,10);

    static {
        Set<Integer> idsOnJs = new HashSet<Integer>();
        for (BuildingType buildingType : values()) {
            if (!idsOnJs.add(buildingType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id");
            }
        }
    }

    private final int idOnJS;
    private final int width;
    private final int height;
    private final int hp;
    private final int ticksToBuild;

    private BuildingType(int idOnJS, int width, int height, int hp, int ticksToBuild) {
        this.idOnJS = idOnJS;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.ticksToBuild = ticksToBuild;
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

    public int getTicksToBuild() {
        return ticksToBuild;
    }

    public static BuildingType getByJsId(int idInIs) {
        for (BuildingType buildingType : values()) {
            if (buildingType.getIdOnJS() == idInIs) {
                return buildingType;
            }
        }
        throw new AssertionError("Unknown building type id in js : " + idInIs);
    }
}
