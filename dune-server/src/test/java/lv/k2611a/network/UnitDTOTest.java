package lv.k2611a.network;

import lv.k2611a.domain.AbstractSeriaizationTest;
import lv.k2611a.domain.EntityFactory;

public class UnitDTOTest extends AbstractSeriaizationTest<UnitDTO> {
    @EntityFactory
    public UnitDTO create() {
        UnitDTO dto = new UnitDTO();
        return dto;
    }
}
