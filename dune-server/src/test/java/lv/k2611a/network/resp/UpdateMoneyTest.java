package lv.k2611a.network.resp;

import lv.k2611a.domain.AbstractSeriaizationTest;
import lv.k2611a.domain.EntityFactory;

public class UpdateMoneyTest extends AbstractSeriaizationTest<UpdateMoney> {

    @EntityFactory
    public UpdateMoney createUpdateMoney() {
        UpdateMoney updateMoney = new UpdateMoney();
        return updateMoney;
    }

}
