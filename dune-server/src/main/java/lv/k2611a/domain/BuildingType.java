package lv.k2611a.domain;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum BuildingType implements EntityType {

    SILO(1,2,2,50,50, 10, -5),
    POWERPLANT(2,2,2,50,100, 10, +100),
    AIRBASE(9,2,2,50,100, 10, -20),
    WALL(14,1,1,50,100, 10, 0),
    REPAIRSHOP(3,3,2,50,100, 10, -15),
    TURRET(10,1,1,50,100, 10, -5),
    ROCKET_TURRET(13,1,1,50,100, 10, -20),
    RADAR(5,2,2,50,100, 10, -15),
    CONCRETE(11,2,2,50,100, 10, 0),
    REFINERY(12,3,2,50,30, 10, -25),
    FACTORY(8,3,2,50,100, 10, -25),
    CONSTRUCTIONYARD(7,2,2,50,150, 10, 0) {
        @Override
        public EnumSet<ConstructionOption> getConstructionOptions() {
            EnumSet<ConstructionOption> constructionOptions = EnumSet.of(
                    ConstructionOption.REFINERY,
                    ConstructionOption.POWERPLANT,
                    ConstructionOption.SILO
            );
            return constructionOptions;
        }
    };

    static {
        Set<Integer> idsOnJs = new HashSet<Integer>();
        for (BuildingType buildingType : values()) {
            if (!idsOnJs.add(buildingType.getIdOnJS())) {
                throw new AssertionError("Duplicate js id");
            }
        }
    }

    private final int idOnJS;
    private final int width;
    private final int height;
    private final int hp;
    private final int ticksToBuild;
    private int costPerTick;
    private final int electricityDelta;

    private BuildingType(int idOnJS, int width, int height, int hp, int ticksToBuild, int costPerTick, int electricityDelta) {
        this.idOnJS = idOnJS;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.ticksToBuild = ticksToBuild;
        this.costPerTick = costPerTick;
        this.electricityDelta = electricityDelta;
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
        for (BuildingType buildingType : values()) {
            if (buildingType.getIdOnJS() == idInIs) {
                return buildingType;
            }
        }
        throw new AssertionError("Unknown building type id in js : " + idInIs);
    }
}
