package lv.k2611a.service.scope;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service(value = "contextService")
@Scope("singleton")
public class ContextServiceImpl implements ContextService {

    private static final Logger LOG = LoggerFactory.getLogger(ContextServiceImpl.class);

    private ThreadLocal<GameKey> currentGameKey = new ThreadLocal<GameKey>();
    private ThreadLocal<ConnectionKey> currentConnectionKey = new ThreadLocal<ConnectionKey>();

    private ConcurrentHashMap<GameKey, Context> gameContexts = new ConcurrentHashMap<GameKey, Context>();
    private ConcurrentHashMap<ConnectionKey, Context> connectionContexts = new ConcurrentHashMap<ConnectionKey, Context>();

    @Override
    public synchronized Context getCurrentGameContext() {
        if (currentGameKey.get() == null) {
            return null;
        }
        Context gameContext = gameContexts.get(currentGameKey.get());
        if (gameContext == null) {
            gameContext = new Context();
            gameContexts.put(currentGameKey.get(), gameContext);
        }
        return gameContext;
    }

    @Override
    public synchronized Context getCurrentConnectionContext() {
        if (currentConnectionKey.get() == null) {
            return null;
        }
        Context connectionContext = connectionContexts.get(currentConnectionKey.get());
        if (connectionContext == null) {
            connectionContext = new Context();
            connectionContexts.put(currentConnectionKey.get(), connectionContext);
        }
        return connectionContext;
    }

    @Override
    public Collection<GameKey> getGameKeys() {
        return gameContexts.keySet();
    }

    @Override
    public Collection<Context> getConnectionContexts() {
        return connectionContexts.values();
    }

    @Override
    public void setGameKey(GameKey value) {
        currentGameKey.set(value);
    }

    @Override
    public GameKey getCurrentGameKey() {
        return currentGameKey.get();
    }

    @Override
    public void clearCurrentGameKey() {
        currentGameKey.set(null);
    }

    @Override
    public synchronized void clearGameKey(GameKey key) {
        Context gameContext = gameContexts.get(key);
        if (gameContext != null) {
            gameContext.clear();
        }
        gameContexts.remove(key);
    }


    @Override
    public void setConnectionKey(ConnectionKey value) {
        currentConnectionKey.set(value);
    }

    @Override
    public ConnectionKey getCurrentConnectionKey() {
        return currentConnectionKey.get();
    }

    @Override
    public void clearCurrentConnectionKey() {
        currentConnectionKey.set(null);
    }

    @Override
    public synchronized void clearConnectionKey(ConnectionKey key) {
        Context connectionContext = connectionContexts.get(key);
        if (connectionContext != null) {
            connectionContext.clear();
        }
        connectionContexts.remove(key);
    }


}