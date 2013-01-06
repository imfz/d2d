package lv.k2611a.service.connection;

import lv.k2611a.ClientConnection;
import lv.k2611a.service.scope.GameKey;

public interface ConnectionState {
    String getUsername();

    void setUsername(String username);

    Integer getPlayerId();

    void setPlayerId(Integer playerId);

    GameKey getGameKey();

    void setGameKey(GameKey gameKey);

    Integer getSelectedBuildingId();

    void setSelectedBuildingId(Integer selectedBuildingId);

    ClientConnection getConnection();

    void setConnection(ClientConnection connection);
}
