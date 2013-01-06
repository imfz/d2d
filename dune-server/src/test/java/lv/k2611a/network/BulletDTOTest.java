package lv.k2611a.network;

import lv.k2611a.domain.AbstractSerializationTest;
import lv.k2611a.domain.EntityFactory;

public class BulletDTOTest extends AbstractSerializationTest<BulletDTO> {
    @EntityFactory
    public BulletDTO create() {
        BulletDTO dto = new BulletDTO();
        return dto;
    }
}
