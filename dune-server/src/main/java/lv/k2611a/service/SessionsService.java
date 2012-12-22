package lv.k2611a.service;

import java.util.Set;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.Response;

public interface SessionsService {
    boolean add(ClientConnection clientConnection);

    boolean remove(Object o);

    Set<ClientConnection> getMembers();

    void sendUpdate(Response response);
}
