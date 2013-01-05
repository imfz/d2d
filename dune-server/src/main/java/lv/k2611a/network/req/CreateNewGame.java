package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.domain.lobby.Game;
import lv.k2611a.network.GameDTO;
import lv.k2611a.network.resp.GameLobbyUpdate;
import lv.k2611a.service.game.GameSessionsService;
import lv.k2611a.service.lobby.LobbyService;
import lv.k2611a.service.scope.ContextService;
import lv.k2611a.service.scope.GameKey;

public class CreateNewGame implements Request {

    private static final Logger log = LoggerFactory.getLogger(CreateNewGame.class);

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private GameSessionsService gameSessionsService;

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
        String username = ClientConnection.getCurrentConnection().getUsername();
        game.setCreator(username);
        game.getObservers().add(username);
        lobbyService.addGame(game);


        int id = game.getId();
        GameKey gameKey = new GameKey(id);
        ClientConnection.getCurrentConnection().setGameKey(gameKey);
        contextService.setSessionKey(gameKey);

        gameSessionsService.add(ClientConnection.getCurrentConnection());

        GameLobbyUpdate update = new GameLobbyUpdate();
        update.setGameDTO(GameDTO.fromGame(game));
        gameSessionsService.sendUpdate(update);

        log.info("Player with username " + username + " has created the game " + id);

    }
}
