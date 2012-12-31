package lv.k2611a.network;

import lv.k2611a.domain.AbstractSeriaizationTest;
import lv.k2611a.domain.EntityFactory;

public class BulletDTOTest extends AbstractSeriaizationTest<BulletDTO> {
    @EntityFactory
    public BulletDTO create() {
        BulletDTO dto = new BulletDTO();
        return dto;
    }
}
