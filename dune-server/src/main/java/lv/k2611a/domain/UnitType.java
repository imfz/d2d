package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum UnitType implements EntityType {
    BATTLE_TANK((byte)1, 40, 100, 100, 5),
    SIEGE_TANK((byte)2, 60, 120, 120, 5),
    LAUNCHER((byte)3, 30, 50, 100, 5),
    DEVASTATOR((byte)4, 100, 200, 200, 5),
    HARVESTER((byte)5, 10, 200, 150, 5),
    JEEP((byte)6, 100, 200, 50, 5),
    TRIKE((byte)7, 100, 200, 20, 5),
    SONIC_TANK((byte)8, 100, 200, 40, 5),
    DEVIATOR((byte)9, 100, 200, 50, 5),
    MCV((byte)10, 100, 200, 100, 5)
    ;


    private static UnitType[] indexByJsId;

    static {
        int maxJsId = 0;
        Set<Byte> idsOnJs = new HashSet<Byte>();
        for (UnitType unitType : values()) {
            if (unitType.getIdOnJS() > maxJsId) {
                maxJsId = unitType.getIdOnJS();
            }
            if (!idsOnJs.add(unitType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id");
            }
        }
        indexByJsId = new UnitType[maxJsId + 1];
        for (UnitType unitType : values()) {
            indexByJsId[unitType.getIdOnJS()] = unitType;
        }
    }

    private final byte idOnJS;
    private final int speed; // ticks for cell
    private final int hp;
    private final int ticksToBuild;
    private final int costPerTick;

    private UnitType(byte idOnJS, int speed, int hp, int ticksToBuild, int costPerTick) {
        this.idOnJS = idOnJS;
        this.speed = speed;
        this.hp = hp;
        this.ticksToBuild = ticksToBuild;
        this.costPerTick = costPerTick;
    }

    public byte getIdOnJS() {
        return idOnJS;
    }

    @Override
    public int getCost() {
        return costPerTick * ticksToBuild;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public BuildingType[] getPrerequisites() {
        return new BuildingType[0];
    }

    public int getSpeed() {
        return speed;
    }

    public int getHp() {
        return hp;
    }

    public int getTicksToBuild() {
        return ticksToBuild;
    }

    public int getCostPerTick() {
        return costPerTick;
    }


    public static UnitType getByJsId(int idInIs) {
        return indexByJsId[idInIs];
    }


}
