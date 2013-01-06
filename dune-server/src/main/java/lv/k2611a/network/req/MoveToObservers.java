package lv.k2611a.network.req;

import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.network.GameDTO;
import lv.k2611a.network.resp.GameLobbyUpdate;
import lv.k2611a.service.game.GameSessionsService;
import lv.k2611a.service.global.LobbyService;

public class MoveToObservers implements Request {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Autowired
    private GameSessionsService gameSessionsService;

    @Autowired
    private LobbyService lobbyService;

    @Override
    public void process() {
        lobbyService.movePlayerToObservers(username);
        GameLobbyUpdate update = new GameLobbyUpdate();
        update.setGameDTO(GameDTO.fromGame(lobbyService.getCurrentGame()));
        gameSessionsService.sendUpdate(update);
    }
}
