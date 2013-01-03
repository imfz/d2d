package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum TileType {
    SAND((byte)1, 10),
    ROCK((byte)2, 10),
    SPICE((byte)3, 10),
    RICH_SPICE((byte)4, 10)
    ;

    private static TileType[] indexByJsId;

    static {
        int maxJsId = 0;
        Set<Byte> idOnJs = new HashSet<Byte>();
        for (TileType tileType : values()) {
            if (tileType.getIdOnJS() > maxJsId) {
                maxJsId = tileType.getIdOnJS();
            }
            if (!idOnJs.add(tileType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id");
            }
        }
        indexByJsId = new TileType[maxJsId + 1];
        for (TileType tileType : values()) {
            indexByJsId[tileType.getIdOnJS()] = tileType;
        }
    }

    private byte idOnJS;
    private int movementCost;
    private boolean allowsBuildings = false;

    private TileType(byte idOnJS, int movementCost) {
        this.idOnJS = idOnJS;
        this.movementCost = movementCost;
    }

    public byte getIdOnJS() {
        return idOnJS;
    }

    public boolean isAllowsBuildings() {
        return allowsBuildings;
    }

    public int getMovementCost() {
        return movementCost;
    }

    public static TileType getByJsId(int idInIs) {
        return indexByJsId[idInIs];
    }
}
