package lv.k2611a.domain;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class BuildingTypeTest {
    @Test
    public void getByJsIdReturnsSameInstance() {
        for (BuildingType buildingType : BuildingType.values()) {
            assertSame(buildingType, BuildingType.getByJsId(buildingType.getIdOnJS()));
        }
    }
}
