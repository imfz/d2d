package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.domain.lobby.Game;
import lv.k2611a.network.GameDTO;
import lv.k2611a.network.resp.AlreadyStarted;
import lv.k2611a.network.resp.GameLobbyUpdate;
import lv.k2611a.service.game.GameService;
import lv.k2611a.service.game.GameSessionsService;
import lv.k2611a.service.lobby.LobbyService;
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
    private GameSessionsService sessionsService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private LobbyService lobbyService;

    @Override
    public void process() {
        GameKey gameKey = new GameKey(id);
        ClientConnection.getCurrentConnection().setGameKey(gameKey);
        contextService.setSessionKey(gameKey);

        if (lobbyService.isCurrentGameStarted()) {
            ClientConnection.getCurrentConnection().sendMessage(new AlreadyStarted());
            return;
        }

        Game currentGame = lobbyService.getCurrentGame();
        lobbyService.addUserToCurrentGame(ClientConnection.getCurrentConnection().getUsername());

        // add user to current game
        sessionsService.add(ClientConnection.getCurrentConnection());

        GameLobbyUpdate update = new GameLobbyUpdate();
        update.setGameDTO(GameDTO.fromGame(currentGame));
        sessionsService.sendUpdate(update);

        log.info("Player with username " + ClientConnection.getCurrentConnection().getUsername() + " has joined the game " + id);
    }
}
