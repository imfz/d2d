package lv.k2611a.service.global;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.Response;
import lv.k2611a.service.game.GameSessionsService;

@Service
@Scope("singleton")
public class GlobalSessionServiceImpl implements GlobalSessionService {

    @Autowired
    private GameSessionsService gameSessionsService;

    private final Set<ClientConnection> members = new CopyOnWriteArraySet<ClientConnection>();

    @Override
    public boolean add(ClientConnection clientConnection) {
        gameSessionsService.add(clientConnection);
        return members.add(clientConnection);
    }

    @Override
    public boolean remove(Object o) {
        gameSessionsService.remove(o);
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
