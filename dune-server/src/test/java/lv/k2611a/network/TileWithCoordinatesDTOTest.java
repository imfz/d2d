package lv.k2611a.network;

import lv.k2611a.domain.AbstractSerializationTest;
import lv.k2611a.domain.EntityFactory;

public class TileWithCoordinatesDTOTest extends AbstractSerializationTest<TileWithCoordinatesDTO> {
    @EntityFactory
    public TileWithCoordinatesDTO create() {
        TileWithCoordinatesDTO dto = new TileWithCoordinatesDTO();
        return dto;
    }
}
