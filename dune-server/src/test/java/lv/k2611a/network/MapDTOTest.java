package lv.k2611a.network;

import lv.k2611a.domain.AbstractSerializationTest;
import lv.k2611a.domain.EntityFactory;

public class MapDTOTest extends AbstractSerializationTest<MapDTO> {
    @EntityFactory
    public MapDTO create() {
        return createMapDTO();
    }

    public static MapDTO createMapDTO() {
        MapDTO mapDTO = new MapDTO();
        mapDTO.setBuildings(createBuildings());
        mapDTO.setTiles(createTiles());
        mapDTO.setUnits(createUnits());
        mapDTO.setBullets(createBullets());
        return mapDTO;
    }

    private static BulletDTO[] createBullets() {
        BulletDTO dto1 = new BulletDTO();
        BulletDTO dto2 = new BulletDTO();
        return new BulletDTO[]{dto1,dto2};
    }

    private static TileDTO[] createTiles() {
        TileDTO tileDTO = new TileDTO();
        TileDTO tileDTO2 = new TileDTO();
        return new TileDTO[]{tileDTO,tileDTO2};
    }

    private static UnitDTO[] createUnits() {
        UnitDTO dto1 = new UnitDTO();
        UnitDTO dto2 = new UnitDTO();
        return new UnitDTO[]{dto1,dto2};
    }

    private static TileWithCoordinatesDTO[] createChangedTiles() {
        TileWithCoordinatesDTO dto1 = new TileWithCoordinatesDTO();
        TileWithCoordinatesDTO dto2 = new TileWithCoordinatesDTO();
        return new TileWithCoordinatesDTO[]{dto1,dto2};
    }

    private static BuildingDTO[] createBuildings() {
        BuildingDTO dto1 = new BuildingDTO();
        BuildingDTO dto2 = new BuildingDTO();
        return new BuildingDTO[]{dto1,dto2};
    }
}
