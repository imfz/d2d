package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum ConstructionOption {

    DEVASTATOR(1,UnitType.DEVASTATOR),
    SIEGE_TANK(2,UnitType.SIEGE_TANK),
    TANK(3,UnitType.SIEGE_TANK),
    LAUNCHER(4,UnitType.LAUNCHER),

    POWERPLANT(5,BuildingType.POWERPLANT),
    HARVESTER(6,UnitType.HARVESTER),
    RADAR(7,BuildingType.RADAR),
    CONCRETE(8, BuildingType.CONCRETE),
    JEEP(9, UnitType.JEEP),
    DEVIATOR(10, UnitType.DEVIATOR),
    REFINERY(11, BuildingType.REFINERY),
    FACTORY(12,BuildingType.FACTORY),
    ROCKET_TURRET(13, BuildingType.ROCKET_TURRET),
    SILO(14,BuildingType.SILO),
    REPAIRSHOP(15,BuildingType.REPAIRSHOP),
    AIRBASE(16,BuildingType.AIRBASE),
    WALL(17, BuildingType.WALL),
    MCV(18, UnitType.MCV),
    TURRET(19, BuildingType.TURRET),
    SONIC_TANK(20, UnitType.SONIC_TANK),
    TRIKE(21, UnitType.TRIKE);


    static {
        Set<Integer> idsOnJs = new HashSet<Integer>();
        for (ConstructionOption buildingType : values()) {
            if (!idsOnJs.add(buildingType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id" + idsOnJs);
            }
        }
    }

    private final int idOnJS;
    private final EntityType entityType;

    private ConstructionOption(int idOnJS, EntityType entityType) {
        this.idOnJS = idOnJS;
        this.entityType = entityType;
    }

    public int getIdOnJS() {
        return idOnJS;
    }

    public int getEntityToBuildIdOnJs() {
        return entityType.getIdOnJS();
    }

    public int getCost() {
        return entityType.getCost();
    }

    public String getName() {
        return entityType.getName();
    }

    public BuildingType[] getPrerequisites() {
        return entityType.getPrerequisites();
    }
}
