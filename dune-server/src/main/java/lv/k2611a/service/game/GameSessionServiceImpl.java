package lv.k2611a.service.game;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.Response;
import lv.k2611a.service.connection.ConnectionState;
import lv.k2611a.service.global.GlobalSessionService;
import lv.k2611a.service.scope.ContextService;
import lv.k2611a.service.scope.GameKey;

@Service
@Scope("singleton")
public class GameSessionServiceImpl implements GameSessionsService {

    @Autowired
    private GlobalSessionService globalSessionService;

    @Autowired
    private ConnectionState connectionState;

    @Autowired
    private ContextService contextService;

    @Override
    public Set<ClientConnection> getCurrentGameConnections() {
        Set<ClientConnection> allConnections = globalSessionService.getMembers();
        final Set<ClientConnection> currentGameConnections = new HashSet<ClientConnection>();
        final GameKey currentGameKey = contextService.getCurrentGameKey();
        for (final ClientConnection connection : allConnections) {
            connection.processInConnectionsContext(new Runnable() {
                @Override
                public void run() {
                    GameKey gameKey = connectionState.getGameKey();
                    if (gameKey != null) {
                        if (gameKey.equals(currentGameKey)) {
                            currentGameConnections.add(connection);
                        }
                    }
                }
            });
        }
        return currentGameConnections;
    }

    @Override
    public void sendUpdate(Response response) {
        for (ClientConnection member : getCurrentGameConnections()) {
            member.sendMessage(response);
        }
    }
}
