package lv.k2611a.network;

import lv.k2611a.domain.AbstractSerializationTest;
import lv.k2611a.domain.EntityFactory;

public class TileDTOTest extends AbstractSerializationTest<TileDTO> {
    @EntityFactory
    public TileDTO create() {
        TileDTO dto = new TileDTO();
        return dto;
    }
}
