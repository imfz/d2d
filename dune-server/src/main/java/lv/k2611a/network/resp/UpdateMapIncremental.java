package lv.k2611a.network.resp;

import lv.k2611a.network.BuildingDTO;
import lv.k2611a.network.TileWithCoordinatesDTO;
import lv.k2611a.network.UnitDTO;

public class UpdateMapIncremental implements Response {
    private UnitDTO[] units;
    private BuildingDTO[] buildings;
    private TileWithCoordinatesDTO[] changedTiles;
    private long tickCount;

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

    public TileWithCoordinatesDTO[] getChangedTiles() {
        return changedTiles;
    }

    public void setChangedTiles(TileWithCoordinatesDTO[] changedTiles) {
        this.changedTiles = changedTiles;
    }

    public long getTickCount() {
        return tickCount;
    }

    public void setTickCount(long tickCount) {
        this.tickCount = tickCount;
    }
}
