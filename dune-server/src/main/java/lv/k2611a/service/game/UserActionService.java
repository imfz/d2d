package lv.k2611a.service.game;

import java.util.List;

import lv.k2611a.network.req.GameStateChanger;

public interface UserActionService {

    void registerAction(GameStateChanger gameStateChanger);

    List<GameStateChanger> drainActions();
}
