package lv.k2611a.domain;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum BuildingType implements EntityType {

    POWERPLANT(2,2,2,50,100, 10, +100, null),
    SILO(1,2,2,50,50, 10, -5, new BuildingType[]{BuildingType.POWERPLANT}),
    AIRBASE(9,2,2,50,100, 10, -20, new BuildingType[]{BuildingType.POWERPLANT}),
    WALL(14,1,1,50,100, 10, 0, new BuildingType[]{BuildingType.POWERPLANT}),
    REPAIRSHOP(3,3,2,50,100, 10, -15, new BuildingType[]{BuildingType.POWERPLANT}),
    TURRET(10,1,1,50,100, 10, -5, new BuildingType[]{BuildingType.POWERPLANT}),
    ROCKET_TURRET(13,1,1,50,100, 10, -20, new BuildingType[]{BuildingType.POWERPLANT}),
    RADAR(5,2,2,50,100, 10, -15, new BuildingType[]{BuildingType.POWERPLANT}),
    CONCRETE(11,2,2,50,100, 10, 0, new BuildingType[]{BuildingType.POWERPLANT}),
    REFINERY(12,3,2,50,30, 10, -25, new BuildingType[]{BuildingType.POWERPLANT}),
    FACTORY(8,3,2,50,100, 10, -25, new BuildingType[]{BuildingType.POWERPLANT}) {
        @Override
        public EnumSet<ConstructionOption> getConstructionOptions() {
            EnumSet<ConstructionOption> constructionOptions = EnumSet.of(
                    ConstructionOption.TANK
            );
            return constructionOptions;
        }
    },
    CONSTRUCTIONYARD(7,2,2,50,150, 10, 0, null) {
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
        Set<Integer> idsOnJs = new HashSet<Integer>();
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

    private final int idOnJS;
    private final int width;
    private final int height;
    private final int hp;
    private final int ticksToBuild;
    private int costPerTick;
    private final int electricityDelta;
    private final BuildingType[] prerequisites;

    private BuildingType(int idOnJS, int width, int height, int hp, int ticksToBuild, int costPerTick, int electricityDelta, BuildingType[] prerequisites) {
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
    }

    @Override
    public int getIdOnJS() {
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
