package lv.k2611a.service.global;

import java.util.Set;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.Response;

public interface GlobalSessionService {
    boolean add(ClientConnection clientConnection);

    boolean remove(ClientConnection o);

    Set<ClientConnection> getMembers();

    void sendUpdate(Response response);
}
