package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public enum UnitType implements EntityType {
    // id speed turnSpeed hp ticksToBuild costPerTick ticksToAttack attackRange attackDamage bulletSpeed bulletType
    // T1 Units
    TRIKE((byte) 1, 15, 4, 130, 40, 4, 15, 2.5, 10, 3, BulletType.TANK_SHOT, ArmorType.LIGHT, DamageType.SHELL),
    TRIKE_2((byte) 2, 12, 3, 150, 40, 4, 14, 2.5, 11, 3, BulletType.TANK_SHOT, ArmorType.LIGHT, DamageType.SHELL),
    QUAD((byte) 3, 25, 4, 180, 50, 5, 25, 3, 25, 3, BulletType.TANK_SHOT, ArmorType.LIGHT, DamageType.BULLET),
    INFANTRIES((byte) 4, 60, 1, 160, 40, 4, 15, 3, 16, 3, BulletType.TANK_SHOT, ArmorType.INFANTRY, DamageType.BULLET),
    ROCKET_TROOPERS((byte) 5, 60, 1, 180, 50, 5, 25, 4.3, 30, 3, BulletType.TANK_SHOT, ArmorType.INFANTRY, DamageType.SHELL),
    // T2 Units
    BATTLE_TANK((byte) 6, 30, 10, 310, 70, 7, 30, 4.3, 45, 4, BulletType.TANK_SHOT, ArmorType.HEAVY, DamageType.SHELL),
    // T3 Units
    SIEGE_TANK((byte) 7, 45, 15, 420, 90, 8, 40, 4.3, 80, 4, BulletType.TANK_SHOT, ArmorType.HEAVY, DamageType.SHELL),
    LAUNCHER((byte) 8, 30, 10, 160, 100, 10, 50, 7.2, 120, 3, BulletType.TANK_SHOT, ArmorType.LIGHT, DamageType.MISSILE),
    // T4 Units
    DEVASTATOR((byte) 9, 100, 1, 650, 150, 14, 40, 5, 120, 4, BulletType.TANK_SHOT, ArmorType.HEAVY, DamageType.SHELL),
    DEVIATOR((byte) 10, 100, 1, 200, 150, 14, 50, 7.2, 1, 3, BulletType.TANK_SHOT, ArmorType.LIGHT, DamageType.MISSILE),
    SONIC_TANK((byte) 11, 100, 1, 330, 150, 14, 45, 5.7, 80, 3, BulletType.TANK_SHOT, ArmorType.LIGHT, DamageType.BULLET),
    // Other Units
    HARVESTER((byte) 12, 15, 10, 200, 120, 10, 999, 1, 1, 1, BulletType.TANK_SHOT, ArmorType.HEAVY, DamageType.MISSILE),
    MCV((byte) 13, 100, 1, 200, 150, 14, 999, 1, 1, 1, BulletType.TANK_SHOT, ArmorType.HEAVY, DamageType.MISSILE),
    // Test dummies
    TESTING_DUMMY_SLOW((byte) 100, 50, 10, 1, 999, 999, 999, 1, 1, 10, BulletType.TANK_SHOT, ArmorType.LIGHT, DamageType.MISSILE),
    TESTING_DUMMY_FAST((byte) 101, 1, 1, 1, 999, 999, 999, 1, 1, 10, BulletType.TANK_SHOT, ArmorType.LIGHT, DamageType.MISSILE),
    TESTING_DUMMY_MINIGUN((byte) 102, 50, 50, 10000, 999, 999, 1, 10, 999, 3, BulletType.TANK_SHOT, ArmorType.LIGHT, DamageType.MISSILE);

    private static UnitType[] indexByJsId;
    public static double launcherMinimumAttackRange = 2.5;

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
    private final double attackRange;
    private final int attackDamage;
    private final int bulletSpeed; // ticks for bullet to fly per cell
    private final BulletType bulletType;
    private final int costEffectiveness;
    private final ArmorType armorType;
    private final DamageType damageType;

    private UnitType(byte idOnJS, int speed, int turnSpeed, int hp, int ticksToBuild, int costPerTick,
            int ticksToAttack, double attackRange, int attackDamage, int bulletSpeed,
            BulletType bulletType,
            ArmorType armorType,
            DamageType damageType
    ) {
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
        this.armorType = armorType;
        this.damageType = damageType;
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

    public double getAttackRange() {
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

    public ArmorType getArmorType() {
        return armorType;
    }

    public DamageType getDamageType() {
        return damageType;
    }
}
