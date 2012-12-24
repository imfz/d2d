package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum TileType {
    SAND(1),ROCK(2),SPICE(3),RICH_SPICE(4);

    static {
        Set<Integer> idsOnJs = new HashSet<Integer>();
        for (TileType tileType : values()) {
            if (!idsOnJs.add(tileType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id");
            }
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
}
