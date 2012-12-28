package lv.k2611a.network.req;

import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.service.game.GameService;

public class SelectBuilding implements Request {

    @Autowired
    private GameService gameService;

    private int selectedId;

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    @Override
    public void process() {
        if (selectedId == -1) {
            // unselect building
            ClientConnection.getCurrentConnection().setSelectedBuildingId(null);
            return;
        }
        if (!gameService.isOwner(selectedId, ClientConnection.getCurrentConnection().getPlayerId())) {
            // cannot select enemy building
            return;
        }

        ClientConnection.getCurrentConnection().setSelectedBuildingId(selectedId);
    }
}
