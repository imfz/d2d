package lv.k2611a.service.game;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.Response;

@Service
@Scope(value = "game", proxyMode = ScopedProxyMode.INTERFACES)
public class GameSessionServiceImpl implements GameSessionsService {
    private final Set<ClientConnection> members = new CopyOnWriteArraySet<ClientConnection>();

    @Override
    public boolean add(ClientConnection clientConnection) {
        return members.add(clientConnection);
    }

    @Override
    public boolean remove(Object o) {
        return members.remove(o);
    }

    @Override
    public Set<ClientConnection> getMembers() {
        return members;
    }

    @Override
    public void sendUpdate(Response response) {
        for (ClientConnection member : members) {
            member.sendMessage(response);
        }
    }
}
