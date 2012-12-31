package lv.k2611a.network.resp;

import lv.k2611a.domain.AbstractSeriaizationTest;
import lv.k2611a.domain.EntityFactory;
import lv.k2611a.network.BuildingDTO;
import lv.k2611a.network.BulletDTO;
import lv.k2611a.network.TileWithCoordinatesDTO;
import lv.k2611a.network.UnitDTO;

public class UpdateMapIncrementalTest extends AbstractSeriaizationTest<UpdateMapIncremental> {
    @EntityFactory
    public UpdateMapIncremental create() {
        UpdateMapIncremental updateMapIncremental = new UpdateMapIncremental();
        updateMapIncremental.setBuildings(createBuildings());
        updateMapIncremental.setChangedTiles(createChangedTiles());
        updateMapIncremental.setUnits(createUnits());
        updateMapIncremental.setBullets(createBullets());
        return updateMapIncremental;
    }

    private UnitDTO[] createUnits() {
        UnitDTO dto1 = new UnitDTO();
        UnitDTO dto2 = new UnitDTO();
        return new UnitDTO[]{dto1,dto2};
    }

    private TileWithCoordinatesDTO[] createChangedTiles() {
        TileWithCoordinatesDTO dto1 = new TileWithCoordinatesDTO();
        TileWithCoordinatesDTO dto2 = new TileWithCoordinatesDTO();
        return new TileWithCoordinatesDTO[]{dto1,dto2};
    }

    private BuildingDTO[] createBuildings() {
        BuildingDTO dto1 = new BuildingDTO();
        BuildingDTO dto2 = new BuildingDTO();
        return new BuildingDTO[]{dto1,dto2};
    }

    private static BulletDTO[] createBullets() {
        BulletDTO dto1 = new BulletDTO();
        BulletDTO dto2 = new BulletDTO();
        return new BulletDTO[]{dto1,dto2};
    }
}
