package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.domain.lobby.Game;
import lv.k2611a.network.GameDTO;
import lv.k2611a.network.resp.GameClosed;
import lv.k2611a.network.resp.GameLobbyUpdate;
import lv.k2611a.network.resp.Left;
import lv.k2611a.network.resp.LeftOk;
import lv.k2611a.service.game.GameSessionsService;
import lv.k2611a.service.lobby.LobbyService;
import lv.k2611a.service.scope.ContextService;

public class LeaveGame implements Request {

    private static final Logger log = LoggerFactory.getLogger(LeaveGame.class);

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private GameSessionsService sessionsService;

    @Autowired
    private ContextService contextService;

    @Override
    public void process() {

        if (lobbyService.getCurrentGame() == null) {
            return;
        }

        Left left = new Left();
        String username = ClientConnection.getCurrentConnection().getUsername();
        left.setNickname(username);

        sessionsService.sendUpdate(left);

        sessionsService.remove(ClientConnection.getCurrentConnection());

        lobbyService.removeUserFromCurrentGame(ClientConnection.getCurrentConnection().getUsername());
        ClientConnection.getCurrentConnection().setGameKey(null);
        ClientConnection.getCurrentConnection().sendMessage(new LeftOk());

        Game currentGame = lobbyService.getCurrentGame();

        GameLobbyUpdate update = new GameLobbyUpdate();
        update.setGameDTO(GameDTO.fromGame(currentGame));
        sessionsService.sendUpdate(update);

        log.info("Player with username " + username + " has left the game " + currentGame.getId());

        if (currentGame.getPlayers().contains(currentGame.getCreator())) {
            return;
        }
        sessionsService.sendUpdate(new GameClosed());
        log.info("Removing orhpan game " + currentGame.getId());
        for (ClientConnection clientConnection : sessionsService.getMembers()) {
            clientConnection.setGameKey(null);
        }
        sessionsService.clear();
        lobbyService.destroy(currentGame);
    }
}
