package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum ConstructionOption {

    // T1 Units
    TRIKE((byte)11, UnitType.TRIKE),
    TRIKE_2((byte)12, UnitType.TRIKE_2),
    QUAD((byte)13, UnitType.QUAD),
    INFANTRIES((byte)14, UnitType.INFANTRIES),
    ROCKET_TROOPERS((byte)15, UnitType.ROCKET_TROOPERS),
    // T2 Units
    HARVESTER((byte)21,UnitType.HARVESTER),
    BATTLE_TANK((byte)22,UnitType.BATTLE_TANK),
    // T3 Units
    MCV((byte)31, UnitType.MCV),
    SIEGE_TANK((byte)32,UnitType.SIEGE_TANK),
    LAUNCHER((byte)33,UnitType.LAUNCHER),
    // T4 Units
    DEVASTATOR((byte)41,UnitType.DEVASTATOR),
    DEVIATOR((byte)42, UnitType.DEVIATOR),
    SONIC_TANK((byte)43, UnitType.SONIC_TANK),
    // Core T1 buildings
    POWERPLANT((byte)101,BuildingType.POWERPLANT),
    REFINERY((byte)102, BuildingType.REFINERY),
    SILO((byte)103,BuildingType.SILO),
    RADAR((byte)104,BuildingType.RADAR),
    // Unit-producing buildings
    BARRACKS((byte)105,BuildingType.BARRACKS),
    LIGHT_FACTORY((byte)106,BuildingType.LIGHT_FACTORY),
    FACTORY((byte)107,BuildingType.FACTORY),
    REPAIRSHOP((byte)108,BuildingType.REPAIRSHOP),
    AIRBASE((byte)109,BuildingType.AIRBASE),
    // Other buildings
    CONCRETE((byte)110, BuildingType.CONCRETE),
    WALL((byte)111, BuildingType.WALL),
    TURRET((byte)112, BuildingType.TURRET),
    ROCKET_TURRET((byte)113, BuildingType.ROCKET_TURRET)
    ;

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
