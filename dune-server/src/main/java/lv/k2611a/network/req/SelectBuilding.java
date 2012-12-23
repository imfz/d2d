package lv.k2611a.network.req;

import lv.k2611a.ClientConnection;

public class SelectBuilding implements Request {

    private int selectedId;

    @Override
    public void process() {
        ClientConnection.getCurrentConnection().setSelectedBuildingId(selectedId);
    }
}
