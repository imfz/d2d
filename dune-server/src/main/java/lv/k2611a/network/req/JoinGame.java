package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.domain.lobby.Game;
import lv.k2611a.network.GameDTO;
import lv.k2611a.network.resp.AlreadyStarted;
import lv.k2611a.network.resp.GameLobbyUpdate;
import lv.k2611a.service.connection.ConnectionState;
import lv.k2611a.service.game.GameService;
import lv.k2611a.service.game.GameSessionsService;
import lv.k2611a.service.global.LobbyService;
import lv.k2611a.service.scope.ContextService;
import lv.k2611a.service.scope.GameKey;

public class JoinGame implements Request {

    private static final Logger log = LoggerFactory.getLogger(JoinGame.class);

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Autowired
    private GameService mapService;

    @Autowired
    private GameSessionsService gameSessionsService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private ConnectionState connectionState;

    @Override
    public void process() {
        GameKey gameKey = new GameKey(id);
        connectionState.setGameKey(gameKey);
        contextService.setGameKey(gameKey);

        if (lobbyService.isCurrentGameStarted()) {
            connectionState.getConnection().sendMessage(new AlreadyStarted());
            return;
        }

        Game currentGame = lobbyService.getCurrentGame();
        lobbyService.addUserToCurrentGame(connectionState.getUsername());

        GameLobbyUpdate update = new GameLobbyUpdate();
        update.setGameDTO(GameDTO.fromGame(currentGame));
        gameSessionsService.sendUpdate(update);

        log.info("Player with username " + connectionState.getUsername() + " has joined the game " + id);
    }
}
