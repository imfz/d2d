package lv.k2611a.domain;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class BulletTypeTest {
    @Test
    public void getByJsIdReturnsSameInstance() {
        for (BulletType bulletType : BulletType.values()) {
            assertSame(bulletType, BulletType.getByJsId(bulletType.getIdOnJS()));
        }
    }
}
