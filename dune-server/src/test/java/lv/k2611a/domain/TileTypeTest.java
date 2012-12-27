package lv.k2611a.domain;

import org.junit.Test;

import static junit.framework.Assert.assertSame;

public class TileTypeTest {
    @Test
    public void getByJsIdReturnsSameInstance() {
        for (TileType tileType : TileType.values()) {
            assertSame(tileType, TileType.getByJsId(tileType.getIdOnJS()));
        }
    }
}
