package lv.k2611a.network;

import lv.k2611a.domain.AbstractSerializationTest;
import lv.k2611a.domain.EntityFactory;

public class ExplosionDTOTest extends AbstractSerializationTest<ExplosionDTO> {
    @EntityFactory
    public ExplosionDTO createExplosionDTO() {
        ExplosionDTO dto =  new ExplosionDTO();
        return dto;
    }

}
