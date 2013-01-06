package lv.k2611a.network.req;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.domain.lobby.Game;
import lv.k2611a.network.resp.UpdateMap;
import lv.k2611a.network.resp.UpdatePlayerId;
import lv.k2611a.service.connection.ConnectionState;
import lv.k2611a.service.game.GameService;
import lv.k2611a.service.game.GameSessionsService;
import lv.k2611a.service.global.LobbyService;
import lv.k2611a.util.MapGenerator;

public class StartGame implements Request {

    private static final Logger log = LoggerFactory.getLogger(StartGame.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private GameSessionsService gameSessionsService;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private ConnectionState connectionState;

    @Override
    public void process() {
        lobbyService.setCurrentGameStarted();
        final Game currentGame = lobbyService.getCurrentGame();
        gameService.start(MapGenerator.generateMap(currentGame.getWidth(), currentGame.getHeight(), currentGame.getPlayers().size()));

        final UpdateMap fullMapUpdate = gameService.getFullMapUpdate();

        log.info("Starting game");

        final AtomicInteger playerId = new AtomicInteger(0);
        for (final ClientConnection clientConnection : gameSessionsService.getCurrentGameConnections()) {
            clientConnection.processInConnectionsContext(new Runnable() {
                @Override
                public void run() {
                    UpdatePlayerId updatePlayerId = new UpdatePlayerId();
                    if (currentGame.getPlayers().contains(connectionState.getUsername())) {
                        connectionState.setPlayerId(playerId.get());
                        log.info("Player " + connectionState.getUsername() + " has joined as " + playerId);
                        updatePlayerId.setPlayerId(playerId.get());
                        playerId.incrementAndGet();
                    } else {
                        connectionState.setPlayerId(null);
                        updatePlayerId.setPlayerId(-1);
                        log.info("Player " + connectionState.getUsername() + " has joined as an observer");
                    }
                    clientConnection.sendMessage(updatePlayerId);
                    clientConnection.sendMessage(fullMapUpdate);
                }
            });

        }
    }
}
