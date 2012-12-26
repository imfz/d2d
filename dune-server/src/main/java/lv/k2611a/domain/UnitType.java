package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum UnitType implements EntityType {
    BATTLE_TANK(1, 40, 100, 100, 5),
    SIEGE_TANK(2, 60, 120, 120, 5),
    LAUNCHER(3, 30, 50, 100, 5),
    DEVASTATOR(4, 100, 200, 200, 5),
    HARVESTER(5, 10, 200, 150, 5),
    JEEP(6, 100, 200, 50, 5),
    TRIKE(7, 100, 200, 20, 5),
    SONIC_TANK(8, 100, 200, 40, 5),
    DEVIATOR(9, 100, 200, 50, 5),
    MCV(10, 100, 200, 100, 5)
    ;

    static {
        Set<Integer> idsOnJs = new HashSet<Integer>();
        for (UnitType unitType : values()) {
            if (!idsOnJs.add(unitType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id");
            }
        }
    }

    private final int idOnJS;
    private final int speed; // ticks for cell
    private final int hp;
    private final int ticksToBuild;
    private final int costPerTick;

    private UnitType(int idOnJS, int speed, int hp, int ticksToBuild, int costPerTick) {
        this.idOnJS = idOnJS;
        this.speed = speed;
        this.hp = hp;
        this.ticksToBuild = ticksToBuild;
        this.costPerTick = costPerTick;
    }

    public int getIdOnJS() {
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
        for (UnitType unitType : values()) {
            if (unitType.getIdOnJS() == idInIs) {
                return unitType;
            }
        }
        throw new AssertionError("Unknown building type id in js : " + idInIs);
    }


}
