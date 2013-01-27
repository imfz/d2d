package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum  ExplosionType {

    BIG((byte)1);

    private static ExplosionType[] indexByJsId;

    static {
        int maxJsId = 0;
        Set<Byte> idsOnJs = new HashSet<Byte>();
        for (ExplosionType explosionType : values()) {
            if (explosionType.getIdOnJS() > maxJsId) {
                maxJsId = explosionType.getIdOnJS();
            }
            if (!idsOnJs.add(explosionType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id");
            }
        }
        indexByJsId = new ExplosionType[maxJsId + 1];
        for (ExplosionType explosionType : values()) {
            indexByJsId[explosionType.getIdOnJS()] = explosionType;
        }
    }

    private final byte idOnJS;

    ExplosionType(byte idOnJS) {
        this.idOnJS = idOnJS;
    }

    public byte getIdOnJS() {
        return idOnJS;
    }
}
