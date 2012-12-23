package lv.k2611a.network.req;

import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.service.UserActionService;

public abstract class AbstractGameStateChanger implements GameStateChanger, Request {

    @Autowired
    private UserActionService userActionService;

    protected int playerId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void process() {
        playerId = ClientConnection.getCurrentConnection().getPlayerId();
        userActionService.registerAction(this);
    }
}
