package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.domain.lobby.Game;
import lv.k2611a.network.resp.UpdateMap;
import lv.k2611a.network.resp.UpdatePlayerId;
import lv.k2611a.service.game.GameService;
import lv.k2611a.service.game.GameSessionsService;
import lv.k2611a.service.lobby.LobbyService;
import lv.k2611a.util.MapGenerator;

public class StartGame implements Request {

    private static final Logger log = LoggerFactory.getLogger(StartGame.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private GameSessionsService gameSessionsService;

    @Autowired
    private LobbyService lobbyService;

    @Override
    public void process() {
        lobbyService.setCurrentGameStarted();
        Game currentGame = lobbyService.getCurrentGame();
        gameService.start(MapGenerator.generateMap(currentGame.getWidth(), currentGame.getHeight(), currentGame.getPlayers().size()));

        UpdateMap fullMapUpdate = gameService.getFullMapUpdate();

        log.info("Starting game");

        int playerId = 0;
        for (ClientConnection clientConnection : gameSessionsService.getMembers()) {
            UpdatePlayerId updatePlayerId = new UpdatePlayerId();
            if (currentGame.getPlayers().contains(clientConnection.getUsername())) {
                clientConnection.setPlayerId(playerId);
                log.info("Player " + clientConnection.getUsername() + " has joined as " + playerId);
                updatePlayerId.setPlayerId(playerId);
                playerId++;
            } else {
                clientConnection.setPlayerId(null);
                updatePlayerId.setPlayerId(-1);
                log.info("Player " + clientConnection.getUsername() + " has joined as an observer");
            }
            clientConnection.sendMessage(updatePlayerId);
            clientConnection.sendMessage(fullMapUpdate);
        }
    }
}
