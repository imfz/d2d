package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum TileType {
    SAND(1),ROCK(2),SPICE(3),RICH_SPICE(4);

    private static TileType[] indexByJsId;

    static {
        int maxJsId = 0;
        Set<Integer> idsOnJs = new HashSet<Integer>();
        for (TileType tileType : values()) {
            if (tileType.getIdOnJS() > maxJsId) {
                maxJsId = tileType.getIdOnJS();
            }
            if (!idsOnJs.add(tileType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id");
            }
        }
        indexByJsId = new TileType[maxJsId + 1];
        for (TileType tileType : values()) {
            indexByJsId[tileType.getIdOnJS()] = tileType;
        }
    }

    private int idOnJS;
    private boolean allowsBuildings = false;

    private TileType(int idOnJS) {
        this.idOnJS = idOnJS;
    }

    public int getIdOnJS() {
        return idOnJS;
    }

    public boolean isAllowsBuildings() {
        return allowsBuildings;
    }

    public static TileType getByJsId(int idInIs) {
        return indexByJsId[idInIs];
    }
}
