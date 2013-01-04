package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.GameFull;
import lv.k2611a.network.resp.Joined;
import lv.k2611a.network.resp.UpdateMap;
import lv.k2611a.service.game.GameService;
import lv.k2611a.service.game.GameSessionsService;
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

    @Override
    public void process() {

        GameKey gameKey = new GameKey(id);
        ClientConnection.getCurrentConnection().setGameKey(gameKey);
        contextService.setSessionKey(gameKey);

        Integer playerId = mapService.getFreePlayer();
        if (playerId == null) {
            // no free slots left
            ClientConnection.getCurrentConnection().setGameKey(null);
            GameFull gameFull = new GameFull();
            ClientConnection.getCurrentConnection().sendMessage(gameFull);
            return;
        }


        ClientConnection.getCurrentConnection().setPlayerId(playerId);

        Joined joined = new Joined();
        String username = ClientConnection.getCurrentConnection().getUsername();
        joined.setNickname(username);

        sessionsService.sendUpdate(joined);

        // add user to current game
        sessionsService.add(ClientConnection.getCurrentConnection());

        // receive full map sync
        UpdateMap updateMap = mapService.getFullMapUpdate();
        updateMap.setPlayerId(playerId);
        ClientConnection.getCurrentConnection().sendMessage(updateMap);

        log.info("Player with username " + username + " has joined the game " + id);
    }
}
