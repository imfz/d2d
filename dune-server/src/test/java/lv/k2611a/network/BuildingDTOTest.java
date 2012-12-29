package lv.k2611a.network;

import lv.k2611a.domain.AbstractSeriaizationTest;
import lv.k2611a.domain.EntityFactory;

public class BuildingDTOTest extends AbstractSeriaizationTest<BuildingDTO> {
    @EntityFactory
    public BuildingDTO create() {
        BuildingDTO dto = new BuildingDTO();
        return dto;
    }
}
