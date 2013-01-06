package lv.k2611a.network;

import lv.k2611a.domain.AbstractSerializationTest;
import lv.k2611a.domain.EntityFactory;

public class OptionDTOTest extends AbstractSerializationTest<OptionDTO> {
    @EntityFactory
    public OptionDTO create() {
        OptionDTO dto = new OptionDTO();
        return dto;
    }
}
