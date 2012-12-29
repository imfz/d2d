package lv.k2611a.network;

import lv.k2611a.domain.AbstractSeriaizationTest;
import lv.k2611a.domain.EntityFactory;

public class OptionDTOTest extends AbstractSeriaizationTest<OptionDTO> {
    @EntityFactory
    public OptionDTO create() {
        OptionDTO dto = new OptionDTO();
        return dto;
    }
}
