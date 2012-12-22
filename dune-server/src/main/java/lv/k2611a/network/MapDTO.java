package lv.k2611a.network;

public class MapDTO {
    private int height;
    private int width;
    private TileDTO[] tiles;
    private UnitDTO[] units;
    private BuildingDTO[] buildings;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public TileDTO[] getTiles() {
        return tiles;
    }

    public void setTiles(TileDTO[] tiles) {
        this.tiles = tiles;
    }

    public UnitDTO[] getUnits() {
        return units;
    }

    public void setUnits(UnitDTO[] units) {
        this.units = units;
    }

    public BuildingDTO[] getBuildings() {
        return buildings;
    }

    public void setBuildings(BuildingDTO[] buildings) {
        this.buildings = buildings;
    }
}
