package lv.k2611a.service.scope;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

@Service(value = "contextService")
@Scope("singleton")
public class ContextServiceImpl implements ContextService {

    private static final Logger LOG = LoggerFactory.getLogger(ContextServiceImpl.class);

    private ThreadLocal<GameKey> currentSessionKey = new ThreadLocal<GameKey>();

    private ConcurrentHashMap<GameKey, GameContext> games = new ConcurrentHashMap<GameKey, GameContext>();

    @Override
    public synchronized GameContext getCurrentGameContext() {
        if (currentSessionKey.get() == null) {
            throw new AssertionError("Game expected, but not found");
        }
        GameContext gameContext = games.get(currentSessionKey.get());
        if (gameContext == null) {
            gameContext = new GameContext();
            games.put(currentSessionKey.get(), gameContext);
        }
        return gameContext;
    }

    @Override
    public void setSessionKey(GameKey value) {
        currentSessionKey.set(value);
    }

    @Override
    public GameKey getCurrentContextKey() {
        return currentSessionKey.get();
    }

    @Override
    public void clearCurrentSessionKey() {
        currentSessionKey.set(null);
    }


}