package lv.k2611a.network;

import lv.k2611a.domain.AbstractSerializationTest;
import lv.k2611a.domain.EntityFactory;

public class UnitDTOTest extends AbstractSerializationTest<UnitDTO> {
    @EntityFactory
    public UnitDTO create() {
        UnitDTO dto = new UnitDTO();
        return dto;
    }
}
