package lv.k2611a.service.scope;

import java.util.Collection;

public interface ContextService {
    Context getCurrentGameContext();

    void setGameKey(GameKey value);

    void clearCurrentGameKey();

    GameKey getCurrentGameKey();

    void clearGameKey(GameKey key);

    Collection<GameKey> getGameKeys();

    void setConnectionKey(ConnectionKey value);

    ConnectionKey getCurrentConnectionKey();

    void clearCurrentConnectionKey();

    void clearConnectionKey(ConnectionKey key);

    Context getCurrentConnectionContext();

    Collection<Context> getConnectionContexts();
}
