package lv.k2611a.domain;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum BuildingType implements EntityType {

    // id width height hp ticksToBuild costPerTick electricity prerequisites
    CONSTRUCTIONYARD((byte) 1,2,2,3500,999,999, 0, null) {
        @Override
        public EnumSet<ConstructionOption> getConstructionOptions() {
            EnumSet<ConstructionOption> constructionOptions = EnumSet.of(
                    ConstructionOption.REFINERY,
                    ConstructionOption.POWERPLANT,
                    ConstructionOption.SILO,
                    ConstructionOption.RADAR,
                    ConstructionOption.BARRACKS,
                    ConstructionOption.LIGHT_FACTORY,
                    ConstructionOption.FACTORY
            );
            return constructionOptions;
        }
    },
    // Core T1 buildings
    POWERPLANT(       (byte) 2,2,2,1000, 50, 15,+100, null),
    REFINERY(         (byte) 3,3,2,1800, 90, 15, -25, new BuildingType[]{BuildingType.POWERPLANT}),
    SILO(             (byte) 4,2,2,1200, 60, 10,  -5, new BuildingType[]{BuildingType.POWERPLANT, BuildingType.REFINERY}),
    RADAR(            (byte) 5,2,2,1500, 75, 20, -15, new BuildingType[]{BuildingType.POWERPLANT, BuildingType.REFINERY}),
    // Unit-producing buildings
    BARRACKS(         (byte) 6,2,2,1500, 75, 10, -15, new BuildingType[]{BuildingType.POWERPLANT}) {
        @Override
        public EnumSet<ConstructionOption> getConstructionOptions() {
            EnumSet<ConstructionOption> constructionOptions = EnumSet.of(
                    ConstructionOption.INFANTRIES,
                    ConstructionOption.ROCKET_TROOPERS
            );
            return constructionOptions;
        }
    },
    LIGHT_FACTORY(    (byte) 7,3,2,2000,100, 15, -15, new BuildingType[]{BuildingType.POWERPLANT, BuildingType.REFINERY}) {
        @Override
        public EnumSet<ConstructionOption> getConstructionOptions() {
            EnumSet<ConstructionOption> constructionOptions = EnumSet.of(
                    ConstructionOption.TRIKE,
                    ConstructionOption.QUAD
            );
            return constructionOptions;
        }
    },
    FACTORY(          (byte) 8,3,2,2200,110, 25, -25, new BuildingType[]{BuildingType.POWERPLANT, BuildingType.RADAR}) {
        @Override
        public EnumSet<ConstructionOption> getConstructionOptions() {
            EnumSet<ConstructionOption> constructionOptions = EnumSet.of(
                    ConstructionOption.HARVESTER,
                    ConstructionOption.BATTLE_TANK,
                    ConstructionOption.SIEGE_TANK,
                    ConstructionOption.LAUNCHER
            );
            return constructionOptions;
        }
    },
    REPAIRSHOP(       (byte) 9,3,2,2000,100, 25,-15, new BuildingType[]{BuildingType.POWERPLANT, BuildingType.FACTORY}),
    AIRBASE(          (byte)10,2,2,2500,125, 30,-20, new BuildingType[]{BuildingType.POWERPLANT, BuildingType.REPAIRSHOP}),
    // Other buildings
    CONCRETE(         (byte)11,2,2, 200, 10, 10,  0, null),
    WALL(             (byte)12,1,1,1000, 20, 15,  0, null),
    TURRET(           (byte)13,1,1,1200, 60, 15, -5, new BuildingType[]{BuildingType.POWERPLANT, BuildingType.RADAR}),
    ROCKET_TURRET(    (byte)14,1,1,2000,100, 20,-20, new BuildingType[]{BuildingType.POWERPLANT, BuildingType.FACTORY});

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
