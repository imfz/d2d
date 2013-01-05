package lv.k2611a.service.scope;

import java.util.Collection;

public interface ContextService {
    GameContext getCurrentGameContext();

    void setSessionKey(GameKey value);

    void clearCurrentSessionKey();

    GameKey getCurrentContextKey();

    void clearContext(GameKey key);

    Collection<GameKey> getGameKeys();
}
