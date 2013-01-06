package lv.k2611a.testContext;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Service;

import lv.k2611a.service.scope.ConnectionKey;
import lv.k2611a.service.scope.Context;
import lv.k2611a.service.scope.ContextService;
import lv.k2611a.service.scope.GameKey;

@Service
public class ContextServiceTestImpl implements ContextService {

    private Context gameContext;
    private GameKey gameKey = new GameKey(1);

    @Override
    public Context getCurrentGameContext() {
        return gameContext;
    }

    @Override
    public void setGameKey(GameKey value) {
    }

    @Override
    public void clearCurrentGameKey() {
    }

    @Override
    public GameKey getCurrentGameKey() {
        return gameKey;
    }

    @Override
    public void clearGameKey(GameKey key) {

    }

    @Override
    public Collection<GameKey> getGameKeys() {
        return new ArrayList<GameKey>();
    }

    @Override
    public void setConnectionKey(ConnectionKey value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ConnectionKey getCurrentConnectionKey() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearCurrentConnectionKey() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearConnectionKey(ConnectionKey key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Context getCurrentConnectionContext() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
