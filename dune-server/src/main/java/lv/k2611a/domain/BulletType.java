package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum BulletType {

    TANK_SHOT((byte)1);

    private static BulletType[] indexByJsId;

    static {
        int maxJsId = 0;
        Set<Byte> idsOnJs = new HashSet<Byte>();
        for (BulletType bulletType : values()) {
            if (bulletType.getIdOnJS() > maxJsId) {
                maxJsId = bulletType.getIdOnJS();
            }
            if (!idsOnJs.add(bulletType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id");
            }
        }
        indexByJsId = new BulletType[maxJsId + 1];
        for (BulletType bulletType : values()) {
            indexByJsId[bulletType.getIdOnJS()] = bulletType;
        }
    }

    private final byte idOnJS;

    private BulletType(byte idOnJS) {
        this.idOnJS = idOnJS;
    }

    public byte getIdOnJS() {
        return idOnJS;
    }

    public static BulletType getByJsId(int idInIs) {
        return indexByJsId[idInIs];
    }
}
