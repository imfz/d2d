package lv.k2611a.service.connection;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import lv.k2611a.ClientConnection;
import lv.k2611a.service.scope.GameKey;

@Service
@Scope(value= "connection", proxyMode = ScopedProxyMode.INTERFACES)
public class ConnectionStateImpl implements ConnectionState {
    private String username;

    // null if observer
    private Integer playerId;

    private GameKey gameKey;

    private Integer selectedBuildingId;

    private ClientConnection connection;

    @Override
    public synchronized String getUsername() {
        return username;
    }

    @Override
    public synchronized void setUsername(String username) {
        this.username = username;
    }

    @Override
    public synchronized Integer getPlayerId() {
        return playerId;
    }

    @Override
    public synchronized void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    @Override
    public synchronized GameKey getGameKey() {
        return gameKey;
    }

    @Override
    public synchronized void setGameKey(GameKey gameKey) {
        this.gameKey = gameKey;
    }

    @Override
    public synchronized Integer getSelectedBuildingId() {
        return selectedBuildingId;
    }

    @Override
    public synchronized void setSelectedBuildingId(Integer selectedBuildingId) {
        this.selectedBuildingId = selectedBuildingId;
    }

    @Override
    public synchronized ClientConnection getConnection() {
        return connection;
    }

    @Override
    public synchronized void setConnection(ClientConnection connection) {
        this.connection = connection;
    }
}
