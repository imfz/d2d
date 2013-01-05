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
    public boolean add(ClientConnection clientConnection) {
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return true;
    }

    @Override
    public Set<ClientConnection> getMembers() {
        return new HashSet<ClientConnection>();
    }

    @Override
    public void sendUpdate(Response response) {
    }

    @Override
    public void clear() {

    }
}
