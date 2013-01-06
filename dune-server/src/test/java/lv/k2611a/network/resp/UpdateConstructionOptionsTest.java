package lv.k2611a.network.resp;

import lv.k2611a.domain.AbstractSerializationTest;
import lv.k2611a.domain.EntityFactory;
import lv.k2611a.network.OptionDTO;

public class UpdateConstructionOptionsTest extends AbstractSerializationTest<UpdateConstructionOptions> {
    @EntityFactory
    public UpdateConstructionOptions create() {
        UpdateConstructionOptions updateConstructionOptions = new UpdateConstructionOptions();
        updateConstructionOptions.setOptions(createOptionDTO());
        return updateConstructionOptions;
    }

    private OptionDTO[] createOptionDTO() {
        OptionDTO dto1 = new OptionDTO();
        OptionDTO dto2 = new OptionDTO();
        return new OptionDTO[]{dto1,dto2};
    }

}
