package lv.k2611a.domain;

import org.junit.Test;

import static junit.framework.Assert.assertSame;

public class UnitTypeTest {
    @Test
    public void getByJsIdReturnsSameInstance() {
        for (UnitType unitType : UnitType.values()) {
            assertSame(unitType, UnitType.getByJsId(unitType.getIdOnJS()));
        }
    }
}
