package lv.k2611a.service;

import java.util.List;

import lv.k2611a.network.req.GameStateChanger;
import lv.k2611a.network.req.UnitAction;

public interface UserActionService {

    void registerAction(GameStateChanger gameStateChanger);

    List<GameStateChanger> drainActions();
}
