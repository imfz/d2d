package lv.k2611a.network.req;

import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.service.connection.ConnectionState;
import lv.k2611a.service.game.GameService;

public class SelectBuilding implements Request {

    @Autowired
    private GameService gameService;

    @Autowired
    private ConnectionState connectionState;

    private int selectedId;

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    @Override
    public void process() {
        Integer playerId = connectionState.getPlayerId();
        if (playerId == null) {
            return;
        }
        if (selectedId == -1) {
            // unselect building
            connectionState.setSelectedBuildingId(null);
            return;
        }
        if (!gameService.isOwner(selectedId, playerId)) {
            // cannot select enemy building
            return;
        }

        connectionState.setSelectedBuildingId(selectedId);
    }
}
