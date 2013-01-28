package lv.k2611a.domain;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum BuildingType implements EntityType {

    POWERPLANT((byte)2,2,2,50,10, 10, +100, null),
    SILO((byte)1,2,2,50,50, 10, -5, new BuildingType[]{BuildingType.POWERPLANT}),
    AIRBASE((byte)9,2,2,50,100, 10, -20, new BuildingType[]{BuildingType.POWERPLANT}),
    WALL((byte)14,1,1,50,100, 10, 0, new BuildingType[]{BuildingType.POWERPLANT}),
    REPAIRSHOP((byte)3,3,2,50,100, 10, -15, new BuildingType[]{BuildingType.POWERPLANT}),
    TURRET((byte)10,1,1,50,100, 10, -5, new BuildingType[]{BuildingType.POWERPLANT}),
    ROCKET_TURRET((byte)13,1,1,50,100, 10, -20, new BuildingType[]{BuildingType.POWERPLANT}),
    RADAR((byte)5,2,2,50,100, 10, -15, new BuildingType[]{BuildingType.POWERPLANT}),
    CONCRETE((byte)11,2,2,50,100, 10, 0, new BuildingType[]{BuildingType.POWERPLANT}),
    REFINERY((byte)12,3,2,50,30, 10, -25, new BuildingType[]{BuildingType.POWERPLANT}),
    FACTORY((byte)8,3,2,50,10, 10, -25, new BuildingType[]{BuildingType.POWERPLANT}) {
        @Override
        public EnumSet<ConstructionOption> getConstructionOptions() {
            EnumSet<ConstructionOption> constructionOptions = EnumSet.of(
                    ConstructionOption.TANK
            );
            return constructionOptions;
        }
    },
    CONSTRUCTIONYARD((byte)7,2,2,50,150, 10, 0, null) {
        @Override
        public EnumSet<ConstructionOption> getConstructionOptions() {
            EnumSet<ConstructionOption> constructionOptions = EnumSet.of(
                    ConstructionOption.REFINERY,
                    ConstructionOption.POWERPLANT,
                    ConstructionOption.SILO,
                    ConstructionOption.FACTORY
            );
            return constructionOptions;
        }
    };

    private static BuildingType[] indexByJsId;

    static {
        int maxJsId = 0;
        Set<Byte> idsOnJs = new HashSet<Byte>();
        for (BuildingType buildingType : values()) {
            if (buildingType.getIdOnJS() > maxJsId) {
                maxJsId = buildingType.getIdOnJS();
            }
            if (!idsOnJs.add(buildingType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id");
            }
        }
        indexByJsId = new BuildingType[maxJsId + 1];
        for (BuildingType buildingType : values()) {
            indexByJsId[buildingType.getIdOnJS()] = buildingType;
        }
    }

    private final byte idOnJS;
    private final int width;
    private final int height;
    private final int hp;
    private final int ticksToBuild;
    private int costPerTick;
    private final int electricityDelta;
    private final BuildingType[] prerequisites;
    private final int costEffectiveness;

    private BuildingType(byte idOnJS, int width, int height, int hp, int ticksToBuild, int costPerTick, int electricityDelta, BuildingType[] prerequisites) {
        this.idOnJS = idOnJS;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.ticksToBuild = ticksToBuild;
        this.costPerTick = costPerTick;
        this.electricityDelta = electricityDelta;
        if (prerequisites == null) {
            this.prerequisites = new BuildingType[0];
        } else {
            this.prerequisites = prerequisites;
        }
        this.costEffectiveness = hp / ticksToBuild;
    }

    @Override
    public byte getIdOnJS() {
        return idOnJS;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getElectricityDelta() {
        return electricityDelta;
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

    @Override
    public BuildingType[] getPrerequisites() {
        return prerequisites;
    }

    @Override
    public int getCost() {
        return costPerTick * ticksToBuild;
    }

    public int getCostEffectiveness() {
        return costEffectiveness;
    }

    public void setCostPerTick(int costPerTick) {
        this.costPerTick = costPerTick;
    }

    public EnumSet<ConstructionOption> getConstructionOptions() {
        return EnumSet.noneOf(ConstructionOption.class);
    }

    public static BuildingType getByJsId(int idInIs) {
        return indexByJsId[idInIs];
    }
}
