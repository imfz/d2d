package lv.k2611a.testContext;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.Response;
import lv.k2611a.service.game.GameSessionsService;

@Service
public class SessionsServiceTestImpl implements GameSessionsService {
    @Override
    public Set<ClientConnection> getCurrentGameConnections() {
        return new HashSet<ClientConnection>();
    }

    @Override
    public void sendUpdate(Response response) {
    }

}
