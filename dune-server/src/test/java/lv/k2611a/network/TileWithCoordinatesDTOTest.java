package lv.k2611a.network;

import lv.k2611a.domain.AbstractSeriaizationTest;
import lv.k2611a.domain.EntityFactory;

public class TileWithCoordinatesDTOTest extends AbstractSeriaizationTest<TileWithCoordinatesDTO> {
    @EntityFactory
    public TileWithCoordinatesDTO create() {
        TileWithCoordinatesDTO dto = new TileWithCoordinatesDTO();
        return dto;
    }
}
