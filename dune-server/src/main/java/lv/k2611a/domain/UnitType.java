package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum UnitType implements EntityType {
    BATTLE_TANK(1, 40, 100),
    SIEGE_TANK(2, 60, 120),
    LAUNCHER(3, 30, 50),
    DEVASTATOR(4, 100, 200),
    HARVESTER(5, 10, 200),
    JEEP(6, 100, 200),
    TRIKE(7, 100, 200),
    SONIC_TANK(8, 100, 200),
    DEVIATOR(9, 100, 200),
    MCV(10, 100, 200)
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

    private UnitType(int idOnJS, int speed, int hp) {
        this.idOnJS = idOnJS;
        this.speed = speed;
        this.hp = hp;
    }

    public int getIdOnJS() {
        return idOnJS;
    }

    @Override
    public int getCost() {
        return 0;
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


}
