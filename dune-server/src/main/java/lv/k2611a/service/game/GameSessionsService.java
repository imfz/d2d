package lv.k2611a.service.game;

import java.util.Set;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.Response;

public interface GameSessionsService {
    // returns connections from current game
    Set<ClientConnection> getCurrentGameConnections();
    // sends update to all current game`s connections
    void sendUpdate(Response response);
}
