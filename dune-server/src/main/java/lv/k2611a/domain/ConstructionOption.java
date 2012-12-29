package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum ConstructionOption {

    DEVASTATOR((byte)1,UnitType.DEVASTATOR),
    SIEGE_TANK((byte)2,UnitType.SIEGE_TANK),
    TANK((byte)3,UnitType.SIEGE_TANK),
    LAUNCHER((byte)4,UnitType.LAUNCHER),

    POWERPLANT((byte)5,BuildingType.POWERPLANT),
    HARVESTER((byte)6,UnitType.HARVESTER),
    RADAR((byte)7,BuildingType.RADAR),
    CONCRETE((byte)8, BuildingType.CONCRETE),
    JEEP((byte)9, UnitType.JEEP),
    DEVIATOR((byte)10, UnitType.DEVIATOR),
    REFINERY((byte)11, BuildingType.REFINERY),
    FACTORY((byte)12,BuildingType.FACTORY),
    ROCKET_TURRET((byte)13, BuildingType.ROCKET_TURRET),
    SILO((byte)14,BuildingType.SILO),
    REPAIRSHOP((byte)15,BuildingType.REPAIRSHOP),
    AIRBASE((byte)16,BuildingType.AIRBASE),
    WALL((byte)17, BuildingType.WALL),
    MCV((byte)18, UnitType.MCV),
    TURRET((byte)19, BuildingType.TURRET),
    SONIC_TANK((byte)20, UnitType.SONIC_TANK),
    TRIKE((byte)21, UnitType.TRIKE);


    static {
        Set<Byte> idsOnJs = new HashSet<Byte>();
        for (ConstructionOption buildingType : values()) {
            if (!idsOnJs.add(buildingType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id" + idsOnJs);
            }
        }
    }

    private final byte idOnJS;
    private final EntityType entityType;

    private ConstructionOption(byte idOnJS, EntityType entityType) {
        this.idOnJS = idOnJS;
        this.entityType = entityType;
    }

    public byte getIdOnJS() {
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
