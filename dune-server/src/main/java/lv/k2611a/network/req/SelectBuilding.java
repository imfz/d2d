package lv.k2611a.network.req;

import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.service.GameService;

public class SelectBuilding implements Request {

    @Autowired
    private GameService gameService;

    private int selectedId;

    @Override
    public void process() {
        if (!gameService.isOwner(selectedId, ClientConnection.getCurrentConnection().getPlayerId())) {
            // cannot select enemy building
            return;
        }

        ClientConnection.getCurrentConnection().setSelectedBuildingId(selectedId);
    }
}
