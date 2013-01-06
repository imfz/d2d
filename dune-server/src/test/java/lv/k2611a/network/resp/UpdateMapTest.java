package lv.k2611a.network.resp;

import lv.k2611a.domain.AbstractSerializationTest;
import lv.k2611a.domain.EntityFactory;
import lv.k2611a.network.MapDTOTest;

public class UpdateMapTest extends AbstractSerializationTest<UpdateMap> {
    @EntityFactory
    public UpdateMap create() {
        return createUpdateMap();
    }

    private UpdateMap createUpdateMap() {
        UpdateMap updateMap = new UpdateMap();
        updateMap.setMap(MapDTOTest.createMapDTO());
        return updateMap;
    }
}
