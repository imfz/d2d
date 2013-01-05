package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.domain.lobby.Game;
import lv.k2611a.service.lobby.LobbyService;

public class CreateNewGame implements Request {

    private static final Logger log = LoggerFactory.getLogger(CreateNewGame.class);

    @Autowired
    private LobbyService lobbyService;

    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void process() {
        log.info("Creating new game");
        Game game = new Game();
        game.setHeight(height);
        game.setWidth(width);
        game.setCreator(ClientConnection.getCurrentConnection().getUsername());
        lobbyService.addGame(game);
    }
}
