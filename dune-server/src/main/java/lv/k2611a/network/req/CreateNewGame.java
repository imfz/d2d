package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.domain.lobby.Game;
import lv.k2611a.service.lobby.LobbyService;

public class CreateNewGame implements Request {

    private static final Logger log = LoggerFactory.getLogger(CreateNewGame.class);

    @Autowired
    private LobbyService lobbyService;

    @Override
    public void process() {
        log.info("Creating new game");
        lobbyService.addGame(new Game());
    }
}
