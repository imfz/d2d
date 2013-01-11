package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum TileType {
    SAND((byte)1),
    ROCK((byte)2),
    SPICE((byte)3),
    RICH_SPICE((byte)4)
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

    private TileType(byte idOnJS) {
        this.idOnJS = idOnJS;
    }

    public byte getIdOnJS() {
        return idOnJS;
    }

    public static TileType getByJsId(int idInIs) {
        return indexByJsId[idInIs];
    }
}
