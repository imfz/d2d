package lv.k2611a.testContext;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Service;

import lv.k2611a.service.scope.ContextService;
import lv.k2611a.service.scope.GameContext;
import lv.k2611a.service.scope.GameKey;

@Service
public class ContextServiceTestImpl implements ContextService {

    private GameContext gameContext;
    private GameKey gameKey = new GameKey(1);

    @Override
    public GameContext getCurrentGameContext() {
        return gameContext;
    }

    @Override
    public void setSessionKey(GameKey value) {
    }

    @Override
    public void clearCurrentSessionKey() {
    }

    @Override
    public GameKey getCurrentContextKey() {
        return gameKey;
    }

    @Override
    public void clearContext(GameKey key) {

    }

    @Override
    public Collection<GameKey> getGameKeys() {
        return new ArrayList<GameKey>();
    }
}
