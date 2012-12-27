package lv.k2611a.service.scope;

public interface ContextService {
    GameContext getCurrentGameContext();

    void setSessionKey(GameKey value);

    void clearCurrentSessionKey();

    GameKey getCurrentContextKey();
}
