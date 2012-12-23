package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum ConstructionOption {

    DEVASTATOR(1,4),
    SIEGE_TANK(2,2),
    TANK(3,1),
    LAUNCHER(4,3),

    //TODO: -1 ;[
    POWERPLANT(5,2),
    HARVESTER(6, -1),
    RADAR(7,5),
    CONCRETE(8, -1),
    JEEP(9, -1),
    DEVIATOR(10, -1),
    REFINERY(11, -1),
    FACTORY(12,8),
    ROCKET_TURRET(13, -1),
    SILO(14,1),
    REPAIRSHOP(15,3),
    AIRBASE(16,9),
    WALL(17, -1),
    MCV(18, -1),
    TURRET(19, -1),
    SONIC_TANK(20, -1),
    TRIKE(21, -1);


    static {
        Set<Integer> idsOnJs = new HashSet<Integer>();
        for (ConstructionOption buildingType : values()) {
            if (!idsOnJs.add(buildingType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id" + idsOnJs);
            }
        }
    }

    private final int idOnJS;
    private final int entityToBuildIdOnJs;

    private ConstructionOption(int idOnJS, int entityToBuildIdOnJs) {
        this.idOnJS = idOnJS;
        this.entityToBuildIdOnJs = entityToBuildIdOnJs;
    }

    public int getIdOnJS() {
        return idOnJS;
    }

    public int getEntityToBuildIdOnJs() {
        return entityToBuildIdOnJs;
    }
}
