package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum UnitType implements EntityType {
    // id speed turnSpeed hp ticksToBuild costPerTick ticksToAttack attackRange attackDamage bulletSpeed bulletType
    BATTLE_TANK((byte)1, 30, 10, 100, 10, 5, 20, 5, 5, 3, BulletType.TANK_SHOT),
    SIEGE_TANK((byte)2, 60, 1, 120, 120, 5, 1, 5, 5, 3, BulletType.TANK_SHOT),
    LAUNCHER((byte)3, 30, 1, 50, 100, 5, 1, 5, 5, 3, BulletType.TANK_SHOT),
    DEVASTATOR((byte)4, 100, 1, 200, 200, 5, 1, 5, 5, 3, BulletType.TANK_SHOT),
    HARVESTER((byte)5, 10, 3, 200, 150, 5, 1, 5, 5, 3, BulletType.TANK_SHOT),
    JEEP((byte)6, 100, 1, 200, 50, 5, 1, 5, 5, 3, BulletType.TANK_SHOT),
    TRIKE((byte)7, 100, 1, 200, 20, 5, 1, 5, 5, 3, BulletType.TANK_SHOT),
    SONIC_TANK((byte)8, 100, 1, 200, 40, 5, 20, 5, 5, 3, BulletType.TANK_SHOT),
    DEVIATOR((byte)9, 100, 1, 200, 50, 5, 20, 5, 5, 3, BulletType.TANK_SHOT),
    MCV((byte)10, 100, 1, 200, 100, 5, 20, 5, 5, 3, BulletType.TANK_SHOT),
    TESTING_DUMMY_SLOW((byte)100, 50, 10, 1, 999, 999, 999, 1, 1, 10, BulletType.TANK_SHOT),
    TESTING_DUMMY_FAST((byte)101, 1, 1, 1, 999, 999, 999, 1, 1, 10, BulletType.TANK_SHOT),
    TESTING_DUMMY_MINIGUN((byte)102, 50, 50, 10000, 999, 999, 1, 10, 999, 3, BulletType.TANK_SHOT)
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
    private final int turnSpeed; // ticks per 45 degree turn
    private final int hp;
    private final int ticksToBuild;
    private final int costPerTick;
    private final int ticksToAttack;
    private final int attackRange;
    private final int attackDamage;
    private final int bulletSpeed; // ticks for bullet to fly per cell
    private final BulletType bulletType;
    private final int costEffectiveness;

    private UnitType(byte idOnJS, int speed, int turnSpeed, int hp, int ticksToBuild, int costPerTick,
                     int ticksToAttack, int attackRange, int attackDamage, int bulletSpeed, BulletType bulletType) {
        this.idOnJS = idOnJS;
        this.speed = speed;
        this.turnSpeed = turnSpeed;
        this.hp = hp;
        this.ticksToBuild = ticksToBuild;
        this.costPerTick = costPerTick;
        this.ticksToAttack = ticksToAttack;
        this.attackRange = attackRange;
        this.attackDamage = attackDamage;
        this.bulletSpeed = bulletSpeed;
        this.bulletType = bulletType;
        this.costEffectiveness = hp / ticksToBuild;
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

    public int getTurnSpeed() {
        return turnSpeed;
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

    public int getTicksToAttack() {
        return ticksToAttack;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getCostEffectiveness() {
        return costEffectiveness;
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public BulletType getBulletType() {
        return bulletType;
    }
}
