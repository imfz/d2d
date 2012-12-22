package lv.k2611a.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lv.k2611a.network.req.GameStateChanger;

@Service
@Scope("singleton")
public class UserActionServiceImpl implements UserActionService {

    private List<GameStateChanger> actions = new ArrayList<GameStateChanger>();

    @Override
    public synchronized void registerAction(GameStateChanger gameStateChanger) {
        this.actions.add(gameStateChanger);
    }

    @Override
    public synchronized List<GameStateChanger> drainActions() {
        List<GameStateChanger> actions = new ArrayList<GameStateChanger>(this.actions);
        this.actions.clear();
        return actions;
    }
}
